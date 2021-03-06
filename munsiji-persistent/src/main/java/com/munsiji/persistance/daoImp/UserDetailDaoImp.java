package com.munsiji.persistance.daoImp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.munsiji.hibernateUtil.HibernateCfg;
import com.munsiji.persistance.resource.UserAccount;
import com.munsiji.persistance.resource.UserDetails;

@Repository
public class UserDetailDaoImp {
	@Autowired
	HibernateCfg hibernateCfg;
	Session session = null;
	
	public Boolean registerUser(UserDetails UserDetails,boolean isUpdate){   // can be return type void
	  Boolean status = false;
	  session = hibernateCfg.getSession();
	  session.saveOrUpdate(UserDetails);
	  session.flush();
      status = true;
	  session.close();
	  return status;
	}
	public List<UserDetails> getUserInfo(String email,String pwd, String key){
		List<UserDetails> userList = null; 
		String str = "from UserDetails where";
		if(key != null){
			str = new StringBuffer(str).append(" key LIKE '" + key + "'").toString();
		}
		else {
			if(email != null){
				str = new StringBuffer(str).append(" emailId = '" + email + "'").toString();
			}
			if(pwd != null){
				str = new StringBuffer(str).append(" and pwd = '" + pwd + "'").toString();
			}
		}

		session = hibernateCfg.getSession();
		Query query = session.createQuery(str);
		//  userDetails = session.get(UserDetails.class, email);
		//  System.out.println("loading the user from db: >>>>>>>:"+userDetails.getEmailId()+"   "+userDetails.getPassword());
		userList = query.list();
		session.close();
		return userList;
	}
	
	public List<UserAccount> getAccountInfo(String email, String accType, String accName, boolean forProfile){
		List<UserAccount> userAccountList = null;
		StringBuffer str = new StringBuffer("from UserAccount where email_id = '"+email+ "'");
		/*String str = "from UserAccount where email_id = '"+email+ "' and type = '"
				+ accType + "' and name = '"+accName+"'";*/
		if((accType != null) && (accName != null)){
			str.append(" and type = '" + accType + "' and name = '"+accName+"'");
		}
		if(!forProfile){
			str.append("  and status = "+ true);
		}
		String queryStr = str.toString();
		try{
			  session = hibernateCfg.getSession();
			  Query query = session.createQuery(queryStr);
			  userAccountList = query.list();
		  }
		  catch(Exception e){
			  System.out.println("exception occur while registering user:"+e);
		  }
		session.close();
		return userAccountList;
	}
	public Boolean saveAccountInfo(UserAccount userAccount){
	  Boolean status = false;
	  try{
		  session = hibernateCfg.getSession();
		  session.saveOrUpdate(userAccount);
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
