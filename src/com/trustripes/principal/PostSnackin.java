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
import org.json.JSONObject;

import com.trustripes.Constants.ConstantValues;

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
import android.widget.TextView;

public class PostSnackin extends Activity {

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
        setContentView(R.layout.activity_post_snackin);
        productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
        
        ambassadorPhoto = (ImageView) findViewById(R.id.postSnackin_ambassador_image);
        ambassadorName = (TextView) findViewById(R.id.postSnackin_ambassador_name);
        
        discovererPhoto = (ImageView) findViewById(R.id.postSnackin_discoverer_image);
        discovererName = (TextView) findViewById(R.id.postSnackin_discoverer_name);
       
        intent = getIntent();
        imagePath = intent.getStringExtra("PRODUCT_PATH");
        productId = intent.getStringExtra("PRODUCT_ID");        
        stringProductName = intent.getStringExtra("PRODUCT_NAME");
        ratingValue = intent.getStringExtra("PRODUCT_RANKING");
        decodeFile(imagePath, productPhoto);
        productName.setText(stringProductName);
        
        productRatingBar.setEnabled(false);
        productRatingBar.setRating(Float.parseFloat(ratingValue));
        
        productCategoryName.setVisibility(View.GONE);
        
        new ProductDetail().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_snackin, menu);
        return true;
    }
    
    public void decodeFile(String filePath, ImageView image) {
 		// Decode image size
 		BitmapFactory.Options o = new BitmapFactory.Options();
 		o.inJustDecodeBounds = true;
 		BitmapFactory.decodeFile(filePath, o);

 		int scale = 1;

 		// Decode with inSampleSize
 		BitmapFactory.Options o2 = new BitmapFactory.Options();
 		o2.inSampleSize = scale;
 		bitmap = BitmapFactory.decodeFile(filePath, o2);
 		image.setImageBitmap(bitmap);
 	}
    
    public class ProductDetail extends AsyncTask<Void, Integer, Void>{
    	@Override
    	protected Void doInBackground(Void... params) {
    		/* Prepare variables for remote data check */
//    		HttpClient client =  new DefaultHttpClient();   		
//    		String postURL = ConstantValues.URL+"/ws/ws-productcomment.php";
//    		HttpPost post = new HttpPost(postURL); 
//    		
//    		List<NameValuePair> param = new ArrayList<NameValuePair>();
//            param.add(new BasicNameValuePair("idproduct",""));
//            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
//            post.setEntity(ent);
//            HttpResponse responsePOST = client.execute(post);
//    		StatusLine status = responsePOST.getStatusLine();
//    		
//    		/* Filter what kind of response was obtained */
//    		/* Filtering http response 200 */
//    		if(status.getStatusCode() == HttpStatus.SC_OK){
//    			HttpEntity entity = responsePOST.getEntity();
//    			InputStream inputStream = entity.getContent();	    			
//    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//    			String line = null;
//    			stringBuilder = new StringBuilder();
//    			while((line = reader.readLine()) != null){
//    				stringBuilder.append(line);
//    			}
//    			
//    			/* Converting obtained string into JSON object */
//    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//    			
//    			/* Obtaining the data we need*/
//    			statusResponse = jsonObject.getString("status");
    		return null;
    	}
    }
}
