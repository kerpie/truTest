package com.creatiwebs.trustripes;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.creatiwebs.Constants.ConstantValues;
import com.google.zxing.client.android.CaptureActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	/* Variable for internal debug control */
	/* ERASE FOR PRODUCTION */
	private static final String TAG = Register.class.getSimpleName();
	
	/* Declaration of UI widgets */
	private TextView textCode;
	private Button btn_again, btn_register, backButton;
	private EditText registerName;
	
	/* Declaration of variable for session control */
	private SharedPreferences session;
	
	/* String values for JSON response */
	private String productName = "";	
	private String userSession = "";
	private String code = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		/* Instantiation of UI widgets */
		registerName = (EditText) findViewById(R.id.register_edittext_name);
		btn_again = (Button) findViewById(R.id.register_button_again);
		btn_register = (Button) findViewById(R.id.register_button_register);
		backButton = (Button) findViewById(R.id.backButton);
		textCode = (TextView) findViewById(R.id.register_textview_code);
		
		/* Get previous session data */
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		
		/* Get intent extra data */
		Intent t = getIntent();
		code = t.getStringExtra("BARCODE");
		
		/* Output of saved 'BARCODE' data */
		textCode.setText(code);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		/* Get User Id stored in SharedPreferences */
		userSession = session.getString("user_id", "No");
		
		/* just in case something happens with the data stored in SharedPreferences, unlikely to happen */
		if (userSession == "No"){
			Toast.makeText(getApplicationContext(), "This device will autodestroy in five seconds from now... RUN!!!", Toast.LENGTH_SHORT).show();
		}
		
		/* Instantiate and associate button events */
		btn_again.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN btn_again");
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		btn_register.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("MAIN", "Click EN btn_register");
				productName = registerName.getText().toString().trim();
				new Registerback().execute();
			}
		});
		
		backButton.setBackgroundResource(android.R.drawable.arrow_up_float);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	public class Registerback extends AsyncTask<Void, Integer, Void> {
		
		/* Internal variables for the thread */
		private StringBuilder stringBuilder;
		private String statusResponse = "";
		private String idproduct = "";
		private boolean showDiscoverer = false;
		private JSONObject jsonObject;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				/* Prepare variables for remote data check */
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://www.trustripes.com/dev/ws/ws-registerproduct.php";
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("productname", productName));
				param.add(new BasicNameValuePair("iduserdiscoverer",userSession));
				param.add(new BasicNameValuePair("codeupean", code));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				StatusLine status = responsePOST.getStatusLine();
				/* Filter what kind of response was obtained */
	    		/* Filtering http response 200 */
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = responsePOST.getEntity();
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line = null;
					stringBuilder = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					
					/* Converting obtained string into JSON object */
					jsonObject = new JSONObject(stringBuilder.toString());
					statusResponse = jsonObject.getString("status");
					if (Integer.parseInt(statusResponse) == 1) {
						showDiscoverer = true;
					} else {
						Log.d("Error Ambassador", "Error en la respuesta");
						showDiscoverer = false;
					}

					reader.close();
					inputStream.close();
				} else {
					/* Check Other Status Code */
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e(TAG, "CheckLoginData: Error ClientProtocolException");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Log.e(TAG, "CheckLoginData: Error UnsupportedEncodingException");
			} catch (IOException e) {
				Log.e(TAG, "CheckLoginData: Error IOException");
				e.printStackTrace();
			} catch (Exception e) {
				Log.e(TAG, "Unknown error");
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(Void result) {
			Intent intent = null;
			if (showDiscoverer) {
				intent = new Intent(getApplicationContext(), Discoverer.class);
				try {
					intent.putExtra("Product_id",  jsonObject.getInt("idproducto") );
					intent.putExtra("Product_name",  jsonObject.getString("producto") );
					intent.putExtra("Total_snacks",  jsonObject.getString("total") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);
				finish();
			} else {
				Log.d("Error Discoverer", "Error en la respuesta");
				Toast.makeText(getApplicationContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
