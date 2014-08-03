package org.spolo.glue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;

import com.eteks.sweethome3d.tools.OperatingSystem;

public class GUtilRunTools {
	private static GUtilRunTools tools = null;
	private String GlueDataDir = null;
	private String SweetHomeData = null;
	// gutil用户验证文件名称
	public static final String AuthFile = "auth.xml";

	private File gutil = null;

	private GUtilRunTools() {
		// 获取配置的文件夹
		this.SweetHomeData = System.getProperty(
				"com.eteks.sweethome3d.applicationFolders", null);
		if (this.SweetHomeData == null) {
			try {
				this.SweetHomeData = (OperatingSystem
						.getDefaultApplicationFolder().getPath());
				this.GlueDataDir = this.SweetHomeData + File.separator
						+ "GlueData";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			}
		} else {
			this.GlueDataDir = this.SweetHomeData + File.separator + "GlueData";
		}
		String mainClassPath = System.getProperty("java.class.path");
		if (mainClassPath != null) {
			mainClassPath = mainClassPath.split(File.pathSeparator)[0];
			gutil = findGUtil(new File(mainClassPath));
			if (gutil == null) {
				gutil = new File("gutil//gutil.exe");
			}
		}
		System.out.println("[GutilPath]" + gutil.getPath());
	}

	/**
	 * 获取GlueUtil auth.xml
	 * 
	 * @return File
	 */
	public File getGlueAuthFile() {
		File dir = new File(this.GlueDataDir + File.separator + AuthFile)
				.getAbsoluteFile();
		return dir;
	}

	/**
	 * 获取GlueData存放目录
	 * 
	 * @return File
	 */
	public File getGlueDataDir() {
		File dir = new File(this.GlueDataDir).getAbsoluteFile();
		return dir;
	}

	/**
	 * 获取sweethomeData存放目录
	 * 
	 * @return File
	 */
	public File getSweetHomeDataDir() {
		File dir = new File(this.SweetHomeData).getAbsoluteFile();
		return dir;
	}

	/**
	 * 获取Glue session的目录
	 * 
	 * @return
	 */
	public File getGlueSessionDir(String userID) {
		File file = new File(this.GlueDataDir + File.separator + userID)
				.getAbsoluteFile();
		return file;
	}

	public File getGlueSessionFile(String userID, String relativePath) {
		File file = new File(this.GlueDataDir + File.separator + userID
				+ File.separator + relativePath).getAbsoluteFile();
		return file;
	}

	/**
	 * 递归查找gutil.exe
	 * 
	 * @param file
	 * @return
	 */
	private File findGUtil(File file) {
		File dir = new File(file.getAbsolutePath() + File.separator + "gutil");
		if (dir.exists()) {
			File gutil = new File(dir.getAbsolutePath() + File.separator
					+ "gutil.exe");
			if (gutil.exists()) {
				return gutil;
			}
		}
		File parentFile = file.getAbsoluteFile().getParentFile();
		if (parentFile == null) {
			return null;
		} else {
			return findGUtil(parentFile);
		}
	}

	public static GUtilRunTools getTools() {
		if (GUtilRunTools.tools == null) {
			GUtilRunTools.tools = new GUtilRunTools();
		}
		return tools;
	}

	/**
	 * 获取基础命令行
	 * 
	 * @param url
	 * @return
	 */
	private CommandLine getBaseCommand(String url) {
		CommandLine command = new CommandLine(gutil);
		command.addArgument("openweb");
		command.addArgument("--url=" + url);
		command.addArgument(" --maximized=true");
		return command;
	}

