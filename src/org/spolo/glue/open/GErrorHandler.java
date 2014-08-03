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
 * @date Jul 28, 2014
 */
public abstract class GErrorHandler {
	private Exception m_exception;
	public abstract void error(String msg);

	public void setException(Exception e){
		this.m_exception = e;
	}

	public Exception getException() {
		return m_exception;
	}
	
}
