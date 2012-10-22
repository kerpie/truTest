package lazylist;

import org.json.JSONArray;
import org.json.JSONObject;

import com.creatiwebs.trustripes.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
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
        
        TextView text=(TextView)vi.findViewById(R.id.wall_item_simple_text);;
        ImageView image=(ImageView)vi.findViewById(R.id.wall_item_product_photo);
        
        try{
	        JSONObject jsonObject = (JSONObject) jsonArray.get(position);
	        String new_name = "";
	        if(jsonObject.getString("username").length() > 15) 
	        	new_name = jsonObject.getString("username").substring(0,15);
	        else
	        	new_name = jsonObject.getString("username");
	        text.setText(new_name + "\n"+jsonObject.getString("productname"));
	        String idProduct = jsonObject.getString("idproduct");
			String photo = jsonObject.getString("photo");
			String tmp = "http://trustripes.com/dev/ws/productphoto/"+idProduct+photo;
	        imageLoader.DisplayImage(tmp, image);
        }catch(Exception e){
        	
        }
        return vi;
    }
}