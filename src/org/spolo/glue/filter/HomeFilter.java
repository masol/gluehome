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
package org.spolo.glue.filter;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.model.Camera;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.viewcontroller.RoomController;
import com.eteks.sweethome3d.viewcontroller.RoomController.WallSide;

/**
 * @date Jul 30, 2013
 */
public class HomeFilter {
	private static HomeFilter m_HomeFilter;

	private HomeFilter() {
	}

	public static HomeFilter getinstance() {
		if (m_HomeFilter == null) {
			m_HomeFilter = new HomeFilter();
		}
		return m_HomeFilter;
	}

	/**
	 * 
	 * @param home
	 * @return
	 */
	public Home filterFurnitureID(Home home) {
		GRequest<Home> hreq = new GRequest<Home>(home);
		GResponse<Home> hres = new GResponse<Home>(home);
		ExportCloudHomeModelFilter mf = new ExportCloudHomeModelFilter();
		mf.doFilter(hreq, hres, null);
		return hres.getResponse();
	}

	public String resetFurnitureID(File file) {
		// 根据文件名重新设置户型id
		String name = file.getName();
		String id = name.substring(0, name.indexOf("."));
		return id;
	}

	public Home GroupHomeModel(Home home) {
		GRequest hreq = new GRequest(home);
		GResponse hres = new GResponse(home);
		CloudRenderHomeFilter mf = new CloudRenderHomeFilter();
		GroupFurnitureFilter mf2 = new GroupFurnitureFilter();
		// mf.doFilter(hreq, hres, null);
		GFilterChain gfc = new GFilterChain();
		gfc.addFilter(mf2);
		gfc.addFilter(mf);
		gfc.doFilter(hreq, hres, null);
		return (Home) hres.getResponse();
	}

