package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Discoverer extends Activity {
	
	Button btn_return;
	Button backButton;
	TextView nametv;
	ImageView product;
	ImageView profile;
	Intent t;
	String code, name;
	String path;
	String profile_path;
	int id;
	Bitmap bitmap;

	private SharedPreferences developmentSession = null;
	String myId;
	int realId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discoverer);
		nametv=(TextView)findViewById(R.id.discoverer_textview_msj);
		btn_return = (Button) findViewById(R.id.discoverer_button_returnWall);
		product = (ImageView) findViewById(R.id.discoverer_product_photo);
		profile = (ImageView) findViewById(R.id.discoverer_wall_item_profile_photo);
		t = getIntent();
		path = t.getStringExtra("Product_image_path");
		backButton = (Button) findViewById(R.id.backButton);
		
		developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        myId= developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(myId);
        
        profile_path = developmentSession.getString("user_external_image_path", "");
        
        decodeFile(profile_path, true);
        decodeFile(path, false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		/* Implementation of Google Analytics for Android */
		if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
		
		name = t.getStringExtra("Product_name");
		nametv.setText(nametv.getText() + name);

		btn_return.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN btn_again");
//				-
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
		if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStop(this);
    	}
	}
	
	public void decodeFile(String filePath, boolean isProfile) {
		//Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		
		//The new size we want to scale to
		final int REQUIRED_SIZE = 1024;
		
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale;
		if(isProfile)
			scale = 1;
		else
			scale = 4; 
		
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		if(isProfile)
			profile.setImageBitmap(bitmap);
		else
			product.setImageBitmap(bitmap);
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
