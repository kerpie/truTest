package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
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
import android.widget.TextView;

public class Discoverer extends Activity {
	Button btn_return;
	Button backButton;
	TextView nametv;
	Intent t;
	String code, name;
	int id;

	private SharedPreferences developmentSession = null;
	String myId;
	int realId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discoverer);
		nametv=(TextView)findViewById(R.id.discoverer_textview_product);
		btn_return = (Button) findViewById(R.id.discoverer_button_returnWall);
		t = getIntent();
		backButton = (Button) findViewById(R.id.backButton);
		
		developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        myId= developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(myId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		/* Implementation of Google Analytics for Android */
    	if(!ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
		
		name = t.getStringExtra("Product_name");
		nametv.setText(name);

		btn_return.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN btn_again");
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		backButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
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
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_discoverer, menu);
		return true;
	}
}
