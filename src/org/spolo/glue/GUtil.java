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

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.commons.exec.ExecuteException;

/**
 * @date 20 Dec 2013
 */
public class GUtil {

	@SuppressWarnings("rawtypes")
	public static GutilResult execute(String cmd) throws Exception {
		GUtilCommand cmdobj = getGutilCommand(cmd);
		return execute(cmdobj);
	}

	/**
	 * 可以构造一个命令行对象来执行
	 * 
	 * @param cmd
	 * @return
	 * @throws ExecuteException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static GutilResult execute(GUtilCommand cmd) throws Exception {
		// 执行命令行
		GUtilRunTools.getTools().run(cmd);
		return cmd.getResult();
	}

	/**
	 * 执行命令行,直接传入所要执行的命令就可以了
	 * 
	 * @param OpenDecorationLib
	 *            ---打开云端装修模板库
	 * @return GUtilCommand
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static GUtilCommand getGutilCommand(String cmd)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class clazz = Class.forName("org.spolo.glue." + cmd);
		GUtilCommand cmdobj = (GUtilCommand) clazz.newInstance();
		return cmdobj;
	}
}
