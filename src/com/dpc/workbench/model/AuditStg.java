package com.dpc.workbench.model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DIM_AUDIT_STG")
public class AuditStg implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int auditSid;
	private String userId;
	private String fileName;
	private String agencyName;
	private String description;
	private int year;
	private int quarter;
	private String status;
//	private Timestamp auditTimeStamp;
	private int currentRecordFlag;
	private Date activeFromDate;
	private Date activeToDate;
	private int workflowExecId;
	private int dateSid;

	public AuditStg() {
	// A No-Args constructor is mandatory for Hibernate to work:
	// ---------------------------------------------------------
	// 1.HibernateUtil loads
	// 2.That forces the static to run buildSessionFactory
	// 3.Hibernate instantiates POJO(s) (e.g. AuditStg.java) via no-args
	// 4.That in turn calls HibernateUtil
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="audit_sid", unique=true ,nullable=false)
	public int getAuditSid() {
		return auditSid;
	}
	@Column(name="userid")
	public String getUserId() {
		return userId;
	}
	@Column(name="filename")
	public String getFileName() {
		return fileName;
	}
	@Column(name="agencyname")
	public String getAgencyName() {
		return agencyName;
	}
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	@Column(name="year")
	public int getYear() {
		return year;
	}
	@Column(name="quarter")
	public int getQuarter() {
		return quarter;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}
//	@Column(name="audit_timestamp")
//	public Date getAuditTimeStamp() {
//		return auditTimeStamp;
//	}
	@Column(name="current_record_flag")
	public int getCurrentRecordFlag() {
		return currentRecordFlag;
	}
	@Column(name="active_from_date")
	public Date getActiveFromDate() {
		return activeFromDate;
	}
	@Column(name="active_to_date")
	public Date getActiveToDate() {
		return activeToDate;
	}
	@Column(name="workflow_exec_id")
	public int getWorkflowExecId() {
		return workflowExecId;
	}
	@Column(name="date_sid")
	public int getDateSid() {
		return dateSid;
	}

	public void setAuditSid(int auditSid) {
		this.auditSid = auditSid;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public void setStatus(String status) {
		this.status = status;
	}
//	public void setAuditTimeStamp(Date auditTimeStamp) {
//		this.auditTimeStamp = auditTimeStamp;
//	}
	public void setCurrentRecordFlag(int currentRecordFlag) {
		this.currentRecordFlag = currentRecordFlag;
	}
	public void setActiveFromDate(Date activeFromDate) {
		this.activeFromDate = activeFromDate;
	}
	public void setActiveToDate(Date activeToDate) {
		this.activeToDate = activeToDate;
	}
	public void setWorkflowExecId(int workflowExecId) {
		this.workflowExecId = workflowExecId;
	}
	public void setDateSid(int dateSid) {
		this.dateSid = dateSid;
	}
}