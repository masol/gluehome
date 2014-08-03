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

import org.spolo.glue.smartlaytout.LayoutFeature.Feature;

/**
 * 每个布局特性所对应的计算方式的管理类
 * 
 * @date Oct 11, 2013
 */
public class LaytouToCalculateMap {

	/**
	 * 返回此布局特性的计算方式
	 * 
	 * @param sLayoutFeature
	 *            布局特性
	 * @return 计算对象
	 * @throws Exception
	 */
	public GCalculate getCalculate(LayoutFeature sLayoutFeature)
			throws Exception {
		Feature f = sLayoutFeature.getFeatureEnum();
		GCalculate refobj = f.getCaculate();
		return refobj;
	}
}
