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

import org.apache.commons.exec.CommandLine;

/**
 * @param <K>
 * @param <E>
 * @param <O>
 * @date 23 Dec 2013
 */
public abstract class GUtilCommand {
	// 所请求的命令行
	protected String url = null;
	// gutil的临时目录
	protected File tempfile = null;
	// 每个命令执行后的结果存放目录
	protected File cmd_temp_file = null;
	// 命令执行之后的结果
	@SuppressWarnings("rawtypes")
	protected GutilResult m_GutilResult = null;

	public GUtilCommand() {
		this.tempfile = GlueUtil.getTempDir();
	}

	protected CommandLine getBaseCommandLine(String url) {
		File gutil = new File("gutil//gutil.exe");
		CommandLine command = new CommandLine(gutil);
		command.addArgument("openweb");
		command.addArgument("--url=" + url);
		command.addArgument("--maximized=true");
		System.out.println(command);
		return command;
	}

	public void onProcessComplete(int arg0) {
		try {
			m_GutilResult = createSuccessResult(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onProcessFailed(Exception arg0) {
		try {
			m_GutilResult = createFailedResult(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 具体的命令行构造
	 * 
	 * @return
	 */
	public abstract CommandLine getCommandLine();

	/**
	 * 命令正常执行之后的正确结果构造
	 * 
	 * @return
	 */
	public abstract GutilResult createSuccessResult(int exitcode)
			throws Exception;

	/**
	 * 命名执行失败之后的结果构造
	 * 
	 * @param exception
	 */
	public abstract GutilResult createFailedResult(Exception exception)
			throws Exception;

	/**
	 * 结果处理对象
	 * 
	 * @return
	 */
	protected GUtilCommand getExecuteResultHandler() {
		return this;
	}

	/**
	 * 获取执行的结果
	 * 
	 * @return
	 */
	public GutilResult getResult() throws Exception {
		return this.m_GutilResult;
	}
}
