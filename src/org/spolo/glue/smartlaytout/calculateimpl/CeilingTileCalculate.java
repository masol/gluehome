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
package org.spolo.glue.smartlaytout.calculateimpl;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spolo.glue.exception.GlueException;
import org.spolo.glue.smartlaytout.GCalculate;
import org.spolo.glue.smartlaytout.GLocation;
import org.spolo.glue.smartlaytout.GPosition;
import org.spolo.glue.smartlaytout.GResult;
import org.spolo.glue.smartlaytout.GRotation;
import org.spolo.glue.smartlaytout.GScale;
import org.spolo.glue.smartlaytout.ReferenceObject;
import org.spolo.glue.smartlaytout.SourceObject;

import com.eteks.sweethome3d.model.Compass;
import com.eteks.sweethome3d.model.DimensionLine;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomeFurnitureGroup;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Label;
import com.eteks.sweethome3d.model.Level;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Sash;
import com.eteks.sweethome3d.model.Selectable;
import com.eteks.sweethome3d.model.TextStyle;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.swing.PlanComponent;
import com.eteks.sweethome3d.viewcontroller.RoomController;
import com.eteks.sweethome3d.viewcontroller.RoomController.WallSide;

/**
 * 可以根据指定的参照物任意计算尺寸和缩放比例以及旋转角度的计算方式
 * 
 * @date Oct 15, 2013
 */
public class CeilingTileCalculate implements GCalculate {
	@Override
	/**
	 * 平铺天花板的自动摆放算法。
	 */
	public GResult calculate(ReferenceObject ro, SourceObject so) {
		// TODO 通过home中能够获取的信息计算这个物体的目标位置,可以任意缩放
		Home home = ro.getHome();
		PieceOfFurniture furniture = so.getHomePieceOfFurniture();
		RoomController rc = ro.getRoomController();
	    List<Selectable> oldSelection = home.getSelectedItems(); 
	    List<Room> selectedRoom = Home.getRoomsSubList(oldSelection);
	    GResult g = new GResult();
	    
	    for (Room room : selectedRoom) {
	    	Rectangle2D roomBounds = room.getShape().getBounds2D();
	    	float roomBoundsWidth = (float) roomBounds.getWidth();
	    	float roomBoundsHeight = (float) roomBounds.getHeight();
	    	float roomBoundsX = (float) roomBounds.getX();
	    	float roomBoundsY = (float) roomBounds.getY();
	    	// 这里天花板的高度先写死为270cm，算是比较大众的数，自动摆放完后进行打组，然后让用户自己去调节高度。
	    	float ceilingHeight = 270;
	    	float furnitureWidth = furniture.getWidth();
	    	float furnitureDepth = furniture.getDepth();
	    	float furnitureHeight = furniture.getHeight();
	    	int countX = (int) Math.ceil(roomBoundsWidth/furnitureWidth);
	    	int countY = (int) Math.ceil(roomBoundsHeight/furnitureDepth);
	    	
	    	float originPointX = roomBoundsX + furnitureWidth/2;
	    	float originPointY = roomBoundsY + furnitureDepth/2;
	    	for (int i = 0; i < countX; i++) {
				for (int j = 0; j < countY; j++) {
					float x = originPointX + i * furnitureDepth;
					float y = originPointY + j * furnitureWidth;
					
					GLocation gl = new GLocation(x, y, ceilingHeight);
					GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureHeight);
					GRotation r = new GRotation(0);
					GPosition p = new GPosition();
					PieceOfFurniture f = furniture;
					p.setLocation(gl);
					p.setRotation(r);
					p.setScale(s);
					p.setFurniture(f);
					g.addPosition(p);
					
				}
			}
		}
	    return g;
	}
}
