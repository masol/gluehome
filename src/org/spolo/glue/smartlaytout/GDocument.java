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

import java.util.ArrayList;

/**
 * @date Oct 16, 2013
 */
public class GDocument {
	private String docid;
	private ArrayList<GContentObject> sourceobjects;

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public ArrayList<GContentObject> getGconContentObject() {
		return sourceobjects;
	}

	public void setGconContentObject(ArrayList<GContentObject> gconContentObject) {
		this.sourceobjects = gconContentObject;
	}

}
