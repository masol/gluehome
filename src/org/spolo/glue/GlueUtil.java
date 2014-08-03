package org.spolo.glue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.smartlaytout.GContentObject;
import org.spolo.glue.smartlaytout.GlobalParameters;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gson.Gson;

public class GlueUtil {
	public static String SupportFileFormat = "sh3d,sh3f";
	//userProperty
	public static final String UserName = "username";
	public static final String Company = "resourceName";
	public static final String CompanyImagePath = "imagePath";
	//用户对应的company信息文件
	public static String CompanyDir = "company";
	public static String CompanyJsonFile = CompanyDir+File.separator+"company.json";
	//gluehome版本号
	public static String Version = "2.0";
	
	/**
	 * 获取glueUtil的临时目录
	 * 
	 * @return File 临时目录
	 */
	public static File getTempDir() {
		File tempHome = new File(System.getProperty("java.io.tmpdir") + File.separator + "GlueTemp");
		if (!tempHome.exists() || !tempHome.isDirectory()) {
			tempHome.mkdirs();
		}
		return tempHome;
	}
	public static File getSceneTempDir(){
		File tempHome = new File(System.getProperty("java.io.tmpdir") + File.separator + "scenetemp");
		if (!tempHome.exists() || !tempHome.isDirectory()) {
			tempHome.mkdirs();
		}
		return tempHome ;
	}
	public static File getRenderTempDir(){
		File tempHome = GlueUtil.getSceneTempDir();
		return createTempFolder(tempHome);
	}

	/**
	 * 获取户型临时目录，用于户型的临时操作
	 * @return
	 */
	public static File getHuxingTempDir(){	  
	    File huxingDir = new File(GUtilRunTools.getTools().getGlueDataDir().getPath()+File.separator+"work");
	    if(!huxingDir.exists()){
    		huxingDir.mkdirs();
	    }
	    return huxingDir;
	}
	/**
	 * 获取家具缓存目录
	 * @return
	 */
	public static File getJiajuDir(){	  
	    File dir = new File(GUtilRunTools.getTools().getSweetHomeDataDir().getPath()+File.separator+"furniture");
	    if(!dir.exists()){
		dir.mkdirs();
	    }
	    return dir;
	}
	/**
	 * 获取贴图缓存目录
	 * @return
	 */
	public static File getTietuDir(){	  
	    File dir = new File(GUtilRunTools.getTools().getSweetHomeDataDir().getPath());
	    if(!dir.exists()){
		dir.mkdirs();
	    }
	    return dir;
	}
	public static File getTextureDir(){
		File textureFile = new File(GUtilRunTools.getTools().getSweetHomeDataDir().getAbsolutePath()+File.separator+"textures");
		if(!textureFile.exists()){
			textureFile.mkdir();			
		}
		return textureFile; 
	}
	public static String getID(File file){
	    String id = file.getName().substring(0,file.getName().lastIndexOf("."));
	    return id;
	}

	/**
	 * 缓存贴图
	 * @param dir
	 */
	public static void cacheTietu(Collection<File> files){
	    File base_dir = getTietuDir(); 
//	    splitTietuJson(files,base_dir);
	}
	/**
	 * 拆分贴图的json文件
	 * @param files
	 * @param base_dir
	 */
//	public static Collection<File> splitTietuJson(Collection<File> files,File to_dir){
//	  //遍历json文件
//	    Collection<File> result = new ArrayList<File>();
//	    for(File file:files){
//		HashMap<String, HashMap<String, String>> map = JsonUtil.getJsonFile(file);
//		String basePath = file.getAbsoluteFile().getParent()+File.separator;
//		for(String filename : map.keySet()){
//		    File imageFile = new File(basePath+filename);
//		    try {
//			String id = getID(imageFile);
//			//copy图片到缓存目录
//			File dir = to_dir;
//			if(to_dir==null){
//			    dir = imageFile.getParentFile();
//			}else{
//			    FileUtils.copyFileToDirectory(imageFile, dir);
//			}
//			//生成json文件到缓存目录
//			File jsonFile = new File(dir.getAbsolutePath()+File.separator+id+".json");
//			HashMap<String, HashMap<String, String>> map_unit = new HashMap<String, HashMap<String, String>>();
//			map_unit.put(filename, map.get(filename));
//			String json_string = new Gson().toJson(map_unit);
//			FileUtils.writeStringToFile(jsonFile, json_string, "utf-8");
//			result.add(jsonFile);
//		    } catch (IOException e) {
//			e.printStackTrace();
//		    }
//		}
//	    }
//	    return result;
//	}

