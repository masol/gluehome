package org.spolo.glue;

import java.util.ArrayList;

import org.sunflow.util.FloatArray;

public class RenderInfoBean {
	
	private ArrayList<RenderCameraBean> cameras;
	private ArrayList<RenderFurnitureBean> models;
	private ArrayList<RenderTexturesBean> materials;
	
	public ArrayList<RenderFurnitureBean> getRenderFurnitures() {
		return models;
	}
	public void setRenderFurnitures(ArrayList<RenderFurnitureBean> models) {
		this.models = models;
	}
	
	public ArrayList<RenderTexturesBean> getRenderTextures() {
		return materials;
	}
	public void setRenderTextures(ArrayList<RenderTexturesBean> materials) {
		this.materials = materials;
	}
	
	public ArrayList<RenderCameraBean> getRenderCameras() {
		return cameras;
	}
	public void setRenderCameras(ArrayList<RenderCameraBean> cameras) {
		this.cameras = cameras;
	}
	
	
	
}
