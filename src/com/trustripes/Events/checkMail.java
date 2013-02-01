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

public class checkMail implements TextWatcher{
	
	Resources r;
	EditText mail;
	TextView message;
	Pattern mailPattern;
	Matcher mailMatch;
	final String MAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static boolean returnValue;
	Context context;
	
	public checkMail(EditText root, TextView errorMessage, Context context){
		mail = root;
		mailPattern = Pattern.compile(MAIL_PATTERN);
		message = errorMessage;
		this.context = context;
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String new_value = s.toString();
		mailMatch = mailPattern.matcher(new_value);
		if(mailMatch.matches()){
			mail.setBackgroundResource(R.drawable.text_background_green);
			message.setVisibility(View.GONE);
			returnValue = true;
		}
		else{
			mail.setBackgroundResource(R.drawable.text_background_red);
			message.setVisibility(View.VISIBLE);
			Toast.makeText(context, R.string.new_user_mail_check_symbol,
					Toast.LENGTH_SHORT).show();
			returnValue = false;
			
		}
	}
}
