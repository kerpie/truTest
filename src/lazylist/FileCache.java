package lazylist;

import java.io.File;
import android.content.Context;

public class FileCache {
    
    private File cacheDir;
	private File dire;
    
    public FileCache(Context context){
        //Find the dir to save cached images
    	
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TruStripes");
            dire = new File(cacheDir, "Cache");    	
        }
        else
            dire=context.getCacheDir();
        if(!dire.exists())
            dire.mkdirs();
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(dire, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=dire.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}