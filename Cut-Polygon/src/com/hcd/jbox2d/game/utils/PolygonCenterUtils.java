package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * 求多边形的重心工具类
 * @author jvaeyhcd.com
 *
 */
public class PolygonCenterUtils {

	public static Vec2 getPolygonCenter(Vec2[] vecs) {
		float x = 0, y = 0;
		vecs = GrahamScanUtils.getGrahamScan(vecs);
		for (int i = 0; i < vecs.length; i++) {
			x += vecs[i].x;
			y += vecs[i].y;
		}
		return new Vec2(x / vecs.length, y / vecs.length);
	}
	
	public static Vec2[] getStandardPolygon(Vec2[] vecs) {
		Vec2[] res = new Vec2[vecs.length];
		Vec2 center = getPolygonCenter(vecs);
		for (int i = 0; i < vecs.length; i++) {
			Vec2 v = new Vec2(vecs[i].x - center.x, vecs[i].y - center.y);
			res[i] = v;
		}
		return res;
	}
}
