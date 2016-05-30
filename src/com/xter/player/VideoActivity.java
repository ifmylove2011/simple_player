package com.xter.player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.xter.player.view.PlayerController;
import com.xter.player.view.PlayerView;
import com.xter.player.view.PlayerView.IOrientationDetector;

public class VideoActivity extends Activity implements IOrientationDetector {

	PlayerView surPlayerView;
	PlayerController fraPlayerController;

	private String videoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		videoUri = getIntent().getStringExtra("path");
		initLayout();
	}

	protected void initLayout() {
		surPlayerView = (PlayerView) findViewById(R.id.sur_player_view);
		fraPlayerController = (PlayerController) findViewById(R.id.fra_player_ctr);
		// surPlayerView.setVideoPath(SysUtils.getSDCardPath()+"/Download/mm.mp4");
		// surPlayerView.setVideoPath("http://10.198.1.54/mmm.mp4");
		// surPlayerView.setVideoPath("/storage/emulated/0/tencent/MobileQQ/funcall/2/shuimu_v_1.mp4");
		surPlayerView.setPlayerController(fraPlayerController);
		surPlayerView.setOrientationDetector(this);
		surPlayerView.setVideoPath(videoUri);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		surPlayerView.close();
	}

	@Override
	public void onOrientationChanged(boolean reqLandscape) {
		boolean flag = getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		if (reqLandscape && flag) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
}
