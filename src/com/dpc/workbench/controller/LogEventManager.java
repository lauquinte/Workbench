package com.dpc.workbench.controller;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.dpc.workbench.model.LogEvent;
import com.dpc.workbench.util.HibernateUtil;

public class LogEventManager  extends HibernateUtil {

	public LogEventManager() {
		// A no-args constructor is needed to avoid Reflection
		// Exception when calling Hibernate implementation
	}

	@SuppressWarnings("unchecked")
	public List<LogEvent> list(int jobExecId, int severityId) {

		Session session = null;
		Transaction transaction = null;
		List<LogEvent> logs = null;
		
		try {
			session = HibernateUtil.getSessionFactory("etlframework.cfg.xml").openSession();
			transaction = session.beginTransaction();
						
			Query query = session.createQuery("from LogEvent where job_exec_id = :job_exec_id "
															+ "and severity_id = :severity_id "
															+ "and message_text like 'Error Message%'");

			query.setParameter("job_exec_id", jobExecId);
			query.setParameter("severity_id", severityId);
			
			logs = (List<LogEvent>)query.list();
			transaction.commit();
		
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();

		} finally {
			session.close();
			transaction = null;
			session = null;
		}
		return logs;
	}
	
}
