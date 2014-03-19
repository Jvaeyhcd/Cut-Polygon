package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * 多边形切割工具类
 * @author jvaeyhcd.com
 *
 */
public class CutPolygonUtils {

	//被切的多边形的点集坐标，这些点击坐标是一个逆时针排列的凸包
	private static Vec2[] vecsPol;
	//切割多边形的线段
	private static Vec2[] line = new Vec2[2];
	//线段与多边形的交点
	private static Vec2[] intersections,instmp;
	//被切割后的多边形的集合
	private static Vec2[] vecsleft, vecsright;
	//交点数目
	private static int num, numl, numr;
	//切割多边形的线段的直线方程系数ax+by+c=0
	private static double a,b,c;
	
	/**
	 * 得到直线line与多边形vecsPol的交点的集合intersections
	 */
	private static void getPolygonsInter() {
		num = 0;
		vecsPol = GrahamScanUtils.getGrahamScan(vecsPol);
		instmp = new Vec2[vecsPol.length];
		if (LineIntersectionUtils.segmentsIntr(vecsPol[0], vecsPol[vecsPol.length - 1], line[0], line[1]) != null) {
			instmp[num] = LineIntersectionUtils.segmentsIntr(vecsPol[0], vecsPol[vecsPol.length - 1], line[0], line[1]);
			num++;
		}
		for (int i =0 ; i < vecsPol.length - 1; i++) {
			if (LineIntersectionUtils.segmentsIntr(vecsPol[i], vecsPol[i + 1], line[0], line[1]) != null) {
				instmp[num] = LineIntersectionUtils.segmentsIntr(vecsPol[i], vecsPol[i + 1], line[0], line[1]);
				num++;
			}
		}
		intersections = new Vec2[num];
		for (int i =0 ; i < num; i++) {
			intersections[i] = instmp[i];
		}
	}
	
	/**
	 * 根据多边形的点与交点的叉积来分类分割后的两个多边形的点的集合
	 */
	private static void getDisPolygon() {
		//获得线段与多边形的交点
		getPolygonsInter();
		if ((intersections != null) && (intersections.length >= 2)) {
			//得到线段方程系数
			getLineCoefficient();
			Vec2[] vecstl = new Vec2[vecsPol.length], vecstr = new Vec2[vecsPol.length];
			numl = 0; numr = 0;
			for (int i = 0; i < vecsPol.length; i++) {
				//分割线段的右边区域
				if (a * vecsPol[i].x + b * vecsPol[i].y + c >= 0) {
					vecstr[numr] = vecsPol[i];
					numr++;
				} else {
					vecstl[numl] = vecsPol[i];
					numl++;
				}
			}
			vecsleft = new Vec2[intersections.length + numl];
			vecsright = new Vec2[intersections.length + numr];
			for (int i = 0; i < numl; i++) {
				vecsleft[i] = vecstl[i];
			}
			for (int i = 0;i < intersections.length; i++) {
				vecsleft[i + numl] = intersections[i];
			}
			vecsleft = GrahamScanUtils.getGrahamScan(vecsleft);
			for (int i = 0; i < numr; i++) {
				vecsright[i] = vecstr[i];
			}
			for (int i = 0;i < intersections.length; i++) {
				vecsright[i + numr] = intersections[i];
			}
			vecsright = GrahamScanUtils.getGrahamScan(vecsright);
		}
	}
	
	public static Vec2[] getLeftCutPolygon(Vec2[] vp, Vec2[] vl) {
		vecsleft = null;
		vecsPol = vp;
		line = vl;
		getDisPolygon();
		return vecsleft;
	}
	
	public static Vec2[] getRightCutPolygon(Vec2[] vp, Vec2[] vl) {
		vecsright = null;
		vecsPol = vp;
		line = vl;
		getDisPolygon();
		return vecsright;
	}
	
	private static void getLineCoefficient() {
		if (line != null) {
			a = line[1].y - line[0].y;
			b = line[0].x - line[1].x;
			c = (line[1].x - line[0].x) * line[0].y - (line[1].y - line[0].y) * line[0].x;
		}
	}
	
}
