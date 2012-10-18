package com.creatiwebs.trustripes.adapters;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.creatiwebs.Constants.ConstantValues;
import com.creatiwebs.trustripes.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomViewPagerAdapter extends PagerAdapter{

	View view;
	SharedPreferences session;
	ImageView profile_image;
	TextView profile_text, wall_text;
	ListView wall_list;
	
	@Override
	public int getCount() {
		//Number of views to switch
		return 2;
	}
	
	public final String[] Titles = {
            "Wall Activity",
            "Profile"};

	 @Override
     public CharSequence getPageTitle(int position) {
         return Titles[position];
     }
	 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        session = container.getContext().getSharedPreferences(ConstantValues.USER_DATA, 0);
        switch (position) {
        case 0:
            resId = R.layout.wall_activity;
            view = inflater.inflate(resId, null);
            wall_text = (TextView) view.findViewById(R.id.wall_text);
            new LoadWallActivity().execute();
            break;
        case 1:
            resId = R.layout.profile;
            view = inflater.inflate(resId, null);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
            profile_text = (TextView) view.findViewById(R.id.profile_textView);
            wall_list = (ListView) view.findViewById(R.id.wall_list);
            new LoadProfileData().execute();
            break;
        }
        ((ViewPager) container).addView(view, 0);
        return view;
	}
	
	@Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	 @Override
     public Parcelable saveState() {
         return null;
     }
	 
	 public class LoadProfileData extends AsyncTask<Void, Integer, Void>{
		
		String id_string; 
		StringBuilder stringBuilder;
		String statusResponse = null;
		String msj = null;
		String email = null;
		String username = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		 
		@Override
		protected Void doInBackground(Void... params) {
			try{	
				id_string = session.getString("user_id", "No data");
				HttpClient client =  new DefaultHttpClient();   		
	            String postURL = "http://www.trustripes.com/dev/ws/ws-perfil.php";
	            HttpPost post = new HttpPost(postURL); 
	            List<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("iduser",id_string));
	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
	            post.setEntity(ent);
	            HttpResponse responsePOST = client.execute(post);    		
	    		StatusLine status = responsePOST.getStatusLine();
	    		if(status.getStatusCode() == HttpStatus.SC_OK){
	    			HttpEntity entity = responsePOST.getEntity();
	    			InputStream inputStream = entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    			String line = null;
	    			stringBuilder = new StringBuilder();
	    			while((line = reader.readLine()) != null){
	    				stringBuilder.append(line);
	    			}
	    			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
	    			statusResponse = jsonObject.getString("status");
	    			if(Integer.parseInt(statusResponse) == 1){
	    				msj = jsonObject.getString("msj");
		    			email = jsonObject.getString("email");
		    			username = jsonObject.getString("username");
	    			}
	    			reader.close();
	    			inputStream.close();
	    		}
	    		
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			profile_text.setText(	"Usuario: "+username+"\n" +
									"Correo: "+ email +"\n");
		}
	 }
	 
	 public class LoadWallActivity extends AsyncTask<Void, Integer, Void>{
		 
		StringBuilder stringBuilder = null;
		String statusResponse = null;
		String id_string = null;
		JSONArray jsonArray = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		 
		 public Void doInBackground(Void... params){
			 try{	
					id_string = session.getString("user_id", "No data");
					HttpClient client =  new DefaultHttpClient();   		
		            String url = "http://www.trustripes.com/dev/ws/ws-listproduct.php";
		            HttpGet httpGet = new HttpGet(url);
		            HttpResponse responseGET = client.execute(httpGet);    		
		    		StatusLine status = responseGET.getStatusLine();
		    		if(status.getStatusCode() == HttpStatus.SC_OK){
		    			HttpEntity entity = responseGET.getEntity();
		    			InputStream inputStream = entity.getContent();
		    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		    			String line = null;
		    			stringBuilder = new StringBuilder();
		    			while((line = reader.readLine()) != null){
		    				stringBuilder.append(line);
		    			}
		    			jsonArray = new JSONArray(stringBuilder.toString());
		    			reader.close();
		    			inputStream.close();
		    		}
		    		
				}catch(Exception e){
					e.printStackTrace();
				}
			 return null;
		 }
		 
		 @Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		 	 
		 @Override
		protected void onPostExecute(Void result) {
			for(int i=0;i<jsonArray.length();i++){
				try{
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					String username = jsonObject.getString("username");
					String idProduct = jsonObject.getString("idproduct");
					String idSnackin = jsonObject.getString("idsnackin");
					String productName = jsonObject.getString("productname");
					String photo = jsonObject.getString("photo");
					
					String tmp = " ";
					tmp = tmp +	"username: " + username + "\n" +
								"ProductID: " + idProduct + "\n" +
								"SnackIn: " + idSnackin + "\n" +
								"Product Name: " + productName + "\n" +
								"Photo: " + photo + "\n";
					
					wall_text.setText(wall_text.getText() + tmp);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	 }
}