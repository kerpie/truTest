package com.creatiwebs.trustripes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Snackin extends Activity {

	TextView snackText;
	Intent t;
	String status;
	ImageView img;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackin);
        
        snackText = (TextView) findViewById(R.id.snackin_text);
        t = getIntent();
        status = t.getStringExtra("AMBASSADOR_STATUS");
        
        img = (ImageView) findViewById(R.id.ambassador_imageView);
    }
    
    
    
    @Override
    public void onStart(){
    	super.onStart();
    	switch(Integer.parseInt(status)){
    		case 0:
    			status = "No se ha convertido en Embajador";
    			img.setVisibility(View.INVISIBLE);
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
							"Product Name: "+ t.getStringExtra("PRODUCT_NAME")+"\n" + 
							"Product Photo: "+ t.getStringExtra("PRODUCT_PHOTO")+"\n" +
							"Estado del Embajador: "+ status +"\n");
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
