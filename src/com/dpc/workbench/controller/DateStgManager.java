package com.dpc.workbench.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.dpc.workbench.model.DateStg;
import com.dpc.workbench.util.HibernateUtil;

public class DateStgManager extends HibernateUtil {

	public DateStgManager() {
		// A no-args constructor is needed to avoid Reflection 
		// Exception when calling Hibernate implementation
	}
	
	public DateStg getDateStg(int year, int month, int day) {

		Session session = null;
		Transaction transaction = null;
		DateStg dateStg = null;
		
		try {
			session = HibernateUtil.getSessionFactory("hibernate.cfg.xml").openSession();
			transaction = session.beginTransaction();
			
			Query query = session.createQuery("from DateStg where calendar_day_of_month_id = :calendar_day_of_month_id "
  														   + "and calendar_month_id = :calendar_month_id "
														   + "and calendar_year_id = :calendar_year_id");

			query.setParameter("calendar_day_of_month_id", day);
			query.setParameter("calendar_month_id", month);
			query.setParameter("calendar_year_id", year);
			
			dateStg = (DateStg)query.uniqueResult();
			transaction.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();

		} finally {
			session.close();
		}
		return dateStg;
	}

	@SuppressWarnings("unchecked")
	public List<String> listFYQs(int calendarYearFrom, int calendarYearTo) {
		
		List<String> lst = null; 
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory("hibernate.cfg.xml").openSession();
			transaction = session.beginTransaction();
			
			String sql = "select distinct financial_year_yyyy_yyyy_qq from dim_date_stg where ";

			if (calendarYearFrom == calendarYearTo) {
				// current year
				sql += "calendar_year_id = :calendar_year_id";

				lst = (List<String>)session.createSQLQuery(sql)
										   .setParameter("calendar_year_id", calendarYearTo)
										   .list();
			
			} else {
				// between starting year from config.xml and current year
				sql += "calendar_year_id between :calendar_year_id_from and :calendar_year_id_to";

				lst = (List<String>)session.createSQLQuery(sql)
						   .setParameter("calendar_year_id_from", calendarYearFrom)
						   .setParameter("calendar_year_id_to", calendarYearTo)
						   .list();
			}

			transaction.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			
		} finally {
			session.close();
		}
		return lst;
	}
}
