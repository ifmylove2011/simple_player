package com.xter.player.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xter.player.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, container, false);
		initLayout(view);
		initData();
		return view;
	}

	protected void initLayout(View view) {

	}

	protected void initData() {

	}

}
