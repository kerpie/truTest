package com.creatiwebs.trustripes;


import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;


public class Portada extends Activity {
	Context mClo = this; 
	Tracker myNewTracker;
	GoogleAnalytics myInstance;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portada);
		
		EasyTracker.getInstance().setContext(this);
		
		
		
	}


	  @Override
	  public void onStop() {
	    super.onStop();
	   
	   EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	  
	  
	  private final Handler handler = new Handler() {
		 
		 //pasa a siguiente activity despues de haber pasado el tiempo

	        @Override
	        public void handleMessage(final Message msg) {
	        	SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    		String Pusuario=preferences.getString("LOGIN","" );//recupera el parametro codigo con su valor
	    		
	    		if(Pusuario.equals("1")){
	    			
	    			  startActivity(new Intent(Portada.this, Principal.class));
	    		       
	    			
	    		}else {
	        	//si esta logeado de frente si no esto
	            startActivity(new Intent(Portada.this, InicioLogin.class));
	            
	    		}
	        }
	    };
	    
	    
	    
	  @Override
	    public void onStart() {
	        super.onStart();
	        
	        EasyTracker.getInstance().activityStart(this); // Add this method.
	      
	        new Thread() {

	        	//declarando tiempo el cual va a estar mostrandose la actividad
	            @Override
	            public void run() {
	                handler.sendMessageDelayed(handler.obtainMessage(), 3000);
	            };
	        }.start();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        
	    }
	    public void onDestroy() {
	        super.onDestroy();
	        
	    
	    }
	    public void onBackPressed() {
			
			return;
	    	
	    	
		}
	    @Override
		public void onConfigurationChanged(Configuration newConfig) {
		    super.onConfigurationChanged(newConfig);
		    
		    
		}
	   

}
