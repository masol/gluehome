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
public class RandomCalculate implements GCalculate {
	@Override
	/**
	 * 踢脚线的自动摆放算法。
	 */
	public GResult calculate(ReferenceObject ro, SourceObject so) {
		// TODO 通过home中能够获取的信息计算这个物体的目标位置,可以任意缩放
		Home home = ro.getHome();
		PieceOfFurniture furniture = so.getHomePieceOfFurniture();
		RoomController rc = ro.getRoomController();
	    List<Selectable> oldSelection = home.getSelectedItems(); 
	    List<Room> selectedRoom = Home.getRoomsSubList(oldSelection);
	    GResult g = new GResult();
//	    if(!selectedRoom.isEmpty()){
	    	List<WallSide> wallSides = rc.getRoomsWallSides(selectedRoom, null);

	    	if(furniture!=null){
	    		float height = furniture.getHeight();
	    		float depth = furniture.getDepth();
	    		// TODO 计算SourceObject的所有目标位置
	    		processWallAtStartAndEnd(wallSides, furniture, g);
	
	    		for (WallSide wallSide : wallSides) {
	    			if(wallSide.getWall().getArcExtent() == null || wallSide.getWall().getArcExtent() == 0){
	    				
	    				processWallWithDoorOrWindow(home, wallSide, furniture, g);
	    				
//	    				double furnitureAngle = 0;
//	    				if(wallSide.getSide() == 0){
//	    					try {
//	    						furnitureAngle = Math.PI - wallSide.getWall().getWallAngle();
//	    					} catch (GlueException e) {
//	    						// TODO Auto-generated catch block
//	    					}
//	    				}else{
//	    					try {
//	    						furnitureAngle = 0 - wallSide.getWall().getWallAngle();
//	    					} catch (GlueException e) {
//	    						// TODO Auto-generated catch block
//	    					}
//	    				}
//	    				float[][] wallPoints = 	wallSide.getWallSidePoints();
//	    				float x = (float) ((wallPoints[0][0] + wallPoints[1][0]) / 2 - depth/2 * Math.sin(furnitureAngle)) ;
//	    				float y = (float) ((wallPoints[0][1] + wallPoints[1][1]) / 2 + depth/2 * Math.cos(furnitureAngle));
//	    				float z = 0;
//	    				float furnitureWidth = (float) Math.sqrt(Math.pow((wallPoints[1][0]-wallPoints[0][0]), 2)+Math.pow((wallPoints[1][1]-wallPoints[0][1]), 2));
//	    				float furnitureDepth = depth;
//	    				float furnitureheight = height;
//	    				
//	    				GLocation gl = new GLocation(x, y, z);
//	    				GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureheight);
//	    				GRotation r = new GRotation(furnitureAngle);
//	    				GPosition p = new GPosition();
//	    				PieceOfFurniture f = furniture;
//	    				p.setLocation(gl);
//	    				p.setRotation(r);
//	    				p.setScale(s);
//	    				p.setFurniture(f);
//	    				g.addPosition(p);
	    				}
	    				else{
	    					//内弧面好说，外弧面不好说
	    					float arc = wallSide.getWall().getArcExtent();
	    					System.out.println(arc);
	    					//以圆心角为10度进行摆放踢脚线。
	    					float preArc = (float) (((5) * Math.PI)/180); 
	    					//向上取整，计算踢脚线个数
	    					int furnitureNum = 0;
	    					float[][] points = wallSide.getWall().getUnjoinedShapePoints();
	    					float xStart = 0;
	    					float yStart = 0;
	    					if(wallSide.getSide() == 0){
	    						if(arc < 0){
	    							xStart = points[0][0];
	    							yStart = points[0][1];
	    						}else{
	    							xStart = points[points.length/2-1][0];
			    					yStart = points[points.length/2-1][1];
	    						}
	    					}else{
	    						if(arc < 0){
	    							xStart = points[points.length-1][0];
	    							yStart = points[points.length-1][1];
	    						}else{
	    							xStart = points[points.length/2][0];
			    					yStart = points[points.length/2][1];
			    					
	    						}
	    						
	    					}
	    					if(arc > 0){
	    						furnitureNum = (int) Math.rint(arc/preArc); 
	    					}else{
	    						furnitureNum = (int) Math.rint((-arc)/preArc);
	    					}
	    					float ext = arc % preArc;
	    					float arcCircleX = wallSide.getWall().getXArcCircleCenter();
	    					float arcCircleY = wallSide.getWall().getYArcCircleCenter();
	    					
	    					float arcRadius = (float)Point2D.distance(xStart, yStart, 
	    							arcCircleX, arcCircleY);
	    					float xEnd = 0;
	    					float yEnd = 0;
	    					double furnitureAngle = 0;
	    					for(int i = 0; i < furnitureNum; i++){
	    						//计算踢脚线另一点的坐标
	    						
	    						
	    						xEnd = (float) ((xStart - arcCircleX) * Math.cos(preArc) + 
	    								(yStart - arcCircleY) * Math.sin(preArc) + arcCircleX);
	    						yEnd = (float) ((yStart - arcCircleY) * Math.cos(preArc) - 
	    								(xStart - arcCircleX) * Math.sin(preArc) + arcCircleY);
	    						
	    						//计算踢脚线角度
	    						float furnitureWidth = (float) Point2D.distance(xStart, yStart, xEnd, yEnd);
	    						float angle = (float) Math.acos((xEnd - xStart)/furnitureWidth);
	    						if(yEnd - yStart > 0){
	    							angle = -angle;
	    						}
	    					
	    						if(wallSide.getSide() == 0){
	    							if(arc > 0){
	    								furnitureAngle = Math.PI - angle + Math.PI;
	    							}else{
	    								furnitureAngle = Math.PI - angle;
	    							}
	    						}else{
	    							if(arc > 0){
	    								furnitureAngle = 0 - angle + Math.PI;
	    							}else{
	    								furnitureAngle = 0 - angle;
	    							}
	    						}
	    						float x = (float) ((xStart + xEnd) / 2 - depth/2 * Math.sin(furnitureAngle)) ;
	    						float y = (float) ((yStart + yEnd) / 2 + depth/2 * Math.cos(furnitureAngle));
	    						float z = 0;
	    						
	    						float furnitureDepth = depth;
	    						float furnitureheight = height;
	    						
	    						GLocation gl = new GLocation(x, y, z);
	    						GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureheight);
	    						GRotation r = new GRotation(furnitureAngle);
	    						GPosition p = new GPosition();
	    						PieceOfFurniture f = furniture;
	    						p.setLocation(gl);
	    						p.setRotation(r);
	    						p.setScale(s);
	    						p.setFurniture(f);
	    						g.addPosition(p);
	    						
	    						xStart = xEnd;
	    						yStart = yEnd;
	    				}
	    			}
	    		}
	    	}
	    return g;
	}
	
	/**
	 * 已知线段起始点坐标(xs,ys)和(xe,ye)，线段上未知点(x,y)，与起点之间距离为dis，线段与X轴的夹角为angle，计算未知点坐标。
	 * @param xs
	 * @param ys
	 * @param xe
	 * @param ye
	 * @param dis
	 * @param angle
	 * @return
	 */
	public float[] calPoint(float xs, float ys, float xe, float ye, float dis, float angle) {
		
		float x = 0;
		float y = 0;
		float[] point = new float[2];
		
		float xDiff = (float) (dis *  Math.sqrt((1 - Math.pow(Math.sin(angle), 2))));
		float yDiff = (float) (dis * Math.sqrt(Math.pow(Math.sin(angle), 2)));
		
		float[] xTemp = new float[2];
		float[] yTemp = new float[2];
		
		xTemp[0] = formatData(xs + xDiff, 3);
		xTemp[1] = formatData(xs - xDiff, 3);
		yTemp[0] = formatData(ys + yDiff, 3);
		yTemp[1] = formatData(ys - yDiff, 3);
		xs = formatData(xs, 3);
		ys = formatData(ys, 3);
		
		
		if (xs >= xe) {
			for (int i = 0; i < xTemp.length; i++) {
				if (xTemp[i] >= xe && xTemp[i] <= xs) {
					x = xTemp[i];
				}
			}
		}else {
			for (int i = 0; i < xTemp.length; i++) {
				if (xTemp[i] <= xe && xTemp[i] >= xs) {
					x = xTemp[i];
				}
			}
		}
		
		if (ys >= ye) {
			for (int i = 0; i < yTemp.length; i++) {
				if (yTemp[i] >= ye && yTemp[i] <= ys) {
					y = yTemp[i];
				}
			}
		}else {
			for (int i = 0; i < yTemp.length; i++) {
				if (yTemp[i] <= ye && yTemp[i] >= ys) {
					y = yTemp[i];
				}
			}
		}
		
		point[0] = x;
		point[1] = y;

		return point;
	}
	
	
	/**
	 * 确定求得的点的具体值
	 * @param wall
	 * @param xDif
	 * @param yDif
	 * @param side  墙体的终点还是起点 , 0代表起点，1代表终点。
	 * @return 求得的最终的模型摆放位置。
	 */
	public float [] proRange(Wall wall, float xDif, float yDif, int side) {
		
		float [] point = new float[2];
		float xStart = wall.getXStart();
		float yStart = wall.getYStart();
		float xEnd = wall.getXEnd();
		float yEnd = wall.getYEnd(); 
		float [][] points = {{xDif,yDif},{-xDif, -yDif},{-xDif, yDif},{xDif, -yDif}};
		float[][] pointSuc = new float[2][2];
		int pointNum = 0;
		float [] dis = new float[2];
		
		for (int i = 0; i < points.length; i++) {
			try {
				if (formatData(points[i][1]/points[i][0], 3) == formatData((float)Math.tan(wall.getWallAngle()), 3) 
						&& points[i][0] != 0) {
					pointSuc[pointNum][0] = points[i][0];
					pointSuc[pointNum][1] = points[i][1];
					pointNum++;
				}
			} catch (GlueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (pointNum != 2) {
			return null;
		}else {
			if (side == 0) {

				for (int i = 0; i < pointSuc.length; i++) {
					dis[i] = (float) Point2D.distance(xEnd, yEnd, pointSuc[i][0]-xStart, pointSuc[i][1]-yStart);
				}

				if (dis[0] >= dis[1]) {
					point[0] = pointSuc[0][0] + xStart;
					point[1] = pointSuc[0][1] + yStart;
				}else {
					point[0] = pointSuc[1][0] + xStart;
					point[1] = pointSuc[1][1] + yStart;
				}
				
			}else {
				
				for (int i = 0; i < pointSuc.length; i++) {
					dis[i] = (float) Point2D.distance(xStart, yStart, pointSuc[i][0]-xEnd, pointSuc[i][1]-yEnd);
				}

				if (dis[0] >= dis[1]) {
					point[0] = pointSuc[0][0] + xEnd;
					point[1] = pointSuc[0][1] + yEnd;
				}else {
					point[0] = pointSuc[1][0] + xEnd;
					point[1] = pointSuc[1][1] + yEnd;
				}
			}
		}
		return point;
	}
	
	/**
	 * 处理墙体打洞的踢脚线摆放。
	 * @param home
	 * @param wallSide
	 * @param furniture
	 * @param g
	 */
	
	public void processWallWithDoorOrWindow(Home home, WallSide wallSide, PieceOfFurniture furniture, GResult g) {
		float height = furniture.getHeight();
		float depth = furniture.getDepth();
		float [][] wallAllPoints = wallSide.getWall().getPoints();
		for (int i = 0; i < wallAllPoints.length; i++) {
			wallAllPoints[i][0] = formatData(wallAllPoints[i][0], 2);
			wallAllPoints[i][1] = formatData(wallAllPoints[i][1], 2);
		}
		Shape wallShape = getShape(wallAllPoints);
//		Area wallArea = new Area(wallShape);
		
		int pieceNum = 0;  //用于记录墙体中门或窗的个数
		List<HomePieceOfFurniture> pieceInWall = new ArrayList<HomePieceOfFurniture>();   //用于记录墙体中的具体的门或窗
		Wall wall = wallSide.getWall();
		for (HomePieceOfFurniture piece : getVisibleDoorsAndWindows(home.getFurniture())) {
			
			//TODO 这里没有很好的办法来判断doorOrWindow是否在墙体中，需要自己实现一个
			//		暂时先通过判断dooeOrWindow的点都在wall中来实现。
			float [][] points = piece.getPoints();
			for (int i = 0; i < points.length; i++) {
				points[i][0] = formatData(points[i][0], 2);
				points[i][1] = formatData(points[i][1], 2);
				
			}
			Shape pieceShape = getShape(points);
			Rectangle2D pieceBounds = pieceShape.getBounds2D();
			
			//这里直接通过判断墙体是否与doorOrWindow相交来判断该墙体上是否有doorOrWindow
			if (wallShape.contains(pieceBounds) && piece.getElevation() < furniture.getHeight()) { 
				pieceNum++;
				pieceInWall.add(piece);
			}
		}
		if (pieceNum > 0) {
			HomePieceOfFurniture [] piecesArray = new HomePieceOfFurniture[pieceInWall.size()];
			for (int i = 0; i < pieceInWall.size(); i++) {
				piecesArray[i] = pieceInWall.get(i);
			}
			//对一堵墙上所有的门或窗进行排序。按照距离墙体起点的距离进行排序。
			if (pieceNum > 1) {
				for (int i = 0; i < piecesArray.length-1; i++) {
					for (int j = i+1; j < piecesArray.length; j++) {
						if (pieceToStartDistance(wallSide, piecesArray[i]) 
								> pieceToStartDistance(wallSide, piecesArray[j])) {
							HomePieceOfFurniture tempPiece = piecesArray[i];
							piecesArray[i] = piecesArray[j];
							piecesArray[j] = tempPiece;
						}
					}
				}
			}
			
			float[][] points = new float[(piecesArray.length + 1) * 2][]; 
			float[][] wallPoints = 	wallSide.getWallSidePoints();
			
			float xStart = wallPoints[0][0];
			float yStart = wallPoints[0][1];
			float xEnd = wallPoints[1][0];
			float yEnd = wallPoints[1][1];
			float wallAngle = 0;
			try {
				wallAngle = wall.getWallAngle();
			} catch (GlueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			points[0] = new float[]{xStart, yStart};
			points[piecesArray.length*2 + 1] = new float[]{xEnd, yEnd};
			
			
			for (int i = 0; i < piecesArray.length; i++) {
				float pieceDistance = pieceToStartDistance(wallSide, piecesArray[i]);
				float pieceHalfWidth = piecesArray[i].getWidth()/2;
				
				// 暂时先考虑最简单的情况，其他情况比较多，一种情况没有问题后再去处理其他情况。
				// TODO 考虑其他情况的摆放。
				
				//求解该墙面上门或窗底的两个点的坐标
				
				points[i * 2 + 1] = calPoint(xStart, yStart, xEnd, yEnd, pieceDistance - pieceHalfWidth, wallAngle);
				points[(i + 1) *2] = calPoint(xStart, yStart, xEnd, yEnd, pieceDistance + pieceHalfWidth, wallAngle);
				
//				if (i == 0) {
//					if (pieceDistance > pieceHalfWidth) {
//						float furnitureWidth = pieceDistance - pieceHalfWidth;
//					}else {
//						
//					}
//				}
			}

			//TODO 如果doorOrWindow在墙体中需要记录下来，根据doorOrWindow的个数各种属性来摆放踢脚线。
			//需要的点的坐标都已经保存在points数组中
			
			for (int i = 0; i < points.length; i = i + 2) {
				
				double furnitureAngle = 0;
				if(wallSide.getSide() == 0){
					try {
						furnitureAngle = Math.PI - wallSide.getWall().getWallAngle();
					} catch (GlueException e) {
						// TODO Auto-generated catch block
					}
				}else{
					try {
						furnitureAngle = 0 - wallSide.getWall().getWallAngle();
					} catch (GlueException e) {
						// TODO Auto-generated catch block
					}
				}
//				float[][] wallPoints = 	wallSide.getWallSidePoints();
				float x = (float) ((points[i][0] + points[i+1][0]) / 2 - depth/2 * Math.sin(furnitureAngle)) ;
				float y = (float) ((points[i][1] + points[i+1][1]) / 2 + depth/2 * Math.cos(furnitureAngle));
				float z = 0;
				float furnitureWidth = (float) Math.sqrt(Math.pow((points[i + 1][0]-points[i][0]), 2)+Math.pow((points[i + 1][1]-points[i][1]), 2));
				float furnitureDepth = depth;
				float furnitureheight = height;
				
				GLocation gl = new GLocation(x, y, z);
				GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureheight);
				GRotation r = new GRotation(furnitureAngle);
				GPosition p = new GPosition();
				PieceOfFurniture f = furniture;
				p.setLocation(gl);
				p.setRotation(r);
				p.setScale(s);
				p.setFurniture(f);
				g.addPosition(p);
				
			}
		}else {
			double furnitureAngle = 0;
			if(wallSide.getSide() == 0){
				try {
					furnitureAngle = Math.PI - wallSide.getWall().getWallAngle();
				} catch (GlueException e) {
					// TODO Auto-generated catch block
				}
			}else{
				try {
					furnitureAngle = 0 - wallSide.getWall().getWallAngle();
				} catch (GlueException e) {
					// TODO Auto-generated catch block
				}
			}
			float[][] wallPoints = 	wallSide.getWallSidePoints();
			float x = (float) ((wallPoints[0][0] + wallPoints[1][0]) / 2 - depth/2 * Math.sin(furnitureAngle)) ;
			float y = (float) ((wallPoints[0][1] + wallPoints[1][1]) / 2 + depth/2 * Math.cos(furnitureAngle));
			float z = 0;
			float furnitureWidth = (float) Math.sqrt(Math.pow((wallPoints[1][0]-wallPoints[0][0]), 2)+Math.pow((wallPoints[1][1]-wallPoints[0][1]), 2));
			float furnitureDepth = depth;
			float furnitureheight = height;
			
			GLocation gl = new GLocation(x, y, z);
			GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureheight);
			GRotation r = new GRotation(furnitureAngle);
			GPosition p = new GPosition();
			PieceOfFurniture f = furniture;
			p.setLocation(gl);
			p.setRotation(r);
			p.setScale(s);
			p.setFurniture(f);
			g.addPosition(p);
			
		}
	}
	
	/**
	 * 处理墙体断面的踢脚线摆放
	 * @param home 
	 * @param piece 踢脚线模型
	 * @param g 摆放结果
	 */
	public void processWallAtStartAndEnd(List<WallSide> wallSides, PieceOfFurniture piece, GResult g) {
		List<Wall> walls = new ArrayList<Wall>();
		List<Wall> tempWalls = new ArrayList<Wall>();
		
		for (int i = 0; i < wallSides.size(); i++) {
			walls.add(wallSides.get(i).getWall());
		}
		
		Iterator<Wall> iteratorWall = walls.iterator();
		while (iteratorWall.hasNext()) {
			Wall wall = (Wall) iteratorWall.next();
			if (tempWalls.contains(wall)) {
				iteratorWall.remove();
			}else {
				tempWalls.add(wall);
			}
		}
		
		for (int i = 0; i < walls.size(); i++) {
			Wall curWall = walls.get(i);
			try {
				double furnitureAngle = 0;
				float [] furniturePoint = new float[2];
				float z = 0;
				float furnitureWidth = curWall.getThickness()+piece.getDepth();
				float furnitureDepth = piece.getDepth();
				float furnitureHeight = piece.getHeight();

				float xStart = formatData(curWall.getXStart(), 3);
				float yStart = formatData(curWall.getYStart(), 3);
				float xEnd = formatData(curWall.getXEnd(), 3);
				float yEnd = formatData(curWall.getYEnd(), 3);

				/**
				 * 已知点为 (x1,y1),求解点为(x,y);
				 * 已知两点间距离--1/2墙体厚度，两点连线与X轴夹角
				 * 定义求解方程需要的变量，
				 * xDif = x1-x;
				 * yDif = y1-y;
				 * a为两点之间距离，a = furnitureDepth/2;
				 * b为两点线段与X轴角度正切值， b = Math.tan(curWall.getWallAngle());
				 * 
				 *  根据二元二次方程组求解（x,y）的值
				 *  XDif^2 + yDif^2 = a^2
				 *  yDif/xDif = b
				 *  
				 *  结果应该为两组值，需要确定哪组才是自己想要的值。
				 */

				float dis = furnitureDepth/2;
				float wallAngle = curWall.getWallAngle();
				
				boolean isJoinedAtStart = false;
				boolean isJoinedAtEnd = false;
			
				//确定墙体的断面的位置，是在起点还是终点
				for (int j = 0; j < walls.size(); j++) {
					if (i != j) {
						Wall wall = walls.get(j);

						if (formatData(wall.getXStart(), 3) == xStart && formatData(wall.getYStart(), 3) == yStart 
								|| formatData(wall.getXEnd(), 3) == xStart && formatData(wall.getYEnd(), 3) == yStart) {
							isJoinedAtStart = true;
						}
						if (formatData(wall.getXStart(), 3) == xEnd && formatData(wall.getYStart(), 3) == yEnd
								||formatData(wall.getXEnd(), 3) == xEnd && formatData(wall.getYEnd(), 3) == yEnd) {
							isJoinedAtEnd = true;
						}
					}
				}

				if (!isJoinedAtStart) {
					furnitureAngle = Math.PI/2 - curWall.getWallAngle();
					
					furniturePoint = calPoint(xStart, yStart, xEnd, yEnd, dis, wallAngle);
					furniturePoint[0] = xStart * 2 - furniturePoint[0];
					furniturePoint[1] = yStart * 2 - furniturePoint[1];

					if (furniturePoint != null) {
						GLocation gl = new GLocation(furniturePoint[0], furniturePoint[1], z);
						GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureHeight);
						GRotation r = new GRotation(furnitureAngle);
						GPosition p = new GPosition();
						PieceOfFurniture f = piece;
						p.setLocation(gl);
						p.setRotation(r);
						p.setScale(s);
						p.setFurniture(f);
						g.addPosition(p);
					}
				}
				if (!isJoinedAtEnd) {
					furnitureAngle = Math.PI*3/2 - curWall.getWallAngle();
					
					furniturePoint = calPoint(xEnd, yEnd, xStart, yStart, dis, wallAngle);
					furniturePoint[0] = xEnd * 2 - furniturePoint[0];
					furniturePoint[1] = yEnd * 2 - furniturePoint[1];
						
//							xDif = (float) Math.sqrt(Math.pow(a, 2)/(1 + Math.pow(b, 2)));
//							yDif = b * xDif;
//								
//							furniturePoint = proRange(curWall, xDif, yDif, 1);
						
					if (furniturePoint != null) {
						GLocation gl = new GLocation(furniturePoint[0], furniturePoint[1], z);
						GScale s = new GScale(furnitureWidth, furnitureDepth, furnitureHeight);
						GRotation r = new GRotation(furnitureAngle);
						GPosition p = new GPosition();
						PieceOfFurniture f = piece;
						p.setLocation(gl);
						p.setRotation(r);
						p.setScale(s);
						p.setFurniture(f);
						g.addPosition(p);
					}
				}

			} catch (GlueException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		}
	}
	
	 /**
	  * 保留小数位数
	  * @param data 需要处理的数据
	  * @param figure 保留的小数位数
	  * @return 处理后的输出。
	  */
	  public float formatData(float data, int figure) {
		
		  BigDecimal f = new BigDecimal(data);
		  float fData = f.setScale(figure, BigDecimal.ROUND_HALF_UP).floatValue();
		  return fData;
	}
	  
	  /**
	   * Returns the points of one of the side of this wall. 
	   */
	  private float [][] getWallSidePoints(WallSide wallSide) {
	    Wall wall = wallSide.getWall();
	    float [][] wallPoints = wall.getPoints();
	    
	    if (wallSide.getSide() == 0) {
	      for (int i = wallPoints.length / 2; i < wallPoints.length; i++) {
	        wallPoints [i][0] = (wallPoints [i][0] + wallPoints [wallPoints.length - i - 1][0]) / 2;
	        wallPoints [i][1] = (wallPoints [i][1] + wallPoints [wallPoints.length - i - 1][1]) / 2;
	      }
	    } else { // WALL_RIGHT_SIDE
	      for (int i = 0, n = wallPoints.length / 2; i < n; i++) {
	        wallPoints [i][0] = (wallPoints [i][0] + wallPoints [wallPoints.length - i - 1][0]) / 2;
	        wallPoints [i][1] = (wallPoints [i][1] + wallPoints [wallPoints.length - i - 1][1]) / 2;
	      }
	    }
	    return wallPoints;
	  }
	  
	  /**
	   * Returns the shape matching the coordinates in <code>points</code> array.
	   */
	  protected Shape getShape(float [][] points) {
	    GeneralPath path = new GeneralPath();
	    path.moveTo(points [0][0], points [0][1]);
	    for (int i = 1; i < points.length; i++) {
	      path.lineTo(points [i][0], points [i][1]);
	    }
	    path.closePath();
	    return path;
	  }
	  
	  /**
	   * Returns all the visible doors and windows in the given <code>furniture</code>.  
	   */
	  private List<HomePieceOfFurniture> getVisibleDoorsAndWindows(List<HomePieceOfFurniture> furniture) {
	    List<HomePieceOfFurniture> visibleDoorsAndWindows = new ArrayList<HomePieceOfFurniture>(furniture.size());
	    for (HomePieceOfFurniture piece : furniture) {
	      if (piece.isVisible()) {
	        if (piece instanceof HomeFurnitureGroup) {
	          visibleDoorsAndWindows.addAll(getVisibleDoorsAndWindows(((HomeFurnitureGroup)piece).getFurniture()));
	        } else if (piece.isDoorOrWindow()) {
	          visibleDoorsAndWindows.add(piece);
	        }
	      }
	    }
	    return visibleDoorsAndWindows;
	  }
	  	  
	  public float pieceToStartDistance(WallSide wallSide, HomePieceOfFurniture piece) {
		  float[][] wallSidePoints = wallSide.getWallSidePoints();
		  float xStart = wallSidePoints[0][0];
		  float yStart = wallSidePoints[0][1];
		  float pieceX = piece.getX();
		  float pieceY = piece.getY();
		  return  (float)Point2D.distance(xStart, yStart, 
				  pieceX, pieceY);
		
	}
	 
	  
}
