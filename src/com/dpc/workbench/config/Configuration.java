package com.dpc.workbench.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.dpc.workbench.config.pojo.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("config") // maps "config" element in XML to this class
public class Configuration {
	
	private static Configuration config = null;

	private String boServerName;
	private String boAuthType;
	private String boAdminUser;
	private String boPassword;
	private String startingCalendarYear;
	private String baseUrl;

	@XStreamAlias("allowedGroups")
	private List<AllowedGroup> allowedGroups = new ArrayList<AllowedGroup>();
	@XStreamAlias("agencies")
	private List<Agency> agencies = new ArrayList<Agency>();
	@XStreamAlias("masterdataList")
	private List<Masterdata> masterdataList = new ArrayList<Masterdata>();
	
	public Configuration() {
	}		
	
	public static Configuration getInstance() {
		if (config == null)
			config = new Configuration();
		return config;
	}
	
	public static void getConfig() throws Exception, FileNotFoundException  {
		Configuration instance = getInstance();
		instance.loadConfig();
	}

	private void loadConfig() throws Exception, FileNotFoundException {	
		FileReader reader = null;
		XStream xstream = null;
		
		try {
			reader  = new FileReader("E:\\HSDH\\Workbench\\config.xml"); //C:\\Temp\\config.xml
			xstream = new XStream();
			
			xstream.processAnnotations(Configuration.class);    // inform XStream to parse annotations in Configuration class
			xstream.processAnnotations(AllowedGroup.class);     // and in the other classes that we use for mappings
			xstream.processAnnotations(Agency.class);
			xstream.processAnnotations(SubAgency.class);
			xstream.processAnnotations(Masterdata.class);   
			
			config = (Configuration) xstream.fromXML(reader); 	// XML parser
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			xstream = null;
			reader.close();
		}
	}
	
	// Setters and Getters
	public String getBoServerName() {
		return getInstance().boServerName;
	}
	public void setBoServerName(String boServerName) {
		this.boServerName = boServerName;
	}

	public String getBoAuthType() {
		return getInstance().boAuthType;
	}
	public void setBoAuthType(String boAuthType) {
		this.boAuthType = boAuthType;
	}

	public String getBoAdminUser() {
		return getInstance().boAdminUser;
	}
	public void setBoAdminUser(String boAdminUser) {
		this.boAdminUser = boAdminUser;
	}

	public String getBoPassword() {
		return getInstance().boPassword;
	}
	public void setBoPassword(String boPassword) {
		this.boPassword = boPassword;
	}
	
	public String getStartingCalendarYear() {
		return getInstance().startingCalendarYear;
	}
	public void setStartingCalendarYear(String startingCalendarYear) {
		this.startingCalendarYear = startingCalendarYear;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public static List<AllowedGroup> getAllowedGroups() {
		return getInstance().allowedGroups;
	}
	public void setAllowedGroups(List<AllowedGroup> allowedGroups) {
		this.allowedGroups = allowedGroups;
	}
	
	public static List<Agency> getAgencies() {
		return getInstance().agencies;
	}
	public void setAgencies(List<Agency> agencies) {
		this.agencies = agencies;
	}
	
	public static List<Masterdata> getMasterdataList() {
		return getInstance().masterdataList;
	}
	public void setMasterdataList(List<Masterdata> masterdataList) {
		this.masterdataList = masterdataList;
	}
	
}