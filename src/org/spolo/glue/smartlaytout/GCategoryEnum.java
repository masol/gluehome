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

import java.util.EnumSet;

/**
 * 墙体,地面,天花板,任意位置的分类信息
 * 
 * @date Oct 14, 2013
 */
public enum GCategoryEnum {
	Home("GHome"),
	Floor("GFloor"),
	Ceiling("GCeiling"),
	Random("Random");	;
	private String m_name;

	private GCategoryEnum(String name) {
		this.m_name = name;
	}

	/**
	 * 获取此分类对应的参考对象
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ReferenceObject getReference() throws Exception {
		Class ref = Class.forName("org.spolo.glue.smartlaytout.categoryimpl."
				+ this.getName());
		ReferenceObject refobj = (ReferenceObject) ref.newInstance();
		return refobj;

	}

	public String getName() {
		return this.m_name;
	}

	/**
	 * @param category_str
	 * @return
	 */
	public static GCategoryEnum formString(String category_str) {
		for (GCategoryEnum Category : EnumSet.range(GCategoryEnum.Home,
				GCategoryEnum.Random)) {
			if (category_str.compareTo(Category.getName()) == 0) {
				return Category;
			}
		}
		return null;
	}
}
