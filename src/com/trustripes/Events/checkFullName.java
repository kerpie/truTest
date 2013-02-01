package com.trustripes.Events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trustripes.principal.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class checkFullName implements TextWatcher{
	
	EditText fullName;
	TextView message;
	final String FULL_NAME_PATTERN = "[a-zA-z[αινσϊ]*]+";
	Pattern fullNamePattern;
	Matcher fullNameMatch;
	Context context;
		
	public static boolean returnValue;
	
	public checkFullName(EditText root, TextView errorMessage, Context context){
		fullName = root;
		message = errorMessage;
		fullNamePattern = Pattern.compile(FULL_NAME_PATTERN);
		this.context = context;
	}

	public void afterTextChanged(Editable s) {
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String value = s.toString();
		String new_value[] = value.split(" ");
		
		for(String tmp: new_value){
			fullNameMatch = fullNamePattern.matcher(tmp);
			if(fullNameMatch.matches()){
				fullName.setBackgroundResource(R.drawable.text_background_green);
				message.setVisibility(View.GONE);
				returnValue = true;
			}
			else{
				fullName.setBackgroundResource(R.drawable.text_background_red);
				message.setVisibility(View.VISIBLE);
				Toast.makeText(context, R.string.no_number_in_name,
						Toast.LENGTH_SHORT).show();
				returnValue = false;
			}
		}
	}
}
