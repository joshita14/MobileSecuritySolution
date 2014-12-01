package com.example.mobilesecurity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

/**
 * This class is an implementation of {@link Activity}. This is the launcher
 * activity i.e, when the app icon is clicked, the onCreate method is called.
 * 
 * @author Priyam Bakliwal
 * @version 1.0
 *
 */
public class FirstTimeSetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the shared preferences for this application.
		SharedPreferences sharedpreferences = getSharedPreferences("CameraApp",
				Context.MODE_PRIVATE);

		// If the boolean isFirstLaunch is not stored, this is the first launch
		// of the app
		if (!sharedpreferences.contains("isFirstLaunch")) {

			Log.i(Util.LOG_TAG,
					"FirstTimeSetupActivity onCreate first time launch.");

			// Store the boolean isFirstLaunch
			Editor editor = sharedpreferences.edit();
			editor.putBoolean("isFirstLaunch", false);
			editor.commit();

			// Start the background service
			Intent startServiceIntent = new Intent(FirstTimeSetupActivity.this,
					CameraService.class);
			startService(startServiceIntent);
		}
		// Else don't start the service as it is already running.
		else {
			Log.i(Util.LOG_TAG,
					"FirstTimeSetupActivity onCreate service already running.");
		}

		// Finish the activity or close the app after starting the service.
		finish();
	}
}
