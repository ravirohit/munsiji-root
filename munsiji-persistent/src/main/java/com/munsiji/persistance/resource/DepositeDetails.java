package com.munsiji.persistance.resource;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="deposite_details")
public class DepositeDetails {
  @Id
  Long id;
  @Column(name="acc_name")
  String acc_name;
  @Column(name="dpste_amount")
  Long dpsteAmount;
  @Column(name="date_of_dpste")
  Date dateOfDpste;
  @Column(name="dpste_desc")
  String dpsteDesc;
  @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
  @JoinColumn(name="email_id", referencedColumnName="email_id")
  UserDetails userDetails;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAcc_name() {
		return acc_name;
	}
	public void setAcc_name(String acc_name) {
		this.acc_name = acc_name;
	}
	public Long getDpsteAmount() {
		return dpsteAmount;
	}
	public void setDpsteAmount(Long dpsteAmount) {
		this.dpsteAmount = dpsteAmount;
	}
	public Date getDateOfDpste() {
		return dateOfDpste;
	}
	public void setDateOfDpste(Date dateOfDpste) {
		this.dateOfDpste = dateOfDpste;
	}
	public String getDpsteDesc() {
		return dpsteDesc;
	}
	public void setDpsteDesc(String dpsteDesc) {
		this.dpsteDesc = dpsteDesc;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	  
}
