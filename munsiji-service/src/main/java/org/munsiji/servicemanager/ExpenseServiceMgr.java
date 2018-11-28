package org.munsiji.servicemanager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.munsiji.commonUtil.DateUtil;
import org.munsiji.commonUtil.MunsijiServiceConstants;
import org.munsiji.commonUtil.UserContextUtils;
import org.munsiji.persistance.daoImp.ExpenseDetailDaoImp;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.persistance.resource.UserExpense;
import org.munsiji.reqresObject.AccExpenseData;
import org.munsiji.reqresObject.AccountContent;
import org.munsiji.reqresObject.ColTitle;
import org.munsiji.reqresObject.ExpenseForAllAccType;
import org.munsiji.reqresObject.ExpenseInfoRes;
import org.munsiji.reqresObject.ExpenseWithAccType;
import org.munsiji.reqresObject.Header;
import org.munsiji.reqresObject.ResponseInfo;
import org.munsiji.reqresObject.UserExpenseReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExpenseServiceMgr {
	
	@Autowired
	ExpenseDetailDaoImp expenseDetailDaoImp;
	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	ObjectMapper mapper = new ObjectMapper();
	
	
	public ResponseInfo addExpense(UserExpenseReq userExpenseReq){
	  //user.setEmailId("ravi.swd@gmail.com");
	  User userInfo = UserContextUtils.getUser();
	  Date date = null;
	  List<UserAccount> userAccountList = null;
	  Boolean status = null;
	  ResponseInfo responseInfo = new ResponseInfo();
	  try{ 
	    System.out.println("object before mapping to entity:"+mapper.writeValueAsString(userExpenseReq));
	  
	    date = DateUtil.convertStringToDate(userExpenseReq.getDateOfExpnse());
		userExpenseReq.setDateOfExpnse(null);
		UserExpense userExpense = dozerBeanMapper.map(userExpenseReq, UserExpense.class); // does not convert String date to Date
		userExpense.setDateOfExpnse(date);
		  //TODO...
		List<UserAccount> userAccountLst = userDetailDaoImp.getAccountInfo(userInfo.getUsername(), userExpenseReq.getAccType(), userExpenseReq.getAccName());
		UserAccount userAccount = null;
		if((userAccountLst == null) ||(userAccountLst.size() == 0)){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg("Account name does not exist for given account type");
			responseInfo.setReason("Account not found");
			responseInfo.setStatusCode(404);
			return responseInfo;
		  
		}
		userAccount = userAccountLst.get(0);
		userExpense.setUserAccount(userAccount);
		UserDetails userDetails = userDetailDaoImp.getUserInfo(userInfo.getUsername(),null,null).get(0);
	    userExpense.setUserDetails(userDetails);
	    //System.out.println("expense saved to the db:"+mapper.writeValueAsString(userExpense));
	    status = expenseDetailDaoImp.saveExpense(userExpense);
		if(status){
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_MSG);
			return responseInfo;
		}
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			return responseInfo;
		}
	  }
	  catch(Exception e){
		System.out.println("exception occur in mapper:"+e);
	  }
	  return responseInfo;
	}
	public ResponseInfo getExpense(String accTypeReq,String accName, String startDateStr,String endDateStr){
	  User userInfo = UserContextUtils.getUser();
	  System.out.println("inside of service mgr method:"+userInfo.getUsername());
	  List<String> list = null;
	  List<Object[]> userObjectExpenseList =null;
	  List<UserExpense> userExpensePerAccList = null;
	  //UserTotalExepense userTotalExepense = null;
	  ExpenseForAllAccType expenseForAllAccType = null;
      ResponseInfo responseInfo = null;
      String startDate = null;
      String endDate = null;
      try {
    	   
		   //System.out.println("UserDetails fetched:"+mapper.writeValueAsString(userAccountList)+"\n"+mapper.writeValueAsString(accMap));
    	  if(startDateStr != null){
	    	   startDate = DateUtil.convertStringToString(startDateStr);
	    	  if(endDateStr != null)
	    	   endDate = DateUtil.convertStringToString(endDateStr);
    	  }
    		userObjectExpenseList = expenseDetailDaoImp.getUsrExpense(accTypeReq,accName,userInfo.getUsername(), startDate, endDate); 
    		if(accName != null){
    			userExpensePerAccList = convertObjectArrayToModelForAcc(userObjectExpenseList);
		    //userExpenseList = expenseDetailDaoImp.getUsrExpense(accTypeReq, user.getEmailId());
    			responseInfo = getExpensePerAccType(userExpensePerAccList,userInfo.getUsername());
    		}
    		else{
    			//userTotalExepense = convertObjectArrayToModel(userObjectExpenseList);
    			expenseForAllAccType = convertObjectArrayToModel(userObjectExpenseList);
    			responseInfo = new ResponseInfo();
    			responseInfo.setData(expenseForAllAccType);
    			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
    			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
    			responseInfo.setMsg("data fetched for all account");
    		}
    	  
		  // System.out.println("userExpenseList:"+mapper.writeValueAsString(userExpenseList));
      }
	   catch (Exception e) {
			System.out.println("exception in getExpense:"+e);
			responseInfo = new ResponseInfo();
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
			responseInfo.setReason("");
		 }
	  return responseInfo;
	}
	private ExpenseForAllAccType convertObjectArrayToModel(List<Object[]> objArray){
		//UserTotalExepense userTotalExepense = new UserTotalExepense();
		Map<String,Map<String,AccExpenseData>> accTypeToExp = new HashMap<>();   
		Map<String,AccExpenseData> accToExp = new HashMap<>();
		ExpenseForAllAccType ExpenseForAllAccType = null;
		for(Object[] object: objArray){
		  String accType = (String)object[0]; 
		  String accName = (String)object[1];
		  Float amount = (Float)object[2];
		  String crtDate = String.valueOf(object[3]);
		  String desc = (String)object[4];
		  AccExpenseData accExpenseData = new AccExpenseData(accName,amount,DateUtil.convertDBStringToViewString(crtDate),desc);
		  if(accTypeToExp.get(accType) == null){
			  accToExp.put(accName, accExpenseData);
			  accTypeToExp.put(accType, accToExp);
		  }
		  else{
			  if(accTypeToExp.get(accType).get(accName) == null){
				  accTypeToExp.get(accType).put(accName, accExpenseData);
			  }
			  else{
				  float amnt = accTypeToExp.get(accType).get(accName).getAmnt();
				  accTypeToExp.get(accType).get(accName).setAmnt(amnt+accExpenseData.getAmnt());
			  }
		  }
		}
		  ExpenseForAllAccType = new ExpenseForAllAccType();
		  Map<String,AccExpenseData> accToExpTemp = null;
		  ExpenseWithAccType expenseWithAccType = null;
		  for(String accTypeKey:accTypeToExp.keySet()){
			  accToExpTemp = accTypeToExp.get(accTypeKey);
			  expenseWithAccType = new ExpenseWithAccType();
			  expenseWithAccType.setAccType(accTypeKey);
			  for(String accNameKey:accToExpTemp.keySet()){
				  expenseWithAccType.getAccExpList().add(accToExpTemp.get(accNameKey));
			  }
			  ExpenseForAllAccType.getExpenseWithAccTypeList().add(expenseWithAccType);
			  expenseWithAccType = null;
		  }
		  
		  return ExpenseForAllAccType;
		  /* if(userTotalExepense.getExpenseforAlAcc().get(accType) == null){
		   ExpensePerAccount expensePerAccount = new ExpensePerAccount();
			   userTotalExepense.getExpenseforAlAcc().put(accType, expensePerAccount);
		   }
		   System.out.println("----------------");
		   Map<String,Float> expensePerAcc = userTotalExepense.getExpenseforAlAcc().get(accType).getExpensePerAcc();
		   if(expensePerAcc.get(accName) == null){
			   expensePerAcc.put(accName, amount);
		   }
		   else{
			   System.out.println("----------------");
			   expensePerAcc.put(accName,expensePerAcc.get(accName)+amount);
		   }
		 }
		    return userTotalExepense;*/

	}
	private List<UserExpense> convertObjectArrayToModelForAcc(List<Object[]> objArray){
		List<UserExpense> userExpenseList = new ArrayList();
		for(Object[] object: objArray){
		      UserExpense userExpense= new UserExpense();
		      userExpense.setAmount((Float)object[0]);
		      userExpense.setDateOfExpnse((Date)object[1]);
		      userExpense.setDesc((String)object[2]);
		      userExpense.setUserAccount((UserAccount)object[3]);
		      userExpense.setUserDetails((UserDetails)object[4]);
		      userExpenseList.add(userExpense);
		    }
		    return userExpenseList;
		
	}
	private ResponseInfo getExpensePerAccType(List<UserExpense> userExpenseList, String email){
	   ResponseInfo responseInfo = new ResponseInfo();
	   AccExpenseData accExpnseData = null;
	   List<UserAccount> userAccountList = null;
	   String accType=null;
	   List<AccExpenseData> accExpnseDataList = new ArrayList<>();
	   Map<String, List<AccExpenseData>> rspDatamap = new HashMap<>();
	   Map<Long, UserAccount> accMap = new HashMap<>();   // 0 index of list is type, 1 is subtype
	   
	   userAccountList = expenseDetailDaoImp.getUsrAccountDetail(email);   // TODO.. Need to get it from UserDao
       System.out.println("userAccountList:"+userAccountList);
       for(UserAccount userAccount:userAccountList){
	    	accMap.put(userAccount.getId(), userAccount);
       }
	   
	   ColTitle colTitle = new ColTitle();
	   colTitle.setAccName("Account Name");
	   colTitle.setAmnt("Amount");
	   colTitle.setDate("Date Of Expense");
	   colTitle.setDesc("Description");
	   List<String> colKey = new ArrayList<>();
	   Field[] field = ColTitle.class.getDeclaredFields();
	   for(Field f:field){
		   String str = String.valueOf(f);        // will come like: "public java.lang.String org.munsiji.resourceController.Test.name"
		   colKey.add(str.substring((str.lastIndexOf(".")+1)));
	   }
	   for(UserExpense userExpense:userExpenseList){
			 //System.out.println("userExpense:"+mapper.writeValueAsString(userExpense));
			 
		     String name = accMap.get(userExpense.getUserAccount().getId()).getName();
		     accType = accMap.get(userExpense.getUserAccount().getId()).getType();
		     accExpnseData = new AccExpenseData(accMap.get(userExpense.getUserAccount().getId()).getName(),
		    		 		userExpense.getAmount(),DateUtil.convertDBStringToViewString(String.valueOf(userExpense.getDateOfExpnse())), userExpense.getDesc());
		   /*  accExpnseData.setAccName();
		     accExpnseData.setAmnt();
		     accExpnseData.setDesc();
		     accExpnseData.setDate(); //TODO...
*/		     //accExpnseDataList = rspDatamap.get(type);
		     accExpnseDataList = rspDatamap.get(name);
		     if(accExpnseDataList == null){
		    	 accExpnseDataList = new ArrayList();
		     }
		     accExpnseDataList.add(accExpnseData);
		     rspDatamap.put(name, accExpnseDataList);
	   }
	   ExpenseInfoRes expenseInfoRes = new ExpenseInfoRes();
	   rspDatamap.forEach((type, accExpnseDataLst) -> {
			 System.out.println((type + ":" + accExpnseDataLst));
			 Float expnseSum = 0f;
			 for(AccExpenseData accExpnseDat : accExpnseDataLst){
				 expnseSum = expnseSum + accExpnseDat.getAmnt();
			 }
			 Header header = new Header();
			 header.setTitle(type);
			 header.setBal(expnseSum);
			 AccountContent accountContent = new AccountContent();
			 accountContent.setHeader(header);
			 accountContent.setData(accExpnseDataLst);
			 expenseInfoRes.getContent().add(accountContent);
	   });
	   expenseInfoRes.setAccType(accType);
	   expenseInfoRes.setColKey(colKey);
	   expenseInfoRes.setColTitle(colTitle);
	   //System.out.println("expenseInfoRes:"+mapper.writeValueAsString(expenseInfoRes));
	   responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
	   responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
	   responseInfo.setData(expenseInfoRes);
	   responseInfo.setMsg(MunsijiServiceConstants.OPER_MSG); 
	   return responseInfo;
	 }

}
