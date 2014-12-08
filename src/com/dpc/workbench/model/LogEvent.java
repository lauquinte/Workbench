package com.dpc.workbench.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOG_EVENT")
public class LogEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private int logEventId;
	private int severityId;
	private int jobExecId;
	private String messageText;

	public LogEvent() {
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="log_event_id")
	public int getLogEventId() {
		return logEventId;
	}
	public void setLogEventId(int logEventId) {
		this.logEventId = logEventId;
	}
	
	@Column(name="severity_id")
	public int getSeverityId() {
		return severityId;
	}
	public void setSeverityId(int severityId) {
		this.severityId = severityId;
	}
	
	@Column(name="job_exec_id")
	public int getJobExecId() {
		return jobExecId;
	}
	public void setJobExecId(int jobExecId) {
		this.jobExecId = jobExecId;
	}
	
	@Column(name="message_text")
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}
