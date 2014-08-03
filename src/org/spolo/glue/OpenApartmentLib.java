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
import java.util.Collection;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;

import com.eteks.sweethome3d.viewcontroller.GlueController;

/**
 * @date 23 Dec 2013
 */
public class OpenApartmentLib extends GUtilCommand {
	
	private File tempFile = GlueUtil.createTempFolder();

	public OpenApartmentLib() {
		super();
//		this.url = "http://www.xuanran001.com/s/gh/decorationlib/index.html";
		this.url = MappingURL.getSroomlibURL();
		System.out.println(url);
	}

	@Override
	public CommandLine getCommandLine() {
		// TODO 获取基础commandline对象
		CommandLine cmd = getBaseCommandLine(url);
		// 然后构造出所每个命令行所独立的参数
		// TODO 获取临时目录
		 cmd.addArgument("--dir=" + tempFile.getAbsolutePath());
		 cmd.addArgument("--appdata=" + GUtilRunTools.getTools().getGlueDataDir());
		 System.out.println(cmd);
		return cmd;
	}

	@Override
	public GutilResult createSuccessResult(int exitcode) throws Exception {
		m_GutilResult = new GutilResult<File, File, File[], RoomInformation>(
				tempFile);
		String[] jsonsuffix = { "json" };
		String[] sh3dsuffix = { "sh3d" };
		String[] jpgsuffix = { "jpg", "png" };
		Collection<File> sh3d_files = FileUtils.listFiles(tempFile, sh3dsuffix,
				true);
		Collection<File> json_files = FileUtils.listFiles(tempFile, jsonsuffix,
				true);
		Collection<File> img_files = FileUtils.listFiles(tempFile, jpgsuffix,
				true);
		// 设置sh3d主文件的位置,如果有多个,只设置第一个
		m_GutilResult.setExitCode(exitcode);
		if (sh3d_files != null && !sh3d_files.isEmpty()) {
//			System.out.println("++++" + sh3d_files.size());
			File sh3d = ((File[])sh3d_files.toArray(new File[]{}))[0];
//			System.out.println(sh3d.getAbsolutePath());
			m_GutilResult.setMainResourceFile(sh3d);
		}
		// 设置所附带的json文件的位置,如果有多个只设置第一个
		if (json_files != null && !json_files.isEmpty()) {
			File json = ((File[]) json_files.toArray(new File[]{}))[0];
			m_GutilResult.setAdditionResourceFile(json);
		}
		// 设置所附带的预览图的文件
		if (img_files != null && !img_files.isEmpty()) {
			File[] priviews = ((File[]) img_files.toArray(new File[]{}));
			m_GutilResult.setAssetFile(priviews);
		}
		// 读取json文件,将json文件变为对象. TODO 如果后面肯定会附带一个json文件的话 就可以直接在父类里面操作
		// JsonUtil.readJson(json, GDocument.class);
		// m_GutilResult.setJsonObject(null);

		return m_GutilResult;
	}

	@Override
	public GutilResult createFailedResult(Exception exception) throws Exception {
		return m_GutilResult;
	}


}
