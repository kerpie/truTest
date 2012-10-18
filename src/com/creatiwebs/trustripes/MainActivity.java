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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView profile_textView = null;
	private Button go_cam;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		go_cam = (Button) findViewById(R.id.main_snackInButton);

		go_cam.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN EL BOTON");
				Intent intent = new Intent(getApplicationContext(),
						CaptureActivity.class);
				startActivity(intent);
			}
		});

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
