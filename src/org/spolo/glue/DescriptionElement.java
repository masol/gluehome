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
package org.spolo.glue;

/**
 * 上传户型和装修模板以及样板间的时候每个元素的描述对象.
 * 
 * @date 2014-3-14
 */
public class DescriptionElement {
	private String name;
	private String path;
	private String type;
	private String selftype;
	private String homeid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSelftype() {
		return selftype;
	}

	public void setSelftype(String selftype) {
		this.selftype = selftype;
	}

	public String getHomeid() {
		return homeid;
	}
	public void setHomeid(String homeid) {
		this.homeid = homeid;
	}
}
