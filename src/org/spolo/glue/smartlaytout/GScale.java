package org.spolo.glue.smartlaytout;



public class GScale {
	/**
	 * 该类用于保存和处理模型的width、depth、height
	 */
	private float w, d, h;

	public GScale(float w, float d, float h) {
			this.w = w;
			this.d = d;
			this.h = h;

		}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}
	
	public void setScale(GScale s){
		this.w = s.w;
		this.d = s.d;
		this.h = s.h;
	}

}
