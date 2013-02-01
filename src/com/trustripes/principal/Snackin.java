package com.trustripes.principal;

import lazylist.ImageLoader;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.TrackedActivity;
import com.google.analytics.tracking.android.Tracker;
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

public class Snackin extends TrackedActivity {

	TextView snackText,productName;
	ImageView img, productImage, profilePhoto;
	Button backButton;
	Button toPostSnackin;
	RelativeLayout relativeContainer, background;
	RatingBar ratingBar = null;
	
	Intent t;
	String status, statusString, imagePath, productId, snackinId;
	String profileImagePath, stringProductName;
	
	Bitmap bitmap;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
	
	String ratingValue = null;
	ImageLoader imageLoader;
	
	boolean isAnotherPhoto;
	long cat =1;
	long cate = 3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(id);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_snackin);
        
        
        EasyTracker.getInstance().setContext(getApplicationContext());
       	
                
        snackText = (TextView) findViewById(R.id.snackin_text);
        productName = (TextView) findViewById(R.id.snackin_product_name);
        t = getIntent();
        status = t.getStringExtra("AMBASSADOR_STATUS");
        productId = t.getStringExtra("PRODUCT_ID");
        imagePath = t.getStringExtra("productPath");
        stringProductName = t.getStringExtra("PRODUCT_NAME");
        snackinId = t.getStringExtra("SNACKIN_ID");
        isAnotherPhoto = t.getBooleanExtra("IS_ANOTHER_PHOTO",false);
        
        backButton = (Button) findViewById(R.id.backButton);
        toPostSnackin = (Button) findViewById(R.id.button_return_wall);
        img = (ImageView) findViewById(R.id.ambassador_imageView);
        productImage = (ImageView) findViewById(R.id.product_photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_snackin_activity);
        profilePhoto = (ImageView) findViewById(R.id.wall_item_profile_photo);
        relativeContainer = (RelativeLayout)findViewById(R.id.ambassador_content);
        background = (RelativeLayout)findViewById(R.id.Background_snackin);
        
        backButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        profileImagePath = Environment.getExternalStorageDirectory()+"/TruStripes/"+ConstantValues.codeName(realId)+".jpg";
               
        ratingValue = t.getStringExtra("RATING");
        ratingBar.setEnabled(false);
        ratingBar.setRating(Float.parseFloat(ratingValue));
        
        toPostSnackin.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(Snackin.this, PostSnackin.class);
	            	intent.putExtra("PRODUCT_PATH", imagePath);
	            	intent.putExtra("PRODUCT_ID", productId);
	            	intent.putExtra("PRODUCT_NAME", stringProductName);
	            	intent.putExtra("PRODUCT_RANKING", ratingValue);
	            	intent.putExtra("SNACKIN_ID", snackinId);
	            	intent.putExtra("IS_ANOTHER_PHOTO", isAnotherPhoto);
	            	startActivity(intent);
	            	finish();               
	            }
	    });
    }
       
	public void decodeFile(String filePath, int requiredHeight, int requiredWidth, boolean isProfile) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath,o);
		int scale = 0;
		if( o.outHeight > requiredHeight || o.outWidth > requiredWidth ){
			if(o.outWidth > o.outHeight){
				scale = Math.round(Math.round((float)o.outHeight/(float)o.outWidth));
			}else{
				scale = Math.round(Math.round((float)o.outWidth/(float)o.outHeight));
			}
		}
		
		o = new BitmapFactory.Options();
		o.inSampleSize = scale;
		o.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, o);		
		if(isProfile)
			profilePhoto.setImageBitmap(bitmap);
		else
			productImage.setImageBitmap(bitmap);
	}
    
    public void decodeFile(String filePath, boolean isProfile) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

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
    	if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
    		EasyTracker.getInstance().activityStart(this);
    		EasyTracker.getTracker().trackEvent("ContSnackin", "Click_Continue","Post_Snackin", cat);
    	}
    	switch(Integer.parseInt(status)){
    		case 0:
    			statusString = "No se ha convertido en Embajador";
    			relativeContainer.setVisibility(View.GONE);
    			break;
    		case 1:
    			statusString = "Te has convertido en Embajadador";
    			relativeContainer.setVisibility(View.VISIBLE);
    			EasyTracker.getTracker().trackEvent("ContAmbassador", "Ambassador","Post_Snackin", cate);
    			//background.setBackgroundResource(R.drawable.backambassador);
    			break;
    		case 2:
    			statusString = "Sigues siendo embajador";
    			relativeContainer.setVisibility(View.GONE);
    			break;
        }
    	
    	if(isAnotherPhoto)
    		decodeFile(imagePath, 100,100, false);
    	else{
    		imageLoader = new ImageLoader(getApplicationContext(), 16);
    		imageLoader.DisplayImage(imagePath, productImage, false, false);
    	}
    	decodeFile(profileImagePath, 100,100,true);
    	
    	productName.setText("Snacked in a "+t.getStringExtra("PRODUCT_NAME"));
    	snackText.setText("Barcode: "+t.getStringExtra("BARCODE"));
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
    		EasyTracker.getInstance().activityStop(this);
    	}
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
