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
package org.spolo.glue.smartlaytout;

import com.eteks.sweethome3d.model.PieceOfFurniture;

/**
 * 一个物体的位置对象,能够通过这个位置对象获取到这个物体的所有位置信息 例如起始点,终点,中心点,旋转等等
 * 
 * @date Oct 10, 2013
 */
/**
 * @date Oct 16, 2013
 */
public class GPosition {
	
	private GLocation m_Location;
	private GScale m_Scale;
	private GRotation m_Rotation;
	private PieceOfFurniture m_PieceOfFurniture =null;

	public GLocation getLocation() {
		return m_Location;
	}

	public void setLocation(GLocation m_Location) {
		this.m_Location = m_Location;
	}

	public GScale getScale() {
		return m_Scale;
	}

	public void setScale(GScale m_Scale) {
		this.m_Scale = m_Scale;
	}

	public GRotation getRotation() {
		return m_Rotation;
	}

	public void setRotation(GRotation m_Rotation) {
		this.m_Rotation = m_Rotation;
	}
	
	public PieceOfFurniture getFurniture(){
		return m_PieceOfFurniture;
	}
	
	public void setFurniture(PieceOfFurniture mPieceOfFurniture){
		this.m_PieceOfFurniture = mPieceOfFurniture;
	}
	
	

}
