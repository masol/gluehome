package org.spolo.glue.smartlaytout;

public class GLocation {
	/**
	 * 给类用于保存模型模型坐标
	 */
	private float x, y, elevation;

	public GLocation(float x, float y, float elevation) {
		this.x = x;
		this.y = y;
		this.elevation = elevation;
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

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	
}
