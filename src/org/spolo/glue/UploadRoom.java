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
package org.spolo.glue;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;

import com.eteks.sweethome3d.viewcontroller.GlueController;
/**
 * @date 2014-3-14
 */
public class UploadRoom extends GUtilCommand {
	private Logger log = Logger.getLogger("UploadRoom");

	@Override
	public CommandLine getCommandLine() {
		this.url = MappingURL.getUploaRoomURL();
		CommandLine cmd = getBaseCommandLine(url);
		String srl = System.getProperty("sroomlowpoly");
		String rl = System.getProperty("roomlowpoly");
		String df = System.getProperty("decfile");
		String gw = System.getProperty("gworkdir");
		File roompoly = new File(rl);
		
		

		DescriptionForGutil dfg = new DescriptionForGutil();
		ArrayList<DescriptionElement> description = new ArrayList<DescriptionElement>();
		
//		if (srl != null) {
			File sroompoly = new File(srl);
			DescriptionElement sroom = new DescriptionElement();
			sroom.setName(sroompoly.getName());
			sroom.setPath(sroompoly.getAbsolutePath());
			sroom.setType("sroom");
			sroom.setSelftype("sroomlowpoly");
			sroom.setHomeid(GlueController.homeidCache);
			
			description.add(sroom);
//		}

		DescriptionElement room = new DescriptionElement();
		room.setName(roompoly.getName());
		room.setPath(roompoly.getAbsolutePath());
		room.setType("room");
		room.setSelftype("roomlowpoly");
		room.setHomeid(GlueController.homeidCache);
		if (df != null) {
			File decfile = new File(df);
			DescriptionElement decoration = new DescriptionElement();
			decoration.setName(decfile.getName());
			decoration.setPath(decfile.getAbsolutePath());
			decoration.setType("decoration");
			decoration.setSelftype("decoration");
			description.add(decoration);
		}
		description.add(room);

		dfg.setDescription(description);
		File descriptionfile = new File(roompoly.getParent() + File.separator
				+ "description.json");
		try {
			JsonUtil.writeObjectToFile(dfg, descriptionfile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		cmd.addArgument("--dir=" + gw);
		cmd.addArgument("--file=" + descriptionfile.getName());

		return cmd;
	}

	@Override
	public GutilResult createSuccessResult(int exitcode) throws Exception {
		log.log(Level.INFO, "upload room and decoration " + exitcode);
		return null;
	}

	@Override
	public GutilResult createFailedResult(Exception exception) throws Exception {
		log.log(Level.INFO,
				"upload room and decoration " + exception.getMessage());
		return null;
	}

}
