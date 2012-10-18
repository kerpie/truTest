package com.creatiwebs.trustripes;

import com.creatiwebs.trustripes.adapters.CustomViewPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView profile_textView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        CustomViewPagerAdapter pagerAdapter = new CustomViewPagerAdapter();
        ViewPager myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(pagerAdapter);
        myPager.setCurrentItem(0);
    }
      
    //For Options Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }

}
