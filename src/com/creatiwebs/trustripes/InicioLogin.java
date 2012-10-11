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
import org.json.JSONTokener;
import packete.datas.LoginData;

import com.creatiwebs.trustripes.R;

import org.json.JSONObject;
import org.json.JSONTokener;
import android.os.Handler;
import android.os.Message;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionStore;

import com.facebook.android.FacebookError;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import Configuracion.ConfiguracionIp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InicioLogin extends Activity {
	
	
	// Declaracion de Variables

	
	
	ProgressDialog progressDialog;
	EditText usuario,password;
	String ip;
	String value="";
	TextView recpassword;
	Button i;
	 
	Context mClo = this; 
	Tracker myNewTracker;
	GoogleAnalytics myInstance;

	ImageView pic;
	   String name = "";
       String id = "";
       String mail = "";
       String idusuario="";
	
  	
  	private ProgressDialog mProgress;
  
  	TextView cal;
    private Facebook mFacebook;
	private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};
	
	private static final String APP_ID = "274388489340768";
  	


  	

	
	
	// Metodo onCreate el cual contiene la pantalla layout
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		

		EasyTracker.getInstance().setContext(this);
		  pic=(ImageView)findViewById(R.id.lface);
		
		  
		  pic.setVisibility(View.INVISIBLE);
		usuario=(EditText)findViewById(R.id.idusuario);
		password=(EditText)findViewById(R.id.idpassword);
		recpassword=(TextView)findViewById(R.id.rpassword);
		i=(Button)findViewById(R.id.bingresar);
		
		 mProgress= new ProgressDialog(this);
		 mFacebook= new Facebook(APP_ID);
	        
		
		
		
	    isOnline();
	
		recpassword.setText(Html.fromHtml("<font size='7'><a href=\"http://www.google.com\"><b>¿Forgot your password?</b></a></font> "));
		
		recpassword.setMovementMethod(LinkMovementMethod.getInstance());
		
		
		
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


	private final class FbLoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
           SessionStore.save(mFacebook, InicioLogin.this);
           
         
			 
           getFbName();
        }

        public void onFacebookError(FacebookError error) {
           Toast.makeText(InicioLogin.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
           
          
        }
        
        public void onError(DialogError error) {
        	Toast.makeText(InicioLogin.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
        	
        	
        }

        public void onCancel() {
        	//mFacebookBtn.setChecked(false);
        }
    }
	private void getFbName() {
		mProgress.setMessage("authenticating...");
		mProgress.show();
		
		new Thread() {
			@Override
			public void run() {
		     
		        int what = 1;
		      //  URL img_url=null;
		        try {
		        	
		        	String me = mFacebook.request("me");
		        	
		        	JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
		        	id = jsonObj.getString("id");
		        	name = jsonObj.getString("name");
		        	mail = jsonObj.getString("email");
		        	//img_url=new URL("http://graph.facebook.com/"+id+"/picture?type=small");
		        	//Bitmap bmp=BitmapFactory.decodeStream(img_url.openConnection().getInputStream());
		        	   SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			    		Editor editor=preferences.edit();
					     editor.putString("idface", String.valueOf(id));
					     editor.putString("nface", String.valueOf(name));
					     editor.putString("cface", String.valueOf(mail));
					     editor.putString("louser","no");
					     
					    editor.commit();
					    
					    Log.d("InicioLogin",String.valueOf(mail) );
		        	what = 0;
		        	
		        	//guardo la sesion de facebook y mando al siguiente con los paramettros
		        	
		        	
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        
		        mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();
		
		
		//postear();
	}
	
	
	
	private void fbLogout() {
		mProgress.setMessage("Desconectandose de  Facebook");
		mProgress.show();
			
		new Thread() {
			@Override
			public void run() {
			SessionStore.clear(InicioLogin.this);
		        	   
				int what = 1;
					
		        try {
		        	mFacebook.logout(InicioLogin.this);
		        		 
		        	what = 0;
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        	
		        mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
		
		//pic.setImageResource(R.drawable.fc2);
	}
	
	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			
			if (msg.what == 0) {
				String username = (String) msg.obj;
		        username = (username.equals("")) ? "No Name" : username;
		            
		     
		        
		        Toast t=Toast.makeText(getApplicationContext(),"Welcome to trueStripers", Toast.LENGTH_SHORT);
			    t.show();
		        Intent intent=new Intent(getApplicationContext(),Principal.class);
		        startActivity(intent);
				
		        
		      //guardo la sesion de facebook y mando al siguiente con los paramettros
			
			} else {
				Toast.makeText(InicioLogin.this, "Conectado a Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			
			if (msg.what == 1) {
				Toast.makeText(InicioLogin.this, "Facebook logout failed", Toast.LENGTH_SHORT).show();
			} else {
				
	        	   
				Toast.makeText(InicioLogin.this, "Desconectado de Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};

  
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		 
		    return true;
		    
		}
		
		final Dialog alertDialog = new Dialog(mClo);
	  	 
	  	 alertDialog.setContentView(R.layout.custom);
		  alertDialog.setTitle("Mensagge trueStripes");
			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
			
			text.setText("This application requires internet connection!");
			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
			//image.setImageResource(R.drawable.taxi);

			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent o = new Intent(); 
			    	o.setAction(Intent.ACTION_MAIN); 
			    	o.addCategory(Intent.CATEGORY_HOME); 
			    	o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			    	startActivity(o); 
			    	android.os.Process.killProcess(android.os.Process.myPid()); 
				}
			});
		
		  alertDialog.show();
		
		
		
		
		return false;
		}
	
	public boolean isOnline2() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		 
		    return true;
		    
		}
		
	 
		
		
		return false;
		}
	
	
	
	
	     // Evento del boton ingresar	 
	public void btn_registrar(View v){
		
		Intent intent=new Intent(this,RegUsuario.class);
		startActivity(intent);
		
		
	}
	      
	public void	btn_lfa(View v){
	
	
		mFacebook.authorize(this, PERMISSIONS, -1, new FbLoginDialogListener());
	
	
}
public void btn_Ingresar(View v){
	
	//validar internet
	if(isOnline2()==false){
		
		 
		Toast t=Toast.makeText(getApplicationContext(),"Please validate your Internet connection or try again later", Toast.LENGTH_SHORT);
		t.show();
		
		
		/*Intent intent=new Intent(getApplicationContext(),Error.class);
		startActivity(intent);
		*/
	
	}else{
	
	
	
		
		
		usuario.setError(null);
    	password.setError(null);
    	
    	
		String user=usuario.getText().toString().trim();
    	String pas=password.getText().toString().trim();
		
    	boolean esError=false;
    
    	if(user.length()==0){
    	usuario.setError("You Enter User");
    	esError=true;
        
    	}
        if(pas.length()==0){
    	
        password.setError("You Enter Password");
     	esError=true;
    	}
    	
	  if(esError) return;
	  
	  
	  i.setEnabled(false);
	  
	  //aki junto
	 String us,p;
	 
	  us=juntar(user);
	  p=juntar(pas);
	  

	  
		
	      //Llamando al progressbar	
	  
	    	progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("authenticating..");
			progressDialog.setCancelable(false);
			
			//progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
			
  
			    
		    Download tarea=new Download();
				
		  
			ConfiguracionIp i=new ConfiguracionIp() ;
			String ip=i.ip;
				
		tarea.execute( new String[]{ip+"/ws/ws-validatelogin.php?user="+us+"&password="+p});
			
		}
		
	
}

// metodo dowloand la cual ejecuta al webservice ante una url declarada arriba	
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
					  Log.d("InicioLogin", String.valueOf(html));
					  
					  
				
				}
				
				reader.close();
				contenido.close();
				
/*
				JSONObject respJSON = new JSONObject(html.toString());
				
				int idCli = respJSON.getInt("oid");*/
				//Pregressbar al 100%
				publishProgress(100);
			
				
				
				//Declaracion de Excepciones
		
			
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
				
				
			
			}catch (RuntimeException e) {
				// TODO Auto-generated catch block
				Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
				t.show();
				e.printStackTrace();
				
				
			
	        
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				Toast t=Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
				t.show();
				e.printStackTrace();
				
				 
			} 
		
		

			
		  	Log.d("InicioLogin",String.valueOf(html.toString()));
		  	JSONObject respJSON;
			String idCli="";
			
			
			
			try {
				respJSON = new JSONObject(html.toString());
				
				idCli = respJSON.getString("msj");
				
			  	Log.d("InicioLogin",String.valueOf(idCli));
			  	
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(idCli.equals("0")){
				
				String v="User /Password incorrect";
				 // i.setEnabled(true);
					
				return v;
				
			
				
			}else{
		  	
		  	
			//Instanciadno libreria gson
			Gson gson=new Gson();
		    
		
		
			//Asignando respuesta a nuestra clase
			LoginData login=gson.fromJson(html.toString(),LoginData.class);
			
		
		
			//Si la respuesta es vacia entonces no existe usuario
			
			
			if(login==null){
				String v="User /Password incorrect";
				return v;
			
			
			
			}else{
			
			
			//String resp="si en tra";
			
			/*LoginData lo=new LoginData();
			
			
			lo.iduser=login.iduser;
			lo.nombre=login.nombre;
		
			*/
			String resp;
			//Asignando valores a parametros de  nuestra clase
				
			
			if( idCli.equals("0") ){//el msj es 0
				resp="User /Password incorrect";
				

			    i.setEnabled(true);
			
				
			}else{ 
				
				//MyApp miApp=(MyApp)getApplication();
				
				//miApp.idUs=login.idUsuario;
				resp="Welcome to trueStripers";
				
				LoginData lo=new LoginData();
				
				
				lo.iduser=login.iduser;
				lo.nombre=login.nombre;
				 idusuario=login.iduser;
				 
				//guardo en pregerencias
				SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    		Editor editor=preferences.edit();
			     editor.putString("idus", String.valueOf(login.iduser));
			     editor.putString("username", String.valueOf(login.nombre));
			     editor.putString("iduserfinal",idusuario);
			     editor.putString("louser","si");
			    
			     //la ruta de la imagen
			     
			    editor.commit();
			
			}
		
	
			return resp;
			} 
			}
	}

	
	@Override
	
	protected void onPostExecute(String result) {//paso 4
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
	
	
		
		if(result.equals("Welcome to trueStripers")){
			
			//guardo login
			
			
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    		Editor editor=preferences.edit();
		    editor.putString("LOGIN", "1");
		    editor.commit();
			
	    Toast t=Toast.makeText(getApplicationContext(),"Welcome to truStripes", Toast.LENGTH_SHORT);
	    t.show();
	    
	    i.setEnabled(true);
	
		Intent intent=new Intent(getApplicationContext(),Principal.class);
		startActivity(intent);
		
		
		}else if(result.equals("User /Password incorrect")){
			
			Toast t=Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT);
		    t.show();
		    i.setEnabled(true);
			
		
	}
	
		
		
	}
	//Antes de Iniciar la llamada va trayendo por background l lista de cursos 
	@Override
	protected void onPreExecute() {//paso1
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog.show();
		
	
		
		
		
		
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
		 i.setEnabled(true);
		
	}
	
	
	
	
	
}
public String juntar(String texto){
	
	String sCadenaSinBlancos="";
	
	
	 for (int x=0; x < texto.length(); x++) {
	      if (texto.charAt(x) != ' ')
	        sCadenaSinBlancos += texto.charAt(x);
	    }
	 
	 return sCadenaSinBlancos; 
	
}




@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    
    
}

	

}
