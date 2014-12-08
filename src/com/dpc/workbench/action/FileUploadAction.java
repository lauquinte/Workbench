package com.dpc.workbench.action;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.SessionAware;
import com.dpc.workbench.config.Configuration;
import com.dpc.workbench.config.pojo.Agency;
import com.dpc.workbench.config.pojo.Masterdata;
import com.dpc.workbench.config.pojo.SubAgency;
import com.dpc.workbench.controller.AuditStgManager;
import com.dpc.workbench.controller.DateStgManager;
import com.dpc.workbench.model.AuditStg;
import com.dpc.workbench.model.DateStg;
import com.dpc.workbench.model.LogEvent;
import com.dpc.workbench.util.WorkbenchUtils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.csv.*;
import org.hibernate.HibernateException;

public class FileUploadAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionAttributes = null;

	private File uploadDoc;
	private String uploadDocContentType;
	private String uploadDocFileName;

	private String loggedUser;
	private String fiscalYearQuarter;
	private int fiscalYear;
	private int fiscalQuarter;
	private String url = null;
	
	private String dataRadioButton = "";
	private String defaultRadioButton = "";
	
	private List<String> dataList = new ArrayList<String>();
	private String selectedData = "";

	private List<String> dataList2 = new ArrayList<String>();
	private String selectedData2 = "";

	private List<String> fyqList = new ArrayList<String>();
	private String selectedFYQ = "";

	private LogEvent log;
	private List<LogEvent> logs;

	public FileUploadAction() {
	}

	public File getUploadDoc() {
		return uploadDoc;
	}

	public void setUploadDoc(File uploadDoc) {
		this.uploadDoc = uploadDoc;
	}

	public String getUploadDocContentType() {
		return uploadDocContentType;
	}

	public void setUploadDocContentType(String uploadDocContentType) {
		this.uploadDocContentType = uploadDocContentType;
	}

	public String getUploadDocFileName() {
		return uploadDocFileName;
	}

	public void setUploadDocFileName(String uploadDocFileName) {
		this.uploadDocFileName = uploadDocFileName;
	}

	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public int getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(int fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public int getFiscalQuarter() {
		return fiscalQuarter;
	}

	public void setFiscalQuarter(int fiscalQuarter) {
		this.fiscalQuarter = fiscalQuarter;
	}

	public String getFiscalYearQuarter() {
		return fiscalYearQuarter;
	}

	public void setFiscalYearQuarter(String fiscalYearQuarter) {
		this.fiscalYearQuarter = fiscalYearQuarter;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataRadioButton() {
		return dataRadioButton;
	}
	public void setDataRadioButton(String dataRadioButton) {
		this.dataRadioButton = dataRadioButton;
	}

	public String getDefaultRadioButton() {
		return defaultRadioButton;
	}
	public void setDefaultRadioButton(String defaultRadioButton) {
		this.defaultRadioButton = defaultRadioButton;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public String getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(String selectedData) {
		this.selectedData = selectedData;
	}

	public List<String> getDataList2() {
		return dataList2;
	}
	public void setDataList2(List<String> dataList2) {
		this.dataList2 = dataList2;
	}

	public String getSelectedData2() {
		return selectedData2;
	}
	public void setSelectedData2(String selectedData2) {
		this.selectedData2 = selectedData2;
	}

	public List<String> getFyqList() {
		return fyqList;
	}
	public void setFyqList(List<String> fyqList) {
		this.fyqList = fyqList;
	}

	public String getSelectedFYQ() {
		return selectedFYQ;
	}
	public void setSelectedFYQ(String selectedFYQ) {
		this.selectedFYQ = selectedFYQ;
	}

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

	@Override
	public void setSession(Map<String, Object> sessionAttr) {
		this.sessionAttributes = sessionAttr;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	public String loadData() {

		List<Agency> agencies = null;
		List<Masterdata> masterdata = null;
		boolean isAdmin = false;
		String userGroup = null;
		int count = 0;
		List<String> lst = new ArrayList<String>();
		List<String> fyq = new ArrayList<String>();
		DateStg dateStg = new DateStg();

		// Read DIM_DATE_STG values for today's date in order to populate
		// DATE_SID, FISCAL_YEAR and FISCAL_QUARTER fields in DIM_AUDIT_STG table
		// Get today's date:
		Calendar lCal = Calendar.getInstance();
		lCal.setTime(new java.util.Date());

		// Split date into day, month, year:
		int year = lCal.get(Calendar.YEAR);
		int month = lCal.get(Calendar.MONTH) + 1;
		int day = lCal.get(Calendar.DATE);

		// If we come from a successful upload and want to perform a new upload
		// we check if session attributes exists to avoid querying the DB again
		if (!sessionAttributes.containsKey("DateSid")
				&& !sessionAttributes.containsKey("Today")) {
			// Use day, month, year to read the record from DIM_DATE_STG table
			// (select single)
			// to make sure we are populating a valid value in fields DATE_SID,
			// FISCAL_YEAR and FISCAL_QUARTER of DIM_AUDIT_STG table:
			dateStg = readDateRecordFromDB(year, month, day);

			// Set DateSid and Today's date values in the session
			sessionAttributes.put("DateSid", dateStg.getDateSid());
			// Set Today's date in SQL Date format to use it for DB operation
			sessionAttributes.put("Today", new java.sql.Date(year - 1900, month - 1, day));
		}

		// Check if isAdmin value is already stored in the session
		if (sessionAttributes.containsKey("isAdmin")) {
			isAdmin = (Boolean) sessionAttributes.get("isAdmin");
		}
		// Check if groupUser value is already stored in the session
		if (sessionAttributes.containsKey("userGroup")) {
			userGroup = (String) sessionAttributes.get("userGroup");
		}

		if (isAdmin) { // Fill Masterdata list
			// We set the default value of RadioButton
			setDefaultRadioButton("Masterdata");
			
			if (getDataRadioButton().equals("") || getDataRadioButton() == null) {
				// If the current RadioButton value is empty, 
				// we populate it with the default value
				setDataRadioButton(getDefaultRadioButton());
			}
			
			masterdata = Configuration.getInstance().getMasterdataList();
			count = masterdata.size();
			
			// Fill generic list to be rendered in the screen
			for (int i = 0; i < count; i++) {
				lst.add(masterdata.get(i).getName());
			}
			
			// Fill agency list
			List<String> lst2 = new ArrayList<String>();
			agencies = Configuration.getInstance().getAgencies();
			count = agencies.size();

			// Fill generic list to be rendered in the screen
			for (int i = 0; i < count; i++) {
				// We display all Sub-Agencies to the list 
				// as we are running as a Workbench Administrator
				List<SubAgency> subAgencies = agencies.get(i).getSubagencies();

				for (int j = 0; j < subAgencies.size(); j++) {
					lst2.add(subAgencies.get(j).getName());
				}
			}
			setDataList2(lst2);
			
			int calendarYearFrom = Integer.parseInt(Configuration.getInstance().getStartingCalendarYear());
			List<String> dateStgs = readFYQfromDB(calendarYearFrom, year);

			for (int i = 0; i < dateStgs.size(); i++) {
				fyq.add(dateStgs.get(i).toString());
			}
			setFyqList(fyq);

		} else { // Fill Agencies list
			agencies = Configuration.getInstance().getAgencies();
			count = agencies.size();
			
			// Fill generic list to be rendered in the screen
			for (int i = 0; i < count; i++) {
				// We loop through each Agency (Cluster) and check its securityGroup
				// If the securityGroup == userGroup (stored in the session)
				// We add the corresponding Sub-Agency to the list 
				// that will be rendered to the screen
				if (agencies.get(i).getSecurityGroup().equals(userGroup)) {
					List<SubAgency> subAgencies = agencies.get(i).getSubagencies();

					for (int j = 0; j < subAgencies.size(); j++) {
						lst.add(subAgencies.get(j).getName());
					}
					break;
				}
			}

			// Set App's FY-FQ to the previous one
			int fyear, fquarter;

			// If we come back from a successful upload and want to perform another upload
			// we check that FY and FQ exist to avoid deriving their values again
			if ( sessionAttributes.containsKey("FiscalYear") && 
				 sessionAttributes.containsKey("FiscalQuarter") ) {
			
				fyear = (Integer)sessionAttributes.get("FiscalYear");
				fquarter = (Integer)sessionAttributes.get("FiscalQuarter");

			} else {
				
				fyear = dateStg.getFinancialYearId();
				fquarter = dateStg.getFinancialQuarterId() - 1;

				if (fquarter == 0) {
					fyear--;
					fquarter = 4;
				}

				// Save Fiscal Year, Fiscal Quarter, DateSID 
				// and Today's date values in the session
				sessionAttributes.put("FiscalYear", fyear);
				sessionAttributes.put("FiscalQuarter", fquarter);
			}

			// Set Fiscal Year and Fiscal Quarter for the view layer
			// i.e. '2014 - 2015 Q2'
			String strFYQ = Integer.toString(year);				// i.e. 2014
			strFYQ = strFYQ.concat(" - ")						
						   .concat(Integer.toString(fyear))		// i.e. 2015
						   .concat(" Q")
						   .concat(Integer.toString(fquarter));	// i.e. 2

			setFiscalYear(fyear);
			setFiscalQuarter(fquarter);
			setFiscalYearQuarter(strFYQ);
		}

		// Set the list for the view layer
		setDataList(lst);

		// Now, the list is available and initialized for rendering
		// as well as fiscal year and fiscal quarter
		return "success";
	}

	public String execute() throws Exception, IOException, HibernateException {

		String filePathUpload = "";
		String filePathBatch = "";
		String fileNameMask = "";
		String description = "";
		String name = "";
		boolean isAdmin = false;
		Masterdata masterdata = null;
		
		try {
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("Begin - Retrieve selected parameters' values");
			
			// Retrieve app properties
			Configuration.getConfig();
			// Check if the userName value is already stored in the session
			if (sessionAttributes.containsKey("loggedUser")) {
				loggedUser = (String) sessionAttributes.get("loggedUser");
			}
			// Check if isAdmin value is already stored in the session
			if (sessionAttributes.containsKey("isAdmin")) {
				isAdmin = (Boolean) sessionAttributes.get("isAdmin");
			}

			// Get selected Agency/Masterdata to determine the corresponding
			// folder for Upload and Batch execution
			if (isAdmin) {
				// Determine if user selected "Masterdata" or "Agency" from the RadioButton
				if (getDataRadioButton()
						.equals(getDefaultRadioButton())) { // We use defaultRadioButton value as it was set as "Masterdata"
					for (int i = 0; i < Configuration.getMasterdataList().size(); i++) {
						if (getSelectedData().equals(
								Configuration.getMasterdataList().get(i).getName())) {
							masterdata = Configuration.getMasterdataList().get(i);
							break;
						}
					}
					filePathUpload = masterdata.getPathForUpload();
					filePathBatch = masterdata.getPathForBatchFile();
					description = "Masterdata File";
					name = masterdata.getName();
					fileNameMask = name;

					// if logged in user is an administrator and Masterdata was selected from the 
					// radioButton, we must check that filename and selected Masterdata are equal
					String extensionRemoved = getUploadDocFileName().split("\\.")[0];
					// and the fileName is not equal to the one specified in
					// config.xml for the selected Masterdata file to upload
					if (!fileNameMask.equals(extensionRemoved)) {
						// display an error message
						addActionError("Filename " + getUploadDocFileName()
								+ " must be equal to Masterdata selection "
								+ getSelectedData() + ".");
						return "input";
					}
					
				} else {
					for (int i = 0; i < Configuration.getAgencies().size(); i++) {
						// We loop through all sub-agencies and we retrieve
						// the corresponding Sub-Agency that matches the one 
						// selected from the screen
						List<SubAgency> subAgencies = Configuration.getAgencies().get(i).getSubagencies();

						for (int j = 0; j < subAgencies.size(); j++) {
							if (getSelectedData2()
									.equals(subAgencies.get(j).getName())) {
								filePathUpload = Configuration.getAgencies().get(i).getPathForUpload();
								filePathBatch = Configuration.getAgencies().get(i).getPathForBatchFile();
								description = "Agency File";
								name = subAgencies.get(j).getName();
								break;
							}
						}
					}
				}
			
			} else {
				for (int i = 0; i < Configuration.getAgencies().size(); i++) {
					// We loop through each Agency (Cluster) and check its securityGroup
					// If the securityGroup == userGroup (stored in the session)
					// we retrieve the corresponding Sub-Agency that matches
					// the one selected from the screen
					if (Configuration.getAgencies().get(i).getSecurityGroup()
							.equals((String)sessionAttributes.get("userGroup"))) {

						List<SubAgency> subAgencies = Configuration.getAgencies().get(i).getSubagencies();
						
						for (int j = 0; j < subAgencies.size(); j++) {
							if (getSelectedData()
									.equals(subAgencies.get(j).getName())) {
								filePathUpload = Configuration.getAgencies().get(i).getPathForUpload();
								filePathBatch = Configuration.getAgencies().get(i).getPathForBatchFile();
								description = "Agency File";
								name = subAgencies.get(j).getName();
								break;
							}
						}
					}					
				}
			}

			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Retrieve selected parameters' values");
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("Begin - CSV format validation");

			File fileToCreate = new File(filePathUpload, getUploadDocFileName());
						
			if (isCSVFileFormat(getUploadDoc())) {
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("End - CSV format validation");
				
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("Begin - File Upload");
				FileUtils.copyFile(getUploadDoc(), fileToCreate);
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("End - File Upload");

				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("Begin - Save AUDIT record");

				// Call HIBERNATE implementation to save record in DIM_AUDIT_STG table:
				// 1. Set Object to be saved
				AuditStg audit = new AuditStg();

				if (isAdmin) {
					audit.setYear(Integer.parseInt(getSelectedFYQ().substring(7, 11)));
					audit.setQuarter(Integer.parseInt(getSelectedFYQ().substring(13)));
				} else {
					if (sessionAttributes.containsKey("FiscalYear")) {
						audit.setYear((Integer) sessionAttributes.get("FiscalYear"));
					}
					if (sessionAttributes.containsKey("FiscalQuarter")) {
						audit.setQuarter((Integer) sessionAttributes.get("FiscalQuarter"));
					}
				}

				if (sessionAttributes.containsKey("DateSid")) {
					audit.setDateSid((Integer) sessionAttributes.get("DateSid"));
				}
				if (sessionAttributes.containsKey("Today")) {
					audit.setActiveFromDate((java.sql.Date) sessionAttributes.get("Today"));
				}

				audit.setActiveToDate(Date.valueOf("9999-12-31"));
				audit.setAgencyName(name);
				audit.setCurrentRecordFlag(1);
				audit.setDescription(description);
				audit.setFileName(getUploadDocFileName());
				audit.setStatus("PENDING");
				audit.setUserId(getLoggedUser());

				// 2. Call save method
				if (saveRecordInDB(audit)) {
					System.out.println("Record saved Successfully to the DB!");
					// Write to Tomcat's log
					WorkbenchUtils.writeToLog("End - Save AUDIT record");
					
					// Write to Tomcat's log
					WorkbenchUtils.writeToLog("Begin - Save parameters' values in session and navigate to Data Load validation");
					
					// Pass needed values to the session for ETL Validation action:
					// Saved AUDIT object
					sessionAttributes.put("AUDIT", audit);
					
					// Path to execute Batch File
					sessionAttributes.put("filePathBatch", filePathBatch);
					
					// Selected value for Agency or Masterdata from DropDown list
					if (isAdmin) { 	// Workbench Admin user
						if (getDataRadioButton().equals("Masterdata")) {
							sessionAttributes.put("selectedDropDownValue", getSelectedData());
						} else if (getDataRadioButton().equals("Agency")) {
							sessionAttributes.put("selectedDropDownValue", getSelectedData2());
						}
					} else { 		// Workbench Agency user
						sessionAttributes.put("selectedDropDownValue", getSelectedData());
					}

					// Write to Tomcat's log
					WorkbenchUtils.writeToLog("End - Save parameters' values in session and navigate to Data Load validation");

					// Navigate to ETL Validation action
					return "success";

				} else {
					System.out.println("Save to the DB Failed!");
					addActionError("Save to the DB Failed!");
					
					// Write to Tomcat's log
					WorkbenchUtils.writeToLog("End - Save AUDIT record");
					
					return "input";
				}
				
			} else {
				addActionError("File " + getUploadDocFileName() + " is not in CSV format (tab delimited).");
				
				// Write to Tomcat's log
				WorkbenchUtils.writeToLog("End - CSV format validation");

				return "input";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Selected parameters' values / File Upload");
			
			return "input";
		}
		
	}

	public boolean isCSVFileFormat(File uploadDoc) throws IOException, Exception {

		try {
			// We try to parse the file to be uploaded (using library Apache-Commons-CSV 1.0)
			CSVFormat format = CSVFormat.TDF.withHeader();
			CSVParser parser = CSVParser.parse(uploadDoc, Charset.defaultCharset(), format);
			parser.close();
			return true;
			
		} catch (IOException e) {
			// If the file to be upload can not be parsed,
			// then it is not in the expected CSV format (tab-delimited) and we catch the exception
			e.printStackTrace();
			return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveRecordInDB(AuditStg audit) throws HibernateException {
		
		AuditStgManager auditDAO;
		auditDAO = new AuditStgManager();

		try {
			auditDAO.add(audit);

			if (audit.getAuditSid() > 0)
				return true;
			else
				return false;

		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}
		
	public DateStg readDateRecordFromDB(int year, int month, int day) throws HibernateException {
		
		DateStg dateStg = new DateStg();
		DateStgManager dateStgDAO = new DateStgManager();

		try {
			dateStg = (DateStg) dateStgDAO.getDateStg(year, month, day);
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return dateStg;
	}

	public List<String> readFYQfromDB(int calendarYearFrom, int calendarYearTo) {
		
		List<String> FYQs = null;
		DateStgManager dateStgDAO = new DateStgManager();

		try {
			FYQs = (List<String>) dateStgDAO.listFYQs(calendarYearFrom, calendarYearTo);
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return FYQs;
	}

}