	public CheckResult checkFurnitureRenderInfo(Home home,RoomController rc) {
		List<HomePieceOfFurniture> furnitures = home.getFurniture();
		StringBuilder sb = new StringBuilder();
		CheckResult r = new CheckResult();
		sb.append("\u5B58\u5728\u4EE5\u4E0B\u5BB6\u5177\u4F1A\u5BFC\u81F4\u6700\u7EC8\u6548\u679C\u56FE\u4E0E\u60A8\u5F53\u524D\u770B\u5230\u7684\u753B\u9762\u4E0D\u7B26");
		sb.append("! \u786E\u8BA4\u662F\u5426\u8FDB\u884C\u6E32\u67D3 ?");
		sb.append("\n");
		// String homeid = home.getHomeid();
		// System.out.println("home id: " + homeid);
		// if (homeid == null || homeid.compareTo("") == 0) {
		// 	sb.append("\u539F\u56E0: \t");
		// 	sb.append("\u5F53\u524D\u573A\u666F\u6CA1\u6709\u623F\u4F53");
		// 	sb.append("\n");
		// 	r.isCorrect = false;
		// }
		for (HomePieceOfFurniture furniture : furnitures) {
			String id = furniture.getCatalogId();
			String name = furniture.getName();
			if (id == null || id.compareTo("") == 0) {
				sb.append("\u539F\u56E0: \t");
				sb.append(name + ": \t" + "\u6CA1\u6709 ID");
				sb.append("\n");
				r.isCorrect = false;
			}
			if (name.contains("组合 -")) {
				sb.append("\u539F\u56E0: \t");
				sb.append(name
						+ ": \t"
						+ "\u5B58\u5728\u6253\u8FC7\u7EC4\u7684\u5BB6\u5177,\u8BF7\u5148\u89E3\u7EC4,\u5426\u5219\u65E0\u6CD5\u6B63\u5E38\u6E32\u67D3\u51FA\u6240\u770B\u5230\u7684\u573A\u666F");
				sb.append("\n");
				r.isCorrect = false;
			}
		}
		
		List<Room> renderRooms = new ArrayList<Room>();
		Room  tempRoom = null;
		Camera currCamera = home.getCamera();
		tempRoom = cameraInRoom(home.getRooms(), currCamera);
		renderRooms.add(tempRoom);
 		for (Camera camera : home.getStoredCameras()) {
 			tempRoom = cameraInRoom(home.getRooms(), camera);
 			renderRooms.add(tempRoom);
		}
		
//		int roomFlag = 0;
//		int ceilingFlag = 0;
//		int wallFlag = 0;
		
//		if (renderRooms.size() > 0) {
//			for (Room room : renderRooms) {
//				if (room.getFloorTexture() == null) {
//					roomFlag++;
//				}
//				if (room.getCeilingTexture() == null) {
//					ceilingFlag++;
//				}
//			}
//			List<WallSide> wallSides = rc.getRoomsWallSides(renderRooms, null);
//			for (WallSide wallSide : wallSides) {
//				if (wallSide.getSide() == 0 && wallSide.getWall().getLeftSideTexture() ==null) {
//					wallFlag++;
//				}
//				if (wallSide.getSide() == 1 && wallSide.getWall().getRightSideTexture() == null) {
//					wallFlag++;
//				}
//			}
				
//		}
		
//		if (roomFlag != 0) {
//			sb.append("\u539F\u56E0: \t");
//			sb.append("\u89C6\u89D2\u6240\u5728\u7684\u623F\u95F4\u6CA1\u6709\u94FA\u8BBE\u5730\u677F");
//			sb.append("\n");
//			r.isCorrect = false;
//		}
//		if (ceilingFlag != 0) {
//			sb.append("\u539F\u56E0: \t");
//			sb.append("\u89C6\u89D2\u6240\u5728\u7684\u623F\u95F4\u6CA1\u6709\u94FA\u8BBE\u5929\u82B1\u677F");
//			sb.append("\n");
//			r.isCorrect = false;
//		}
//		if (wallFlag != 0) {
//			sb.append("\u539F\u56E0: \t");
//			sb.append("\u89C6\u89D2\u6240\u5728\u7684\u623F\u95F4\u6CA1\u6709\u94FA\u8BBE\u58C1\u7EB8");
//			sb.append("\n");
//			r.isCorrect = false;
//		}
		
		if (!r.isCorrect) {
			r.info = sb.toString();
		}
		return r;
	}
	
	/**
	 * 判断camera所在的room
	 * @param rooms home 中所有的Room
	 * @param camera 要判断的视角
	 * @return
	 */
	public Room cameraInRoom(List<Room> rooms, Camera camera) {
		float cameraX = camera.getX();
		float cameraY = camera.getY();
		float cameraZ = camera.getZ();
		float cameraToRoom = cameraZ;
		Room tempRoom = null;
		for (Room room : rooms) {
			Shape roomShape = room.getShape();
			if (room.getLevel() != null) {
				if (roomShape.contains(cameraX, cameraY) 
						&& cameraZ > room.getLevel().getElevation() 
						&& cameraToRoom >= cameraZ-room.getLevel().getElevation()) {
					cameraToRoom = cameraZ - room.getLevel().getElevation();
					tempRoom = room;
				}
			}else {
				if (roomShape.contains(cameraX, cameraY)) {
					tempRoom = room;
				}
			}
		}
		return tempRoom;
	}

	public Home resetModelID(Home home) {
		// 过滤掉Home中所有模型的id
		Home temp = home.clone();
		List<HomePieceOfFurniture> furnitures = temp.getFurniture();
		for (HomePieceOfFurniture furniture : furnitures) {
			String old = furniture.getCatalogId();
			if (old != null && old.contains("apartment")) {
				old = old.replace("apartment_", "");
				furniture.setCatalogId(old);
				System.out.println("[debug]: new id> " + old);
			}
		}
		return temp;
	}

	public class CheckResult {
		public String info = null;
		public boolean isCorrect = true;
	}
	
	
}
