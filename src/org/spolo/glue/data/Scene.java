package org.spolo.glue.data;

import java.util.List;
import java.util.Map;

public class Scene {
	private List<Map<String, String>> models;
	private List<Map<String, String>> materials;
	private List<Map<String, String>> housepics;
	private String projectname;
	private String id;
	
	public List<Map<String, String>> getModels() {
		return models;
	}
	public void setModels(List<Map<String, String>> models) {
		this.models = models;
	}
	
	public List<Map<String, String>> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Map<String, String>> materials) {
		this.materials = materials;
	}
	
	public List<Map<String, String>> getHousepics() {
		return housepics;
	}
	public void setHousepics(List<Map<String, String>> housepics) {
		this.housepics = housepics;
	}
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
