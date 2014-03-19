package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * ���͹�����ϵĹ�����
 * ������һ����ļ��ϣ������ɵ�͹���ļ���
 * ��������͹�����Ǹ�����άƽ���ϵĵ㼯��͹�����ǽ������ĵ������������ɵ�͹����ͣ����ܰ����㼯�����е��
 * @author jvaeyhcd.com
 *
 */
public class GrahamScanUtils {

	private static Vec2[] vecs, vecst;
	private static int top;
	
	/**
	 * ��������֮��ľ���
	 * @param a
	 * @param b
	 * @return ����ľ���
	 */
	private static double distance(Vec2 a, Vec2 b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	
	/**
	 * ���߶�v0v1���߶�v0v2�Ĳ��
	 * @param v1
	 * @param v2
	 * @param v0
	 * @return ������������0����˵��ֱ��v0v1��v0v2��˳ʱ�뷽�򣬼�v0v1��v0v2����
	 */
	private static double cross(Vec2 v1, Vec2 v2, Vec2 v0) {
		return (v1.x - v0.x) * (v2.y - v0.y) - (v2.x - v0.x) * (v1.y - v0.y);
	}
	private static void grahamScan() {
		Vec2 tmp;
		int k = 0;
		//�ҳ������½ǵĵ���Ϊ���
		for (int i = 1; i < vecs.length; ++i) {
			if ((vecs[i].y < vecs[k].y) || ((vecs[i].y == vecs[k].y) && (vecs[i].x < vecs[k].x))) {
				k = i;
			}
		}
		tmp = vecs[0];
		vecs[0] = vecs[k];
		vecs[k] = tmp;
		
		//���ռ��Ǵ�С��ʱ������
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
		
		//�ȰѼ�����С��3������ջ��
		top = -1;
		vecst[++top] = vecs[0];
		vecst[++top] = vecs[1];
		vecst[++top] = vecs[2];
		
		for (int i = 3; i < vecs.length; ++i) {
			//�����Ϊ0ʱ˵�����㹲�ߣ����ߵĵ�Ҳ������͹����
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
					//ɾ��Ԫ��
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
