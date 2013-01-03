package com.trustripes.principal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailSnackin extends Activity {
	Button backButton;
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
	ArrayList<String> listr = new ArrayList<String>();
	String jsonArray;
	String idproductos;
	Vector<String> vecPro = new Vector<String>();
	String posi = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_snackin);
		productPhoto = (ImageView) findViewById(R.id.postSnack_product_image);
		productName = (TextView) findViewById(R.id.postSnack_product_name);
		productCategoryName = (TextView) findViewById(R.id.postSnack_product_category_name);
		productRatingBar = (RatingBar) findViewById(R.id.postSnack_ratingbar);
		ambassadorPhoto = (ImageView) findViewById(R.id.postSnackin_ambassador_image);
		ambassadorName = (TextView) findViewById(R.id.postSnackin_ambassador_name);
		discovererPhoto = (ImageView) findViewById(R.id.postSnackin_discoverer_image);
		discovererName = (TextView) findViewById(R.id.postSnackin_discoverer_name);
		backButton = (Button) findViewById(R.id.backButton);
		new LoadDetailSnackin().execute();
		intent = getIntent();
		jsonArray = intent.getStringExtra("jsonArray");
		posi = intent.getStringExtra("va");
		int y = 2;
		y += 1;
		try {
			JSONArray array = new JSONArray(jsonArray);
			for (int i = 0; i < array.length(); i++) {
				JSONObject ip = array.getJSONObject(i);
				idproductos = ip.getString("idproduct");
				vecPro.add(idproductos);				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
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

	public class LoadDetailSnackin extends AsyncTask<Boolean, Integer, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {

			return null;
		}

	}

}
