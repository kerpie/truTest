package lazylist;

import org.json.JSONArray;
import org.json.JSONObject;

import com.trustripes.Constants.ConstantValues;
import com.trustripes.principal.R;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    public JSONArray jsonArray = null;
    
    public LazyAdapter(LayoutInflater a, JSONArray array) {
        jsonArray = array;
        inflater = a;
        imageLoader=new ImageLoader(inflater.getContext());
    }

    public int getCount() {
        return jsonArray.length();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.wall_item, null);
        
        TextView username = (TextView)vi.findViewById(R.id.wall_item_username_text);
        //TextView product = (TextView) vi.findViewById(R.id.wall_item_product_text);
        ImageView image=(ImageView)vi.findViewById(R.id.wall_item_product_photo);
        TextView snack_count = (TextView) vi.findViewById(R.id.totalSnacks);
        ImageView profilePhoto = (ImageView) vi.findViewById(R.id.wall_item_profile_photo);
        
        try{
	        JSONObject jsonObject = (JSONObject) jsonArray.get(position);
	        String url, new_user_id;
	        
	        new_user_id = jsonObject.getString("user_id");
	        if(!(new_user_id.length()>1)){
	        	new_user_id = "0"+new_user_id;
	        }     	
	        if(!jsonObject.getString("photoUser").equalsIgnoreCase("null")){
	        	url = ConstantValues.URL + "/public/user/"+ new_user_id +"/" + jsonObject.getString("photoUser");
	        	imageLoader.DisplayImage(url, profilePhoto, true);
	        }
	        else{
	          Bitmap source = BitmapFactory.decodeResource(inflater.getContext().getResources(), R.drawable.default_avatar);
	          Bitmap result = Bitmap.createBitmap(source.getWidth(),source.getHeight(), Config.ARGB_8888);
	          Canvas canvas = new Canvas(result);
	          Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	          RectF rect = new RectF(0,0,source.getWidth(), source.getHeight()); 
	          float radius = 500.0f;
	          paint.setColor(Color.BLUE);
	          canvas.drawRoundRect(rect, radius, radius, paint);
	          paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	          canvas.drawBitmap(source, 0, 0, paint);
	          paint.setXfermode(null);
	          profilePhoto.setImageBitmap(result);  
	        } 
	        
	        snack_count.setText(jsonObject.getString("totalSnackin"));
	        
	        String new_name = "";
	        if(jsonObject.getString("username").length() > 15) 
	        	new_name = jsonObject.getString("username").substring(0,15);
	        else
	        	new_name = jsonObject.getString("username");
	        username.setText(new_name + "  snacked in  " + jsonObject.getString("productname"));
	        
	        //product.setText(jsonObject.getString("productname"));
	        
	        String idProduct = jsonObject.getString("idproduct");
			String photo = jsonObject.getString("photo");
			String tmp = ConstantValues.URL+"/ws/productphoto/"+idProduct+"/thumbnails/"+photo;
	        imageLoader.DisplayImage(tmp, image, false);
        }catch(Exception e){
        }
        return vi;
    }
}