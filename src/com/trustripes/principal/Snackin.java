package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Snackin extends Activity {

	TextView snackText;
	Intent t;
	String status;
	ImageView img;
	Button backButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_snackin);
        
        snackText = (TextView) findViewById(R.id.snackin_text);
        t = getIntent();
        status = t.getStringExtra("AMBASSADOR_STATUS");
        
        backButton = (Button) findViewById(R.id.backButton);
        img = (ImageView) findViewById(R.id.ambassador_imageView);
        
        backButton.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    	EasyTracker.getInstance().activityStart(this);
    	
    	switch(Integer.parseInt(status)){
    		case 0:
    			status = "No se ha convertido en Embajador";
    			img.setVisibility(View.GONE);
    			break;
    		case 1:
    			status = "Te has convertido en Embajadador";
    			img.setVisibility(View.VISIBLE);
    			break;
    		case 2:
    			status = "Sigues siendo embajador";
    			img.setVisibility(View.VISIBLE);
    			break;
        }
    	
    	snackText.setText(	"Barcode: "+ t.getStringExtra("BARCODE")+"\n" +
							"Product Name: "+ t.getStringExtra("PRODUCT_NAME")+"\n");
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
