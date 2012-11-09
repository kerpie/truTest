package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PreSnackin extends Activity {
	
	/* Constant for debugging */
	private final static String TAG = "PRE SNACKIN ACTIVITY";
	
	/* Declaration of UI widgets */
	Button snackAgainButton, realSnackInButton, backButton;
	ImageView image;
	
	Bitmap bitmap; 
	
	String obtainedCode, productId, userId, productName, productPhoto;
	boolean canSnack;
	
	SharedPreferences session;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pre_snackin);

		/* Instantiation of UI widgets */
		snackAgainButton = (Button) findViewById(R.id.presnack_button_again);
		realSnackInButton=(Button)findViewById(R.id.presnack_button_Snack);
		backButton = (Button)findViewById(R.id.backButton);
		image = (ImageView) findViewById(R.id.presnack_imageview_add);
		
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		
		obtainedCode = getIntent().getStringExtra("BARCODE");
		productId = getIntent().getStringExtra("PRODUCT_ID");
		productName = getIntent().getStringExtra("PRODUCT_NAME");
		productPhoto = getIntent().getStringExtra("PRODUCT_PHOTO");
		
		new LoadPhoto().execute();
		
		userId = session.getString("user_id", "No user");
		
		/* Instantiation and button event association */
		snackAgainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN snackAgainButton");
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		realSnackInButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new SendSnackIn().execute();
				realSnackInButton.setClickable(false);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pre_snackin, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	public class LoadPhoto extends AsyncTask<Void, Integer, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			if(productPhoto.length() >= 10){
				URL myFileUrl =null; 
				try {
					myFileUrl= new URL(ConstantValues.URL+"/ws/productphoto/"+ productId +"/thumbnails/"+productPhoto);
					HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			image.setImageBitmap(bitmap);
			//Toast.makeText(getApplicationContext(), "Done Loading!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public class SendSnackIn extends AsyncTask<Void, Integer, Void>{
		
    	private String statusResponse = null;
    	private String responseMessage = null;
    	private String snackCount = null;
    	private String ambassadorStatus = null;
    	private StringBuilder stringBuilder = null;
		
		@Override
		protected Void doInBackground(Void... params) {
			try{
    			/* Prepare variables for remote data check */
	    		HttpClient client =  new DefaultHttpClient();   		
	            String postURL = "http://www.trustripes.com/dev/ws/ws-registersnackin.php";
	            HttpPost post = new HttpPost(postURL); 
	            List<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("codeupean",obtainedCode));
	            param.add(new BasicNameValuePair("idproduct",productId));
	            param.add(new BasicNameValuePair("iduser",userId));
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
	    				snackCount = jsonObject.getString("total");
	    				ambassadorStatus = jsonObject.getString("foco");
		    			canSnack = true;
	    			}
	    			else{	    		
	    				/* Can't Login */
	    				canSnack = false;
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
			if(canSnack){
				Intent intent = new Intent(getApplicationContext(), Snackin.class);
				intent.putExtra("BARCODE", obtainedCode);
				intent.putExtra("PRODUCT_NAME", productName);
				intent.putExtra("PRODUCT_PHOTO", productPhoto);
				intent.putExtra("AMBASSADOR_STATUS", ambassadorStatus);
				startActivity(intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "Something wrong happened, please try again", Toast.LENGTH_SHORT ).show();
			}
		}
	}
}
