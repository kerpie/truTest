package com.creatiwebs.Events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creatiwebs.trustripes.R;

import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

public class checkUsername implements View.OnKeyListener{

	Resources r;
	EditText username;
	
	public checkUsername(Resources resource){
		r = resource;
	}
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		username = (EditText) v;
		ViewParent parent = v.getParent();
		TextView message = (TextView)((View)parent).findViewById(R.id.error_message);
		Pattern usernamePattern;
		Matcher usernameMatch;
		
		final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";
		usernamePattern = Pattern.compile(USERNAME_PATTERN);
		
		if(KeyEvent.ACTION_UP == event.getAction()){
			usernameMatch = usernamePattern.matcher(username.getText().toString().trim());
			if(username.getText().toString().trim().length()>=5){
				if(usernameMatch.matches()){
					username.setBackgroundResource(R.drawable.text_background_green);
					message.setVisibility(View.GONE);
				}
				else{
					username.setBackgroundResource(R.drawable.text_background_red);
					message.setVisibility(View.VISIBLE);
					message.setText(r.getString(R.string.new_user_username_check_symbol) );
				}
			}
			else{
				username.setBackgroundResource(R.drawable.text_background_red);
				message.setVisibility(View.VISIBLE);
				message.setText(R.string.new_user_username_check);
			}
		}
		return false;
	}

}
