package com.xter.player.view;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.xter.player.util.LogUtils;
import com.xter.player.util.SysUtils;
import com.xter.player.view.PlayerController.PlayerControl;

import java.io.IOException;

public class PlayerView extends SurfaceView implements Callback, PlayerControl {

	public static final int STATE_PLAYING = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_PREPARED = 3;

	private int mPlayerState;

	private Uri mUri;
	private String mPath;
	private MediaPlayer mPlayer;
	private Context mContext;
	private SurfaceHolder mHolder;
	private PlayerController playerController;

	private int mVideoWidth;
	private int mVideoHeight;
	private int mDuration;
	private int mBufferProgress;

	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PlayerView(Context context) {
		super(context);
		init(context, null);
	}

	protected void init(Context context, AttributeSet attrs) {
		mContext = context;
		// 这里不给mHolder赋值是为了避免openVideo的过早运行
		getHolder().addCallback(this);
	}

	public void setVideoPath(String path) {
		LogUtils.d(path);
		mUri = Uri.parse(path);
		mPath = path;
		openVideo();
	}

	protected void openVideo() {
		if (mUri == null || mHolder == null)
			return;
		AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		try {
			mPlayer = new MediaPlayer();

			mPlayer.setOnPreparedListener(onPreparedListener);
			mPlayer.setOnErrorListener(onErrorListener);
			mPlayer.setOnCompletionListener(onCompletionListener);
			mPlayer.setOnInfoListener(onInfoListener);
			mPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);

			mPlayer.setDataSource(mPath);
			mPlayer.setDisplay(mHolder);
			mPlayer.setScreenOnWhilePlaying(true);
			mPlayer.prepareAsync();
			LogUtils.d("准备");

			playerController.setPlayerControl(this);
		} catch (IOException e) {
			playerController.showError();
			e.printStackTrace();
		}
	}

	private OnPreparedListener onPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			mPlayerState = STATE_PREPARED;

			mVideoHeight = mp.getVideoHeight();
			mVideoWidth = mp.getVideoWidth();

			getHolder().setFixedSize(mVideoWidth, mVideoHeight);

			mDuration = mp.getDuration();
			playerController.initTvPos();
			playerController.setTitle(SysUtils.getMovieTitle(mPath));
		}
	};

	private OnErrorListener onErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			LogUtils.e("ERROR:" + what + "," + extra);
			playerController.showError();
			return true;
		}
	};

	private OnCompletionListener onCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			LogUtils.i("总时长" + mp.getDuration() / 1000 + "s 结束");
		}
	};

	private OnInfoListener onInfoListener = new OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			boolean flag = false;
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				LogUtils.i("onInfo MediaPlayer.MEDIA_INFO_BUFFERING_START");
				if (playerController != null) {
					playerController.showLoading();
				}
				flag = true;
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				LogUtils.i("onInfo MediaPlayer.MEDIA_INFO_BUFFERING_END");
				if (playerController != null) {
					playerController.hideLoading();
				}
				flag = true;
				break;
			}
			LogUtils.i("INFO:" + what + "," + extra);
			return flag;
		}
	};

	private OnBufferingUpdateListener onBufferingUpdateListener = new OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mBufferProgress = percent;
		}
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		LogUtils.d("sur created");
		mHolder = holder;
		openVideo();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHolder = null;
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}
	}

	public void setPlayerController(PlayerController controller) {
		playerController = controller;
		playerController.setPlayerControl(this);
	}

	protected boolean inNormalState() {
		return mPlayerState == STATE_PAUSED || mPlayerState == STATE_PREPARED;
	}

	@Override
	public void start() {
		mPlayer.start();
		mPlayerState = STATE_PLAYING;
	}

	@Override
	public void pause() {
		if (mPlayer.isPlaying())
			mPlayer.pause();
		mPlayerState = STATE_PAUSED;
	}

	@Override
	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}

	@Override
	public int getDuration() {
		if (mPlayer == null)
			return 0;
		if (mDuration != mPlayer.getDuration()) {
			return mDuration;
		}
		return mPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		if (mPlayer != null) {
			return mPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public int getBufferPercent() {
		if (mPlayer != null)
			return mBufferProgress;
		return 0;
	}

	@Override
	public void seekTo(int pos) {
		if (mPlayer != null) {
			mPlayer.seekTo(pos);
		}
	}

	@Override
	public void close() {

	}

}
