package com.xter.player.fragment;

import java.util.List;

import com.xter.player.R;
import com.xter.player.VideoActivity;
import com.xter.player.adapter.LocalVideoRecycleAdapter;
import com.xter.player.adapter.LocalVideoRecycleAdapter.OnRecycleItemListener;
import com.xter.player.adapter.LocalVideosAdapter;
import com.xter.player.model.Video;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

	private ListView lvLocalVideos;
	private LocalVideosAdapter localVideosAdapter;
	private SwipeRefreshLayout srlLocalVideos;

	private RecyclerView rvTest;
	private LocalVideoRecycleAdapter lvrAdapter;

	private List<Video> videos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		videos = bundle.getParcelableArrayList("videos");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, container, false);
		initLayout(view);
		initData();
		return view;
	}

	protected void initLayout(View view) {
		srlLocalVideos = (SwipeRefreshLayout) view.findViewById(R.id.srl_local_videos);

		lvLocalVideos = (ListView) view.findViewById(R.id.lv_local_videos);

		rvTest = (RecyclerView) view.findViewById(R.id.rv_test);
		// 固定大小
		rvTest.setHasFixedSize(true);
		// 设置布局
		LayoutManager lm = new LinearLayoutManager(getActivity());
		rvTest.setLayoutManager(lm);
	}

	protected void initData() {
		srlLocalVideos.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				videos.add(new Video(22, "a", "aa"));
				localVideosAdapter.notifyDataSetChanged();
				srlLocalVideos.setRefreshing(false);
			}
		});

		localVideosAdapter = new LocalVideosAdapter(getActivity(), videos);
		lvLocalVideos.setAdapter(localVideosAdapter);
		lvLocalVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), VideoActivity.class);
				intent.putExtra("path", videos.get(position).getPath());
				startActivity(intent);
			}
		});
		localVideosAdapter.notifyDataSetChanged();

		// 使用recycleview
		lvrAdapter = new LocalVideoRecycleAdapter(getActivity(), videos);
		rvTest.setAdapter(lvrAdapter);
		lvrAdapter.setOnRecycleItemListener(new OnRecycleItemListener() {

			@Override
			public boolean onItemLongClick(int position) {
				return false;
			}

			@Override
			public void onItemClick(int position) {
				Intent intent = new Intent(getActivity(), VideoActivity.class);
				intent.putExtra("path", videos.get(position).getPath());
				startActivity(intent);
			}
		});
	}

}
