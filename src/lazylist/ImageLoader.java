package lazylist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.trustripes.principal.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	MemoryCache memoryCache;
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Context context;
	boolean isProfile;

	public ImageLoader(Context context, int memory) {
		this.context = context;
		fileCache = new FileCache(context);
		memoryCache = new MemoryCache(memory);
		executorService = Executors.newFixedThreadPool(5);
	}

	public ImageLoader() {
	}

	public void DisplayImage(String url, ImageView imageView, boolean profile,
			boolean inSD) {
		isProfile = profile;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (isProfile){
				//bitmap = makeItCircular(bitmap);
			}
			//bitmap = Nothing(bitmap);
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, inSD);
			imageView.setImageResource(R.drawable.loading);
		}
	}

	private void queuePhoto(String url, ImageView imageView, boolean inSD) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, inSD);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(PhotoToLoad ptl) {

		if (ptl.isInsideAnotherDirectory) {
			return decodeFile(new File(ptl.url), 4);
		}
		File f = fileCache.getFile(ptl.url);

		// from SD cache
		Bitmap b = decodeFile(f, 1);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(ptl.url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f, 1);
			if (isProfile) {
				bitmap = makeItCircular(bitmap);
			}
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError) {
				memoryCache.clear();
				Log.i("ImageLoader", "Memoria liberada");
			}
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f, int scale) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public boolean isInsideAnotherDirectory;

		public PhotoToLoad(String u, ImageView i, boolean iiad) {
			url = u;
			imageView = i;
			isInsideAnotherDirectory = iiad;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad);
			if (photoToLoad.isInsideAnotherDirectory)
				memoryCache.clear();
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(R.drawable.loading);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public Bitmap makeItCircular(Bitmap bitmap) {
		Bitmap source = bitmap;
		Bitmap result = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), Config.ARGB_8888);
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
	
	public Bitmap Nothing (Bitmap bitmap){
		Bitmap bit = null;
		bit = bitmap;		
		return bit;		
	}
}
