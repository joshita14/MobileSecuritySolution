package com.example.mobilesecurity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class is an implementation of {@link SurfaceView} which acts as the
 * preview for the camera.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 */
@SuppressLint("NewApi")
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

	/**
	 * Instance of {@link Camera}.
	 */
	private Camera camera;

	/**
	 * Instance of {@link CameraController}.
	 */
	private CameraController cameraController;

	/**
	 * Parameterized constructor for {@link Preview}.
	 * 
	 * @param context
	 *            application context
	 * @param camera
	 *            instance of {@link Camera}
	 * @param cameraController
	 *            instance of {@link CameraController}
	 */
	@SuppressWarnings("deprecation")
	public Preview(Context context, Camera camera,
			CameraController cameraController) {
		super(context);

		Log.d(Util.LOG_TAG, "Preview creaed");

		this.camera = camera;
		this.cameraController = cameraController;

		// Get this view's holder
		SurfaceHolder surfaceHolder = getHolder();

		// Add callback to this holder
		surfaceHolder.addCallback(this);

		// Set surface's type.
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		try {
			// Set preview display
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			Log.e("Preview",
					"Preview surfaceCreated exception on setPreviewDisplay: ");
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Dispose the camera controller when surface is destroyed
		cameraController.dispose();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Not used for this app.
	}
}
