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
import org.munsiji.persistance.daoImp.ExpenseDetailDaoImp;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.persistance.resource.UserExpense;
import org.munsiji.reqresObject.AccExpnseData;
import org.munsiji.reqresObject.AccountContent;
import org.munsiji.reqresObject.ColTitle;
import org.munsiji.reqresObject.ContentData;
import org.munsiji.reqresObject.ExpenseInfoRes;
import org.munsiji.reqresObject.Header;
import org.munsiji.reqresObject.ResponseInfo;
import org.munsiji.reqresObject.UserDetailReq;
import org.munsiji.reqresObject.UserExpenseReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	
	public ResponseInfo addExpense(UserExpenseReq userExpenseReq, UserDetailReq user){
	  user.setEmailId("ravi.swd@gmail.com");
	  Date date = null;
	  List<UserAccount> userAccountList = null;
	  Boolean status = null;
	  ResponseInfo responseInfo = new ResponseInfo();
	  try{ 
	    System.out.println("object before mapping to entity:"+mapper.writeValueAsString(userExpenseReq));
	  
	    date = DateUtil.convertStringToDate(userExpenseReq.getDateOfExpnse());
		userExpenseReq.setDateOfExpnse(null);;
		UserExpense userExpense = dozerBeanMapper.map(userExpenseReq, UserExpense.class); // does not convert String date to Date
		userExpense.setDateOfExpnse(date);
		  //TODO...
		List<UserAccount> userAccountLst = userDetailDaoImp.getAccountInfo(user.getEmailId(), userExpenseReq.getAccType(), userExpenseReq.getAccName());
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
		UserDetails userDetails = userDetailDaoImp.getUserInfo(user.getEmailId()).get(0);
	    userExpense.setUserDetails(userDetails);
	    //System.out.println("expense saved to the db:"+mapper.writeValueAsString(userExpense));
	    status = expenseDetailDaoImp.saveExpense(userExpense);
		if(status){
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_MSG);
			return responseInfo;
		}
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			
			responseInfo.setStatusCode(500);
			return responseInfo;
		}
	  }
	  catch(Exception e){
		System.out.println("exception occur in mapper:"+e);
	  }
	  return responseInfo;
	}
	public ResponseInfo getExpense(String accTypeReq, UserDetailReq user){
	  user.setEmailId("ravi.swd@gmail.com");
	  List<String> list = null;
	  String accType=null;
	  List<UserAccount> userAccountList = null;
	  List<UserExpense> userExpenseList = null;
	  List<AccExpnseData> accExpnseDataList = new ArrayList<>();
	  AccExpnseData accExpnseData = null;
	  Map<Long, UserAccount> accMap = new HashMap<>();   // 0 index of list is type, 1 is subtype
	  Map<String, List<AccExpnseData>> rspDatamap = new HashMap();
      ResponseInfo responseInfo = new ResponseInfo();
      try {
        userAccountList = expenseDetailDaoImp.getUsrAccountDetail(user.getEmailId());   // TODO.. Need to get it from UserDao
        System.out.println("userAccountList:"+userAccountList);
        for(UserAccount userAccount:userAccountList){
	    	accMap.put(userAccount.getId(), userAccount);
        }
	   //System.out.println("UserDetails fetched:"+mapper.writeValueAsString(userAccountList)+"\n"+mapper.writeValueAsString(accMap));
	   
	   userExpenseList = expenseDetailDaoImp.getUsrExpense(accTypeReq, user.getEmailId());
	  // System.out.println("userExpenseList:"+mapper.writeValueAsString(userExpenseList));
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
		 accExpnseData = new AccExpnseData();
	     String name = accMap.get(userExpense.getUserAccount().getId()).getName();
	     accType = accMap.get(userExpense.getUserAccount().getId()).getType();
	     accExpnseData.setAccName(accMap.get(userExpense.getUserAccount().getId()).getName());
	     accExpnseData.setAmnt(userExpense.getAmount());
	     accExpnseData.setDesc(userExpense.getDesc());
	     accExpnseData.setDate(String.valueOf(userExpense.getDateOfExpnse())); //TODO...
	     //accExpnseDataList = rspDatamap.get(type);
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
		 for(AccExpnseData accExpnseDat : accExpnseDataLst){
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
	   responseInfo.setData(expenseInfoRes);
	   responseInfo.setMsg(MunsijiServiceConstants.OPER_MSG); 
	 }catch (Exception e) {
		System.out.println("exception in getExpense:"+e);
		responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
		responseInfo.setReason("");
		responseInfo.setStatusCode(500);
		return responseInfo;
	 }
      
		  
	  return responseInfo;
	}

}
