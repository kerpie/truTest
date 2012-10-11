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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;

import Configuracion.ConfiguracionIp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;

import android.content.Intent;
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
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegUsuario extends Activity {


		ProgressDialog progressDialog;
		EditText mail,user,contraseña,repcontra,nombre,apell;
		CheckBox cb;
		Context RegUsuario;
		Button r;
		Spinner sp;
		String psexo="";
		Context mCtr = this;
		String pathg="";
		String pathc="";
		String estafoto="";
		String potofinal="";
	 
		ImageButton imgperf;
		
		Tracker myNewTracker;
		GoogleAnalytics myInstance;
		private final int REQ_CODE_PICK_IMAGE= 1;
		private final int CAMERA_RESULT = 2;// Get current context.
	

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.registrousuario);
			//isOnline();
			
			EasyTracker.getInstance().setContext(this);
			 creararchivo(this);
			
			mail=(EditText)findViewById(R.id.email);
			user=(EditText)findViewById(R.id.usuario);
			contraseña=(EditText)findViewById(R.id.contra);
			repcontra=(EditText)findViewById(R.id.rcontra);
			nombre=(EditText)findViewById(R.id.nomb);
			apell=(EditText)findViewById(R.id.apellido);
			 imgperf=(ImageButton)findViewById(R.id.fotoperf);
			
			
			cb = (CheckBox)findViewById(R.id.checkrecordar);
			
			r=(Button)findViewById(R.id.bregistrar);
			
			sp= (Spinner)findViewById(R.id.sexo);
			
			 String[] d={"Male","Female"};
			 
		      
				ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, d); 
				adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(adaptador);
				
				
				sp.setOnItemSelectedListener(new OnItemSelectedListener() 
				  {

								public void onItemSelected(android.widget.AdapterView<?> parentView,
										View selectedItemView, int position, long id) {
									// TODO Auto-generated method stub
									 
									 String textmida = parentView.getItemAtPosition(position).toString();
							
									psexo=textmida;
									 Log.d("RegUsuario",String.valueOf(textmida));
								
										
							
				}

				public void onNothingSelected(android.widget.AdapterView<?> parentView) {
									// TODO Auto-generated method stub
					
					psexo="Male";
									
				}
						    	   
				});
			
			
	}
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
		
		
		public void img_perfil(View v){
			
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
					
						  PackageManager pm = getPackageManager();
						  
						

						    if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

						   Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						   i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getApplicationContext())) ); 

						   startActivityForResult(i, CAMERA_RESULT);
						   
							alertDialog.dismiss();

						  } else {

						   Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
							alertDialog.dismiss();
						  }   
						
					}
					}
				);


			  alertDialog.show();
			
		
			
			
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
			  
				
				  return new File(path, "imageuse.jpg");

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
				 
			
				  file2 = new File( Environment.getExternalStorageDirectory() ,this.getPackageName()+"/UserCamera"+ale()+".jpg"); //aleatori0 
					try {
						BitmapFactory.Options bmOptions;
						bmOptions = new BitmapFactory.Options();
						bmOptions.inSampleSize = 8;
						bmp = LoadImage2(pat, bmOptions);
						
						
						outStream = new FileOutputStream(file2);
						bmp.compress(Bitmap.CompressFormat.JPEG, 15, outStream);
						
						potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
						
						Log.d("RegUsuaioCamararasformao", potofinal);
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
						bmp.compress(Bitmap.CompressFormat.JPEG, 15, outStream);
						
						potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
						
						Log.d("RegUsuaioCamararasformao", potofinal);
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
							bmp.compress(Bitmap.CompressFormat.JPEG, 15, outStream);
							
							potofinal=String.valueOf(String.valueOf(Uri.fromFile(file2))).substring(7);
							
							Log.d("RegUsuaioCamararasformao", potofinal);
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
	            Uri selectedImage = imageReturnedIntent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            pathg = cursor.getString(columnIndex);
	            cursor.close();
	            estafoto="si";
	        	//Log.d("RutaGaleria",""+pathg);
	        	
	        	 


	            Bitmap yourSelectedImage = BitmapFactory.decodeFile(pathg);
	            
	            
	            resisar(pathg,yourSelectedImage);//esto me tiene que devolver una ruta
	        
	            imgperf.setImageBitmap(getResizedBitmap(yourSelectedImage,130,130));
	        }
	    case CAMERA_RESULT:
	    	  if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
		       
		        	//int nu=MyFileContentProvider.n;

	    		  final File file = getTempFile(this);
	    		   Bitmap captureBmp=null;
	    		
	    		  try {
	    			  
	    			  
		    			  pathc=String.valueOf(Uri.fromFile(file));
		    			  
		    			//  Log.d("RutadePruebaa",""+pathc); 
		    	          	
		    	          	//Log.d("RutaFoto",""+pathc);
		    	         captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
	    	         
	    	          	
	    	          	String sub=String.valueOf(pathc).substring(6);
	    	        	resisarcamara(captureBmp,sub);//esto me tiene que devolver una ruta
	    	          	
	    	          	
	    	          	
			         imgperf.setImageBitmap(getResizedBitmap(captureBmp,130,130));
			            estafoto="si";
			            
			            
			            imgperf.setEnabled(false);
			            
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
			  Bitmap resizedBitmap=null;
	        int width = bm.getWidth();
	        int height = bm.getHeight();
	        float scaleWidth = ((float) newWidth) / width;
	        float scaleHeight = ((float) newHeight) / height;
	        // CREATE A MATRIX FOR THE MANIPULATION
	        Matrix matrix = new Matrix();
	        // RESIZE THE BIT MAP
	        matrix.postScale(scaleWidth, scaleHeight);


	        // RECREATE THE NEW BITMAP
	        resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	        return resizedBitmap;
	    }
		/*  @Override
			public void onConfigurationChanged(Configuration newConfig) {
			    super.onConfigurationChanged(newConfig);
			}*/
		public boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			
			 
			    return true;
			    
			}

			final Dialog alertDialog = new Dialog(mCtr);
				  	 
				 	 alertDialog.setContentView(R.layout.custom);
					  alertDialog.setTitle("message truStripers");
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
		
		public void btn_confirmar(View v){
			
			
			if(isOnline2()==false){
				
				 
			Intent intent=new Intent(getApplicationContext(),Error.class);
				startActivity(intent);
				
			
			}else{
			
				
				
			mail.setError(null);
	    	user.setError(null);
	    	contraseña.setError(null);
	    	repcontra.setError(null);
	    	nombre.setError(null);
	    	apell.setError(null);
	    	
	    	//aki ya les traigo junto
	    	
	    	
	    	
	    	 String use=user.getText().toString().trim();
	    	 String con=contraseña.getText().toString().trim();
	    	 String em=mail.getText().toString().trim();
	    	 String repem=repcontra.getText().toString().trim();
	    	 String nom=nombre.getText().toString().trim();
	    	 String ape=apell.getText().toString().trim();
			
	    	
	    	
	    	boolean esError=false,esErrorPas=false,esErrort=false,esErrorc=false,esErrormail=false;
	    
	    	if(use.length()==0){
	    	user.setError("You Enter User");
	    	esError=true;
	        
	    	}
	    	
	    	
	    	 
	    	 
	        if(con.length()==0){
	    	
	        contraseña.setError("You Enter password");
	     	esError=true;
	    	}
	        if(em.length()==0){
	        	
	          mail.setError("You Enter email");
	         	esError=true;
	        	}
	        if(repem.length()==0){
	        	
	          repcontra.setError("Repeat  password");
	         	esError=true;
	       }
	        if(nom.length()==0){
	        	
		          nombre.setError("Enter Name");
		         	esError=true;
		       }
	        if(ape.length()==0){
	        	
		         apell.setError("Enter LastName");
		         	esError=true;
		       }
	        if(potofinal.equals("")){
	        	
	        	  Toast t=Toast.makeText(getApplicationContext(),"Select an image for the product! ", Toast.LENGTH_LONG);
				   t.show();
				   
				   esError=true;
	        	
	        }
	       if(esError) return;
	        
	       

	    	 if(use.length()<6){
	    		 final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	 alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge truStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("The minimum length of user must be 6 characters!");
	    		//	ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    		//	image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    			
	    				public void onClick(View v) {
	    					
	    					  alertDialog.dismiss();
	    					
	    				}
	    			});
	    		
	    		  alertDialog.show();
		          esErrort=true;
		          
		    	  if(esErrort) return;
		       }

	        if(con.length()<6){
		    	
	        	final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	  	 alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge trueStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("The minimum password length should be 6 characters!");
	    			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    		//	image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    				
	    				public void onClick(View v) {
	    					
	    					 alertDialog.dismiss();
	    					
	    				}
	    			});
	    		
	    		  alertDialog.show();
		     	esErrort=true;
		     	
		  	 
		    	}
	       
	        if(esErrort) return;
	       
	        if(con.equals(repem)){
	        	
	        	esErrorPas=false;
	        	
	        }else{
	        	
	        	  
 final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	 	 alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge trueStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("Make sure the passwords are the same!");
	    			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    		//	image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    				
	    				public void onClick(View v) {
	    					
	    					 alertDialog.dismiss();
	    					
	    				}
	    			});
	    		
	    		  alertDialog.show();
				  esErrorPas=true;
	        
	        }
	        	
	     if(esErrorPas) return;
	
		   if(checkEmail(em) == false){
		    	
				  
			   final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	   alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge trueStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("Enter a valid email address!");
	    			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    			//image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    				
	    				public void onClick(View v) {
	    					
	    					 alertDialog.dismiss();
	    					
	    				}
	    			});
	    		
	    		  alertDialog.show();
		    		
	   		
		    	esErrormail=true;
		        
		    	}
		   if(esErrormail) return;
			  
			
		  if(cb.isChecked()==false){
				 
			  final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	  	alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge trueStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("Accept the conditions");
	    			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    			//image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    				
	    				public void onClick(View v) {
	    					
	    					 alertDialog.dismiss();
	    					
	    				}
	    			});
	    		
	    		  alertDialog.show();
		    		
				   
				
				  if(esErrorc) return; 
				 
			}else{
				
		
		    		
		  r.setEnabled(false);
				
		    progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("Sending registration..");
			progressDialog.setCancelable(false);
			//progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
		 
		
		  Download tarea=new Download();
				
		  
				ConfiguracionIp i=new ConfiguracionIp() ;
				String ip=i.ip;
			
				/*String us=juntar(use);
				String co=juntar(con);
				String e=juntar(em);
				String ipe=getLocalIpAddress();
				String no=juntar(nom);
				String ap=juntar(ape);*/
				
				
				
				/*String rus=remove1(use);
				String rco=remove1(con);
				String re=remove1(em);
				
				String rno=remove1(nom);
				String rap=remove1(ape);*/
				String rus="",rco="",re="",rno="",rap="";
				try {
					rus = URLEncoder.encode(use, "utf-8");
					rco = URLEncoder.encode(con, "utf-8");
					re = URLEncoder.encode(em, "utf-8");
					rno = URLEncoder.encode(nom, "utf-8");
					rap = URLEncoder.encode(ape, "utf-8");
					
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String ipe=getLocalIpAddress();
				
			//String sub=String.valueOf(potofinal).substring(36);
				
				
		tarea.execute( new String[]{ip+"/ws/ws-userregister.php?email="+re+"&username="+rus+"&displayname="+rno+rap+"&password="+rco+"&ip="+ipe+"&firstname="+rno+"&lastname="+rap+"&gender="+psexo+"&photo="+potofinal});
   
			} 
		
		}
		
		}
		public static String remove1(String input) {
		    // Cadena de caracteres original a sustituir.
		    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		    // Cadena de caracteres ASCII que reemplazarán los originales.
		    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		    String output = input;
		    for (int i=0; i<original.length(); i++) {
		        // Reemplazamos los caracteres especiales.
		        output = output.replace(original.charAt(i), ascii.charAt(i));
		    }//for i
		    return output;
		}//remov
		 public String getLocalIpAddress() {
			    try {
			        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			            NetworkInterface intf = en.nextElement();
			            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
			                InetAddress inetAddress = enumIpAddr.nextElement();
			                if (!inetAddress.isLoopbackAddress()) {
			                    String ip = Formatter.formatIpAddress(inetAddress.hashCode());
			                    Log.d("RegUsuario", "***** IP="+ ip);
			                   
			                    return ip;
			                    
			                    
			                }
			            }
			        }
			    } catch (SocketException ex) {
			        Log.d("UbicacionUsuario", ex.toString());
			    }
			    return null;
			}
		
		
		
		
		private class Download extends AsyncTask<String,Integer,String>{

			
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
				String msj="";
				
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
			

			  	Log.d("RegUsuario",String.valueOf(url.toString()));
				
				// Estableciendo barra de progreso al 10 %	
				publishProgress(10);
		
				
				
				try {
					
					 Log.e("RegProduct","Inside second Method");

				   	  FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );

				   	   // open a URL connection to the Servlet

				   	 
				   	   URL url2 = new URL(url);


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

				   	   Log.e("RegUsuario",exsistingFileName);

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

				   	   BufferedReader in = new BufferedReader(
				   			   		new InputStreamReader(
				               		   conn.getInputStream()));
							
								
								while ((inputLine = in.readLine()) != null) {
								//html=inputLine.toString();
									Log.e("RegUsuario", inputLine.toString());
									
									JSONObject respJSON;
									
								

								try {
									respJSON = new JSONObject(inputLine.toString());
									
									msj = respJSON.getString("msj");
									
							
									Log.d("RegistroProduc",String.valueOf(msj));
								
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
						//idCli="1";
				   	   
				   	  // Log.e("MediaPlayer","File is written");
				   	   fileInputStream.close();
				   	   dos.flush();
				   	   dos.close();
				   	   

				   	   Log.e("RegUsuario","llego");
				
					  	
						
						

				
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
			
	
			
			
				return msj;
			
				
			
					
				
					
					
			}

			
			@Override
			
			protected void onPostExecute(String result) {//paso 4
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
			
			   
					
				//Sino muestra mensaje de bienvenida y pasa a la siguiente actividad
			
				if(result.equals("1")){
				
					rcompleto();
					
					  r.setEnabled(true);
					
					/*Toast t=Toast.makeText(getApplicationContext(),"Se Registro Completo Exitosamente! ", Toast.LENGTH_LONG);
				    t.show();
				

				    			Intent i=new Intent(getApplicationContext(),InicioLogin.class);
							
				    			startActivity(i);*/
					   
					

			

				    
				    
				}else if(result.equals("2")){
					 r.setEnabled(true);
				    limpiar();

					Toast t=Toast.makeText(getApplicationContext(),"This user is already registered! ", Toast.LENGTH_SHORT);
				    t.show();  	
				    
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
			
			
			
			
			
			
		}
		
		public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
		          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
		          "\\@" +
		          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
		          "(" +
		          "\\." +
		          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
		          ")+"
		      );
		
		private boolean checkEmail(String email) {
	        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
		}
		
		
		
		public void rcompleto(){
			
			
			  final Dialog alertDialog = new Dialog(mCtr);
	    	  	 
	    	   alertDialog.setContentView(R.layout.custom);
	    		  alertDialog.setTitle("Mensagge trueStripers");
	    			TextView text = (TextView)  alertDialog.findViewById(R.id.text);
	    			
	    			text.setText("To complete your registration, check your email");
	    			//ImageView image = (ImageView)  alertDialog.findViewById(R.id.image);
	    			//image.setImageResource(R.drawable.taxi);

	    			Button dialogButton = (Button)  alertDialog.findViewById(R.id.dialogButtonOK);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    			
	    				public void onClick(View v) {
			    	 
			    	 Intent i=new Intent(getApplicationContext(),InicioLogin.class);
						
		    			startActivity(i);
			   
			
			    	
			     }
			  });
			
			  alertDialog.show();
	    		
			   
			
			
			
		}
		public void limpiar(){
			
			 	user.setText("");
	    		contraseña.setText("");
	    		mail.setText("");
	    	    repcontra.setText("");
	    	    cb.setChecked(false);
			
		}
		public void btn_limpiar(View v){
			

	    	limpiar();
			   
			
			
		}
		public String juntar(String texto){
			
			String sCadenaSinBlancos="";
	    	
	    	
	    	 for (int x=0; x < texto.length(); x++) {
	   	      if (texto.charAt(x) != ' ')
	   	        sCadenaSinBlancos += texto.charAt(x);
	   	    }
	    	 
	    	 return sCadenaSinBlancos; 
			
		}

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
		 file = new File( Environment.getExternalStorageDirectory(),this.getPackageName()+"/UserGalery"+ale()+".jpg");//aleatorio
		 
			try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegUsuarioGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
			
			
		 
		 
		 
		 
		 
		 
	 }else {
		 
		 file=this.getCacheDir();
		 
		 try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegUsuarioGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegUsuario.this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegUsuario.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
		
	} if(! file.exists()){
		  file.mkdir();
		  
		  try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				potofinal=String.valueOf(String.valueOf(Uri.fromFile(file))).substring(7);
				
				Log.d("RegUsuarioGaleriaTrasformao", potofinal);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegUsuario.this, e.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(RegUsuario.this, e.toString(), Toast.LENGTH_LONG).show();
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
			  @Override
				public void onConfigurationChanged(Configuration newConfig) {
				    super.onConfigurationChanged(newConfig);
				    
				    
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

		
}
