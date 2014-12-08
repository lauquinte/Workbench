package com.dpc.workbench.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DIM_DATE_STG")
public class DateStg implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int dateSid;
	private int financialQuarterId;
	private int financialYearId;
	private String financialYearYyyyYyyyQq;

	public DateStg() {
	// A No-Args constructor is mandatory for Hibernate to work:
	// ---------------------------------------------------------
	// 1.HibernateUtil loads
	// 2.That forces the static to run buildSessionFactory
	// 3.Hibernate instantiates POJO(s) (e.g. DateStg.java) via no-args
	// 4.That in turn calls HibernateUtil
	}

	@Id
	@GeneratedValue
	@Column(name="date_sid", unique=true ,nullable=false)
	public int getDateSid() {
		return dateSid;
	}
	public void setDateSid(int dateSid) {
		this.dateSid = dateSid;
	}

	@Column(name="financial_quarter_id")
	public int getFinancialQuarterId() {
		return financialQuarterId;
	}
	public void setFinancialQuarterId(int financialQuarterId) {
		this.financialQuarterId = financialQuarterId;
	}

	@Column(name="financial_year_id")
	public int getFinancialYearId() {
		return financialYearId;
	}
	public void setFinancialYearId(int financialYearId) {
		this.financialYearId = financialYearId;
	}

	@Column(name="financial_year_yyyy_yyyy_qq")
	public String getFinancialYearYyyyYyyyQq() {
		return financialYearYyyyYyyyQq;
	}
	public void setFinancialYearYyyyYyyyQq(String financialYearYyyyYyyyQq) {
		this.financialYearYyyyYyyyQq = financialYearYyyyYyyyQq;
	}

}
