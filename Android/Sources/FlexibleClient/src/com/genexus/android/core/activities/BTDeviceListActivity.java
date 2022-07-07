package com.genexus.android.core.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genexus.android.R;
import com.genexus.android.core.adapters.RecyclerStringAdapter;
import com.genexus.android.core.utils.TaskRunner;

@SuppressLint("MissingPermission")
public class BTDeviceListActivity extends Activity {

    public static final int REQUEST_CONNECT_BT = 311;
	private static final int REQUEST_ENABLE_BT = 312;

	private static BluetoothAdapter mBluetoothAdapter = null;
	private static RecyclerStringAdapter mRecyclerAdapter = null;
	private static ArrayList<BluetoothDevice> btDevices = null;
    
	private static final UUID SPP_UUID = UUID
			.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	// UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private BluetoothSocket mbtSocket = null;
	private static BluetoothDevice btDevice = null;

	public static BluetoothDevice getBTDevice()
	{
		return btDevice;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.GXM_BluetoothDevices);
		setContentView(R.layout.btdevicelist);

		if (initDevicesList() != 0) {
			this.finish();
			return;
		}

		IntentFilter btIntentFilter = new IntentFilter( BluetoothDevice.ACTION_FOUND);
		registerReceiver(mBTReceiver, btIntentFilter);

		Button button = this.findViewById(R.id.buttoncancel);
		button.setOnClickListener(mDoCancel);
	}

	//Click event
	private final View.OnClickListener mDoCancel = new View.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			Intent resultIntent = new Intent();
			setResult(RESULT_CANCELED, resultIntent);
			finish();
		}
	};

	private void flushData() {
		try {
			if (mbtSocket != null) {
				mbtSocket.close();
				mbtSocket = null;
			}

			if (mBluetoothAdapter != null) {
				mBluetoothAdapter.cancelDiscovery();
			}

			if (btDevices != null) {
				btDevices.clear();
				btDevices = null;
			}

			if (mRecyclerAdapter != null) {
				mRecyclerAdapter.clear();
				mRecyclerAdapter = null;
			}
		} catch (IOException ignored) {
		}
	}

	private int initDevicesList() {
		flushData();

		final BluetoothManager bluetoothManager =
			(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),	"Bluetooth not supported!!", Toast.LENGTH_LONG).show();
			return -1;
		}

		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		mRecyclerAdapter = new RecyclerStringAdapter(this);
		mRecyclerAdapter.setOnClickListener(mOnListItemClick);
		RecyclerView recyclerView = findViewById(R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mRecyclerAdapter);

		Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE);
		try {
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} catch (ActivityNotFoundException ex) {
			return -2;
		}

		Toast.makeText(getApplicationContext(), "Getting all available Bluetooth Devices", Toast.LENGTH_SHORT).show();

		return 0;
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
		super.onActivityResult(reqCode, resultCode, intent);

		if (reqCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				Set<BluetoothDevice> btDeviceList = mBluetoothAdapter.getBondedDevices();
				if (btDeviceList.size() > 0) {

					for (BluetoothDevice device : btDeviceList) {
						if (!btDeviceList.contains(device)) {
							btDevices.add(device);
							mRecyclerAdapter.add(device.getName() + "\n" + device.getAddress());
						}
					}
				}
			}
		}

		mBluetoothAdapter.startDiscovery();
	}

    private boolean getIsPrinter(int major) {
		//AUDIO_VIDEO = 1024;
		//COMPUTER = 256;
		//HEALTH = 2304;
		//IMAGING = 1536;
		//MISC = 0;
		//NETWORKING = 768;
		//PERIPHERAL = 1280;
		//PHONE = 512;
		//TOY = 2048;
		//UNCATEGORIZED = 7936;
		//WEARABLE = 1792;
        return (major == BluetoothClass.Device.Major.COMPUTER ||
				major == BluetoothClass.Device.Major.NETWORKING ||
				major == BluetoothClass.Device.Major.PERIPHERAL ||
				major == BluetoothClass.Device.Major.UNCATEGORIZED ||
        		major == BluetoothClass.Device.Major.MISC);
    }

	private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (btDevices == null)
					btDevices = new ArrayList<>();

				boolean isPrinter = getIsPrinter(device.getBluetoothClass().getMajorDeviceClass());
				if (isPrinter && !btDevices.contains(device)) {
					btDevices.add(device);
					mRecyclerAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" );
				}
			}
		}
	};

	public static BluetoothSocket connectDevice(BluetoothDevice btDevice) throws IOException {
		UUID uuid = btDevice.getUuids()[0].getUuid();
		return btDevice.createRfcommSocketToServiceRecord(uuid);
	}

	private final RecyclerStringAdapter.OnClickListener mOnListItemClick = position -> {
		if (mBluetoothAdapter == null) {
			return;
		}

		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		Toast.makeText(getApplicationContext(), "Connecting to " + btDevices.get(position).getName() + "," + btDevices.get(position).getAddress(), Toast.LENGTH_SHORT).show();

		TaskRunner.execute(() -> {
			try {
				boolean gotUuid = btDevices.get(position).fetchUuidsWithSdp();
				if (!gotUuid || btDevices.get(position).getUuids() == null) {
					runOnUiThread(() -> // printer error
						Toast.makeText(getApplicationContext(), "Failed to connect to device", Toast.LENGTH_SHORT).show());
				}
				else {
					btDevice = btDevices.get(position);
					mbtSocket = connectDevice(btDevice);
					setResult(RESULT_OK);
				}
			} catch (IOException ex) {
				runOnUiThread(() -> { // socket error
					Toast.makeText(getApplicationContext(),	"Cannot establish connection", Toast.LENGTH_SHORT).show();
					mBluetoothAdapter.startDiscovery();
				});
				try {
					mbtSocket.close();
				} catch (IOException ignored) {
				}
				mbtSocket = null;
			} finally {
				runOnUiThread(this::finish);
			}
		});
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, Menu.FIRST, Menu.NONE, "Refresh Scanning");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == Menu.FIRST) {
			initDevicesList();
		}

		return true;
	}
}
