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
package org.spolo.glue.filter;

import java.util.List;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeFurnitureGroup;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Level;

/**
 * �ύ��Ⱦ�����ʱ��Ե�ǰ����������ģ��������,�������е�ģ��id��ԭ����ͨid,ͬʱ����һ������id
 * 
 * @date Jul 31, 2013
 */
public class GroupFurnitureFilter implements GFilter {

	@Override
	public void doFilter(GRequest req, GResponse res, GFilter chain) {
		Home home = (Home) req.getParameter();
		List<HomePieceOfFurniture> oldFurnitures = home.getFurniture();
		for (HomePieceOfFurniture f : oldFurnitures) {
			if (f instanceof HomeFurnitureGroup) {
				Level level = f.getLevel();
				List<HomePieceOfFurniture> furnitureWithinGroups = HomeFurnitureGroup
						.getFurnitureWithoutGroups(((HomeFurnitureGroup) f)
								.getFurniture());
				home.deletePieceOfFurniture(f);
				for(HomePieceOfFurniture fg : furnitureWithinGroups){
					Level level2 = null;
					if (level != null) {
						level2 = new Level(level.getName(),
								level.getElevation(),
								level.getFloorThickness(), level.getHeight());
					}
					home.addPieceOfFurniture(fg);
					fg.setLevel(level2);
				}
			}
		}
		res.setResponse(home);
	}
}
