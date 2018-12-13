package org.munsiji.reqresObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileInfo implements GenericResponse{
	
	String userName;
	String emailId;
	String mobNo;
	Map<String,List<AccExpenseData>> accountInfo;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public Map<String, List<AccExpenseData>> getAccountInfo() {
		if(accountInfo == null)
		{
			accountInfo = new HashMap<String,List<AccExpenseData>>();
		}
		return accountInfo;
	}
	public void setAccountInfo(Map<String, List<AccExpenseData>> accountInfo) {
		this.accountInfo = accountInfo;
	}
	
	
	

}
