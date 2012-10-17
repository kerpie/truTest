package com.creatiwebs.trustripes;

import com.creatiwebs.Constants.ConstantValues;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);
        new SplashScreen().execute();
    }
    
    public class SplashScreen extends AsyncTask<Void, Integer, Void>{

    	private boolean connection_status = false;
    	private boolean session_status = false;
    	
    	private final int CONNECTION_SUCCESS = 200;
    	private final int CONNECTION_FAIL = 300;
    	
    	private final int SESSION_OPEN = 400;
    	private final int SESSION_CLOSED = 500;
    	
		@Override
		protected Void doInBackground(Void... params) {
			 /* Check Connection Status */
	        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        if(wifi.isAvailable() || mobile.isAvailable()){
	        	publishProgress(CONNECTION_SUCCESS);
	        	connection_status = true;
	        }
	        else{
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
	        	session_status = false;
	        	publishProgress(SESSION_CLOSED);
	        }
	        try  
            {  
                //Get the current thread's token  
                synchronized (this)  
                {  
                    //Initialize an integer (that will act as a counter) to zero  
                    int counter = 0;  
                    //While the counter is smaller than four  
                    while(counter <= 4)  
                    {  
                        //Wait 850 milliseconds  
                        this.wait(850);  
                        //Increment the counter  
                        counter++;  
                        //Set the current progress.  
                        //This value is going to be passed to the onProgressUpdate() method.  
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
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			switch(values[0]){
				case CONNECTION_SUCCESS: 
					Toast.makeText(getApplicationContext(), "Hay Conexion", Toast.LENGTH_SHORT).show();
					break;
				case CONNECTION_FAIL:
					Toast.makeText(getApplicationContext(), "No Hay Conexion", Toast.LENGTH_SHORT).show();
					break;
				case SESSION_OPEN:
					Toast.makeText(getApplicationContext(), "Sesion abierta", Toast.LENGTH_SHORT).show();
					break;
				case SESSION_CLOSED:
					Toast.makeText(getApplicationContext(), "No hay Sesion", Toast.LENGTH_SHORT).show();
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
	        	/* Load the crash activity announcing there is no connection and gracefully exits the application */
	        }
			finish();
		}
    	
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	return;
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash_activity, menu);
        return true;
    }
}
