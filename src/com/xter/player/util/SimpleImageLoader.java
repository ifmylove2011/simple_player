package com.xter.player.util;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

public class SimpleImageLoader {
	// 载入位图线程标识
	public static final int MESSAGE_POST_RESULT = 1;
	// CPU数量
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	// 核心线程数--保持存活
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	// 最大线程数
	private static int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	// 非核心线程闲置回收时间
	private static final long KEEP_ALIVE = 10L;

	
	
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
		}

	};

	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
			KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10), sThreadFactory);

	private Handler mMainHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				LoaderResult result = (LoaderResult) msg.obj;
				result.imageView.setImageBitmap(result.bitmap);
				LogUtils.i("setBitmap");
			}
		}

	};

	public static SimpleImageLoader build(Context context) {
		return new SimpleImageLoader();
	}
	
	public void setBitmap(final Bitmap bitmap, final ImageView imageView) {
		// 开启载入任务
		Runnable loadBitmapTask = new Runnable() {

			@Override
			public void run() {
				LoaderResult result = new LoaderResult(bitmap, imageView);
				mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
			}
		};
		THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
	}

	private static class LoaderResult {
		public Bitmap bitmap;
		public ImageView imageView;

		public LoaderResult(Bitmap bitmap, ImageView imageView) {
			super();
			this.bitmap = bitmap;
			this.imageView = imageView;
		}
	}

}