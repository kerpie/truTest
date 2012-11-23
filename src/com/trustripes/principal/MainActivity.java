package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;
import com.trustripes.adapters.CustomViewPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	/* Declaration of UI widgets */
	private Button snackIn_button;
	ViewPager myPager;
	SharedPreferences session;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		/* Rescue the session variable */
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		
		/* Instantiation and event association for the snackin button */
		snackIn_button = (Button) findViewById(R.id.main_snackInButton);
		snackIn_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN EL BOTON");
				/* If the help is activated then show the help for how to snack */
				if(session.getBoolean("show_snack_help", true)){
					Intent intent = new Intent(getApplicationContext(), Tip_camera.class);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
					startActivity(intent);
				}
			}
		});
				
		/* Preparing the Custom Page Swipe Adapter */
		CustomViewPagerAdapter pagerAdapter = new CustomViewPagerAdapter(this);
		myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(pagerAdapter);
		myPager.setCurrentItem(0);
		

		 developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		 id = developmentSession.getString("user_id", "-1");
		 realId = Integer.parseInt(id);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		/* Implementation of Google Analytics for Android */
    	if(!ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		/* Implementation of Google Analytics for Android */
    	if(!ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStop(this);
    	}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		myPager.onSaveInstanceState();
		super.onSaveInstanceState(outState);
	}
	
	// For Options Menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_settings:
				logOut();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void logOut() {
		/* Get the session variable */
		/* Edit the status variable then finish the activity */
		SharedPreferences.Editor settingsEditor = session.edit();
		settingsEditor.putString("user_status", "0");
		settingsEditor.commit();
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
