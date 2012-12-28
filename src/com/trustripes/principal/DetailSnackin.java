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

public class DetailSnackin extends Activity {
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
		setContentView(R.layout.activity_detail_snackin);
		
		productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
        
        ambassadorPhoto = (ImageView) findViewById(R.id.postSnackin_ambassador_image);
        ambassadorName = (TextView) findViewById(R.id.postSnackin_ambassador_name);
        
        discovererPhoto = (ImageView) findViewById(R.id.postSnackin_discoverer_image);
        discovererName = (TextView) findViewById(R.id.postSnackin_discoverer_name);
        
        backButton = (Button)findViewById(R.id.backButton);
       
     /*   intent = getIntent();
        imagePath = intent.getStringExtra("PRODUCT_PATH");
        productId = intent.getStringExtra("PRODUCT_ID");        
        stringProductName = intent.getStringExtra("PRODUCT_NAME");
        ratingValue = intent.getStringExtra("PRODUCT_RANKING");
        decodeFile(imagePath, productPhoto);
        productName.setText(stringProductName);
        
        productRatingBar.setEnabled(false);
        productRatingBar.setRating(Float.parseFloat(ratingValue));
        
        productCategoryName.setVisibility(View.GONE);
        */
        
    	backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
    public void decodeFile(String filePath, ImageView image) {
 		// Decode image size
 		BitmapFactory.Options o = new BitmapFactory.Options();
 		o.inJustDecodeBounds = true;
 		BitmapFactory.decodeFile(filePath, o);

 		int scale = 1;

 		// Decode with inSampleSize
 		BitmapFactory.Options o2 = new BitmapFactory.Options();
 		o2.inSampleSize = scale;
 		bitmap = BitmapFactory.decodeFile(filePath, o2);
 		image.setImageBitmap(bitmap);
 	}

}
