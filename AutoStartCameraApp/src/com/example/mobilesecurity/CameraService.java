package com.example.mobilesecurity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This class is an implementation of {@link Service}. This is started on first
 * launch of the app and device boot. Also, Android operating system may restart
 * it due to lack of system resources.
 * 
 * This service acts as a background service which takes snapshots at a fixed
 * interval of time.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public class CameraService extends Service {

	/**
	 * Instance of {@link CameraController}.
	 */
	private CameraController cameraController;

	/**
	 * {@link ExecutorService} for taking pictures at a fixed interval of time.
	 */
	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * Click interval in seconds.
	 */
	private static long clickInterval = 60;

	/**
	 * Initial delay for clicking pictures.
	 */
	private static long initialDelay = 10;

	@Override
	public void onCreate() {
		Log.i(Util.LOG_TAG, "CameraService onCreate");
	}

	public void onDestroy() {

		Log.i(Util.LOG_TAG, "CameraService onDestroy");

		// If controller is created
		if (cameraController != null) {

			// Dispose controller
			cameraController.dispose();

			// Nullify controller
			cameraController = null;
		}

		// If executor service/timer is created
		if (scheduledExecutorService != null) {

			// Shut down service/timer
			scheduledExecutorService.shutdownNow();

			// Nullify service/timer
			scheduledExecutorService = null;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i(Util.LOG_TAG, "CameraService onStartCommand");

		// If controller is not created
		if (cameraController == null) {

			// Create the controller
			cameraController = new CameraController(getApplicationContext());
		}

		// If service/timer is not created
		if (scheduledExecutorService == null) {

			// Create service/timer with single thread
			scheduledExecutorService = Executors.newScheduledThreadPool(1);

			// Schedule or start the service/timer
			scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {

					Log.d(Util.LOG_TAG, "ScheduledExecutorService Runnable run");

					// Take picture
					cameraController.takePicture();
				}
			}, initialDelay, clickInterval, TimeUnit.SECONDS);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
