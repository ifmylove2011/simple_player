package com.xter.player.demo;

import java.io.IOException;

import com.xter.player.R;
import com.xter.player.util.LogUtils;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class DemoActivity extends Activity implements Callback {

	private SurfaceView demoSur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_player);
		initLayout();
	}

	/**
	 * 
	 */
	protected void initLayout() {
		demoSur = (SurfaceView) findViewById(R.id.demo_sur);
		demoSur.getHolder().addCallback(this);

	}

	protected void openVideo() {
		MediaPlayer mp = new MediaPlayer();

		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setDisplay(demoSur.getHolder());

		mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				LogUtils.d(mp.isPlaying()+"");
				mp.start();
				LogUtils.d(mp.isPlaying()+"");
			}
		});

		try {
			mp.setDataSource("http://10.198.1.54/mmm.mp4");
			mp.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		openVideo();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
}
