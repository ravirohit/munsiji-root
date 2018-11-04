package org.munsiji.persistance.daoImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.munsiji.hibernateUtil.HibernateCfg;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.persistance.resource.UserExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	public List<Object[]> getUsrExpense(String accType, String usrEmail){
	  List<UserExpense> userExpenseList = null;
	  List<Object[]> userObjectExpenseList = null;
	  String queryStr;
	  if(accType == null) {
		  queryStr = "select a.type, a.name, e.amount from UserAccount a, UserExpense e "
	  		               + "where e.userDetails = '"+usrEmail+"' and e.userAccount = a.id";
	  }
	  else {
		   queryStr = "select e.amount, e.dateOfExpnse, e.desc, e.userAccount, e.userDetails"
			  		+ "  FROM UserExpense e join e.userAccount a where a.type='" + accType +"' and  e.userAccount = a.id "
			  		+ " and e.userDetails = '"+usrEmail + "'";
	  }
	  try{
	    session =hibernateCfg.getSession();
	    System.out.println("start---------------");
	    Query query = session.createQuery(queryStr);
	    System.out.println("end---------------");
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
