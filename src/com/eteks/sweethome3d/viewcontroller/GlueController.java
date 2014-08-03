/**
 * This file is part of the spp(Superpolo Platform). Copyright (C) by SanPolo
 * Co.Ltd. All rights reserved.
 *
 * See http://www.spolo.org/ for more information.
 *
 * SanPolo Co.Ltd http://www.spolo.org/ Any copyright issues, please contact:
 * copr@spolo.org
 **/
package com.eteks.sweethome3d.viewcontroller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.FurnituresInfomation;
import org.spolo.glue.GUtil;
import org.spolo.glue.GUtilRunTools;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.GutilResult;
import org.spolo.glue.MappingURL;
import org.spolo.glue.RoomInformation;
import org.spolo.glue.data.SceneInfo;
import org.spolo.glue.filter.HomeFilter;
import org.spolo.glue.smartlaytout.GlobalParameters;
import org.spolo.glue.smartlaytout.SourceObjectUtil;

import com.eteks.sweethome3d.HomeFramePane;
import com.eteks.sweethome3d.SweetHome3D;
import com.eteks.sweethome3d.io.FileUserPreferences;
import com.eteks.sweethome3d.model.Camera;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeRecorder;
import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Selectable;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.swing.PlanComponent;

/**
 * A MVC controller for Glue Cloud.
 */
public class GlueController implements Controller {
	private Logger log = Logger.getLogger(GlueController.class.getName());
	// 添加进度条
	private final Home home;
	private final UserPreferences preferences;
	private final ViewFactory viewFactory;
	private HomeController m_HomeController;
	public static String homeidCache = null;
	public static String homeFile_tmp = null;
	private HomeView homeView;
	private ContentManager contentmanager;
	private String saveHousePath = null;

	public GlueController(Home home, UserPreferences preferences,
			ViewFactory viewFactory, ContentManager c) {
		this.home = home;
		this.preferences = preferences;
		this.viewFactory = viewFactory;
		this.contentmanager = c;
	}

	public void setHomeController(HomeController hc) {
		if (m_HomeController == null) {
			this.m_HomeController = hc;
			Home tempHome = hc.getHome();
			if (homeidCache != null) {
				tempHome.InitHome();
				hc.save();
			}

		}
	}

	@Override
	public HomeView getView() {
		if (this.homeView == null) {
			this.homeView = this.viewFactory.createHomeView(this.home,
					this.preferences, this.m_HomeController);
		}
		return this.homeView;
	}

	/**
	 * 设置GlueHome显示或隐藏。
	 */
	private void visibleOrNot() {

		Map<Home, JFrame> homeFrames = SweetHome3D.getHomeFrames();
		for (JFrame f : homeFrames.values()) {
			f.setVisible(!f.isVisible());
			System.out.println("setVisible to " + !f.isVisible());
		}
	}

	/**
	 * 导入云端户型
	 */
	public void improtGlueHomeModel() {
		log.log(Level.INFO, "Import Cloud Home Model");
		// 调用 新版的Gutil,完善新的命令行调用框架
		File homefile = null;
		try {
			GutilResult<File, File, File[], RoomInformation> result = GUtil
					.execute("OpenRoomLib");
			// sh3f 文件对象
			homefile = result.getMainResourceFile();
			if (homefile != null) {

				m_HomeController.open(homefile.getAbsolutePath());
				homeidCache = HomeFilter.getinstance().resetFurnitureID(
						homefile);
				homeFile_tmp = homefile.getParent();

				File srcDir = new File(homeFile_tmp);
				File roomlib = new File(GUtilRunTools.getTools().getSweetHomeDataDir().getPath() + File.separator + "roomlib");
				String[] fileList = srcDir.list();
				File zipFile = null;
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].endsWith(".zip")) {
						zipFile = new File(srcDir.toString() + File.separator
								+ fileList[i]);
						zipFile.delete();
					}
				}
				try {
					System.out.println("copy file to roomlib");
					FileUtils.copyDirectory(srcDir, roomlib);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// TODO 抛出异常,没有主文件
				log.log(Level.INFO, "没有获取任何户型！");
				return;
			}
			// 此户型所附带的json文件中内容的对象
			RoomInformation jsonobj = result.getJsonObject();
			if (jsonobj != null) {
				// TODO
			}
			// 此户型所附带的json文件对象
			File json_file = result.getAdditionResourceFile();
			if (json_file != null) {
				// TODO
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		changeTitle();

	}
	
