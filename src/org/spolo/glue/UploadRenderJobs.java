package org.spolo.glue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;

public class UploadRenderJobs extends GUtilCommand {
	
	private File tempFile = GlueUtil.createTempFolder();
	
	public UploadRenderJobs(){
		super();
//		this.url = "http://www.xuanran001.com/s/gh/job/upload.html";
		this.url = MappingURL.getUpload();
	}

	@Override
	public CommandLine getCommandLine() {
		// TODO 获取CommandLine对象
		
		File src = new File(GUtilRunTools.getTools().getGlueDataDir().getAbsoluteFile()+ File.separator + "rederJson");
		try {
			FileUtils.copyDirectory(src, tempFile);
			FileUtils.deleteDirectory(src);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		CommandLine cmd = getBaseCommandLine(url);		
		cmd.addArgument("--dir=" + tempFile.getAbsolutePath());
		cmd.addArgument("--client=sweethome");
		 cmd.addArgument("--appdata=" + GUtilRunTools.getTools().getGlueDataDir());
		
		 System.out.println(cmd);
		//TODO 添加命令行的其他参数。
		return cmd;
	}

	@Override
	public GutilResult createSuccessResult(int exitcode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GutilResult createFailedResult(Exception exception) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
