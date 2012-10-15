package com.creatiwebs.trustripes;

import android.os.AsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

public class LoginActivity extends Activity {

	EditText loginUsername = null;
	EditText loginPass = null;
	Button loginButton = null;
	private static String APP_ID = "274388489340768"; // Facebook ID AlertaMóvil

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	Button btnFb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		loginUsername = (EditText) findViewById(R.id.login_username);
		loginPass = (EditText) findViewById(R.id.login_password);
		loginButton = (Button) findViewById(R.id.login_button);
		btnFb = (Button) findViewById(R.id.login_facebook_button);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {				
			}
		});

		btnFb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Button", "button Clicked FB");
				loginToFacebook();
			}
		});

	}

	public class CheckLoginData extends AsyncTask<Void, Integer, Void> {
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

	// Get Data From jSon?
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

}
