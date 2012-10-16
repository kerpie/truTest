package com.creatiwebs.trustripes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.creatiwebs.Constants.ConstantValues;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";
	private final String USER_DATA = "UserDataPreferences";
	
	EditText loginUsername = null;
	EditText loginPass = null;
	Button loginButton = null;
	
	SharedPreferences newSettings = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
               
        loginUsername = (EditText) findViewById(R.id.login_username);
        loginPass = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        
        newSettings = getSharedPreferences(USER_DATA, MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				new CheckLoginData().execute();
			}
		});
    }

    public class CheckLoginData extends AsyncTask<Void, Integer, Void>{
    	
    	String username = null;
    	String pass = null;
    	StringBuilder stringBuilder = null;
    	String iduser = null;
    	String name = null;
    	String msj = null;
    	
    	@Override
    	protected void onPreExecute() {
    		username = loginUsername.getText().toString().trim();
    		pass = loginPass.getText().toString().trim();
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params) {    		
    		try{
	    		HttpClient client =  new DefaultHttpClient();
	    		HttpGet httpGet = new HttpGet(ConstantValues.getLoginUrl(username, pass));
	    		HttpResponse httpResponse = client.execute(httpGet);
	    		StatusLine status = httpResponse.getStatusLine();
	    		int estado = status.getStatusCode();
	    		if(status.getStatusCode() == HttpStatus.SC_OK){
	    			HttpEntity entity = httpResponse.getEntity();
	    			InputStream inputStream = entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    			String line = null;
	    			stringBuilder = new StringBuilder();
	    			while((line = reader.readLine()) != null){
	    				stringBuilder.append(line);
	    			}
	    			
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			iduser = jsonObject.getString("iduser");
	    			name = jsonObject.getString("nombre"); 
	    			msj = jsonObject.getString("msj");
	    			    			
	    			reader.close();
	    			inputStream.close();
	    		}
	    		else{
	    			/* Check Other Status Code */
	    		}
	    	}catch(ClientProtocolException e){
    			e.printStackTrace();
    			Log.e(TAG,"CheckLoginData: Error ClientProtocolException");
    		} catch (IOException e) {
    			Log.e(TAG,"CheckLoginData: Error IOException");
				e.printStackTrace();
			}catch(Exception e){
				Log.e(TAG, "Unknown error");
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		SharedPreferences.Editor settingsEditor = newSettings.edit();
			settingsEditor.putString("user_id", iduser);
			settingsEditor.putString("user_name", name);
			settingsEditor.putString("user_status", msj);
			settingsEditor.commit();
			showStatusMessage();
    	}
    }
    
    public void showStatusMessage(){
    	int status = Integer.parseInt(newSettings.getString("user_status", "No data"));
    	switch(status){
    		case 1:
    			finish();
    			Intent startWallActivity = new Intent(getApplicationContext(), WallActivity.class);
    			startActivity(startWallActivity);
    			break;
    		case 0:
    			/* show error message */
    			break;
    		default:
    			break;
    	}
    } 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
}