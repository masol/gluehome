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
 * 分类接口
 * 
 * 墙,地面,天花板
 * 
 * @date Oct 11, 2013
 */
public class GCategory {
	private GCategoryEnum m_GCategoryEnum;

	public GCategory(GCategoryEnum category) {
		this.m_GCategoryEnum = category;
	}

	/**
	 * 获得当前
	 * @return
	 */
	public String getName() {
		return this.m_GCategoryEnum.getName();
	}

	public GCategoryEnum getCategoryEnum() {
		return this.m_GCategoryEnum;
	}
}
