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

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.viewcontroller.RoomController;

/**
 * 参考物的抽象基类
 * 
 * @date Oct 10, 2013
 */
public abstract class ReferenceObject implements Cloneable {
	protected Home m_Home;
	protected RoomController m_RoomController;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @param m_Home
	 */
	public void setHome(Home home) {
		this.m_Home = home;
	}

	public Home getHome() {
		return this.m_Home;
	}

	/**
	 * @param RoomController
	 */
	public void setRoomContrller(RoomController roomController) {
		this.m_RoomController = roomController;
	}

	public RoomController getRoomController() {
		return this.m_RoomController;
	}

}
