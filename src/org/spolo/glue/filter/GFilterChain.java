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

import java.util.ArrayList;

/**
 * @param <T>
 * @date Jul 30, 2013
 */
public class GFilterChain implements GFilter {
	ArrayList<GFilter> filters = new ArrayList<GFilter>();

	public void addFilter(GFilter f) {
		this.filters.add(f);
	}

	@Override
	public void doFilter(GRequest req, GResponse hres, GFilter chain) {
		for (int i = 0; i < filters.size(); i++) {
			filters.get(i).doFilter(req, hres, this);
		}
	}
}
