package org.spolo.glue;

import org.sunflow.util.FloatArray;

public class RenderFurnitureBean {
	
	private String id;
	private String name;
	private float angle;
	private float x;
	private float y;
	private float z;
	private String scale; //缩放不知道用不用的到，暂时先写上。
	private float width;
	private float height;
	private float depth;
	private boolean doorOrWindow;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getDepth() {
		return depth;
	}
	public void setDepth(float depth) {
		this.depth = depth;
	}
	
	public boolean isDoorOrWindow() {
		return doorOrWindow;
	}
	public void setDoorOrWindow(boolean doorOrWindow) {
		this.doorOrWindow = doorOrWindow;
	}
}
