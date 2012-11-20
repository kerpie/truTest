package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.view.KeyEvent;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserRegistration extends Activity{

	private final static String TAG = "NewUserRegistration AsyncTask";
	
	private EditText username;
	private EditText mail;
	private EditText pass;
	private EditText visiblePass;
	private EditText full_name;
	private ImageView newProfilePhoto;
	private Button send;
	private ProgressBar progressBar;
	private TextView errorMessage;
	private CheckBox passCheck;
	private RadioButton gender;
	private ImageView male,female;
	
	private final int CAMERA_RESULT = 200;
	private final int GALLERY_RESULT = 300;
	
	private Bitmap bitmap;
	
	private SharedPreferences newSettings = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_user_registration);
        username = (EditText) findViewById(R.id.username_editText);
        full_name = (EditText) findViewById(R.id.full_name_editText);
        pass = (EditText) findViewById(R.id.pass_editText);
        mail = (EditText) findViewById(R.id.mail_editText);
        visiblePass = (EditText) findViewById(R.id.visible_pass_editText);
        send = (Button) findViewById(R.id.register_user_button);
        progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        errorMessage = (TextView) findViewById(R.id.error_message);
        passCheck = (CheckBox) findViewById(R.id.pass_check);
        newProfilePhoto = (ImageView) findViewById(R.id.register_user_photo);
        
        male = (ImageView)findViewById(R.id.avatar_male);
        female = (ImageView)findViewById(R.id.avatar_female);
        
        newSettings = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        
        progressBar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        
        username.addTextChangedListener(new checkUsername(username, errorMessage));
        mail.addTextChangedListener(new checkMail(mail, errorMessage));
        full_name.addTextChangedListener(new checkFullName(full_name, errorMessage));
        pass.addTextChangedListener(new checkPass(pass, errorMessage));
        visiblePass.addTextChangedListener(new checkPass(visiblePass, errorMessage));
        
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                
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
        
        newProfilePhoto.setClickable(true);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick image from");
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		View new_layout = inflater.inflate(R.layout.pick_photo, null);
		builder.setView(new_layout);
		final AlertDialog choosePictureDialog = builder.create();
		
		Button cameraButton = (Button) new_layout.findViewById(R.id.launch_camera_button);
		Button galleryButton = (Button) new_layout.findViewById(R.id.choose_from_gallery_button);
		
		cameraButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
				startActivityForResult(cameraIntent, CAMERA_RESULT);
				choosePictureDialog.dismiss();	
			}
		});
	
		galleryButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, GALLERY_RESULT);
				choosePictureDialog.dismiss();
			}
		});
        
        newProfilePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				choosePictureDialog.show();
			}
		});
               
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
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		int scale = 1;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		newProfilePhoto.setImageBitmap(bitmap);
	}
    
    @Override
    protected void onStart() {
    	super.onStart();   	
    	
    	EasyTracker.getInstance().activityStart(this);
    	
    	send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!passCheck.isChecked()){
					if(checkUsername.returnValue == true && checkMail.returnValue == true && checkFullName.returnValue == true && checkPass.returnValue == true ){
						new SendNewUser().execute(username.getText().toString(), full_name.getText().toString(),mail.getText().toString(),pass.getText().toString());
					}
					else{
						Toast.makeText(getApplicationContext(), "All fields are required to Register", Toast.LENGTH_SHORT).show();
					}
				}	
				else
					Toast.makeText(getApplicationContext(), "Please hide your password to proceed", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	EasyTracker.getInstance().activityStop(this);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_user_registration, menu);
        return true;
    }
    
    public class SendNewUser extends AsyncTask<String, Integer, Void>{
    	private String usernameToSend;
    	private String mailToSend;
    	private String fullNameToSend;
    	private String passToSend;
    	
    	private StringBuilder stringBuilder;
    	private String statusResponse;
    	
    	private String iduser = null;
    	private String name = null;
    	
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
				bitmap.compress(CompressFormat.JPEG, 50, bos);
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
	    		SharedPreferences.Editor settingsEditor = newSettings.edit();
				settingsEditor.putString("user_id", iduser);
				settingsEditor.putString("user_name", name);
				settingsEditor.putString("user_status", statusResponse);
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
    
    public void showStatusMessage(){
    	int status = Integer.parseInt(newSettings.getString("user_status", "-1"));
    	switch(status){
    		case 1:
//    			/* Finish the current activity */
//    			finish();
    			
    			/* Start MainActivity */ 
    			Intent startWallActivity = new Intent(getApplicationContext(), MainActivity.class);
    			
    			/* AÑADIR ESTA ACTIVIDAD PARA SER ELIMINADA DESDE LA OTRA */
    			startWallActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(startWallActivity);
    			Log.i(TAG, "Starting MainActivity");
    			break;
    		case 0:
    			Log.i(TAG, "Couldn't start MainActivity");
    			break;
    		default:
    			Log.i(TAG, "Default Message: There is no 'user_status' value: You shouldn't be seeing this");
    			break;
    	}
    }
    
    
    public void ChooseGender(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.avatar_male:
                if (checked)
            //    	newProfilePhoto.setImageResource(male);
                break;
            case R.id.avatar_female:
                if (checked)
                    // Ninjas rule
                break;
        }
    }
    
}
