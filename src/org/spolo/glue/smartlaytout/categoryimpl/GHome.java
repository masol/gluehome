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
package org.spolo.glue.smartlaytout.categoryimpl;

import java.util.logging.Logger;

import org.spolo.glue.smartlaytout.ReferenceObject;

/**
 * @date Oct 14, 2013
 */
public class GHome extends ReferenceObject {
	private Logger log = Logger.getLogger("Wall-log");

	@Override
	protected GHome clone() throws CloneNotSupportedException {
		return new GHome();
	}
}
