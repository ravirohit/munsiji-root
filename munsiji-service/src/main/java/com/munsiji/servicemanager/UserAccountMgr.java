package com.munsiji.servicemanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.munsiji.commonUtil.DateUtil;
import com.munsiji.commonUtil.GeneratePassword;
import com.munsiji.commonUtil.MunsijiServiceConstants;
import com.munsiji.commonUtil.UserContextUtils;
import com.munsiji.customthread.CustomExecutors;
import com.munsiji.customthread.MailThread;
import com.munsiji.model.AccTypeMapToName;
import com.munsiji.model.Login;
import com.munsiji.persistance.daoImp.UserDetailDaoImp;
import com.munsiji.persistance.resource.UserAccount;
import com.munsiji.persistance.resource.UserDetails;
import com.munsiji.reqresObject.AccExpenseData;
import com.munsiji.reqresObject.ResponseInfo;
import com.munsiji.reqresObject.UserAccountReq;
import com.munsiji.reqresObject.UserDetailReq;
import com.munsiji.reqresObject.UserProfileInfo;

@Component
public class UserAccountMgr {
	
	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	public ResponseInfo registerUser(UserDetailReq userDetailReq){
		List<UserDetails> userList = null;
		ResponseInfo responseInfo = new ResponseInfo();
		UserDetails userDetails = dozerBeanMapper.map(userDetailReq, UserDetails.class);
		System.out.println("registerUser userDetailDaoImp:"+userDetails.getEmailId()+" :"+userDetails.getPwd());
		try{
			userList = userDetailDaoImp.getUserInfo(userDetails.getEmailId(),null,null);   
			if((userList.size() == 0)){
				userDetailDaoImp.registerUser(userDetails,false);
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_REGISTER_MSG);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
				return responseInfo;
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg(MunsijiServiceConstants.FAILURE_REGISTER_MSG);
				responseInfo.setReason("");
				responseInfo.setStatusCode(MunsijiServiceConstants.MULTIPLE_RECORD_ERROR_CODE);
				return responseInfo;
			}
	  }catch(Exception e){
		  e.printStackTrace();
		  responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		  responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
		  responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		  responseInfo.setReason("");
		  return responseInfo;
	  }
		
	}
	public ResponseInfo login(Login login) throws UnsupportedEncodingException{
		ResponseInfo responseInfo = new ResponseInfo();
		List<UserDetails> userList = null;
		try{
			userList = userDetailDaoImp.getUserInfo(login.getUserName(),login.getPwd(),null);
			if((userList.size() == 1)){
				UserDetails userDetails = userList.get(0);
				/*Random random = new Random();
				int n = random.nextInt(1000);
				String str = login.getUserName() + n +login.getPwd();
				byte[] byteArray = str.getBytes("utf-8");
				String base64String =Base64.getEncoder().encodeToString(byteArray);
				String keyToStore = new StringBuffer("|").append(Base64.getEncoder().encodeToString(byteArray)).append("|").toString();
				if((userDetails.getKey() == null)||(userDetails.getKey().trim().equals("")))
				{
					userDetails.setKey(keyToStore);
				}
				else{
					String storeKey = userDetails.getKey();
					String keyAfterAppendNewKey = new StringBuffer(storeKey).append(keyToStore).toString();
					userDetails.setKey(keyAfterAppendNewKey);
					
				}*/
				String base64String = userDetails.addCurrentKeyForLogin(login.getUserName(), login.getPwd());
				userDetailDaoImp.registerUser(userDetails, true);				
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setMsg(base64String);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			}
			else{
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setMsg("Username or password is wrong");
				responseInfo.setReason("User is not authorized");
				responseInfo.setStatusCode(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE);
			}
		}catch(Exception e){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
			responseInfo.setReason("");
		}
		return responseInfo;
	}
	public ResponseInfo resetPwd(String email, String newPwd){
		ResponseInfo responseInfo = new ResponseInfo();
		GeneratePassword generatePassword = null;
		List<UserDetails> userList = null;
		UserDetails userInfo = UserContextUtils.getUser();
		if(newPwd != null){
			email = userInfo.getUsername();
		}
		userList =  userDetailDaoImp.getUserInfo(email,null,null);
		
		try{
			 if(userList.size() != 0){
				 UserDetails userDetails = userList.get(0);
				 if(newPwd == null){
					 generatePassword = new GeneratePassword();
					 newPwd = generatePassword.getPassword();
					 CustomExecutors.executeThread(new MailThread(userDetails.getEmailId(), newPwd), "sending forget password service");
					 System.out.println("New password sent to your emailID:"+newPwd);
					 responseInfo.setMsg("New password sent to your emailID. If not received please try after sometime");
					 responseInfo.setReason("user forgot password");
				 }
				 else{
					 responseInfo.setMsg("User password has been changed");
					 responseInfo.setReason("Change password");
				 }
				 userDetails.setPwd(newPwd);
				 userDetails.setKey("");
				 userDetailDaoImp.registerUser(userDetails, true);
				 System.out.println("password saved to the db:"+newPwd);
				 responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				 responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			 }
			 else{
				 responseInfo.setData(null);
				 responseInfo.setMsg("User does not exist. Please Sing up to create a new account.");
				 responseInfo.setReason("User does not exist");
				 responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				 responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				 
			 }
		}
		catch(Exception e){
			System.out.println("Exception occur while reseting user password:"+e);
			 responseInfo.setData(null);
			 responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			 responseInfo.setReason(MunsijiServiceConstants.SEVER_ERROR);
			 responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			 responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		System.out.println("status code in method:---------"+responseInfo.getStatusCode());
		return responseInfo;
	}
	public ResponseInfo logout(){
		List<UserDetails> userList = null;
		boolean status = false;
		ResponseInfo responseInfo = new ResponseInfo();
		UserDetails userDetails = UserContextUtils.getUser();
		System.out.println("user id while logging out:"+userDetails.getUsername());
		/*String storeKey = userDetails.getKey();
		String logOutKeyStr = new StringBuffer("|").append(userDetails.getCurrentLoginKey()).append("|").toString();
		int keyIndex = storeKey.indexOf(logOutKeyStr);
		String strBeforeKey = storeKey.substring(0,keyIndex);
		String strAfterKey = storeKey.substring(keyIndex+logOutKeyStr.length());
		String remainKey = new StringBuffer(strBeforeKey).append(strAfterKey).toString();
		userDetails.setKey(remainKey); */
		userDetails.removeCurrentKeyForLogout();
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
	  UserDetails userInfo = UserContextUtils.getUser();
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
			UserDetails userInfo = UserContextUtils.getUser();
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
	public ResponseInfo getUserProfile(){
		ResponseInfo responseInfo = new ResponseInfo();
		List<UserDetails> userList = null;
		List<UserAccount> userAccountList = null;
		UserProfileInfo userProfileInfo = new UserProfileInfo();
		String emailId = UserContextUtils.getUser().getUsername();
		System.out.println("getting user info");
		try{
			userList = userDetailDaoImp.getUserInfo(emailId, null, null);
			
			if((userList.size() == 1)){
				UserDetails userDetails = userList.get(0);
				userProfileInfo.setEmailId(userDetails.getEmailId());
				userProfileInfo.setUserName(userDetails.getUname());
				userProfileInfo.setMobNo(userDetails.getMobileNo());
			}
			System.out.println("getting user account info");
			userAccountList = userDetailDaoImp.getAccountInfo(emailId, null, null);
			Map<String,List<AccExpenseData>> accountInfo = userProfileInfo.getAccountInfo();
		
			for(UserAccount userAccount: userAccountList){
				if(accountInfo.get(userAccount.getType()) == null){
					accountInfo.put(userAccount.getType(), new ArrayList<AccExpenseData>());
				}
				String name = userAccount.getName();
				Float amount = userAccount.getInvestedAmnt();;
			   String date = DateUtil.convertDBStringToViewString(userAccount.getCrteDate());
			   String desc = userAccount.getDesc();
				AccExpenseData accExpenseData = new AccExpenseData(name, amount, date, desc);
				accountInfo.get(userAccount.getType()).add(accExpenseData);
			}
			responseInfo.setData(userProfileInfo);
			responseInfo.setMsg("User profile details");
			responseInfo.setReason("");
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		}
		catch(Exception e){
			System.out.println("Exception occur while fetching profile data");
			responseInfo.setData(null);
			responseInfo.setMsg("Account Details for addExpense screen");
			responseInfo.setReason("Exception occur while fetching data from DB");
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		
		return responseInfo;
	}

}
