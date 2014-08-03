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
 * @date Jul 23, 2014
 */
public class GWorkerFarmer {
	private static GWorkerFarmer farmer = null;

	private GWorkerFarmer() {
	};

	/**
	 * 向一个异步的任务队列中添加一个任务,在将来的某个时刻所添加的任务会被执行到
	 * 
	 * @param work
	 * @throws WorkFramerException
	 */
	public void addWork(GWork work) throws Exception {
		// TODO
	}

	public static void doWork(GWork work) throws Exception {
		if (farmer == null) {
			farmer = new GWorkerFarmer();
		}
		farmer.excute(work);
	}

	private void excute(final GWork work) {
		new Thread(new Runnable() {
			public void run() {
				try {
					work.doWork();
				} catch (Exception e) {					
					GErrorHandler handler = work.getErrorHandler();
					if(handler!=null){
						handler.setException(e);
						handler.error(e.getMessage());
					}					
				}
			}
		}).start();
	}
}
