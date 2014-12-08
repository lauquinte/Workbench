package com.dpc.workbench.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dpc.workbench.model.AuditStg;
import com.dpc.workbench.util.HibernateUtil;

public class AuditStgManager extends HibernateUtil {

	public AuditStgManager() {
		// A no-args constructor is needed to avoid Reflection 
		// Exception when calling Hibernate implementation
	}
	
	public AuditStg add(AuditStg auditStg) {

		Session session = null;
		Transaction transaction = null;
		
		try {
			session = HibernateUtil.getSessionFactory("hibernate.cfg.xml").openSession();
			transaction = session.beginTransaction();

			session.save(auditStg);
			transaction.commit();
			
			// Print generated AUDIT_SID from the new record
			System.out.println("Record saved - Generated AUDIT_SID: " + auditStg.getAuditSid());
			
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();

		} finally {
			session.close();
			transaction = null;
			session = null;
		}
		return auditStg;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditStg> list() {
		
		Session session = null;
		Transaction transaction = null;
		List<AuditStg> auditStgs = null;
		
		try {	
			session = HibernateUtil.getSessionFactory("hibernate.cfg.xml").openSession();
			transaction = session.beginTransaction();
			
			auditStgs = (List<AuditStg>)session.createQuery("from AuditStg").list();
			transaction.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			
		} finally {
			session.close();
			transaction = null;
			session = null;
		}
		return auditStgs;
	}
	
	public AuditStg getAuditStg(int audit_sid) {
		
		Session session = null;
		Transaction transaction = null;
		AuditStg auditStg = null;
		
		try {	
			session = HibernateUtil.getSessionFactory("hibernate.cfg.xml").openSession();
			transaction = session.beginTransaction();

			Query query = session.createQuery("from AuditStg where audit_sid = :audit_sid");
			query.setParameter("audit_sid", audit_sid);

			auditStg = (AuditStg)query.uniqueResult();
			transaction.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			
		} finally {
			session.close();
			transaction = null;
			session = null;
		}
		return auditStg;
	}

}
