package com.hcd.jbox2d.game.activity;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.hcd.jbox2d.game.obj.GameButton;
import com.hcd.jbox2d.game.obj.Line;
import com.hcd.jbox2d.game.obj.Platform;
import com.hcd.jbox2d.game.obj.Polygon;
import com.hcd.jbox2d.game.utils.CutPolygonUtils;
import com.hcd.jbox2d.game.utils.GrahamScanUtils;
import com.hcd.jbox2d.game.utils.PolygonCenterUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FirstActivity extends Activity {

	//���ذٷֱ�
	private static float PASSSCORE = 1.0f;
	//������Ļ����������ı���px/m
	private final static int RATE = 60;
	private World world;
	//ģ���Ƶ��
	private float timeStep;
	//����Խ��ģ��Խ��ȷ��������Խ��
	private int iterations;
	private Handler mHandler;
	private Jbox2dView myView;
	//��Ļ�Ŀ����߶�
	private float screenWidth, screenHeight;
	private Polygon polygon2;
	private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	private Line line = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	private boolean inScreen, outScreen;
	private static int lineNum = 3;
	private Platform platform;
	//�ж����������Ƿ񶼾�ֹ
	private boolean isSleeping;
	//��Ϸ���÷����İٷֱ�
	private int removed;
	//��ʼ���������
	private float initArea;
	private float cutArea;
	private boolean gameOver;
	private GameButton retryButton, homeButton, exitButton;
	private boolean touchRetryButton = false;
	
	class Jbox2dView extends View {

		private Canvas canvas;
		private Paint paint;

		public Jbox2dView(Context context) {
			super(context);
			paint = new Paint();
		}


		private void drawPlatform() {
			paint.setAntiAlias(true);
			paint.setColor(Color.DKGRAY);
			canvas.drawRect(platform.getX1() * RATE, platform.getY1() * RATE, platform.getX2() * RATE, platform.getY2() * RATE, paint);
		}


		private void drawLine(float x1, float y1, float x2, float y2) {
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			canvas.drawLine(x1, y1, x2, y2, paint);
		}

		private void drawPolygon(Polygon polygon) {
			paint.setAntiAlias(true);
			paint.setColor(Color.rgb(111, 100, 10));
			Path path = new Path();
			Vec2[] vecs = polygon.getVecs();
			Vec2[] tmp = new Vec2[polygon.getEdge()];
			
			//������ת�ĽǶȻ�ø����������
			for (int i = 0; i < polygon.getEdge(); i++) {
				float vecX = (float) (vecs[i].x * Math.cos(polygon.getAngle()) - vecs[i].y
						* Math.sin(polygon.getAngle()));
				float vecY = (float) (vecs[i].x * Math.sin(polygon.getAngle()) + vecs[i].y
						* Math.cos(polygon.getAngle()));
				tmp[i] = new Vec2(vecX, vecY);
			}
			Vec2 centerPoint = new Vec2(0, 0);//PolygonCenterUtils.getPolygonCenter(vecs);
			path.moveTo((polygon.getX() + tmp[0].x - centerPoint.x) * RATE,
					(polygon.getY() + tmp[0].y - centerPoint.y) * RATE);// �˵�Ϊ����ε����
			for (int i = 1; i < polygon.getEdge(); i++) {
				path.lineTo((polygon.getX() + tmp[i].x - centerPoint.x) * RATE,
						(polygon.getY() + tmp[i].y - centerPoint.y) * RATE);
			}
			path.close(); // ʹ��Щ�㹹�ɷ�յĶ����
			canvas.drawPath(path, paint);
		}

		private void setScore() {
			paint.setTypeface(Typeface.SANS_SERIF);
			paint.setTextSize(20);
			canvas.drawText("Removed:" + removed+"%", 10 , screenHeight - 10, paint);
			canvas.drawText("Target:" + (int)Math.round(PASSSCORE*100)+"%" , 150, screenHeight - 10, paint);
			if (removed >= PASSSCORE * 100) {
				canvas.drawText("SUCCESS!", screenWidth - 100, 30, paint);
			} else {
				if (lineNum == 3) {
					canvas.drawLine(screenWidth - 20, 10, screenWidth - 30, 30, paint);
					canvas.drawLine(screenWidth - 30, 10, screenWidth - 40, 30, paint);
					canvas.drawLine(screenWidth - 40, 10, screenWidth - 50, 30, paint);
				} else if (lineNum == 2) {
					canvas.drawLine(screenWidth - 20, 10, screenWidth - 30, 30, paint);
					canvas.drawLine(screenWidth - 30, 10, screenWidth - 40, 30, paint);
				} else if(lineNum == 1) {
					canvas.drawLine(screenWidth - 20, 10, screenWidth - 30, 30, paint);
				}
			}
		}
		
		private void drawButton()
		{
			paint.setColor(retryButton.getColor());
			if (touchRetryButton){
				paint.setColor(Color.CYAN);
			}
			canvas.drawRect(retryButton.getX1(), retryButton.getY1(), retryButton.getX2(), retryButton.getY2(), paint);
			paint.setColor(homeButton.getColor());
			canvas.drawRect(homeButton.getX1(), homeButton.getY1(), homeButton.getX2(), homeButton.getY2(), paint);
			paint.setColor(exitButton.getColor());
			canvas.drawRect(exitButton.getX1(), exitButton.getY1(), exitButton.getX2(), exitButton.getY2(), paint);
			paint.setTextSize(retryButton.getY2()-retryButton.getY1());
			paint.setColor(Color.BLACK);
			paint.setTypeface(Typeface.SERIF);
			float offSetY = (retryButton.getY2() - retryButton.getY1()) / 6;
			float offSetX = screenWidth / 20;
			canvas.drawText(retryButton.getText(), retryButton.getX1() + offSetX, retryButton.getY2() - offSetY, paint);
			canvas.drawText(homeButton.getText(), homeButton.getX1() + offSetX, homeButton.getY2() - offSetY, paint);
			canvas.drawText(exitButton.getText(), exitButton.getX1() + offSetX, exitButton.getY2() - offSetY, paint);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			this.canvas = canvas;
			setBackgroundColor(Color.WHITE);
			drawPlatform();
			for (int i = 0; i < polygons.size(); i++){
				drawPolygon(polygons.get(i));
			}
			if (line != null) {
				drawLine(line.getV1().x, line.getV1().y, line.getV2().x, line.getV2().y);
			}
			setScore();
			if (gameOver) {
				paint.setColor(Color.YELLOW);
				canvas.drawRect(screenWidth*4/5, screenHeight*4/5, screenWidth, screenHeight, paint);
			}
			drawButton();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				if (event.getX() >= retryButton.getX1() && event.getX() <= retryButton.getX2() && event.getY() >= retryButton.getY1() && event.getY() <= retryButton.getY2()){
					touchRetryButton = true;
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP){
				touchRetryButton = false;
			}
			if (lineNum > 0) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����϶�
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
					Log.d("�����¼�", "�ƶ�");
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ������
					line.setV1(new Vec2(event.getX(), event.getY()));
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
					inScreen = true;
					Log.d("�����¼�", "������Ļ");
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					line.setV2(new Vec2(event.getX(), event.getY()));
					outScreen = true;
					Log.d("�����¼�", "�뿪��Ļ");
				}
			}
			return true;
		}

	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGame();
	}
	
	/**
	 * ��ʼ����Ϸ
	 */
	private void initGame() {
		gameOver = false;
		removed = 0;
		inScreen = false;
		outScreen = false;
		isSleeping = true;
		cutArea = 0.0f;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥtitle
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		
		retryButton = new GameButton(screenWidth*4/5, screenHeight*4/5, screenWidth, screenHeight*13/15, "Retry", Color.YELLOW);
		homeButton = new GameButton(screenWidth*4/5, screenHeight*13/15, screenWidth, screenHeight*14/15, "Home", Color.GREEN);
		exitButton = new GameButton(screenWidth*4/5, screenHeight*14/15, screenWidth, screenHeight, "Exit", Color.MAGENTA);
		
		Vec2 gravity = new Vec2(0.0f, 10.0f); // ������������ʾ��ǰ������������򣬵�һ������Ϊˮƽ���򣬸���Ϊ��������Ϊ�ҡ��ڶ���������ʾ��ֱ����
		world = new World(gravity);
		createPlatform(0, screenHeight / 2, screenWidth / 2, 10);
		createBorder(false, false, false, false);
		createPolygon();
		myView = new Jbox2dView(this);
		timeStep = 1.0f / 60.0f; // ����Ƶ��
		iterations = 10; // �������
		setContentView(myView);
		
		mHandler = new Handler();
		mHandler.post(update);
		mHandler.post(cutloop);
	}

	private Runnable update = new Runnable() {

		@Override
		public void run() {
			if (!gameOver){
				world.step(timeStep, iterations, iterations);
				myView.invalidate();
				mHandler.postDelayed(update, (long) timeStep * 1000);
			}
		}
	};
	
	private Runnable cutloop = new Runnable() {
		
		@Override
		public void run() {
			if(inScreen && outScreen && !gameOver) {
				boolean isCut = false;
				ArrayList<Polygon> polytemp = new ArrayList<Polygon>();
				for (int i = 0; i < polygons.size(); i++) {
					Vec2[] temp = new Vec2[2];
					temp[0] = new Vec2(line.getV1().x / RATE, line.getV1().y / RATE);
					temp[1] = new Vec2(line.getV2().x / RATE, line.getV2().y / RATE);
					if ((CutPolygonUtils.getLeftCutPolygon(polygons.get(i).getNowVecs(), temp) != null) && 
							(CutPolygonUtils.getRightCutPolygon(polygons.get(i).getNowVecs(), temp) != null)) {
						isCut = true;
						//���屻�г������Ӧ����ȥ��֮ǰ�����������е�����
						world.destroyBody(polygons.get(i).getBody());
						Vec2[] left = CutPolygonUtils.getLeftCutPolygon(polygons.get(i).getNowVecs(), temp);
						float x = PolygonCenterUtils.getPolygonCenter(left).x;
						float y = PolygonCenterUtils.getPolygonCenter(left).y;
						left = PolygonCenterUtils.getStandardPolygon(left);
						Polygon pleft = new Polygon(world, x, y, left, left.length, 0.1f, 0.1f, 1.0f, 0.0f);
						Vec2[] right = CutPolygonUtils.getRightCutPolygon(polygons.get(i).getNowVecs(), temp);
						x = PolygonCenterUtils.getPolygonCenter(right).x;
						y = PolygonCenterUtils.getPolygonCenter(right).y;
						right = PolygonCenterUtils.getStandardPolygon(right);
						Polygon pright = new Polygon(world, x, y, right, right.length, 0.1f, 0.1f, 1.0f, 0.0f);
						polytemp.add(pright);
						polytemp.add(pleft);
					} else {
						polytemp.add(polygons.get(i));
					}
				}
				polygons.clear();
				polygons = polytemp;
				inScreen = false;
				outScreen = false;
				line = new Line(0.0f, 0.0f, 0.0f, 0.0f);
				if (isCut) lineNum--;
			}
			//�ж������Ƿ������Ļ�����Լ������Ƿ�ֹͣ�˶�
			isSleeping = true;
			{
				ArrayList<Polygon> temp_poly = new ArrayList<Polygon>();
				for (int i = 0; i < polygons.size(); i++) {
					if (polygons.get(i).getBody().isAwake()) {
						isSleeping = false;
					}
					Vec2[] vecs = polygons.get(i).getNowVecs();
					boolean out_screen = true;
					for (int j = 0; j < vecs.length; j++) {
						if (vecs[j].x * RATE < screenWidth && vecs[j].y * RATE < screenHeight) {
							out_screen = false;
						}
					}
					if (!out_screen) {
						temp_poly.add(polygons.get(i));
					} else {
						cutArea += polygons.get(i).getMass();
						Log.i("������Ļ��","cutArea / initArea=" + cutArea / initArea);
						removed = (int) Math.rint((cutArea / initArea) * 100);
					}
				}
				polygons.clear();
				polygons = temp_poly;
			}
			if (isSleeping && lineNum <= 0) {
				gameOver = true;
				Log.i("��Ϸ״̬", "��Ϸ����" + cutArea / initArea);
				if (PASSSCORE <= cutArea / initArea) {
					//��Ϸ����
					Log.i("��Ϸ���", "����");
				}
			}
			if (PASSSCORE == cutArea / initArea) {
				gameOver = true;
			}
			mHandler.postDelayed(cutloop, (long) timeStep * 1000);
		}
	};


	private void createPlatform(float x, float y, float width, float height) {
		platform = new Platform(world, screenWidth * 3 / 8 / RATE, screenHeight * 5 / 6 / RATE, screenWidth * 5 / 8 / RATE, screenHeight * 7 / 8 / RATE);
	}


	private void createBorder(boolean top,boolean left, boolean bottom, boolean right) {

		BodyDef bd = new BodyDef();
		Body border = world.createBody(bd);
		EdgeShape es = new EdgeShape();
		if (left) {
			es.set(new Vec2(0, 0), new Vec2(0, screenHeight / RATE));
			border.createFixture(es, 0.0f);
		}
		if (bottom) {
			es.set(new Vec2(screenWidth / RATE, 0), new Vec2(screenWidth / RATE,
					screenHeight / RATE));
			border.createFixture(es, 0.0f);
		}
		if (right) {
			es.set(new Vec2(0, screenHeight / RATE), new Vec2(screenWidth / RATE,
					screenHeight / RATE));
			border.createFixture(es, 0.0f);
		}
		if (top) {
			es.set(new Vec2(0, 0), new Vec2(screenWidth / RATE, 0));
			border.createFixture(es, 0.0f);
		}
	}

	private void createPolygon() {
		Vec2[] vecs = new Vec2[4];
		vecs[0] = new Vec2(screenWidth / 5 / RATE, (screenHeight) / 3 / RATE);
		vecs[1] = new Vec2(-screenWidth / 5 / RATE, (screenHeight) / 3 / RATE);
		vecs[2] = new Vec2(-screenWidth / 5 / RATE, -(screenHeight) / 3 / RATE);
		vecs[3] = new Vec2(screenWidth / 5 / RATE, -(screenHeight) / 3 / RATE);
		Vec2[] vecst = GrahamScanUtils.getGrahamScan(vecs);
		polygon2 = new Polygon(world, screenWidth / 2 / RATE, (screenHeight) / 2 / RATE, vecst, vecst.length, 0.0f,
				0.5f, 1.0f, 0.0f);
		initArea = polygon2.getMass();
		polygons.add(polygon2);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {

		/**
		 * ǿ�Ʊ�ɺ��������ܱ������
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}
}