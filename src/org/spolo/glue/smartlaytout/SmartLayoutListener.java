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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.spolo.glue.GUtil;
import org.spolo.glue.GUtilRunTools;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.GutilResult;
import org.spolo.glue.RoomInformation;

import com.eteks.sweethome3d.io.FileUserPreferences;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.TexturesCatalog;
import com.eteks.sweethome3d.model.TexturesCategory;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.ImportedTextureWizardController;
import com.eteks.sweethome3d.viewcontroller.RoomController;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

/**
 * @date Oct 22, 2013
 */
public class SmartLayoutListener implements ActionListener {
	private RoomController m_RoomController;
	private Logger log = Logger.getLogger("SmartLayoutListener-log");
	private final Home home;
	private final UserPreferences preferences;
	private final ContentManager contentManager;

	public SmartLayoutListener(Home h, UserPreferences p, ContentManager c) {
		this.home = h;
		this.preferences = p;
		this.contentManager = c;
	}

	public void setRoomController(RoomController roomcontroller) {
		this.m_RoomController = roomcontroller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean visible = m_RoomController.getAreaVisible();
		log.log(Level.INFO, "is visible : " + visible);
//		this.preferences.getTexturesCatalog().getCategories().get(0)
//				.getTextures().get(0).getWidth();
		addHomeStyle();
	}



	/**
	 * 添加自动摆放入口
	 */
	public void addHomeStyle() {
		File resourceFile = null;
		File[] textureFiles = null;
		try {
			String decoratFilePath = GUtilRunTools.getTools().getGlueDataDir().getAbsoluteFile()+ File.separator + "decorate.json";
			try {
				SourceObjectUtil sourceobj = SourceObjectUtil.getInstance(contentManager, preferences);
				sourceobj.writeSourceInfo(home, decoratFilePath);
			} catch (Exception e) {
				log.log(Level.INFO, e.getMessage());
				log.log(Level.INFO, "导出装修模板失败！");
			}
			
			
			File decorateFile = new File(decoratFilePath);
			long t1 = decorateFile.lastModified();
			GutilResult<File, File[], File[], RoomInformation> result = GUtil
					.execute("OpenDecorationLib");
			if (result.getMainResourceFile() != null) {
				resourceFile = result.getMainResourceFile();
				long t2 = resourceFile.lastModified();
				if (t1 == t2) {
					log.log(Level.INFO, "装修模板文件未修改！");
					resourceFile = null;
					return;
				}
			}else {
				log.log(Level.INFO, "装修模板主文件丢失！");
				return;
			}
			
			textureFiles = result.getAdditionResourceFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SourceObjectUtil sourceobj = SourceObjectUtil.getInstance(
				contentManager, preferences);
		ImportedTextureWizardController importTexture = new ImportedTextureWizardController("decorate",this.preferences, 
		        m_RoomController.viewFactory, this.contentManager);
		LinkedList<SourceObject> sourceobjlist = null;
//		try {
//			FileUtils.copyFile(resourceFile, new File(GUtilRunTools.getTools().getGlueDataDir() + File.separator + "decorate.json"));
		if (textureFiles != null) {
			for (int i = 0; i < textureFiles.length; i++) {
				System.out.println(textureFiles[i].getAbsolutePath());
				importTexture.importCloudTexture(textureFiles[i]);
			}
		}
//		
//		for (HomePieceOfFurniture furniture : home.getFurniture()) {
//			if (furniture.isAutoAdded()) {
////				home.deletePieceOfFurniture(furniture);
//			}
//		}
			
		sourceobjlist = sourceobj
					.syncreadSourceInfo(resourceFile.getAbsolutePath());
			
				
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		SmartLayout sl = new SmartLayout(this.home, this.preferences,
				this.contentManager);
		sl.setRoomController(this.m_RoomController);
		this.m_RoomController.smartLayout(sourceobjlist, sl);
		((FileUserPreferences) preferences).updateFurnitureDefaultCatalog();
	}
}
