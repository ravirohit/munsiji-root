package org.munsiji.securityImp;

import java.util.List;

import org.munsiji.hibernateUtil.HibernateCfg;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
	  List<org.munsiji.persistance.resource.UserDetails> usrDetailsList= userDetailDaoImp.getUserInfo(emailId,null,null);
	  if(usrDetailsList == null || usrDetailsList.size() == 0)
		  return null;
	  org.munsiji.persistance.resource.UserDetails usrDetails = usrDetailsList.get(0);
	  return usrDetails;
	}

}
