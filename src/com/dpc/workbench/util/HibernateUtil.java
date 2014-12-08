package com.dpc.workbench.util;

import com.dpc.workbench.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jboss.logging.Logger;

public class HibernateUtil {

	private static SessionFactory sessionFactory = null;
	
	@SuppressWarnings("deprecation")
	public static SessionFactory getSessionFactory(String dbConfigFile) {
		try {
	        // Create the SessionFactory from "hibernate.cfg.xml" or "etlframework.cfg.xml"
	        Configuration cfg = new Configuration();
	        sessionFactory = cfg.configure(dbConfigFile).buildSessionFactory();
		} catch (Exception ex) {
	        // Make sure you log the exception, as it might be swallowed
	        Logger.getLogger(HibernateUtil.class).error("Initial SessionFactory creation failed." + ex, ex);
	        throw new ExceptionInInitializerError(ex);
	    }
    	return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
	
}