package com.xter.player;

import android.app.Activity;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.xter.player.fragment.VideoFragment;
import com.xter.player.model.Video;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends Activity {

	private FragmentManager fm;
	private VideoFragment videoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		initLayout();
	}

	protected void initLayout() {
		fm = getFragmentManager();
		setDefaultFragment();
	}

	protected void setDefaultFragment() {
		if (videoFragment == null) {
			videoFragment = new VideoFragment();
			new LoadMediaDataTask().execute();
		}
	}


	protected List<Video> getData() {
		//定义将要查询的列
		String[] columns = new String[]{"Distinct " + MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.RESOLUTION, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DATE_ADDED,MediaStore.Video.Media.MINI_THUMB_MAGIC};
		//获取数据游标
		Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,columns, MediaStore.Video.Media.DISPLAY_NAME + " IS NOT NULL", null, null);
		//得到索引
		int indexVideoId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
		int indexVideoName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
		int indexVideoPath = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		int indexVideoSize = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
		int indexVideoDuration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
		int indexVideoResolution = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
		int indexVideoType = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
		int indexVideoDate = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
		int indexVideoThumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MINI_THUMB_MAGIC);
		//填充值
		List<Video> videos = new ArrayList<Video>();
		while (cursor.moveToNext()) {
			Video video = new Video();
			video.setId(cursor.getLong(indexVideoId));
			video.setName(cursor.getString(indexVideoName));
			video.setPath(cursor.getString(indexVideoPath));
			video.setSize(cursor.getInt(indexVideoSize));
			video.setDuration(cursor.getInt(indexVideoDuration));
			video.setResolution(cursor.getString(indexVideoResolution));
			video.setType(cursor.getString(indexVideoType));
			video.setDateAdded(cursor.getLong(indexVideoDate));
			video.setThumb(cursor.getString(indexVideoThumb));
			videos.add(video);
		}
		cursor.close();
		return videos;
	}

	/**
	 * 加载查询任务
	 */
	private class LoadMediaDataTask extends AsyncTask<Void, Void, List<Video>> {

		@Override
		protected List<Video> doInBackground(Void... params) {
			return getData();
		}

		@Override
		protected void onPostExecute(List<Video> videos) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("videos", (ArrayList<? extends Parcelable>) videos);
			videoFragment.setArguments(bundle);
			android.app.FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.media_content, videoFragment, "videos");
			ft.commit();
		}
	}
}
