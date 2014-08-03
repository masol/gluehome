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
package org.spolo.glue.open;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.MappingURL;
import org.spolo.glue.RendercfgBean;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @date Jul 23, 2014
 */
public class UploadJob extends GWork {
	private String arg = null;
	private  HomeController m_HomeController = null;

	public UploadJob(String args, HomeController homecontroller) {
		this.m_HomeController = homecontroller;
		this.arg = args;
	}

	@Override
	public void doWork()  throws Exception{
		GsonBuilder gsonbuild = new GsonBuilder();
		Gson gson = gsonbuild.create();
		System.out.println("启动参数: " + arg);		
		try {
			arg = URLDecoder.decode(arg, "utf-8");
		} catch (UnsupportedEncodingException e) {			
			throw  new Exception("0");
		}		
		RendercfgBean render = gson.fromJson(arg, RendercfgBean.class);
		Home home = this.m_HomeController.getCurrentHomeCopy();
		home.getCamera().setName("\u5F53\u524D\u89C6\u89D2");
		File tempdir = GlueUtil.getSceneTempDir();
		String scenedir = render.hid;
		scenedir = tempdir + File.separator + scenedir;
		String zip = null;
		File scenedir_f = new File(scenedir);
		if(!scenedir_f.exists()){
			throw new Exception("4");
		}
		try {
			zip = syncpackScene(scenedir);
		} catch (ExecuteException e) {
			throw new Exception("1");
		} 
		String data = gson.toJson(render.data);		
		try {
			data = URLEncoder.encode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception("2");
		}		
		try {
			postRenderRes(zip, render.cookies, data);
		} catch (ExecuteException e) {
			throw new Exception("3");
		} 
		FileUtils.forceDelete(scenedir_f);
	}	
	public String syncpackScene(String src) throws ExecuteException, IOException {
		String zip = src + "\\render.zip";
		CommandLine cmdLine = new CommandLine("gutil//7za");
		cmdLine.addArgument("a");
		cmdLine.addArgument("-mcu=on");
		cmdLine.addArgument(zip);
		cmdLine.addArgument(src + "\\*");
		System.out.println(cmdLine);
		DefaultExecutor executor = new DefaultExecutor();
		executor.execute(cmdLine);		
		return zip;
	}

	public void postRenderRes(String scene_path, String cookies, String args) throws ExecuteException, IOException{		
		CommandLine cmdLine = new CommandLine("gutil//curl.exe");		
		cmdLine.addArgument("-b");
		cmdLine.addArgument(cookies);		
		cmdLine.addArgument("-Fdata="+args);		
		cmdLine.addArgument("-FfilePicName=@"+scene_path);
		cmdLine.addArgument(MappingURL.uploadjob);		
		System.out.println("上传场景: "+cmdLine);
		DefaultExecutor executor = new DefaultExecutor();
		executor.execute(cmdLine);

	}
	
}
