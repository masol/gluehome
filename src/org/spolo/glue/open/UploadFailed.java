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
package org.spolo.glue.open;

/**
 * @date Jul 28, 2014
 */
public class UploadFailed extends GErrorHandler{
	// 根据需要通过构造函数来传递所需要的对象
	// 然后根据所返回的错误信息来做不同的处理
	@Override
	public void error(String msg) {
		if(msg.endsWith("0")){
		
		}else if(msg.endsWith("1")){
			
		}else if(msg.endsWith("2")){
			
		}else if(msg.endsWith("3")){
			
		}else if(msg.endsWith("4")){
			
		}
	}

}
