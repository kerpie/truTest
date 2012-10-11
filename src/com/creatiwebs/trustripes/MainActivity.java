package com.creatiwebs.trustripes;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {

	private ViewSwitcher viewSwitcher;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        //Create a new instance of the SplashScreen(AsyncTask) to run in background 
        new SplashScreen().execute();
    }

    private class SplashScreen extends AsyncTask<Void, Integer, Void>{
    	
    	//Instantiate the layout widgets
    	TextView loadingText = null;
    	ProgressBar progressBar = null;
    	
    	private final int CONNECTION_SUCCESS = 100;
    	private final int CONNECTION_FAILED = 200;
    	
    	@Override
    	protected void onPreExecute() {
    		 		
    		/*
    		 * Switching views with ViewSwitcher
    		
    		//Create the ViewSwitcher that hold the SplashScreen and the MainActivity 
    		viewSwitcher = new ViewSwitcher(MainActivity.this);
    		//Add the SplashScreen
    		viewSwitcher.addView(ViewSwitcher.inflate(MainActivity.this, R.layout.splash_screen, null));
    		//Instantiate the widgets in the splashscreen
    		loadingText = (TextView) viewSwitcher.findViewById(R.id.splashscreen_loadingText); 
    		progressBar = (ProgressBar) viewSwitcher.findViewById(R.id.splashscreen_progressBar);
    		//Set Limit of the progress bar to 100
    		progressBar.setMax(100);
    		//Set the viewSwitcher as the layout to be shown
    		setContentView(viewSwitcher);
    		
    		*/
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			/* You can erase from here and nothing bad will happen */
			try{
				synchronized (this){
					int counter = 0;
					while(counter < 4){
						if(counter == 3){
							/* You can erase until here and nothing bad will happen*/
							
							/* This is where any previous data is checked before running the app */

							//Check the availability of the connections
							ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					    	NetworkInfo wifi = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					    	NetworkInfo mobile = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					    	//Check if it is available
					    	if(wifi.isAvailable() || mobile.isAvailable()){
					    		//Launch a message to be shown if available
					    		publishProgress(CONNECTION_SUCCESS);
					    	}
					    	else{
					    		//Launch a message to be shown if not available
					    		publishProgress(CONNECTION_FAILED);
					    	}
					    	//Add any previous check you need to do before start the app here

					    	/*You can erase from here and nothing bad will happen*/
						}
						this.wait(850);
						counter++;
						publishProgress(counter*25);
					}
				}
			} 
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			/* You can erase until here and nothing bad will happen */
			/* REMINDER: REMEMBER TO MANAGE THE GROWTH OF THE PROGRESS BAR*/ 
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			switch(values[0]){
				case CONNECTION_SUCCESS:
					Toast.makeText(getApplicationContext(), "Hay conexion", Toast.LENGTH_SHORT).show();
					//REMINDER: If progressBar is a bar and not a circle increase the size of the loading
					break;
				case CONNECTION_FAILED:
					Toast.makeText(getApplicationContext(), "No hay conexion", Toast.LENGTH_SHORT).show();
					//If connection fails, load the application with dummy data
					break;
				default:
					loadingText.setText("Progress: " + Integer.toString(values[0]) + "%");
					progressBar.setProgress(values[0]);
					break;
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			//Adding the MainActivity to show after the loading 
			viewSwitcher.addView(ViewSwitcher.inflate(MainActivity.this, R.layout.activity_main, null));
			viewSwitcher.showNext();
		}
    	
    }
    
    @Override
    public void onBackPressed() {
    	//Block the back key if screen is showing the load screen
    	if(viewSwitcher.getDisplayedChild() == 0){
    		return;
    	}
    	else{
    		super.onBackPressed();
    	}
    }
      
    //For Options Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
