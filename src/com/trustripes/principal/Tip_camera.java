package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.GoogleTracker;
import com.google.analytics.tracking.android.TrackedActivity;
import com.google.analytics.tracking.android.Tracker;
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

public class Tip_camera extends TrackedActivity {

	Button go;
	CheckBox check;
	ImageView imageTip;

	private SharedPreferences newSettings;
	SharedPreferences.Editor settingsEditor;
	Tracker tracker = null;
	GoogleAnalytics ga;
	long cat = 2;
	
	private SharedPreferences developmentSession = null;
	String myId;
	int realId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tip_camera);
		EasyTracker.getInstance().setContext(getApplicationContext());
		newSettings = getSharedPreferences(ConstantValues.USER_DATA,MODE_PRIVATE);
		settingsEditor = newSettings.edit();
		go = (Button) findViewById(R.id.i_go_it);
		go.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN EL BOTON");
				EasyTracker.getTracker().trackEvent("Tip_Camera", "Click","Next", cat);				
				Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});

		check = (CheckBox) findViewById(R.id.checkBox);
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {

					settingsEditor.putBoolean("show_snack_help", false);
				} else {
					settingsEditor.putBoolean("show_snack_help", true);

				}
				settingsEditor.commit();

			}
		});
		imageTip = (ImageView) findViewById(R.id.imageTip);
		imageTip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("MAIN", "Click EN LA IMAGEN");
				EasyTracker.getTracker().trackEvent("Tip_Camera", "ImageClick", "Next", cat);
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
				finish();

			}
		});
		
		
		
		developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        myId= developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(myId);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
			EasyTracker.getInstance().activityStart(this);
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
			EasyTracker.getInstance().activityStop(this);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tip_camera, menu);
		return true;
	}
}
