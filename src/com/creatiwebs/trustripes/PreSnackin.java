package com.creatiwebs.trustripes;

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

public class PreSnackin extends Activity {
	
	/* Declaration of UI widgets */
	TextView te;
	Button snackAgainButton, realSnackInButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pre_snackin);
		
		/* Instantiation of UI widgets */
		snackAgainButton = (Button) findViewById(R.id.presnack_button_again);
		realSnackInButton=(Button)findViewById(R.id.presnack_button_Snack);
		
		/* Instantiation and button event association */
		snackAgainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN snackAgainButton");
				Intent intent = new Intent(getApplicationContext(),
						CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		realSnackInButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pre_snackin, menu);
		return true;
	}
	
	
	

}
