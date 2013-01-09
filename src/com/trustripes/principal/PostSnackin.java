package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trustripes.Constants.ConstantValues;
import com.trustripes.adapters.ItemAdapter;
import com.trustripes.interfaces.ItemType;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PostSnackin extends Activity {
	
	Button backButton;
	ImageView productPhoto;
	TextView productName;
	TextView productCategoryName;
	RatingBar productRatingBar;
	ListView listData;
	
	String imagePath;
	String productId;
	String stringProductName;
	String ratingValue;
	String snackinId;
	Intent intent;	
	Bitmap bitmap;
	
	ArrayList<ItemType> items = new ArrayList<ItemType>();
	
	SharedPreferences session;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_snackin);
		
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);
		
		productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
        
        listData = (ListView) findViewById(R.id.ps_listview);
        
        intent = getIntent();
        imagePath = intent.getStringExtra("PRODUCT_PATH");
        productId = intent.getStringExtra("PRODUCT_ID");
        stringProductName = intent.getStringExtra("PRODUCT_NAME");
        ratingValue = intent.getStringExtra("PRODUCT_RANKING");
        snackinId = intent.getStringExtra("SNACKIN_ID");
        
        productName.setText(stringProductName);
        productRatingBar.setRating(Float.parseFloat(ratingValue));
        decodeFile(imagePath);
        
        backButton = (Button)findViewById(R.id.backButton);
    	backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
    	
    	new LoadData().execute();
	}
	
    public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		int scale = 1;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		productPhoto.setImageBitmap(bitmap);
	}
    
    public class LoadData extends AsyncTask<Void, Integer, Void>{
    	
    	StringBuilder stringBuilder;
    	JSONObject pointsResponse;
    	JSONObject snackResponse;
    	boolean gotPoints = false;
    	boolean gotInformation = false;
    	
    	@Override
    	protected Void doInBackground(Void... params) {
    		   		
    		try{
    			
    			HttpClient client = new DefaultHttpClient();
    			String postURL = ConstantValues.URL + "/ws/ws-ruleuser.php";
    			HttpPost post = new HttpPost(postURL);
    			List<NameValuePair> param = new ArrayList<NameValuePair>();
    			
    			param.add(new BasicNameValuePair("idProducto", productId));
    			param.add(new BasicNameValuePair("idUsuario", session.getString("user_id", "-1")));
    			param.add(new BasicNameValuePair("idSnackin", snackinId));
    			
    			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
    			post.setEntity(ent);
    			HttpResponse responsePOST = client.execute(post);
    			StatusLine status = responsePOST.getStatusLine();
    			
    			if(status.getStatusCode() == HttpStatus.SC_OK){
    				HttpEntity new_entity = responsePOST.getEntity();
    				InputStream inputStream = new_entity.getContent();
    				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    				String line = null;
    				stringBuilder = new StringBuilder();
    				while((line = reader.readLine()) != null){
    					stringBuilder.append(line);
    				}
    				
    				pointsResponse = new JSONObject(stringBuilder.toString());
    				String statusResponse = pointsResponse.getString("status");
    				if(Integer.parseInt(statusResponse) == 1){
    					gotPoints = true;
    				}
    				else{
    					gotPoints = false;
    				}
    				
    				reader.close();
					inputStream.close();
    			}
    			else{
    				
    			}
    			
    			postURL = ConstantValues.URL + "/ws/ws-productodetalle.php";
    			post = new HttpPost(postURL);
    			param = new ArrayList<NameValuePair>();
    			
    			param.add(new BasicNameValuePair("idproduct", productId));
    			
    			ent = new UrlEncodedFormEntity(param);
    			post.setEntity(ent);
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
    				
    				snackResponse = new JSONObject(stringBuilder.toString());
    				String statusResponse = snackResponse.getString("status");
    				if(Integer.parseInt(statusResponse) == 1){
    					gotInformation = true;
    				}
    				else{
    					gotInformation = false;
    				}
    				
    				reader.close();
					inputStream.close();
    			}
    			
    		}catch(Exception e){
    			
    		}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		try{
	    		if(gotPoints){
	    			
	    		}
	    		if(gotInformation){
	    			items.add(new HeaderItem("Ambassador"));
	    			items.add(new RegularItem(ConstantValues.URL + ConstantValues.PhotoUrl(snackResponse.getString("fotoembajador")),snackResponse.getString("embajadorDisplay"),snackResponse.getString("embajador")));
	    			
	    			if(Integer.parseInt(snackResponse.getString("statusComentarios")) == 1){
	    				JSONArray array = new JSONArray(snackResponse.getString("datosComentarios"));
	    				for(int it = 0; it < array.length(); it++){
	    					JSONObject obj = array.getJSONObject(it);
	    					items.add(new RegularItem(ConstantValues.URL+ConstantValues.PhotoUrl("/public/user/"+obj.getString("user_id")+"/newProfileImage.jpg"), obj.getString("displayname") + obj.getString("username"), obj.getString("COMMENT")));
	    				}
	    			}
	    			else{
	    				items.add(new MessageItem("No existen comentarios para este producto"));
	    			}
	    		}
	    		ItemAdapter adapter = new ItemAdapter(getApplicationContext(), items);
    			listData.setAdapter(adapter);
    		}catch(JSONException e){
    			
    		}
    	}
    }

}
