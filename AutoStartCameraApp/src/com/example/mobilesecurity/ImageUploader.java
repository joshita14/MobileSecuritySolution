package com.example.mobilesecurity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * This class provides functionality for image uploading to server.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public class ImageUploader {

	/**
	 * The server url for posting image to
	 */
	private static String serviceUrl = "http://192.168.1.107:9090/BillSoft/rest/hello/placeorder​";

	/**
	 * {@link HashMap} of device identifiers
	 */
	private static HashMap<String, String> hashMap;

	/**
	 * Create JSON data to be posted.
	 * 
	 * @param data
	 *            image data
	 * @param dateTime
	 *            date of click
	 * @return json string to upload
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 */
	private static String getJsonDataToPost(byte[] data, String dateTime)
			throws UnsupportedEncodingException, JSONException {

		// Create new JSON object
		JSONObject jsonObject = new JSONObject();

		// Add data to json object
		jsonObject.put(Util.IMAGE_DATA, new String(data, "UTF-8"));
		jsonObject.put(Util.TIME_STAMP, dateTime);
		jsonObject.put(Util.ANDROID_ID, hashMap.get(Util.ANDROID_ID));
		jsonObject.put(Util.TM_DEVICE_ID, hashMap.get(Util.TM_DEVICE_ID));
		jsonObject.put(Util.TM_SERIAL_ID, hashMap.get(Util.TM_SERIAL_ID));
		jsonObject.put(Util.WIFI_MAC_ID, hashMap.get(Util.WIFI_MAC_ID));

		// Return the json string
		return jsonObject.toString();
	}

	/**
	 * Upload image to server.
	 * 
	 * @param data
	 *            image data
	 * @param dateTime
	 *            date of click
	 * @param context
	 *            app context
	 */
	public static void uploadImage(byte[] data, String dateTime, Context context) {
		Log.d(Util.LOG_TAG, "ImageUploader uploadImage");

		// If hash map is null
		if (hashMap == null) {
			// Get device id hash map
			hashMap = Util.getDeviceId(context);
		}

		HttpURLConnection urlConnection = null;
		OutputStream out = null;
		try {
			// Get json string & convert it to byte array
			byte[] paramByteArray = getJsonDataToPost(data, dateTime).getBytes(
					"UTF-8");

			// create connection
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) urlToRequest.openConnection();
			urlConnection.setConnectTimeout(10000);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type",
					"application/json; charset=utf8");
			urlConnection.setFixedLengthStreamingMode(paramByteArray.length);

			// Get connection's output stream
			out = urlConnection.getOutputStream();

			// Write json data to stream
			out.write(paramByteArray);

			// Close the stream
			out.close();

			// Get the status code of request
			int statusCode = urlConnection.getResponseCode();

			// If status code is error
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				Log.i(Util.LOG_TAG,
						"ImageUploader uploadImage HTTP_UNAUTHORIZED");
			} else if (statusCode == HttpURLConnection.HTTP_OK) {
				Log.i(Util.LOG_TAG, "ImageUploader uploadImage HTTP_OK");
			}

		} catch (MalformedURLException e) {
			Log.e(Util.LOG_TAG, "ImageUploader uploadImage exception: ");
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			Log.e(Util.LOG_TAG, "ImageUploader uploadImage exception: ");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(Util.LOG_TAG, "ImageUploader uploadImage exception: ");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(Util.LOG_TAG, "ImageUploader uploadImage exception: ");
			e.printStackTrace();
		} finally {
			// Disconnect the connection
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}
	public static void uploadImage(String bucketName, String filePath) {
		Log.d(Util.LOG_TAG, "ImageUploader uploadImage");
		try {
			CloudStorage.uploadFile(bucketName, filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
