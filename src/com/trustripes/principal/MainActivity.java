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
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;
import com.trustripes.adapters.CustomViewPagerAdapter;
import com.trustripes.adapters.CustomViewPagerAdapter.SnackImageLoad;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
	String obtainedBarcode = ConstantValues.codeUnRegister;

	/* Declaration of UI widgets */
	private Button snackIn_button;
	ViewPager myPager;
	SharedPreferences session;

	private SharedPreferences developmentSession = null;
	String id;
	int realId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		/* Rescue the session variable */
		session = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE);

		/* Instantiation and event association for the snackin button */
		snackIn_button = (Button) findViewById(R.id.main_snackInButton);
		snackIn_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "Click EN EL BOTON");
				if (ConstantValues.Scan == false) {
					new SnackinDebug().execute();
				} else {
					if (session.getBoolean("show_snack_help", true)) {
						Intent intent = new Intent(getApplicationContext(),
								Tip_camera.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(),
								CaptureActivity.class);
						startActivity(intent);
					}
				}
			}
		});

		/* Preparing the Custom Page Swipe Adapter */
		CustomViewPagerAdapter pagerAdapter = new CustomViewPagerAdapter(this);
		myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(pagerAdapter);
		myPager.setCurrentItem(0);

		developmentSession = getSharedPreferences(ConstantValues.USER_DATA,
				MODE_PRIVATE);
		id = developmentSession.getString("user_id", "-1");
		realId = Integer.parseInt(id);

	}

	@Override
	protected void onStart() {
		super.onStart();

		/* Implementation of Google Analytics for Android */
		if (ConstantValues.URL == "http://www.trustripes.com"
				&& !ConstantValues.isInDevelopmentTeam(realId)) {
			EasyTracker.getInstance().activityStart(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		/* Implementation of Google Analytics for Android */
		if (ConstantValues.URL == "http://www.trustripes.com"
				&& !ConstantValues.isInDevelopmentTeam(realId)) {
			EasyTracker.getInstance().activityStop(this);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		myPager.onSaveInstanceState();
		super.onSaveInstanceState(outState);
	}

	// For Options Menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			logOut();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void logOut() {
		/* Get the session variable */
		/* Edit the status variable then finish the activity */
		SharedPreferences.Editor settingsEditor = session.edit();
		settingsEditor.putString("user_status", "0");
		settingsEditor.commit();
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 200:
			CustomViewPagerAdapter pagerAdapter = new CustomViewPagerAdapter(
					this);
			myPager = (ViewPager) findViewById(R.id.pager);
			myPager.setAdapter(pagerAdapter);
			myPager.setCurrentItem(1);
			break;
		}
	}

	public class SnackinDebug extends AsyncTask<Void, Integer, Void> {

		StringBuilder stringBuilder;
		String statusResponse = "";
		String productId, productName, productPhoto;
		boolean canSnack;

		@Override
		protected Void doInBackground(Void... params) {

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = ConstantValues.URL
						+ "/ws/ws-barcodevalidation.php";
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("codigo", obtainedBarcode));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				StatusLine status = responsePOST.getStatusLine();
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = responsePOST.getEntity();
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line = null;
					stringBuilder = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					JSONObject jsonObject = new JSONObject(
							stringBuilder.toString());
					statusResponse = jsonObject.getString("status");
					if (Integer.parseInt(statusResponse) == 1) {
						productId = jsonObject.getString("idproduct");
						productPhoto = jsonObject.getString("foto");
						canSnack = true;
					} else {
						canSnack = false;
					}
					productName = jsonObject.getString("producto");

					reader.close();
					inputStream.close();
				} else {
					/* Check Other Status Code */
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = null;
			canSnack = false;

			intent = new Intent(getApplicationContext(), Register.class);

			intent.putExtra("BARCODE", obtainedBarcode);
			intent.putExtra("PRODUCT_NAME", productName);
			startActivity(intent);
			finish();

		}
	}

}
