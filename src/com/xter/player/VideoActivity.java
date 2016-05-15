package com.xter.player;

import android.app.Activity;
import android.os.Bundle;

import com.xter.player.view.PlayerController;
import com.xter.player.view.PlayerView;

public class VideoActivity extends Activity {

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
		surPlayerView.setPlayerController(fraPlayerController);
//		surPlayerView.setVideoPath(SysUtils.getSDCardPath()+"/Download/mm.mp4");
//		surPlayerView.setVideoPath("http://10.198.1.54/mmm.mp4");
//		surPlayerView.setVideoPath("/storage/emulated/0/tencent/MobileQQ/funcall/2/shuimu_v_1.mp4");
		surPlayerView.setVideoPath(videoUri);
	}
}
