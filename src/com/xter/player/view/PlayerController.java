package com.xter.player.view;

import com.xter.player.R;
import com.xter.player.util.LogUtils;
import com.xter.player.util.SysUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayerController extends FrameLayout {

	/**
	 * 界面中间的布局--信息栏
	 */
	private LinearLayout llPlayerLoadingLayout;
	private LinearLayout llPlayerErrorLayout;
	private ImageView imgCenterPlayPause;
//	private TextView tvPlayerLoading;
//	private TextView tvPlayerError;
	private ProgressBar pgbPlayerLoading;

	/**
	 * 界面上方的布局--标题栏
	 */
	private LinearLayout llPlayerTitleBar;
	private TextView tvPlayerTitle;

	/**
	 * 界面下方的布局--操作栏
	 */
	private LinearLayout llPlayerBottomBar;
	private ImageButton ibtnPlayPause;
	private TextView tvCurPos;
	private TextView tvFinalPos;
	private ImageButton ibtnList;
	private SeekBar sbCurPos;

	private PlayerControl playerControl;

	public static final int SHOW_PROGRESS = 1;
	public static final int SHOW_BAR = 2;
	public static final int HIDE_BAR = 3;
	public static final int SHOW_LOADING = 4;
	public static final int SHOW_ERROR = 5;
	public static final int HIDE_LOADING = 6;
	public static final int SWITCH_PLAY_PAUSE_STATE = 7;
	public static final int SHOW_REFRESH = 8;

	public PlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public PlayerController(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PlayerController(Context context) {
		super(context);
		init(context, null);
	}

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_PROGRESS:
					int pos = setShowProgress();
					if (playerControl.isPlaying())
						sendMessageDelayed(obtainMessage(SHOW_PROGRESS), 1000 - (pos % 1000));
					break;
				case SHOW_BAR:
					showBar();
					break;
				case HIDE_BAR:
					hideBar();
					break;
				case SHOW_LOADING:
					showLoadingLayout();
					break;
				case HIDE_LOADING:
					hideLoadingLayout();
					break;
				case SHOW_ERROR:
					showErrorLayout();
					break;
				case SWITCH_PLAY_PAUSE_STATE:
					updateControlBar();
					break;
				case SHOW_REFRESH:
					showCenterRefresh();
					break;
			}
			super.handleMessage(msg);
		}
	};

	protected void init(Context context, AttributeSet attrs) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View rootView = layoutInflater.inflate(R.layout.controller_layout, this);
		initView(rootView);
		initListener();
	}

	protected void initView(View rootView) {
		llPlayerLoadingLayout = (LinearLayout) rootView.findViewById(R.id.player_loading_layout);
		llPlayerErrorLayout = (LinearLayout) rootView.findViewById(R.id.player_error_layout);
		imgCenterPlayPause = (ImageView) rootView.findViewById(R.id.img_player_center);
//		tvPlayerLoading = (TextView) rootView.findViewById(R.id.tv_loading);
//		tvPlayerError = (TextView) rootView.findViewById(R.id.tv_error);
		pgbPlayerLoading = (ProgressBar) rootView.findViewById(R.id.pgb_loading);

		llPlayerTitleBar = (LinearLayout) rootView.findViewById(R.id.title_bar);
		tvPlayerTitle = (TextView) rootView.findViewById(R.id.tv_playing_title);

		llPlayerBottomBar = (LinearLayout) rootView.findViewById(R.id.bottom_bar);
		ibtnPlayPause = (ImageButton) rootView.findViewById(R.id.ibtn_play_or_pause);
		ibtnList = (ImageButton) rootView.findViewById(R.id.ibtn_list);
		tvCurPos = (TextView) rootView.findViewById(R.id.tv_cur_pos);
		tvFinalPos = (TextView) rootView.findViewById(R.id.tv_final_pos);
		sbCurPos = (SeekBar) rootView.findViewById(R.id.seb_cur_pos);

		llPlayerLoadingLayout.setVisibility(GONE);
		llPlayerErrorLayout.setVisibility(GONE);
	}

	protected void initListener() {
		ibtnPlayPause.setOnClickListener(onPlayPauseClickListener);
		sbCurPos.setOnSeekBarChangeListener(onSeekBarChangeListener);
		imgCenterPlayPause.setOnClickListener(onCenterPlayClickListener);
	}

	public void setPlayerControl(PlayerControl control) {
		playerControl = control;
	}

	/**
	 * 转换播放/暂停状态
	 */
	protected void switchPlayPause() {
		if (playerControl.isPlaying()) {
			playerControl.pause();
			LogUtils.w("暂停" + SysUtils.longToTimeStr(playerControl.getCurrentPosition()));
		} else {
			playerControl.start();
			LogUtils.w("播放");
		}
		updateControlBar();
	}

	protected void updateControlBar() {
		if (playerControl.isPlaying()) {
			ibtnPlayPause.setBackgroundResource(R.drawable.ibtn_pause);
			imgCenterPlayPause.setVisibility(GONE);
		} else {
			ibtnPlayPause.setBackgroundResource(R.drawable.ibtn_play);
			imgCenterPlayPause.setImageResource(R.drawable.img_player_center);
			imgCenterPlayPause.setVisibility(VISIBLE);
		}
		mHandler.removeMessages(SHOW_PROGRESS);
		mHandler.sendMessage(mHandler.obtainMessage(SHOW_PROGRESS));
	}
	
	private OnClickListener onPlayPauseClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			LogUtils.i("play or pause");
			if (playerControl != null)
				switchPlayPause();
		}
	};

	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		int newPos = 0;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			int pos = playerControl.getDuration() * newPos / 1000;
			playerControl.seekTo(pos);
			updateControlBar();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			if (playerControl == null)
				return;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (playerControl == null || !fromUser)
				return;
			newPos = progress;
			int pos = playerControl.getDuration() * newPos / 1000;
			tvCurPos.setText(SysUtils.longToTimeStr(pos));
		}
	};

	private OnClickListener onPlayListClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	private OnClickListener onCenterPlayClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switchPlayPause();
		}
	};

	public void initTvPos() {
		tvCurPos.setText(SysUtils.longToTimeStr(0));
		tvFinalPos.setText(SysUtils.longToTimeStr(playerControl.getDuration()));
	}

	public int setShowProgress() {
		int currentPosition = playerControl.getCurrentPosition();
		tvCurPos.setText(SysUtils.longToTimeStr(currentPosition));
		if (playerControl != null) {
			int progress = currentPosition * 1000 / playerControl.getDuration();
			sbCurPos.setProgress(progress);
			sbCurPos.setSecondaryProgress(playerControl.getBufferPercent() * 10);
		}
		return currentPosition;
	}

	public void switchBar() {
		LogUtils.i("switch bar");
		mHandler.removeMessages(SHOW_BAR);
		mHandler.removeMessages(HIDE_BAR);
		if (isBarShowing()) {
			mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE_BAR), 5000);
		} else {
			mHandler.sendMessage(mHandler.obtainMessage(SHOW_BAR));
		}
	}

	public void showBar() {
		llPlayerTitleBar.setVisibility(VISIBLE);
		llPlayerBottomBar.setVisibility(VISIBLE);
		switchBar();
	}

	private void hideBar() {
		llPlayerTitleBar.setVisibility(GONE);
		llPlayerBottomBar.setVisibility(GONE);
	}

	public boolean isBarShowing() {
		return llPlayerTitleBar.isShown() && llPlayerBottomBar.isShown();
	}

	private void showErrorLayout() {
		llPlayerErrorLayout.setVisibility(VISIBLE);
	}

	private void showLoadingLayout() {
		llPlayerLoadingLayout.setVisibility(VISIBLE);
	}

	private void hideLoadingLayout() {
		llPlayerLoadingLayout.setVisibility(GONE);
	}
	
	private void showCenterRefresh(){
		imgCenterPlayPause.setVisibility(VISIBLE);
		imgCenterPlayPause.setImageResource(R.drawable.img_refresh);
	}

	public void showError() {
		mHandler.sendEmptyMessage(SHOW_ERROR);
	}

	public void showLoading() {
		mHandler.sendEmptyMessage(SHOW_LOADING);
	}

	public void hideLoading() {
		mHandler.sendEmptyMessage(HIDE_LOADING);
	}

	public void switchPlayPauseState(){
		mHandler.sendMessage(mHandler.obtainMessage(SWITCH_PLAY_PAUSE_STATE));
	}

	public void showBarState(){
		mHandler.sendMessage(mHandler.obtainMessage(SHOW_BAR));
	}
	
	public void showRefresh(){
		mHandler.sendMessage(mHandler.obtainMessage(SHOW_REFRESH));
	}

	public void removeCallback() {
		mHandler.removeMessages(SHOW_PROGRESS);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switchBar();
		return super.dispatchTouchEvent(ev);
	}

	public void setTitle(String title) {
		tvPlayerTitle.setText(title);
	}

	public interface PlayerControl {
		void start();

		void pause();

		boolean isPlaying();

		int getDuration();

		int getCurrentPosition();

		int getBufferPercent();

		void seekTo(int pos);

		void close();
	}
}