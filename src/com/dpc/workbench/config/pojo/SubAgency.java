package com.dpc.workbench.config.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("subagency") // maps "subagency" element in XML to this class
public class SubAgency {
	@XStreamAlias("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
