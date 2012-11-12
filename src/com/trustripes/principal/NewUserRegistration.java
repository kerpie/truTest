package com.trustripes.principal;

import java.io.BufferedReader;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.trustripes.Constants.ConstantValues;
import com.trustripes.Events.checkFullName;
import com.trustripes.Events.checkMail;
import com.trustripes.Events.checkPass;
import com.trustripes.Events.checkUsername;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserRegistration extends Activity{

	private final static String TAG = "NewUserRegistration AsyncTask";
	
	private EditText username;
	private EditText mail;
	private EditText pass;
	private EditText visiblePass;
	private EditText full_name;
	private Button send;
	private ProgressBar progressBar;
	private TextView errorMessage;
	private CheckBox passCheck;
	
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
        newSettings = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        
        progressBar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        
        username.addTextChangedListener(new checkUsername(username, errorMessage));
        mail.addTextChangedListener(new checkMail(mail, errorMessage));
        full_name.addTextChangedListener(new checkFullName(full_name, errorMessage));
        pass.addTextChangedListener(new checkPass(pass, errorMessage));
        visiblePass.addTextChangedListener(new checkPass(visiblePass, errorMessage));
                
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
    			
    			/* Prepare variables for remote data check */
	    		HttpClient client =  new DefaultHttpClient();   		
	            String postURL = ConstantValues.URL + "/ws/ws-userregister.php";
	            HttpPost post = new HttpPost(postURL); 
	            List<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("email",mailToSend));
	            param.add(new BasicNameValuePair("username",usernameToSend));
	            param.add(new BasicNameValuePair("displayname",fullNameToSend));
	            param.add(new BasicNameValuePair("password",passToSend));
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


    
}
