package com.xter.player.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xter.player.R;
import com.xter.player.VideoActivity;
import com.xter.player.adapter.LocalVideosAdapter;
import com.xter.player.model.Video;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

	private ListView lvLocalVideos;

	private List<Video> videos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		videos = bundle.getParcelableArrayList("videos");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, container, false);
		initLayout(view);
		initData();
		return view;
	}

	protected void initLayout(View view) {
		lvLocalVideos = (ListView) view.findViewById(R.id.lv_local_videos);
	}

	protected void initData() {
		lvLocalVideos.setAdapter(new LocalVideosAdapter(getActivity(), videos));
		lvLocalVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), VideoActivity.class);
				intent.putExtra("path", videos.get(position).getPath());
				startActivity(intent);
			}
		});
	}


}
