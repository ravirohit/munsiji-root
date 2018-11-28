package org.munsiji.persistance.resource;

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
@Table(name="user_expense")
public class UserExpense {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;   
	@Column(name="amount")
	Float amount;
	@Column(name="date_of_expnse")
	Date dateOfExpnse;
	@Column(name="expns_desc")
	String desc;
	@ManyToOne(fetch=FetchType.EAGER) //cascade=CascadeType.ALL
	@JoinColumn(name="account_id", referencedColumnName="id")
	UserAccount userAccount;
	
	@ManyToOne(fetch=FetchType.EAGER) //cascade=CascadeType.ALL
	@JoinColumn(name="email_id", referencedColumnName="email_id")
	UserDetails userDetails;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Date getDateOfExpnse() {
		return dateOfExpnse;
	}
	public void setDateOfExpnse(Date dateOfExpnse) {
		this.dateOfExpnse = dateOfExpnse;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	

}
