package com.creatiwebs.trustripes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class PreSnackin extends Activity {
	TextView te;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_snackin);
		te = (TextView) findViewById(R.id.textView1);
		Intent t = getIntent();
		te.setText(t.getStringExtra("RESULT"));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pre_snackin, menu);
		return true;
	}

}
