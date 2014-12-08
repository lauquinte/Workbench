package com.dpc.workbench.config.pojo;

import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.dpc.workbench.config.pojo.SubAgency;

@XStreamAlias("cluster") // maps "cluster" element in XML to this class
public class Agency {

	private String name;
	private String securityGroup;
	private String pathForUpload;
	private String pathForBatchFile;
	@XStreamAlias("subagencies")
	private List<SubAgency> subagencies = new ArrayList<SubAgency>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSecurityGroup() {
		return securityGroup;
	}
	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
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
	
	public List<SubAgency> getSubagencies() {
		return subagencies;
	}
	public void setSubagencies(List<SubAgency> subagencies) {
		this.subagencies = subagencies;
	}

}
