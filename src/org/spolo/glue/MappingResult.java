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
import java.util.HashMap;

/**
 * @date Oct 16, 2013
 */
public class MappingResult {
	private String id;
	private String path;
	private int type;
	private boolean isCached = true;
	public MappingResult(){
		mappingresultlist = new  HashMap<String ,MappingResult>();
	}
	public MappingResult(String id,String mainFile,int type){
	    this.id = id;
	    this.path = mainFile;
	    this.type = type;  
	}
	

	public boolean isCached() {
	    return isCached;
	}
	public void setCached(boolean isCached) {
	    this.isCached = isCached;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取此资源的绝对路径
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public File getFile() {
		return new File(this.path);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	private  HashMap<String ,MappingResult> mappingresultlist;
	
	public void addMappingResultList(HashMap<String ,MappingResult> map){
		this.mappingresultlist.putAll(map);
	}
	public MappingResult getResultByID(String id){
		return this.mappingresultlist.get(id);
	}
}
