package org.munsiji.persistance.daoImp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.munsiji.hibernateUtil.HibernateCfg;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDetailDaoImp {
	@Autowired
	HibernateCfg hibernateCfg;
	Session session = null;
	
	public Boolean registerUser(UserDetails UserDetails){
	  Boolean status = false;
	  try{
		  session = hibernateCfg.getSession();
		  session.save(UserDetails);
		  session.flush();
	      status = true;
	      System.out.println("saved data");
	  }
	  catch(Exception e){
		  System.out.println("exception occur while registering user:"+e);
	  }
	  session.close();
	  return status;
	}
	public List<UserDetails> getUserInfo(String email){
		List<UserDetails> userList = null; 
		String str = "from UserDetails where email_id = '" + email+"'";
		try{
			  session = hibernateCfg.getSession();
			  Query query = session.createQuery(str);
			  userList = query.list();
		  }
		  catch(Exception e){
			  System.out.println("exception occur while registering user:"+e);
		  }
		session.close();
		return userList;
	}
	
	public List<UserAccount> getAccountInfo(String email, String type, String name){
		List<UserAccount> userAccountList = null;
		String str = "from UserAccount where email_id = '"+email+ "' and type = '"
				+ type + "' and name = '"+name+"'";
		try{
			  session = hibernateCfg.getSession();
			  Query query = session.createQuery(str);
			  userAccountList = query.list();
		      System.out.println("getAccountinfoList data:"+userAccountList.size());
		  }
		  catch(Exception e){
			  System.out.println("exception occur while registering user:"+e);
		  }
		session.close();
		return userAccountList;
	}
	public Boolean saveAccountInfo(UserAccount userAccount){
	  Boolean status = false;
	  System.out.println("date value:"+userAccount.getCrteDate());
	  try{
		  session = hibernateCfg.getSession();
		  session.save(userAccount);
		  session.flush();
	      status = true;
	      System.out.println("saved Account data");
	  }
	  catch(Exception e){
		  System.out.println("exception occur while saving user account:"+e);
	  }
	  session.close();
	  return status;
	}

}
