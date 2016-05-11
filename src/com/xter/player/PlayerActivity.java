package com.xter.player;

import com.xter.player.util.LogUtils;
import com.xter.player.util.SysUtils;
import com.xter.player.view.PlayerController;
import com.xter.player.view.PlayerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class PlayerActivity extends Activity {

	PlayerView surPlayerView;
	PlayerController fraPlayerController;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		initLayout();
	}

	protected void initLayout() {
		surPlayerView = (PlayerView) findViewById(R.id.sur_player_view);
		fraPlayerController = (PlayerController) findViewById(R.id.fra_player_ctr);
		surPlayerView.setPlayerController(fraPlayerController);
//		surPlayerView.setVideoPath(SysUtils.getSDCardPath()+"/Download/mm.mp4");
//		surPlayerView.setVideoPath("http://10.198.1.54/mmm.mp4");
		surPlayerView.setVideoPath("/storage/emulated/0/tencent/MobileQQ/funcall/2/shuimu_v_1.mp4");
	}

}
