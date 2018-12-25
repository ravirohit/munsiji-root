package com.munsiji.persistance.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;               // should be incremented automatically
	@Column(name="acc_name")
	String name;
	@Column(name="acc_type")
	String type;
	@Column(name="invested_amnt")
	Float investedAmnt;
	@Column(name="acc_desc")
	String desc;
	@Column(name="crte_date")
	Date crteDate;
	@Column(name="acc_status")
	Character status;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="email_id", referencedColumnName="email_id")
	UserDetails userDetails;
	
	public Float getInvestedAmnt() {
		return investedAmnt;
	}
	public void setInvestedAmnt(Float investedAmnt) {
		this.investedAmnt = investedAmnt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Date getCrteDate() {
		return crteDate;
	}
	public void setCrteDate(Date crteDate) {
		this.crteDate = crteDate;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	
	
	
}
