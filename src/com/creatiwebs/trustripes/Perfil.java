package com.creatiwebs.trustripes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import packete.datas.LoginData;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import com.markupartist.android.widget.ActionBar;

import Configuracion.ConfiguracionIp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Perfil extends Activity {
	TextView nom;
	ImageView foto;
	String img_url="";
	Bitmap bm;
	String iduser="";
	String nuser="";
	String nuserlo="",iduse="",nomuse="",eslo="";
   public ImageLoader imageLoader; 
   public static Perfil self;
	ProgressDialog progressDialog;

	Context mClo = this; 
    Tracker myNewTracker;
	GoogleAnalytics myInstance;
	
	@SuppressLint("NewApi")
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.perfil);
	        
	        self = this;
	        imageLoader=new ImageLoader(getApplicationContext());
	        
	        EasyTracker.getInstance().setContext(this);
			
	   
	       //	StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        //StrictMode.setThreadPolicy(policy);
	        
	        nom=(TextView)findViewById(R.id.nombrep);
		    foto=(ImageView)findViewById(R.id.imagenperfil);
		    
		    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	   	     iduser=preferences.getString("idface","");//recupera elid de facebook
	    	 nuser=preferences.getString("nface","");	//recuperta el nombre de facebook
			 nuserlo=preferences.getString("nom","");//recupera el id de usuario
			 iduse= preferences.getString("idus", "");
			 nomuse=preferences.getString("username", "");
			 eslo= preferences.getString("louser","");
		
			 if(isOnline2()==false){
					
				 
					
					
					
					Intent intent=new Intent(getApplicationContext(),Error.class);
					startActivity(intent);
					
				
				}else{
				
		 	
	    	progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("Charge Profile..");
			progressDialog.setCancelable(false);
			
			
		    Download tarea=new Download();
		    tarea.execute();
		    
				}
			
	    }


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

	public boolean isOnline2() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		 
		    return true;
		    
		}
		
	 
		
		
		return false;
		}
	
	public void rodar(ActionBar  ac){
		
		ac.setProgressBarVisibility(View.GONE);
		
	}
	

	private class Download extends AsyncTask<String,Integer,String>{

		
		@Override
		public String doInBackground(String... params) {//paso 3
			// TODO Auto-generated method stub
			
			
				publishProgress(10);
				Log.d("Perfil", "si llega");
				String resp="";
				
				 
				 Log.d("Perfil", iduser);
				 Log.d("Perfil", nuser);
				 Log.d("Perfil", nuserlo);
				 
				 Log.d("Perfil", nuserlo);
				 Log.d("Perfil", iduse);
				 Log.d("Perfil", nomuse);
				 Log.d("Perfil", eslo);
				
				publishProgress(30);
				try {
					publishProgress(50);
					if(eslo.equals("no")){
						
					
				    	resp="1";
				        
					  
				    
					}else if(eslo.equals("si")){
					resp="2";
			
						
					}
			
									
					publishProgress(100);
			
		        
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
					t.show();
					e.printStackTrace();
					
					 
				} 
			
				Log.d("Perfil", "si llega");
			
		
			
		
				return resp;
				}
			
		

		
		@Override
		
		protected void onPostExecute(String result) {//paso 4
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			if(result.equals("1")){
			  nom.setText(String.valueOf(nuser));
			    
			
			
		    
				
			}else{
				
				 	nom.setText(String.valueOf(nomuse));
				    
				    nom.setVisibility((View.VISIBLE));
				    
				
				
				
			}
			   
			  
			
			
		}
		//Antes de Iniciar la llamada va trayendo por background l lista de cursos 
		@Override
		protected void onPreExecute() {//paso1
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			

			if(eslo.equals("no")){
				
				
				 	img_url="https://graph.facebook.com/"+iduser+"/picture&type=large";
				    
				    Log.d("Perfil", img_url);
				    
				 
				    imageLoader.DisplayImage(img_url, foto);
		        
			  
		    
			}else if((eslo.equals("si"))){
				
				//seteo imagen
				
				
			}

	
		}
		
		//Cuando se esta ejecutanto la llamada el progreesbar va tomando valores

		@Override
		protected void onProgressUpdate(Integer... values) {//paso2
			// TODO Auto-generated method stub
			
			
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
			
			
		}
		@Override
		protected void onCancelled() {
		
			
		}
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
