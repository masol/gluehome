package org.spolo.glue;

import org.apache.commons.exec.CommandLine;

public class ManageRenderings extends GUtilCommand {
	
	public ManageRenderings(){
		super();
//		this.url = "http://www.xuanran001.com/s/gh/mypreviewlib/index.html";
		this.url = MappingURL.getMypreviewlib();
	}

	@Override
	public CommandLine getCommandLine() {
		// TODO 获取CommandLine对象
		CommandLine cmd = getBaseCommandLine(url);
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
