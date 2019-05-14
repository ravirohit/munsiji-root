package com.munsiji.persistance.daoImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.munsiji.hibernateUtil.HibernateCfg;
import com.munsiji.persistance.resource.UserAccount;
import com.munsiji.persistance.resource.UserDetails;
import com.munsiji.persistance.resource.UserExpense;

@Repository

public class ExpenseDetailDaoImp {
	@Autowired
	HibernateCfg hibernateCfg;
	Session session = null;
	
	public Boolean saveExpense(UserExpense userExpense){
	  Boolean status = false;
	  try{
		session = hibernateCfg.getSession();
		session.save(userExpense);
		session.flush();
		status = true;
	  }
	  catch(Exception e){
		System.out.println("error occur while saving expense:"+e);
	  }
	  session.close();
	  return status;
	}
	public List<UserAccount> getUsrAccountDetail(String usrEmail){
	  List<UserAccount> userAccountList = null;
	  String str = " from UserAccount where userDetails = '"+usrEmail + "'";
	  try{
	    session =hibernateCfg.getSession();
	    Query query = session.createQuery(str);
	    userAccountList = query.list();
	  }
	  catch(Exception e){
	    System.out.println("exception occur while getUsrAccontDetails:"+e);
	  }
	  session.close();
	  return userAccountList;
	}
	public List<Object[]> getUsrExpense(String accType,String accName, String usrEmail, String startDate, String endDate, String[] accNameArr){
	  List<Object[]> userObjectExpenseList = null;
	  String queryStr = "";
	  if(accNameArr != null){
		  StringBuffer str = new StringBuffer("select e.amount, e.dateOfExpnse, e.userAccount, e.desc"                    // detailed exp for each account download
			  		+ "  FROM UserExpense e join e.userAccount a where a.type='" + accType +"' and  e.userAccount = a.id "
			  		+ " and e.userDetails = '"+usrEmail + "' and a.status="+true);
		  for(String accEle: accNameArr){
			  str.append(" or a.name = '"+accEle+"'");
		  }
		  queryStr = str.toString();
		  System.out.println("query stirng to download:"+queryStr);
	  }
	  else if(accName != null) {
		  queryStr = "select e.amount, e.dateOfExpnse, e.desc, e.userAccount, e.userDetails"                    // detailed exp for each account
			  		+ "  FROM UserExpense e join e.userAccount a where a.type='" + accType +"' and  e.userAccount = a.id "
			  		+ " and e.userDetails = '"+usrEmail + "'"+" and a.name = '"+accName+"' and a.status="+true;
	  }
	  else if(accType != null){
		  queryStr = "select a.type, a.name, e.amount, a.crteDate, a.desc from UserAccount a, UserExpense e "             // for home page
	  		               + "where e.userDetails = '"+usrEmail+"' and e.userAccount = a.id and a.type = '" +accType +"' and a.status="+true ;
	  }
	  else {
		  queryStr = "select a.type, a.name, e.amount, a.crteDate, a.desc, a.status from UserAccount a, UserExpense e "             // for home page
		               + "where e.userDetails = '"+usrEmail+"' and e.userAccount = a.id";
	  }
	  if(startDate != null){
		  queryStr = new StringBuffer(queryStr).append(" and e.dateOfExpnse >= '"+startDate+"'").toString();
		  if(endDate !=null){
			  queryStr = new StringBuffer(queryStr).append(" and e.dateOfExpnse <= '"+endDate+"'").toString();
		  }
	  }
	  /*else {
		   queryStr = "select e.amount, e.dateOfExpnse, e.desc, e.userAccount, e.userDetails"
			  		+ "  FROM UserExpense e join e.userAccount a where a.type='" + accType +"' and  e.userAccount = a.id "
			  		+ " and e.userDetails = '"+usrEmail + "'";
		   if(startDate != null){
			   queryStr = new StringBuffer(queryStr).append(" and e.dateOfExpnse >= '"+startDate+"'").toString();
			   if(endDate != null){
				   queryStr = new StringBuffer(queryStr).append(" and e.dateOfExpnse <= '"+endDate+"'").toString();
			   }
		   }
	  }*/
	  try{
	    session =hibernateCfg.getSession();
	    Query query = session.createQuery(queryStr);
	    System.out.println("quried run:"+query.list().getClass());
	    //if(accType != null){
	    	//userExpenseList = getUsrExpensePerAcctype(query);
	    userObjectExpenseList = query.list();
	   // }
	  }
	  catch(Exception e){
	    System.out.println("exception occur while getUsrAccontDetails:"+e);
	  }
	  session.close();
	  //return userExpenseList;
	  return userObjectExpenseList;
	}
	/*private List<UserExpense> getUsrExpensePerAcctype(Query query ){
		List<Object[]> userObjectExpenseList = null;
		List<UserExpense> userExpenseList = new ArrayList();
		userObjectExpenseList = query.list();
	    for(Object[] object: userObjectExpenseList){
	      UserExpense userExpense= new UserExpense();
	      userExpense.setAmount((Float)object[0]);
	      userExpense.setDateOfExpnse((Date)object[1]);
	      userExpense.setDesc((String)object[2]);
	      userExpense.setUserAccount((UserAccount)object[3]);
	      userExpense.setUserDetails((UserDetails)object[4]);
	      userExpenseList.add(userExpense);
	    }
	    System.out.println("quried run:"+query.list().getClass());
	    return userExpenseList;
	}*/

}
