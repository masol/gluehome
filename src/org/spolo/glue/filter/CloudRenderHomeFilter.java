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

/**
 * 提交渲染任务的时候对当前场景的所有模型做过滤,将户型中的模型id还原回普通id,同时增加一个户型id
 *
 * @date Jul 31, 2013
 */
public class CloudRenderHomeFilter implements GFilter {
	private boolean homeid = false;

	@Override
	public void doFilter(GRequest req, GResponse res, GFilter chain) {
		Home home = (Home) req.getParameter();
		List<HomePieceOfFurniture> oldFurnitures = home.getFurniture();

		for (HomePieceOfFurniture nf : oldFurnitures) {
			String id = nf.getCatalogId();
			// 如果id值中带apartment字样,就说明是需要过滤掉的
			if (id != null && id.contains("apartment")) {
				// String newid = id.substring(0, id.indexOf("_"));
				// 然后增加一个户型id,也就是.sh3d文件名
				if (!homeid) {
					homeid = true;
					// 重新构造一个户型模型
					HomePieceOfFurniture homemodel = nf.clone();
					// 模型标示
					homemodel.setCatalogId(home.getHomeid());
					homemodel.setName("wall");
					// 模型位置信息
					homemodel.setX(0);
					homemodel.setY(0);
					homemodel.setElevation(0);
					// 水平旋转角度
					homemodel.setAngle(0);
					// 模型尺寸信息
					homemodel.setWidth(0);
					homemodel.setDepth(0);
					homemodel.setHeight(0);
					// 设置缩放系数为1
					homemodel.setWidthScale(1);
					homemodel.setDepthScale(1);
					homemodel.setHeightScale(1);
					home.addPieceOfFurniture(homemodel);
				}
				// home.deletePieceOfFurniture(nf);
				// 将所有的apartment的标记去掉,还原成云端模型id
				// id = id.replace("apartment_", "");
				// f.setCatalogId(id);
			}
		}
		res.setResponse(home);
	}
}
