package org.spolo.glue;

import java.io.File;
import java.util.Collection;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;

public class OpenFurnitureLib extends GUtilCommand {
	
	private File tempFile = GlueUtil.createTempFolder();
	
	public OpenFurnitureLib(){
		super();
//		this.url = "http://www.xuanran001.com/s/gh/modellib/index.html";
		this.url = MappingURL.getModellib();
	}

	@Override
	public CommandLine getCommandLine() {
		// TODO 获取CommandLine对象
		CommandLine cmd = getBaseCommandLine(url);
		//TODO 添加命令行的其他参数。
		cmd.addArgument("--dir=" + GUtilRunTools.getTools().getSweetHomeDataDir() + File.separator + "furniture");
		cmd.addArgument("--appdata=" + GUtilRunTools.getTools().getGlueDataDir());
		return cmd;
	}

	@Override
	public GutilResult createSuccessResult(int exitcode) throws Exception {
		m_GutilResult = new GutilResult<File[], File[], File[], FurnitureInfo>(
				tempfile);
		return m_GutilResult;
	}

	@Override
	public GutilResult createFailedResult(Exception exception) throws Exception {
		// TODO Auto-generated method stub
		return m_GutilResult;
	}

}
