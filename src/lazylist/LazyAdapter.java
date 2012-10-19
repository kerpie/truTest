package lazylist;

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
    
    public LazyAdapter(LayoutInflater a, String[] d) {
        data=d;
        inflater = a;
        imageLoader=new ImageLoader(inflater.getContext());
    }

    public int getCount() {
        return data.length;
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
        text.setText("item "+position);
        imageLoader.DisplayImage(data[position], image);
        return vi;
    }
}