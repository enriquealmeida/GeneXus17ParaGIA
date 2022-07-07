package com.artech.activities;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.artech.R;


public class BTDeviceList extends ListActivity {

    public static final int REQUEST_CONNECT_BT = 311;

	private static final int REQUEST_ENABLE_BT = 312;

	private static BluetoothAdapter mBluetoothAdapter = null;

	private static ArrayAdapter<String> mArrayAdapter = null;

	private static ArrayAdapter<BluetoothDevice> btDevices = null;
    
	private static final UUID SPP_UUID = UUID
			.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	// UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private BluetoothSocket mbtSocket = null;
	private Button mButton = null;

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

		mButton = this.findViewById(R.id.buttoncancel);

		mButton.setOnClickListener(mDoCancel);
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

			if (mArrayAdapter != null) {
				mArrayAdapter.clear();
				mArrayAdapter.notifyDataSetChanged();
				mArrayAdapter.notifyDataSetInvalidated();
				mArrayAdapter = null;
			}
		} catch (IOException e) {
		}
	}

	private int initDevicesList() {

		flushData();

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),	"Bluetooth not supported!!", Toast.LENGTH_LONG).show();
			return -1;
		}

		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

		setListAdapter(mArrayAdapter);

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
						if (btDeviceList.contains(device) == false) {

							btDevices.add(device);

							mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
							mArrayAdapter.notifyDataSetInvalidated();
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

				if (btDevices == null) {
					btDevices = new ArrayAdapter<BluetoothDevice>(
							getApplicationContext(), android.R.layout.simple_list_item_1);
				}
				boolean isPrinter = getIsPrinter(device.getBluetoothClass().getMajorDeviceClass());
				if (isPrinter && btDevices.getPosition(device) < 0) {
					btDevices.add(device);
					mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" );
					mArrayAdapter.notifyDataSetInvalidated();
				}
			}
		}
	};

	public static BluetoothSocket connectDevice(BluetoothDevice btDevice) throws IOException {
		UUID uuid = btDevice.getUuids()[0].getUuid();
		BluetoothSocket btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
		return btSocket;
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (mBluetoothAdapter == null) {
			return;
		}

		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		Toast.makeText(	getApplicationContext(), "Connecting to " + btDevices.getItem(position).getName() + "," + btDevices.getItem(position).getAddress(), Toast.LENGTH_SHORT).show();

		Thread connectThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					boolean gotuuid = btDevices.getItem(position).fetchUuidsWithSdp();
					if (!gotuuid || btDevices.getItem(position).getUuids() == null) {
						runOnUiThread(printerErrorRunnable);
					}
					else {
						btDevice = btDevices.getItem(position);
						mbtSocket = connectDevice( btDevice);
						setResult(RESULT_OK);
					}
				} catch (IOException ex) {
					runOnUiThread(socketErrorRunnable);
					try {
						mbtSocket.close();
					} catch (IOException e) {
					}
					mbtSocket = null;
					return;
				} finally {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							finish();

						}
					});
				}
			}
		});

		connectThread.start();
	}

	private Runnable printerErrorRunnable = new Runnable() {

		@Override
		public void run() {
			Toast.makeText(getApplicationContext(),	"Failed to connect to device", Toast.LENGTH_SHORT).show();
		}
	};

	private Runnable socketErrorRunnable = new Runnable() {

		@Override
		public void run() {
			Toast.makeText(getApplicationContext(),	"Cannot establish connection", Toast.LENGTH_SHORT).show();
			mBluetoothAdapter.startDiscovery();

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, Menu.FIRST, Menu.NONE, "Refresh Scanning");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case Menu.FIRST:
			initDevicesList();
			break;
		}

		return true;
	}

}
