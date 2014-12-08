package com.dpc.workbench.config.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("masterdata") // maps "masterdata" element in XML to this class
public class Masterdata {

	private String name;
	private String pathForUpload;
	private String pathForBatchFile;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPathForUpload() {
		return pathForUpload;
	}
	public void setPathForUpload(String pathForUpload) {
		this.pathForUpload = pathForUpload;
	}
	
	public String getPathForBatchFile() {
		return pathForBatchFile;
	}
	public void setPathForBatchFile(String pathForBatchFile) {
		this.pathForBatchFile = pathForBatchFile;
	}
	
}
