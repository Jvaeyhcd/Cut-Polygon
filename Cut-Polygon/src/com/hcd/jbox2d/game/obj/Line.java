package com.hcd.jbox2d.game.obj;

import org.jbox2d.common.Vec2;

public class Line {

	private Vec2 v1, v2;
	
	public Line(Vec2 v1, Vec2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Line(float x1, float y1, float x2, float y2) {
		this.v1 = new Vec2(x1, y1);
		this.v2 = new Vec2(x2, y2);
	}

	public Line() {
		// TODO Auto-generated constructor stub
	}

	public Vec2 getV1() {
		return v1;
	}

	public void setV1(Vec2 v1) {
		this.v1 = v1;
	}

	public Vec2 getV2() {
		return v2;
	}

	public void setV2(Vec2 v2) {
		this.v2 = v2;
	}
	
	
}
