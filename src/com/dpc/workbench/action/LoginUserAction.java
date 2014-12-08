package com.dpc.workbench.action;

import java.io.IOException;
import java.util.Map;
import com.crystaldecisions.sdk.exception.*;
import com.dpc.workbench.config.Configuration;
import com.dpc.workbench.model.User;
import com.dpc.workbench.util.BOE;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

public class LoginUserAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionAttributes = null;
	private User user = null;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionAttr) {
		this.sessionAttributes = sessionAttr;
	}
	
	public String execute() throws Exception, IOException, SDKException {
		// Read App's configuration parameters
		Configuration.getConfig();

//		// If we are coming from a successful upload and want to perform another upload
//		// we check if we have all session attributes to avoid logging in/querying BOE server again
//        if ( sessionAttributes.containsKey("loggedUser") && 
//        	 sessionAttributes.containsKey("isAdmin") && 
//        	 sessionAttributes.containsKey("DateSid") && 
//        	 sessionAttributes.containsKey("Today") ) {
//        	return "success";
//        }
		
		// Check if the userName is already stored in the session
        if (sessionAttributes.containsKey("loggedUser")) {
        	this.user = new User((String)sessionAttributes.get("loggedUser"));
        }
        
		if ((getUser().getUsername().isEmpty() || getUser().getUsername().equals(null))) {
			addActionError("Username can not be empty.");
            return "error";
		} else {
			BOE boe = new BOE();
			// If user login was successful
			if (boe.isLoggedInToBOE()) {
				// If user belongs to one of the groups defined in config.xml 
				// continues processing, else display an error message
				if (boe.isValid(this.user)) {
//					// Add isAdmin and userGroup property values to the session
		            sessionAttributes.put("isAdmin", getUser().getIsAdmin());
		            sessionAttributes.put("userGroup", getUser().getSecurityGroup());
		            return "success";
				} else {
					Configuration.getInstance();
					String msg = "Sorry " + getUser().getUsername() + ", you do not have permission to view the Human Services Data Hub Workbench.";
					addActionError(msg);
					return "error";
				}
			} else {
				addActionError("Login to BOE failed.");
				return "error"; 
			}
		}
	}

}