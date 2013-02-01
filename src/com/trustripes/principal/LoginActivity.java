package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Environment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.trustripes.Constants.ConstantValues;

public class LoginActivity extends Activity {
	
	/* Variable for Internal Debug */
	private static final String TAG = "LoginActivity";
	
	/* Variable for Internal Control */
	public boolean canLogin = false;
	
	/* Declaration of UI widgets */
	private EditText loginUsername = null;
	private EditText loginPass = null;
	private Button loginButton = null;
	private TextView errorText = null;
	private TextView registerText = null;
	private ProgressBar progressBar = null;
	private Button btnFb = null;
	
	/* ID for Facebook connect (Temporal data) */
	private static String APP_ID = "274388489340768"; // Facebook ID AlertaMóvil
	
	/* Session variable */
	private SharedPreferences newSettings = null;
	
	/* Instance of Facebook Class */
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	
	/* FB session (or it looks like so) */
	private SharedPreferences mPrefs;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        /* Instantiate widget variables */
        loginUsername = (EditText) findViewById(R.id.login_username);
        loginPass = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        errorText = (TextView) findViewById(R.id.login_error_message);        
        newSettings = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        registerText = (TextView) findViewById(R.id.login_createUser_textView);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        //btnFb = (Button) findViewById(R.id.login_facebook_button);
        
        /* AsyncTask to connect with FB */
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        
        /* Removing visibility of temporal widgets */
        /* These will be shown according to the status received by the server */
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        
        developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(id);
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	
    	/* Implementation of Google Analytics for Android */
    	if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
    		EasyTracker.getInstance().activityStart(this);    		
    	}
    
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
    	/* Implementation of Google Analytics for Android */
    	if( !(ConstantValues.URL == "http://www.trustripes.com" && ConstantValues.isInDevelopmentTeam(realId))){
    		EasyTracker.getInstance().activityStop(this);
    	}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
