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
package org.spolo.glue.OutputDebugInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//有关io的一些方法，会慢慢把有用到io的地方都放在这个类里方便管理
public class IoUtil {
	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		return bartDateFormat.format(date);
	}
}