package com.dpc.workbench.action;

import java.util.List;

import com.dpc.workbench.controller.AuditStgManager;
import com.dpc.workbench.model.AuditStg;

import com.opensymphony.xwork2.ActionSupport;

public class AuditStgAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private AuditStg auditStg;
	private List<AuditStg> auditStgList;
	private int auditSid;
	private AuditStgManager auditStgManager;
	
	public AuditStgAction() {
		auditStgManager =  new AuditStgManager();
	}
	
	public String execute() {
		try {
			this.auditStgList = auditStgManager.list();
	        System.out.println("execute called");
			System.out.println(auditStgList);
			System.out.println(auditStgList.size());
	        return "success";
		} finally {
		}
	}
	
	public String addAuditStg() {
		System.out.println(getAuditStg());
		try {
			auditStgManager.add(getAuditStg());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public AuditStg getAuditStg() {
		return auditStg;
	}
	public void setAuditStg(AuditStg auditStg) {
		this.auditStg = auditStg;
	}

	public List<AuditStg> getAuditStgList() {
		return auditStgList;
	}
	public void setAuditStgList(List<AuditStg> auditStgList) {
		this.auditStgList = auditStgList;
	}
	
	public int getAuditSId() {
		return auditSid;
	}
	public void setAuditSId(int auditSid) {
		this.auditSid = auditSid;
	}
}