	public void importGLueApartmentModel() {
		log.log(Level.INFO, "Import Cloud Home Model");
		// 调用 新版的Gutil,完善新的命令行调用框架
		File homefile = null;
		try {
			GutilResult<File, File, File[], RoomInformation> result = GUtil
					.execute("OpenApartmentLib");
			// sh3f 文件对象
			homefile = result.getMainResourceFile();
			if (homefile != null) {

				m_HomeController.open(homefile.getAbsolutePath());
				homeidCache = HomeFilter.getinstance().resetFurnitureID(
						homefile);

//				File srcDir = new File(homeFile_tmp);
//				File roomlib = new File(GUtilRunTools.getTools()
//						.getSweetHomeDataDir().getPath()
//						+ File.separator + "roomlib");
//				String[] fileList = srcDir.list();
//				File zipFile = null;
//				for (int i = 0; i < fileList.length; i++) {
//					if (fileList[i].endsWith(".zip")) {
//						zipFile = new File(srcDir.toString() + File.separator
//								+ fileList[i]);
//						zipFile.delete();
//					}
//				}
				//TODO  确定是否需要户型缓存及户型缓存目录。
//				try {
//					System.out.println("copy file to roomlib");
//					FileUtils.copyDirectory(srcDir, roomlib);
//				} catch (IOException e) { //
//					e.printStackTrace();
//				}
			} else {
				// TODO 抛出异常,没有主文件
				log.log(Level.INFO, "没有获取任何户型！");
				return;
			}
			// 此户型所附带的json文件中内容的对象
			RoomInformation jsonobj = result.getJsonObject();
			if (jsonobj != null) {
				// TODO
			}
			// 此户型所附带的json文件对象
			File json_file = result.getAdditionResourceFile();
			if (json_file != null) {
				// TODO
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		changeTitle();
	}
	


	public void improtLocalHomeModel() {
		System.out.println("[INFO]: Import Local Home Model");

		File homefile = null;
		File dir = GlueUtil.createHuxingTempFolder();
		try {
			homefile = GlueUtil.openLocalRoomlib(dir);
			if (homefile != null) {
				m_HomeController.open(homefile.getAbsolutePath());
				homeidCache = HomeFilter.getinstance().resetFurnitureID(
						homefile);
				homeFile_tmp = homefile.getParent();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		changeTitle();

	}

	/**
	 * 导入云端模型
	 */
	public void importCloudModel() {
		System.out.println("[INFO]: Import Cloud Model");
			try {
				GutilResult<File[], File[], File[], FurnituresInfomation> result = GUtil
						.execute("OpenFurnitureLib");
				if(result != null){
					File furnitureDir = new File(GUtilRunTools.getTools().getSweetHomeDataDir() + File.separator + "furniture");
					String[] fileList = furnitureDir.list();
					File zipFile = null;
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].endsWith(".zip")) {
							zipFile = new File(furnitureDir.toString() + File.separator
									+ fileList[i]);
							zipFile.delete();
						}
					}
					importModel();
				}else{
					log.log(Level.INFO, "没有获取任何家具模型！");
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		changeTitle();

	}

	/**
	 * 导入云端外景
	 */
	public void importCloudOutDoorScene() {
		// visibleOrNot();
		System.out.println("[TODO]: import outdoor scene...");

		// visibleOrNot();
	}

	/**
	 * 管理渲染任务
	 */
	public void manageRenderTask() {

		try {
			// visibleOrNot();
			GlueUtil.openJobManager();
			// visibleOrNot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		changeTitle();
	}

	/**
	 * 管理我的效果图
	 */
	public void manageMyRenderings() {

		try {
			// visibleOrNot();
			GlueUtil.openMyPreviewlib();
			// visibleOrNot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		changeTitle();
	}
	
	/**
	 * 查看我的标书
	 */
	public void manageTender() {

		try {
			// visibleOrNot();
			GUtil.execute("ManageTender");
			// visibleOrNot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		changeTitle();
	}



	public void singleCloudRender() {

		Home home = m_HomeController.getCurrentHomeCopy();
		home.clearCameraStore();
//		if (m_HomeController.isRenderOK(home.getCamera()) != null) {
		home.getCamera().setName("\u5F53\u524D\u89C6\u89D2");
		String info = null;
		info = m_HomeController.isRenderOK(home.getCamera());
		if (info != null) {
			int iscontinue = JOptionPane.showConfirmDialog(null, info,
					"\u8B66\u544A", JOptionPane.YES_NO_OPTION);
			switch (iscontinue) {
			case 0:
				break;
			case 1:
			return;
			}
		}
		HomeFilter.getinstance().GroupHomeModel(home);
		List<HomePieceOfFurniture> furniture = home.getFurniture();
		boolean noWalls = home.getWalls().isEmpty();
		int ccount = home.getStoredCameras().size();
		if (furniture.size() > 0 || !noWalls) {
			org.spolo.glue.filter.HomeFilter.CheckResult result = HomeFilter
					.getinstance().checkFurnitureRenderInfo(home, m_HomeController.getRoomController());
			if (!result.isCorrect) {
				int iscontinue = JOptionPane.showConfirmDialog(null,
						result.info);
				switch (iscontinue) {
				case 0:
					break;
				case 1:
					return;
				case 2:
					return;
				}
			}
			File dir = new File(GUtilRunTools.getTools().getGlueDataDir().getAbsoluteFile()+ File.separator + "rederJson");
			dir.mkdirs();
			String objName = "render.obj";
			String objFile = dir.toString() + File.separator + objName;
			try {
				//渲染当前视角就没有必要再去对楼层进行考虑了。
					m_HomeController.getView().exportHouseToOBJ(objFile);
				// 对产生的文件进行过滤，避免上传无用数据。
				String[] fileList = dir.list();
				File pic = null;
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].endsWith(".png")) {
						pic = new File(dir.toString() + File.separator
								+ fileList[i]);
						pic.delete();
					}
				}
				this.exportHomeJsonInfo(home, dir);
				this.saveWallHouse(dir);
				// 没有保存视点的情况下
				GUtil.execute("UploadRenderJobs");
				// visibleOrNot();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					FileUtils.deleteDirectory(dir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"\u8bf7\u5148\u6253\u5f00\u4e00\u4e2a\u6837\u677f\u95f4\uff0c\u7136\u540e\u518d\u8fdb\u884c\u62cd\u7167\u3002");
		}
		changeTitle();
	}

	/**
	 * 将本地的资源数据上传到云端,然后利用云端的高质量模型来渲染最终的效果图
	 *
	 * @param <CheckResult>
	 */
	public <CheckResult> void cloudRender() {
		// m_HomeController.save();
		Home home = m_HomeController.getCurrentHomeCopy();
		home.getCamera().setName("\u5F53\u524D\u89C6\u89D2");
		String info = null;
		List<Camera> cameras = new ArrayList<Camera>();
		List<Camera> stroreCamera = home.getStoredCameras();
		cameras.add(home.getCamera());
		for (Camera camera : stroreCamera) {
			cameras.add(camera);
		}
		for (Camera camera : cameras) {
			info = m_HomeController.isRenderOK(camera);
		}
		if (info != null) {
			int iscontinue = JOptionPane.showConfirmDialog(null, info,
					"\u8B66\u544A", JOptionPane.YES_NO_OPTION);
			switch (iscontinue) {
			case 0:
				break;
			case 1:
			return;
			}
		}
		HomeFilter.getinstance().GroupHomeModel(home);
		List<HomePieceOfFurniture> furniture = home.getFurniture();
		boolean noWalls = home.getWalls().isEmpty();
		int ccount = home.getStoredCameras().size();
		if (furniture.size() > 0 || !noWalls) {
			org.spolo.glue.filter.HomeFilter.CheckResult result = HomeFilter
					.getinstance().checkFurnitureRenderInfo(home,m_HomeController.getRoomController());
			if (!result.isCorrect) {
				int iscontinue = JOptionPane.showConfirmDialog(null,
						result.info);
				switch (iscontinue) {
				case 0:
					break;
				case 1:
					return;
				case 2:
					return;
				}
			}
			File dir = GlueUtil.getRenderTempDir();			
			String objName = "render.obj";
			String objFile = dir.toString() + File.separator + objName;
			try {
				if (!home.getEnvironment().isAllLevelsVisible()) {
					m_HomeController.getHomeController3D().displayAllLevels();
					m_HomeController.getView().exportHouseToOBJ(objFile);
					m_HomeController.getHomeController3D().displaySelectedLevel();
				}else {
					m_HomeController.getView().exportHouseToOBJ(objFile);
				}
				// 对产生的文件进行过滤，避免上传无用数据。
				String[] fileList = dir.list();
				File pic = null;
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].endsWith(".png")) {
						pic = new File(dir.toString() + File.separator
								+ fileList[i]);
						pic.delete();
					}
				}
				this.exportHomeJsonInfo(home, dir);
				this.saveWallHouse(dir);
				// 没有保存视点的情况下
				if (ccount == 0) {
					int action = JOptionPane
							.showConfirmDialog(
									null,
									"\u6CA1\u6709\u4FDD\u5B58\u4EFB\u4F55\u89C6\u89D2,\u786E\u5B9A\u6E32\u67D3\u5F53\u524D3D\u89C6\u56FE\u4E2D\u7684\u89C6\u89D2?");
					switch (action) {
					case 0:
						// visibleOrNot();
						
						prepareRender(new String[]{"当前视角"},dir.getName());				
						// visibleOrNot();
						break;
					case 1:
						break;
					case 2:
						break;
					}
				}
				// 保存了视点
				else {
					// visibleOrNot();
					List<Camera> cs = home.getStoredCameras();
					String[] camerasarray = new String[ccount];
					for(int i=0;i<camerasarray.length;i++){
						camerasarray[i]=cs.get(i).getName();						
					}
					prepareRender(camerasarray,dir.getName());			
					// visibleOrNot();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"\u8bf7\u5148\u6253\u5f00\u4e00\u4e2a\u6837\u677f\u95f4\uff0c\u7136\u540e\u518d\u8fdb\u884c\u62cd\u7167\u3002");
		}
		changeTitle();

	}
	public void prepareRender(String[] cameras,String id){
		Desktop desktop = Desktop.getDesktop(); 
		String path = MappingURL.uploadjoburl+"?";						
		File gutil = new File("gutil//gutil.exe");
		CommandLine command = new CommandLine(gutil);
		command.addArgument("openweb");
		for(int i=0;i<cameras.length;i++){
			String cameraname =cameras[i]; 			
			String encodingname = null;
			try {
				encodingname = URLEncoder.encode(cameraname,"utf-8");
			} catch (UnsupportedEncodingException e) { 
				e.printStackTrace();
				return;
			}
			path +="c="+encodingname+"&";							
		}
		path +="hid="+id;	
		System.out.println("请求的url为: "+path);
		try {
		    //创建URI统一资源标识符
			URI uri = new URI(path);
		    //使用默认浏览器打开超链接
		    desktop.browse(uri);
		} catch(Exception ex) {
		    // TODO 弹出提示
		}
	}
	
	/**
	 * 创建标书
	 */
	public void createTender() {
		// m_HomeController.save();
		Home home = m_HomeController.getCurrentHomeCopy();
		if (m_HomeController.isRenderOK(home.getCamera()) != null) {
			return;
		}
		org.spolo.glue.filter.HomeFilter.CheckResult result = HomeFilter
				.getinstance().checkFurnitureRenderInfo(home,m_HomeController.getRoomController());
		if (!result.isCorrect) {
			int iscontinue = JOptionPane.showConfirmDialog(null,
					result.info);
			switch (iscontinue) {
			case 0:
				break;
			case 1:
				return;
			case 2:
				return;
			}
		}
		
		HomeFilter.getinstance().GroupHomeModel(home);
		Callable<Void> exportDataTask = new Callable<Void>() {
			public Void call() throws Exception {
				exportDataTask();
				return null;
			}
		};
		ThreadedTaskController.ExceptionHandler exceptionHandler = new ThreadedTaskController.ExceptionHandler() {
			public void handleException(Exception ex) {

				// getView().showError("error");
			}
		};
		new ThreadedTaskController(exportDataTask,
				"\u51C6\u5907\u6570\u636E\u4E2D\u2026\u2026",
				exceptionHandler, this.preferences, this.viewFactory)
				.executeTask(this.m_HomeController.getView());
		
		

	}
	
	private void exportDataTask() {
		Home home = m_HomeController.getCurrentHomeCopy();
		home.getCamera().setName("\u5F53\u524D\u89C6\u89D2");
		List<HomePieceOfFurniture> furniture = home.getFurniture();
		boolean noWalls = home.getWalls().isEmpty();
		int ccount = home.getStoredCameras().size();
		if (furniture.size() > 0 || !noWalls) {
			
			
			File dir = new File(GUtilRunTools.getTools().getGlueDataDir()
					.getAbsoluteFile()
					+ File.separator + "rederJson");
			dir.mkdirs();
			String objName = "render.obj";
			String objFile = dir.toString() + File.separator + objName;
			File imageFile = new File(dir.getAbsolutePath().toString() + File.separator + "topview.jpg");
			try {
				m_HomeController.getView().exportHouseToOBJ(objFile);
				// 对产生的文件进行过滤，避免上传无用数据。
				String[] fileList = dir.list();
				File pic = null;
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].endsWith(".png")) {
						pic = new File(dir.toString() + File.separator
								+ fileList[i]);
						pic.delete();
					}
				}
				saveTopImage(imageFile);
				exportHomeJsonInfo(home, dir);
				// 没有保存视点的情况下
				if (ccount == 0) {
					int action = JOptionPane
							.showConfirmDialog(
									null,
									"\u6CA1\u6709\u4FDD\u5B58\u4EFB\u4F55\u89C6\u89D2,\u786E\u5B9A\u6E32\u67D3\u5F53\u524D3D\u89C6\u56FE\u4E2D\u7684\u89C6\u89D2?");
					switch (action) {
					case 0:
						// visibleOrNot();
						GUtil.execute("CreateTender");
						// visibleOrNot();
						break;
					case 1:
						break;
					case 2:
						break;
					}
				}
				// 保存了视点
				else {
					// visibleOrNot();
					GUtil.execute("CreateTender");
					// visibleOrNot();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					FileUtils.deleteDirectory(dir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"\u8bf7\u5148\u6253\u5f00\u4e00\u4e2a\u6837\u677f\u95f4\uff0c\u7136\u540e\u518d\u8fdb\u884c\u62cd\u7167\u3002");
		}
		changeTitle();

	}
	
	

	/**
	 * 导出户型低模
	 */
	public void exportHome() {
		// TODO 对Home中所有模型的id做过滤
		//System.out.println("[DEBUG]: export home");
		Home home = m_HomeController.getCurrentHomeCopy();
		String name = m_HomeController.getContentManager().showSaveDialog(null,
				"save new sh3d", ContentManager.ContentType.SWEET_HOME_3D,
				"test");
		exportModelsToJson(home, name);
		saveHomeToLowModel(home, name);
	}
	private void exportModelsToJson(Home home, String path) {
		File models_json = new File(path);
		String models = models_json.getParentFile().getAbsolutePath()
				+ File.separator + "models.json";
		try {
			// Home temp = HomeFilter.getinstance().resetModelID(home);
			this.writeHomeToFile(home, models);
		} catch (Exception e1) {
			System.out.println("[ERROR]: Export models json file failed!!!");
			e1.printStackTrace();
		}

	}

	private void saveHomeToLowModel(Home h, String path) {
		// 过滤Home对象中模型的id
//		Home home = HomeFilter.getinstance().filterFurnitureID(h);
		// 基本元素锁死
		h.setBasePlanLocked(true);
		// 获取读取对象
		HomeRecorder re = m_HomeController.getHomeApplication()
				.getHomeRecorder(HomeRecorder.Type.COMPRESSED);

		// List<HomePieceOfFurniture> furnitures = home.getFurniture();
		// for (HomePieceOfFurniture f : furnitures) {
		// System.out.println("Furniture ID : " + f.getCatalogId());
		// }
		try {
			re.writeHome(h, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 从样板间中提取户型并保存
	 */
	private void saveHouseToFile(Home h, String path) {
		List<HomePieceOfFurniture> furnitures = h.getFurniture();
		List<Room> rooms = h.getRooms();
		Collection<Wall> allWalls = h.getWalls();
		List<Wall> walls = new ArrayList<Wall>(allWalls);
		for (Room room : rooms) {
//			if (room.getFloorTexture() != null) {
				room.setFloorTexture(null);
				room.setCeilingTexture(null);
//			}
		}
		for (Wall wall : walls) {
			wall.setLeftSideTexture(null);
			wall.setRightSideTexture(null);
		}
		for (HomePieceOfFurniture piece : furnitures) {
			if (!(piece instanceof HomeDoorOrWindow)) {
				h.deletePieceOfFurniture(piece);
			}
		}
		
		saveHomeToLowModel(h, path);
		
		
	}
	
	/**
	 * 保存户型
	 *
	 */
	public void saveHouse() {
		saveHousePath = null;
		Home home = m_HomeController.getCurrentHomeCopy();
		homeidCache = home.getReferid();
		String houseFile = m_HomeController.getContentManager().showSaveDialog(
				null, "save new sh3d",
				ContentManager.ContentType.SWEET_HOME_3D, "test");
		saveHousePath = houseFile;
		// 导出户型低模;
		saveHomeToLowModel(home, houseFile);
	}
	/**
	 * 通过样板间文件名生成户型文件名
	 */
	private String getHouseName(String sh3fHomeFile) {
		String sh3fHouseFile = null;
		String HomeName = sh3fHomeFile.substring(sh3fHomeFile.lastIndexOf("\\")+1, sh3fHomeFile.lastIndexOf("."));
		String HouseName = HomeName + "_House.sh3d";
		HomeName = HomeName + ".sh3d";
		sh3fHouseFile = sh3fHomeFile.replaceAll(HomeName, HouseName);
		return sh3fHouseFile;
	}

	/**
	 * 户型入库,包括已经调整好的装修模板,户型低模,和墙体obj等等
	 */
	public void submitRoom() {
		Home home = m_HomeController.getCurrentHomeCopy();
		String sh3f_file = null;
		if (saveHousePath == null) {
			sh3f_file = m_HomeController.getContentManager().showSaveDialog(
					null, "save new sh3d",
					ContentManager.ContentType.SWEET_HOME_3D, "test");
		}else {
			sh3f_file = getHouseName(saveHousePath);
		}
		
		// 导出户型低模;
		saveHomeToLowModel(home, sh3f_file);
//		exportModelsToJson(home, sh3f_file);
		File sh3f_file_obj = new File(sh3f_file);
		// 导出装修模板的json文件
		try {
			SourceObjectUtil sourceobj = SourceObjectUtil.getInstance(
					contentmanager, preferences);
			File tempfile = new File(sh3f_file);
			String decfile_path = tempfile.getParent() + File.separator
					+ "decoration.json";
			// 将当前户型中所应用的装修模板重新导出到json文件中
			sourceobj.writeSourceInfo(home, decfile_path);
			File decfile = new File(decfile_path);
			System.setProperty("decfile", decfile.getAbsolutePath());
		} catch (Exception e) {
			log.log(Level.INFO, e.getMessage());
		}
		
		Home home2 = m_HomeController.getCurrentHomeCopy();
		home.setBasePlanLocked(false);
		String housePath = getHouseName(sh3f_file);
		saveHouseToFile(home2, housePath);

		try {
//			if (saveHousePath != null) {
				System.setProperty("sroomlowpoly", housePath);
//			}
			System.setProperty("roomlowpoly", sh3f_file);
			System.setProperty("gworkdir", sh3f_file_obj.getParent());
			GUtil.execute("UploadRoom");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO 额外生成一个索引的json文件.以便告诉gutil读取哪些文件.
		// TODO 导出户型的墙体.

	}
	private void importModeltask() {
		// TODO 是gutil下载完了zip包，然后解压到了gluetemp目录下了，之后zip就删除了，
		// 这里通过FileUtils.copyFile来把sh3f文件以同名的json文件同时拷贝到SweetHome缓存目录下就行了，

//		if (models != null) {
//			Iterator<File> itr = models.iterator();
			// while (itr.hasNext()) {
			// File element = itr.next();
			// String path = element.getAbsolutePath();
			// FileUtils.copyFile(srcFile, destFile);
			// importGlueFurnitureLibrary(path);
			// FileUserPreferences.updateTexturesDefaultCatalog();
			// while(itr.hasNext()){
			// File furnitureLibruary = itr.next();
			// String fileName = furnitureLibruary.getName().substring(0,
			// furnitureLibruary.getName().indexOf("."));
			// String srcPath =
			// furnitureLibruary.getAbsolutePath().replace(furnitureLibruary.getName(),
			// "");
			// String destPath = System.getenv("APPDATA") +
			// "\\eTeks\\Sweet Home 3D\\furniture";
			// copyFolder(srcPath, destPath);
			((FileUserPreferences) preferences).updateFurnitureDefaultCatalog();

//		}
	}

	private void importModel() {
		Callable<Void> importModelTask = new Callable<Void>() {
			public Void call() throws Exception {
				// TODO Auto-generated method stub
				importModeltask();
				return null;
			}
		};
		ThreadedTaskController.ExceptionHandler exceptionHandler = new ThreadedTaskController.ExceptionHandler() {
			public void handleException(Exception ex) {

				// getView().showError("error");
			}
		};
		new ThreadedTaskController(importModelTask,
				"\u6B63\u5728\u5BFC\u5165\u5BB6\u5177\u2026\u2026",
				exceptionHandler, this.preferences, this.viewFactory)
				.executeTask(this.m_HomeController.getView());
	}

	/**
	 * Imports a given furniture library.
	 */
	public void importGlueFurnitureLibrary(String furnitureLibraryName) {
		m_HomeController.importFurnitureLibrary(furnitureLibraryName);
	}

	public void changeTitle() {
		HomeFramePane.getInstance().setTitle();
	}

	/**
	 * home数据导出到一个文件中
	 *
	 * @param home
	 * @return
	 * @throws Exception
	 */
	public File exportHomeJsonInfo(Home home, File dir) throws Exception {
		String path = dir.getAbsolutePath() + File.separator + "render.json";
		return writeHomeToFile(home, path);
	}

	public File writeHomeToFile(Home home, String path) throws Exception {
		SceneInfo si = parserHome(home, this.m_HomeController);
		File renderFile = GlueUtil.createNewFile(path);
		FileUtils.writeStringToFile(renderFile, si.toString(), "utf-8");
		return renderFile;
	}

	/**
	 * 将home中的数据生成glue标准格式的json串
	 *
	 * @param home
	 * @return
	 */
	public SceneInfo parserHome(Home home, HomeController controller) {
		SceneInfo scene = new SceneInfo(home, controller);
		return scene;
	}

	/**
	 * 打开用户中心页面
	 */
	public void usercenter() {
		try {
			GlueUtil.openUserCenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}
	
	public void saveTopImage(File imageFile){
		
		if (this.home.getBackgroundImage() != null) {
			m_HomeController.hideBackgroundImage();
		}
		if (this.home.getCompass().isVisible()) {
			this.home.getCompass().setVisible(false);
		}
		
		PlanComponent planComponent = new PlanComponent(home,preferences, m_HomeController.getPlanController());
		BufferedImage imageTemp = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		Graphics g = imageTemp.createGraphics();
		List<Selectable> printedItems = planComponent.getPaintedItems(); 
		Rectangle2D printedItemBounds = planComponent.getItemsBounds(g, printedItems);
		
		int imageWidth = (int)printedItemBounds.getWidth()+1;
		int imageHeight = (int)printedItemBounds.getHeight()+1;
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g2 = image.createGraphics();
		Graphics2D g2D = (Graphics2D) g2.create();
		
		g2D.setBackground(Color.WHITE);
		g2D.fillRect(0, 0, imageWidth, imageHeight);
		float printScale = planComponent.getScale();
		g2D.translate(-printedItemBounds.getX(), -printedItemBounds.getY());
		try {
			planComponent.paintContent(g2D,printScale, planComponent.getPaintMode());
			ImageIO.write(image, "jpg", imageFile);
			
			g2D.dispose();
		} catch (InterruptedIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if (this.home.getBackgroundImage() != null) {
			m_HomeController.showBackgroundImage();
		}
	}
	
	public void saveWallHouse(File dir) {
		if (this.home.getBackgroundImage() != null) {
			m_HomeController.deleteBackgroundImage();
		}
		Home home = m_HomeController.getCurrentHomeCopy();
		List<HomePieceOfFurniture> furnitures = home.getFurniture();
		Collection<Wall> allWalls = home.getWalls();
		List<Wall> walls = new ArrayList<Wall>(allWalls);
		List<Room> rooms = home.getRooms();
		if (!furnitures.isEmpty()) {
			for (HomePieceOfFurniture furniture : furnitures) {
				home.deletePieceOfFurniture(furniture);
			}
		}
		if (!walls.isEmpty()) {

			for (Wall wall : walls) {
				if (wall.getLeftSideTexture() != null) {
					HomeTexture realLeftHomeTexture = wall.getLeftSideTexture();
					HomeTexture fakeLeftHomeTexture = fakeHomeTexture(realLeftHomeTexture);
					wall.setLeftSideTexture(fakeLeftHomeTexture);
				}
				if (wall.getRightSideTexture() != null) {
					HomeTexture realRightHomeTexture = wall.getRightSideTexture();
					HomeTexture fakeRightHomeTexture = fakeHomeTexture(realRightHomeTexture);
					wall.setRightSideTexture(fakeRightHomeTexture);
				}
			}
		}
		if (!rooms.isEmpty()) {
			for (Room room : rooms) {
				if (room.getCeilingTexture() != null) {
					HomeTexture ceilingHomeTexture = room.getCeilingTexture();
					HomeTexture fakeCeilingHomeTexture = fakeHomeTexture(ceilingHomeTexture);
					room.setCeilingTexture(fakeCeilingHomeTexture);
				}
				if (room.getFloorTexture() != null) {
					HomeTexture floorHomeTexture = room.getFloorTexture();
					HomeTexture fakeFloorHomeTexture = fakeHomeTexture(floorHomeTexture);
					room.setFloorTexture(fakeFloorHomeTexture);
				}
			}
		}
		saveHomeToLowModel(home, dir.getAbsolutePath() + File.separator +  "house.sh3d");
	}
	
	/**
	 * 伪造墙体上贴图的信息，去除图片数据
	 */
	public HomeTexture fakeHomeTexture(HomeTexture texture){
		HomeTexture  homeTexture = null;
		String textureName = texture.getName();
		float width = texture.getWidth();
		float height = texture.getHeight();
		CatalogTexture ca = new CatalogTexture(textureName, null, width, height);
		homeTexture = new HomeTexture(ca);
		return homeTexture;
	}
	
	
	public File mappingFile(String id, String sourceType, File dataDir) {
		File sourceFile = null;
		if (sourceType.equals(GlobalParameters.SOURCE_FILE_FURNITURE)) {
			sourceFile = new File(dataDir.getAbsolutePath() + File.separator + id + ".sh3f");
		}
		if (sourceType.equals(GlobalParameters.SOURCE_FILE_TEXTURE)) {
			sourceFile = new File(dataDir.getAbsolutePath() + File.separator + id + ".json");
		}
		return sourceFile;
	}
}
