package org.spolo.glue;

import java.util.ArrayList;
import java.util.Map;

public class FurnitureInfo {
	private String price;
	private String resizable;
	private String category;
	private String type;
	private String selftype;
	private String feature;
	private ArrayList<Map> furnitureinfo = new ArrayList<Map>();

//	public float getPrice() {
//		return Float.parseFloat(price);
//	}
//	public void setPiece(String piece) {
//		this.price = piece;
//	}
	public String getCludeCategory() {
		return category;
	}
	public void setCludeCategory(String cludeCategory) {
		this.category = cludeCategory;
	}
	public boolean isResizable() {
		return Boolean.parseBoolean(resizable);
	}
	public void setResizable(String resizable) {
		this.resizable = resizable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Map> getFurnitureinfo() {
		return furnitureinfo;
	}
	public void setFurnitureinfo(ArrayList<Map> furnitureinfo) {
		this.furnitureinfo = furnitureinfo;
	}
	public String getSelfType() {
		return selftype;
	}
	public void setSelfType(String selfType) {
		this.selftype = selfType;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}


}
