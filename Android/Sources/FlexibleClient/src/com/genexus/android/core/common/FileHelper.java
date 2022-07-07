package com.genexus.android.core.common;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileHelper {

	private static final String TEMP_FILE_PREFIX = "tmp__";

	public static boolean copyFile(@NonNull String inFileName, @NonNull String outFileName, boolean cleanDestination) throws IOException {
		File dbFile = new File(inFileName);

		File copieDB = new File(outFileName);
		if (dbFile.exists()) {
			Services.Log.debug("copyFile", " DB file exist " + inFileName);
			@SuppressWarnings("resource")
			FileChannel src = new FileInputStream(dbFile).getChannel();
			@SuppressWarnings("resource")
			FileChannel dst = new FileOutputStream(copieDB).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			return true;
		}
		else if (cleanDestination)
		{
			// if cleanDestination and nothing to copy, clean output file
			if (copieDB.exists())
				copieDB.delete();
		}
		return false;
	}

	public static void deleteDir(@NonNull String dir) {
		File fDir = new File(dir);
		try {
			FileUtils.deleteDirectory(fDir);
		}
		catch (IOException ex) {
			Services.Log.error( "Unable to delete directory. " + ex.getMessage());
		}
	}

	public static boolean writeStringAsFile(final String fileContents, @NonNull String filePath) {
		OutputStreamWriter osw = null;
		try (FileOutputStream fos = new FileOutputStream(filePath))
		{
			osw = new OutputStreamWriter(fos, UTF_8);
			osw.write(fileContents);
			osw.close();
			return true;
		} catch (IOException ex) {
			Services.Log.error( "Unable to write file. " + ex.getMessage());
		}
		return false;
	}

	public static String readFileAsString(@NonNull String filePath) {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try (FileInputStream fis = new FileInputStream(filePath)) {
			InputStreamReader isr = new InputStreamReader(fis, UTF_8);
			BufferedReader buffreader = new BufferedReader(isr);
			while ((line = buffreader.readLine()) != null) stringBuilder.append(line);
			isr.close();
		} catch (FileNotFoundException ex) {
			Services.Log.error( "Unable to read file. " + ex.getMessage());
			return null;
		} catch (IOException ex) {
			Services.Log.error( "Unable to read file. " + ex.getMessage());
			return null;
		}
		return stringBuilder.toString();
	}

	public static boolean deleteFile(String filePath) {
		if (Strings.hasValue(filePath)) {
			File file = new File(filePath);
			if (file.exists())
				return file.delete();
		}

		return false;
	}

	public static String renameToTemporal(String original) {
		if (Strings.hasValue(original)) {
			File originalPath = new File(original);
			File tempPath = new File(getTempPath(originalPath));
			if (rename(originalPath, tempPath))
				return tempPath.getAbsolutePath();
		}

		return null;
	}

	public static void undoTemporalRename(String destination) {
		if (Strings.hasValue(destination)) {
			File tempFile = new File(destination);
			File originalFile = new File(getOriginalName(tempFile));
			rename(tempFile, originalFile);
		}
	}

	private static boolean rename(File origin, File destination) {
		if (origin.exists())
			return origin.renameTo(destination);

		return false;
	}

	private static String getOriginalName(File tempFile) {
		if (tempFile != null) {
			String originalFileName = tempFile.getName().replaceFirst(TEMP_FILE_PREFIX, Strings.EMPTY);
			String path = tempFile.getAbsolutePath();
			int slashIndex = path.lastIndexOf(File.separator);
			return path.substring(0, slashIndex) + File.separator + originalFileName;
		}

		return null;
	}

	private static String getTempPath(File file) {
		if (file != null) {
			String originalFileName = file.getName();
			String path = file.getAbsolutePath();
			int slashIndex = path.lastIndexOf(File.separator);
			return path.substring(0, slashIndex) + File.separator + TEMP_FILE_PREFIX + originalFileName;
		}

		return null;
	}
}
