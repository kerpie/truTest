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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trustripes.Constants.ConstantValues;
import com.trustripes.Constants.LifeGuard;
import com.trustripes.adapters.CustomViewPagerAdapter.SnackImageLoad;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDescription extends Activity {

	ImageView productPhoto;
	TextView productName;
	TextView productCategoryName;
	RatingBar productRatingBar;
	
	ImageView ambassadorPhoto;
	TextView ambassadorName;
	
	ImageView discovererPhoto;
	TextView discovererName;
	
	String imagePath;
	String productId;
	String stringProductName;
	String ratingValue;
	Intent intent;
	
	Bitmap bitmap;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_description);
        productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
        
        ambassadorPhoto = (ImageView) findViewById(R.id.postSnackin_ambassador_image);
        ambassadorName = (TextView) findViewById(R.id.postSnackin_ambassador_name);
        
        discovererPhoto = (ImageView) findViewById(R.id.postSnackin_discoverer_image);
        discovererName = (TextView) findViewById(R.id.postSnackin_discoverer_name);
       
        intent = getIntent();
        String productId = intent.getStringExtra("PRODUCT_ID");
                
        productCategoryName.setVisibility(View.GONE);
        new ProductDetail().execute(productId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_snackin, menu);
        return true;
    }
    
    public class ProductDetail extends AsyncTask<String, Integer, Void>{
    	StringBuilder stringBuilder;
    	String statusResponse;
    	JSONObject SnackjsonObject;
    	boolean gotInformation;
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		try {

    			String id = params[0];
    			
				/* Prepare variables for remote data check */
				HttpClient client = new DefaultHttpClient();
				String postURL = ConstantValues.URL+ "/ws/ws-productodetalle.php";
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("idproduct", id));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				StatusLine status = responsePOST.getStatusLine();
				/* Filter what kind of response was obtained */
				/* Filtering http response 200 */
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity new_entity = responsePOST.getEntity();
					InputStream inputStream = new_entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line = null;
					stringBuilder = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					SnackjsonObject = new JSONObject(stringBuilder.toString());
					statusResponse = SnackjsonObject.getString("status=");
					if (Integer.parseInt(statusResponse)== 1) {
						gotInformation = true;					
					} else {
						gotInformation = false;
					}
					reader.close();
					inputStream.close();
					
				} else {
					/* Check Other Status Code */
				}			
			} catch (Exception e) {			
				e.printStackTrace();
			}
			return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		if(gotInformation){
    			try {
    				discovererName.setText(SnackjsonObject.getString("descubridor"));
    				String tmpAmbassadorName = SnackjsonObject.getString("embajador");
    				if(tmpAmbassadorName == "false"){
    					
    					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ambassadorName.getLayoutParams();
    					params.setMargins(30, 10, 30, 10); //substitute parameters for left, top, right, bottom
    					ambassadorName.setLayoutParams(params);
    					ambassadorName.setText("This product doesn't have an ambassador yet. you can become "+ SnackjsonObject.getString("producto=") +"'s ambassador!");
    					ambassadorPhoto.setVisibility(View.GONE);
    				}
    				else{
    					ambassadorName.setText("Good Luck Kerry");
    				}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "We can't show information about this product at this time. Please, try again later", Toast.LENGTH_SHORT).show();
	    			finish();
				}
    		}else{
    			Toast.makeText(getApplicationContext(), "We can't show information about this product at this time. Please, try again later", Toast.LENGTH_SHORT).show();
    			finish();
    		}
    	}
    }
}
