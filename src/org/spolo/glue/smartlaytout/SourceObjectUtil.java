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
package org.spolo.glue.smartlaytout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.JsonUtil;
import org.spolo.glue.MappingResult;
import org.spolo.glue.TextureBean;
import org.spolo.glue.smartlaytout.LayoutFeature.Feature;
import org.spolo.glue.smartlaytout.sourceimpl.DefaultSourceObject;

import com.eteks.sweethome3d.io.DefaultFurnitureCatalog;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.TexturesCatalog;
import com.eteks.sweethome3d.model.TexturesCategory;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.tools.TemporaryURLContent;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;
import com.google.gson.Gson;

/**
 * 一个读取和生成目标物信息的工具类
 * 
 * @date Oct 11, 2013
 */
public class SourceObjectUtil {
	private Logger log = Logger.getLogger("SourceObjectUtil-log");
	private ContentManager m_contentmanager;
	private UserPreferences m_preferences;
	private static SourceObjectUtil m_SourceObjectUtil;

	private SourceObjectUtil(ContentManager contentmanager,
			UserPreferences preferences) {
		m_contentmanager = contentmanager;
		this.m_preferences = preferences;
	}

	public static SourceObjectUtil getInstance(ContentManager contentmanager,
			UserPreferences preferences) {
		if (m_SourceObjectUtil != null) {
			return m_SourceObjectUtil;
		} else {
			m_SourceObjectUtil = new SourceObjectUtil(contentmanager,
					preferences);
			return m_SourceObjectUtil;
		}

	}

	/**
	 * 
	 * 读取下载到本地的json户型模板信息 同步调用
	 * 
	 * @return
	 * 
	 */
	public LinkedList<SourceObject> syncreadSourceInfo(String source) {
		GDocument gobj = readJsonFile(source);
		// TODO 检查当前文件中所需要文件是否存在
		// 检查的过程可能是异步的,也可能是同步的
		checkResouce(gobj);
		// // 这里可以直接通过gson来构造处对象的,但是在不明确之前还是手动把
		return paserGdocument(gobj);
	}

	/**
	 * 解析gdocment对象
	 * 
	 * @param gobj
	 * @return
	 */
	public LinkedList<SourceObject> paserGdocument(GDocument gobj) {
		ArrayList<GContentObject> contents = gobj.getGconContentObject();
		LinkedList<SourceObject> sos = new LinkedList<SourceObject>();

		for (GContentObject c : contents) {
			if (c != null && c.isAbletouse()) {
				String category_str = c.getCategory();
				String feature_str = c.getFeature();
				int type_str = c.getType();
				log.log(Level.INFO, category_str);
				log.log(Level.INFO, feature_str);
				log.log(Level.INFO, type_str + "");
				GCategory category = new GCategory(
						GCategoryEnum.formString(category_str));
				LayoutFeature layoutfeature = new LayoutFeature(
						Feature.formString(feature_str));
				SourceObject so = new DefaultSourceObject(category,
						layoutfeature);
				if (type_str == GlobalParameters.SOURCEOBJ_TYPE_FURNITURE) {
					DefaultFurnitureCatalog dfc = new DefaultFurnitureCatalog();
					File file = c.getSourceFile();
					if (file.exists()) {
						dfc.autoimportfuniture(file);
						CatalogPieceOfFurniture furniture = dfc.funiture.get(0);
						furniture.setId(c.getId());
						log.log(Level.INFO, furniture.getCreator());
						so.setHomePieceOfFurniture(furniture);
					}
				} else if (type_str == GlobalParameters.SOURCEOBJ_TYPE_TEXTURE) {
					File josnfile = c.getSourceFile();
					HomeTexture texture = readHomeTextureFromJson(josnfile,
							m_contentmanager);
					if (category.getName() == "GHome") {
						so.setHomeTexture(texture);
					} else if (category.getName() == "GFloor") {
						so.setFloorTexture(texture);
					} else if (category.getName() == "GCeiling") {
						so.setCeilingTexture(texture);
					}
				}
				sos.add(so);
			}
		}
		return sos;
	}

