package com.trustripes.Constants;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConstantValues {
	public static final Boolean Scan = true;
	public static final String codeUnRegister = "7750041586911";
	
	/* Everything here are variables available for every class in the app */

	/* Root URL for connection */
	public static final String URL = "http://dev.trustripes.com";

	/* Quality Assurance */
	// public static final String URL = "http://qas.trustripes.com";

	/* Production */
	//public static final String URL = "http://www.trustripes.com";

	/* For Session Status */
	public static final String USER_DATA = "UserDataPreferences";

	public static final int[] developmentIds = { 1, 2, 3, 5, 6, 7, 8, 28, 42,
			46, 73, 75, 76, 78, 79, 80, 82, 83, 84, 85, 86 ,87, 88, 89, 90, 91, 97, 99, 100 };

	public static boolean isInDevelopmentTeam(int key) {
		for (int i = 0; i < 4; i++) {
			if (key == developmentIds[i]) {
				return true;
			}
		}
		return false;
	}

	public static final String codeName(int i) {
		int previous;
		int next;

		previous = i - 11;
		next = i + 11;

		if (i <= 10)
			previous = previous * -1;

		return Integer.toHexString(previous) + Integer.toHexString(i)
				+ Integer.toHexString(next);
	}

	public static final boolean getConnectionStatus(Context context) {
		/* Check Connection Status */
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifi.isAvailable() || mobile.isAvailable()) {
			/* There is connection to transfer data */
			return true;
		} else {
			/* There isn't connection */
			return false;
		}
	}

	public static Bitmap makeItCircular(Bitmap bitmap) {
		Bitmap source = bitmap;
		Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		float radius = 500.0f;
		paint.setColor(Color.BLUE);
		canvas.drawRoundRect(rect, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		paint.setXfermode(null);
		return result;
	}

	public static String PhotoUrl(String tempUrl) {
		String photo = null;
		String URL = null;
		String[] arrayPhotoURL = tempUrl.split("/");
		photo = String.valueOf(arrayPhotoURL[3]);
		if (photo.length() < 2) {
			photo = "0" + photo;
		}
		arrayPhotoURL[3] = photo;
		URL = "";
		for (int i = 1; i < arrayPhotoURL.length; i++) {
			URL += "/" + arrayPhotoURL[i];
		}
		return URL;
	}
	


}