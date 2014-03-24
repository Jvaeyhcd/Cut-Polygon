package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * 计算多边形面积工具类
 * @author jvaeyhcd.com
 *
 */
public class PolygonAreaUtils {

	public static float getPolygonArea(Vec2[] vecs) {
		float area = 0.0f;
		for (int i = 0, j = 1; i < vecs.length; i++, j++) {
			j = j % vecs.length;
			area += vecs[i].x * vecs[j].y - vecs[i].y * vecs[j].x;
		}
		return Math.abs(area / 2);
	}
}
