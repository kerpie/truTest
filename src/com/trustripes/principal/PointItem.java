package com.trustripes.principal;

import com.trustripes.interfaces.ItemType;

public class PointItem implements ItemType{

	String pointDescription;
	String realPoint;
	
	public PointItem(String pointDescription, String realPoint){
		this.pointDescription = pointDescription;
		this.realPoint = realPoint;
	}
	
	public String getPointDescription() {
		return pointDescription;
	}

	public void setPointDescription(String pointDescription) {
		this.pointDescription = pointDescription;
	}

	public String getRealPoint() {
		return realPoint;
	}

	public void setRealPoint(String realPoint) {
		this.realPoint = realPoint;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	@Override
	public boolean isMessage() {
		return false;
	}

	@Override
	public boolean isPoint() {
		return true;
	}

}
