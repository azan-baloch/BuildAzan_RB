package com.buildazan.helper;

public class Message {
	private String message;
	private String cssClass;
	
	public Message(String message, String cssClass) {
		this.message = message;
		this.cssClass = cssClass;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	

}
