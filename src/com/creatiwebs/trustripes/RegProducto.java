package com.creatiwebs.trustripes;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;


import Configuracion.ConfiguracionIp;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegProducto extends Activity {

	EditText nom,pais;
	TextView codigoean;
	ProgressDialog progressDialog,progressDialog2;
	String code;
	String pathg="";
	String pathc="";
	String estafoto="";
	String potofinal="";
	String nomp="";
	String codp="";
	String paisp="";
    Context Rp = this; 
	ImageButton imgp;

    Tracker myNewTracker;
	GoogleAnalytics myInstance;

	private final int REQ_CODE_PICK_IMAGE= 1;
	private final int CAMERA_RESULT = 2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.registro_producto);
	    
	    nom=(EditText)findViewById(R.id.proname);
	    codigoean=(TextView)findViewById(R.id.ean);
	    pais=(EditText)findViewById(R.id.country);
	    imgp=(ImageButton)findViewById(R.id.fotopro);
	  
	    creararchivo(this);
	    
	    
	    Bundle extras = getIntent().getExtras(); 
        if(extras !=null) 
        { 
         
        
     
         code =extras.getString("Qr"); 
         

			codigoean.setText(String.valueOf(code));
		
       
          
           
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

	
	public void img_pro(View v){
		
		final Dialog alertDialog = new Dialog(this);
	  	 
	  	 alertDialog.setContentView(R.layout.elegirfoto);
	
		
			Button dialogButton = (Button)  alertDialog.findViewById(R.id.degaleria);
		
			dialogButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				
					
					Intent i = new Intent(Intent.ACTION_PICK,
				               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, REQ_CODE_PICK_IMAGE); 
								
								alertDialog.dismiss();
									
					
				}
				}
			);
			Button dialogcamara = (Button)  alertDialog.findViewById(R.id.decamara);
			
			dialogcamara.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
			
				
					
					try{
						
					
						
					   Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					   i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getApplicationContext())) ); 

					   startActivityForResult(i, CAMERA_RESULT);
					   
						alertDialog.dismiss();

					  } catch(Exception e) {

					   Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
						alertDialog.dismiss();
					  }   
					
				}
				}
			);


		  alertDialog.show();
		
	
		
		
	}	//CAMBIARRR
	
	public void creararchivo(Context context){
		
		 final File  path;
		  if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		 
		 path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
		   else
			  path =context.getCacheDir();
		  if(!path.exists()){
			    path.mkdir();
			  }
		
	}
	
	
	
	private File getTempFile(Context context){
		  //it will return /sdcard/image.tmp
	//Creating an internal dir;
		 final File  path;
		  if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		 
		 path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
		   else
			  path =context.getCacheDir();
		  if(!path.exists()){
			    path.mkdir();
			  }
		  
			
			  return new File(path, "image.temp");

		}
	private Bitmap LoadImage2(String URL, BitmapFactory.Options options)
	{        
		Bitmap bitmap = null;
		FileInputStream in = null;        
	    try {
	    	in=new FileInputStream(new File(URL));
	        bitmap = BitmapFactory.decodeStream(in, null, options);
	        in.close();
	        
	      
	    } catch (IOException e1) {
	    }
	    return bitmap;                
	}
	//CAMBIAR
	
