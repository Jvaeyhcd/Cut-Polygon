package com.hcd.jbox2d.game.obj;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * ��ֹ������ƽ̨
 * @author jvaeyhcd.com
 *
 */
public class Platform {

	//ƽ̨����Ļ�ϵ�����
	private float x1,y1,x2,y2;
	private Body body;
	//���������������ڵ�����
	private World world;
	
	public Platform(World world, float x1, float y1, float x2, float y2) {
		this.world = world;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		createPlatform();
	}
	
	private void createPlatform() {
		PolygonShape ps = new PolygonShape();
		// ���óɾ��Σ�ע�����������������ֱ��Ǵ˾��γ����һ��
		ps.setAsBox(Math.abs(x2 - x1) / 2, Math.abs(y2 - y1) / 2);
		FixtureDef fd = new FixtureDef();
		fd.friction = 0.2f;
		fd.restitution = 0.5f;
		fd.shape = ps;

		BodyDef bd = new BodyDef();
		bd.position = new Vec2((x1 + x2) / 2, (y1 + y2) / 2);
		bd.type = BodyType.STATIC;
		body = world.createBody(bd);
		body.createFixture(fd);
	}

	public float getX1() {
		return x1;
	}

	public float getY1() {
		return y1;
	}

	public float getX2() {
		return x2;
	}

	public float getY2() {
		return y2;
	}
}
