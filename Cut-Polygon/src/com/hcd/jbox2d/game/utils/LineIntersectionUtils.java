package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;
/**
 * ����ֱ�ߵĽ��㹤����
 * @author jvaeyhcd.com
 *
 */
public class LineIntersectionUtils {
	
	/**
	 * ����ֱ��ab��ֱ��cd�Ľ���
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return ֱ��ab��ֱ��cd�Ľ���
	 */
	public static Vec2 segmentsIntr(Vec2 a, Vec2 b, Vec2 c, Vec2 d){  
		  
	    // ������abc �����2��  
	    double area_abc = (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);  
	  
	    // ������abd �����2��  
	    double area_abd = (a.x - d.x) * (b.y - d.y) - (a.y - d.y) * (b.x - d.x);   
	  
	    // ���������ͬ���������߶�ͬ��,���ཻ (�Ե����߶��ϵ����,�����������ཻ����);  
	    if ( area_abc * area_abd > 0 ) {  
	        return null;
	    }  
	  
	    // ������cda �����2��  
	    double area_cda = (c.x - a.x) * (d.y - a.y) - (c.y - a.y) * (d.x - a.x);  
	    // ������cdb �����2��  
	    // ע��: ������һ��С�Ż�.����Ҫ���ù�ʽ�������,����ͨ����֪����������Ӽ��ó�.  
	    double area_cdb = area_cda + area_abc - area_abd ;  
	    if (  area_cda * area_cdb > 0 ) {  
	        return null;  
	    }  
	  
	    //���㽻������  
	    double t = area_cda / ( area_abd- area_abc );  
	    double dx= t*(b.x - a.x), dy= t*(b.y - a.y);
	    return new Vec2((float)(a.x + dx), (float)(a.y + dy)); 
	}

}
