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

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * 正常输出流
 */
public class NPrintStream extends PrintStream {
	private JTextArea component1;
	static StringBuffer Nsb = new StringBuffer();

	public NPrintStream(OutputStream out, JTextArea component1) {
		super(out);
		this.component1 = component1;

	}

	/**
	 * 重写write()方法，将输出信息填充到GUI组件。
	 */
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		component1.append(message);
	}

}
