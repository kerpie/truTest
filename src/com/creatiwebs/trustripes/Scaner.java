package com.creatiwebs.trustripes;




import com.creatiwebs.trustripes.R;
import com.google.zxing.client.android.CaptureActivity;



import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

public class Scaner extends Activity {
	int REQUEST_SCAN; //Request code for Intent result
	String packageString = "com.creaqtiwebs.proyecto";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        
     
	
    }


	public void btn_on(View v){
    	/*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
    	intent.setPackage(packageString);
    	//Add any optional extras to pass
    	intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
    	//Launch
    	startActivityForResult(intent, REQUEST_SCAN);*/
    	
    	Intent intent = new Intent(this, CaptureActivity.class);
    	startActivityForResult(intent, 0);
    
    	 
    }
	/*public void scan(){

		IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    	
    	
		
	}*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
  	   if (requestCode == 0) {
  	      if (resultCode == RESULT_OK) {
  	         String contents = intent.getStringExtra("SCAN_RESULT");
  	       Toast.makeText(getApplicationContext(),""+contents, Toast.LENGTH_LONG).show();
		   
		    
  	         // Handle successful scan
  	      } else if (resultCode == RESULT_CANCELED) {  
  	         // Handle cancel
  	      }
  	   }
  	
    }
  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String response = data.getAction();

            if(Pattern.matches("[0-9]{1,13}", response)) {
                // response is a UPC code, fetch product meta data
                // using Google Products API, Best Buy Remix, etc.          
            } else {
                // QR codes - phone #, url, location, email, etc. 
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(response));
                startActivity(intent);
            }
        }
    } */

}
