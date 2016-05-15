package com.xter.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xter.player.R;
import com.xter.player.model.Video;

import java.util.List;

/**
 * Created by XTER on 2016/5/15.
 */
public class LocalVideosAdapter extends BaseAdapter {

	private List<Video> videos;
	private LayoutInflater layoutInflater;

	public LocalVideosAdapter(Context context, List<Video> videos) {
		layoutInflater = LayoutInflater.from(context);
		this.videos = videos;
	}

	@Override
	public int getCount() {
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		return videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return videos.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_video, null);
			TextView tvVideoName = (TextView) convertView.findViewById(R.id.tv_video_name);
			TextView tvVideoPath = (TextView) convertView.findViewById(R.id.tv_video_path);

			tvVideoName.setText(videos.get(position).getName());
			tvVideoPath.setText(videos.get(position).getPath());
		}

		return convertView;
	}
}