	/**
	 * 通过id获取家具
	 * @param id
	 * @return
	 */
	public static  HashMap<String, MappingResult> mappingJiaju(String[] id){
	    HashMap<String, MappingResult> result = new HashMap<String, MappingResult>();
	    //需要下载的模型id
	    ArrayList<String> download_id = new ArrayList<String>();
	    for(String _id:id){
		//缓存目录
	    	result = getJiajuFromCache(_id);
//		if(jiaju!=null && !jiaju.isEmpty()){
//		    result.putAll(jiaju);
//		}else{
//		    download_id.add(_id); 
//		}
//	    }
//	    if(!download_id.isEmpty()){
//		try {
//		    Collection<File> files = DownLoadModels(createTempFolder(), download_id.toArray(new String[download_id.size()]));
//		    for(File file : files){
//			String _id = getID(file);
//			MappingResult mr = new MappingResult(_id, file.getAbsolutePath(), GlobalParameters.SOURCEOBJ_TYPE_FURNITURE);
//			mr.setCached(false);
//			result.put(_id, mr);
//		    }
//		} catch (ExecuteException e) {
//		    e.printStackTrace();
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
	    }
	    return result;
	}
	/**
	 * 通过id获取贴图
	 * @param id
	 * @return
	 */
	public static  HashMap<String, MappingResult> mappingTietu(String[] id){
	    HashMap<String, MappingResult> result = new HashMap<String, MappingResult>();
	    //需要下载的模型id
//	    ArrayList<String> download_id = new ArrayList<String>();
	    HashMap<String, MappingResult> tietu = getTietuFromCache(id);
		for (String _id : tietu.keySet()) {
			// 缓存目录
//			if (tietu.get(_id) != null) {
				result.put(_id, tietu.get(_id));
//			} else {
//				download_id.add(_id);
//			}
		}
//	    if(!download_id.isEmpty()){
//        	    try {
////        	    File file = new File("C:/Users/Administrator/AppData/Roaming/eTeks/Sweet Home 3D/96fad2d5-f88c-4d61-b2cb-6a4121b00983.json");
//        		File file = DownloadTexture(createTempFolder(), download_id.toArray(new String[download_id.size()]));
//        		Collection<File> jsonfiles = new ArrayList<File>();
//        		jsonfiles.add(file);
//        		jsonfiles = splitTietuJson(jsonfiles, null);
//        		for(File jsonfile:jsonfiles){
//                		String _id = getID(jsonfile);
//        			MappingResult mr = new MappingResult(_id, jsonfile.getAbsolutePath(), GlobalParameters.SOURCEOBJ_TYPE_TEXTURE);
//        			mr.setCached(false);
//        			result.put(_id, mr);
//        		}
//        	    } catch (ExecuteException e) {
//        		e.printStackTrace();
//        	    } catch (IOException e) {
//        		e.printStackTrace();
//        	    }
//	    }
	    return result;
	}
	/**
	 * 从缓存中获取一张贴图
	 * @param id
	 * @return
	 */
	public static  HashMap<String, MappingResult> getTietuFromCache(String[] id){
	    HashMap<String, MappingResult> result = new HashMap<String, MappingResult>();
	    try{
    	    	File dir =  getTietuDir();
    	    	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    	    	domFactory.setNamespaceAware(true);
    	    	DocumentBuilder builder = domFactory.newDocumentBuilder();
    	    	org.w3c.dom.Document doc = builder.parse(dir.getAbsolutePath()+File.separator+"preferences.xml");
    	    	//读取xml，查找id对应的pref
    	    	for(String _id : id){
    	    	    String prefFileName = findTietuPREF(_id,doc);
    	    	    File prefFile = new File(dir.getAbsolutePath()+File.separator+prefFileName);
    	    	    if(prefFileName!=null){
    	    		//更新json文件中的图片指向pref
    	    		try {
    	    		    File jsonfile = new File(dir.getAbsolutePath()+File.separator+ "textures" + File.separator +_id+".json");
//    	    		    TextureBean map_unit = JsonUtil.getJsonFile(jsonfile);
//    	    		    map_unit.put(prefFileName,  map_unit.get(_id));
//    	    		    map_unit.remove(_id);
//    	    		    String json_string = new Gson().toJson(map_unit);
//			    FileUtils.writeStringToFile(jsonfile, json_string, "utf-8");
			} catch (Exception e) {
			    //e.printStackTrace();
			}	
    	    		result.put(_id, new MappingResult(_id, prefFile.getAbsolutePath(), GlobalParameters.SOURCEOBJ_TYPE_TEXTURE));
    	    	    }else{
    	    		result.put(_id, null);
    	    	    }
    	    	}
	    }catch(Exception e){
		e.printStackTrace();
	    }
	    return result;
	}
	/**
	 * 通过xpath查询文件
	 * @param id
	 * @param doc
	 * @throws XPathExpressionException 
	 * @throws Exception
	 */
	public static String findTietuPREF(String id,org.w3c.dom.Document doc){ 
	    String prefFileName = null;
	    try{
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression queryID = xpath.compile("//entry[text()='"+id+"']");
	    NodeList nodes = (NodeList)queryID.evaluate(doc, XPathConstants.NODESET);
	    if(nodes.getLength()>0){
		String key = nodes.item(0).getAttributes().getNamedItem("key").getNodeValue();
		key = key.replace("Name", "Image");
		XPathExpression queryFile = xpath.compile("//entry[@key='"+key+"']");
		NodeList nodes2 = (NodeList)queryFile.evaluate(doc, XPathConstants.NODESET);
		if(nodes2.getLength()>0){
		    prefFileName = nodes2.item(0).getTextContent();
		    prefFileName = prefFileName.substring(prefFileName.indexOf(":")+1, prefFileName.length());
		}
	    }
	    }catch(XPathExpressionException e){
		e.printStackTrace();
	    }
	    return prefFileName;
	}

