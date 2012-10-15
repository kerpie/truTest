package com.creatiwebs.trustripes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	EditText loginUsername = null;
	EditText loginPass = null;
	Button loginButton = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
               
        loginUsername = (EditText) findViewById(R.id.login_username);
        loginPass = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
    }

    public class CheckLoginData extends AsyncTask<Void, Integer, Void>{
    	@Override
    	protected Void doInBackground(Void... params) {
    		// TODO Auto-generated method stub
    		return null;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		super.onProgressUpdate(values);
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
}
