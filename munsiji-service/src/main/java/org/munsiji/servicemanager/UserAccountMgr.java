package org.munsiji.servicemanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.munsiji.commonUtil.DateUtil;
import org.munsiji.commonUtil.MunsijiServiceConstants;
import org.munsiji.model.AccTypeMapToName;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.reqresObject.ResponseInfo;
import org.munsiji.reqresObject.UserAccountReq;
import org.munsiji.reqresObject.UserDetailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserAccountMgr {
	
	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	public ResponseInfo registerUser(UserDetailReq userDetailReq){
		List<UserDetails> userList = null;
		// validate the reqObj
		//map it to the entity class object
		ResponseInfo responseInfo = new ResponseInfo();
		Boolean status = null;
		UserDetails userDetails = dozerBeanMapper.map(userDetailReq, UserDetails.class);
		System.out.println("registerUser userDetailDaoImp:"+userDetails.getEmailId()+" :"+userDetails.getPwd());
		userList = userDetailDaoImp.getUserInfo(userDetails.getEmailId());
		if(userList == null){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			responseInfo.setStatusCode(500);
			return responseInfo;
		}else if((userList.size() == 0)){
			status = userDetailDaoImp.registerUser(userDetails);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg("Account Created Successfully");
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
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg("User Account already exists for the given email ID");
			responseInfo.setReason("");
			responseInfo.setStatusCode(100);
			return responseInfo;
		}
	}
	public ResponseInfo createAccount(UserAccountReq userAccountReq){ 
	  UserDetails user = null;
	  Date date = null;
	  List<UserAccount> userAccountList = null;
	  Boolean status = null;
	  ResponseInfo responseInfo = new ResponseInfo();
	  ObjectMapper mapper = new ObjectMapper();
	  try{ 
	   System.out.println("object before mapping to entity:"+mapper.writeValueAsString(userAccountReq));
	  }
	  catch(Exception e){
		  System.out.println("exception occur in mapper:"+e);
		  
	  }
	  date = DateUtil.convertStringToDate(userAccountReq.getCrteDate());
	  userAccountReq.setCrteDate(null);
	  UserAccount uerAccount = dozerBeanMapper.map(userAccountReq, UserAccount.class); // does not convert String date to Date
	  uerAccount.setCrteDate(date);
	  uerAccount.setStatus(MunsijiServiceConstants.ACTIVE);
	  user = new UserDetails();      //TODO...
	  user.setEmailId("ravi.swd@gmail.com");
	  uerAccount.setUserDetails(user);
	  try{ 
		   System.out.println("object before mapping to entity:"+mapper.writeValueAsString(uerAccount));
		  }
		  catch(Exception e){
			  System.out.println("exception occur in mapper:"+e);
			  
		  }
	  userAccountList = userDetailDaoImp.getAccountInfo(user.getEmailId(), uerAccount.getType(), uerAccount.getName());
	  if(userAccountList == null){
	    responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
		responseInfo.setReason("");
		responseInfo.setStatusCode(500);
		return responseInfo;
	  }
	  else if(userAccountList.size() == 0){
		  status = userDetailDaoImp.saveAccountInfo(uerAccount);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg("Investment account type Created Successfully");
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
	  else{
		responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		responseInfo.setMsg("Investment account type already exist for the given name and type");
		responseInfo.setReason("");
		responseInfo.setStatusCode(100);
		return responseInfo;
		  
	  }
	  
	}
	public ResponseInfo getAccountInfo(String email){
		ResponseInfo responseInfo = new ResponseInfo() ;
		AccTypeMapToName accountInfo = new AccTypeMapToName();
		Map<String,List<String>> accTypeMaptoName = accountInfo.getAccountDetail();
		try{
			List<UserAccount> userAccountList = userDetailDaoImp.getAccountInfo(email,null,null);
			for(UserAccount userAccount: userAccountList){
				String accType = userAccount.getType();
				if(accTypeMaptoName.get(accType) == null){
					accTypeMaptoName.put(accType, new ArrayList<String>());
				}
				accTypeMaptoName.get(accType).add(userAccount.getName());
			}
			responseInfo.setData(accountInfo);
			responseInfo.setMsg("Account Details for addExpense screen");
			responseInfo.setReason("");
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setStatusCode(200);
		}
		catch(Exception e){
			System.out.println("Exception occur while fetching data from DB");
			responseInfo.setData(null);
			responseInfo.setMsg("Account Details for addExpense screen");
			responseInfo.setReason("Exception occur while fetching data from DB");
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(500);
		}
		return responseInfo;
		
	}

}
