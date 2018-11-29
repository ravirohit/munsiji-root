package org.munsiji.persistance.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@Entity
@Table(name="user_details")
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails{
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
  @Column(name="role")
  String role;
	
	
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
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String[] roleList = {this.role};
		return AuthorityUtils.createAuthorityList(roleList);   //TODO... need to create role variable
	}
	@Override
	public String getPassword() {
		return pwd;
	}
	@Override
	public String getUsername() {
		return emailId;             //  here username is the login user name which is emailID
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	  
  
  
  
	
}
