/**
 *  This file is part of the spp(Superpolo Platform).
 *  Copyright (C) by SanPolo Co.Ltd.
 *  All rights reserved.
 *
 *  See http://www.spolo.org/ for more information.
 *
 *  SanPolo Co.Ltd
 *  http://www.spolo.org/
 *  Any copyright issues, please contact: copr@spolo.org
 **/
package org.spolo.glue.smartlaytout;


/**
 * 所有摆放算法的接口
 * 
 * @date Oct 11, 2013
 */
public interface GCalculate {

	/**
	 * 具体的计算方法,不同的实现方式会从参照物中获取此计算方法中所需要的数值,
	 * 
	 * 同时依据目标物中的各种信息来计算出该物体所有场景信息
	 * 
	 * 坐标点,长宽高,缩放系数,旋转角度等等.
	 * 
	 * @param ro
	 *            参考物
	 * @param so
	 *            目标物
	 * @return 所有的结果集
	 */
	public GResult calculate(ReferenceObject ro, SourceObject so);
}
