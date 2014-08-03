package org.spolo.glue.exception;

public class GlueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String info = null;
	
	public GlueException() {
		// TODO Auto-generated constructor stub
	}
	
	public GlueException(String msg) {
		super(msg);
	}
	
	
}
