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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trustripes.Constants.ConstantValues;
import com.trustripes.Constants.LifeGuard;
import com.trustripes.adapters.ItemAdapter;
import com.trustripes.adapters.CustomViewPagerAdapter.SnackImageLoad;
import com.trustripes.interfaces.ItemType;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDescription extends Activity {

	ImageView productPhoto;
	TextView productName;
	TextView productCategoryName;
	RatingBar productRatingBar;
	ListView listData;
	
	String imagePath;
	String productId;
	String stringProductName;
	String ratingValue;
	Intent intent;
	Button backButton;
	Bitmap bitmap;
	
	ArrayList<ItemType> list = new ArrayList<ItemType>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_description);
        productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
        productName = (TextView) findViewById(R.id.postSnack_product_name);
        productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
        productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);     
        backButton = (Button)findViewById(R.id.backButton);
        
        listData = (ListView) findViewById(R.id.pd_data_listview);
        
        productRatingBar.setEnabled(false);
        
    	backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
       
        intent = getIntent();
        productId = intent.getStringExtra("PRODUCT_ID");                
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
    	boolean gotInformation = false;
    	
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
					statusResponse = SnackjsonObject.getString("status");
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
    			try{
	    			LifeGuard lg = new LifeGuard();
	    			lg.setPath(SnackjsonObject.getString("fotoproducto"));
	    			lg.setImage(productPhoto);
	    			
	    			new GetImage().execute(lg);
	    			
	    			productName.setText(SnackjsonObject.getString("producto"));
	    			productRatingBar.setRating(Float.parseFloat(SnackjsonObject.getString("promedio")));
	    			
	    			list.add(new HeaderItem("Ambassador"));
	    			if(Integer.parseInt(SnackjsonObject.getString("statusEmbajador")) == 1){
		    			list.add(new RegularItem(ConstantValues.URL +  ConstantValues.PhotoUrl(SnackjsonObject.getString("fotoembajador")), SnackjsonObject.getString("embajadorDisplay"), SnackjsonObject.getString("embajador") ));
	    			}
	    			else{
	    				list.add(new MessageItem("This product doesn't have an ambassador yet. you can become "+ SnackjsonObject.getString("producto") +"'s ambassador!"));
	    			}
	    			
	    			list.add(new HeaderItem("Discoverer"));
	    			list.add(new RegularItem(ConstantValues.URL + ConstantValues.PhotoUrl(SnackjsonObject.getString("fotodescubridor")), SnackjsonObject.getString("descubridorDisplay"), SnackjsonObject.getString("descubridor")));
	    			
	    			list.add(new HeaderItem("Comments"));
	    			
	    			if(Integer.parseInt(SnackjsonObject.getString("statusComentarios")) == 1){
	    				JSONArray array = new JSONArray(SnackjsonObject.getString("datosComentarios"));
	    				for(int it = 0; it < array.length(); it++){
	    					JSONObject obj = array.getJSONObject(it);
	    					list.add(new RegularItem(ConstantValues.URL+ConstantValues.PhotoUrl("/public/user/"+obj.getString("user_id")+"/newProfileImage.jpg"), obj.getString("displayname") + " ("+ obj.getString("username")+")", obj.getString("COMMENT")));
	    				}
	    			}
	    			else{
	    				list.add(new MessageItem("No existen comentarios para este producto"));
	    			}
	    			
	    			ItemAdapter adapter = new ItemAdapter(getApplicationContext(), list);
	    			listData.setAdapter(adapter);
	    			
    			}catch(JSONException e){}
    		}else{
    			Toast.makeText(getApplicationContext(), "We can't show information about this product at this time. Please, try again later", Toast.LENGTH_SHORT).show();
    			finish();
    		}
    	}
    }
    
    public class GetImage extends AsyncTask<LifeGuard, Void, Void>{
    	
    	LifeGuard lg;
    	Bitmap bitmap;
    	
    	@Override
    	protected Void doInBackground(LifeGuard... params) {
    		
    		lg = params[0];
    		
    		URL myFileUrl =null; 
			try {  
				myFileUrl= new URL(ConstantValues.URL+"/ws/productphoto/"+ productId +"/thumbnails/"+lg.getPath());
				HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		
    		lg.getImage().setImageBitmap(bitmap);
    	}
    }    
}
