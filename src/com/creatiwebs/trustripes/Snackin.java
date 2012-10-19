package com.creatiwebs.trustripes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Snackin extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_snackin, menu);
        return true;
    }
}
