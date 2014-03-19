package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;

/**
 * ������и����
 * @author jvaeyhcd.com
 *
 */
public class CutPolygonUtils {

	//���еĶ���εĵ㼯���꣬��Щ���������һ����ʱ�����е�͹��
	private static Vec2[] vecsPol;
	//�и����ε��߶�
	private static Vec2[] line = new Vec2[2];
	//�߶������εĽ���
	private static Vec2[] intersections,instmp;
	//���и��Ķ���εļ���
	private static Vec2[] vecsleft, vecsright;
	//������Ŀ
	private static int num, numl, numr;
	//�и����ε��߶ε�ֱ�߷���ϵ��ax+by+c=0
	private static double a,b,c;
	
	/**
	 * �õ�ֱ��line������vecsPol�Ľ���ļ���intersections
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
	 * ���ݶ���εĵ��뽻��Ĳ��������ָ�����������εĵ�ļ���
	 */
	private static void getDisPolygon() {
		//����߶������εĽ���
		getPolygonsInter();
		if ((intersections != null) && (intersections.length >= 2)) {
			//�õ��߶η���ϵ��
			getLineCoefficient();
			Vec2[] vecstl = new Vec2[vecsPol.length], vecstr = new Vec2[vecsPol.length];
			numl = 0; numr = 0;
			for (int i = 0; i < vecsPol.length; i++) {
				//�ָ��߶ε��ұ�����
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
