package com.trustripes.Constants;

public class ConstantValues {
	/* Everything here are variables available for every class in the app */
	
	/* Root URL for connection */
	public static final String URL = "http://www.trustripes.com/dev";
	//public static final String URL = "http://qas.trustripes.com";
	
	/* For Session Status */
	public static final String USER_DATA = "UserDataPreferences";
	
	public static final int[] developmentIds = {1,2,28,42};
	
	public static boolean isInDevelopmentTeam(int key){
		for(int i=0;i<4;i++){
			if(key == developmentIds[i]){
				return true;
			}
		}
		return false;
	}
}