package com.eteks.sweethome3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.GUtilRunTools;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.UpdateInfo;

import com.google.gson.Gson;

public class CheckUpdates implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		File wgetFile = new File("gutil//wget.exe");
		CommandLine cmdLine = new CommandLine(wgetFile);
		cmdLine.addArgument("-N");
		cmdLine.addArgument("-P");
		cmdLine.addArgument(wgetFile.getParent());
		cmdLine.addArgument("www.xuanran001.com/update/glueversion.html"); // 添加cloudjson 下载链接
		System.out.println(cmdLine);
		DefaultExecutor executor = new DefaultExecutor();
		try {
			executor.execute(cmdLine);
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int updateChoice;

		String localJsonPath = "LICENSE/glueversion.html";
		String cloudJsonPath = "gutil/glueversion.html";

		File localJson = new File(localJsonPath);
		File cloudJson = new File(cloudJsonPath);

		if (cloudJson.exists()) {
			UpdateInfo localInfo = null;
			if (!localJson.exists()) {
				localInfo = new UpdateInfo();
				localInfo.setVersion("00");
			}else {
				localInfo = getUpdateInfo(localJson);
			}
			UpdateInfo cloudInfo = getUpdateInfo(cloudJson);
			
			if (isUpdateNeeded(localInfo, cloudInfo)) {
				updateChoice = showUpdateMessage(cloudInfo.isForce_update());
				if (updateChoice == 0) {
					CommandLine updateCmd = new CommandLine("update.exe");
					DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
					ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
					DefaultExecutor updateExecutor = new DefaultExecutor();
					executor.setExitValue(1);
					executor.setWatchdog(watchdog);
					try {
						System.out.println("exec -- " + updateCmd);
						updateExecutor.execute(updateCmd, resultHandler);
					} catch (ExecuteException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}else if (updateChoice == 2) {
					copyFile(cloudJsonPath, localJsonPath);
					cloudJson.delete();
				}
			}
		}
	}

	public UpdateInfo getUpdateInfo(File infojson) {
		System.out.println(infojson.getAbsolutePath());
		UpdateInfo updateInfo = null;
		try {
			String content = FileUtils.readFileToString(infojson, "utf-8");
			Gson gson = new Gson();
			updateInfo = gson.fromJson(content, UpdateInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updateInfo;
	}

	public boolean isUpdateNeeded(UpdateInfo localInfo, UpdateInfo cloudInfo) {

		boolean needUpdate = false;
		long oldVersion = Long.valueOf(localInfo.getVersion()).longValue();
		long newVersion = Long.valueOf(cloudInfo.getVersion()).longValue();
		if (oldVersion < newVersion) {
			needUpdate = true;
			System.out.println("newer");
		} else {
			needUpdate = false;
			System.out.println("elder");
		}
		return needUpdate;
	}

	public int showUpdateMessage(boolean forceUpdate) {
		if (forceUpdate) {
//			return JOptionPane
//					.showConfirmDialog(
//							null,
//							"\u63D0\u793A\uFF1A\u53D1\u73B0\u8F6F\u4EF6\u5B58\u5728\u66F4\u65B0\uFF0C\u662F\u5426\u66F4\u65B0\uFF1F",
//							"\u901A\u77E5", JOptionPane.OK_OPTION);
			return JOptionPane
					.showOptionDialog(
							null,
							"\u63D0\u793A\uFF1A\u53D1\u73B0\u8F6F\u4EF6\u5B58\u5728\u66F4\u65B0\uFF0C\u662F\u5426\u66F4\u65B0\uFF1F",
							"\u901A\u77E5", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null,
							new String[] { "\u66F4\u65B0", "\u53D6\u6D88","\u4EE5\u540E\u518D\u63D0\u793A" }, null);
		} else {
			return JOptionPane
					.showOptionDialog(
							null,
							"\u63D0\u793A\uFF1A\u53D1\u73B0\u8F6F\u4EF6\u5B58\u5728\u66F4\u65B0\uFF0C\u662F\u5426\u66F4\u65B0\uFF1F",
							"\u901A\u77E5", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null,
							new String[] { "\u66F4\u65B0", "\u53D6\u6D88","\u4EE5\u540E\u518D\u63D0\u793A" }, null);
		}
	}
	
	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public void copyFile(String oldPath, String newPath) { 
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   System.out.println(bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           System.out.println("复制单个文件操作出错"); 
           e.printStackTrace(); 

       } 

   } 

}
