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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.RoomController;

/**
 * 自动摆放的对外暴漏使用接口
 * 
 * @date Oct 11, 2013
 */
public class SmartLayout {
	private CategoryToReferenceMap m_CategoryToReferenceMap;
	private LaytouToCalculateMap m_LaytouToCalculateMap;
	private final Home home;
	private final UserPreferences preferences;
	private final ContentManager contentManager;
	
	private Logger log = Logger.getLogger("SmartLayout-log");

	public SmartLayout(Home h, UserPreferences p, ContentManager c) {
		// 初始化这个映射关系的时候可以从配置文件读取也可以通过代码
		this.home = h;
		this.preferences = p;
		this.contentManager = c;
		m_CategoryToReferenceMap = new CategoryToReferenceMap(home);
		m_LaytouToCalculateMap = new LaytouToCalculateMap();
	}

	/**
	 * 几所所指定的模型在挡墙户型中的所有合适位置
	 * 
	 * @param so
	 *            所要摆放的模型
	 * @return 所有的目标位置信息
	 */
	public GResult autoLayout(SourceObject so) {
		// 获取主分类(墙,天花板,地面);
		GCategory category = so.getCategory();
		// 特性,灯的瓦数,是否缩放,固定大小
		LayoutFeature sLayoutFeature = so.getLayoutFeature();
		// 获取对应此布局特性的计算对象
		GCalculate gc = null;
		try {
			gc = m_LaytouToCalculateMap.getCalculate(sLayoutFeature);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// TODO 这里是需要一个for循环来不断的传入单个的参照物还是直接传入所有?
		// 获取这个分类所对应的所要摆放的目标对象
		ReferenceObject ro = null;
		try {
			ro = m_CategoryToReferenceMap.getReferenceMap(category);
			if (ro ==null) {
				log.log(Level.INFO, "获取模型参照物出错，模型不支持自动摆放");
				return null;
			}
			ro.setRoomContrller(this.m_RoomController);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 给定参照物,以及 所要摆放的物体
		GResult result = gc.calculate(ro, so);
		return result;
	}
	private RoomController m_RoomController;

	/**
	 * @param roomController
	 */
	public void setRoomController(RoomController roomController) {
		m_RoomController = roomController;
	}

	public RoomController getRoomController() {
		return this.m_RoomController;
	}

}
