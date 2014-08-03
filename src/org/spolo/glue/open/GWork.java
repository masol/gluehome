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
package org.spolo.glue.open;

/**
 * @date Jul 23, 2014
 */
public abstract class GWork {	
	private GErrorHandler error = null;
	public abstract void doWork() throws Exception;

	public void setErrorHandler(GErrorHandler e){
		this.error = e;
	}
	public GErrorHandler getErrorHandler(){
		return error;
	}
}
