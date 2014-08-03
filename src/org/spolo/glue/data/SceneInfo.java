package org.spolo.glue.data;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.eteks.sweethome3d.model.Camera;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.RoomController;
import com.eteks.sweethome3d.viewcontroller.RoomController.WallSide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SceneInfo {
  private JsonObject data    = null;
  private JsonObject scene   = null;
  private JsonArray  cameras = null;
  private JsonArray  models  = null;
  private JsonObject  sroom   = null;
  private JsonArray  materials = null;
  private JsonObject dataversion = null;
  private Home       home    = null;
  private JsonArray rooms = null;
  private JsonObject project = null;
  private HomeController controller = null;

  public SceneInfo(Home home, HomeController controller) {
    this.home = home;
    this.controller = controller;
    importHomeData(home, controller);
  }

  private void initdata() {
    this.data = new JsonObject();
    this.scene = new JsonObject();
    this.cameras = new JsonArray();
    this.models = new JsonArray();
    this.sroom = new JsonObject();
    this.materials = new JsonArray();
    this.dataversion = new JsonObject();
    this.rooms = new JsonArray();
    this.project = new JsonObject();
    this.data.add("scene", scene);
    this.data.add("cameras", cameras);
    this.data.add("models", models);
    this.data.add("sroom", sroom);
    this.data.add("dataversion", dataversion);
    this.data.add("materials", materials);
    this.data.add("rooms", rooms);
    this.data.add("project", project);
  }

  public void importHomeData(Home home, HomeController controller) {
    initdata();
    int i = 0;
    for (HomePieceOfFurniture nf : home.getFurniture()) {
		String id = nf.getCatalogId();
		// 如果id值中带apartment字样,就说明是需要过滤掉的
		if (id.contains("apartment")) {
			i++;
		}
    }
    if (i == 0) {
		dataversion.addProperty("dataversion", 2.0);
	}
    scene.addProperty("lengthunit", "cm");
    List<HomePieceOfFurniture> furnitures = home.getFurniture();
    for (HomePieceOfFurniture furniture : furnitures) {
      addModel(furniture);
    }
    List<Camera> cameraList = home.getStoredCameras();
    if (cameraList.size() > 0) {
    	addCamera(home.getCamera());
      for (Camera camera : cameraList) {
        addCamera(camera);
      }
    } else {
      addCamera(home.getCamera());
    }
    addSroom(home);
    addMaterial(home);
    addRooms(home, controller);
    addProject(home);
  }

  public void addCamera(Camera camera) {
    JsonObject cameraJson = new JsonObject();
    float x = camera.getX();
    float y = camera.getY();
    float z = camera.getZ();
    String name = camera.getName();
    if ((name == null) || (name.compareTo("null") == 0) || (name.compareTo("") == 0)) {
      name = "camera";
    }else{
      name = name.replace("\\", "_").replace("/","_").replace(":","_");
    }
    float pitch = camera.getPitch();
    float fieldOfView = camera.getFieldOfView();
    float yaw = camera.getYaw();
    cameraJson.addProperty("x", x);
    cameraJson.addProperty("y", y);
    cameraJson.addProperty("z", z);
    cameraJson.addProperty("name", name);
    cameraJson.addProperty("pitch", pitch);
    cameraJson.addProperty("fieldOfView", fieldOfView);
    cameraJson.addProperty("yaw", yaw);
    cameras.add(cameraJson);
  }

  public void addModel(HomePieceOfFurniture furniture) {
    JsonObject model = new JsonObject();
    String name = furniture.getName();
    String catalogId = furniture.getCatalogId();
    String id = catalogId;
//    if (catalogId != null) {
//      String [] splited_id = catalogId.split("_");
//      id = splited_id [splited_id.length - 1];
//    }
    float angle = furniture.getAngle();
    float x = furniture.getX();
    float y = furniture.getY();
    float z = furniture.getElevation();
    if (furniture.getLevel() != null) {
    	z = furniture.getLevel().getElevation() + furniture.getElevation();
	}

    float width = furniture.getWidth();
    float height = furniture.getHeight();
    float depth = furniture.getDepth();
    String scale = furniture.getWidthScale()+"," +furniture.getDepthScale()+"," +furniture.getHeightScale();
    model.addProperty("id", id);
    model.addProperty("name", name);
    model.addProperty("angle", angle);
    model.addProperty("x", x);
    model.addProperty("y", y);
    model.addProperty("z", z);
    model.addProperty("scale", scale);
    model.addProperty("width", width);
    model.addProperty("height", height);
    model.addProperty("depth", depth);
    if (furniture instanceof HomeDoorOrWindow) {
		model.addProperty("doorOrWindow", true);
	}else{
		model.addProperty("doorOrWindow", false);
	}
    models.add(model);
  }
  
  public void  addMaterial(Home home) {
	Collection<Wall> allWalls = home.getWalls();
	List<Wall> walls = new ArrayList<Wall>(allWalls);
	List<Room> rooms = home.getRooms();
	for (Wall wall : walls) {
		
		if (wall.getLeftSideTexture() != null) {
			JsonObject leftSideMaterial = new JsonObject();
			String leftSideTextureID = wall.getLeftSideTexture().getName();
			leftSideMaterial.addProperty("id", leftSideTextureID);
			leftSideMaterial.addProperty("info", "LeftSideTextureID");
			materials.add(leftSideMaterial);
		}
		if (wall.getRightSideTexture() != null) {
			JsonObject rightSideMaterial = new JsonObject();
			String RightSideTextureID = wall.getRightSideTexture().getName();
			rightSideMaterial.addProperty("id", RightSideTextureID);
			rightSideMaterial.addProperty("info", "RightSideTextureID");
			materials.add(rightSideMaterial);
		}
	}
	for (Room room : rooms) {
		if (room.getFloorTexture() != null) {
			JsonObject floorMaterial = new JsonObject();
			String floorID = room.getFloorTexture().getName();
			floorMaterial.addProperty("id", floorID);
			floorMaterial.addProperty("info", "FloorTextureID");
			materials.add(floorMaterial);
		}
		if (room.getCeilingTexture() != null) {
			JsonObject ceilingMaterial = new JsonObject();
			String ceilingID = room.getCeilingTexture().getName();
			ceilingMaterial.addProperty("id", ceilingID);
			ceilingMaterial.addProperty("info", "CeilingTextureID");
			materials.add(ceilingMaterial);
		}
	}
	
  }
  
  public void  addSroom(Home home) {
	  String homeid = home.getHomeid();
	  System.out.println(homeid);
	  sroom.addProperty("id", homeid);
}
	public void addProject(Home home) {
		String projectID = home.getProjectid();
		String projectName = home.getProjectName();
		project.addProperty("id", projectID);
		project.addProperty("name", projectName);
	}
  
	public void addRooms(Home home, HomeController controller) {
		List<Camera> cameras = new ArrayList<Camera>();
		List<Camera> cameraList = home.getStoredCameras();
		for (Camera camera : cameraList) {
			cameras.add(camera);
		}
		cameras.add(home.getCamera());
		List<Room> rooms = home.getRooms();
		for (Room room : rooms) {
			int cameraNum = 0;
			float roomRealArea = 0;
			float floorRealArea = 0;
			float ceilingRealArea = 0;
			if (room.getLevel() == null) {
				JsonObject roomInfo = new JsonObject();
				JsonArray cameraInfo = new JsonArray();
				Shape roomShape = room.getShape();
				for (Camera camera : cameras) {
					JsonObject cameraName = new JsonObject();
					float cameraX = camera.getX();
					float cameraY = camera.getY();
					if (roomShape.contains(cameraX, cameraY)) {
						cameraName.addProperty("name", camera.getName());
						cameraInfo.add(cameraName);
						cameraNum++;
					}
				}
				roomInfo.add("camera", cameraInfo);
				if (room.getFloorTexture() != null) {
					JsonObject floorMaterial = new JsonObject();
					String floorID = room.getFloorTexture().getName();
					float floorArea = room.getArea();
					floorMaterial.addProperty("id", floorID);
					floorMaterial.addProperty("area", floorArea);
					roomInfo.add("floor", floorMaterial);
				}
				if (room.getCeilingTexture() != null) {
					JsonObject ceilingMaterial = new JsonObject();
					String ceilingID = room.getCeilingTexture().getName();
					float ceilingArea = room.getArea();
					ceilingMaterial.addProperty("id", ceilingID);
					ceilingMaterial.addProperty("area", ceilingArea);
					roomInfo.add("ceiling", ceilingMaterial);
				}
				JsonArray wallsideInfo = new JsonArray();
				List<Room> tempRoom = new ArrayList<Room>();
				tempRoom.add(room);
				List<WallSide> wallsides = controller.getRoomController().getRoomsWallSides(tempRoom, null);
				for (WallSide wallSide : wallsides) {
					JsonObject wallpaperInfo = new JsonObject();
					Wall wall = wallSide.getWall();
					String wallpaperName = "";
					if (wallSide.getSide() == 0 && wall.getLeftSideTexture() != null) {
						wallpaperName = wall.getLeftSideTexture().getName();
					}else if (wallSide.getSide() == 1 && wall.getRightSideTexture() != null) {
						wallpaperName = wall.getRightSideTexture().getName();
					}
					float wallArea = wall.getLength() * wall.getHeight();
					roomRealArea = roomRealArea + wallArea;
					wallpaperInfo.addProperty("id", wallpaperName);
					wallpaperInfo.addProperty("area", wallArea);
					wallsideInfo.add(wallpaperInfo);
				}
				roomInfo.add("wallside", wallsideInfo);
				JsonArray furnitureInfo = new JsonArray();
				List<HomePieceOfFurniture> furnitures = home.getFurniture();
				for (HomePieceOfFurniture piece : furnitures) {
					JsonObject pieceInfo = new JsonObject();
					float pieceX = piece.getX();
					float pieceY = piece.getY();
					if (roomShape.contains(pieceX, pieceY) && piece.getSelfType() != null && piece.getSelfType().equals("skirtingboard")) {
						pieceInfo.addProperty("id", piece.getCatalogId());
						pieceInfo.addProperty("length", piece.getWidth());
						furnitureInfo.add(pieceInfo);
					}
				}
				JsonArray models = new JsonArray();
				for (HomePieceOfFurniture piece : furnitures) {
					JsonObject pieceInfo = new JsonObject();
					float pieceX = piece.getX();
					float pieceY = piece.getY();
					if (roomShape.contains(pieceX, pieceY)) {
						pieceInfo.addProperty("id", piece.getCatalogId());
						models.add(pieceInfo);
					}
				}
				roomInfo.add("models", models);
				JsonArray textures = new JsonArray();
				List<Room> selectedRoom = new ArrayList<Room>();
				selectedRoom.add(room);
				List<WallSide> roomWallSides = controller.getRoomController().getRoomsWallSides(selectedRoom, null);
				for (WallSide wallSide : roomWallSides) {
					JsonObject textureInfo = new JsonObject();
					HomeTexture leftTexture = wallSide.getWall().getLeftSideTexture();
					HomeTexture rightTexture = wallSide.getWall().getRightSideTexture();
					if (wallSide.getSide() == 0 && wallSide.getWall().getLeftSideTexture() != null) {
						textureInfo.addProperty("id", leftTexture.getName());
					}else if(wallSide.getSide() == 1 && wallSide.getWall().getRightSideTexture() != null){
						textureInfo.addProperty("id", rightTexture.getName());
					}else {
						textureInfo = null;
					}
					if (textureInfo != null) {
						textures.add(textureInfo);
					}
				}
				if (room.getCeilingTexture() != null) {
					JsonObject textureInfo = new JsonObject();
					textureInfo.addProperty("id", room.getCeilingTexture().getName());
					textures.add(textureInfo);
				}
				if (room.getFloorTexture() != null) {
					JsonObject textureInfo = new JsonObject();
					textureInfo.addProperty("id", room.getFloorTexture().getName());
					textures.add(textureInfo);
				}
				roomInfo.add("textures", textures);
				
				JsonObject floorArea = new JsonObject();
				floorRealArea = room.getArea();
				floorArea.addProperty("area", floorRealArea);
				roomInfo.add("floorarea", floorArea);
				
				JsonObject CeilingArea = new JsonObject();
				ceilingRealArea = room.getArea();
				CeilingArea.addProperty("area", ceilingRealArea);
				roomInfo.add("ceilingarea", CeilingArea);
				
				JsonObject wallTotalArea = new JsonObject();
				wallTotalArea.addProperty("area", roomRealArea);
				roomInfo.add("wallarea", wallTotalArea);
				
				if (cameraNum > 0) {
					this.rooms.add(roomInfo);
				}	
			}
		}
	}
//  public String getHomeid(Home home) {
		// 根据文件名获取homeid
//		String name = home.getName();
//		String id = name.substring(name.lastIndexOf("\\")+1, name.lastIndexOf("."));
//		return id;
//	}

  @Override
  public String toString() {
    return data.toString();
  }
}
