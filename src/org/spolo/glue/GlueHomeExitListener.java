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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @date 2013-8-7
 */
public class GlueHomeExitListener {

	/**
	 * 清理GlueTemp目录中所有内容
	 */
	public static void clearGlueTemp() {
		File gluetemp = GlueUtil.getTempDir();
		try {
			FileUtils.forceDelete(gluetemp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
