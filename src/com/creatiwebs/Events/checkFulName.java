package com.creatiwebs.Events;

import com.creatiwebs.trustripes.R;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class checkFulName implements View.OnKeyListener {
	
	EditText fullName;
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		fullName = (EditText) v;
		
		if(KeyEvent.ACTION_UP == event.getAction()){
			v.setBackgroundResource(R.drawable.text_background_green);
		}
		return false;
	}
}
