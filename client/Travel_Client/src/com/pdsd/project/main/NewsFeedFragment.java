package com.pdsd.project.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class NewsFeedFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout listLayout = (LinearLayout) inflater.inflate(R.layout.news_feed_list, container, false);
		
		return listLayout;
	}
	
	
}
