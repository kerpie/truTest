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

public class checkMail implements View.OnKeyListener {
	
	Resources r;
	EditText mail;
	
	public checkMail(Resources resource){
		r = resource;
	}
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		mail = (EditText) v;
		ViewParent parent = v.getParent();
		TextView message = (TextView) ((View)parent).findViewById(R.id.error_message);
		Pattern mailPattern;
		Matcher mailMatch;
		
		final String MAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		mailPattern = Pattern.compile(MAIL_PATTERN);
		
		if(KeyEvent.ACTION_UP == event.getAction()){
			mailMatch = mailPattern.matcher(mail.getText().toString().trim());			
			if(mailMatch.matches()){
				mail.setBackgroundResource(R.drawable.text_background_green);
				message.setVisibility(View.GONE);
			}
			else{
				message.setVisibility(View.VISIBLE);
				mail.setBackgroundResource(R.drawable.text_background_red);
				message.setText(r.getString(R.string.new_user_mail_check_symbol));
			}
		}
		return false;
	}
}
