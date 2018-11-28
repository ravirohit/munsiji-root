package org.munsiji.servicemanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dozer.DozerBeanMapper;
import org.munsiji.commonUtil.DateUtil;
import org.munsiji.commonUtil.MunsijiServiceConstants;
import org.munsiji.commonUtil.UserContextUtils;
import org.munsiji.model.AccTypeMapToName;
import org.munsiji.model.Login;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.munsiji.persistance.resource.UserAccount;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.reqresObject.ResponseInfo;
import org.munsiji.reqresObject.UserAccountReq;
import org.munsiji.reqresObject.UserDetailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
		userList = userDetailDaoImp.getUserInfo(userDetails.getEmailId(),null,null);    //TODO ...
		if(userList == null){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
			responseInfo.setReason("");
			return responseInfo;
		}else if((userList.size() == 0)){
			status = userDetailDaoImp.registerUser(userDetails,false);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg("Account Created Successfully");
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
				return responseInfo;
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
				responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				responseInfo.setReason("");
				return responseInfo;
			}
		}
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg("User Account already exists for the given email ID");
			responseInfo.setReason("");
			responseInfo.setStatusCode(MunsijiServiceConstants.MULTIPLE_RECORD_ERROR_CODE);
			return responseInfo;
		}
	}
	public ResponseInfo login(Login login) throws UnsupportedEncodingException{
		ResponseInfo responseInfo = new ResponseInfo();
		List<UserDetails> userList = null;
		Boolean status = null;
		userList = userDetailDaoImp.getUserInfo(login.getUserName(),login.getPwd(),null);
		if((userList.size() == 1)){
			UserDetails userDetails = userList.get(0);
			Random random = new Random();
			int n = random.nextInt(1000);
			String str = login.getUserName() + n +login.getPwd();
			byte[] byteArray = str.getBytes("utf-8");
			String base64String = Base64.getEncoder().encodeToString(byteArray);
			userDetails.setKey(base64String);
			status = userDetailDaoImp.registerUser(userDetails, true);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg(base64String);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
				responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				responseInfo.setReason("");
			}
		}
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg("Username or password is wrong");
			responseInfo.setReason("User is not authorized");
			responseInfo.setStatusCode(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE);
		}
		return responseInfo;
	}
	public ResponseInfo logout(){
		List<UserDetails> userList = null;
		boolean status = false;
		ResponseInfo responseInfo = new ResponseInfo();
		User userInfo = UserContextUtils.getUser();
		userList = userDetailDaoImp.getUserInfo(userInfo.getUsername(),null,null);    //TODO ...
		System.out.println("user id while logging out:"+userInfo.getUsername());
		if((userList.size() == 1)){
			UserDetails userDetails = userList.get(0);
			userDetails.setKey("");
			status = userDetailDaoImp.registerUser(userDetails, true);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
				responseInfo.setMsg("User logout successfully");
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
				responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				responseInfo.setReason("");
			}
		}
		else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg("multiple user exist for the provided emailId");
			responseInfo.setStatusCode(MunsijiServiceConstants.MULTIPLE_RECORD_ERROR_CODE);
			responseInfo.setReason("One emailId can't for multiple user");
		}
		return responseInfo;
		
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
	  User userInfo = UserContextUtils.getUser();
	  user = new UserDetails();      //TODO...
	  user.setEmailId(userInfo.getUsername());
	  System.out.println("<<<< userId for create account:"+userInfo.getUsername());   // username is email_id
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
		responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		responseInfo.setReason("");
		return responseInfo;
	  }
	  else if(userAccountList.size() == 0){
		  status = userDetailDaoImp.saveAccountInfo(uerAccount);
			if(status){
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
				responseInfo.setMsg("Investment account type Created Successfully");
				return responseInfo;
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
				responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				responseInfo.setReason("");
				return responseInfo;
			}
	  }
	  else{
		responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		responseInfo.setStatusCode(MunsijiServiceConstants.MULTIPLE_RECORD_ERROR_CODE);
		responseInfo.setMsg("Investment account type already exist for the given name and type");
		responseInfo.setReason("Dublicate account under one account type can't be created");
		return responseInfo;
		  
	  }
	  
	}
	public ResponseInfo getAccountInfo(String accTypeToBeUsed){
		ResponseInfo responseInfo = new ResponseInfo() ;
		AccTypeMapToName accountInfo = new AccTypeMapToName();
		Map<String,List<String>> accTypeMaptoName = accountInfo.getAccountDetail();
		try{
			User userInfo = UserContextUtils.getUser();
			List<UserAccount> userAccountList = userDetailDaoImp.getAccountInfo(userInfo.getUsername(),null,null);   // username is email Id
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
			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		}
		catch(Exception e){
			System.out.println("Exception occur while fetching data from DB");
			responseInfo.setData(null);
			responseInfo.setMsg("Account Details for addExpense screen");
			responseInfo.setReason("Exception occur while fetching data from DB");
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		return responseInfo;
		
	}

}
