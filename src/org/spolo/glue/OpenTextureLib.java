package org.spolo.glue;

import java.io.File;
import java.util.Collection;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;

public class OpenTextureLib extends GUtilCommand {

	private File tempFile = GlueUtil.createTempFolder();

	public OpenTextureLib(){
		super();
//		this.url = "http://www.xuanran001.com/s/gh/materiallib/index.html";
		this.url = MappingURL.getMateriallib();
	}

	@Override
	public CommandLine getCommandLine() {
		// TODO 获取CommandLine对象
		CommandLine cmd = getBaseCommandLine(url);
		cmd.addArgument("--dir=" + tempFile.getAbsolutePath());
		cmd.addArgument("--appdata=" + GUtilRunTools.getTools().getGlueDataDir());
		//TODO 添加命令行的其他参数。
		System.out.println(cmd);
		return cmd;
	}

	@Override
	public GutilResult createSuccessResult(int exitcode) throws Exception {
		m_GutilResult = new GutilResult<Collection<File>, Collection<File>, File[], TextureInfomation>(
				tempFile);
		String[] jsonsuffix = { "json" };
		String[] texturesuffix = { "jpg", "png" };
		Collection<File> texture_files = FileUtils.listFiles(tempFile, texturesuffix,
				true);
		Collection<File> json_files = FileUtils.listFiles(tempFile, jsonsuffix,
				true);
		// 设置sh3d主文件的位置,如果有多个,只设置第一个
		m_GutilResult.setExitCode(exitcode);
		if (texture_files != null && !texture_files.isEmpty()) {
			System.out.println("++++" + texture_files.size());
			Collection<File> textures = texture_files;
			m_GutilResult.setMainResourceFile(texture_files);
		}
		// 设置所附带的json文件的位置,如果有多个只设置第一个
		if (json_files != null && !json_files.isEmpty()) {
			Collection<File> json = json_files;
			m_GutilResult.setAdditionResourceFile(json);
		}
		// 设置所附带的预览图的文件
//		if (img_files != null && !img_files.isEmpty()) {
//			File[] priviews = ((File[]) img_files.toArray(new File[]{}));
//			m_GutilResult.setAssetFile(priviews);
//		}
		// 读取json文件,将json文件变为对象. TODO 如果后面肯定会附带一个json文件的话 就可以直接在父类里面操作
		// JsonUtil.readJson(json, GDocument.class);
		// m_GutilResult.setJsonObject(null);

		return m_GutilResult;

	}

	@Override
	public GutilResult createFailedResult(Exception exception) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
