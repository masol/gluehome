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

package com.eteks.sweethome3d.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Catch {

	private String yangbanjianPath;
	private String zhanghaoPath;
	private String zhuangxiumobanPath;
	private final String appdataPath;

	private TexturesCatalog texturesCatalog;
	private FurnitureCatalog furnitureCatalog;

	public Catch(TexturesCatalog _texturesCatalog,
			FurnitureCatalog _furnitureCatalog) {
		this.appdataPath = System.getenv().get("APPDATA") + File.separator
				+ "eTeks" + File.separator + "Sweet Home 3D";
		this.texturesCatalog = _texturesCatalog;
		this.furnitureCatalog = _furnitureCatalog;
		this.yangbanjianPath = this.appdataPath + File.separator + "roomlib";
		this.zhanghaoPath = this.appdataPath + File.separator + "GlueData";
		this.zhuangxiumobanPath = this.appdataPath + File.separator
				+ "decorationlib";
	}

	/*
	 * 删除本地样板间缓存
	 */
	public void deleteYBJCatch() {
		File roomlib = new File(this.getYangbanjianPath());
		if (roomlib.exists()) {
			File[] rooms = roomlib.listFiles();
			for (File room : rooms) {
				room.delete();
			}
		}
	}

	/*
	 * 删除家具缓存
	 */
	public void deleteJJCatch() {
		for (FurnitureCategory fc : this.furnitureCatalog.getCategories()) {
			for (CatalogPieceOfFurniture piece : fc.getFurniture()) {
				this.furnitureCatalog.delete(piece);
			}
		}
		for (File f : new File(this.appdataPath + File.separator + "furniture")
				.listFiles()) {
			if (f.isDirectory()) {
				try {
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				f.delete();
			}

		}
	}

	/*
	 * 删除账户缓存
	 */
	public void deleteZHCatch() {
		File zhanghu = new File(this.getZhanghaoPath());
		if (zhanghu.exists()) {
			File[] fs = zhanghu.listFiles();
			for (File f : fs) {
				if (f.isDirectory() && !f.getName().equals("work")) {
					try {
						FileUtils.deleteDirectory(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (f.isFile()) {
					f.delete();
				}
			}
		}
	}

	/*
	 * 删除材质缓存
	 */
	public void deleteCZCatch() {
		for (TexturesCategory tc : this.texturesCatalog.getCategories()) {
			for (CatalogTexture texture : tc.getTextures()) {
				this.texturesCatalog.delete(texture);
			}
		}
	}

	/**
	 * 删除装修模版缓存
	 */
	public void deleteZXMBCatch() {
		File ZXMBDirectory = new File(this.zhuangxiumobanPath);
		if (ZXMBDirectory.exists()) {
			for (File f : ZXMBDirectory.listFiles()) {
				if (f.isDirectory()) {
					try {
						FileUtils.deleteDirectory(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					f.delete();
				}
			}
		}
	}

	public String getYangbanjianPath() {
		return yangbanjianPath;
	}

	public void setYangbanjianPath(String yangbanjianPath) {
		this.yangbanjianPath = yangbanjianPath;
	}

	public String getZhanghaoPath() {
		return zhanghaoPath;
	}

	public void setZhanghaoPath(String zhanghaoPath) {
		this.zhanghaoPath = zhanghaoPath;
	}

}
