package com.example.mobilesecurity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * This class is a utility class that provides some utility methods for this
 * component.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public abstract class Util {

	public final static String LOG_TAG = "MobileSecurity";
	public final static String TIME_STAMP = "timestamp";
	public final static String IMAGE_DATA = "imageData";
	public final static String WIFI_MAC_ID = "wifiMacID";
	public final static String TM_DEVICE_ID = "tmDeviceId";
	public final static String TM_SERIAL_ID = "tmSerialId";
	public final static String ANDROID_ID = "androidId";

	/**
	 * Get different unique identifiers of this device.
	 * 
	 * @param context
	 *            the application context
	 * @return a {@link HashMap} containing different unique identifiers
	 */
	public static HashMap<String, String> getDeviceId(Context context) {

		// Get the wifi manager of this device
		final WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		// Get the telephony manager of this device
		final TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDeviceId, tmSerialId, androidId, wifiMacID;

		// Get the wifi mac address of this device
		wifiMacID = wifiManager.getConnectionInfo().getMacAddress();

		// Get the device id
		tmDeviceId = "" + telephonyManager.getDeviceId();

		// Get the serial number
		tmSerialId = "" + telephonyManager.getSimSerialNumber();

		// Get the Android_ID of this device
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		// Create new hash map
		HashMap<String, String> hashMap = new HashMap<String, String>();

		// Put all the id's
		hashMap.put(WIFI_MAC_ID, wifiMacID);
		hashMap.put(TM_DEVICE_ID, tmDeviceId);
		hashMap.put(TM_SERIAL_ID, tmSerialId);
		hashMap.put(ANDROID_ID, androidId);

		// Return map with id's
		return hashMap;
	}

	/**
	 * Convert a file to byte array.
	 * 
	 * @param file
	 *            {@link File} to be converted
	 * @return byte array corresponding to the file
	 */
	public static byte[] convertFileToByteArray(File file) {

		// Create a buffer of file's length
		byte[] buffer = new byte[(int) file.length()];
		InputStream ios = null;
		try {
			// Create an input stream & read the file into a buffer
			ios = new FileInputStream(file);
			ios.read(buffer);
		}

		catch (IOException e) {
			Log.e(LOG_TAG,
					"Util convertFileToByteArray exception in reading file: ");
			e.printStackTrace();
		}

		finally {
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				Log.e(LOG_TAG,
						"Util convertFileToByteArray exception in closing stream: ");
				e.printStackTrace();
			}
		}

		// Return the byte array
		return buffer;
	}

	/**
	 * Convert a YUV array to JPEG byte array.
	 * 
	 * @param yuvArray
	 *            YUV array to be converted
	 * @param camera
	 *            {@link Camera} with which picture is clicked
	 * @return JPEG byte array
	 */
	public static byte[] convertYUVToJPEGByteArray(byte[] yuvArray,
			Camera camera) {

		// Return null if camera/YUVarray is null
		if (camera == null || yuvArray == null) {
			return null;
		}
		ByteArrayOutputStream outputStream = null;

		// Get height & width of the camera preview
		int width = camera.getParameters().getPreviewSize().width;
		int height = camera.getParameters().getPreviewSize().height;

		// Create a YUV image with given camera params
		YuvImage yuvImage = new YuvImage(yuvArray, camera.getParameters()
				.getPreviewFormat(), width, height, null);

		// Create new out stream
		outputStream = new ByteArrayOutputStream();

		// Compress YUV array to JPEG array and write it to out stream
		yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, outputStream);

		// Return JPEG byte array
		return outputStream.toByteArray();
	}

	/**
	 * Get current date.
	 * 
	 * @return current date
	 */
	public static String getCurrentDateTime() {

		// Create new date format
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss",
				Locale.ENGLISH);

		// Get current date
		Date date = new Date();

		// Return current date in given format
		return dateFormat.format(date);
	}

}