	/**
	 * 执行给定的命令
	 * 
	 * @param cmd
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public void run(GUtilCommand cmd) {
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = -1;
		try {
			exit_code = executor.execute(cmd.getCommandLine());
			cmd.onProcessComplete(exit_code);
		} catch (ExecuteException e) {
			cmd.onProcessFailed(e);
		} catch (IOException e) {
			cmd.onProcessFailed(new ExecuteException("Execution failed",
					exit_code, e));
		}
	}

	/**
	 * 下载家具模型的命令行
	 * 
	 * @param tempFolder
	 * @return
	 */
	private CommandLine getDownloadModelsCommand(File tempFolder) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/modellib/index.html");
		command.addArgument(" --fileformat=" + GlueUtil.SupportFileFormat);
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 上传渲染 （拍照） 的命令行
	 * 
	 * @param dir
	 *            指定的目录，该目录中的内容会打包成为zip上传到服务器用于渲染
	 * @return
	 */
	private CommandLine getUploadRenderInfoCommand(File dir) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/job/upload.html");
		command.addArgument(" --client=sweethome");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("dir", dir);
		command.addArgument(" --dir=${dir}");
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 下载户型命令行
	 * 
	 * @param tempFolder
	 * @return
	 */
	private CommandLine getDownloadRoomCommand(File tempFolder) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/modules/gutil/roomlib/index.html");
		command.addArgument(" --fileformat=" + GlueUtil.SupportFileFormat);
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 获取打开拍照中的项目的命令行
	 * 
	 * @return
	 */
	private CommandLine getOpenJobManagerCommand() {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/job/rendering.html");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 打开我的效果图命令行
	 * 
	 * @return
	 */
	private CommandLine getOpenPreviewlibCommand() {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/mypreviewlib/index.html");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 下载贴图的命令
	 * 
	 * @param tempFolder
	 *            下载到的目录
	 * @return
	 */
	private CommandLine getDownloadTextureCommand(File tempFolder) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/modules/materiallib/gutil_index.html");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --appdata=${appdata}");
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 打开用户中心的命令行
	 * 
	 * @return
	 */
	private CommandLine getOpenUserCenterCommand() {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/gluehome3d/user/index.html");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 获取通过id直接贴图的命令
	 * 
	 * @param tempFolder
	 *            指定下载到的目录
	 * @param id
	 *            指定的需要下载的id数组
	 * @return
	 */
	private CommandLine getDownloadTextureCommand(File tempFolder, String[] id) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/materiallib/index.html");
		// command.addArgument(" --fileformat=" + GlueUtil.SupportFileFormat);
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --appdata=${appdata}");
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.setSubstitutionMap(map);
		String allid = "";
		for (int index = 0; index < id.length - 1; index++) {
			allid = allid + id[index] + ",";
		}
		allid = allid + id[id.length - 1];
		command.addArgument(" --params=" + allid);
		return command;
	}

	/**
	 * 打开本地户型库的命令行
	 * 
	 * @param tempFolder
	 *            户型使用目录
	 * @return
	 */
	private CommandLine getOpenLocalRoomLibCommand(File tempFolder) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/local/roomlib/index.html");
		command.addArgument(" --fileformat=" + GlueUtil.SupportFileFormat);
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}

