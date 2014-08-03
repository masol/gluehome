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
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

/**
 * 导出户型的时候对当前场景中的所有模型增加户型标志 apartment
 * 
 * @date Jul 30, 2013
 */
public class ExportCloudHomeModelFilter {

	public void doFilter(GRequest req, GResponse res, GFilter chain) {
		Home home = (Home) req.getParameter();
		// 过滤掉Home中所有模型的id
		List<HomePieceOfFurniture> furnitures = home.getFurniture();
		for (HomePieceOfFurniture furniture : furnitures) {
			String old = furniture.getCatalogId();
			furniture.setMovable(false);
			if (old != null) {
				if (!old.contains("apartment")) {
					 furniture.setCatalogId("apartment_" + old);
					 System.out.println("apartment_" + old);
				}
			} else {
				System.out
						.println("[ERROR]:  \u5B58\u5728\u4E8E\u4E91\u7AEF\u4E0D\u540C\u6B65\u7684\u6A21\u578B(\u6CA1\u6709ID)");
			}
		}
		home.setBasePlanLocked(true);
		// 这里可以将homeid设置为空?TODO
		// home.setHomeid("");
		res.setResponse(home);
	}

}
