package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PreSnackin extends Activity {
	
	/* Constant for debugging */
	private final static String TAG = "PRE SNACKIN ACTIVITY";
	
	private final int CAMERA_RESULT = 200;
	private final int GALLERY_RESULT = 300;
	
	private ProgressDialog dialog;
	
	/* Declaration of UI widgets */
	Button snackAgainButton, realSnackInButton, backButton;
	ImageView image;
	RatingBar ratingBar;
	EditText commentBox;
	TextView titleName;
	
	Bitmap bitmap = null; 
	Bitmap bitmapToUpload = null;
	
	String obtainedCode, productId, userId, productName, productPhoto;
	String finalImagePath;
	String profileImagePath;
	String ratingValue;
	
	SendSnackIn sendSnackin;
	
	boolean snackStatus;
	boolean commentStatus;
	boolean ratingStatus;
	boolean snackPhotoStatus;
	boolean ratingBarValueChanged;
	
	boolean loadedImage;
	boolean isAnotherPhoto=false;
	
	LoadPhoto loadPhoto;
	
	SharedPreferences session;
	
	private SharedPreferences developmentSession = null;
	String id;
	int realId;
	
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
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);		
		commentBox = (EditText)findViewById(R.id.presnack_edittext_comment);
		titleName = (TextView) findViewById(R.id.presnack_textview_tittle);
		
		sendSnackin = new SendSnackIn();
		
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		
		obtainedCode = getIntent().getStringExtra("BARCODE");
		productId = getIntent().getStringExtra("PRODUCT_ID");
		productName = getIntent().getStringExtra("PRODUCT_NAME");
		productPhoto = getIntent().getStringExtra("PRODUCT_PHOTO");
		
		titleName.setText(productName);
		
		ratingBar.setRating(0);
		ratingValue = "0";
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				ratingBarValueChanged = true;
				ratingValue = String.valueOf(rating);
			}
		});
		
		loadPhoto = new LoadPhoto();
		
		if(savedInstanceState != null){
			boolean imageWasLoaded = savedInstanceState.getBoolean("loadingStatus");
			if(imageWasLoaded){
				String path = savedInstanceState.getString("ImagePath");
				if(!path.isEmpty()){
					decodeFile(path, 200, 200);
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
				if(ConstantValues.getConnectionStatus(getApplicationContext())){
					sendSnackin.execute();
					realSnackInButton.setClickable(false);
				}
				else{
					Toast.makeText(getApplicationContext(), "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		image.setClickable(true);
		
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
		
		image.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				choosePictureDialog.show();
			}
		});
		
		developmentSession = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
        id = developmentSession.getString("user_id", "-1");
        realId = Integer.parseInt(id);
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
	
	private File getTempFile(){
		  //it will return /sdcard/image.tmp
		  final File path = new File( Environment.getExternalStorageDirectory(),"TruStripes");
		  if(!path.exists())
		    path.mkdir();
		  return new File(path, "image.tmp");
	}
	
	public void decodeFile(String filePath, int requiredHeight, int requiredWidth) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath,o);
		int scale = 0;
		if( o.outHeight > requiredHeight || o.outWidth > requiredWidth ){
			if(o.outWidth > o.outHeight){
				scale = Math.round(Math.round((float)o.outHeight/(float)o.outWidth));
			}else{
				scale = Math.round(Math.round((float)o.outWidth/(float)o.outHeight));
			}
		}
		
		o = new BitmapFactory.Options();
		o.inSampleSize = scale;
		o.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, o);		
		finalImagePath = filePath;		
		image.setImageBitmap(bitmap);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		isAnotherPhoto = true;
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
							decodeFile(filePath, 200, 200);
							finalImagePath = filePath;
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
						finalImagePath = file.getAbsolutePath();	
						decodeFile(finalImagePath, 200, 200);				
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
	
	@Override
	protected void onStart() {
		super.onStart();
		
		/* Implementation of Google Analytics for Android */
		if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStart(this);
    	}
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		/* Implementation of Google Analytics for Android */
		if(ConstantValues.URL == "http://www.trustripes.com" && !ConstantValues.isInDevelopmentTeam(realId)){
    		EasyTracker.getInstance().activityStop(this);
    	}
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
			finalImagePath = Environment.getExternalStorageDirectory()+"/TruStripes/image_preview.jpg";
			File directory = new File(finalImagePath);
			FileOutputStream outStream;
		    try {

		        outStream = new FileOutputStream(directory);
		        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outStream); 
		        /* 100 to keep full quality of the image */

		        outStream.flush();
		        outStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

			decodeFile(finalImagePath, 100, 100);
		}
	}
	
	
	public class SendSnackIn extends AsyncTask<Void, Integer, Void>{
		
    	private String statusResponse = null;
    	private String responseMessage = null;
    	private String snackCount = null;
    	private String ambassadorStatus = null;
    	private StringBuilder stringBuilder = null;
    	private String snackinId = null;    	
    	private String averagePoints = "0";
		
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		dialog = ProgressDialog.show(PreSnackin.this, "Uploading","Please wait...", true);
    	}
    	
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
	    			HttpEntity new_entity = responsePOST.getEntity();
	    			InputStream inputStream = new_entity.getContent();
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
	    				/* Snack register ok */
	    				snackCount = jsonObject.getString("total");
	    				ambassadorStatus = jsonObject.getString("foco");
	    				snackinId = jsonObject.getString("idsnackin");
	    				snackStatus = true;
	    			}
	    			else{	    		
	    				/* Snack register failed */
	    				snackStatus = false;
	    			}
	    			    			
	    			reader.close();
	    			inputStream.close();
	    		}
	    		else{
	    			/* Check Other Status Code */
	    		}
	    		
	    		/* If another image was choosen upload */
	    		if(isAnotherPhoto){
		    		client =  new DefaultHttpClient();   		
		            postURL = ConstantValues.URL + "/ws/ws-registersnackinphoto.php";
		            post = new HttpPost(postURL);    
		    		MultipartEntity entity = new MultipartEntity();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 30, bos);
					byte[] data = bos.toByteArray();
					entity.addPart("uploadedfile", new ByteArrayBody(data, "myImage.jpg"));
					entity.addPart("idproduct", new StringBody(productId));
					entity.addPart("iduser", new StringBody(userId));
					entity.addPart("idsnackin", new StringBody(snackinId));
					post.setEntity(entity);
					responsePOST = client.execute(post);    		
		    		status = responsePOST.getStatusLine();
		    		if(status.getStatusCode() == HttpStatus.SC_OK){
		    			HttpEntity new_entity = responsePOST.getEntity();
		    			InputStream inputStream = new_entity.getContent();
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
		    				/* Snack register ok */
		    				snackPhotoStatus = true;
		    			}
		    			else{	    		
		    				/* Snack register failed */
		    				snackPhotoStatus = false;
		    			}
		    			    			
		    			reader.close();
		    			inputStream.close();
		    		}
		    		else{
		    			/* Check Other Status Code */
		    		}
	    		}
            		    		
	    		/* Preparing to send rating value */  		
	            postURL = ConstantValues.URL + "/ws/ws-registerrating.php";
	            post = new HttpPost(postURL); 
	            
	            param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("idsnackin",snackinId));
	            param.add(new BasicNameValuePair("idproduct",productId));
            	param.add(new BasicNameValuePair("rating",ratingValue));
            	ent = new UrlEncodedFormEntity(param);
	            post.setEntity(ent);
	            responsePOST = client.execute(post);    		
	    		status = responsePOST.getStatusLine();
	    		/* Filter what kind of response was obtained */
	    		/* Filtering http response 200 */
	    		if(status.getStatusCode() == HttpStatus.SC_OK){
	    			HttpEntity new_entity = responsePOST.getEntity();
	    			InputStream inputStream = new_entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    			String line = null;
	    			stringBuilder = new StringBuilder();
	    			while((line = reader.readLine()) != null){
	    				stringBuilder.append(line);
	    			}
	    			
	    			/* Converting obtained string into JSON object */
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			statusResponse = jsonObject.getString("status");
	    			averagePoints = jsonObject.getString("promedio");
	    			if(Integer.parseInt(statusResponse) == 1){
	    				/* Rating value register ok */
	    				ratingStatus= true;
	    			}
	    			else{	    		
	    				/* Rating value register failed */
	    				ratingStatus = false;
	    			}
	    		}
			
	    		String comment = commentBox.getText().toString();
	    		
	    		if(!comment.isEmpty()){
	    			/* Preparing to send comment */  		
		            postURL = ConstantValues.URL + "/ws/ws-registercomment.php";
		            post = new HttpPost(postURL); 
		            param = new ArrayList<NameValuePair>();
		            param.add(new BasicNameValuePair("idsnackin",snackinId));
		            param.add(new BasicNameValuePair("idproduct",productId));
		            param.add(new BasicNameValuePair("comment",comment));
		            ent = new UrlEncodedFormEntity(param);
		            post.setEntity(ent);
		            responsePOST = client.execute(post);    		
		    		status = responsePOST.getStatusLine();
		    		/* Filter what kind of response was obtained */
		    		/* Filtering http response 200 */
		    		if(status.getStatusCode() == HttpStatus.SC_OK){
		    			HttpEntity new_entity = responsePOST.getEntity();
		    			InputStream inputStream = new_entity.getContent();
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
		    				/* Comment register ok */
		    				commentStatus= true;
		    			}
		    			else{	    		
		    				/* Comment register failed */
		    				commentStatus = false;
		    			}
		    		}
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
			if(snackStatus){
				Intent intent = new Intent(getApplicationContext(), Snackin.class);
				intent.putExtra("BARCODE", obtainedCode);
				intent.putExtra("PRODUCT_NAME", productName);
				intent.putExtra("PRODUCT_ID",productId);
				intent.putExtra("PRODUCT_PHOTO", productPhoto);
				intent.putExtra("AMBASSADOR_STATUS", ambassadorStatus);
				intent.putExtra("productPath", finalImagePath);
				intent.putExtra("RATING", averagePoints);
				startActivity(intent);
				finish();
			}
			else{
				if (dialog.isShowing())
					dialog.dismiss();
				sendSnackin = new SendSnackIn();
				realSnackInButton.setClickable(true);
				Toast.makeText(getApplicationContext(), "Something wrong happened, please try again", Toast.LENGTH_SHORT ).show();
			}
		}
	}
}
