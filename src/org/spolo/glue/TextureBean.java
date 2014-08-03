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

import java.util.Hashtable;

public class TextureBean {

	private String resourceName;
	private String length;
	private String NewestVersion;
	private String width;
	private String CurrentVersion;
	private String introduction;
	private String keyInfo;
	private Hashtable<String, String>[] textureinfo;
	private String path;
	private String texturefile;
	private String[] preview;
	private String seatPath;
	private String category;
	private String selftype;
	private String type;
	private String feature;
	

	public String getId() {
		return this.texturefile.substring(0, this.texturefile.indexOf("."));
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String lenght) {
		this.length = lenght;
	}

	public String getNewestVersion() {
		return NewestVersion;
	}

	public void setNewestVersion(String newestVersion) {
		NewestVersion = newestVersion;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getCurrentVersion() {
		return CurrentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		CurrentVersion = currentVersion;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}

	public Hashtable<String, String>[] getTextureinfo() {
		return textureinfo;
	}

	public void setTextureinfo(Hashtable<String, String>[] textureinfo) {
		this.textureinfo = textureinfo;
	}

	public String getPath() {
		return path;
	}

	public String getSeatPath() {
		return seatPath;
	}

	public void setSeatPath(String seatPath) {
		this.seatPath = seatPath;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTexturefile() {
		return texturefile;
	}

	public void setTexturefile(String texturefile) {
		this.texturefile = texturefile;
	}

	public String[] getPreview() {
		return preview;
	}

	public void setPreview(String[] preview) {
		this.preview = preview;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSelftype() {
		return selftype;
	}

	public void setSelftype(String selftype) {
		this.selftype = selftype;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
