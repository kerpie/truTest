package com.creatiwebs.trustripes;








import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.zxing.client.android.CaptureActivity;
import com.markupartist.android.widget.ActionBar;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class Principal extends TabActivity {
		String dv="";
		String na="";
		  public static Principal pri;
		  ActionBar actionBar;
		 MemoryCache m;
		 
	
		   public ImageLoader imageLoader; 
		Context mClo = this; 
	    Tracker myNewTracker;
		GoogleAnalytics myInstance;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		
		

        imageLoader=new ImageLoader(getApplicationContext());
        m=new MemoryCache();
        
        //imageLoader.clearCache();
			EasyTracker.getInstance().setContext(this);
			
			
			
		   pri= this;
          actionBar = (ActionBar) findViewById(R.id.actionbar);
        //actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));

        
		
		

        //final Action shareAction = new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default);
       // actionBar.addAction(shareAction);
        //final Action otherAction = new IntentAction(this, new Intent(this, OtherActivity.class), R.drawable.ic_title_export_default);
        //actionBar.addAction(otherAction);
        
     
		
		
        TabHost tabHost = getTabHost();
        
        // Tab for Photos
 
        
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Wall");
        // setting Title and Icon for the Tab
        songspec.setIndicator("Wall",null);
        Intent songsIntent = new Intent(this,Muro.class);
     
        songspec.setContent(songsIntent);
        
        TabSpec photospec = tabHost.newTabSpec("My Profile");
        photospec.setIndicator("My Profile",null);
        Intent photosIntent = new Intent(this, Perfil.class);
      
        photospec.setContent(photosIntent);
        
        // Tab for Videos
      /* TabSpec videospec = tabHost.newTabSpec("Explore");
        videospec.setIndicator("Explore",null);
        Intent videosIntent = new Intent(this,Explorar.class);
        videospec.setContent(videosIntent);*/
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(songspec); // 
        tabHost.addTab(photospec); // Adding photos tab
   // Adding songs tab
       // tabHost.addTab(videospec); // Adding videos
        
     
      getTabHost().setOnTabChangedListener(new OnTabChangeListener() {

        
        	public void onTabChanged(String tabId) {

        	int i = getTabHost().getCurrentTab();
        	
        	 Log.d("@@@@@@@@ ANN CLICK TAB NUMBER", "------" + i);

        	    if (i ==1) {
        	    	
        	    	
        	    	 //Perfil.self.rodar(actionBar);
        	    	
        	    
        	    	
        	            
        	    }

        	  }
        	});
		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//para cear las opcines del menu
		// TODO Auto-generated method stub
		//return super.onCreateOptionsMenu(menu);
		
		
		menu.setGroupVisible(R.id.grupo1 , true);
		
		
		MenuInflater menuinflater=getMenuInflater();
		menuinflater.inflate(R.menu.activity_main,menu);
	
	//menu.setGroupEnabled(R.id.mnuAgregar ,false);
				//menu.setGroupVisible(R.id.grupo1 , false);
			
	
	    	return true;
	    }
	    //  EasyTracker.getInstance().activityStart(this); // Add this method.
		
	 @Override
	  public void onStop() {
	    super.onStop();
	   
	   EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	 @Override
	  public void onStart() {
	    super.onStart();
	   
	   EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//para cuando damos clik en una de las opciones del menu
		// TODO Auto-generated method stub
		
		
		switch (item.getItemId()) {
		
	
			
		case R.id.mnucerrar:
			
			
			Intent i=new Intent(this,InicioLogin.class);
			 startActivity(i);	
		  	
			  
			    
			Toast.makeText(this, "Bye!", Toast.LENGTH_SHORT).show();
			    
		//manda a login 
	    	
	    	break;	
			
		
		default:
			break;
		}
		
		
		
		return super.onOptionsItemSelected(item);
	}
	
	public void onBackPressed() {
		
		
		Intent o = new Intent(); 
    	o.setAction(Intent.ACTION_MAIN); 
    	o.addCategory(Intent.CATEGORY_HOME); 
    	o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    	startActivity(o); 
    	android.os.Process.killProcess(android.os.Process.myPid());
    	
    	
		}


	
	
	public void btn_on(View v){
		
       // imageLoader.clearCache();
		// unbindDrawables(findViewById(R.id.conmuro));
		 //   System.gc();
		
		Intent intent = new Intent(this, CaptureActivity.class);
    	startActivity(intent);
    
		
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	 
	   // unbindDrawables(findViewById(R.id.conmuro));
	   // System.gc();
	}
	 
	private void unbindDrawables(View view) {
	    if (view.getBackground() != null) {
	        view.getBackground().setCallback(null);
	    }
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            unbindDrawables(((ViewGroup) view).getChildAt(i));
	        }
	        ((ViewGroup) view).removeAllViews();
	    }
	}
}
