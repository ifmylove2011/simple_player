package com.xter.player.adapter;

import java.util.List;

import com.xter.player.R;
import com.xter.player.model.Video;
import com.xter.player.util.SimpleImageLoader;

import android.content.Context;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by XTER on 2016/5/15.
 */
public class LocalVideosAdapter extends BaseAdapter {

	private List<Video> videos;
	private LayoutInflater layoutInflater;
	SimpleImageLoader loader;
	boolean isFirstLoad;

	public LocalVideosAdapter(Context context, List<Video> videos) {
		loader = SimpleImageLoader.build(context);
		layoutInflater = LayoutInflater.from(context);
		this.videos = videos;
		isFirstLoad = false;
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_video, null);

			holder.ivVideoThumb = (ImageView) convertView.findViewById(R.id.iv_video_thumb);
			holder.tvVideoName = (TextView) convertView.findViewById(R.id.tv_video_name);
			holder.tvVideoPath = (TextView) convertView.findViewById(R.id.tv_video_path);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String uri = videos.get(position).getPath();

//		if (isFirstLoad)
////			holder.ivVideoThumb.setImageBitmap(ThumbnailUtils.createVideoThumbnail(uri, Images.Thumbnails.MICRO_KIND));
//			loader.setBitmap(ThumbnailUtils.createVideoThumbnail(uri, Images.Thumbnails.MICRO_KIND),
//					holder.ivVideoThumb);
		holder.tvVideoName.setText(videos.get(position).getName());
		holder.tvVideoPath.setText(uri);
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		isFirstLoad = true;
	}

	static class ViewHolder {
		ImageView ivVideoThumb;
		TextView tvVideoPath;
		TextView tvVideoName;
	}
}
