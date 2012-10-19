package com.creatiwebs.trustripes;

import com.google.zxing.client.android.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ambassador extends Activity {
	Button btn_return;
	TextView idtv, nametv, codetw;
	Intent t;
	String code, name;
	int id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ambassador);
		btn_return = (Button) findViewById(R.id.ambassador_button_returnWall);
		t = getIntent();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		id = t.getIntExtra("Product_id", -1);
		name = t.getStringExtra("Product_name");
		code = t.getStringExtra("Total_snacks");

		btn_return.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN btn_again");
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_ambassador, menu);
		return true;
	}
}
