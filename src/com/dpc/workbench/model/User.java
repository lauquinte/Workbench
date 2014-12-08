package com.dpc.workbench.model;

public class User {

	private String username;
	private boolean isAdmin;
	private String securityGroup;
	
	public User() {
		//No-args constructor
	}
	
	public User(String username) {
		setUsername(username);
		setIsAdmin(false);
		setSecurityGroup("");
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}
	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}
}
