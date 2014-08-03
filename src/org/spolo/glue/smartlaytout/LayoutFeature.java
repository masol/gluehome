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

import java.util.EnumSet;

/**
 * 此物品的布局特性,例如固定大小,可以任意缩放,按面积摆放...
 * 
 * @date Oct 11, 2013
 */
public class LayoutFeature {
	// TODO 这里需要一个枚举
	public enum Feature {
		RandomCalculate("RandomCalculate"), 
		CeilingTileCalculate("CeilingTileCalculate"),
		Primeval("PrimevalCalculate");
		private String m_name;

		private Feature(String name) {
			this.m_name = name;
		}

		public String getName() {
			return this.m_name;
		}

		/**
		 * @return
		 * @throws Exception
		 */
		public GCalculate getCaculate() throws Exception {
			Class ref = Class
					.forName("org.spolo.glue.smartlaytout.calculateimpl."
							+ this.m_name);
			GCalculate refobj = (GCalculate) ref.newInstance();
			return refobj;
		}

		/**
		 * @param feature_str
		 * @return
		 */
		public static Feature formString(String feature_str) {
			for (Feature feature : EnumSet.range(Feature.RandomCalculate,
					Feature.Primeval)) {
				if (feature_str.compareTo(feature.getName()) == 0) {
					return feature;
				}
			}
			return null;
		}

	}

	private Feature m_Feature;

	public LayoutFeature(Feature f) {
		this.m_Feature = f;
	}

	public String getName() {
		return this.m_Feature.getName();
	}

	public Feature getFeatureEnum() {
		return this.m_Feature;
	}
}
