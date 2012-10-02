package com.creatiwebs.trustripes;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import Configuracion.ConfiguracionIp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import eu.erikw.R;

public class Muro extends Activity {
	private PullToRefreshListView listView;
	private  ArrayList<UserRecord> users;
	  ProgressDialog progressDialog;
   
	   public ImageLoader imageLoader; 
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.muro);
	        users = new ArrayList<UserRecord>();
	        

	  		
		     
	        imageLoader=new ImageLoader(getApplicationContext());
	       // imageLoader.clearCache();
	        

	        if(isOnline2()==false){
	        		
	        		 
	        		
	        		
	        		
	        		Intent intent=new Intent(getApplicationContext(),Error.class);
	        		startActivity(intent);
	        		
	        	
	        	}else{
	    
			progressDialog=new ProgressDialog(this);
	  		progressDialog.setMessage("Charge Wall..");
	  		progressDialog.setCancelable(false);
	  	
	        
		  	   Download tarea=new Download();
		  	   

				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;


		  		tarea.execute( new String[]{ip+"/ws/ws-listproduct.php"});

	  		
	     
	        
	        
        listView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        //texto2=(TextView)findViewById(R.id.t2);
        // Set the onRefreshListener on the list. You could also use
        // listView.setOnRefreshListener(this); and let this Activity
        // implement OnRefreshListener.
        listView.setOnRefreshListener(new OnRefreshListener() {
	
			public void onRefresh() {//cada dos minutos se ejecuta este metodo que esta dentro de esto
				// Your code to refresh the list contents goes here
				//imageLoader.clearCache();
				
				users.clear();
				
				//consulta de nuevo pero sin progresss
				
				Download2 tarea2=new Download2();

				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;

		  		tarea2.execute( new String[]{ip+"/ws/ws-listproduct.php"});
				
		
			  
			    
		        
	 
				
				
				listView.postDelayed(new Runnable() {
					
					public void run() {
						listView.onRefreshComplete();
					}
				}, 3000);
			}
		});
        //aki settep de fprma normal
        
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
       //new UserItemAdapter(this, android.R.layout.simple_list_item_1, users)
        //listView.setAdapter(adapter);
    // listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, users));
	        	}
	    }
	public boolean isOnline2() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		 
		    return true;
		    
		}
		
	 
		
		
		return false;
		}
	
	
	
	public class UserRecord {
		@SerializedName("idproduct")
		public String idproduct;
		
		@SerializedName("idsnackin")
		public String idsnackin;
		
		@SerializedName("productname")
		public String productname;
		
		@SerializedName("photo")
		public String photo;
		
		@SerializedName("username")
		public String username;
		
		
		public UserRecord(String idproduct, String idsnackin,String productname,String photo,String username) {
			this.idproduct = idproduct;
			this.idsnackin= idsnackin;
			this.productname= productname;
			this.photo= photo;
			this.username= username;
			
		}
	}
	
	
	public class UserItemAdapter extends ArrayAdapter<UserRecord> {
		private ArrayList<UserRecord> users;

		public UserItemAdapter(Context context, int textViewResourceId, ArrayList<UserRecord> users) {
			super(context, textViewResourceId, users);
			this.users = users;
		}
		 public boolean isEnabled(int position) 
		    { 
		            return false; 
		    } 
		 
	
		// private View mLastItem; 
		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			  View v = convertView;
		
			  UserRecord user = users.get(position);
			
		

				
					if (v == null) {
		
						LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						 //if (position == lastpos) {
						v = vi.inflate(R.layout.con_muro, null);
				    
					}
					
					
				
					if (user != null) {
						
						
							
							
						TextView username = (TextView) v.findViewById(R.id.username);
						TextView email = (TextView) v.findViewById(R.id.email);
						ImageView person=(ImageView)v.findViewById(R.id.idperson);
						ImageView product=(ImageView)v.findViewById(R.id.idproduct);
						
		
						if (username != null) {
							username.setText(user.username);
						}
		
						if(email != null) {
							email.setText("" + user.productname );
						}
						if(person != null) {
							   //imageLoader.DisplayImage(user.imgperfil, person);
							
						  
						
						}
						if(product!= null) {
							
							ConfiguracionIp i=new ConfiguracionIp() ;
							String ip=i.ip;
							
							  imageLoader.DisplayImage(ip+"/ws/productphoto/"+user.idproduct+user.photo, product);
							

					   
						}
						
					}
						
						return v;
				
		
		
		}
		
		
		
		
		
		
	}
	  private class Download extends AsyncTask<String,Integer,String>{//paso 3

  		
  		@Override
  		public String doInBackground(String... params) {
  			// TODO Auto-generated method stub
  			
  			
  		
  			//declaracoion de variables de conexion
  			
  				String url=params[0];
  				DefaultHttpClient cliente=new DefaultHttpClient();
  				HttpGet httpGet=new HttpGet(url);
  				String data="";
  				String html="";
  			
  		
  				
 			       
  				//progreesbar al 10%
  				publishProgress(10);
  	
  				
  				try {
  					
  					//trayendop y leyendo respuesta
  					HttpResponse respuesta=cliente.execute(httpGet);
  				
  					InputStream contenido=respuesta.getEntity().getContent();
  			
  					BufferedReader reader=new BufferedReader(new InputStreamReader(contenido));
  				
  					publishProgress(40);
  				
  					while((data=reader.readLine())!=null){
  				
  						html +=data;
  						
  					
  					}
  					
  					reader.close();
  					contenido.close();
  					//progreebar al 100%
  					publishProgress(100);
  					
  					
  					//declaracion de excepciones
  				} catch (MalformedURLException e) {
  					// TODO Auto-generated catch block
  					
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
  			
  			

  			
  				
  				Gson gson=new Gson();
  			    
  			
  			
  			
  				UserRecord[]  login=gson.fromJson(html.toString(),UserRecord[].class);//login =new login(en jason)
  		
  				
  				
  				//si la espuesta esta vacia asigna variable
	    			
  				if(login==null){
  					String v="Error";
  					return v;
  				
  				
  				             
  				}else{      
  				
  					
  					//llenando array de preguntas
  					
  			for(int i=0;i<login.length;i++){    
  				
  				UserRecord lo=new UserRecord("","","","","");	
  			 
  				
  				lo.idproduct=login[i].idproduct;
  				lo.idsnackin=login[i].idsnackin;
  				lo.productname=login[i].productname;
  				lo.photo=login[i].photo;
  				lo.username=login[i].username;
  				
  				
  				
  				

  				
  				users.add(lo);
  					}
  			
  			Log.d("Muro",""+users.size());
  				return String.valueOf(users.size());
  				
  				
  				}
  				
  		}

  		
  		@Override
  		
  		protected void onPostExecute(String result) {
  			// TODO Auto-generated method stub
  			super.onPostExecute(result);
  			progressDialog.dismiss();
  			
  			
  			//si la respuesta esta vacia entonces manda mensaje
  			if(result.equals("Error")){
  		 
  				
  				
  			}else{
  				Log.d("Muro",""+result);
  				 listView.setAdapter(new UserItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, users));
		    				
  		
  			}
  			
  			
  		}

  		@Override
  		protected void onPreExecute() {
  			// TODO Auto-generated method stub
  			super.onPreExecute();
  			progressDialog.show();
  		
  			
  			
  		}

  		@Override
  		protected void onProgressUpdate(Integer... values) {
  			// TODO Auto-generated method stub
  			
  			
  			super.onProgressUpdate(values);
  			progressDialog.setProgress(values[0]);
  			
  			
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
	  
	  //el refreshhhh
	  
	  
	  private class Download2 extends AsyncTask<String,Integer,String>{//paso 3

	  		
	  		@Override
	  		public String doInBackground(String... params) {
	  			// TODO Auto-generated method stub
	  			
	  			
	  		
	  			//declaracoion de variables de conexion
	  			
	  				String url=params[0];
	  				DefaultHttpClient cliente=new DefaultHttpClient();
	  				HttpGet httpGet=new HttpGet(url);
	  				String data="";
	  				String html="";
	  		
	  				
	  				try {
	  					
	  					//trayendop y leyendo respuesta
	  					HttpResponse respuesta=cliente.execute(httpGet);
	  				
	  					InputStream contenido=respuesta.getEntity().getContent();
	  			
	  					BufferedReader reader=new BufferedReader(new InputStreamReader(contenido));
	  				
	  				
	  				
	  					while((data=reader.readLine())!=null){
	  				
	  						html +=data;
	  						
	  					
	  					}
	  					
	  					reader.close();
	  					contenido.close();
	  					
	  					
	  					
	  					//declaracion de excepciones
	  				} catch (MalformedURLException e) {
	  					// TODO Auto-generated catch block
	  					
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
	  			
	  			

	  			
	  				
	  				Gson gson=new Gson();
	  			    
	  			
	  			
	  			
	  				UserRecord[]  login=gson.fromJson(html.toString(),UserRecord[].class);//login =new login(en jason)
	  		
	  				
	  				
	  				//si la espuesta esta vacia asigna variable
		    			
	  				if(login==null){
	  					String v="Error";
	  					return v;
	  				
	  				
	  				             
	  				}else{      
	  				
	  					
	  					//llenando array de preguntas
	  					
	  			for(int i=0;i<login.length;i++){    
	  				
	  				UserRecord lo=new UserRecord("","","","","");	
	  			 
	  				
	  				lo.idproduct=login[i].idproduct;
	  				lo.idsnackin=login[i].idsnackin;
	  				lo.productname=login[i].productname;
	  				lo.photo=login[i].photo;
	  				lo.username=login[i].username;
	  				
	  				
	  				
	  				

	  				
	  				users.add(lo);
	  					}
	  			
	  			Log.d("Muro",""+users.size());
	  				return String.valueOf(users.size());
	  				
	  				
	  				}
	  				
	  		}

	  		
	  		@Override
	  		
	  		protected void onPostExecute(String result) {
	  			// TODO Auto-generated method stub
	  			super.onPostExecute(result);
	  			progressDialog.dismiss();
	  			
	  			
	  			//si la respuesta esta vacia entonces manda mensaje
	  			if(result.equals("Error")){
	  		 
	  				
	  				
	  			}else{
	  				Log.d("Muro",""+result);
	  				 listView.setAdapter(new UserItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, users));
			    				
	  		
	  			}
	  			
	  			
	  		}

	  		@Override
	  		protected void onPreExecute() {
	  			// TODO Auto-generated method stub
	  			super.onPreExecute();
	  			
	  		
	  			
	  			
	  		}

	  		@Override
	  		protected void onProgressUpdate(Integer... values) {
	  			// TODO Auto-generated method stub
	  			
	  			
	  		
	  			
	  			
	  		}
	  		
	  		
	  	}
	  @Override
	  public void onResume()
	  {
	    super.onResume();
	    
	    Log.d("Muro", "en resume");
	   
	  }
	  @Override
	  public void onPause()
	  {
	    super.onPause();
	   // unbindDrawables(findViewById(R.layout.muro));
	   //(findViewById(R.id.RootView));
	    //System.gc();
	   // unbindDrawables(findViewById(R.id.RootView));
	   // System.gc();
	    Log.d("Muro", "en pausa");
	     
	  }
	  @Override
	  public void onStop()
	  {
	    super.onStop();
	    Log.d("Muro", "en stop");
	   // unbindDrawables(findViewById(R.layout.muro));
	  // unbindDrawables(findViewById(R.id.RootView));
	  // System.gc();
	   
	  
	  }
	  @Override
	  public void onDestroy()
	  {      super.onDestroy();
	  
	  Log.d("Muro", "destrosado");
	  
	  listView.setAdapter(null);
	     // unbindDrawables(findViewById(R.layout.muro));
	   //unbindDrawables(findViewById(R.layout.con_muro));
	     // System.gc();
	  }

	 // unbindDrawables(findViewById(R.id.conmuro));
	 //   System.gc();

	 
	 
  }


