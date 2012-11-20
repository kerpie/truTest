package com.trustripes.Events;

import com.trustripes.adapters.CustomViewPagerAdapter;
import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class EndlessScrollListener implements OnScrollListener{
	
	private int visibleThreshold = 2;
    private int previousTotal = 0;
    
    private boolean loading = true;
	Context context;
	CustomViewPagerAdapter newTaskContainer;
	
//	public EndlessScrollListener(Context context) {
//		this.context = context;
//		newTaskContainer = new CustomViewPagerAdapter();
//	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}
