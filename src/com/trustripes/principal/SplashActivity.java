package com.trustripes.principal;

import com.google.analytics.tracking.android.EasyTracker;
import com.trustripes.Constants.ConstantValues;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	/* Variable for Internal Debug */
	private static final String TAG = "SplashActivity";
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	/* Hide title in app */
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	/* Setting view */
        setContentView(R.layout.splash_activity);
        
        developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(id);
        /* Start SplasScreen in another thread and check for previous data */
        new SplashScreen().execute();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();    	
    	
    	/* Implementation of Google Analytics for Android */
    	if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	/* Implementation of Google Analytics for Android */
    	if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStop(this);
    	}
    }
    
    /* Class to check data in background */
    public class SplashScreen extends AsyncTask<Void, Integer, Void>{
    	
    	/* Previous status to check before starting */
    	private boolean connection_status = false;
    	private boolean session_status = false;
    	
    	/* Internal results to Toast status */
    	/* Erase for production */
    	private final int CONNECTION_SUCCESS = 200;
    	private final int CONNECTION_FAIL = 300;
    	private final int SESSION_OPEN = 400;
    	private final int SESSION_CLOSED = 500;
    	
    	/* Background work */
		@Override
		protected Void doInBackground(Void... params) {
			
			 /* Check Connection Status */
	        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        if(wifi.isAvailable() || mobile.isAvailable()){
	        	/* There is connection to transfer data */
	        	publishProgress(CONNECTION_SUCCESS);
	        	connection_status = true;
	        }
	        else{
	        	/* There isn't connection */ 
	        	publishProgress(CONNECTION_FAIL);
	        	connection_status = false;
	        }

	        /* Check for open session */
	        SharedPreferences session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
	        if (Integer.parseInt(session.getString("user_status", "0")) == 1){
	        	/* Open Session */
	        	session_status = true;
	        	publishProgress(SESSION_OPEN);
	        }
	        else{
	        	/* No previous session */
	        	session_status = false;
	        	publishProgress(SESSION_CLOSED);
	        }
	        /* Dummy sleep to keep activity running */
	        try  
            {  
	        	/* Get the current thread's token */  
                synchronized (this)  
                {  
                    /* Initialize an integer (that will act as a counter) to zero */  
                    int counter = 0;  
                    /* While the counter is smaller than four */  
                    while(counter <= 4)  
                    {  
                        /* Wait 600 milliseconds */  
                        this.wait(600);  
                        /* Increment the counter */  
                        counter++;  
                        /* Set the current progress.  
                           This value is going to be passed to the onProgressUpdate() method. */  
                    }  
                }  
            }  
            catch (InterruptedException e)  
            {  
                e.printStackTrace();  
            }
	        /* Check any other kind of data */
	        return null;
		}
		
		/* Show Toast for Debugging */
		/* Erase for production */
		@Override
		protected void onProgressUpdate(Integer... values) {
			switch(values[0]){
				case CONNECTION_SUCCESS: 
//					Toast.makeText(getApplicationContext(), "Hay Conexion", Toast.LENGTH_SHORT).show();
					break;
				case CONNECTION_FAIL:
//					Toast.makeText(getApplicationContext(), "No Hay Conexion", Toast.LENGTH_SHORT).show();
					break;
				case SESSION_OPEN:
//					Toast.makeText(getApplicationContext(), "Sesion abierta", Toast.LENGTH_SHORT).show();
					break;
				case SESSION_CLOSED:
//					Toast.makeText(getApplicationContext(), "No hay Sesion", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(connection_status){
	        	/*If there is an active session: load data*/
	        	if(session_status){
	        		/* load saved session */
	        		Intent showWallActivity = new Intent(getApplicationContext(), MainActivity.class);
	        		startActivity(showWallActivity);
	        	}
	        	else{
	        		/* if not show Login form */
	        		Intent showLoginForm = new Intent(getApplicationContext(), LoginActivity.class);
	        		startActivity(showLoginForm);
	        	}
	        }
	        else{
	        	Toast.makeText(getApplicationContext(), "Please check your connection and try again", Toast.LENGTH_SHORT).show();
	        }
			finish();
		}  	
    }

    /* Override back button behavior */
    @Override
    public void onBackPressed() {
    	return;
    }
}
