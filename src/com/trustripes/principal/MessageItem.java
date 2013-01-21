package com.trustripes.principal;

import com.trustripes.interfaces.ItemType;

public class MessageItem implements ItemType{

	String message;
	
	public MessageItem(String message){
		this.message = message;
	}
		
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	@Override
	public boolean isMessage() {
		return true;
	}

	@Override
	public boolean isPoint() {
		return false;
	}

}