	/**
	 * 从缓存中获取一个家具
	 * @param id
	 * @return
	 */
	public static  HashMap<String, MappingResult> getJiajuFromCache(String id){
	    HashMap<String, MappingResult> result = null;
	    File dir =  getJiajuDir();
	    //缓存目录
	    File file = new File(dir.getAbsolutePath()+File.separator+id+".sh3f");
	    if(file.exists()&&file.isFile()){
		result = new HashMap<String, MappingResult>();
		result.put(id, new MappingResult(id, file.getAbsolutePath(), GlobalParameters.SOURCEOBJ_TYPE_FURNITURE));
	    }
	    return result;
	}
	/**
	 * 在临时目录中创建临时文件夹，保存下载结果文件
	 * 
	 * @return 返回该文件夹
	 */
	public static File createHuxingTempFolder() {
		File tempHome = GlueUtil.getHuxingTempDir();
		return createTempFolder(tempHome);
	}
	/**
	 * 在临时目录中创建临时文件夹，保存下载结果文件
	 * 
	 * @return 返回该文件夹
	 */
	public static File createTempFolder() {
		File tempHome = GlueUtil.getTempDir();
		return createTempFolder(tempHome);
	}

	/**
	 * 字啊临时目录中创建临时文件夹，保存下载结果文件
	 * 
	 * @return 返回该文件夹
	 */
	public static File createTempFolder(File tempHome) {
		File tempFolder;
		do {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.S");
			tempFolder = new File(tempHome.getAbsolutePath() + File.separator
					+ df.format(calendar.getTime()));
		} while (tempFolder.exists());
		tempFolder.mkdirs();
		return tempFolder;
	}
	/**
	 * 创建一个新文件
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static File createNewFile(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
		    file.createNewFile();
		}
		return file;
	}

	/**
	 * 同步调用gutil，下载模型功能
	 * 
	 * @return 返回文件集合，集合内的文件为sweethome导入模型所需要的
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static Collection<File> DownLoadModels(File dir) throws ExecuteException,
			IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Jiaju);
		int exit_code = GUtilRunTools.getTools().DownLoadModels(tempFolder);
		handler.onProcessComplete(exit_code);
		return handler.getResult();
	}

	/**
	 * 异步调用gutil，下载模型功能
	 * 
	 * @param callback
	 *            DownloadCallback实例，用来处理下载结果
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static void DownLoadModels(File dir,DownloadCallback callback)
			throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Jiaju,
				callback);
		GUtilRunTools.getTools().DownLoadModels(tempFolder, handler);
	}

	/**
	 * 同步调用gutil，下载户型模型功能
	 * 
	 * @return 返回文件集合，集合内的文件为sweethome导入模型所需要的
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static File DownLoadRooms(File dir) throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Huxing);
		int exit_code = GUtilRunTools.getTools().DownLoadRooms(tempFolder);
		handler.onProcessComplete(exit_code);
		Iterator<File> i = handler.getResult().iterator();
		if (i.hasNext()) {
			return i.next();
		}
		return null;
	}

	/**
	 * 异步调用gutil，下载户型模型功能
	 * 
	 * @param callback
	 *            DownloadCallback实例，用来处理下载结果
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static void DownLoadRooms(File dir,DownloadCallback callback)
			throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Huxing,
				callback);
		GUtilRunTools.getTools().DownLoadRooms(tempFolder, handler);
	}
	
	/**
	 * 同步调用gutil，下载贴图功能
	 * 
	 * @return 返回一个图片文件
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static Collection<File> DownloadTexture(File dir) throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Tietu);
		int exit_code = GUtilRunTools.getTools().DownloadTexture(tempFolder);
		handler.onProcessComplete(exit_code);
		return handler.getResult();
	}

	/**
	 * 异步调用gutil，下载贴图功能
	 * 
	 * @param callback
	 *            DownloadCallback实例，用来处理下载结果
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static void DownloadTexture(File dir,DownloadCallback callback)
			throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Tietu,callback);
		GUtilRunTools.getTools().DownloadTexture(tempFolder, handler);
	}
	
	/**
	 * 同步调用gutil，通过id下载模型功能
	 * 
	 * @return 返回文件集合，集合内的文件为sweethome导入模型所需要的
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static Collection<File> DownLoadModels(File dir,String[] id) throws ExecuteException,
			IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Jiaju);
		int exit_code = GUtilRunTools.getTools().DownLoadModels(tempFolder,id);
		handler.onProcessComplete(exit_code);
		return handler.getResult();
	}

	/**
	 * 同步调用gutil，通过id下载贴图功能
	 * 
	 * @return 返回一个图片文件
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static File DownloadTexture(File dir,String[] id) throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Tietu);
		int exit_code = GUtilRunTools.getTools().DownloadTexture(tempFolder,id);
		handler.onProcessComplete(exit_code);
		Iterator<File> i = handler.getResult().iterator();
		if (i.hasNext()) {
			return i.next();
		}
		return null;
	}
	
	/**
	 * 同步调用gutil，上传渲染任务
	 * 
	 * @throws Exception
	 */
	public static void uploadRenderInfo(File dir) throws Exception {
		GUtilRunTools.getTools().uploadRenderInfo(dir.getAbsoluteFile());
	}

