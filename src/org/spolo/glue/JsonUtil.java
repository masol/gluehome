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
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	static Gson gson = new Gson();

	public static TextureBean getJsonFile(File file) {
		// File jsonFile = new
		// File("C:/Users/Administrator/Desktop/newjson.json");
		TextureBean json = null;
		try {
			String json_str = FileUtils.readFileToString(file, "utf-8");
			TypeToken<HashMap<String, HashMap<String, String>>> list = new TypeToken<HashMap<String, HashMap<String, String>>>() {
			};
			json = gson.fromJson(json_str, TextureBean.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T> Object readJson(File json, Class<T> c) {
		String json_str = null;
		if (json != null) {
			try {
				json_str = FileUtils.readFileToString(json, "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gson.fromJson(json_str, c);
	}
	/**
	 * 把一个对象变成json格式,然后写入到指定的文件中.
	 * @param c
	 * @param f
	 */
	public static void writeObjectToFile(Object c, File f) throws Exception {
		String json_str = gson.toJson(c);
			FileUtils.writeStringToFile(f, json_str, "utf-8");
	}
}
