package com.hcd.jbox2d.game.view;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.hcd.jbox2d.game.activity.LevelActivity;
import com.hcd.jbox2d.game.activity.Stage1Activity;
import com.hcd.jbox2d.game.obj.Line;
import com.hcd.jbox2d.game.obj.Platform;
import com.hcd.jbox2d.game.obj.Polygon;
import com.hcd.jbox2d.game.utils.CutPolygonUtils;
import com.hcd.jbox2d.game.utils.GrahamScanUtils;
import com.hcd.jbox2d.game.utils.PolygonCenterUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Stage1View extends View {

	private Stage1View gameView;
	//���ذٷֱ�
	private static float PASSSCORE = 0.8f;
	//������Ļ����������ı���px/m
	private final static int RATE = 60;
	private World world;
	//ģ���Ƶ��
	private float timeStep;
	//����Խ��ģ��Խ��ȷ��������Խ��
	private int iterations;
	private Handler mHandler;
	//��Ļ�Ŀ����߶�
	private float screenWidth, screenHeight;
	private Polygon polygon2;
	private ArrayList<Polygon> polygons;
	private Line line = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	private boolean inScreen, outScreen;
	private static int lineNum;
	private Platform platform;
	private ArrayList<Platform> platforms;
	//�ж����������Ƿ񶼾�ֹ
	private boolean isSleeping;
	//��Ϸ���÷����İٷֱ�
	private int removed;
	//��ʼ���������
	private float initArea;
	private float cutArea;
	
	public boolean gameOver;
	public boolean gameSuccess;
	public boolean haveWriteDb;
	
	private Canvas canvas;
	private Paint paint;
	
	public Stage1View(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);
		initGame();
	}

	private void drawPlatform(Platform platform) {
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
		if (gameOver) {
			if (gameSuccess) {
				canvas.drawText("SUCCESS!", screenWidth - 100, 30, paint);
				//��Ϸ���غ��ʼ����һ������
				if (!haveWriteDb) {
					if (LevelActivity.lvManager.getStageByLevel(2).size() == 0){
						LevelActivity.lvManager.insertLevelInfo(2, 0, 0);
					}else if (LevelActivity.lvManager.getStageByLevel(2).size() == 1) {
						//��һ�������ݲ��ó�ʼ��
						//LevelActivity.lvManager.updateLevelInfo(3, 0, 0);
					} else {
						Log.i("Erro Message", "������ݲ���Ψһ��");
					}
				}
			} else {
				canvas.drawText("FAILED!", screenWidth - 80, 30, paint);
			}
			if (!haveWriteDb) {
				//��Ϸ�����󱣴�����
				if (LevelActivity.lvManager.getStageByLevel(1).size() == 0){
					LevelActivity.lvManager.insertLevelInfo(1, removed, 1);
				}else if (LevelActivity.lvManager.getStageByLevel(1).size() == 1) {
					int oldScore = LevelActivity.lvManager.getStageByLevel(1).get(0).getScore();
					LevelActivity.lvManager.updateLevelInfo(1, oldScore > removed ? oldScore : removed, 1);
				} else {
					Log.i("Erro Message", "������ݲ���Ψһ��");
				}
				haveWriteDb = true;
			}
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		setBackgroundColor(Color.WHITE);
		for (int i = 0; i < platforms.size(); i++) {
			drawPlatform(platforms.get(i));
		}
		for (int i = 0; i < polygons.size(); i++){
			drawPolygon(polygons.get(i));
		}
		if (line != null) {
			drawLine(line.getV1().x, line.getV1().y, line.getV2().x, line.getV2().y);
		}
		setScore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
	
	/**
	 * ��ʼ����Ϸ
	 */
	private void initGame() {
		polygons = new ArrayList<Polygon>();
		platforms = new ArrayList<Platform>();
		lineNum = 3;
		gameOver = false;
		gameSuccess = false;
		haveWriteDb = false;
		gameView = this;
		removed = 0;
		inScreen = false;
		outScreen = false;
		isSleeping = true;
		cutArea = 0.0f;
		screenWidth = Stage1Activity.screenWidth;
		screenHeight = Stage1Activity.screenHeight;
		Vec2 gravity = new Vec2(0.0f, 10.0f); // ������������ʾ��ǰ������������򣬵�һ������Ϊˮƽ���򣬸���Ϊ��������Ϊ�ҡ��ڶ���������ʾ��ֱ����
		world = new World(gravity);
		createPlatform();
		createBorder(false, false, false, false);
		createPolygon();
		timeStep = 1.0f / 60.0f; // ����Ƶ��
		iterations = 10; // �������
		mHandler = new Handler();
		mHandler.post(update);
		mHandler.post(cutloop);
	}

	private Runnable update = new Runnable() {

		@Override
		public void run() {
			if (!gameOver){
				world.step(timeStep, iterations, iterations);
				gameView.invalidate();
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
					gameSuccess = true;
					Log.i("��Ϸ���", "����");
				}else {
					gameSuccess = false;
				}
			}
			if (0.99 <= cutArea / initArea) {
				gameOver = true;
				gameSuccess = true;
			}
			mHandler.postDelayed(cutloop, (long) timeStep * 1000);
		}
	};


	private void createPlatform() {
		platform = new Platform(world, screenWidth * 3 / 8 / RATE, screenHeight * 5 / 6 / RATE, screenWidth * 5 / 8 / RATE, screenHeight * 7 / 8 / RATE);
		platforms.add(platform);
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
}

