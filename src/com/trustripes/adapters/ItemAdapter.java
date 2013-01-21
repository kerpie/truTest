package com.trustripes.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.trustripes.Constants.ConstantValues;
import com.trustripes.Constants.LifeGuard;
import com.trustripes.interfaces.ItemType;
import com.trustripes.principal.HeaderItem;
import com.trustripes.principal.MessageItem;
import com.trustripes.principal.R;
import com.trustripes.principal.RegularItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<ItemType> {

	private Context context;
	private LayoutInflater li;
	private ArrayList<ItemType> items;
	
	public ItemAdapter(Context context, ArrayList<ItemType> itemList){
		super(context, 0, itemList);
		items = itemList;
		this.context = context;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		ItemType item = items.get(position);
		if(item != null){
			if(item.isHeader()){
				HeaderItem hi = (HeaderItem) item;
				v = li.inflate(R.layout.header_item, null);
				
				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);
				
				TextView tv = (TextView) v.findViewById(R.id.hi_section_name);
				tv.setText(hi.getTitle());
			}
			else{
				if(item.isMessage()){
					MessageItem mi = (MessageItem) item;
					v = li.inflate(R.layout.message_item, null);
					TextView messageTV = (TextView) v.findViewById(R.id.mi_message);					
					messageTV.setText(mi.getMessage());
				}
				else{
					RegularItem ri = (RegularItem) item;
					v = li.inflate(R.layout.regular_item, null);
					
					ImageView iv = (ImageView) v.findViewById(R.id.ri_user_image);
					TextView fullName = (TextView) v.findViewById(R.id.ri_user_full_name);
					TextView userName = (TextView) v.findViewById(R.id.ri_user_nick_name);
					
					fullName.setText(ri.getFullName());
					userName.setText(ri.getUserName());
					
					LifeGuard lg = new LifeGuard();
					lg.setImage(iv);
					lg.setPath(ri.getFullPath());
					
					new LoadImage().execute(lg);
				}
			}
		}
		
		return v;
	}
	
	public class LoadImage extends AsyncTask<LifeGuard, Integer, Void>{
		
		ImageView iv;
		String path;
		Bitmap bitmap;
		
		@Override
		protected Void doInBackground(LifeGuard... params) {
			
			iv = params[0].getImage();
			path = params[0].getPath();
			
			URL myFileUrl =null; 
			try {
				if(!path.isEmpty()){
					myFileUrl= new URL(path);
					HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(is);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(!path.isEmpty())
				iv.setImageBitmap(bitmap);
			//iv.setImageBitmap(ConstantValues.makeItCircular(bitmap));
			else
				iv.setVisibility(View.GONE);
		}
		
	}
	
}
