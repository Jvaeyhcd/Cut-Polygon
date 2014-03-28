package com.hcd.jbox2d.game.obj;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ͼƬ��ť��
 * 
 * @author Salvador
 * 
 */
public class ImageButton {

	/** ��ťͼƬ **/
	private Bitmap mBitButton = null;

	/** ͼƬ���Ƶ�XY���� **/
	private int mPosX = 0;
	private int mPosY = 0;
	/** ͼƬ���ƵĿ�� **/
	private int mWidth = 0;
	private int mHeight = 0;

	public ImageButton(Context context, int frameBitmapID, int x, int y) {
		mBitButton = ReadBitMap(context, frameBitmapID);
		mPosX = x;
		mPosY = y;
		mWidth = mBitButton.getWidth();
		mHeight = mBitButton.getHeight();
	}

	/**
	 * ����ͼƬ��ť
	 * 
	 * @param canvas
	 * @param paint
	 */
	public void DrawImageButton(Canvas canvas, Paint paint) {
		canvas.drawBitmap(mBitButton, mPosX, mPosY, paint);
	}

	/**
	 * �ж��Ƿ����ͼƬ��ť
	 * 
	 * @param x
	 * @param y
	 */
	public boolean IsClick(int x, int y) {
		boolean isClick = false;
		if (x >= mPosX && x <= mPosX + mWidth && y >= mPosY
				&& y <= mPosY + mHeight) {
			isClick = true;
		}
		return isClick;
	}

	/**
	 * ��ȡͼƬ��Դ
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap ReadBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
}
