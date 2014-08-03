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
package org.spolo.glue.OutputDebugInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @date Nov 21, 2013
 */
public class LogFileOutputStream extends PrintStream {
	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public LogFileOutputStream(File file) throws FileNotFoundException {
		super(file);
	}

	@Override
	public void write(byte[] buf, int off, int len) {
		String message = new String(buf, off, len);
		if (message.equals("\n") || message.equals("\r")
				|| message.equals("\r\n")) {
			super.write(message.getBytes(), off, message.length());

		} else {
			message = "[" + IoUtil.getTime() + "]" + message;
			super.write(message.getBytes(), off, message.length());
		}
		message = null;
	}
}
