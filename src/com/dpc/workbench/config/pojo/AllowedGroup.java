package com.dpc.workbench.config.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("allowedGroup") // maps "allowedGroup" element in XML to this class
public class AllowedGroup {
	@XStreamAlias("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
