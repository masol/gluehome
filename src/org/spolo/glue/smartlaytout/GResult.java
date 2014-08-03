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

import java.util.LinkedList;

/**
 * 一个物体经过计算之后的位置信息
 * 
 * @date Oct 11, 2013
 */
public class GResult {
	private LinkedList<GPosition> positions = null;

	public GResult() {
		positions = new LinkedList<GPosition>();
	}

	public void addPosition(GPosition p) {
		this.positions.add(p);
	}

	public LinkedList<GPosition> getPositions() {
		return this.positions;
	}

	public GPosition getFirstPosition() {
		return this.positions.peekFirst();
	}
}
