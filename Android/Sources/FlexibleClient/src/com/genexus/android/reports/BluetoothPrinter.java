package com.genexus.android.reports;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.artech.base.services.IBluetoothPrinter;
import com.genexus.android.core.activities.BTDeviceListActivity;

import java.io.*;
import java.util.UUID;

public class BluetoothPrinter implements IBluetoothPrinter
{
    private BluetoothSocket btSocket;
    private OutputStream btOutputStream;

    public void connect()
    {
        BluetoothDevice btDevice = BTDeviceListActivity.getBTDevice();
        if (btDevice != null) {
            try {
                btSocket = connectDevice( btDevice);
                btSocket.connect();

                btOutputStream = btSocket.getOutputStream();
            }
            catch( IOException e)
            {
                System.err.println(e.toString());
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    public static BluetoothSocket connectDevice(BluetoothDevice btDevice) throws IOException
    {
        UUID uuid = btDevice.getUuids()[0].getUuid();
        BluetoothSocket btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
        return btSocket;
    }

    @Override
    public boolean print( FileInputStream in)
    {
        boolean bRet = true;
        connect();

        //Services.Log.debug("isconnected:" + btSocket.isConnected());
        // this wait is necessary to the printer work.
        // It should be not connected or raise an exception, but not. Only do not work without the sleep
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        //Services.Log.debug("isconnected:" + btSocket.isConnected());

        if (btOutputStream != null)
        {
            try
            {
                byte[] buf = new byte[1024];
                int len;
                while((len=in.read(buf))>0){
                    btOutputStream.write(buf,0,len);
                }
                btOutputStream.flush();
            }
            catch (IOException e)
            {
                bRet = false;
                //Services.Log.debug("Socket FAILED :" + e.toString());
                System.err.println(e.toString());
            }
        }
        return bRet;
    }

    @Override
    public void cleanUp()
    {
        try
        {
            if(btOutputStream != null)
            {
                btOutputStream.close();
                btOutputStream = null;
            }
        }
        catch (IOException e)
        {
        }
        try
        {
            if(btSocket != null)
            {
                btSocket.close();
                btSocket = null;
            }
        }
        catch (IOException e)
        {
        }
    }
}