	/**
	 * 打开云端任务管理页面
	 * 
	 * @throws Exception
	 */
	public static void openJobManager() throws Exception {
		GUtilRunTools.getTools().openJobManager();
	}
	/**
	 * 打开用户中心页面
	 * 
	 * @throws Exception
	 */
	public static void openUserCenter() throws Exception {
		GUtilRunTools.getTools().openUserCenter();
	}
	/**
	 * 打开我的效果图页面
	 * 
	 * @throws Exception
	 */
	public static void openMyPreviewlib() throws Exception {
		GUtilRunTools.getTools().openMyPreviewlib();
	}
	/**
	 * 打开本地户型库
	 * @return 
	 * 
	 * @throws Exception
	 */
	public static File openLocalRoomlib(File dir) throws ExecuteException, IOException {
		File tempFolder = dir;
		DownloadResultHandler handler = new DownloadResultHandler(tempFolder,DownloadResultHandler.Huxing);
		int exit_code = GUtilRunTools.getTools().openLocalRoomlib(tempFolder);
		handler.onProcessComplete(exit_code);
		Iterator<File> i = handler.getResult().iterator();
		if (i.hasNext()) {
			return i.next();
		}
		return null;
	}
	public static String getUserProperty(String key){
	    String value = null;
	    value = GlueUtil.getUserProperties().get(key);
	    return value;
	}
	
