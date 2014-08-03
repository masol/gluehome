package org.spolo.glue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonFileHandler {
    HashMap<String, String> map = new HashMap<String, String>();
    public JsonFileHandler(File file){
	if(file.exists()){ 
	    try {
		 String json_str = FileUtils.readFileToString(file, "utf-8");
		 Gson gson = new Gson();
		 TypeToken<HashMap<String, String>> json_type_token = new TypeToken<HashMap<String, String>>() {};
		 map = gson.fromJson(json_str, json_type_token.getType());
	    } catch (IOException e) {
	    	System.out.println(file.getAbsolutePath()+" : "+e.getMessage());
	    }
	}
    }
    
    public HashMap<String, String> getResult(){
	return map;
    }
}
