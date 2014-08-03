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
 * @date Jul 23, 2014
 */
public class RendercfgBean {
	public RenderData data;
	public String cookies;	
	public String hid;

	public class RenderData {
		public String unitPrice;
		public String jobprice;
		public String notifyEmail;
		public String[] cameraName;
		public String resolution;
		public String samples;
		public String userJobName;
		public String watermark;
		public String customerName;
		public String type;
		public String size;
	}

}
