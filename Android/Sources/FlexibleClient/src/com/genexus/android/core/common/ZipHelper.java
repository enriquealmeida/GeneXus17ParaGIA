package com.genexus.android.core.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.superapps.MiniApp;
import com.genexus.android.core.base.services.Services;

import org.apache.commons.io.IOUtils;

public class ZipHelper {
	private static final String LOG_TAG = "ZipHelper";

	private String mZipFile;
	private String mLocation;
	private InputStream mInput;

	private static final int BUFFER = 80000;

	public ZipHelper(String zipFile, String location) {
    	mZipFile = zipFile;
    	mLocation = location;
    	dirCheckerLocal("");
	}
  
  	public ZipHelper(InputStream zipFile) {
	  mInput = zipFile;
  }

  	// unzip for metadata files
	public void unzip(Context context, GenexusApplication genexusApplication) {
		try (InputStream fileInputStream = (mInput == null) ? new FileInputStream(mZipFile) : new BufferedInputStream(mInput))
		{
       	 	try (ZipInputStream zipInput = new ZipInputStream(fileInputStream)) {
				ZipEntry zipEntry;
				final byte[] buffer = new byte[200000];
				while ((zipEntry = zipInput.getNextEntry()) != null) {
					Services.Log.debug(LOG_TAG, "Unzipping " + zipEntry.getName());

					if (zipEntry.isDirectory()) {
						dirCheckerLocal(zipEntry.getName());
					} else {
						FileOutputStream fos;
						String fileName = genexusApplication.getName() + zipEntry.getName().replace(".json", "");
						if (genexusApplication.isMiniApp()) {
							File miniAppDir = new File(((MiniApp) genexusApplication).getBaseDir(), fileName);
							fos = new FileOutputStream(miniAppDir);
						} else {
							fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
						}

						try {
							for (int c = zipInput.read(buffer, 0, buffer.length); c != -1; c = zipInput.read(buffer, 0, buffer.length))
								fos.write(buffer, 0, c);

							zipInput.closeEntry();
						} finally {
							IOUtils.closeQuietly(fos);
						}
					}
				}
			}

    	} catch(IOException e) {
      		Services.Log.error(LOG_TAG, "Exception unzipping", e);
    	}
	}

	private void dirCheckerLocal(String dir) {
  		dirCheckOrCreate(mLocation + dir);
  	}


  	// zip static helper generic methods

	// zip usage
	// String[] s = new String[2];
	// //Type the path of the files in here
	// s[0] = inputPath + "/image.jpg";
	// s[1] = inputPath + "/textfile.txt"; // /sdcard/ZipDemo/textfile.txt
	//
	// // first parameter is d files second parameter is zip file name
	//
	// // calling the zip function
	// ZipHelper.zip(s, inputPath + inputFile);
	public static boolean zip(String[] files, @NonNull String zipFileName) {
		BufferedInputStream origin = null;
		try (FileOutputStream dest = new FileOutputStream(zipFileName))
		{
			try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(dest)))
			{
				byte[] data = new byte[BUFFER];
				for (int i = 0; i < files.length; i++)
				{
					//Services.Log.debug("Zip Adding: " + files[i]);
					FileInputStream fileInput = new FileInputStream(files[i]);
					origin = new BufferedInputStream(fileInput, BUFFER);

					ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
					zipOut.putNextEntry(entry);
					int count;

					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						zipOut.write(data, 0, count);
					}
					origin.close();
				}
			}
		} catch (IOException e) {
			Services.Log.error("Error in zip compress" + e.getMessage());
			return false;
		}
		return true; // All Ok
	}

	// unzip usage
	// ZipHelper.unzip(inputPath + inputFile, outputPath);
	public static boolean unzip(@NonNull String zipFilePath, @NonNull String targetLocation) {

		//create target location folder if not exist
		dirCheckOrCreate(targetLocation);

		try {
			File fileDir = new File(targetLocation);
			// get directory canonical path
			String dirCanonicalPath = fileDir.getCanonicalPath();

			ZipFile zipFile = new ZipFile(zipFilePath);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File file = new File(targetLocation, entry.getName());
				// check entry path, must start with Dir path.
				String canonicalPath = file.getCanonicalPath();
				if (!canonicalPath.startsWith(dirCanonicalPath)) {
					Services.Log.error("Error in unzip file entry canonicalPath not match");
					return false;
				}
				else
				{
					if (entry.isDirectory()) {
						file.mkdirs();
					} else {
						file.getParentFile().mkdirs();
						try (InputStream in = zipFile.getInputStream(entry)) {
							copy(in, file);
						}
					}
				}
			}
		} catch (IOException ex) {
			Services.Log.error("Error in unzip file" + ex.getMessage());
			return false;
		}
		return true; // All Ok
	}

	private static void copy(InputStream in, File file) throws IOException {
		;
		try (OutputStream out = new FileOutputStream(file)) {
			copy(in, out);
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}


	public static void dirCheckOrCreate(@NonNull String dir) {
		File f = new File(dir);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}

}
