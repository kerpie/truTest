package com.trustripes.principal;

import com.google.zxing.client.android.CaptureActivity;
import com.trustripes.Constants.ConstantValues;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Tip_camera extends Activity {
	
	Button go;
	CheckBox check;
	 
	 private SharedPreferences newSettings;
	 SharedPreferences.Editor settingsEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tip_camera);
        newSettings = getSharedPreferences(ConstantValues.USER_DATA, MODE_PRIVATE)  ;
      settingsEditor = newSettings.edit();
    go = (Button)findViewById(R.id.i_go_it);
    go.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			Log.d("MAIN", "Click EN EL BOTON");
			Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
			startActivity(intent);
			finish();
		}
	});
    
    check = (CheckBox)findViewById(R.id.checkBox); 
    check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			 if (isChecked) {
			
				 settingsEditor.putBoolean("show_snack_help", false);
	            }
	            else {
	            	 settingsEditor.putBoolean("show_snack_help", true);
	               
	        }
			 settingsEditor.commit();
			
		}
	}) ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tip_camera, menu);
        return true;
    }
}
