package com.dpc.workbench.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import com.dpc.workbench.config.Configuration;
import com.dpc.workbench.controller.AuditStgManager;
import com.dpc.workbench.controller.LogEventManager;
import com.dpc.workbench.model.AuditStg;
import com.dpc.workbench.model.LogEvent;
import com.dpc.workbench.util.ExecuteBatchProcess;
import com.dpc.workbench.util.WorkbenchUtils;
import com.opensymphony.xwork2.ActionSupport;

public class EtlValidationAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionAttributes = null;
	private String url = null;
	private LogEvent log;
	private List<LogEvent> logs;
	
	public LogEvent getLog() {
		return log;
	}
	public void setLog(LogEvent log) {
		this.log = log;
	}

	public List<LogEvent> getLogs() {
		return logs;
	}
	public void setLogs(List<LogEvent> logs) {
		this.logs = logs;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public EtlValidationAction() {
	}

	public String execute() throws Exception, IOException, HibernateException, FileNotFoundException {

		int exitCode = -1;
		AuditStg audit = new AuditStg();
		String filePathBatch = null;
		String param = null;

		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("Begin - Retreive values from session");
		
		// If save to DB was successful, execute batch file with
		// the auto-generated @AUDIT_SID value as input parameter
		if (sessionAttributes.containsKey("AUDIT")) {
			audit = (AuditStg)sessionAttributes.get("AUDIT");
		}
		if (sessionAttributes.containsKey("filePathBatch")) {
			filePathBatch = (String)sessionAttributes.get("filePathBatch");
		}
		if (sessionAttributes.containsKey("selectedDropDownValue")) {
			param = (String)sessionAttributes.get("selectedDropDownValue");
		}

		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("End - Retreive values from session");

		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("Begin - Execute Data Load Validation");

		ExecuteBatchProcess p = new ExecuteBatchProcess();
		exitCode = p.executeBatchFile(filePathBatch, String.valueOf(audit.getAuditSid()));

		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("End - Execute Data Load Validation");
		
		// Evaluate batch execution return code
		if (exitCode == 0) {
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("Begin - Display result screen");

			audit = readJobIdStatusFromDB(audit.getAuditSid());
			int jobId = audit.getWorkflowExecId(); 	//344;
			String status = audit.getStatus(); 		//"ERROR";
			String url = null;
			
			System.out.println("Batch File executed Successfully!");
			addActionMessage("Data Load Validation has been executed with status '" + status.toUpperCase() + "'");

			// Read Invalid or Error Url from properties file
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("global.properties");
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file global.properties not found in the classpath.");
			}
			
			if (status.equalsIgnoreCase("ERROR")) {
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("Begin - ERROR status logging");
				
				// Read logs from ETL Framework DB
				// Redirect to next screen to display status/error
				// messages from DataServices Job execution
				setLogs(readLogRecordsFromDB(jobId));

				// Show "Support Link" Url to folder in SAP BOBJ server
				url = Configuration.getInstance().getBaseUrl();
				url = url.concat(prop.getProperty("msg.support.url"));
				setUrl(url);

				System.out.println("Support Link: " + url);

				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("End - ERROR status logging");
			}
			
			if (status.equalsIgnoreCase("INVALID")) {
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("Begin - INVALID status Redirect to R013");
				
				// Show report R013 with a list of errors forcing a URL redirect
				url = Configuration.getInstance().getBaseUrl();
				url = url.concat(prop.getProperty("msg.invalid.url"));
				
				if (!(param.equals(null) || param.equals(" ") || param.isEmpty())) {
					param = param.replace(' ', '+');
					url = url.concat(param);
					setUrl(url);

					System.out.println("R013 Link: " + url);
					
					// Write to Tomcat's log
					WorkbenchUtils.writeToLog("End - INVALID status Redirect to R013");

					// Force URL redirect via Struts2 action of type 'Redirect'
					return "redirect";
				}
			}
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Display result screen");

			// Display result screen
			return "success";
			
		} else {
			System.out.println("Batch File execution Failed!");
			addActionError("Data Load validation failed!");
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Execute Data Load Validation");

			return "error";
		}
	}
	
	@Override
	public void setSession(Map<String, Object> sessionAttr) {
		this.sessionAttributes = sessionAttr;
	}

	public AuditStg readJobIdStatusFromDB(int audit_sid) throws HibernateException {
		
		AuditStgManager auditDAO;
		auditDAO = new AuditStgManager();
		AuditStg auditStg = new AuditStg();

		try {
			auditStg = auditDAO.getAuditStg(audit_sid);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return auditStg;
	}
	
	public List<LogEvent> readLogRecordsFromDB(int jobExecId) {
		
		List<LogEvent> logs = null;
		LogEventManager logDAO = new LogEventManager();
		
		try {
			logs = (List<LogEvent>) logDAO.list(jobExecId, 2);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return logs;
	}

}
