package com.xter.player.adapter;

import java.util.List;

import com.xter.player.R;
import com.xter.player.adapter.LocalVideoRecycleAdapter.VH;
import com.xter.player.model.Video;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalVideoRecycleAdapter extends Adapter<VH> {

	public interface OnRecycleItemListener {
		void onItemClick(int position);

		boolean onItemLongClick(int position);
	}

	private OnRecycleItemListener mOnRecycleItemListener;
	private LayoutInflater layoutInflater;
	private List<Video> mVideos;

	public LocalVideoRecycleAdapter(Context context, List<Video> videos) {
		layoutInflater = LayoutInflater.from(context);
		this.mVideos = videos;
	}

	public void setOnRecycleItemListener(OnRecycleItemListener OnRecycleItemListener) {
		this.mOnRecycleItemListener = OnRecycleItemListener;
	}

	@Override
	public int getItemCount() {
		return mVideos.size();
	}

	@Override
	public void onBindViewHolder(VH holder, int pos) {
		holder.position = pos;
		Video video = mVideos.get(pos);
		holder.tvVideoName.setText(video.getName());
		holder.tvVideoPath.setText(video.getPath());
	}

	@Override
	public VH onCreateViewHolder(ViewGroup vg, int pos) {
		View view = layoutInflater.inflate(R.layout.item_video, null);
		return new VH(view);
	}

	class VH extends ViewHolder implements OnClickListener, OnLongClickListener {
		public VH(View itemView) {
			super(itemView);
			rootView = itemView.findViewById(R.id.ll_video);
			rootView.setOnLongClickListener(this);
			rootView.setOnClickListener(this);
			ivVideoThumb = (ImageView) itemView.findViewById(R.id.iv_video_thumb);
			tvVideoName = (TextView) itemView.findViewById(R.id.tv_video_name);
			tvVideoPath = (TextView) itemView.findViewById(R.id.tv_video_path);
		}

		View rootView;
		ImageView ivVideoThumb;
		TextView tvVideoPath;
		TextView tvVideoName;
		int position;

		@Override
		public boolean onLongClick(View v) {
			if (mOnRecycleItemListener != null)
				mOnRecycleItemListener.onItemLongClick(position);
			return false;
		}

		@Override
		public void onClick(View v) {
			if (mOnRecycleItemListener != null)
				mOnRecycleItemListener.onItemClick(position);
		}
	}

}
