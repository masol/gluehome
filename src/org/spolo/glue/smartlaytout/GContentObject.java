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

import java.io.File;

/**
 * 一个json文件中信息到对象的映射类
 * 
 * @date Oct 15, 2013
 */
/**
 * @date Oct 15, 2013
 */
public class GContentObject {
	private String category;
	private String feature;
	private String id;
	// 仅有 furniture=1/texture=3 两种属性,因为通过gson来赋值,所以这不适用枚举
	private int type;
	private String path;
	private File m_file;
	private boolean abletouse = false;
	private boolean isCache = false;
	private String selftype;
	/**
	 * 此模型的分类
	 * 
	 * @return
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * 布局特性
	 * 
	 * @return
	 */
	public String getFeature() {
		return this.feature;
	}

	/**
	 * 模型id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 标示此对象是一个家具还是一个贴图
	 * 
	 * @return
	 */
	public int getType() {
		return this.type;
	}

	public void setSourceFile(File f) {
		this.m_file = f;
	}

	public File getSourceFile() {
		return this.m_file;
	}

	public void setPath(String p) {
		this.path = p;
	}

	public void setType(int t) {
		this.type = t;
	}

	public void setCategory(String cate) {
		this.category = cate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public boolean isAbletouse() {
		return abletouse;
	}

	public void setAbletouse(boolean abletouse) {
		this.abletouse = abletouse;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setIsCache(boolean isCache) {
		this.isCache = isCache;
	}

	public String getSelftype() {
		return selftype;
	}

	public void setSelftype(String selftype) {
		this.selftype = selftype;
	}
	

}
