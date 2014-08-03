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

import org.apache.commons.io.FileUtils;

/**
 * @date 23 Dec 2013
 */
/**
 * @date 25 Dec 2013
 * @param <MAIN>
 * @param <ADDTION>
 * @param <JSON>
 */
/**
 * @date 25 Dec 2013
 * @param <MAIN>
 *            主要资源结果
 * @param <ADDTION>
 *            附加资源结果
 * @param <JSON>
 *            描述对象
 */
public class GutilResult<MAIN, ADDTION, ASSET, JSON> {
	protected File tempFile;
	protected int exitCode = -1;
	protected JSON jsonObject = null;
	protected MAIN mainResourceFile = null;
	protected ADDTION additionResourceFile = null;
	protected ASSET assetFile = null;

	/**
	 * 命令执行完之后的临时目录
	 * 
	 * @param tmpf
	 */
	public GutilResult(File tmpf) {
		this.tempFile = tmpf;
	}

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public MAIN getMainResourceFile() {
		return mainResourceFile;
	}

	public void setMainResourceFile(MAIN mainResourceFile) {
		this.mainResourceFile = mainResourceFile;
	}

	public JSON getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSON jsonObject) {
		this.jsonObject = jsonObject;
	}

	public File getTempFile() {
		return tempFile;
	}

	public ADDTION getAdditionResourceFile() {
		return additionResourceFile;
	}

	public void setAdditionResourceFile(ADDTION additionResourceFile) {
		this.additionResourceFile = additionResourceFile;
	}

	public ASSET getAssetFile() {
		return assetFile;
	}

	public void setAssetFile(ASSET assetFile) {
		this.assetFile = assetFile;
	}

	/**
	 * 清理临时目录
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO 可以对临时目录做清理
		// 子类可以继续重载此方法,但是必须super.finalize(),以便清理临时目录
//		 FileUtils.forceDelete(tempFile);
	}
}
