package com.creatiwebs.Constants;

public class ConstantValues {
	public static final String URL = "http://www.trustripes.com/dev";
	
	/* For Session Status */
	public static final String USER_DATA = "UserDataPreferences";
	
	public static String getLoginUrl(String usuario, String password){
		String login_url = URL + "/ws/ws-validatelogin.php?user="+usuario+"&password="+password;
		return login_url; 
	}
}