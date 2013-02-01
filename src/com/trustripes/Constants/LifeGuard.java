package com.trustripes.Constants;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class LifeGuard {

	ImageView image;
	String path;
	ProgressBar progress;
	
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

	public ProgressBar getProgress() {
		return progress;
	}

	public void setProgress(ProgressBar progress) {
		this.progress = progress;
	}
}
