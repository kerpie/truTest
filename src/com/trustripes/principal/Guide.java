package com.trustripes.principal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Guide extends Activity {
	
	ViewFlipper vf;
	Button buttonNext;
	Button buttonPrevious;
	
	GestureDetector gestureDetector;
	
	float xi;
	float yi;
	float xf;
	float yf;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	/* Hide title in app */
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        
        vf = (ViewFlipper) findViewById(R.id.guide_views);
        
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonPrevious = (Button) findViewById(R.id.button_previous);
        buttonPrevious.setVisibility(View.GONE);
        buttonNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(vf.getCurrentView() == vf.getChildAt(3)){
					Intent intent = new Intent(Guide.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
				else{
					vf.showNext();
					if(vf.getCurrentView() != vf.getChildAt(0))
						buttonPrevious.setVisibility(View.VISIBLE);
					else
						buttonPrevious.setVisibility(View.GONE);
				}
			}
		});
        
        buttonPrevious.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				vf.showPrevious();
				if(vf.getCurrentView() != vf.getChildAt(0))
					buttonPrevious.setVisibility(View.VISIBLE);
				else
					buttonPrevious.setVisibility(View.GONE);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_guide_part_one, menu);
        return true;
    }
}
