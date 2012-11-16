package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

public class PreSnackin extends Activity {
	
	/* Constant for debugging */
	private final static String TAG = "PRE SNACKIN ACTIVITY";
	
	/* Declaration of UI widgets */
	Button snackAgainButton, realSnackInButton, backButton;
	ImageView image;
	
	Bitmap bitmap; 
	
	String obtainedCode, productId, userId, productName, productPhoto;
	String finalImagePath;
	String profileImagePath;
	boolean canSnack;
	boolean loadedImage;
	
	LoadPhoto loadPhoto;
	
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
		
		loadPhoto = new LoadPhoto();
		
		if(savedInstanceState != null){
			boolean imageWasLoaded = savedInstanceState.getBoolean("loadingStatus");
			if(imageWasLoaded){
				String path = savedInstanceState.getString("ImagePath");
				if(!path.isEmpty()){
					decodeFile(path);
				}
			}else{
				loadPhoto.execute();
			}
		}
		else{
			loadPhoto.execute();
		}
		
		userId = session.getString("user_id", "No user");
		
		/* Instantiation and button event association */
		snackAgainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click en snackAgainButton");
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
	
	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		finalImagePath = filePath;
		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
//		while (true) {
//			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//				break;
//			width_tmp /= 2;
//			height_tmp /= 2;
//			scale *= 2;
//		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		image.setImageBitmap(bitmap);
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("ImagePath", finalImagePath);
		Toast.makeText(getApplicationContext(),  loadPhoto.getStatus().toString(), Toast.LENGTH_LONG).show();
		if(loadPhoto.getStatus() == Status.FINISHED || loadPhoto.getStatus() == Status.PENDING){
			loadedImage = true;
		}
		else{
			loadPhoto.cancel(false);
			if(loadPhoto.isCancelled()){
				loadedImage = false;
			}
		}
		
		outState.putBoolean("loadingStatus", loadedImage);
		Toast.makeText(getApplicationContext(), "loadedImage: "+loadedImage, Toast.LENGTH_SHORT).show();
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
			finalImagePath = Environment.getExternalStorageDirectory()+"/TruStripes/image_preview.png";
			File directory = new File(finalImagePath);
			FileOutputStream outStream;
		    try {

		        outStream = new FileOutputStream(directory);
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
		        /* 100 to keep full quality of the image */

		        outStream.flush();
		        outStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

			image.setImageBitmap(bitmap);
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
	            String postURL = ConstantValues.URL + "/ws/ws-registersnackin.php";
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
				intent.putExtra("productPath", finalImagePath);
				startActivity(intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "Something wrong happened, please try again", Toast.LENGTH_SHORT ).show();
			}
		}
	}
}
