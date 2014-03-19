package com.hcd.jbox2d.game.activity;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.hcd.jbox2d.game.obj.Line;
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
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	//������Ļ����������ı���px/m
	private final static int RATE = 10;
	private World world;
	//�����е�һЩ����
	private Body m_platform;
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
	private boolean iscut = false;
	//�ж����������Ƿ񶼾�ֹ
	private boolean isStatic = true;

	class Jbox2dView extends View {

		private Canvas canvas;
		private Paint paint;

		public Jbox2dView(Context context) {
			super(context);
			paint = new Paint();
		}


		private void drawPlatform(float x, float y, float width, float height) {
			paint.setAntiAlias(true);
			paint.setColor(Color.DKGRAY);
			canvas.drawRect(x * RATE, y * RATE, x * RATE + width, y * RATE
					+ height, paint);
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

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			this.canvas = canvas;
			drawPlatform(m_platform.getPosition().x, m_platform.getPosition().y
					- 10 / RATE, screenWidth / 2, 20);
			
			for (int i = 0; i < polygons.size(); i++){
				drawPolygon(polygons.get(i));
			}
			if (line != null) {
				drawLine(line.getV1().x, line.getV1().y, line.getV2().x, line.getV2().y);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (isStatic) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����϶�
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ������
					line.setV1(new Vec2(event.getX(), event.getY()));
					line.setV2(new Vec2(event.getX(), event.getY()));
					invalidate();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					line.setV2(new Vec2(event.getX(), event.getY()));
					iscut = true;
				}
			}
			return true;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥtitle
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		Vec2 gravity = new Vec2(0.0f, 10.0f); // ������������ʾ��ǰ������������򣬵�һ������Ϊˮƽ���򣬸���Ϊ��������Ϊ�ҡ��ڶ���������ʾ��ֱ����
		world = new World(gravity);
		createPlatform(0, screenHeight / 2, screenWidth / 2, 10);
		createBorder();
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
			world.step(timeStep, iterations, iterations);
			myView.invalidate();
			mHandler.postDelayed(update, (long) timeStep * 1000);
		}
	};
	
	private Runnable cutloop = new Runnable() {
		
		@Override
		public void run() {

			if(iscut) {
				ArrayList<Polygon> polytemp = new ArrayList<Polygon>();
				for (int i = 0; i < polygons.size(); i++) {
					isStatic = !polygons.get(i).getBody().isAwake();
					Vec2[] temp = new Vec2[2];
					temp[0] = new Vec2(line.getV1().x / RATE, line.getV1().y / RATE);
					temp[1] = new Vec2(line.getV2().x / RATE, line.getV2().y / RATE);
					if ((CutPolygonUtils.getLeftCutPolygon(polygons.get(i).getNowVecs(), temp) != null) && 
							(CutPolygonUtils.getRightCutPolygon(polygons.get(i).getNowVecs(), temp) != null)) {
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
				iscut = false;
			}
			mHandler.postDelayed(cutloop, (long) timeStep * 1000);
		}
	};


	private void createPlatform(float x, float y, float width, float height) {

		PolygonShape ps = new PolygonShape();
		// ���óɾ��Σ�ע�����������������ֱ��Ǵ˾��γ����һ��
		ps.setAsBox(width / RATE, height / RATE);
		FixtureDef fd = new FixtureDef();
		fd.friction = 1.0f;
		fd.restitution = 0.5f;
		fd.shape = ps;

		BodyDef bd = new BodyDef();
		bd.position = new Vec2(x / RATE, y / RATE);
		m_platform = world.createBody(bd);
		m_platform.createFixture(fd);
	}


	private void createBorder() {

		BodyDef bd = new BodyDef();
		Body border = world.createBody(bd);
		EdgeShape es = new EdgeShape();
		es.set(new Vec2(0, 0), new Vec2(0, screenHeight / RATE));

		border.createFixture(es, 0.0f);

		es.set(new Vec2(screenWidth / RATE, 0), new Vec2(screenWidth / RATE,
				screenHeight / RATE));
		border.createFixture(es, 0.0f);

		es.set(new Vec2(0, screenHeight / RATE), new Vec2(screenWidth / RATE,
				screenHeight / RATE));
		border.createFixture(es, 0.0f);

		es.set(new Vec2(0, 0), new Vec2(screenWidth / RATE, 0));
		border.createFixture(es, 0.0f);
	}

	private void createPolygon() {
		Vec2[] vecs = new Vec2[4];
		vecs[0] = new Vec2(screenWidth / 2 / RATE, 40 / RATE);
		vecs[1] = new Vec2(-screenWidth / 2 / RATE, 40 / RATE);
		vecs[2] = new Vec2(-screenWidth / 2 / RATE, -40 / RATE);
		vecs[3] = new Vec2(screenWidth / 2 / RATE, -40 / RATE);
		Vec2[] vecst = GrahamScanUtils.getGrahamScan(vecs);
		polygon2 = new Polygon(world, 240 / RATE, 350 / RATE, vecst, vecst.length, 0.5f,
				0.5f, 1.0f, 0.0f);
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
		 * ǿ�Ʊ�����������ܱ�ɺ���
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

}