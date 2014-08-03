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

import java.util.logging.Level;
import java.util.logging.Logger;


import com.eteks.sweethome3d.model.Home;

/**
 * 所有分类到这个分类所对应的参考物的一个映射关系维护类
 * 
 * @date Oct 11, 2013
 */
public class CategoryToReferenceMap {
	private Home m_Home;

	private Logger log = Logger.getLogger("CategoryToReferenceMap-log");
	/**
	 * 参照物中的所有信息都是从Home对象中获取
	 * 
	 * @param home
	 */
	public CategoryToReferenceMap(Home home) {
		this.m_Home = home;
	}

	/**
	 * 返回指定的分类所依赖的参照对象
	 * 
	 * @param category
	 *            分类
	 * @return 参考物
	 * @throws Exception
	 */
	public ReferenceObject getReferenceMap(GCategory category) throws Exception {
		// TODO Auto-generated method stub
		GCategoryEnum cate = category.getCategoryEnum();
		if (cate == null) {
			log.log(Level.INFO, "该模型不支持自动摆放");
			return null;
		}else {
			ReferenceObject ro = cate.getReference();
			ro.setHome(this.m_Home);
			return ro;
		}
		
	}
}