public void resisarcamara(Bitmap bmp,String pat){
		
		
		
		OutputStream outStream = null;
		 File file2 ;
		 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			 
		
			  file2 = new File( Environment.getExternalStorageDirectory() ,this.getPackageName()+"/ProductCamera"+ale()+".jpg"); //aleatori0 
				try {
					BitmapFactory.Options bmOptions;
					bmOptions = new BitmapFactory.Options();
				    bmOptions.inSampleSize = 8;
					bmp = LoadImage2(pat, bmOptions);
					
					
					outStream = new FileOutputStream(file2);
					bmp.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
					
					potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
					
					Log.d("RegProductoCamararasformao", potofinal);
				      outStream.flush();
				      outStream.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
		
		 }else{
			   file2=this.getCacheDir();
			   
				try {
					BitmapFactory.Options bmOptions;
					bmOptions = new BitmapFactory.Options();
					bmOptions.inSampleSize = 8;
					bmp = LoadImage2(pat, bmOptions);
					
					
					outStream = new FileOutputStream(file2);
					bmp.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
					
					potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
					
					Log.d("RegProductoCamararasformao", potofinal);
				      outStream.flush();
				      outStream.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
			 
			 
		 }
			
		  if(! file2.exists()){
			  
				  file2.mkdir();
				  
					try {
						BitmapFactory.Options bmOptions;
						bmOptions = new BitmapFactory.Options();
						bmOptions.inSampleSize = 8;
						bmp = LoadImage2(pat, bmOptions);
						
						
						outStream = new FileOutputStream(file2);
						bmp.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
						
						potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
						
						Log.d("RegProductoCamararasformao", potofinal);
					      outStream.flush();
					      outStream.close();
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
		   }
			
		
		
	
      	
		
	}
	protected void onActivityResult(int requestCode, int resultCode,Intent imageReturnedIntent) {
		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		    switch(requestCode) { 
		    case REQ_CODE_PICK_IMAGE:
		        if(resultCode == RESULT_OK){  
		        	
		        	 Bitmap yourSelectedImage;
		            Uri selectedImage = imageReturnedIntent.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            pathg = cursor.getString(columnIndex);
		            cursor.close();
		            estafoto="si";
		        	//Log.d("RutaGaleria",""+pathg);
		        	
		        	 


		           yourSelectedImage = BitmapFactory.decodeFile(pathg);
		            
		            
		            resisar(pathg,yourSelectedImage);//esto me tiene que devolver una ruta
		        
		            imgp.setImageBitmap(getResizedBitmap(yourSelectedImage,130,130));
		        }
		    case CAMERA_RESULT:
		    	  if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
			       
			        	//int nu=MyFileContentProvider.n;
		    		  System.gc();
		    		  final File file = getTempFile(this);
		    		  Bitmap captureBmp=null;
		    		  Bitmap capturado=null;
		    		  try {
		    			  
		    			  
			    			  pathc=String.valueOf(Uri.fromFile(file));
			    			  
			    			//  Log.d("RutadePruebaa",""+pathc); 
			    	          	
			    	          	//Log.d("RutaFoto",""+pathc);
			    	         captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
		    	         
		    	          	
		    	          	String sub=String.valueOf(pathc).substring(6);
		    	        	resisarcamara(captureBmp,sub);//esto me tiene que devolver una ruta
		    	          	capturado=getResizedBitmap(captureBmp,110,110);
		    	          	
		    	          	
				         imgp.setImageBitmap(capturado);
				         
				         
				         imgp.setEnabled(false);
				            estafoto="si";
				            
				            pathc="";
				        	/*File file3 = new File(sub);
							boolean deleted = file3.delete();
				            */
		    	       
		    	        } catch (IOException e) {
		    	          e.printStackTrace();
		    	        } 
	    	          	
		    	      break;

			     	     
			     	  }
		    }
	    
		}
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);


        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
	
