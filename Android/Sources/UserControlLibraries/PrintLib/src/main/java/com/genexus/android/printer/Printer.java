package com.genexus.android.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.genexus.android.core.base.services.Services;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

// This code is based on https://www.androidcode.ninja/android-bluetooth-tutorial/
public class Printer {
    private String mPrinterName;
    private int mDpi;
    private int mWidth;

    public enum Rotation
    {
        No,
        Left,
        Right
    }

    // android built in classes for bluetooth operations
	private final Context mContext;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    // needed for communication to bluetooth device / network
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private Thread mWorkerThread;
    private byte[] mReadBuffer;
    private int mReadBufferPosition;
    private volatile boolean mStopWorker;

    // needed for autoclose connection
    private Timer mCloseTimer;

    public Printer(Context context) {
    	mContext = context;
        mDpi = 203; // 8 * 25.4 dpi = 8 px/mm
        mWidth = 48; // mm
    }

    public String getName() {
        return mPrinterName;
    }

    public void setName(String printerName) {
        mPrinterName = printerName;
    }

    public int getDpi() {
        return mDpi;
    }

    public void setDpi(int dpi) {
        mDpi = dpi;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public String[] getDevices() {
        Set<BluetoothDevice> pairedDevices = getBondedDevices();
        String[] devices;
        if (pairedDevices == null) {
            devices = new String[0];
        } else {
            devices = new String[pairedDevices.size()];
            int n = 0;
            for (BluetoothDevice device : pairedDevices)
                devices[n++] = device.getName();
        }
        return devices;
    }

    public void print(String filePath, Rotation rotation) {
        if (findBT() && openBT()) {
            sendData(filePath, rotation);
        }
    }

    public void cutPaper() {
        if (findBT() && openBT()) {
            sendCutPaper();
        }
    }

    // this will return the bonded devices
    private Set<BluetoothDevice> getBondedDevices() {
		final BluetoothManager bluetoothManager =
			(BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Services.Log.error("No bluetooth adapter available");
            return null;
        }
        else {
            return bluetoothAdapter.getBondedDevices();
        }
    }

    // this will find a bluetooth printer device
    private boolean findBT() {
        if (mPrinterName == null) {
            Services.Log.info("Printer Name not set");
            return false;
        }
        else if (mDevice != null) {
            if (mDevice.getName().equals(mPrinterName)) {
                Services.Log.debug("Using the same printer");
                return true;
            }
            else {
                mDevice = null;
                if (mSocket != null && mSocket.isConnected()) {
                    closeBT();
                    mSocket = null;
                }
            }
        }

        Set<BluetoothDevice> pairedDevices = getBondedDevices();

        if (pairedDevices != null) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(mPrinterName)) {
                    mDevice = device;
                    break;
                }
            }
        }

        if (mDevice != null) {
            Services.Log.info("Bluetooth device found");
            return true;
        } else {
            Services.Log.info("Bluetooth device not found");
            return false;
        }
    }

    // tries to open a connection to the bluetooth printer device
    private boolean openBT() {
        if (mSocket != null && mSocket.isConnected()) {
            resetCloseTimer();
            return true;
        }

        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect();
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            beginListenForData();
            resetCloseTimer();

            Services.Log.info("Bluetooth Opened");
            return true;
        } catch (IOException e) {
            Services.Log.error(e);
            return false;
        }
    }

    // close the connection to bluetooth printer.
    private boolean closeBT() {
        try {
            mStopWorker = true;
            mOutputStream.close();
            mInputStream.close();
            mSocket.close();
            Services.Log.info("Bluetooth Closed");
            return true;
        } catch (IOException e) {
            Services.Log.error(e);
            return false;
        }
    }

    private void resetCloseTimer() {
        if (mCloseTimer != null)
            mCloseTimer.cancel();
        mCloseTimer = new Timer();
        mCloseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                closeBT();
            }
        }, 15 * 60 * 1000L); // 15 minutes
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    private void beginListenForData() {
        // this is the ASCII code for a newline character
        final byte delimiter = 10;

        mStopWorker = false;
        mReadBufferPosition = 0;
        mReadBuffer = new byte[1024];

        mWorkerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !mStopWorker) {
                try {
                    int bytesAvailable = mInputStream.available();

                    if (bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[mReadBufferPosition];
                                System.arraycopy(
                                        mReadBuffer, 0,
                                        encodedBytes, 0,
                                        encodedBytes.length
                                );

                                // specify US-ASCII encoding
                                final String data = new String(encodedBytes, "US-ASCII");
                                mReadBufferPosition = 0;

                                // tell the user data were sent to bluetooth printer device
                                Services.Log.info(data);
                            } else {
                                mReadBuffer[mReadBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException ex) {
                    mStopWorker = true;
                }
            }
        });

        mWorkerThread.start();
    }

    // this will send the data to be printed by the bluetooth printer
    private int sendData(String fileUri, Rotation rotation) {
        try {
            if (fileUri.startsWith(ContentResolver.SCHEME_FILE))
                fileUri = Uri.parse(fileUri).getPath();

            String extension = FilenameUtils.getExtension(fileUri).toLowerCase();

            if (extension.equals("pdf")) {
                ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(new File(fileUri), ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);

                 // let us just render all pages
                 final int pageCount = renderer.getPageCount();
                 for (int i = 0; i < pageCount; i++) {
                     PdfRenderer.Page page = renderer.openPage(i);

                     double maxScale = mDpi / 72.;  // 72 dpi is default on page
                     double width = (mWidth / 25.4) * mDpi;  // convert to inch and multiply by dpi
                     int widthScaled;
                     int heightScaled;
                     if (rotation == Rotation.No) {
                         widthScaled = (int) Math.round(Math.min(page.getWidth() * maxScale, width) / 8.) * 8;  // multiple of 8 and not scale down
                         heightScaled = (int) (page.getHeight() * (widthScaled / (double)page.getWidth()) + .5);  // scale relative to width
                     } else {
                         heightScaled = (int) Math.round(Math.min(page.getHeight() * maxScale, width) / 8.) * 8;  // multiple of 8 and not scale down
                         widthScaled = (int) (page.getWidth() * (heightScaled / (double)page.getHeight()) + .5);  // scale relative to height
                     }
                     Bitmap bitmap = Bitmap.createBitmap(widthScaled, heightScaled, Bitmap.Config.ARGB_8888);

                     // say we render for showing on the screen
                     Services.Log.debug("Rendering data");
                     page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                     printBitmap(getRotatedBitmap(bitmap, rotation));

                     page.close();
                }
                // close the renderer
                parcelFileDescriptor.close();
                renderer.close();

            } else if (extension.equals("png") || extension.equals("jpg")) {
                 BitmapFactory.Options options = new BitmapFactory.Options();
                 options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                 Bitmap bitmap = BitmapFactory.decodeFile(fileUri, options);
                 printBitmap(getResizedBitmap(getRotatedBitmap(bitmap, rotation)));
            } else if (extension.equals("txt")) {
                 int c;
                 BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(fileUri));
                 int lastC = '\n';
                 while ((c = bufferedReader.read()) != -1) {
                     mOutputStream.write(c);
                     lastC = c;
                 }
                 if (lastC != '\n')
                     mOutputStream.write('\n');
                 bufferedReader.close();
            } else {
                Services.Log.info(String.format("Unknown extension '%s', supported extensions pdf, png, jpg and txt", extension));
            }

            return 0;
        } catch (IOException e) {
            closeBT(); // so we reconnect again next time, in case that fixes the problem
            mSocket = null;
            Services.Log.error(e);
            return 1;
        }
    }

    private void printBitmap(Bitmap bitmap) throws IOException {
        Services.Log.debug("Creating data");
        PrintImage printImage = new PrintImage(bitmap);

        Services.Log.debug("Preparing data");
        printImage.prepareImage();

        // There seems to a bug in mOutputStream.write(byte[]), we use the write(byte) method instead
        // Similar problem can be found here
        // https://stackoverflow.com/questions/8079836/outputstream-write-has-has-buffer-overflow
        Services.Log.debug("Sending data to printer");
        byte[] data = printImage.getPrintImageData();
        for (int p = 0; p < data.length; p++)
            mOutputStream.write(data[p]);
        Services.Log.debug("Data sent");
    }

    private Bitmap getRotatedBitmap(Bitmap bm, Rotation rotation) {
        if (rotation == Rotation.No)
            return bm;

        Services.Log.debug("Applying Rotation");
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation == Rotation.Left ? -90 : 90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        bm.recycle();
        return rotatedBitmap;
    }

    // this will send a cut paper command in case the printer supports it
    private int sendCutPaper() {
        try {
            byte[] data = PrintImage.PAPER_CUT;
            for (int p = 0; p < data.length; p++)
                mOutputStream.write(data[p]);
            return 0;
        } catch (IOException e) {
            Services.Log.error(e);
            return 1;
        }
    }

    // This method was extracted from Bluetooth Print app from Play Store
    private Bitmap getResizedBitmap(Bitmap bm) {
        int reqWidth = mWidth * 8;
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (width == reqWidth) {
            return bm;
        }
        float scaleWidth;
        float scaleHeight;
        Matrix matrix;
        Bitmap resizedBitmap;
        if (width < reqWidth && width > 16) {
            int diff = width % 8;
            if (diff == 0) {
                return bm;
            }
            scaleWidth = ((float) (width - diff)) / ((float) width);
            scaleHeight = ((float) (((width - diff) * height) / width)) / ((float) height);
            matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        } else if (width <= 16) {
            return bm;
        } else {
            scaleWidth = ((float) reqWidth) / ((float) width);
            scaleHeight = ((float) ((reqWidth * height) / width)) / ((float) height);
            matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        }
    }
}
