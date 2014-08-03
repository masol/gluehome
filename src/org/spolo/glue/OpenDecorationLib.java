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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.smartlaytout.GDocument;


/**
 * @date 23 Dec 2013
 */
public class OpenDecorationLib extends GUtilCommand {
	
	private File tempFile = GlueUtil.createTempFolder();
//	private File tempFile = new File("D:\\test\\SweetHome3D-4.1-src\\gutil\\temp");
//	private File tempFile = new File(GUtilRunTools.getTools().getGlueDataDir().getPath()+ File.separator + "decoration");
	
	public OpenDecorationLib() {
		super();
//		this.url = "http://www.xuanran001.com/s/gh/decorationlib/index.html";
//		this.url = MappingURL.getDecorationlib();
		this.url = MappingURL.getDecoratlist();
	}

	@Override
	public CommandLine getCommandLine() {
		tempFile.mkdir();
		File src = new File(GUtilRunTools.getTools().getGlueDataDir().getAbsoluteFile()+ File.separator + "decorate.json");
		try {
			if (src.exists()) {
				FileUtils.moveFileToDirectory(src, tempFile, false);
				src.delete();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO 获取基础commandline对象
		CommandLine cmd = getBaseCommandLine(url);
		// 然后构造出所每个命令行所独立的参数
		cmd.addArgument("--dir=" + tempFile.getAbsolutePath());
//		cmd.addArgument("--dir=" + tempFile.getAbsolutePath());
		cmd.addArgument("--file=decorate.json");
		cmd.addArgument("--appdata=" + GUtilRunTools.getTools().getGlueDataDir());
		return cmd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GutilResult createSuccessResult(int exitcode) {
		m_GutilResult = new GutilResult<File, File[], File[], GDocument>(
				tempFile);
		
		String[] sh3fsuffix = {"sh3f"};
		Collection<File> sh3fFiles = FileUtils.listFiles(tempFile, sh3fsuffix, true);
		Collection<File> allFiles = FileUtils.listFiles(tempFile, null, true);
////		String[] jsonsuffix = { "json" };
//		String[] decoratesuffix = { "json" };
//		String[] jpgsuffix = { "jpg", "png" };
////		Collection<File> jsonfiles = FileUtils.listFiles(tempfile, jsonsuffix,
////				true);
//		Collection<File> decoratefiles = FileUtils.listFiles(tempfile, decoratesuffix,
//				true);
//		Collection<File> imgfiles = FileUtils.listFiles(tempfile, jpgsuffix,
//				true);
		// 获取 json文件
		m_GutilResult.setExitCode(exitcode);
//		File decorate = new File(GUtilRunTools.getTools().getGlueDataDir() +File.separator + "decorate.json");
		
		//删除temp目录中的模型数据，方便读取贴图数据。
		if (!sh3fFiles.isEmpty()) {
			java.util.Iterator<File> sh3fFileiterator = sh3fFiles.iterator();
			while (sh3fFileiterator.hasNext()) {
				File sh3ffile = (File) sh3fFileiterator.next();
				String id = sh3ffile.getName().substring(0, sh3ffile.getName().indexOf("."));
				System.out.println(id);
				java.util.Iterator<File> allFileiterator = allFiles.iterator();
				while (allFileiterator.hasNext()) {
					File file = (File) allFileiterator.next();
					if (file.getName().contains(id)) {
						file.delete();
					}
					
				}
			}
		}
		
		File decorate = new File(tempFile + File.separator + "decorate.json");
	
		
		String[] texturesuffix = {"json"};
		Collection<File> allJsonFiles = FileUtils.listFiles(tempFile, texturesuffix, true);
		ArrayList<File> jsonFiles = new ArrayList<File>(allJsonFiles);
		ArrayList<File> textureJson = new ArrayList<File>();
		
		if (jsonFiles != null && !jsonFiles.isEmpty()) {
			for (File file : jsonFiles) {
				if (!file.getName().contains("decorate")) {
					textureJson.add(file);
				}
			}
			
			File[] modelFiles = textureJson.toArray(new File[]{});
			m_GutilResult.setAdditionResourceFile(modelFiles);
		}
		if (decorate.exists()) {
			m_GutilResult.setMainResourceFile(decorate);
		}else {
			m_GutilResult.setMainResourceFile(null);
		}
//		if (decoratefiles != null && !decoratefiles.isEmpty()) {
//			File decoration = ((File[])decoratefiles.toArray(new File[]{}))[0];
//			System.out.println(decoration.getAbsolutePath());
//			m_GutilResult.setMainResourceFile(decoration);
//		}
		// 设置所附带的json文件的位置,如果有多个只设置第一个
//		if (json_files != null && !json_files.isEmpty()) {
//			File json = ((File[]) json_files.toArray(new File[]{}))[0];
//			m_GutilResult.setAdditionResourceFile(json);
//		}
		// 设置所附带的预览图的文件
//		if (imgfiles != null && !imgfiles.isEmpty()) {
//			File[] priviews = ((File[]) imgfiles.toArray(new File[]{}));
//			m_GutilResult.setAssetFile(priviews);
//		}
		return m_GutilResult;
	}

	@Override
	public GutilResult createFailedResult(Exception exception) {
		return m_GutilResult;
	}

}
