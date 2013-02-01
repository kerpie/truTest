package com.trustripes.Events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trustripes.principal.R;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class checkUsername implements TextWatcher {

	Resources r;
	EditText username;
	TextView message;
	final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";
	Pattern usernamePattern;
	Matcher usernameMatch;
	public static boolean returnValue;
	Context context;

	public checkUsername(EditText root, TextView errorMessage, Context context) {
		username = root;
		usernamePattern = Pattern.compile(USERNAME_PATTERN);
		message = errorMessage;
		this.context = context;
	}

	public void afterTextChanged(Editable arg0) {

	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String new_value = arg0.toString();
		usernameMatch = usernamePattern.matcher(new_value);
		if (new_value.trim().length() >= 5) {
			if (usernameMatch.matches()) {
				username.setBackgroundResource(R.drawable.text_background_green);
				message.setVisibility(View.GONE);
				returnValue = true;
			} else {
				username.setBackgroundResource(R.drawable.text_background_red);
				message.setVisibility(View.VISIBLE);
				returnValue = false;
				Toast.makeText(context, R.string.new_user_username_check_symbol,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			username.setBackgroundResource(R.drawable.text_background_red);
			message.setVisibility(View.VISIBLE);			
			returnValue = true;
			Toast.makeText(context, R.string.new_user_username_check,
					Toast.LENGTH_SHORT).show();
		}
	}
}
