package com.munsiji.securityImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.munsiji.hibernateUtil.HibernateCfg;
import com.munsiji.persistance.daoImp.UserDetailDaoImp;

@Component("customUserDetialsService")
public class CustomUserDetialsService implements UserDetailsService{

	@Autowired
	HibernateCfg hibernateCfg;
	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
	 System.out.println("loadUserByUsername is getting executed");
	 emailId = "ravi.swd@gmail.com";
	  List<com.munsiji.persistance.resource.UserDetails> usrDetailsList= userDetailDaoImp.getUserInfo(emailId,null,null);
	  if(usrDetailsList == null || usrDetailsList.size() == 0)
		  return null;
	  com.munsiji.persistance.resource.UserDetails usrDetails = usrDetailsList.get(0);
	  return usrDetails;
	}

}