public void limi(){
	
	
	nom.setText("");
	pais.setText("");
	pathc="";
	pathg="";
	potofinal="";
}
public void limi2(){
	
	
nom.setText("");
	pais.setText("");
	pathc="";
	pathg="";
	potofinal="";
}
	
	
	
	public void btn_limpiarpro(View v){
		
		limi();
		
	
    }
	public void btn_regpro(View v){
		
		if(isOnline2()==false){
			
			 
			
			
			
			Intent intent=new Intent(getApplicationContext(),Error.class);
			startActivity(intent);
			
		
		}else{
		
		nom.setError(null);
    	//codigoean.setError(null);
    	pais.setError(null);
    	
    	
		nomp=nom.getText().toString().trim();
    	 codp=codigoean.getText().toString().trim();
    	paisp=pais.getText().toString().trim();
		
    	boolean esError=false;
    
    	if(nomp.length()==0){
    	nom.setError("You Enter Name Product");
    	esError=true;
        
    	}
      
        if(paisp.length()==0){
        	
           pais.setError("You Enter Country");
         	esError=true;
         }
        if(potofinal.equals("")){
        	
        	  Toast t=Toast.makeText(getApplicationContext(),"Select an image for the product! ", Toast.LENGTH_LONG);
			   t.show();
			   
			   esError=true;
        	
        }
    	
	  if(esError) return;
	  
	  	progressDialog2=new ProgressDialog(this);
		progressDialog2.setMessage("Registering product..");
		progressDialog2.setCancelable(false);
		
		
		    
	Download2 tarea=new Download2();
	
	//String cod=juntar(codp);
	//String pro=juntar(nomp);
	String pai=juntar(paisp);
	//recuperar el id de usuario
	
	//quitar acentos
	
	//String rpro=remove1(pro);
	//String rpai=remove1(pai);

	//Log.d("RegProductoTildes", pro);
	
	
	String rpro="",rpai="";
	try {
		rpro = URLEncoder.encode(nomp, "utf-8");
		rpai = URLEncoder.encode(pai, "utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	//Log.d("RegProductoTilde", rpai);
	
	String sub=String.valueOf(potofinal).substring(37);
	

     SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	 String   id=preferences.getString("iduserfinal","");//recupera elid de facebook

	ConfiguracionIp i=new ConfiguracionIp() ;
	String ip=i.ip;
	tarea.execute( new String[]{ip+"/ws/ws-registerproduct.php?productname="+rpro+"&iduserdiscoverer="+id+"&codeupean="+codp+"&idambassador="+rpai+"&photo="+sub});
		}
		
		
	}
	public static String remove1(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
	    // Cadena de caracteres ASCII que reemplazar·n los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}//remov
	public boolean isOnline2() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		 
		    return true;
		    
		}
		
	 
		
		
		return false;
		}
	
	
private class Download2 extends AsyncTask<String,Integer,String>{

		
		@Override
		public String doInBackground(String... params) {//paso 3
			// TODO Auto-generated method stub
			
			
		
			    // Declarando parametros de conexion

				String url=params[0];
				DefaultHttpClient cliente=new DefaultHttpClient();
				//HttpGet httpPost=new HttpGet(url);
				String data="";
				String html="";
				String idCli="";
			    String msj="",foc="",idpro="",product="",foto="",tot="";
				
				//---------------------------------
				
				  HttpURLConnection conn = null;
			   	  DataOutputStream dos = null;
			   	  DataInputStream inStream = null; 

			   	  String exsistingFileName = potofinal;
			   	  // Is this the place are you doing something wrong.

			   	  String lineEnd = "\r\n";
			   	  String twoHyphens = "--";
			   	  String boundary =  "*****";
			      String inputLine="";
			   	  
			  	ContentResolver cr = getContentResolver();


			    

			   	  int bytesRead, bytesAvailable, bufferSize;

			   	  byte[] buffer;

			   	  int maxBufferSize = 1*1024*1024;

			   	  String responseFromServer = "";
			   	  

			   	  
			   	  //---------------------------------------
			

			  	Log.d("RegProducto",String.valueOf(url.toString()));
				
				// Estableciendo barra de progreso al 10 %	
				publishProgress(10);
		
				
				
				try {
					
					 Log.e("RegProduct","Inside second Method");

				   	  FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );

				   	   // open a URL connection to the Servlet

				   	 
				   	   URL url2 = new URL(url);
				   	   // String encodedQueryString = URLEncoder.encode(url2.getQuery());
				     	//String encodedUrl = url.replace(url2.getQuery(), encodedQueryString);


				   	   // Open a HTTP connection to the URL

				   	   conn = (HttpURLConnection) url2.openConnection();

				   	   // Allow Inputs
				   	   conn.setDoInput(true);
				   	   
				       publishProgress(30);

				   	   // Allow Outputs
				   	   conn.setDoOutput(true);

				   
				   	   conn.setUseCaches(false);

				   	   // Use a post method.
				   	   conn.setRequestMethod("POST");

				   	   conn.setRequestProperty("Connection", "Keep-Alive");
				   	 
				   	   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

				   	   dos = new DataOutputStream( conn.getOutputStream() );

				   	   dos.writeBytes(twoHyphens + boundary + lineEnd);
				   	   dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + exsistingFileName+"\"" + lineEnd);
				   	   dos.writeBytes(lineEnd);
				   	   
				   	publishProgress(40);

				   	   Log.e("RegProducto",exsistingFileName);

				   	   // create a buffer of maximum size

				   	   bytesAvailable = fileInputStream.available();
				   	   bufferSize = Math.min(bytesAvailable, maxBufferSize);
				   	   buffer = new byte[bufferSize];
						publishProgress(50);
						
						
				   	   // read file and write it into form...

				   	   bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				   	   while (bytesRead > 0)
				   	   {
				   	    dos.write(buffer, 0, bufferSize);
				   	    bytesAvailable = fileInputStream.available();
				   	    bufferSize = Math.min(bytesAvailable, maxBufferSize);
				   	    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				   	   }
					
						
						
				   	   // send multipart form data necesssary after file data...

				   	   dos.writeBytes(lineEnd);
				   	   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				   	   BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
							
								
								while ((inputLine = in.readLine()) != null) {
								
									Log.e("RegProducto", inputLine.toString());
								
								
								JSONObject respJSON;
							    //String msj="",foc="",idpro="",product="",foto="",tot="";
								
								
								
								try {
									respJSON = new JSONObject(inputLine.toString());
									
									msj = respJSON.getString("msj");
									
									idpro= respJSON.getString("idproducto");
									product= respJSON.getString("producto");
									foto= respJSON.getString("foto");
									tot= respJSON.getString("total");
									foc = respJSON.getString("foco");
									
									
									
									
								  	Log.d("RegistroProduc",String.valueOf(msj));
									Log.d("RegistroProduc",String.valueOf(idpro));
									Log.d("RegistroProduc",String.valueOf(product));
									Log.d("RegistroProduc",String.valueOf(foto));
									Log.d("RegistroProduc",String.valueOf(tot));
									Log.d("RegistroProduc",String.valueOf(foc));
									
									
									  SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							    		Editor editor=preferences.edit();
									    editor.putString("idpros", String.valueOf(idpro));
									    editor.putString("products", String.valueOf(product));
									    editor.putString("fotos", String.valueOf(foto));
									    editor.putString("tots", String.valueOf(tot));
									   // editor.putString("focs", String.valueOf(foc));
									   
									    editor.commit();
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								}
				   	   
									
						//idCli="1";
				   	   
				
				   	   fileInputStream.close();
				   	   dos.flush();
				   	   dos.close();
				   	   

				   	   Log.e("RegProducto","llego");
				
					  	
						
						

				
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
				}catch (Throwable t) {
					Log.e("RegProducto", "OMGCrash", t);
					        // maybe throw it again
				 throw new RuntimeException(t);
					    
					
				}  
	
			
			
				return msj;
			
		
				
			
				
				
		}

		
		@Override
		
		protected void onPostExecute(String result) {//paso 4
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog2.dismiss();
		
		   
				
			//Sino muestra mensaje de bienvenida y pasa a la siguiente actividad
		
			if(result.equals("1")){
			
			 
				 Intent i=new Intent(getApplicationContext(),Snackit.class);
				 //mando
				 
				 i.putExtra("Snack","12");
				 startActivity(i);
				 finish();
				 
				 
				 limi();
			    
			    
			}
			if(result.equals("0")){
				
				 Toast t=Toast.makeText(getApplicationContext(),"Error no register! ", Toast.LENGTH_LONG);
				   t.show();
				

			  
			    
			}
			
		
			
		}
		//Antes de Iniciar la llamada va trayendo por background l lista de cursos 
		@Override
		protected void onPreExecute() {//paso1
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog2.show();
			
		
			
			
			
			
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {//paso2
			// TODO Auto-generated method stub
			
			
			super.onProgressUpdate(values);
			progressDialog2.setProgress(values[0]);
			
			
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
//minimizar la imagennn

public void resisar(String url,Bitmap bmp){
	BitmapFactory.Options bmOptions;
	bmOptions = new BitmapFactory.Options();
	bmOptions.inSampleSize = 2;
	bmp = LoadImage(url, bmOptions);
	
	
	
}
private Bitmap LoadImage(String URL, BitmapFactory.Options options)
{        
	Bitmap bitmap = null;
	FileInputStream in = null;        
    try {
    	in=new FileInputStream(new File(URL));
        bitmap = BitmapFactory.decodeStream(in, null, options);
        in.close();
        miniimage(URL,bitmap);
      
    } catch (IOException e1) {
    }
    return bitmap;                
}


//CAMBIARRR
public void miniimage(String ur,Bitmap bm){
	//String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	OutputStream outStream = null;
	File file;
	//String.valueOf(Uri.fromFile(getTempFile(getApplicationContext())))
	//File file = new File(getTempFile2(getApplicationContext()));//lugar donde lo va grabar
	
	
	 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
		 file = new File( Environment.getExternalStorageDirectory(),this.getPackageName()+"/ProductGalery"+ale()+".jpg");//aleatorio
		 
			try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegProductoGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
		 
		 
		 
		 
		 
		 
	 }else {
		 
		 file=this.getCacheDir();
		 
		 try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegProductoGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
		
	} if(! file.exists()){
		  file.mkdir();
		  
		  try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegProductoGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegProducto.this, e.toString(), Toast.LENGTH_LONG).show();
			}
		  
		
		 
		
	}
	
	
}
	public int ale(){
		int n=0;
		Random r = new Random();
		int limite=10000;
		 n=(int)r.nextInt(limite+1);
		 
		 return n;
	}
	 

	

}
