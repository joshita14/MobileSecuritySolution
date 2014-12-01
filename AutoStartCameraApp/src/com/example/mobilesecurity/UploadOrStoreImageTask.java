package com.example.mobilesecurity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * This class is an implementation of {@link Runnable} used for performing image
 * upload or storage.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public class UploadOrStoreImageTask implements Runnable {

	/**
	 * Data to be uploaded or stored
	 */
	private byte[] data;

	/**
	 * Internet connection state
	 */
	private boolean isConnectedToNetwork;

	/**
	 * File name to be uploaded
	 */
	private String fileName;

	/**
	 * Application context
	 */
	private Context context;

	/**
	 * Parameterized constructor for {@link UploadOrStoreImageTask} for
	 * storage/upload.
	 * 
	 * @param data
	 *            data to be uploaded/stored
	 * @param isConnectedToNetwork
	 *            connection state
	 * @param context
	 *            app context
	 */
	public UploadOrStoreImageTask(byte[] data, boolean isConnectedToNetwork,
			Context context) {
		Log.d(Util.LOG_TAG, "UploadOrStoreImageTask created");
		this.context = context;
		this.data = data;
		this.isConnectedToNetwork = isConnectedToNetwork;
	}

	/**
	 * Parameterized constructor for {@link UploadOrStoreImageTask} for bulk
	 * upload.
	 * 
	 * @param data
	 *            data to be uploaded/stored
	 * @param isConnectedToNetwork
	 *            connection state
	 * @param fileName
	 *            name of file to be uploaded
	 * @param context
	 *            app context
	 */
	public UploadOrStoreImageTask(byte[] data, boolean isConnectedToNetwork,
			String fileName, Context context) {
		Log.d(Util.LOG_TAG, "UploadOrStoreImageTask bulk upload created");
		this.context = context;
		this.data = data;
		this.isConnectedToNetwork = isConnectedToNetwork;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		Log.d(Util.LOG_TAG, "UploadOrStoreImageTask run()");

		// If connected to network
		if (isConnectedToNetwork) {
			// If file name is null
			if (fileName == null) {
				// Set file name as current date
				fileName = Util.getCurrentDateTime();
			}
			// Upload image to server
			ImageUploader.uploadImage("ewatchman", "honey1.png");
		}
		// If not connected to network
		else {
			// Write image on SD card
			storeOnSdCard();
		}
	}

	/**
	 * Store image on SD card.
	 */
	private void storeOnSdCard() {
		Log.d(Util.LOG_TAG, "UploadOrStoreImageTask storeOnSdCard");
		FileOutputStream outputStream = null;
		try {
			// Initialize file
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "MobileSecurity" + File.separator
					+ Util.getCurrentDateTime() + ".jpeg");

			// Create new file
			file.createNewFile();

			// Create new output stream
			outputStream = new FileOutputStream(file);

			// write the bytes in file
			outputStream.write(data);
		} catch (IOException e) {
			Log.e(Util.LOG_TAG,
					"UploadOrStoreImageTask storeOnSdCard exception: ");
			e.printStackTrace();
		} finally {
			try {
				// If output stream is not null
				if (outputStream != null) {
					// Flush & close the stream
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				Log.e(Util.LOG_TAG,
						"UploadOrStoreImageTask storeOnSdCard exception in  closing stream: ");
				e.printStackTrace();
			}
		}
	}

}