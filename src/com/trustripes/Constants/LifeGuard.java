package com.trustripes.Constants;

import android.widget.ImageView;

public class LifeGuard {

	ImageView image;
	String path;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setImage(ImageView image) {
		this.image = image;
	}
	
	public ImageView getImage(){
		return image;
	}
}
