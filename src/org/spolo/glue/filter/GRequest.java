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
package org.spolo.glue.filter;

/**
 * @date Jul 30, 2013
 */
public class GRequest<T> {
	protected T m_paramter;

	public GRequest(T h) {
		this.m_paramter = h;
	}

	public T getParameter() {
		return m_paramter;
	}
}