//    	btnFb.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				loginToFacebook();
//			}
//		});
    	
        loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(ConstantValues.getConnectionStatus(getApplicationContext())){
					/* Check if there is data in the EditText */
					if(loginUsername.getText().toString().length() <= 3 || loginPass.getText().toString().length() <= 3){
						/* If text inserted in the textviews are lower than 3 */
						/* By definition the username can't be smaller than 5 characters */
						errorText.setVisibility(View.VISIBLE);
						errorText.setText(R.string.fill_fields_to_login);
					}else{
						/* Start AsyncTask */
						new CheckLoginData().execute();
						/* Hide the TextView for errors */
						errorText.setVisibility(View.GONE);
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        registerText.setOnClickListener(new View.OnClickListener(){	
        	public void onClick(View v){
        		/* Start an intent to register a new user */
        		Intent intent = new Intent(getApplicationContext(),NewUserRegistration.class);
        		startActivity(intent);
        	}
        });
    }
    
    /* Asynctask to check the login fields with the server */
    public class CheckLoginData extends AsyncTask<Void, Integer, Void>{
    	
    	/* Variables to send to the server */
    	private String username = null;
    	private String pass = null;
    	
    	/* Variables to receive the data sent by the server */
    	private String iduser = null;
    	private String name = null;
    	
    	private String full_name = null;
    	private String mail = null;
    	private String photoUrl = null;
    	
    	private String statusResponse = null;
    	private String responseMessage = null;
    	
    	/* Temporal variable to store de json response */
    	private StringBuilder stringBuilder = null;
    	
    	@Override
    	protected void onPreExecute() {
    		/* Get string values from the text boxes on UI */
    		username = loginUsername.getText().toString().trim();
    		pass = loginPass.getText().toString().trim();
    		
    		/* Show the progressBar */
    		progressBar.setVisibility(View.VISIBLE);
    		
    		/* Hide the textview for errors */
    		errorText.setVisibility(View.GONE);
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params) {    		
    		try{
    			/* Prepare variables for remote data check */
	    		HttpClient client =  new DefaultHttpClient();   		
	    		String postURL = ConstantValues.URL+"/ws/ws-validatelogin.php";
	    		HttpPost post = new HttpPost(postURL); 
	    		
	    		List<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("user",username));
	            param.add(new BasicNameValuePair("password",pass));
	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
	            post.setEntity(ent);
	            HttpResponse responsePOST = client.execute(post);
	    		StatusLine status = responsePOST.getStatusLine();
	    		
	    		/* Filter what kind of response was obtained */
	    		/* Filtering http response 200 */
	    		if(status.getStatusCode() == HttpStatus.SC_OK){
	    			HttpEntity entity = responsePOST.getEntity();
	    			InputStream inputStream = entity.getContent();	    			
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    			String line = null;
	    			stringBuilder = new StringBuilder();
	    			while((line = reader.readLine()) != null){
	    				stringBuilder.append(line);
	    			}
	    			
	    			/* Converting obtained string into JSON object */
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			
	    			/* Obtaining the data we need*/
	    			statusResponse = jsonObject.getString("status");
	    			
	    			/* Message sent by the server */
	    			responseMessage = jsonObject.getString("msj");
	    			
	    			/* Response from the server if the user can login */
	    			if(Integer.parseInt(statusResponse) == 1){
	    				/* Success Login */
	    				iduser = jsonObject.getString("iduser");
		    			name = jsonObject.getString("username");
		    			
		    			full_name = jsonObject.getString("display");
		    			mail = jsonObject.getString("email");
		    			photoUrl = jsonObject.getString("photo");
		    			
		    			canLogin = true;
	    			}
	    			else{	    		
	    				/* Can't Login */
	    				canLogin = false;
	    			}
	    			    			
	    			reader.close();
	    			inputStream.close();
	    		}
	    		else{
	    			/* Check Other Status Code */
	    			/* response 404 for example */
	    			canLogin = false;
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

    		/* if login was successfull save data */
    		if(canLogin){
	    		SharedPreferences.Editor settingsEditor = newSettings.edit();
	    		settingsEditor.putString("user_status", statusResponse);
	    		
	    		settingsEditor.putString("user_id", iduser);
				settingsEditor.putString("user_name", name);
				settingsEditor.putString("user_full_name", full_name);
				settingsEditor.putString("user_mail", mail);
				
				int id = Integer.parseInt(iduser);
				settingsEditor.putString("user_external_image_path", Environment.getExternalStorageDirectory()+"/TruStripes/"+ConstantValues.codeName(id)+".jpg");
				settingsEditor.putString("user_photo_url", photoUrl);
				
				settingsEditor.putBoolean("show_snack_help", true);
				settingsEditor.commit();
				showStatusMessage();
    		}
    		else{
        		/* If can't login display message sent by the server */
       			errorText.setText(responseMessage);
       			errorText.setVisibility(View.VISIBLE);
       			progressBar.setVisibility(View.GONE);
    		}
    	}
    }
    
    public void showStatusMessage(){
    	int status = Integer.parseInt(newSettings.getString("user_status", "-1"));
    	switch(status){
    		case 1:
    			/* Finish the current activity */
    			finish();
    			/* Start MainActivity */ 
    			Intent startWallActivity = new Intent(getApplicationContext(), MainActivity.class);
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

	//Login to FB
	public void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						public void onCancel() {
							// Function to handle cancel event(Agree)
						}

						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							getProfile();
						}

						public void onError(DialogError error) {
							// Function to handle error

						}

						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});

		}

	}

	// GET DATA USERS FROM FB
	public void getProfile() {
		mAsyncRunner.request("me", new RequestListener() {
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					// getting name of the user
					final String name = profile.getString("name");

					// getting email of the user
					final String email = profile.getString("email");

					// getting id of the user
					final String id = String.valueOf(profile.getLong("id"));

					runOnUiThread(new Runnable() {

						public void run() {
							Toast.makeText(
									getApplicationContext(),
									"Name: " + name + "\nEmail: " + email
											+ "\nId" + id, Toast.LENGTH_LONG)
									.show();
						}

					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onIOException(IOException e, Object state) {
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			public void onFacebookError(FacebookError e, Object state) {
			}
		});

	}

	// Get Data From json?
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}