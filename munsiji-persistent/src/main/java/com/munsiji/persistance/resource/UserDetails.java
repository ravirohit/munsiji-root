package com.munsiji.persistance.resource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;


@Entity
@Table(name="user_details")
public class UserDetails{
  @Id
  @Column(name = "email_id", nullable = false, unique = true)
  String emailId;
  @Column(name="uname")
  String uname;
  @Column(name="mobile_no")
  String mobileNo;
  @Column(name="pwd")
  String pwd;
  @Column(name="usr_key")
  String key;
  @Column(name="current_key")
  String currentLoginKey;
  @Column(name="role")
  String role;
	
	public String getCurrentLoginKey() {
	    return currentLoginKey;
	}
	public void setCurrentLoginKey(String currentLoginKey) {
		this.currentLoginKey = currentLoginKey;
	}
	public String getEmailId() {
	return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    public void removeCurrentKeyForLogout(){
    	//String storeKey = userDetails.getKey();
		String currentKey = new StringBuffer("|").append(getCurrentLoginKey()).append("|").toString();
		int keyIndex = key.indexOf(currentKey);
		String strBeforeCurrentKey = key.substring(0,keyIndex);
		String strAfterCurrentKey = key.substring(keyIndex+currentKey.length());
		key = new StringBuffer(strBeforeCurrentKey).append(strAfterCurrentKey).toString();
    }
    public String addCurrentKeyForLogin(String uName, String pwd) throws UnsupportedEncodingException{
    	Random random = new Random();
		int n = random.nextInt(10000);
		String str = uName + n + pwd;
		byte[] byteArray = str.getBytes("utf-8");
	//	String base64String =Base64.getEncoder().encodeToString(byteArray);
		String base64String = convertStringToHexString(Base64.getEncoder().encodeToString(byteArray));
		String keyToStore = new StringBuffer("|").append(base64String).append("|").toString();
		if((getKey() == null)||(getKey().trim().equals("")))
		{
		   setKey(keyToStore);
		}
		else{
			String storeKey = getKey();
			String keyAfterAppendNewKey = new StringBuffer(storeKey).append(keyToStore).toString();
			setKey(keyAfterAppendNewKey);
			
		}
		return base64String;
    }
    private String convertStringToHexString(String base64String){
    	StringBuffer sb = new StringBuffer();
    	try {
			byte[] base64StringByteArray = base64String.getBytes("utf-8");
			for(byte b:base64StringByteArray){
				sb.append(String.format("%02X", b));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return sb.toString();
    }
  
	
}
