package org.munsiji.resourceController;

import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.munsiji.persistance.resource.Test;
import org.munsiji.persistance.resource.UserDetails;
import org.munsiji.reqresObject.EnumTest;
import org.munsiji.reqresObject.ResponseInfo;
import org.munsiji.reqresObject.TestEnumReq;
import org.munsiji.reqresObject.UserAccountReq;
import org.munsiji.reqresObject.UserDetailReq;
import org.munsiji.reqresObject.UserExpenseReq;
import org.munsiji.servicemanager.ExpenseServiceMgr;
import org.munsiji.servicemanager.UserAccountMgr;
import org.munsiji.commonUtil.MunsijiServiceConstants;
//import org.munsiji.persistancetest.HibernateCfg;
//import org.munsiji.persistancetest.Test;
import org.munsiji.hibernateUtil.HibernateCfg;
import org.munsiji.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/myapp")
//@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Order") // need to test
public class MyResourceController {
	@Autowired
	HibernateCfg hibernateCfg;
	@Autowired
	UserAccountMgr userAccountMgr;
	@Autowired
	ExpenseServiceMgr expenseServiceMgr;
	@PostConstruct
	public void init(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	@RequestMapping(value="test", method = RequestMethod.GET)
    public String test(@RequestParam(value = "reqKey", required = false) String reqKey) {      // security related experiment
		System.out.println("test  service get callled:");
		String usrName = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDetails usrDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("usrDetials fromDB:"+usrName+"  "+usrDetails);
        Session session = hibernateCfg.getSession();
        Query query = session.createQuery(" from Test");
        List<Test> testlist = query.list();
        return ReqHoldingClass.getReqHoldingMap().get(reqKey);
    }
	@RequestMapping(value="gettest", method = RequestMethod.GET)
    public String getTest(@RequestParam(value = "reqKey", required = false) String reqKey) {
		System.out.println("getTest  service get callled with req param:"+reqKey);
		
        return ReqHoldingClass.getReqHoldingMap().get(reqKey);
    }
	@RequestMapping(value="test12", method = RequestMethod.POST)
    public ResponseInfo test12(@RequestBody TestEnumReq testEnumReq) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("req obj:"+mapper.writeValueAsString(testEnumReq));
			ReqHoldingClass.getReqHoldingMap().put(testEnumReq.getReqId(), testEnumReq.getRestUrl());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		ResponseInfo responseInfo =  new ResponseInfo();
		System.out.println("test service get callled:");
		
		EnumTest obj=EnumTest.SUNDAY;
        responseInfo.setData(obj);
        responseInfo.setMsg("msg");
        responseInfo.setStatus("Success");
        return responseInfo;
    }
	@RequestMapping(value="registeruser",method = RequestMethod.POST)
	public ResponseInfo register(@RequestBody UserDetailReq userDetailReq){
	  ResponseInfo responseInfo =  null;
	  System.out.println("UserDetails is :" + userDetailReq.getPwd());
	  responseInfo = userAccountMgr.registerUser(userDetailReq);
	  System.out.println("register controllerd excuted:"+responseInfo);
	  return responseInfo;
	}
	@RequestMapping(value="login",method=RequestMethod.POST)
	public ResponseInfo login(@RequestBody Login login){
		System.out.println("hiiiiiiiiiiii");
		ResponseInfo responseInfo = null;
		try{
			responseInfo = userAccountMgr.login(login);
		}
		catch(Exception e){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			responseInfo.setStatusCode(500);
		}
		return responseInfo;
		
	}
	@RequestMapping(value="logout",method=RequestMethod.GET)
	public ResponseInfo logout(){
		ResponseInfo responseInfo = null;
		try{
			responseInfo = userAccountMgr.logout();
		}
		catch(Exception e){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			responseInfo.setStatusCode(500);
		}
		return responseInfo;
		
	}
	@RequestMapping(value="createaccount",method = RequestMethod.POST)
	public ResponseInfo createAccount(@RequestBody UserAccountReq userAccountReq){
	  ResponseInfo responseInfo =  null;
	  System.out.println("account crete resource called");
	  responseInfo = userAccountMgr.createAccount(userAccountReq);
	  return responseInfo;
	}
	@RequestMapping(value="addexpense",method = RequestMethod.POST)
	public ResponseInfo addExpense(@RequestBody UserExpenseReq userExpenseReq){
	 ResponseInfo responseInfo =  null;
	 ObjectMapper mapper  = new ObjectMapper();
	  try {
		System.out.println("accName is :"+mapper.writeValueAsString(userExpenseReq));
		responseInfo = expenseServiceMgr.addExpense(userExpenseReq);
	  } catch (JsonProcessingException e) {
		System.out.println("exception occur in addExpense:"+e);
	  }
	  return responseInfo;
	}
	@RequestMapping(value="getexpense",method = RequestMethod.GET)
	public ResponseInfo getExpense(@RequestParam(value = "accType", required = false) String accType,
								   @RequestParam(value = "accName", required = false) String accName){
	  System.out.println("accName is :"+accType);
	  ResponseInfo responseInfo =  null;
	  ObjectMapper mapper  = new ObjectMapper();
	  try {
		System.out.println("accName is :");
		responseInfo = expenseServiceMgr.getExpense(accType,accName);
	  } catch (Exception e) {
		System.out.println("exception occur in addExpense:"+e);
	  }
	  return responseInfo;
	}
	@RequestMapping(value="getacctypeandname",method = RequestMethod.GET)
	public ResponseInfo getAccountTypeAndName(@RequestParam(value = "accType", required = false) String accType){
		System.out.println("getAccountTypeAndNamec method called");
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.getAccountInfo(accType);
		return responseInfo;
	}
   	
}
