package com.hcd.jbox2d.game.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class TestView extends View {

	private Paint paint;
	public TestView(Context context) {
		super(context);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText("Hello!", 100, 100, paint);
	}
}
