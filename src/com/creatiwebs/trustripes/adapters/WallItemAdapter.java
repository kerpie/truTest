package com.creatiwebs.trustripes.adapters;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.creatiwebs.trustripes.R;

public class WallItemAdapter extends ArrayAdapter<JSONObject>{

	private ArrayList<JSONObject> items;
	private Context context;
	Bitmap productphoto;
	
	public WallItemAdapter(Context context, int textViewResourceId,ArrayList<JSONObject> objects) {
		super(context, textViewResourceId, objects);
		this.items = items;
		this.context = context; 
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v.inflate(context, R.layout.wall_item, null);
			JSONObject jObject = items.get(position);
			if (jObject!=null){
				ImageView productImage = (ImageView) v.findViewById(R.id.wall_item_product_photo) ;
				TextView userText = (TextView) v.findViewById(R.id.wall_item_simple_text);
				try{
					userText.setText(jObject.getString("username"));
					HttpClient client = new DefaultHttpClient();
					String url = "http://trustripes.com/dev/wa/productphoto/"+jObject.getString("idproduct")+jObject.getString("photo");
					InputStream in = new URL(url).openStream();
					productphoto = BitmapFactory.decodeStream(in);
					productImage.setImageBitmap(productphoto);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return v;
	}
}





















