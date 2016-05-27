package com.xter.player.view;

import com.xter.player.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class VolumeView extends View {

	private Paint mPaint;

	private int mFrontColor;
	private int mBackColor;
	private int mLineWidth;
	private int mCount;
	private Bitmap mIcon;

	public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public VolumeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
	}

	public VolumeView(Context context) {
		super(context);
		initView(context, null);
	}

	protected void initView(Context context, AttributeSet attrs) {
		final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);
		final int count = ta.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.VolumeView_front_color:
				mFrontColor = ta.getColor(attr, 0);
				break;
			case R.styleable.VolumeView_back_color:
				mBackColor = ta.getColor(attr, 0);
				break;
			case R.styleable.VolumeView_count:
				mCount = ta.getInt(attr, 10);
				break;
			case R.styleable.VolumeView_line_width:
				mLineWidth = ta.getDimensionPixelSize(attr, 10);
				break;
			case R.styleable.VolumeView_icon:
				mIcon = BitmapFactory.decodeResource(getResources(), ta.getResourceId(attr, 0));
				break;
			}
		}
		ta.recycle();
		
		mPaint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawArc(new RectF(0,0,20,20), 0, 40, true, mPaint);
		canvas.drawColor(Color.WHITE);
	}

}
