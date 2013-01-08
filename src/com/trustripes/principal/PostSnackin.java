package com.trustripes.principal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PostSnackin extends Activity {
	Button backButton;
	ImageView productPhoto;
	TextView productName;
	TextView productCategoryName;
	RatingBar productRatingBar;
	ImageView ambassadorPhoto;
	TextView ambassadorName;	
	ImageView discovererPhoto;
	TextView discovererName;	
	String imagePath;
	String productId;
	String stringProductName;
	String ratingValue;
	Intent intent;	
	Bitmap bitmap;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_snackin);
		
		productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
        
        ambassadorPhoto = (ImageView) findViewById(R.id.postSnackin_ambassador_image);
        ambassadorName = (TextView) findViewById(R.id.postSnackin_ambassador_name);
        
        discovererPhoto = (ImageView) findViewById(R.id.postSnackin_discoverer_image);
        discovererName = (TextView) findViewById(R.id.postSnackin_discoverer_name);
        
        backButton = (Button)findViewById(R.id.backButton);
        
    	backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

}
