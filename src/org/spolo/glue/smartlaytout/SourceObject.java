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

import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.PieceOfFurniture;

/**
 * 目标物
 * 
 * @date Oct 11, 2013
 */
/**
 * @date Oct 16, 2013
 */
public abstract class SourceObject {
	protected GCategory m_category;
	protected LayoutFeature m_layoutfeature;
	protected PieceOfFurniture m_HomePieceOfFurniture;
	protected HomeTexture m_homtexture;
	protected HomeTexture m_FloorTexture;
	protected HomeTexture m_CeilingTexture;

	public SourceObject(GCategory category, LayoutFeature layoutfeature) {
		this.m_category = category;
		this.m_layoutfeature = layoutfeature;
	}

	/**
	 * 获取分类信息
	 * 
	 * @return
	 */
	public GCategory getCategory() {
		return this.m_category;
	}

	/**
	 * 获取此物品布局时的一些额外属性,例如固定大小,存在一些缩放级别,或者按照参照物的大小来自动计算
	 * 
	 * @return
	 */
	public LayoutFeature getLayoutFeature() {
		return this.m_layoutfeature;
	}

	public PieceOfFurniture getHomePieceOfFurniture() {
		return m_HomePieceOfFurniture;
	}

	public void setHomePieceOfFurniture(PieceOfFurniture homePieceOfFurniture) {
		this.m_HomePieceOfFurniture = homePieceOfFurniture;
	}

	/**
	 * X轴长度
	 * 
	 * @return
	 */
	public float getWidth() {
		return 0;

	}

	/**
	 * Y轴长度
	 * 
	 * @return
	 */
	public float getDepth() {
		return 0;
	}

	/**
	 * 高度
	 * 
	 * @return
	 */
	public float getHeight() {
		return 0;
	}

	/**
	 * @param homtexture
	 */
	public void setHomeTexture(HomeTexture homtexture) {
		this.m_homtexture = homtexture;
	}

	public HomeTexture getHomeTexture() {
		return this.m_homtexture;
	}
	
	/**
	 * @param homtexture
	 */
	public void setFloorTexture(HomeTexture floortexture) {
		this.m_FloorTexture = floortexture;
	}

	public HomeTexture getFloorTexture() {
		return this.m_FloorTexture;
	}
	
	/**
	 * @param homtexture
	 */
	public void setCeilingTexture(HomeTexture ceilingtexture) {
		this.m_CeilingTexture = ceilingtexture;
	}

	public HomeTexture getCeilingTexture() {
		return this.m_CeilingTexture;
	}
	
}
