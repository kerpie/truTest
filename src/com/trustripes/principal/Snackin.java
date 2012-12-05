package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.trustripes.Constants.ConstantValues;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Snackin extends Activity {

	TextView snackText,productName;
	ImageView img, productImage, profilePhoto;
	Button backButton;
	Button returnWall;
	RelativeLayout relativeContainer;
	RatingBar ratingBar;
	
	Intent t;
	String status, statusString, imagePath;
	String profileImagePath;
	
	Bitmap bitmap;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
	String ratingValue;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_snackin);
        
        snackText = (TextView) findViewById(R.id.snackin_text);
        productName = (TextView) findViewById(R.id.snackin_product_name);
        t = getIntent();
        status = t.getStringExtra("AMBASSADOR_STATUS");
        imagePath = t.getStringExtra("productPath");
        backButton = (Button) findViewById(R.id.backButton);
        returnWall = (Button) findViewById(R.id.button_return_wall);
        img = (ImageView) findViewById(R.id.ambassador_imageView);
        productImage = (ImageView) findViewById(R.id.product_photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_snackin_activity);
        profilePhoto = (ImageView) findViewById(R.id.wall_item_profile_photo);
        relativeContainer = (RelativeLayout)findViewById(R.id.ambassador_content);
        
        backButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        profileImagePath = Environment.getExternalStorageDirectory()+"/TruStripes/profileImage.png";
        
        decodeFile(profileImagePath, true);
        returnWall.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                    finish();               
	            }
	    });
        
        ratingValue = t.getStringExtra("RATING");

        ratingBar.setEnabled(false);

        ratingBar.setRating(Float.parseFloat(ratingValue));
        
        Toast.makeText(getApplicationContext(), ratingValue, Toast.LENGTH_SHORT).show();
        
        developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(id);
        
    }
       
    public void decodeFile(String filePath, boolean isProfile) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		if(isProfile)
			profilePhoto.setImageBitmap(bitmap);
		else
			productImage.setImageBitmap(bitmap);
	}
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    	EasyTracker.getInstance().activityStart(this);
    	
    	switch(Integer.parseInt(status)){
    		case 0:
    			statusString = "No se ha convertido en Embajador";
    			relativeContainer.setVisibility(View.GONE);
    			break;
    		case 1:
    			statusString = "Te has convertido en Embajadador";
    			relativeContainer.setVisibility(View.VISIBLE);
    			break;
    		case 2:
    			statusString = "Sigues siendo embajador";
    			relativeContainer.setVisibility(View.VISIBLE);
    			break;
        }
    	
    	decodeFile(imagePath, false);
    	productName.setText(t.getStringExtra("PRODUCT_NAME"));
    	snackText.setText("Barcode: "+t.getStringExtra("BARCODE"));
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_snackin, menu);
        return true;
    }

    @Override
	public void onBackPressed() {
		finish();
	}

}