	public static String getUserID(){
	    File xmlFile = GUtilRunTools.getTools().getGlueAuthFile();
	    AuthXmlHandler handler = new AuthXmlHandler();
	    try {
		if(xmlFile!=null && xmlFile.exists()){
		    XMLReader reader;
		    reader = XMLReaderFactory.createXMLReader();
		    reader.setContentHandler(handler);
		    FileInputStream input = new FileInputStream(xmlFile);
		    InputSource is = new InputSource(input);
		    reader.parse(is);
		    input.close();
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return handler.getResult().get(UserName);
	}

	public static Map<String, String> getUserProperties(){
	   String userID = getUserID();
	   File companyFile = GUtilRunTools.getTools().getGlueSessionFile(userID,CompanyJsonFile); 
	   HashMap<String, String> map = new JsonFileHandler(companyFile).getResult();
	   if(map!=null){
	       map.put(UserName, userID);
	   }
	   return map;
	}
	
	public static File getCompanyImage(){
	    File image = null;
	    Map<String, String> properties = getUserProperties();
	    String imageRelPath = properties.get(CompanyImagePath);		   
	    if(imageRelPath!=null){
		image = GUtilRunTools.getTools().getGlueSessionFile(properties.get(UserName),CompanyDir+File.separator+transformFilePath(imageRelPath));	  
	    }
	    return image;
	}
	
	public static String transformFilePath(String path){ 
	    String result = "";
	    if(path!=null){
		result = path.replace("/", File.separator).replace("\\", File.separator);
	    }
	    return result;
	}
	
	public static String getVersion(){
		return Version;
	}
	public static MappingResult getFurnitureByid(ArrayList<GContentObject> gcobj) {
		MappingResult list = new MappingResult();
		ArrayList<String> aljiaju = new ArrayList<String>();
		ArrayList<String> altietu = new ArrayList<String>();

		for (int i = 0; i < gcobj.size(); i++) {
			String id = gcobj.get(i).getId();
			int type = gcobj.get(i).getType();
			if (type == GlobalParameters.SOURCEOBJ_TYPE_FURNITURE) {
				aljiaju.add(id);
			} else if (type == GlobalParameters.SOURCEOBJ_TYPE_TEXTURE) {
				altietu.add(id);
			}
		}
		String[] jiajuarray = aljiaju.toArray(new String[aljiaju.size()]);
		String[] tietuarray = altietu.toArray(new String[altietu.size()]);
		HashMap<String, MappingResult> mrjiaju = GlueUtil
				.mappingJiaju(jiajuarray);
		HashMap<String, MappingResult> mrtietu = new  HashMap<String, MappingResult>(); 
		for(String tietuid:tietuarray){	    		
			String tpath = GlueUtil.getTextureDir().getAbsolutePath();
			MappingResult mr = new MappingResult(tietuid, tpath+File.separator+tietuid+".json", GlobalParameters.SOURCEOBJ_TYPE_TEXTURE);
			mr.setCached(false);
			mrtietu.put(tietuid, mr);
		}
		list.addMappingResultList(mrtietu);
		list.addMappingResultList(mrjiaju);
		return list;
	}
	
	public static String Encode(String s) {
		StringBuffer writer = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			writer.append('\\');
			writer.append('u');
			writer.append(Integer.toHexString((c >> 12) & 0xF));
			writer.append(Integer.toHexString((c >> 8) & 0xF));
			writer.append(Integer.toHexString((c >> 4) & 0xF));
			writer.append(Integer.toHexString(c & 0xF));
		}
		return writer.toString();
	}
}
