package com.creatiwebs.trustripes.adapters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.creatiwebs.Constants.ConstantValues;
import com.creatiwebs.trustripes.R;
import com.creatiwebs.trustripes.R.id;

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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomViewPagerAdapter extends PagerAdapter{

	View view;
	TextView textMessage;
	ImageView profile_image;
	ListView wall_listView;
	JSONArray wall_array;
	
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
        switch (position) {
        case 0:
            resId = R.layout.wall_activity;
            view = inflater.inflate(resId, null);
            wall_listView= (ListView) view.findViewById(R.id.wall_list);
            new LoadWallActivity().execute();
            break;
        case 1:
            resId = R.layout.profile;
            view = inflater.inflate(resId, null);
            textMessage = (TextView) view.findViewById(R.id.profile_textView);
            textMessage.setText("Hola Midory!");
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
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
		 
		StringBuilder sb = null;
		String finalMessage = "";
		Bitmap image = null;
		SharedPreferences session = getSharedPreferences(ConstantValues.USER_DATA,0);  
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try{
				HttpClient client = new DefaultHttpClient();
				String url = "http://www.trustripes.com/dev/ws/ws-perfil.php";
//				HttpGet httpGet = new HttpGet(url);
				HttpPost post = new HttpPost(url); 
	            List<NameValuePair> param = new ArrayList<NameValuePair>();
	            param.add(new BasicNameValuePair("iduser",));
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
	    		}
				
				
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream input = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(input)); 
				String line = null;
				sb = new StringBuilder();
    			while((line = br.readLine()) != null){
    				sb.append(line);
    			}
    			finalMessage = sb.toString();
    			JSONArray arrayResponse = new JSONArray(sb.toString());
    			for(int i = 0; i<arrayResponse.length(); i++){
    				JSONObject objeto = arrayResponse.getJSONObject(i);
    				finalMessage = finalMessage + objeto.getString("name") + "\n";
//    			}
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
		}
		 
	 }
	 
	 public class LoadWallActivity extends AsyncTask<Void, Integer, Void>{

		 StringBuilder sb = null;
		 String finalMessage = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try{
			HttpClient client = new DefaultHttpClient();
			String url = "http://www.trustripes.com/dev/ws/ws-listproduct.php";
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream input = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(input)); 
			String line = null;
			sb = new StringBuilder();
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			finalMessage = sb.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {

		}
		
		@Override
		protected void onPostExecute(Void result) {
			try{
				wall_array = new JSONArray(finalMessage);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 }
}
