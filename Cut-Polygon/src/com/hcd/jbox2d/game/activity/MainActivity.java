package com.hcd.jbox2d.game.activity;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.hcd.jbox2d.game.obj.Line;
import com.hcd.jbox2d.game.obj.Platform;
import com.hcd.jbox2d.game.obj.Polygon;
import com.hcd.jbox2d.game.utils.CutPolygonUtils;
import com.hcd.jbox2d.game.utils.GrahamScanUtils;
import com.hcd.jbox2d.game.utils.PolygonCenterUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	//过关百分比
	private static float PASSSCORE = 0.7f;
	//物理屏幕与物理世界的比例px/m
	private final static int RATE = 60;
	private World world;
	//模拟的频率
	private float timeStep;
	//迭代越大，模拟越精确，但性能越低
	private int iterations;
	private Handler mHandler;
	private Jbox2dView myView;
	//屏幕的宽度与高度
	private float screenWidth, screenHeight;
	private Polygon polygon2;
	private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	private Line line = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	private boolean inScreen, outScreen;
	private static int lineNum = 3;
	private Platform platform;
	//判断所有物体是否都静止
	private boolean isSleeping;
	//游戏所得分数的百分比
	private float score;
	//初始物体的质量
	private float initArea;
	private float cutArea;
	
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
			
			//根据旋转的角度获得各个点的坐标
			for (int i = 0; i < polygon.getEdge(); i++) {
				float vecX = (float) (vecs[i].x * Math.cos(polygon.getAngle()) - vecs[i].y
						* Math.sin(polygon.getAngle()));
				float vecY = (float) (vecs[i].x * Math.sin(polygon.getAngle()) + vecs[i].y
						* Math.cos(polygon.getAngle()));
				tmp[i] = new Vec2(vecX, vecY);
			}
			Vec2 centerPoint = new Vec2(0, 0);//PolygonCenterUtils.getPolygonCenter(vecs);
			path.moveTo((polygon.getX() + tmp[0].x - centerPoint.x) * RATE,
					(polygon.getY() + tmp[0].y - centerPoint.y) * RATE);// 此点为多边形的起点
			for (int i = 1; i < polygon.getEdge(); i++) {
				path.lineTo((polygon.getX() + tmp[i].x - centerPoint.x) * RATE,
						(polygon.getY() + tmp[i].y - centerPoint.y) * RATE);
			}
			path.close(); // 使这些点构成封闭的多边形
			canvas.drawPath(path, paint);
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
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (lineNum > 0) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 如果拖动
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
					Log.d("触摸事件", "移动");
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 如果点击
					line.setV1(new Vec2(event.getX(), event.getY()));
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
					inScreen = true;
					Log.d("触摸事件", "点下屏幕");
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					line.setV2(new Vec2(event.getX(), event.getY()));
					outScreen = true;
					Log.d("触摸事件", "离开屏幕");
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
	 * 初始化游戏
	 */
	private void initGame() {
		inScreen = false;
		outScreen = false;
		isSleeping = true;
		cutArea = 0.0f;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 去title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		Vec2 gravity = new Vec2(0.0f, 10.0f); // 向量，用来标示当前世界的重力方向，第一个参数为水平方向，负数为做，正数为右。第二个参数表示垂直方向
		world = new World(gravity);
		createPlatform(0, screenHeight / 2, screenWidth / 2, 10);
		createBorder(false, true, false, false);
		createPolygon();
		myView = new Jbox2dView(this);
		timeStep = 1.0f / 60.0f; // 定义频率
		iterations = 10; // 定义迭代
		setContentView(myView);
		mHandler = new Handler();
		mHandler.post(update);
		mHandler.post(cutloop);
	}

	private Runnable update = new Runnable() {

		@Override
		public void run() {
			world.step(timeStep, iterations, iterations);
			myView.invalidate();
			mHandler.postDelayed(update, (long) timeStep * 1000);
		}
	};
	
	
	
	private Runnable cutloop = new Runnable() {
		
		@Override
		public void run() {
			if(inScreen && outScreen) {
				boolean isCut = false;
				ArrayList<Polygon> polytemp = new ArrayList<Polygon>();
				for (int i = 0; i < polygons.size(); i++) {
					Vec2[] temp = new Vec2[2];
					temp[0] = new Vec2(line.getV1().x / RATE, line.getV1().y / RATE);
					temp[1] = new Vec2(line.getV2().x / RATE, line.getV2().y / RATE);
					if ((CutPolygonUtils.getLeftCutPolygon(polygons.get(i).getNowVecs(), temp) != null) && 
							(CutPolygonUtils.getRightCutPolygon(polygons.get(i).getNowVecs(), temp) != null)) {
						isCut = true;
						//物体被切成两块后，应该先去除之前创建在世界中的物体
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
			//判断物体是否调出屏幕外面以及物体是否停止运动
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
						Log.i("掉出屏幕了","cutArea / initArea=" + cutArea / initArea);
					}
				}
				polygons.clear();
				polygons = temp_poly;
			}
			if (isSleeping && lineNum <= 0) {
				Log.i("游戏状态", "游戏结束" + cutArea / initArea);
				if (PASSSCORE <= cutArea / initArea) {
					//游戏过关
					Log.i("游戏结果", "过关");
				}
			}
			mHandler.postDelayed(cutloop, (long) timeStep * 1000);
		}
	};


	private void createPlatform(float x, float y, float width, float height) {
//										
//		PolygonShape ps = new PolygonShape();
//		// 设置成矩形，注意这里是两个参数分别是此矩形长宽的一半
//		ps.setAsBox(width / RATE, height / RATE);
//		FixtureDef fd = new FixtureDef();
//		fd.friction = 1.0f;
//		fd.restitution = 0.5f;
//		fd.shape = ps;
//
//		BodyDef bd = new BodyDef();
//		bd.position = new Vec2(x / RATE, y / RATE);
//		m_platform = world.createBody(bd);
//		m_platform.createFixture(fd);
		platform = new Platform(world, 0, screenHeight / 2 / RATE, screenWidth / 2 / RATE, screenHeight * 9 / 16 / RATE);
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
		vecs[0] = new Vec2(screenWidth / 3 / RATE, (screenHeight - 10) / 6 / RATE);
		vecs[1] = new Vec2(-screenWidth / 3 / RATE, (screenHeight - 10) / 6 / RATE);
		vecs[2] = new Vec2(-screenWidth / 3 / RATE, -(screenHeight - 10) / 6 / RATE);
		vecs[3] = new Vec2(screenWidth / 3 / RATE, -(screenHeight - 10) / 6 / RATE);
		Vec2[] vecst = GrahamScanUtils.getGrahamScan(vecs);
		polygon2 = new Polygon(world, screenWidth / 3 / RATE, (screenHeight) / 3 / RATE, vecst, vecst.length, 0.0f,
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
		 * 强制变成竖屏，不能变成横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

}