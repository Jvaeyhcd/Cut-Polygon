package com.hcd.jbox2d.game.obj;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.hcd.jbox2d.game.utils.PolygonAreaUtils;
import com.hcd.jbox2d.game.utils.PolygonCenterUtils;

/**
 * 定义一个多边形的类
 * @author jvaeyhcd.com
 *
 */
public class Polygon {

	//定义多边形物体所在的世界
	private World world;
	//多边形物体的坐标
	private float x, y;
	//构成多边形的点
	private Vec2[] vecs;
	//物体的弹性系数
	private float restitution;
	//物体的密度
	private float density;
	//物体的旋转角度和物体的摩擦
	private float angle, friction;
	//自定义多边形物体的边数
	private int edge;
	private Body body;
		
	public float getMass() {
		return PolygonAreaUtils.getPolygonArea(vecs);
	}
	
	public Polygon(World world, float x, float y, Vec2[] vecs, int edge,  float restitution, float density, float friction, float angle) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.vecs = vecs;
		this.restitution = restitution;
		this.density = density;
		this.angle = angle;
		this.edge = edge;
		createBody();
	}
	/**
	 * 创建多边形物体
	 */
	private void createBody() {
		//定义物体的形状是多边形形状
		PolygonShape shape = new PolygonShape();
		shape.set(vecs, edge);
		
		//设置多边形物体的一些固定的物理属性
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		
		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = BodyType.DYNAMIC;
		bd.angle = angle;
		bd.allowSleep = true;
		bd.setAwake(true);
		
		//根据bodyDef创建物体到世界中
		body = world.createBody(bd);
		//设置好物体的固定属性
		body.createFixture(fd);
	}
	
	public void destroyBody() {
		world.destroyBody(getBody());
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public float getX() {
		return body.getPosition().x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return body.getPosition().y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Vec2[] getVecs() {
		return vecs;
	}

	public void setVecs(Vec2[] vecs) {
		this.vecs = vecs;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getAngle() {
		return body.getAngle();
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public int getEdge() {
		return edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public Vec2[] getNowVecs() {
		Vec2[] tmp = new Vec2[edge];
		
		Vec2 centerPoint = PolygonCenterUtils.getPolygonCenter(vecs);
		//根据旋转的角度获得各个点的坐标
		for (int i = 0; i < edge; i++) {
			float vecX = (float) (vecs[i].x * Math.cos(getAngle()) - vecs[i].y
					* Math.sin(getAngle()));
			float vecY = (float) (vecs[i].x * Math.sin(getAngle()) + vecs[i].y
					* Math.cos(getAngle()));
			tmp[i] = new Vec2(vecX + getBody().getPosition().x - centerPoint.x , vecY + getBody().getPosition().y - centerPoint.y);
		}
		return tmp;
	}
}