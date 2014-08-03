package org.spolo.glue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MappingURL {

	private static String roomlib="http://www.xuanran001.com/s/gh/roomlib/index.html";
	private static String localRoomLib = "http://www.xuanran001.com/s/gh/local/roomlib/index.html";
	private static String modellib = "http://www.xuanran001.com/s/gh/modellib/index.html";
	private static String materiallib = "http://www.xuanran001.com/s/gh/materiallib/index.html";
	private static String upload = "http://www.xuanran001.com/s/gh/job/upload.html";
	private static String rendering = "http://www.xuanran001.com/s/gh/job/rendering.html";
	private static String mypreviewlib = "http://www.xuanran001.com/s/gh/mypreviewlib/index.html";
	private static String decorationlib = "​http://www.xuanran001.com/s/gh/decorationlib/index.html";
	private static String decoratelist = "http://www.xuanran001.com/s/gh/decorationlib/lists.html";
	private static String uploadroom = "http://www.xuanran001.com/s/gh/uploadroomtemplate/index.html";
	private static String sroomlib = "http://www.xuanran001.com/s/gh/sroomlib/index.html";
	private static String createTender = "http://www.xuanran001.com/s/gh/createbid/index.html";
	private static String manageTender = "http://www.xuanran001.com/s/gh/mybidlib/index.html";
	public static String uploadjob = "http://www.xuanran001.com/userdata/job/create";
	public static String uploadjoburl = "http://www.xuanran001.com/s/gh/job/nupload.html";
	
	public static void loadURL() {
	    Properties prop = new Properties();
	    File properFile = new File("URLconfig.properties");
	    if (properFile.exists()) {

	        try {
	            prop.load(new FileInputStream("URLconfig.properties"));
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        //      System.out.println(prop.getProperty(roomlib));
	        if (prop.containsKey("roomlib")) {
	            roomlib = prop.getProperty("roomlib");
	        }
	        if (prop.containsKey("localRoomLib")) {
	            localRoomLib = prop.getProperty("localRoomLib");
	        }
	        if (prop.containsKey("modellib")) {
	            modellib = prop.getProperty("modellib");
	        }
	        if (prop.containsKey("materiallib")) {
	        	materiallib = prop.getProperty("materiallib");
	        }
	        if (prop.containsKey("upload")) {
	            upload = prop.getProperty("upload");
	        }
	        if (prop.containsKey("rendering")) {
	            rendering = prop.getProperty("rendering");
	        }
	        if (prop.containsKey("mypreviewlib")) {
	            mypreviewlib = prop.getProperty("mypreviewlib");
	        }
	        if (prop.containsKey("decorationlib")) {
	            decorationlib = prop.getProperty("decorationlib");
	        }
	        if (prop.containsKey("decoratelist")) {
	        	decoratelist = prop.getProperty("decoratelist");
	        }
	        if (prop.containsKey("uploadroom")) {
	        	uploadroom = prop.getProperty("uploadroom");
	        }
	        if (prop.containsKey("sroomlib")) {
	        	sroomlib = prop.getProperty("sroomlib");
	        }
	        if (prop.containsKey("createTender")) {
	        	createTender = prop.getProperty("createTender");
	        }
	        if (prop.containsKey("manageTender")) {
	        	manageTender = prop.getProperty("manageTender");
	        }
	    }
	}

	public static String getRoomlib() {
		return roomlib;
	}

	public static String getLocalRoomLib() {
		return localRoomLib;
	}

	public static String getModellib() {
		return modellib;
	}

	public static String getMateriallib() {
		return materiallib;
	}

	public static String getUpload() {
		return upload;
	}

	public static String getRendering() {
		return rendering;
	}

	public static String getMypreviewlib() {
		return mypreviewlib;
	}

	public static String getDecorationlib() {
		return decorationlib;
	}
	
	public static String getDecoratlist() {
		return decoratelist;
	}
	public static String getUploaRoomURL(){
		return uploadroom;
	}
	public static String getSroomlibURL(){
		return sroomlib;
	}
	public static String getCreateTenderURL(){
		return createTender;
	}
	public static String getManageTenderURL(){
		return manageTender;
	}
}


