package com.hcd.jbox2d.game.utils;

import org.jbox2d.common.Vec2;
/**
 * 求两直线的交点工具类
 * @author jvaeyhcd.com
 *
 */
public class LineIntersectionUtils {
	
	/**
	 * 计算直线ab与直线cd的交点
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return 直线ab与直线cd的交点
	 */
	public static Vec2 segmentsIntr(Vec2 a, Vec2 b, Vec2 c, Vec2 d){  
		  
	    // 三角形abc 面积的2倍  
	    double area_abc = (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);  
	  
	    // 三角形abd 面积的2倍  
	    double area_abd = (a.x - d.x) * (b.y - d.y) - (a.y - d.y) * (b.x - d.x);   
	  
	    // 面积符号相同则两点在线段同侧,不相交 (对点在线段上的情况,本例当作不相交处理);  
	    if ( area_abc * area_abd > 0 ) {  
	        return null;
	    }  
	  
	    // 三角形cda 面积的2倍  
	    double area_cda = (c.x - a.x) * (d.y - a.y) - (c.y - a.y) * (d.x - a.x);  
	    // 三角形cdb 面积的2倍  
	    // 注意: 这里有一个小优化.不需要再用公式计算面积,而是通过已知的三个面积加减得出.  
	    double area_cdb = area_cda + area_abc - area_abd ;  
	    if (  area_cda * area_cdb > 0 ) {  
	        return null;  
	    }  
	  
	    //计算交点坐标  
	    double t = area_cda / ( area_abd- area_abc );  
	    double dx= t*(b.x - a.x), dy= t*(b.y - a.y);
	    return new Vec2((float)(a.x + dx), (float)(a.y + dy)); 
	}

}
