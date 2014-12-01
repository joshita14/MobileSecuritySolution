package com.example.mobilesecurity;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * This class controls the {@link Camera} hardware of the device. It provides
 * functionality of taking pictures.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public class CameraController {

	/**
	 * Directory path for this app on the SD card.
	 */
	private final static String DIRECTORY_PATH = Environment
			.getExternalStorageDirectory() + File.separator + Util.LOG_TAG;

	/**
	 * Instance of {@link Camera} hardware.
	 */
	private static Camera camera;

	/**
	 * Camera preview.
	 */
	protected static Preview surfaceView;

	/**
	 * {@link ThreadPoolExecutor} for processing clicked image.
	 */
	private static ThreadPoolExecutor processImageThreadPoolExecutor;

	/**
	 * {@link BlockingQueue} for queuing {@link UploadOrStoreImageTask}.
	 */
	private static BlockingQueue<Runnable> processImageBlockingQueue;

	/**
	 * {@link ThreadPoolExecutor} for image upload.
	 */
	private static ThreadPoolExecutor bulkImageUploadThreadPoolExecutor;

	/**
	 * {@link BlockingQueue} for queuing {@link UploadOrStoreImageTask}.
	 */
	private static BlockingQueue<Runnable> bulkImageUploadBlockingQueue;

	/**
	 * Core pool size i.e., number of thread in threadPoolExecutor.
	 */
	private final static int CORE_POOL_SIZE = 1;

	/**
	 * The maximum idle time of thread before killing it.
	 */
	private final static long KEEP_ALIVE_TIME = 60;

	/**
	 * {@link AtomicBoolean} representing internet connection status.
	 */
	private static AtomicBoolean isInternetConnected = new AtomicBoolean();

	/**
	 * Application context.
	 */
	private static Context context;

	/**
	 * Parameterized constructor for {@link CameraController}.
	 * 
	 * @param cntxt
	 *            application context
	 */
	public CameraController(Context cntxt) {
		Log.d(Util.LOG_TAG, "CameraController created with context " + cntxt);
		context = cntxt;
		// Get initial network connection status
		isInternetConnected.set(isConnectedToNetwork());
		initializeThreadPools();
		initializeCamera();
		createDirectory();
	}

	/**
	 * Get network connection status.
	 * 
	 * @return <code>true</code> if connected to network else,
	 *         <code>false</code>.
	 */
	private static boolean isConnectedToNetwork() {

		// Get connectivity manager of this device
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Get network information
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		// Get connection state
		boolean isConnected = netInfo != null
				&& netInfo.isConnectedOrConnecting();

		Log.i(Util.LOG_TAG, "CameraController isConnectedToNetwork "
				+ isConnected);

		// Return whether device is connected to network
		return isConnected;
	}

	/**
	 * Create directory for storing images.
	 */
	private void createDirectory() {

		// Create new file
		File dir = new File(DIRECTORY_PATH);

		// If directory not exists
		if (!dir.exists()) {
			boolean isDirectoryCreated = dir.mkdir();
			Log.d(Util.LOG_TAG,
					"CameraController createDirectory isDirectoryCreated "
							+ isDirectoryCreated);
		}
		// If directory exists
		else {
			Log.d(Util.LOG_TAG,
					"CameraController createDirectory directory already exists.");
		}
	}

	/**
	 * Initialize the {@link ThreadPoolExecutor}'s.
	 */
	private void initializeThreadPools() {

		Log.d(Util.LOG_TAG, "CameraController initializeThreadPools");

		processImageBlockingQueue = new LinkedBlockingQueue<Runnable>();
		processImageThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
				CORE_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				processImageBlockingQueue);

		bulkImageUploadBlockingQueue = new LinkedBlockingQueue<Runnable>();
		bulkImageUploadThreadPoolExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, bulkImageUploadBlockingQueue);
	}

	/**
	 * Initialize the {@link Camera}.
	 */
	private void initializeCamera() {
		Log.d(Util.LOG_TAG, "CameraController initializeCamera");
		try {
			// If camera is null
			if (camera == null) {
				// Get camera. Here zero means the first camera as there may be
				// multiple cameras on the device.
				camera = Camera.open(0);
			}

			// Create the camera preview
			surfaceView = new Preview(context, camera, CameraController.this);

			// Get the window manager
			WindowManager windowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);

			// Set this view as the top most view.
			surfaceView.setZOrderOnTop(true);

			// Create layout parameters of the window.
			LayoutParams params = new WindowManager.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
					WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
					PixelFormat.TRANSPARENT);

			// Set layout params & view to window
			if (windowManager != null) {
				windowManager.addView(surfaceView, params);
			}

			// To hide the preview
			surfaceView.getHolder().setFixedSize(1, 1);
		} catch (Exception e) {
			Log.e(Util.LOG_TAG, "CameraController initializeCamera exception: ");
			e.printStackTrace();
		}
	}

	/**
	 * Dispose this instance of {@link CameraController}.
	 */
	public void dispose() {
		Log.d(Util.LOG_TAG, "CameraController dispose.");
		try {
			// Shut down & nullify the thread pool
			if (processImageThreadPoolExecutor != null) {
				processImageThreadPoolExecutor.shutdownNow();
				processImageThreadPoolExecutor = null;
			}
			// Clear & nullify the queue
			if (processImageBlockingQueue != null) {
				processImageBlockingQueue.clear();
				processImageBlockingQueue = null;
			}
			// Shut down & nullify the thread pool
			if (bulkImageUploadThreadPoolExecutor != null) {
				bulkImageUploadThreadPoolExecutor.shutdownNow();
				bulkImageUploadThreadPoolExecutor = null;
			}
			// Clear & nullify the queue
			if (bulkImageUploadBlockingQueue != null) {
				bulkImageUploadBlockingQueue.clear();
				bulkImageUploadBlockingQueue = null;
			}

			// Release & nullify the camera
			if (camera != null) {
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
			Log.e(Util.LOG_TAG,
					"CameraController dispose exception in dispose: ");
			e.printStackTrace();
		}
	}

	public void takePicture() {
		Log.d(Util.LOG_TAG, "CameraController takePicture.");
		try {
			// If camera is not null & preview's holder is not null
			if (camera != null && surfaceView.getHolder() != null) {

				// Start the preview
				camera.startPreview();

				// Take one shot picture
				camera.setOneShotPreviewCallback(previewCallback);
			}
		} catch (Exception e) {
			Log.e(Util.LOG_TAG, "CameraController takePicture exception: ");
			e.printStackTrace();
		}
	}

	/**
	 * Implementation of {@link PreviewCallback} called when a picture is
	 * clicked.
	 */
	PreviewCallback previewCallback = new PreviewCallback() {

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {

			Log.d(Util.LOG_TAG,
					"CameraController PreviewCallback onPreviewFrame " + data);

			// Create a new UploadOrStoreImageTask. The data received in this
			// callback is YUV type. Converted it to JPEG byte array.
			UploadOrStoreImageTask uploadOrStoreImageTask = new UploadOrStoreImageTask(
					Util.convertYUVToJPEGByteArray(data, camera),
					isInternetConnected.get(), context);

			// If camera is not null, stop the preview
			if (camera != null) {
				try {
					camera.stopPreview();
				} catch (Exception e) {
					Log.e(Util.LOG_TAG,
							"CameraController PreviewCallback onPreviewFrame exception while stopping preview: ");
					e.printStackTrace();
				}
			}

			// Submit the task to thread pool for execution.
			processImageThreadPoolExecutor.submit(uploadOrStoreImageTask);
		}
	};

	/**
	 * This inner class is an implementation of {@link BroadcastReceiver}. It
	 * receives the callback when either wifi connectivity is changed or device
	 * boots up.
	 * 
	 * @author Priyam Bakliwal
	 * @version 1.0
	 */
	public static class BootAndConnectivityActionObserver extends
			BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(Util.LOG_TAG,
					"BootAndConnectivityActionObserver onReceive device booted");

			// If device boot intent is received
			if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

				Log.d(Util.LOG_TAG,
						"BootAndConnectivityActionObserver onReceive device booted");

				// Start the service on device boot
				Intent startServiceIntent = new Intent(context,
						CameraService.class);
				context.startService(startServiceIntent);
			}
			// If connectivity change intent is received
			else if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

				// Get connection status
				boolean isConnected = isConnectedToNetwork();

				// If status is same as the intent received
				if (isConnected == isInternetConnected.get()) {
					Log.d(Util.LOG_TAG,
							"BootAndConnectivityActionObserver onReceive connectivity change same state.");
				} else {
					Log.d(Util.LOG_TAG,
							"BootAndConnectivityActionObserver onReceive connectivity change different state.");

					// Set connection state
					isInternetConnected.set(isConnected);

					// If connected to network
					if (isConnected) {
						Log.d(Util.LOG_TAG,
								"BootAndConnectivityActionObserver onReceive connected");

						// Get the directory
						File dir = new File(DIRECTORY_PATH);

						if (dir.exists() && dir.isDirectory()) {

							// Get all the files in the directory
							File[] files = dir.listFiles();

							// For each file in the directory
							for (File file : files) {

								// Get byte array from file
								byte[] data = Util.convertFileToByteArray(file);

								// Create new task for uploading
								UploadOrStoreImageTask task = new UploadOrStoreImageTask(
										data, isConnected, file.getName()
												.split(".")[0], context);

								// Submit it to thread pool for execution
								bulkImageUploadThreadPoolExecutor.submit(task);

								// Delete the file after uploading
								file.delete();
							}
						}
					}
				}
			}
		}
	}

}