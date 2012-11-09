package com.trustripes.Events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trustripes.principal.R;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class checkUsername implements TextWatcher{

	Resources r;
	EditText username;
	TextView message;
	final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";
	Pattern usernamePattern;
	Matcher usernameMatch;
	public static boolean returnValue;
	
	public checkUsername(EditText root, TextView errorMessage){
		username = root;
		usernamePattern = Pattern.compile(USERNAME_PATTERN);
		message = errorMessage;
	}
	
	public void afterTextChanged(Editable arg0) {
		
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String new_value = arg0.toString();
		usernameMatch = usernamePattern.matcher(new_value);
		if(new_value.trim().length() >= 5){
			if(usernameMatch.matches()){
				username.setBackgroundResource(R.drawable.text_background_green);
				message.setVisibility(View.GONE);
				returnValue = true;
			}
			else{
				username.setBackgroundResource(R.drawable.text_background_red);
				message.setVisibility(View.VISIBLE);
				message.setText(R.string.new_user_username_check_symbol);
				returnValue = true;
			}
		}
		else{
			username.setBackgroundResource(R.drawable.text_background_red);
			message.setVisibility(View.VISIBLE);
			message.setText(R.string.new_user_username_check);
			returnValue = true;
		}
	}
}
