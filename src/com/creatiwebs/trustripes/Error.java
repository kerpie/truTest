package com.creatiwebs.trustripes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Error extends Activity {
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.error);
	    }
	public void onBackPressed() {
		
		
		Intent o = new Intent(); 
		o.setAction(Intent.ACTION_MAIN); 
		o.addCategory(Intent.CATEGORY_HOME); 
		o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(o); 
		android.os.Process.killProcess(android.os.Process.myPid());
		
		
		}
}
