package com.xter.player;

import java.util.ArrayList;
import java.util.List;

import com.xter.player.fragment.VideoFragment;
import com.xter.player.model.Video;

import android.app.Activity;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;

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
		String[] columns = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.DATE_ADDED};
		//获取数据游标
		Cursor cursor = this.getContentResolver().query(MediaStore.Files.getContentUri("external"),columns,MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"video/mp4"}, null);
		//得到索引
		int indexVideoId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
		int indexVideoName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
		int indexVideoPath = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		int indexVideoSize = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
		int indexVideoType = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
		int indexVideoDate = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
		//填充值
		List<Video> videos = new ArrayList<Video>();
		while (cursor.moveToNext()) {
			Video video = new Video();
			video.setId(cursor.getLong(indexVideoId));
			video.setName(cursor.getString(indexVideoName));
			video.setPath(cursor.getString(indexVideoPath));
			video.setSize(cursor.getInt(indexVideoSize));
			video.setType(cursor.getString(indexVideoType));
			video.setDateAdded(cursor.getLong(indexVideoDate));
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
