package com.trustripes.Events;

import com.trustripes.principal.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class checkPass implements TextWatcher{

	EditText password;
	TextView message;
	
	public static boolean returnValue;
	
	public checkPass(EditText root, TextView errorMessage){
		password = root;
		message = errorMessage;
	}
	
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String value = s.toString();
		if(value.length()>=5){
			password.setBackgroundResource(R.drawable.text_background_green);
			message.setVisibility(View.GONE);
			returnValue = true;
		}
		else{
			password.setBackgroundResource(R.drawable.text_background_red);
			message.setVisibility(View.VISIBLE);
			message.setText(R.string.password_too_short);
			returnValue = false;
		}
	}
}
