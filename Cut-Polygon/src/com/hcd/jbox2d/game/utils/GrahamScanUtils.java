package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * 求解凸包集合的工具类
 * 给定的一个点的集合，所构成的凸包的集合
 * 我所理解的凸包就是给定二维平面上的点集，凸包就是将最外层的点连接起来构成的凸多边型，它能包含点集中所有点的
 * @author jvaeyhcd.com
 *
 */
public class GrahamScanUtils {

	private static Vec2[] vecs, vecst;
	private static int top;
	
	/**
	 * 计算两点之间的距离
	 * @param a
	 * @param b
	 * @return 两点的距离
	 */
	private static double distance(Vec2 a, Vec2 b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	
	/**
	 * 求线段v0v1与线段v0v2的叉积
	 * @param v1
	 * @param v2
	 * @param v0
	 * @return 叉积，如果大于0，则说明直线v0v1在v0v2的顺时针方向，即v0v1在v0v2下面
	 */
	private static double cross(Vec2 v1, Vec2 v2, Vec2 v0) {
		return (v1.x - v0.x) * (v2.y - v0.y) - (v2.x - v0.x) * (v1.y - v0.y);
	}
	private static void grahamScan() {
		Vec2 tmp;
		int k = 0;
		//找出最左下角的点作为起点
		for (int i = 1; i < vecs.length; ++i) {
			if ((vecs[i].y < vecs[k].y) || ((vecs[i].y == vecs[k].y) && (vecs[i].x < vecs[k].x))) {
				k = i;
			}
		}
		tmp = vecs[0];
		vecs[0] = vecs[k];
		vecs[k] = tmp;
		
		//按照级角大小逆时针排序
		for (int i = 1; i < vecs.length; ++i) {
			k = i;
			for (int j = i + 1; j < vecs.length; ++j) {
				if ((cross(vecs[j], vecs[k], vecs[0]) > 0) || ((cross(vecs[j], vecs[k], vecs[0]) == 0) && (distance(vecs[0], vecs[j]) < distance(vecs[0], vecs[k])))) {
					k = j;
				}
			}
			tmp = vecs[i];
			vecs[i] = vecs[k];
			vecs[k] = tmp;
		}
		
		//先把极交最小的3个存入栈中
		top = -1;
		vecst[++top] = vecs[0];
		vecst[++top] = vecs[1];
		vecst[++top] = vecs[2];
		
		for (int i = 3; i < vecs.length; ++i) {
			//当叉积为0时说明三点共线，共线的点也保留在凸包中
			while(cross(vecs[i], vecst[top], vecst[top - 1]) > 0){
				top--;
			}
			vecst[++top] = vecs[i];
		}
		top++;
	}

	public static Vec2[] getGrahamScan(Vec2[] vec) {
		vecs = vec;
		vecst = new Vec2[vecs.length];
		grahamScan();
		Vec2[] vecsres = new Vec2[top];
		for (int i = 0; i < top; i++) {
			vecsres[i] = vecst[i];
		}
		return deleteDuplicateVecs(vecsres);
	}
	
	private static Vec2[] deleteDuplicateVecs(Vec2[] vecs) {
		int len = 0;
		Vec2[] vecres,vecst = new Vec2[vecs.length];
		for (int i = 0; i < vecs.length; i++) {
			
			boolean haveSame = false;
			for (int j =i + 1; j < vecs.length; j++) {
				if ((vecs[i].x == vecs[j].x) && (vecs[i].y == vecs[j].y)) {
					//删除元素
					haveSame = true;
				}
			}
			if (!haveSame) {
				vecst[len] = vecs[i];
				len++;
			}
		}
		vecres = new Vec2[len];
		for (int i = 0 ; i < len; i++) {
			vecres[i] = vecst[i];
		}
		return vecres;
	}
}
