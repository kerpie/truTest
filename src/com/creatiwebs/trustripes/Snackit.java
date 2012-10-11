package com.creatiwebs.trustripes;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.markupartist.android.widget.ActionBar;

import eu.erikw.R;


import Configuracion.ConfiguracionIp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Snackit extends Activity {
	ProgressDialog progressDialog;
	 public ImageLoader imageLoader;
ImageView im;
TextView tp,ns,ns2,tp3;
ActionBar actionBar;
String idproduct="",nproduct="",fotoproduc="",tot="",foc="";

LinearLayout rlayout,rlayoute ;
Context mClo = this; 
Tracker myNewTracker;
GoogleAnalytics myInstance;

	@Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.snackit);
	        
	      
	        EasyTracker.getInstance().setContext(this);
	        
	        actionBar = (ActionBar) findViewById(R.id.actionbar);
	        
	        imageLoader=new ImageLoader(getApplicationContext());
	  
	        rlayout = (LinearLayout) findViewById(R.id.lvisible);
	        rlayoute = (LinearLayout) findViewById(R.id.lvisiblee);
	        
	        //rlayout.setVisibility(View.GONE);
	    //rlayoute.setVisibility(View.INVISIBLE);
	        
	      rlayout.setVisibility(View.GONE);
	      rlayoute.setVisibility(View.GONE);
		 
	        im=(ImageView)findViewById(R.id.imgprod);
	        
	       tp=(TextView)findViewById(R.id.tproducts);
	       
	      // tp2=(TextView)findViewById(R.id.tproduct2);
	       ns=(TextView)findViewById(R.id.tnsnack);
	       tp3=(TextView)findViewById(R.id.tproducts3);
	       ns2=(TextView)findViewById(R.id.tnsnack2);
	        
	        
	        String code="";
		    Bundle extras = getIntent().getExtras(); 
	        
		    if(extras !=null) 
	        { 
	         code =extras.getString("Snack"); 
	         
	         if(code.equals("11")){
			     SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		   	     String codpro=preferences.getString("idpro","");//recupera elid de facebook
		   	     String barco=preferences.getString("barcodepro","");

				  
		   	 String   iduser=preferences.getString("iduserfinal","");
		   	 String   ema=preferences.getString("cface","");
		  
		   	  if(ema.equals("")){
		   	     //barcodepro
		   	     
		   		progressDialog=new ProgressDialog(this);
				progressDialog.setMessage("Barcode Product..");
				progressDialog.setCancelable(false);
				
				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
				

		   	  Download tarea=new Download();
		    	tarea.execute( new String[]{ip+"/ws/ws-registersnackin.php?idproduct="+codpro+"&iduser="+iduser+"&codeupean="+barco});
		   	  }else{
		   		  
		   		progressDialog=new ProgressDialog(this);
				progressDialog.setMessage("Barcode Product..");
				progressDialog.setCancelable(true);
				
				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
				

		   	  Download tarea=new Download();
		    	tarea.execute( new String[]{ip+"/ws/ws-registersnackin.php?idproduct="+codpro+"&iduser="+ema+"&codeupean="+barco});
		   		  
		   		  
		   		  
		   		  
		   		  
		   		  
		   	  }
	       
	         
	         
	         
	         }
	         
	         else if(code.equals("12")){
			    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		   	       String idpro=preferences.getString("idpros","");//recupera elid de facebook
		   	       String prod=preferences.getString("products","");
		   	       String fts=preferences.getString("fotos","");
                   String tots=preferences.getString("tots","");
                 //  String foc=preferences.getString("focs","");
                   
                   
				 rlayoute.setVisibility(View.VISIBLE);
				
			

				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
				
			   	   String rut=ip+"/ws/productphoto/"+idpro+fts;
			   	   Log.d("esta es la ruta", ""+rut);

			     tp3.setText(""+prod);
			   
			     ns2.setText(""+tots);
			    
		
			  
			    
				 imageLoader.DisplayImage(rut, im);

		   
	       
	         
	         
	         
	         }
	         
	         
	         
	         
	         
	        
	        
	}
		
		

}
	 @Override
	  public void onStop() {
	    super.onStop();
	    
		
	    Log.d("Muro", "en stop");
	 

		 System.gc();
		
	   
	   EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	 @Override
	  public void onStart() {
	    super.onStart();
	   
	   EasyTracker.getInstance().activityStop(this); // Add this method.
	  }

	public void btn_back(View v) {
		
		Intent intent = new Intent(this, Principal.class);
		startActivity(intent);
		finish();

	}

	private class Download extends AsyncTask<String,Integer,String>{

		
		@Override
		public String doInBackground(String... params) {//paso 3
			// TODO Auto-generated method stub
			
			
		
			    // Declarando parametros de conexion

				String url=params[0];
				DefaultHttpClient cliente=new DefaultHttpClient();
				HttpGet httpGet=new HttpGet(url);
				String data="";
				String html="";
				

			  	Log.d("Principal",String.valueOf(url.toString()));
				
				// Estableciendo barra de progreso al 10 %	
				publishProgress(10);
		
				
				
				try {
					
					//Extraendo respuesta de webservice	
					HttpResponse respuesta=cliente.execute(httpGet);
					
					
					// Escribiendo respuesta
			    	InputStream contenido=respuesta.getEntity().getContent();
			
					
			    	// Leyendo respuesta	
					BufferedReader reader=new BufferedReader(new InputStreamReader(contenido));
					// Estableciendo barra de progreso al 40 %
					publishProgress(40);
				
					
					
					// Asignando respuesta a variable	
					while((data=reader.readLine())!=null){
				
						html +=data;
						Log.d("Principal",String.valueOf(html.toString()));
						  
						  
					
					}
					
					reader.close();
					contenido.close();
					
					
				  	

				
					publishProgress(100);
				
					
					
					
			
				
				} catch (MalformedURLException e) {
					
					 Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
						t.show();
					
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
					t.show();
					e.printStackTrace();
				}catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
					t.show();
					e.printStackTrace();
				}	catch (Exception e) {
					// TODO Auto-generated catch block
					Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
					t.show();
					e.printStackTrace();
				} 
			
			
				
				
				
			  	JSONObject respJSON;
				String men="";
				String idpro="";
				String pro="";
				String foto="";
				
				
				
				try {
					respJSON = new JSONObject(html.toString());
					
					men = respJSON.getString("msj");
					idproduct = respJSON.getString("idproducto");
					nproduct = respJSON.getString("producto");
					fotoproduc = respJSON.getString("foto");
					tot = respJSON.getString("total");
					foc = respJSON.getString("foco");
					
				
					//rutaperfil
				    //rutaproducto
					//producto
					//nrosnack
					//dias
					
				
					
					  
					
				
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			  
			
				return men;
			
		
				
			
				
				
		}

		
		@Override
		
		protected void onPostExecute(String result) {//paso 4
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
		
		
		
			if(result.equals("1")){
			
		if(foc.equals("0")){
			
			  //System.gc();
				rlayout.setVisibility(View.VISIBLE);
			    
			ConfiguracionIp i=new ConfiguracionIp() ;
			String ip=i.ip;
			
		   	   String rut=ip+"/ws/productphoto/"+idproduct+fotoproduc;
		   	   Log.d("esta es la ruta", ""+rut);

		     tp.setText(""+nproduct);
		     //tp2.setText(""+nproduct);
		     ns.setText(""+tot);
		    
	
		     
		     
		     Log.d("Producto", ""+nproduct);
		    
			 imageLoader.DisplayImage(rut, im);
			   
			
		        
			}else if(foc.equals("1")){
				
				
				  //System.gc();
				
				 rlayoute.setVisibility(View.VISIBLE);
				
			

				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
				
			   	   String rut=ip+"/ws/productphoto/"+idproduct+fotoproduc;
			   	   Log.d("esta es la ruta", ""+rut);

			     tp3.setText(""+nproduct);
			     //tp2.setText(""+nproduct);
			     ns2.setText(""+tot);
			    
		
			     
			     
			     Log.d("Producto", ""+nproduct);
			    
				 imageLoader.DisplayImage(rut, im);
				
			}else if(foc.equals("2")){
				
				//  System.gc();
				
				rlayout.setVisibility(View.VISIBLE);
			    
				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
				
			   	   String rut=ip+"/ws/productphoto/"+idproduct+fotoproduc;
			   	   Log.d("esta es la ruta", ""+rut);

			     tp.setText(""+nproduct);
			    // tp2.setText(""+nproduct);
			     ns.setText(""+tot);
			    
		
			     
			     
			     Log.d("Producto", ""+nproduct);
			    
				 imageLoader.DisplayImage(rut, im);
				   
				
			}
			    
			}
			if(result.equals("0")){
				
				Intent intent=new Intent(getApplicationContext(),Principal.class);
				startActivity(intent);
		    	
			    
			  
			    
			}
			
		
			
		}
		//Antes de Iniciar la llamada va trayendo por background l lista de cursos 
		@Override
		protected void onPreExecute() {//paso1
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
			
			
			
			
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {//paso2
			// TODO Auto-generated method stub
			
			
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
			
			
		}
		
		
		
		
		
		
	}
	 public void onBackPressed() {
		 	onDestroy();
			Intent intent=new Intent(getApplicationContext(),Principal.class);
			startActivity(intent);
			finish();
	    	
	}
	

	 
	  @Override
	  public void onPause()
	  {
	
		  onDestroy();
	      super.onPause();

	
	  }
	  @Override
	  public void onResume()
	  {
	    super.onResume();
		  onDestroy();

	  }
	  @Override
	  public void onDestroy()
	  {   
		  super.onDestroy();
	  }

	}
