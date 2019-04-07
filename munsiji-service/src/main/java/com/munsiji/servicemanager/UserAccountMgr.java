package com.munsiji.servicemanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.munsiji.commonUtil.DateUtil;
import com.munsiji.commonUtil.EncryptDecryptData;
import com.munsiji.commonUtil.GeneratePassword;
import com.munsiji.commonUtil.MunsijiServiceConstants;
import com.munsiji.commonUtil.UserContextUtils;
import com.munsiji.customthread.CustomExecutors;
import com.munsiji.customthread.MailThread;
import com.munsiji.model.AccTypeMapToName;
import com.munsiji.model.Login;
import com.munsiji.model.PwdReset;
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
		EncryptDecryptData encryptDecryptData = new EncryptDecryptData();
		ResponseInfo responseInfo = new ResponseInfo();
		UserDetails userDetails = dozerBeanMapper.map(userDetailReq, UserDetails.class);
		System.out.println("registerUser userDetailDaoImp:"+userDetails.getEmailId()+" :"+userDetails.getPwd());
		try{
			userList = userDetailDaoImp.getUserInfo(userDetails.getEmailId(),null,null);   
			if((userList.size() == 0)){
				String hashedPwdValue = encryptDecryptData.convertTextToHashedValue(userDetails.getPwd());
				System.out.println("text value for pwd:"+userDetails.getPwd()+"  hashed value:"+hashedPwdValue);
				userDetails.setPwd(hashedPwdValue);
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
		  System.out.println("exception occur while registering the user:"+e);
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
		EncryptDecryptData encryptDecryptData = new EncryptDecryptData();
		try{
			String pwd = encryptDecryptData.convertTextToHashedValue(login.getPwd());
			userList = userDetailDaoImp.getUserInfo(login.getUserName(),pwd,null);
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
		UserDetails userDetails = UserContextUtils.getUser();
		if(newPwd != null){
			email = userDetails.getEmailId();
		}
		//userList =  userDetailDaoImp.getUserInfo(email,null,null);
		
		try{
			// if(userList.size() != 0){
				// UserDetails userDetails = userList.get(0);
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
			// }
			/* else{
				 responseInfo.setData(null);
				 responseInfo.setMsg("User does not exist. Please Sing up to create a new account.");
				 responseInfo.setReason("User does not exist");
				 responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				 responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
				 
			 }*/
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
	public ResponseInfo pwdReset(PwdReset pwdReset){
		ResponseInfo responseInfo = new ResponseInfo();
		try{
			UserDetails userDetails = UserContextUtils.getUser();
			if(pwdReset.getCurrentPwd() == null){
				responseInfo.setMsg(MunsijiServiceConstants.FAILURE_NEW_PWD_RESET);
				responseInfo.setReason("");
				responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
				responseInfo.setStatusCode(MunsijiServiceConstants.BAD_REQUEST_ERROR_CODE);
			}
			else if(userDetails.getPwd().equals(pwdReset.getCurrentPwd())){
				userDetails.setPwd(pwdReset.getNewPwd1());
				userDetails.setKey("");
				userDetails.setCurrentLoginKey("");
				userDetailDaoImp.registerUser(userDetails, true);
				responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_PWD_RESET);
				responseInfo.setReason("");
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			}
			else{
				responseInfo.setMsg(MunsijiServiceConstants.FAILURE_OLD_PWD_RESET);
				responseInfo.setReason("");
				responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
				responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
			}
		}
		catch(Exception e){
			System.out.println("Exception while resetting password:"+e);
			 responseInfo.setData(null);
			 responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			 responseInfo.setReason(MunsijiServiceConstants.SEVER_ERROR);
			 responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			 responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		return responseInfo;
	}
	public ResponseInfo logout(){
		List<UserDetails> userList = null;
		boolean status = false;
		ResponseInfo responseInfo = new ResponseInfo();
		UserDetails userDetails = UserContextUtils.getUser();
		System.out.println("user id while logging out:"+userDetails.getEmailId());
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
	//  UserDetails user = null;
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
	  uerAccount.setStatus(true);
	  UserDetails user = UserContextUtils.getUser();
	//  user = new UserDetails();      //TODO...
	  //user.setEmailId(userInfo.getUsername());
	  System.out.println("userId for create account:"+user.getEmailId());   // username is email_id
	  uerAccount.setUserDetails(user);
	  try{ 
		   System.out.println("object before mapping to entity:"+mapper.writeValueAsString(uerAccount));
		 
		  userAccountList = userDetailDaoImp.getAccountInfo(user.getEmailId(), uerAccount.getType(), uerAccount.getName(),false);
		  if(userAccountList == null){
		    responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
			responseInfo.setReason("");
			//return responseInfo;
		  }
		  else if(userAccountList.size() == 0){
			  status = userDetailDaoImp.saveAccountInfo(uerAccount);
				if(status){
					responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
					responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
					responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_ACCT_CREATE);
					//return responseInfo;
				}
				else{
					responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
					responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
					responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
					responseInfo.setReason("");
					//return responseInfo;
				}
		  }
		  else{
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.MULTIPLE_RECORD_ERROR_CODE);
			responseInfo.setMsg(MunsijiServiceConstants.FAILURE_DUPLICATE_ACCOUNT);
			responseInfo.setReason("");
			//return responseInfo;
		  }
	  }
	  catch(Exception e){
		  System.out.println("exception occur in mapper:"+e);
		  responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
		  responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
		  responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
	  }
	  return responseInfo;
	}
	public ResponseInfo getAccountInfo(String accTypeToBeUsed){
		ResponseInfo responseInfo = new ResponseInfo() ;
		AccTypeMapToName accountInfo = new AccTypeMapToName();
		Map<String,List<String>> accTypeMaptoName = accountInfo.getAccountDetail();
		try{
			UserDetails userInfo = UserContextUtils.getUser();
			List<UserAccount> userAccountList = userDetailDaoImp.getAccountInfo(userInfo.getEmailId(),null,null,false);   // username is email Id
			for(UserAccount userAccount: userAccountList){
				String accType = userAccount.getType();
				if(accTypeMaptoName.get(accType) == null){
					accTypeMaptoName.put(accType, new ArrayList<String>());
				}
				accTypeMaptoName.get(accType).add(userAccount.getName());
			}
			responseInfo.setData(accountInfo);
			responseInfo.setMsg(MunsijiServiceConstants.ACCT_INFO);
			responseInfo.setReason("");
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		}
		catch(Exception e){
			System.out.println("Exception occur while fetching data from DB:"+e);
			responseInfo.setData(null);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		return responseInfo;
		
	}
	public ResponseInfo getUserProfile(){
		ResponseInfo responseInfo = new ResponseInfo();
		List<UserAccount> userAccountList = null;
		UserProfileInfo userProfileInfo = new UserProfileInfo();
		//String emailId = UserContextUtils.getUser().getUsername();
		UserDetails userDetails = UserContextUtils.getUser();
		System.out.println("getting user info");
		try{
			//userList = userDetailDaoImp.getUserInfo(emailId, null, null);
			
		//	if((userList.size() == 1)){
				// UserDetails userDetails = userList.get(0);
				userProfileInfo.setEmailId(userDetails.getEmailId());
				userProfileInfo.setUserName(userDetails.getUname());
				userProfileInfo.setMobNo(userDetails.getMobileNo());
			//}
			System.out.println("getting user account info");
			userAccountList = userDetailDaoImp.getAccountInfo(userDetails.getEmailId(), null, null, true);
			Map<String,List<AccExpenseData>> accountInfo = userProfileInfo.getAccountInfo();
			for(UserAccount userAccount: userAccountList){
				if(accountInfo.get(userAccount.getType()) == null){
					accountInfo.put(userAccount.getType(), new ArrayList<AccExpenseData>());
				}
				String name = userAccount.getName();
				Float amount = userAccount.getInvestedAmnt();;
			    String date = DateUtil.convertDBStringToViewString(userAccount.getCrteDate());
			    String desc = userAccount.getDesc();
			    boolean status = userAccount.getStatus();
				AccExpenseData accExpenseData = new AccExpenseData(name, amount, date, desc,status);
				accountInfo.get(userAccount.getType()).add(accExpenseData);
			}
			responseInfo.setData(userProfileInfo);
			responseInfo.setMsg(MunsijiServiceConstants.USER_PROFILE_INFO);
			responseInfo.setReason("");
			responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
			responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		}
		catch(Exception e){
			System.out.println("Exception occur while fetching profile data:"+e);
			responseInfo.setData(null);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		
		return responseInfo;
	}
	public ResponseInfo changeAccountState(Map<String,Boolean> map){
		ResponseInfo responseInfo = new ResponseInfo();
		List<UserAccount> userAccountList = null;
		UserDetails userDetails = UserContextUtils.getUser();
		userAccountList = userDetailDaoImp.getAccountInfo(userDetails.getEmailId(), null, null,false);
		for(UserAccount userAccount: userAccountList){
			if(map.containsKey(userAccount.getName())){
				userAccount.setStatus(map.get(userAccount.getName()));
				userDetailDaoImp.saveAccountInfo(userAccount);
			}
		}
		responseInfo.setMsg("successfully saved");
		responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
		responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		return responseInfo;
	}
	public ResponseInfo editProfile(Map<String,?> map){
		ResponseInfo responseInfo = new ResponseInfo();
		EncryptDecryptData encryptToHash = new EncryptDecryptData();
		List<UserAccount> userAccountList = null;
		UserDetails userDetails = UserContextUtils.getUser();
		String editProfileFlag = (String)map.get("editProfileFlag");
		try{
			if(editProfileFlag.equals("accountStatus")){
				System.out.println("Changing acount status");
				userAccountList = userDetailDaoImp.getAccountInfo(userDetails.getEmailId(), null, null,false);
				for(UserAccount userAccount: userAccountList){
					if(map.containsKey(userAccount.getName())){
						userAccount.setStatus((boolean)map.get(userAccount.getName()));
						userDetailDaoImp.saveAccountInfo(userAccount);
					}
				}
				responseInfo.setMsg("Account status changed sucessfully");
				
			}
			else if(editProfileFlag.equals("passwordReset")){
				System.out.println("reset password operation is getting execute");
				String oldPwd = (String)map.get("oldPwd");
				String oldPwdHashValue = encryptToHash.convertTextToHashedValue(oldPwd);
				System.out.println("old pwd hash value:"+userDetails.getPwd()+" store old pwd value:"+userDetails.getPwd());
				if(userDetails.getPwd().equals(oldPwdHashValue)){   // need to convert enter pwd to hash and then compare
					String newPwd = (String)map.get("newPwd1");
					String newPwdHashValue = encryptToHash.convertTextToHashedValue(newPwd);
					System.out.println("new pwd hash value:"+newPwdHashValue);
					userDetails.setPwd(newPwdHashValue);
					userDetails.setKey("");
					userDetails.setCurrentLoginKey("");
					userDetailDaoImp.registerUser(userDetails, true);
					responseInfo.setMsg(MunsijiServiceConstants.SUCCESS_PWD_RESET);
				}
			}
			else if(editProfileFlag.equals("userInfoEdit")){
				System.out.println("user info edit operation called");
				if((map.get("newUname") != null)&&(!((String)map.get("newUname")).trim().equals("")))
				{
					if((userDetails.getUname() !=  null) && (userDetails.getUname().equals((String)map.get("oldUname")))){
						userDetails.setUname(((String)map.get("newUname")).trim());
					}
					else{
						userDetails.setUname(((String)map.get("newUname")).trim());    // need to be comment.... right now there is no username at signup ..so to check functionality
					}
				}
				if((map.get("newEmailId") != null)&&(!((String)map.get("newEmailId")).trim().equals(""))){
					if((userDetails.getEmailId() != null) && userDetails.getEmailId().equals((String)map.get("oldEmailId"))){
						userDetails.setEmailId(((String)map.get("newEmailId")).trim());
					}
				}
				if((map.get("newContactNo") != null)&&(!((String)map.get("newContactNo")).trim().equals(""))){
					if((userDetails.getMobileNo() != null) && userDetails.getMobileNo().equals((String)map.get("oldContactNo"))){
						userDetails.setMobileNo(((String)map.get("newContactNo")).trim());
					}
				}
					userDetailDaoImp.registerUser(userDetails, true);
					responseInfo.setMsg("user info updatd successfully");
					responseInfo.setReason("");
					responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
					responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
				}
		}
		catch(Exception e){
			System.out.println("Exception occur while upadte profile operation:"+e);
			responseInfo.setData(null);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		responseInfo.setReason("");
		responseInfo.setStatus(MunsijiServiceConstants.SUCCESS);
		responseInfo.setStatusCode(MunsijiServiceConstants.SUCCESS_STATUS_CODE);
		return responseInfo;
	}
	

}
