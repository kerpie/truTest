package com.trustripes.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lazylist.LazyAdapter;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.trustripes.Constants.ConstantValues;
import com.trustripes.principal.NewUserRegistration;
import com.trustripes.principal.R;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CustomViewPagerAdapter extends PagerAdapter{

	View view;
	SharedPreferences session;
	ImageView profile_image;
	TextView profile_text, feedback_text;
	ListView wall_list;
	LazyAdapter adapter;
	LayoutInflater new_inflater;
	Button logOutButton, editProfileButton;
	Context context;
	JSONArray jsonArray = null;
	ProgressBar loadingImage = null;
	
	LoadWallActivity loadWallActivity;
	
	String profileImagePath;
	
	Activity parentActivity;
	
	boolean firstTime = true;
	
	private int visibleThreshold = 1;
	private int previousTotal = 0;
    private boolean loading = true;
	    
	public CustomViewPagerAdapter(Activity a){
		parentActivity = a;
	}
		
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
		LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        context = container.getContext();
        session = container.getContext().getSharedPreferences(ConstantValues.USER_DATA, 0);
        switch (position) {
        case 0:
        	loadWallActivity = new LoadWallActivity();
        	
            resId = R.layout.wall_activity;
            view = inflater.inflate(resId, null);
            wall_list = (ListView) view.findViewById(R.id.wall_list);

            wall_list.setOnScrollListener(new EndlessScroll());
            
            ((PullToRefreshListView) wall_list).setOnRefreshListener(new OnRefreshListener() {
                public void onRefresh() {
                	if(ConstantValues.getConnectionStatus(context)){
                        // Do work to refresh the list here.
                		if(loadWallActivity.getStatus() == AsyncTask.Status.RUNNING){
                			Toast.makeText(context, "It's already refreshing...", Toast.LENGTH_SHORT).show();
                		}
                		else{
                			if(loadWallActivity.getStatus() == AsyncTask.Status.FINISHED){
                				loadWallActivity = new LoadWallActivity();
                			}
                			loadWallActivity.execute(true);
                		}
                	}
                	else{
    					Toast.makeText(context, "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
            		}
                }
            });
            
            wall_list.setOnItemClickListener(new OnItemClickListener() {
            	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            		if(ConstantValues.getConnectionStatus(context)){
                        // Do work to refresh the list here.
                        new LoadWallActivity().execute(false);
                	}
            		else{
    					Toast.makeText(context, "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
            		}
            	}
			});
            
            
            adapter = new LazyAdapter();
            new_inflater = inflater;
            
            if(ConstantValues.getConnectionStatus(context)){
            	new LoadWallActivity().execute(false);
            }
            else{
				Toast.makeText(context, "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
    		}
            break;
        case 1:
            resId = R.layout.profile;
            view = inflater.inflate(resId, null);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
           profile_text = (TextView) view.findViewById(R.id.user_name);
            logOutButton = (Button) view.findViewById(R.id.logout_button);
            editProfileButton = (Button) view.findViewById(R.id.edit_profile_button);
            loadingImage = (ProgressBar) view.findViewById(R.id.profile_image_loader);
            
            logOutButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SharedPreferences.Editor settingsEditor = session.edit();
					settingsEditor.putString("user_status", "0");
					settingsEditor.commit();
					parentActivity.finish();
				}
			});
                        
            editProfileButton.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
//					Intent i = new Intent(context, NewUserRegistration.class);
//					i.putExtra("isEdit", true);
//					context.startActivity(i);
					
		            Intent testIntent = new Intent(context, NewUserRegistration.class);
		            testIntent.putExtra("isEdit", true);
		            ((Activity)view.getContext()).startActivityForResult(testIntent,100);
				}
			});
         
            
            feedback_text = (TextView) view.findViewById(R.id.feedback);
            feedback_text.setOnClickListener( new View.OnClickListener(){
            	public void onClick(View v){
            		Uri uri = Uri.parse("http://www.surveygizmo.com/s3/1094333/Feedback-truStripes");
            		context.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
            	}
            });
            
            profile_text.setText(session.getString("user_name", "No saved value"));
            				
            String imagePath = session.getString("user_external_image_path", "");
            File imageFile = new File(imagePath);
            
            if(imageFile.exists())
            	decodeFile(imagePath);
            else
            	if(ConstantValues.getConnectionStatus(context)){
            		new LoadProfileData().execute();
            	}   
            	else{
					Toast.makeText(context, "Looks like you have no connection, please check it and try again", Toast.LENGTH_SHORT).show();
				}
            break;
        }
        ((ViewPager) container).addView(view, 0);
        return view;
	}
	
	public void decodeFile(String filePath) {
		Bitmap bitmap;
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		int scale = 1;
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		profile_image.setImageBitmap(ConstantValues.makeItCircular(bitmap));
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
		
		int id;
		
		String id_string; 
		String statusResponse = null;
		String photoURL = null;
		Bitmap bitmap;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id_string = session.getString("user_id", "-1");
			loadingImage.setVisibility(View.VISIBLE);
		}
		 
		@Override
		protected Void doInBackground(Void... params) {
			try{	
				photoURL = session.getString("user_photo_url", "");
				if(photoURL.length() >= 10){
    				URL myFileUrl =null; 
    				try {
    					myFileUrl= new URL(ConstantValues.URL+photoURL);
    					HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
    					conn.setDoInput(true);
    					conn.connect();
    					InputStream is = conn.getInputStream();
    					bitmap = BitmapFactory.decodeStream(is);
    				} catch (MalformedURLException e) {
    					e.printStackTrace();
    				}catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    			else{
    				
    			}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
			
		@Override
		protected void onPostExecute(Void result) {
			id = Integer.valueOf(id_string);
			if(bitmap != null ){
				profile_image.setImageBitmap( ConstantValues.makeItCircular(bitmap));
				try{
					profileImagePath = session.getString("user_external_image_path", "");
					File directory = new File(profileImagePath);
					FileOutputStream outStream;
					outStream = new FileOutputStream(directory);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
					/* 100 to keep full quality of the image */
					outStream.flush();
					outStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				//prueba cambio por otro avatar
				profile_image.setImageResource(R.drawable.default_avatar);
			loadingImage.setVisibility(View.GONE);
		}
	 }
	 
	public class LoadWallActivity extends AsyncTask<Boolean, Integer, Void>{
		 
		StringBuilder stringBuilder = null;
		String statusResponse = null;
		String id_string = null;
		JSONArray tmpJsonArray = null;
		boolean isRefresh = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		 
		public Void doInBackground(Boolean... params){
			 try{	
				id_string = session.getString("user_id", "No data");
				
				isRefresh = params[0];
				if(isRefresh){
					firstTime = true;
				}
				
				HttpClient client =  new DefaultHttpClient();   		
	            String url = ConstantValues.URL+"/ws/ws-listproduct.php";
	            
	            /* Prepare variables for remote data check */
	    		HttpPost post = new HttpPost(url); 
	            List<NameValuePair> param = new ArrayList<NameValuePair>();
	            if((jsonArray == null) || isRefresh){
	            	param.add(new BasicNameValuePair("total","0"));
	            	previousTotal = 0;
	            }
	            else{
	            	param.add(new BasicNameValuePair("total",String.valueOf(jsonArray.length())));
	            }
	            
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
    				if(firstTime)
	    				jsonArray = new JSONArray(stringBuilder.toString());
	    			else{
	    				tmpJsonArray = new JSONArray(stringBuilder.toString());
	    				
	    				/* agregar nuevo json al antiguo*/
	    				for(int k=0;k<tmpJsonArray.length();k++){
	    					jsonArray.put(tmpJsonArray.get(k));
	    				}
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
			 if(jsonArray != null){
				 if(firstTime){
					 firstTime = false;
					 adapter.instantiateValues(new_inflater, jsonArray);
					 wall_list.setAdapter(adapter);
					 if(isRefresh){
						 ((PullToRefreshListView) wall_list).onRefreshComplete();
					 }
				 }
				 else{
					 adapter.notifyDataSetChanged();
				 }
			 }
			 else{
				 Toast.makeText(context, "UPS!", Toast.LENGTH_SHORT).show();
			 }
			 
		}
	 }
	 
	public class EndlessScroll implements OnScrollListener{
		    
	     public void onScrollStateChanged(AbsListView view, int scrollState) {			
	     }
			
	     public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	    	 if (loading) {
		            if (totalItemCount > previousTotal) {
		                loading = false;
		                previousTotal = totalItemCount;
		            }
		        }
		        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
		        	new LoadWallActivity().execute(false);
		            loading = true;
		        }
				
			}
		}
}