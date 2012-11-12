package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discoverer);
		nametv=(TextView)findViewById(R.id.discoverer_textview_product);
		btn_return = (Button) findViewById(R.id.discoverer_button_returnWall);
		t = getIntent();
		backButton = (Button) findViewById(R.id.backButton);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		EasyTracker.getInstance().activityStart(this);
		
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
		EasyTracker.getInstance().activityStop(this);
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