	public GDocument readJsonFile(String source) {
		File jsonfile = new File(source);
		GDocument gobj = null;
		if (jsonfile.exists()) {
			try {
				String content = FileUtils.readFileToString(jsonfile, "utf-8");
				// log.log(Level.INFO, content);
				Gson gson = new Gson();
				gobj = gson.fromJson(content, GDocument.class);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return gobj;
	}

	public void checkResouce(GDocument gdoc) {
		ArrayList<GContentObject> contents = gdoc.getGconContentObject();
		MappingResult results = GlueUtil.getFurnitureByid(contents);
		for (GContentObject gcobj : contents) {
			String id = gcobj.getId();
			MappingResult furnitureobj = results.getResultByID(id);
			File sourcefile = furnitureobj.getFile();
			if (sourcefile.exists()) {
				// log.log(Level.INFO,
				// "本地存在缓存文件:  " + sourcefile.getAbsolutePath());
				gcobj.setSourceFile(sourcefile);
				gcobj.setAbletouse(true);
				gcobj.setIsCache(furnitureobj.isCached());
			} else {
				// TODO 如果此文件不在则说明下载说明失败,需要处理
				// 首先这个模型是不可用的,
				gcobj.setAbletouse(false);
			}
		}

	}

//	public void main(String[] args) {
//		GDocument gobj = SourceObjectUtil.getInstance(null, null).readJsonFile(
//				"test.json");
//		log.log(Level.INFO, gobj.getGconContentObject().size() + "");
//		SourceObjectUtil.getInstance(null, null).paserGdocument(gobj);
//	}

	/**
	 * 指定json路径通过json文件中的信息产生HomeTexture对象
	 * 
	 * @param path
	 *            json文件的绝对路径
	 * @param contentmanager
	 * @return
	 */
	public HomeTexture readHomeTextureFromJson(File jsonfile,
			ContentManager contentmanager) {
		// TODO 读取Json文件中的内容,获取png图片,以及尺寸信息
		TextureBean json = null;
		json = JsonUtil.getJsonFile(jsonfile);
		// for (String key : json.keySet()) {
		// imagename = key;
		// imagefile = new File(jsonfile.getParent() + File.separator
		// + imagename);
		// }
		// HashMap<String, String> jsoninfo = json.get(imagename);
		String id = jsonfile.getName().toString().replace(".json", "");
		String imagepath = null;
		String tempPath = jsonfile.getParent() + File.separator + id + ".jpg";
		File imageFile = new File(tempPath);
		if (imageFile.exists()) {
			imagepath = tempPath;
		}else {
			tempPath = jsonfile.getParent() + File.separator + id + ".png";
			imagepath = tempPath;

		}
		File imagefile = new File(imagepath);
//		File imagefile = new File(jsonfile.getAbsoluteFile().getParent()
//				+ File.separator + json.getTexturefile());
		float length = Float.valueOf(json.getLength());
		float width = Float.valueOf(json.getWidth());
		Content imageContent = null;
		HomeTexture hometexture = null;
		String textureName = imagefile.getName().substring(0,
				imagefile.getName().indexOf("."));
		try {
			imageContent = TemporaryURLContent
					.copyToTemporaryURLContent(contentmanager
							.getContent(imagefile.getAbsolutePath()));
			CatalogTexture ca = new CatalogTexture("test-id", textureName,
					imageContent, width, length, "test");
			hometexture = new HomeTexture(ca);
		} catch (RecorderException ex) {
		} catch (IOException ex) {
		}
		return hometexture;
	}

	public HomeTexture readHomTextureFromPREF(UserPreferences preferences,
			String id, File preffile) {
		HomeTexture hometexture = null;
		TexturesCatalog texturescatalog = preferences.getTexturesCatalog();
		List<TexturesCategory> categories = texturescatalog.getCategories();
		for (int i = 0; i < categories.size(); i++) {
			TexturesCategory category = categories.get(i);
			for (int z = 0; z < category.getTexturesCount(); z++) {
				CatalogTexture categorytexture = category.getTexture(z);
				String textureid = categorytexture.getName();
				if (textureid.equals(id)) {
					hometexture = new HomeTexture(categorytexture);
					// return hometexture;
				}
			}
		}
		return hometexture;
	}

	/**
	 * 将选中的一些模型信息以json的形式输出到指定的文件中
	 * 
	 * @param path
	 */
	public void writeSourceInfo(Home home, String path) throws Exception {
		GDocument gdoc = new GDocument();
		List<HomePieceOfFurniture> furnitures = home.getFurniture();
		List<Room> rooms = home.getRooms();
		Collection<Wall> allWalls = home.getWalls();
		List<Wall> walls = new ArrayList<Wall>(allWalls);
		ArrayList<GContentObject> gconContentObject = new ArrayList<GContentObject>();
		File textureDir = GlueUtil.getTextureDir();
		HashMap<String, String> temp = new HashMap<String, String>();
		for (HomePieceOfFurniture hpof : furnitures) {
			String id = hpof.getCatalogId().replace("apartment_", "");
			String ca = hpof.getCludeCategory();
			if (temp.get(id + ca) != null) {
				continue;
			} else {
				GContentObject gco = new GContentObject();

				String fe = hpof.getFeature();
				String ty = hpof.getSelfType();
				// String id = hpof.getCatalogId().replace("apartment_", "");
				String type = hpof.getType();
				if (checkData(id, ca, fe, ty, type)) {
					
//				}
//				if (ca != null && !ca.equals("none") && fe != null
//						&& !fe.equals("none") && ty != null
//						&& !ty.equals("none") && id != null && type != null
//						&& !type.equals("none")) {
					int ti = (int) Float.parseFloat(type);
					gco.setId(id);
					gco.setCategory(ca);
					gco.setFeature(fe);
					gco.setType(ti);
					gco.setSelftype(ty);
					gconContentObject.add(gco);
					temp.put(id+ca, id);
				} else {
					log.log(Level.INFO, "模型" + hpof.getName() + "--" + id
							+ "没有自动装修信息！");
					continue;
				}
			}
		}
		for (Wall wall : walls) {
			if (wall.getLeftSideTexture() != null) {
				GContentObject gco = new GContentObject();
				String leftSideTextureID = wall.getLeftSideTexture().getName();
				String ca = "GHome";
				if (temp.get(leftSideTextureID + ca) != null) {
					continue;
				} else {
					File leftTextureJsonFile = new File(textureDir
							+ File.separator + leftSideTextureID + ".json");
					if (!leftTextureJsonFile.exists()) {
						log.log(Level.INFO, "本地没有贴图信息！");
						continue;
					}
					TextureBean leftTexturejson = null;
					leftTexturejson = (TextureBean) JsonUtil.readJson(
							leftTextureJsonFile, TextureBean.class);

					String fe = leftTexturejson.getFeature();
					String ty = leftTexturejson.getSelftype();
					String type = leftTexturejson.getType();
					if (checkData(leftSideTextureID, ca, fe, ty, type)) {
						
//					}
//					if (ca != null && !ca.equals("none") && fe != null
//							&& !fe.equals("none") && ty != null
//							&& !ty.equals("none") && leftSideTextureID != null
//							&& type != null && !type.equals("none")) {
						int ti = (int) Float.parseFloat(type);
						gco.setId(leftSideTextureID);
						gco.setFeature(fe);
						gco.setSelftype(ty);
						gco.setType(ti);
						gco.setCategory(ca);
						gconContentObject.add(gco);
						temp.put(leftSideTextureID + ca, leftSideTextureID);
					} else {
						log.log(Level.INFO, "壁纸贴图" + leftSideTextureID
								+ "没有自动装修信息！");
						continue;
					}
				}
			}
			if (wall.getRightSideTexture() != null) {
				GContentObject gco = new GContentObject();
				String rightSideTextureID = wall.getRightSideTexture().getName();
				String ca = "GHome";
				if (temp.get(rightSideTextureID + ca) != null) {
					continue;
				} else {
					File rightTextureJsonFile = new File(textureDir
							+ File.separator + rightSideTextureID + ".json");
					if (!rightTextureJsonFile.exists()) {
						log.log(Level.INFO, "本地没有贴图信息！");
						continue;
					}
					TextureBean rightTexturejson = null;
					rightTexturejson = (TextureBean) JsonUtil.readJson(
							rightTextureJsonFile, TextureBean.class);

					String fe = rightTexturejson.getFeature();
					String ty = rightTexturejson.getSelftype();
					String type = rightTexturejson.getType();
					if (checkData(rightSideTextureID, ca, fe, ty, type)) {
						
//					}
//					if (ca != null && !ca.equals("none") && fe != null
//							&& !fe.equals("none") && ty != null
//							&& !ty.equals("none") && rightSideTextureID != null
//							&& type != null && !type.equals("none")) {
						int ti = (int) Float.parseFloat(type);
						gco.setId(rightSideTextureID);
						gco.setFeature(fe);
						gco.setSelftype(ty);
						gco.setType(ti);
						gco.setCategory(ca);
						gconContentObject.add(gco);
						temp.put(rightSideTextureID + ca, rightSideTextureID);
					} else {
						log.log(Level.INFO, "壁纸贴图" + rightSideTextureID
								+ "没有自动装修信息！");
						continue;
					}
				}
			}
		}
		for (Room room : rooms) {
			if (room.getCeilingTexture() != null) {
				GContentObject gco = new GContentObject();
				String ceilingTextureID = room.getCeilingTexture().getName();
				String ca = "GCeiling";
				if (temp.get(ceilingTextureID + ca) != null) {
					continue;
				} else {
					File ceilingTextureJsonFile = new File(textureDir
							+ File.separator + ceilingTextureID + ".json");
					if (!ceilingTextureJsonFile.exists()) {
						log.log(Level.INFO, "本地没有贴图信息！");
						continue;
					}
					TextureBean ceilingTexturejson = null;
					ceilingTexturejson = (TextureBean) JsonUtil.readJson(
							ceilingTextureJsonFile, TextureBean.class);
					String fe = ceilingTexturejson.getFeature();
					String ty = ceilingTexturejson.getSelftype();
					String type = ceilingTexturejson.getType();
					if (checkData(ceilingTextureID, ca, fe, ty, type)) {
						
//					}
//					if (ca != null && !ca.equals("none") && fe != null
//							&& !fe.equals("none") && ty != null
//							&& !ty.equals("none") && ceilingTextureID != null && type != null
//							&& !type.equals("none")) {
						int ti = (int) Float.parseFloat(type);
						gco.setId(ceilingTextureID);
						gco.setFeature(fe);
						gco.setSelftype(ty);
						gco.setType(ti);
						gco.setCategory(ca);
						gconContentObject.add(gco);
						temp.put(ceilingTextureID + ca, ceilingTextureID);
					} else {
						log.log(Level.INFO, "地板贴图" + ceilingTextureID
								+ "没有自动装修信息！");
						continue;
					}
				}
			}
			if (room.getFloorTexture() != null) {
				GContentObject gco = new GContentObject();
				String floorTextureID = room.getFloorTexture().getName();
				String ca = "GFloor";
				if (temp.get(floorTextureID + ca) != null) {
					continue;
				} else {
					File floorTextureJsonFile = new File(textureDir
							+ File.separator + floorTextureID + ".json");
					if (!floorTextureJsonFile.exists()) {
						log.log(Level.INFO, "本地没有贴图信息！");
						continue;
					}
					TextureBean floorTexturejson = null;
					floorTexturejson = (TextureBean) JsonUtil.readJson(
							floorTextureJsonFile, TextureBean.class);
					String fe = floorTexturejson.getFeature();
					String ty = floorTexturejson.getSelftype();
					String type = floorTexturejson.getType();
					if (checkData(floorTextureID, ca, fe, ty, type)) {
						
//					}
//					if (ca != null && !ca.equals("none") && fe != null
//							&& !fe.equals("none") && ty != null
//							&& !ty.equals("none") && floorTextureID != null
//							&& type != null && !type.equals("none")) {
						int ti = (int) Float.parseFloat(type);
						gco.setId(floorTextureID);
						gco.setFeature(fe);
						gco.setSelftype(ty);
						gco.setType(ti);
						gco.setCategory(ca);
						gconContentObject.add(gco);
						temp.put(floorTextureID + ca, floorTextureID);
					} else {
						log.log(Level.INFO, "天花板贴图" + floorTextureID
								+ "没有自动装修信息！");
						continue;
					}
				}
			}
		}
		if (gconContentObject.size() > 0) {
			// gconContentObject = removeDuplicate(gconContentObject);

			gdoc.setGconContentObject(gconContentObject);
			File f = new File(path);
			JsonUtil.writeObjectToFile(gdoc, f);
		} else {
			log.log(Level.INFO, "户型中不存在可以自动装修的元素！");
			throw new Exception("no decoration element");
		}
	}
	
	public boolean checkData(String id, String ca, String fe, String ty,  String type) {
		if (id != null 
				&&ca != null && !ca.equals("none") && !ca.isEmpty() 
				&& fe != null && !fe.equals("none") && !fe.isEmpty()
				&& ty != null && !ty.equals("none") && !ty.isEmpty()
				&& type != null && !type.equals("none") && !type.isEmpty()) {
			return true;
		}else {
			return false;
		}
		
	}
}
