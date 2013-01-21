package com.trustripes.principal;

import com.trustripes.interfaces.ItemType;

public class HeaderItem implements ItemType{

	String title;
	
	public HeaderItem(String title){
		this.title = title;
	}
	
	@Override
	public boolean isHeader() {
		return true;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
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
