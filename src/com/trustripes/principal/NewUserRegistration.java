package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.trustripes.Constants.ConstantValues;
import com.trustripes.Events.checkFullName;
import com.trustripes.Events.checkMail;
import com.trustripes.Events.checkPass;
import com.trustripes.Events.checkUsername;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserRegistration extends Activity{

	/* Variable for Internal Debug */
	private final static String TAG = "NewUserRegistration AsyncTask";
	
	/* Declaration of UI Widgets */
	private EditText username;
	private EditText mail;
	private EditText pass;
	private EditText visiblePass;
	private EditText full_name;
	private ImageView newProfilePhoto;
	private Button send;
	private CheckBox passCheck;
	private ProgressBar progressBar;
	private TextView errorMessage;
	private TextView passwordAdvice;
	
	/* Only visible when registering a new user */
	private RadioGroup gender;
	
	/* Only visible when editting user data */
	private EditText originalPass;
	
	/* Values for filter in onActivityResult */
	private final int CAMERA_RESULT = 200;
	private final int GALLERY_RESULT = 300;
	
	/* Image to send */
	private Bitmap bitmap;
	
	/* Default images when registering */
	private Bitmap maleProfile, femaleProfile;
		
	/* True if the user is editting his own data  */
	private boolean isEdit;
	
	/* Different than null only if the user is updating his own data */
	private String lastId = null;
	
	/* Path names */
	private String destination;
	private String source;
	
	/* Session variable */
	private SharedPreferences session = null;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Hide title in app */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        /* Setting view */
        setContentView(R.layout.activity_new_user_registration);
        
        /* Variable to receive information sent by the previous activity (only when updating) */
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        
        /* Instantiation of the UI widgets */
        username = (EditText) findViewById(R.id.username_editText);
        full_name = (EditText) findViewById(R.id.full_name_editText);
        pass = (EditText) findViewById(R.id.pass_editText);
        visiblePass = (EditText) findViewById(R.id.visible_pass_editText);
        mail = (EditText) findViewById(R.id.mail_editText);
        send = (Button) findViewById(R.id.register_user_button);
        progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        errorMessage = (TextView) findViewById(R.id.error_message);
        passwordAdvice = (TextView) findViewById(R.id.new_password_advice);
        passCheck = (CheckBox) findViewById(R.id.pass_check);
        newProfilePhoto = (ImageView) findViewById(R.id.register_user_photo);
        originalPass = (EditText) findViewById(R.id.original_pass_editText);
        gender = (RadioGroup) findViewById(R.id.gender);
        
        /* Default */
        username.setBackgroundResource(R.drawable.text_background_purple);
        full_name.setBackgroundResource(R.drawable.text_background_purple);
        mail.setBackgroundResource(R.drawable.text_background_purple);
        pass.setBackgroundResource(R.drawable.text_background_purple);
        visiblePass.setBackgroundResource(R.drawable.text_background_purple);   
        passwordAdvice.setVisibility(View.GONE);
        
        /* Loading image resource for the variables */
        maleProfile = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_masculino);
        femaleProfile = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_femenino);
        
        /* Loading the session variable */
        session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        
        /* Hiding the temporal widgets */
        /* Only visible when asynctask is working */
        progressBar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        
        /* Add listeners to check if the fields have valid data */
        username.addTextChangedListener(new checkUsername(username, errorMessage));
        mail.addTextChangedListener(new checkMail(mail, errorMessage));
        full_name.addTextChangedListener(new checkFullName(full_name, errorMessage));
        pass.addTextChangedListener(new checkPass(pass, errorMessage));

        /* Loading the default avatar from the image resource */
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        
        /* If updating information fill the fields with known data */
        if(isEdit){
        	pass.setHint("New password");
        	username.setText(session.getString("user_name", ""));
        	username.setEnabled(false);
        	
        	full_name.setText(session.getString("user_full_name","Hola"));
        	
        	mail.setText(session.getString("user_mail", ""));
        	mail.setEnabled(false);
        	
        	lastId = session.getString("user_id", "");
        	        	        	
        	/* Show the field to insert the curret password */
        	originalPass.setVisibility(View.VISIBLE);
        	
        	/* Hide the gender select option */
        	gender.setVisibility(View.GONE);
        	
        	passwordAdvice.setVisibility(View.VISIBLE);
        	String tmpPath = session.getString("user_external_image_path", "");
        	
        	destination = tmpPath;
        	
        	File tmpFile = new File(tmpPath);
        	if(tmpFile.exists())
        		decodeFile(tmpPath);
        	else
        		newProfilePhoto.setImageBitmap(bitmap);
        	
        	send.setText("Update information");
        }else{
        	/* If registering a new user hide the field because there is no current password */
        	originalPass.setVisibility(View.GONE);
        }
        
        /* Only visible if updating information */
        visiblePass.addTextChangedListener(new checkPass(visiblePass, errorMessage));
                      
        /* Listener to show the password entered in the field */
        passCheck.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		 if(passCheck.isChecked()){
        	    		visiblePass.setText(pass.getText().toString());
        	    		visiblePass.setVisibility(View.VISIBLE);
        	    		pass.setVisibility(View.GONE);
    	    	}
    	    	else{
    	    		pass.setText(visiblePass.getText().toString());
    	    		pass.setVisibility(View.VISIBLE);
    	    		visiblePass.setVisibility(View.GONE);
    	    	}
    		}
        });
        
        /* Listener to show a default image according to the selection */
        gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case R.id.avatar_male:
						newProfilePhoto.setImageBitmap(maleProfile);
						bitmap = maleProfile;
						break;
					case R.id.avatar_female:
						newProfilePhoto.setImageBitmap(femaleProfile);
						bitmap = femaleProfile;
						break;
				}
			}
		});
              
        /* Getting the builder to create the dialog to choose the image source */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        /* Setting the title for the dialog */
		builder.setTitle("Pick image from");
		
		/* Geting the system inflater to create the UI of the dialog */
		LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		
		/* Loading the view */
		View new_layout = inflater.inflate(R.layout.pick_photo, null);
		
		/* Setting the view */
		builder.setView(new_layout);
		
		/* Now we create the dialog */
		final AlertDialog choosePictureDialog = builder.create();
		
		/* Instantiation of the buttons from the layout loaded previously */
		Button cameraButton = (Button) new_layout.findViewById(R.id.launch_camera_button);
		Button galleryButton = (Button) new_layout.findViewById(R.id.choose_from_gallery_button);
		
		/* Setting the listener for the camera button */
		/* Check the onActivity result for the value it returns */
		cameraButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
				startActivityForResult(cameraIntent, CAMERA_RESULT);
				choosePictureDialog.dismiss();	
			}
		});
	
		/* Setting the listener for the gallery button */
		/* Check the onActivity result for the value it returns */
		galleryButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, GALLERY_RESULT);
				choosePictureDialog.dismiss();
			}
		});
        
        /* Making the image clickable */
        newProfilePhoto.setClickable(true);
		
        /* Setting the listener to show the dialog to pick an image */
        /* Tip: Add a plus sign to know it is clickable */
        newProfilePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				choosePictureDialog.show();
			}
		});
        
        developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = session.getString("user_id", "-1");
        realId = Integer.parseInt(id);
               
    }

    private File getTempFile(){
		  //it will return /sdcard/image.tmp
		  final File path = new File( Environment.getExternalStorageDirectory(),"TruStripes");
		  if(!path.exists())
		    path.mkdir();
		  return new File(path, "newProfileImage_.tmp");
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case GALLERY_RESULT:
				if (resultCode == Activity.RESULT_OK) {
					Uri selectedImageUri = data.getData();
					String filePath = null;
					try {
						// OI FILE Manager
						String filemanagerstring = selectedImageUri.getPath();
	
						// MEDIA GALLERY
						String selectedImagePath = getPath(selectedImageUri);
	
						if (selectedImagePath != null) {
							filePath = selectedImagePath;
							source = filePath;
						} else if (filemanagerstring != null) {
							filePath = filemanagerstring;
						} else {
							Toast.makeText(getApplicationContext(), "Unknown path",Toast.LENGTH_LONG).show();
							Log.e("Bitmap", "Unknown path");
						}
	
						if (filePath != null) {
							decodeFile(filePath);
						} else {
							bitmap = null;
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Internal error",Toast.LENGTH_LONG).show();
						Log.e(e.getClass().getName(), e.getMessage(), e);
					}
				}
				break;
			case CAMERA_RESULT:
				if (resultCode == Activity.RESULT_OK) {
					final File file = getTempFile();
					source = file.getAbsolutePath();
					try {
						Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
						bitmap = captureBmp;
						newProfilePhoto.setImageBitmap(bitmap);							
						// do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
		}
	}
    
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
	
    public void decodeFile(String filePath) {
    	// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// Find the correct scale value. It should be the power of 2.
		// int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 2;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		newProfilePhoto.setImageBitmap(bitmap);
	}
    
    @Override
    protected void onStart() {
    	super.onStart();   	
    	
    	/* Implementation of Google Analytics for Android */
    	if(!ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
    	
    	/* Filter if send to register a new user or to update a current one */
    	send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
							
				/* If it is updating the current user's information */
				if(isEdit){
					if(!passCheck.isChecked()){
						if(originalPass.getText().toString().length() >= 2 ){
							if(checkUsername.returnValue == true && checkMail.returnValue == true && checkFullName.returnValue == true && (checkPass.returnValue == true || pass.getText().length() == 0)){
								/* If the fields are filled with new validated data */
								new SendUpdateUser().execute(username.getText().toString(), full_name.getText().toString(),mail.getText().toString(),pass.getText().toString(), originalPass.getText().toString());
							}
							else
								/* Show an error message to indicate the fields are not with validated data */
								Toast.makeText(getApplicationContext(), "The fields are not with validated data", Toast.LENGTH_SHORT).show();
						}
						else{
							/* Toast to show when current password's length is too short */
							Toast.makeText(getApplicationContext(), "Please type your actual password to proceed", Toast.LENGTH_SHORT).show();
						}
					}
					else{
						/* Block to execute when new_password is readable */
						Toast.makeText(getApplicationContext(), "Please hide your password to proceed", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					/* Check if password is hidden */
					if(!passCheck.isChecked()){
						if(checkUsername.returnValue == true && checkMail.returnValue == true && checkFullName.returnValue == true && checkPass.returnValue == true ){
							/* Start the AsyncTask to register a new user */
							new SendNewUser().execute(username.getText().toString(), full_name.getText().toString(),mail.getText().toString(),pass.getText().toString());
						}
						else{
							Toast.makeText(getApplicationContext(), "All fields are required to Register", Toast.LENGTH_SHORT).show();
						}
					}	
					else{
						/*  */
						Toast.makeText(getApplicationContext(), "Please hide your password to proceed", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	/* Implementation of Google Analytics for Android */
    	if(!ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStop(this);
    	}
    }

    /* AsynTask to register a new user */
    public class SendNewUser extends AsyncTask<String, Integer, Void>{
    	private String usernameToSend;
    	private String mailToSend;
    	private String fullNameToSend;
    	private String passToSend;
    	
    	private StringBuilder stringBuilder;
    	private String statusResponse;
    	
    	private String iduser = null;
    	private String name = null;
    	private String photoUrl = null;
    	private String full_name = null;
    	private String mail = null;
    	
    	private String message = null;
    	
    	private boolean canLogin;
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		progressBar.setVisibility(View.VISIBLE);
    		errorMessage.setVisibility(View.GONE);
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		try{
    			
    			usernameToSend = params[0];
    			fullNameToSend = params[1];
    			mailToSend = params[2];
    			passToSend = params[3];
    			HttpClient client = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				String postURL = ConstantValues.URL + "/ws/ws-userregister.php";
				HttpPost post = new HttpPost(postURL);
				MultipartEntity entity = new MultipartEntity();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 30, bos);
				byte[] data = bos.toByteArray();
				entity.addPart("uploadedfile", new ByteArrayBody(data, "newProfileImage.jpg"));
				entity.addPart("email", new StringBody(mailToSend));
				entity.addPart("username", new StringBody(usernameToSend));
				entity.addPart("displayname", new StringBody(fullNameToSend));
				entity.addPart("password", new StringBody(passToSend));
				post.setEntity(entity);
				HttpResponse responsePOST = client.execute(post, localContext);
				StatusLine status = responsePOST.getStatusLine();
				/* Filter what kind of response was obtained */
	    		/* Filtering http response 200 */
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity new_entity = responsePOST.getEntity();
					InputStream inputStream = new_entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
					String line = null;
					stringBuilder = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}

	    			/* Converting obtained string into JSON object */
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			statusResponse = jsonObject.getString("status");
	    			if(Integer.parseInt(statusResponse) == 1){
		    				/* Success Login */
		    				iduser = jsonObject.getString("iduser");
			    			name = jsonObject.getString("user");
			    			
			    			full_name = jsonObject.getString("display");
			    			mail = jsonObject.getString("email");
			    			photoUrl = jsonObject.getString("photo");
			    			
			    			canLogin = true;
	    			}	
	    			else{
		    				/* Can't Login */    				
		    				message = jsonObject.getString("msj");
		    				canLogin = false;
	    			}
	    			    			
	    			reader.close();
	    			inputStream.close();
	    		}
	    		else{
	    			/* Check Other Status Code */
	    		}
	    	}catch(ClientProtocolException e){
    			e.printStackTrace();
    			Log.e(TAG,"CheckLoginData: Error ClientProtocolException");
    		}catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    			Log.e(TAG,"CheckLoginData: Error UnsupportedEncodingException");
			}catch (IOException e) {
    			Log.e(TAG,"CheckLoginData: Error IOException");
				e.printStackTrace();
			}catch(Exception e){
				Log.e(TAG, "Unknown error");
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		if(canLogin){
	    		SharedPreferences.Editor settingsEditor = session.edit();
							
				settingsEditor.putString("user_status", statusResponse);
	    		settingsEditor.putString("user_id", iduser);
				settingsEditor.putString("user_name", name);
				settingsEditor.putString("user_full_name", full_name);
				settingsEditor.putString("user_mail", mail);
				
				int id = Integer.parseInt(iduser);
				settingsEditor.putString("user_external_image_path", Environment.getExternalStorageDirectory()+"/TruStripes/"+ConstantValues.codeName(id)+".png");
				settingsEditor.putString("user_photo_url", photoUrl);
				
				settingsEditor.putBoolean("show_snack_help", true);
				settingsEditor.commit();
				showStatusMessage();
			}
    		else{
    			progressBar.setVisibility(View.GONE);
    			errorMessage.setText(message);
    			errorMessage.setVisibility(View.VISIBLE);
    		}
    	}
    }
    
    /* AsyncTask to update information */
    public class SendUpdateUser extends AsyncTask<String, Integer, Void>{
    	private String usernameToSend;
    	private String mailToSend;
    	private String fullNameToSend;
    	private String passToSend;
    	private String originalPassToSend;
    	
    	private StringBuilder stringBuilder;
    	private String statusResponse;
    	   	
    	private String message = null;
    	
    	private boolean updated;
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		progressBar.setVisibility(View.VISIBLE);
    		errorMessage.setVisibility(View.GONE);
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		try{
    			
    			usernameToSend = params[0];
    			fullNameToSend = params[1];
    			mailToSend = params[2];
    			passToSend = params[3];
    			originalPassToSend = params[4];
    			
    			HttpClient client = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				String postURL = ConstantValues.URL + "/ws/ws-useredit.php";
				HttpPost post = new HttpPost(postURL);
				
				MultipartEntity entity = new MultipartEntity();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				
				bitmap.compress(CompressFormat.JPEG, 50, bos);
				
				byte[] data = bos.toByteArray();
				
				entity.addPart("id", new StringBody(lastId));
				//entity.addPart("email", new StringBody(mailToSend));
				//entity.addPart("username", new StringBody(usernameToSend));
				entity.addPart("displayname", new StringBody(fullNameToSend));
				if(passToSend.length() == 0)
					entity.addPart("nuevaClave", new StringBody(originalPassToSend));
				else
					entity.addPart("nuevaClave", new StringBody(passToSend));
				
				entity.addPart("password", new StringBody(originalPassToSend));

				entity.addPart("uploadedfile", new ByteArrayBody(data, "newProfileImage.jpg"));
				
				post.setEntity(entity);
				HttpResponse responsePOST = client.execute(post, localContext);
				StatusLine status = responsePOST.getStatusLine();
				/* Filter what kind of response was obtained */
	    		/* Filtering http response 200 */
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity new_entity = responsePOST.getEntity();
					InputStream inputStream = new_entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
					String line = null;
					stringBuilder = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					
	    			/* Converting obtained string into JSON object */
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			statusResponse = jsonObject.getString("status");
	    			if(Integer.parseInt(statusResponse) == 1){
		    				/* Success Update */
			    			updated = true;
	    			}	
	    			else{
	    					/* Success Update */   				
		    				updated = false;
	    			}
	    			message = jsonObject.getString("msj");
	    			    			
	    			reader.close();
	    			inputStream.close();
	    		}
	    		else{
	    			/* Check Other Status Code */
	    		}
	    	}catch(ClientProtocolException e){
    			e.printStackTrace();
    			Log.e(TAG,"CheckLoginData: Error ClientProtocolException");
    		}catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    			Log.e(TAG,"CheckLoginData: Error UnsupportedEncodingException");
			}catch (IOException e) {
    			Log.e(TAG,"CheckLoginData: Error IOException");
				e.printStackTrace();
			}catch(Exception e){
				Log.e(TAG, "Unknown error");
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		if(updated){
	    		SharedPreferences.Editor settingsEditor = session.edit();
	    		settingsEditor.putString("user_status", statusResponse);
				settingsEditor.putString("user_id", lastId);
				//settingsEditor.putString("user_name", username.getText().toString());
				settingsEditor.putString("user_mail", mailToSend);
				settingsEditor.putString("user_full_name", fullNameToSend);
				settingsEditor.commit();
				try {
					FileUtils.copyFile(new File(source), new File(destination));
				} catch (IOException e) {
					e.printStackTrace();
				}
				showStatusMessage();
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    		}
    		else{
    			progressBar.setVisibility(View.GONE);
    			errorMessage.setText(message);
    			errorMessage.setVisibility(View.VISIBLE);
    		}
    	}
    }
    
    public void showStatusMessage(){
    	int status = Integer.parseInt(session.getString("user_status", "-1"));
    	switch(status){
    		case 1:
    			if(isEdit){
    				/* Finish the current activity */
    				/* MainActivity was already in the botton of the stack */
    				setResult(200);
    				finish();
    			}else{
    				/* Start MainActivity */ 
        			Intent startWallActivity = new Intent(getApplicationContext(), MainActivity.class);
        			
        			/* AÑADIR ESTA ACTIVIDAD PARA SER ELIMINADA DESDE LA OTRA */
        			startWallActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        			startActivity(startWallActivity);
        			Log.i(TAG, "Starting MainActivity");
    			}
    			break;
    		case 0:
    			Log.i(TAG, "Couldn't start MainActivity");
    			break;
    		default:
    			Log.i(TAG, "Default Message: There is no 'user_status' value: You shouldn't be seeing this");
    			break;
    	}
    }
}
