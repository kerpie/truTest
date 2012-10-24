package com.creatiwebs.trustripes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class Snackin extends Activity {

	TextView snackText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackin);
        
        snackText = (TextView) findViewById(R.id.snackin_text);
        
        snackText.setText(	"Barcode: "+ getIntent().getStringExtra("BARCODE")+"\n" +
        					"Product Name: "+ getIntent().getStringExtra("PRODUCT_NAME")+"\n" + 
        					"Product Photo: "+ getIntent().getStringExtra("PRODUCT_PHOTO")+"\n" +
        					"Estado del Embajador: "+ getIntent().getStringExtra("AMBASSADOR_STATUS")+"\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_snackin, menu);
        return true;
    }
}
