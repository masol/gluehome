package org.spolo.glue;

import org.omg.CORBA.PRIVATE_MEMBER;

public class RenderCameraBean {
	
	private String name;
	private float x;
	private float y;
	private float z;
	private float pitch;
	private float yaw;
	private float fieldOfView;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}

}
