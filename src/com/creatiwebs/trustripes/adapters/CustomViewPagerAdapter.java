package com.creatiwebs.trustripes.adapters;

import com.creatiwebs.trustripes.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewPagerAdapter extends PagerAdapter{

	@Override
	public int getCount() {
		//Number of views to switch
		return 2;
	}
	
	public final String[] Titles = {
            "Wall Activity",
            "Profile"};

	 @Override
     public CharSequence getPageTitle(int position) {
         return Titles[position];
     }
	 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        switch (position) {
        case 0:
            resId = R.layout.wall_activity;
            break;
        case 1:
            resId = R.layout.profile;
            break;
        }
        View view = inflater.inflate(resId, null);
        ((ViewPager) container).addView(view, 0);
        return view;
	}
	
	@Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	 @Override
     public Parcelable saveState() {
         return null;
     }
}
