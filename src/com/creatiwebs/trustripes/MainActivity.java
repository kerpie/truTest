package com.creatiwebs.trustripes;

import com.creatiwebs.trustripes.adapters.CustomViewPagerAdapter;
import com.google.zxing.client.android.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	/* Declaration of UI widgets */
	private Button snackIn_button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		/* Instantiation and event association */
		snackIn_button = (Button) findViewById(R.id.main_snackInButton);
		snackIn_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN EL BOTON");
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		/* Preparing the Custom Page Swipe Adapter */
		CustomViewPagerAdapter pagerAdapter = new CustomViewPagerAdapter();
		ViewPager myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(pagerAdapter);
		myPager.setCurrentItem(0);
	}

	// For Options Menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
