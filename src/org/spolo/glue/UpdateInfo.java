package org.spolo.glue;

public class UpdateInfo {

	private String svn_revision;
	private String build_time;
	private String version;
	private boolean force_update;
	
	public String getSvn_revision() {
		return svn_revision;
	}
	public void setSvn_revision(String svn_revision) {
		this.svn_revision = svn_revision;
	}
	
	public String getBuild_time() {
		return build_time;
	}
	public void setBuild_time(String build_time) {
		this.build_time = build_time;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isForce_update() {
		return force_update;
	}
	public void setForce_update(boolean force_update) {
		this.force_update = force_update;
	}
}
