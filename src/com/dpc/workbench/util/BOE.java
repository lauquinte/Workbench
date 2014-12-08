package com.dpc.workbench.util;

import com.crystaldecisions.sdk.framework.*;
import com.crystaldecisions.sdk.exception.*;
import com.crystaldecisions.sdk.occa.infostore.*;
//import com.crystaldecisions.sdk.occa.security.*;
import com.crystaldecisions.sdk.properties.IProperties;
import com.dpc.workbench.config.Configuration;
import com.dpc.workbench.model.User;

public class BOE {

	private IEnterpriseSession boSession = null;
	private IInfoObjects groups = null;
	private IInfoObject user = null;
	private IProperties userProps = null;
	
	public boolean isLoggedInToBOE() throws SDKException, Exception {

		try {
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("Begin - Login as Admin user to query BO objects");
			
			ISessionMgr sessionMgr = null;
//			ILogonTokenMgr tokenMgr = null;

			String boServerName = Configuration.getInstance().getBoServerName();
			String boAuthType 	= Configuration.getInstance().getBoAuthType();
			String boUser		= Configuration.getInstance().getBoAdminUser();
			String boPassword	= Configuration.getInstance().getBoPassword();
//			String logonToken 	= "";

			sessionMgr = CrystalEnterprise.getSessionMgr();
			// Logon and obtain an Enterprise Session
			boSession = sessionMgr.logon(boUser, boPassword, boServerName, boAuthType);
			System.out.println("You have successfully logged on!");

//			tokenMgr = boSession.getLogonTokenMgr();
//			logonToken = tokenMgr.getDefaultToken();
//			boSession = CrystalEnterprise.getSessionMgr().logonWithToken(logonToken);
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Login as Admin user to query BO objects");
			
			return true;

		} catch (SDKException e) {  
			e.printStackTrace();
			System.out.println("Logon error: " + e.getMessage());
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Login as Admin user to query BO objects");
			
			return false;

		} catch (Exception e) {   
			e.printStackTrace(); 
			System.out.println("Logon error: " + e.getMessage());
			
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Login as Admin user to query BO objects");
			
			return false;
		}
	}

	@SuppressWarnings("static-access")
	public boolean isValid(User boLoggedUser) throws Exception {
		
		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("Begin - Validate if user belongs to a workbench's group");

		IInfoStore boInfoStore = null;
		boolean belongsToBOUserGroup = false;
				
		try {
			boInfoStore = (IInfoStore) boSession.getService("", "InfoStore");
			int count = Configuration.getInstance().getAllowedGroups().size();

			// Get groups from InfoStore
			String sQueryGroupName = "SELECT SI_ID, SI_NAME FROM CI_SYSTEMOBJECTS WHERE (SI_NAME = '";
			
			for (int i = 0; i < count; i++) {
				sQueryGroupName = sQueryGroupName.concat(Configuration.getInstance().getAllowedGroups().get(i).getName());
				
				if (i != count-1)
					sQueryGroupName = sQueryGroupName.concat("' or SI_NAME = '");
				else
					sQueryGroupName = sQueryGroupName.concat("') AND SI_KIND = 'UserGroup'");
			}
						
			groups = boInfoStore.query(sQueryGroupName); 

			// Get users from InfoStore
			String sQueryUsers = "SELECT SI_ID, SI_NAME, SI_USERGROUPS FROM CI_SYSTEMOBJECTS WHERE SI_KIND = 'User' AND SI_NAME ='" + boLoggedUser.getUsername() + "'";
			IInfoObjects users = boInfoStore.query(sQueryUsers); 

			// Loop through users to get user properties and group membership properties 
			for (int i = 0; i < users.size(); i++) { 
				// Get user and display properties 
				user = (IInfoObject)users.get(i); 
				System.out.println("user SI_NAME = " + user.getTitle() + " - user SI_ID = " + user.getID()); 
				System.out.println("Belongs to the following user group(s):"); 

				// Get user group membership from SI_USERGROUPS property bag 
				userProps = (IProperties)user.properties().getProperty("SI_USERGROUPS").getValue(); 
				int userGroupCount = ((Integer)userProps.getProperty("SI_TOTAL").getValue()).intValue(); 

				// Loop through each user group 
				for (int j = 1; j <= userGroupCount; j++) {
					// Get user group SI_ID 
					String groupID = userProps.getProperty(Integer.toString(j)).getValue().toString(); 

					for (int k = 0; k < groups.getResultSize(); k++) {
						String groupNameID = Integer.toString( ((IInfoObject)groups.get(k)).getID() );
						String groupTitle = ((IInfoObject)groups.get(k)).getTitle();

						if (groupID.equals(groupNameID)) {
							belongsToBOUserGroup = true;
							boLoggedUser.setSecurityGroup(groupTitle);

							// If user is Workbench Administrator set property
							if (groupTitle.equals(Configuration.getInstance().getAllowedGroups().get(0).getName())) {
								boLoggedUser.setIsAdmin(true);
							}

							// Display user group properties
							System.out.println("User Group SI_ID = " + groupNameID + " - Group SI_NAME = " + groupTitle);
							break;
						}
					}
				}

				if (!belongsToBOUserGroup) {
					System.out.println("NONE:: You do not have permission to view the Data Hub Workbench.");
				}
			}

		} catch (Exception e) {
			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("End - Validate if user belongs to a workbench's group");

			
			// Log the error and the stack trace to the system out log file
			System.out.println("An error was encountered. A description of the error is written below:");
			System.out.println(e);
			System.out.println("The stack trace for the error is written below:");
			e.printStackTrace(System.out);
			
		} finally {
			closeBOSession();
		}

		// Write to Tomcat's log
		WorkbenchUtils.writeToLog("End - Validate if user belongs to a workbench's group");

		return belongsToBOUserGroup;
	}

	public void closeBOSession() {
		if (boSession != null) {

			// Write to Tomcat's log
			WorkbenchUtils.writeToLog("Closing BO session");
			
			boSession.logoff();   
			boSession = null;   
		}
	}

}
