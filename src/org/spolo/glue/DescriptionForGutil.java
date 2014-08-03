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

import java.util.ArrayList;

/**
 * 为Gutil提供的一个所要上传文件列表的描述对象
 * 
 * @date 2014-3-14
 */
public class DescriptionForGutil {
	private ArrayList<DescriptionElement> description;

	public ArrayList<DescriptionElement> getDescription() {
		return description;
	}

	public void setDescription(ArrayList<DescriptionElement> description) {
		this.description = description;
	}

}
