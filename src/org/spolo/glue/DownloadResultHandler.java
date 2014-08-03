package org.spolo.glue;

import java.io.File;
import java.util.Collection;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.io.FileUtils;

public class DownloadResultHandler implements ExecuteResultHandler {
    private File tempFolder = null;
    private String[] suffix = null;
    private Collection<File> result = null;
    private DownloadCallback callback = null;
    private int type;
    
    public static final int Jiaju = 1;
    public static final int Huxing = 2;
    public static final int Tietu = 3;
    /**
     * 构造方法
     * 
     * @param tempFolder
     *            本次下载的内容 保存到的文件夹
     */
    public DownloadResultHandler(File tempFolder,int type) {
	this.tempFolder = tempFolder;
	setType(type);
    }
    /**
     * 构造方法
     * 
     * @param tempFolder
     *            本次下载的内容 保存到的文件夹
     * @param callback
     *            下载完成后的callback对象
     */
    public DownloadResultHandler(File tempFolder,int type, DownloadCallback callback) {
	this.tempFolder = tempFolder;
	setType(type);
	this.callback = callback;
    }

    public void setType(int type){
	this.type = type;
	switch(type){
		case Jiaju:{this.suffix = new String[]{"sh3f"};break;}
    		case Huxing:{this.suffix = new String[]{"sh3d"};break;}
    		case Tietu:{this.suffix = new String[]{"json"};break;}
	}
    }
    /**
     * 命令行调用完成后的回调
     */
    @Override
    public void onProcessComplete(int arg0) {
	this.result = FileUtils.listFiles(this.tempFolder, this.suffix, true);
	if (this.result.isEmpty()) {
	    this.tempFolder.delete();
	}else{
	    //遍历处理类型
	    switch(type){
		case Jiaju:{
		    break;
		}
		case Tietu:{
		    break;
		}
	    }
	}
	if (this.callback != null) {
	    this.callback.handleResult(this.result);
	}
    }

    @Override
    public void onProcessFailed(ExecuteException arg0) {
	tempFolder.delete();
	if (this.callback != null) {
	    this.callback.handleResult(null);
	}
    }

    public Collection<File> getResult() {
	return this.result;
    }

    public void setCallback(DownloadCallback callback) {
	this.callback = callback;
    }
}