	/**
	 * 获取通过id直接下载模型的命令
	 * 
	 * @param tempFolder
	 *            指定下载到的目录
	 * @param id
	 *            指定的需要下载的id数组
	 * @return
	 */
	private CommandLine getDownloadModelsCommand(File tempFolder, String[] id) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/session_gutil.html?redirect=/modules/gutil/modellib/downloading.html");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --appdata=${appdata}");
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.setSubstitutionMap(map);
		String allid = "";
		for (int index = 0; index < id.length - 1; index++) {
			allid = allid + id[index] + ",";
		}
		allid = allid + id[id.length - 1];
		command.addArgument(" --params=" + allid);
		return command;
	}

	/**
	 * 同步调用gutil，下载家具模型功能
	 * 
	 * @return 返回文件集合，集合内的文件为sweethome导入模型所需要的
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int DownLoadModels(File tempFolder) throws ExecuteException,
			IOException {
		CommandLine command = this.getDownloadModelsCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 异步调用gutil，下载家具模型功能
	 * 
	 * @param callback
	 *            DownloadCallback实例，用来处理下载结果
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public void DownLoadModels(File tempFolder, ExecuteResultHandler handler)
			throws ExecuteException, IOException {
		CommandLine command = this.getDownloadModelsCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		executor.execute(command, handler);
	}

	/**
	 * 同步调用gutil，下载户型模型功能
	 * 
	 * @return 返回文件集合，集合内的文件为sweethome导入模型所需要的
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int DownLoadRooms(File tempFolder) throws ExecuteException,
			IOException {
		CommandLine command = this.getDownloadRoomCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 异步调用gutil，下载户型模型功能
	 * 
	 * @param callback
	 *            DownloadCallback实例，用来处理下载结果
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public void DownLoadRooms(File tempFolder, ExecuteResultHandler handler)
			throws ExecuteException, IOException {
		CommandLine command = this.getDownloadRoomCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		executor.execute(command, handler);
	}

	/**
	 * 同步调用gutil，上传渲染任务
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int uploadRenderInfo(File dir) throws ExecuteException, IOException {
		CommandLine command = this.getUploadRenderInfoCommand(dir);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 打开云渲染管理页面
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int openJobManager() throws ExecuteException, IOException {
		CommandLine command = this.getOpenJobManagerCommand();
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 打开我的效果图库页面
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int openMyPreviewlib() throws ExecuteException, IOException {
		CommandLine command = this.getOpenPreviewlibCommand();
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 异步下载贴图
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public void DownloadTexture(File tempFolder, ExecuteResultHandler handler)
			throws ExecuteException, IOException {
		CommandLine command = this.getDownloadTextureCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		executor.execute(command, handler);
	}

	/**
	 * 同步下载贴图
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int DownloadTexture(File tempFolder) throws ExecuteException,
			IOException {
		CommandLine command = this.getDownloadTextureCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 打开用户中心
	 * 
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int openUserCenter() throws ExecuteException, IOException {
		CommandLine command = this.getOpenUserCenterCommand();
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 同步通过id下载家具模型
	 * 
	 * @param tempFolder
	 *            下载目录
	 * @param id
	 *            字符串数组
	 * @return
	 * @throws ExecuteException
	 * @throws IOException
	 */
	public int DownLoadModels(File tempFolder, String[] id)
			throws ExecuteException, IOException {
		CommandLine command = this.getDownloadModelsCommand(tempFolder, id);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 同步通过id下载贴图
	 * 
	 * @param tempFolder
	 *            下载目录
	 * @param id
	 *            字符串数组
	 * @return
	 * @throws ExecuteException
	 * @throws IOException
	 */
	public int DownloadTexture(File tempFolder, String[] id)
			throws ExecuteException, IOException {
		CommandLine command = this.getDownloadTextureCommand(tempFolder, id);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 同步调用gutil，打开本地户型库
	 * 
	 * @return return code
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int openLocalRoomlib(File tempFolder) throws ExecuteException,
			IOException {
		CommandLine command = this.getOpenLocalRoomLibCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 同步调用gutil，打开本地装修模板库
	 * 
	 * @return return code
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public int openDecorationlib(File tempFolder) throws ExecuteException,
			IOException {
		CommandLine command = this.getOpenDecorationlibCommand(tempFolder);
		System.out.println("[INFO]" + command);
		DefaultExecutor executor = new DefaultExecutor();
		int exit_code = executor.execute(command);
		return exit_code;
	}

	/**
	 * 打开本地户型库的命令行
	 * 
	 * @param tempFolder
	 *            户型使用目录
	 * @return
	 */
	private CommandLine getOpenDecorationlibCommand(File tempFolder) {
		CommandLine command = getBaseCommand("http://www.xuanran001.com/s/gh/local/roomlib/index.html");
		command.addArgument(" --fileformat=json");
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("appdata", new File(GlueDataDir));
		map.put("tempFolder", tempFolder);
		command.addArgument(" --file=${tempFolder}");
		command.addArgument(" --dir=${tempFolder}");
		command.addArgument(" --appdata=${appdata}");
		command.setSubstitutionMap(map);
		return command;
	}
}
