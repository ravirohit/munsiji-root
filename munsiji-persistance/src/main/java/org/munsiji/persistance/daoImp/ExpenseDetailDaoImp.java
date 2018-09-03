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
	public List<UserExpense> getUsrExpense(String accType, String usrEmail){
	  List<Object[]> userObjectExpenseList = null;
	  List<UserExpense> userExpenseList = new ArrayList();
	  String str = "select e.amount, e.dateOfExpnse, e.desc, e.userAccount, e.userDetails"
			  		+ "  FROM UserExpense e join e.userAccount a where a.type='" + accType +"' and  e.userAccount = a.id "
			  		+ " and e.userDetails = '"+usrEmail + "'";
	  try{
	    session =hibernateCfg.getSession();
	    System.out.println("start---------------");
	    Query query = session.createQuery(str);
	    System.out.println("end---------------");
	    System.out.println("quried run:"+query.list().getClass());
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
	  }
	  catch(Exception e){
	    System.out.println("exception occur while getUsrAccontDetails:"+e);
	  }
	  session.close();
	  return userExpenseList;
	}

}
