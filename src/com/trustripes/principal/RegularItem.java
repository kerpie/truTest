package com.trustripes.principal;

import android.widget.ImageView;

import com.trustripes.interfaces.ItemType;

public class RegularItem implements ItemType{

	String fullPath; 
	String fullName;
	String userName;
	
	public RegularItem(String fullPath, String fullName, String userName) {
		this.fullPath = fullPath;
		this.fullName = fullName;
		this.userName = userName;
	}
	
	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public boolean isMessage() {
		return false;
	}

	@Override
	public boolean isPoint() {
		return false;
	}
	
}
