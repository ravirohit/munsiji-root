package org.munsiji.resourceController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

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
import org.munsiji.commonUtil.UserContextUtils;
//import org.munsiji.persistancetest.HibernateCfg;
//import org.munsiji.persistancetest.Test;
import org.munsiji.hibernateUtil.HibernateCfg;
import org.munsiji.model.Login;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munsiji.customthread.CustomExecutors;
import com.munsiji.customthread.DocCleanThread;

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
	@Autowired
	@Qualifier("jobLauncher")
	SimpleJobLauncher jobLauncher;
	@Autowired
	@Qualifier("expenseResultJob")
	Job job;
	
	@PostConstruct
	public void init(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@RequestMapping(value="startbatch", method = RequestMethod.GET)
	public String startBatch(){
		System.out.println("batch starting endpoint get called");
		System.out.println(jobLauncher);   // checking if autoInjection working fine for batch purpose
		System.out.println(job);
		try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Exit Status : "+ execution.getStatus());
      
        } catch (Exception e) {
            System.out.println("Job ExamResult failed");
            e.printStackTrace();
        }
		return "batch started";
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
	@RequestMapping(value="getdata", method = RequestMethod.GET)
    public String getTest(@RequestParam(value = "reqKey", required = false) String reqKey) {
		System.out.println("getTest  service get callled with req param:"+reqKey);
        return ReqHoldingClass.getReqHoldingMap().get(reqKey);
    }
	@RequestMapping(value="putdata", method = RequestMethod.POST)
    public ResponseEntity<ResponseInfo> test12(@RequestBody TestEnumReq testEnumReq, HttpServletResponse response ) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("req obj:"+mapper.writeValueAsString(testEnumReq));
			ReqHoldingClass.getReqHoldingMap().put(testEnumReq.getReqId(), testEnumReq.getRestUrl());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		ResponseInfo responseInfo =  new ResponseInfo();		
		EnumTest obj=EnumTest.SUNDAY;
        responseInfo.setData(obj);
        responseInfo.setMsg("msg");
        responseInfo.setStatus("Success");
        //return responseInfo;
        return new ResponseEntity<>(responseInfo,HttpStatus.FORBIDDEN);
    }
	@RequestMapping(value="registeruser",method = RequestMethod.POST)
	public ResponseEntity<ResponseInfo> register(@RequestBody UserDetailReq userDetailReq){
	  ResponseInfo responseInfo =  null;
	  responseInfo = userAccountMgr.registerUser(userDetailReq);
	  if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
		  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
	  }
	  else{
		  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
	  }
	  
	}
	@RequestMapping(value="login",method=RequestMethod.POST)
	public ResponseEntity<ResponseInfo> login(@RequestBody Login login){
		ResponseInfo responseInfo = null;
		try{
			responseInfo = userAccountMgr.login(login);
		}
		catch(Exception e){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			responseInfo.setStatusCode(500);
			return new ResponseEntity<>(responseInfo,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
			  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
		}
		else if(responseInfo.getStatusCode() == MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE){
			return new ResponseEntity<>(responseInfo,HttpStatus.FORBIDDEN);
		}
		else{
			  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
		}
		
	}
	@RequestMapping(value="resetpassword",method=RequestMethod.POST)
	public ResponseEntity<ResponseInfo> passwordReset(@RequestParam(value = "emailId", required = false) String emailId,@RequestBody(required = false) String newPwd){
		System.out.println("Password reset method called----:"+emailId+"  newpwd:"+newPwd);
		if(newPwd != null){
			int sindex = newPwd.indexOf(":") + 2;
			int lindex = newPwd.lastIndexOf("\"");
			newPwd = newPwd.substring(sindex, lindex);
			System.out.println("sent new pwd:"+newPwd);
		}
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.resetPwd(emailId,newPwd);
		if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
			  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
		 }
		 else{
			  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
		 }
	}
	@RequestMapping(value="logout",method=RequestMethod.GET)
	public ResponseEntity<ResponseInfo> logout(){
		ResponseInfo responseInfo = null;
		try{
			responseInfo = userAccountMgr.logout();
		}
		catch(Exception e){
			responseInfo.setStatus(MunsijiServiceConstants.FAILURE);
			responseInfo.setMsg(MunsijiServiceConstants.SEVER_ERROR);
			responseInfo.setReason("");
			responseInfo.setStatusCode(MunsijiServiceConstants.SERVER_ERROR_CODE);
		}
		if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
			  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
		 }
		 else{
			  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
		 }
		
	}
	@RequestMapping(value="createaccount",method = RequestMethod.POST)
	public ResponseEntity<ResponseInfo> createAccount(@RequestBody UserAccountReq userAccountReq){
	  ResponseInfo responseInfo =  null;
	  System.out.println("account crete resource called");
	  responseInfo = userAccountMgr.createAccount(userAccountReq);
	  if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
		  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
	  }
	  else{
		  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
	  }
	}
	@RequestMapping(value="addexpense",method = RequestMethod.POST)
	public ResponseEntity<ResponseInfo> addExpense(@RequestBody UserExpenseReq userExpenseReq){
	 ResponseInfo responseInfo =  null;
	 ObjectMapper mapper  = new ObjectMapper();
	  try {
		System.out.println("accName is :"+mapper.writeValueAsString(userExpenseReq));
		responseInfo = expenseServiceMgr.addExpense(userExpenseReq);
	  } catch (JsonProcessingException e) {
		System.out.println("exception occur in addExpense:"+e);
	  }
	  if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
		  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
	  }
	  else{
		  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
	  }
	}
	@RequestMapping(value="getexpense",method = RequestMethod.GET)
	public ResponseEntity<ResponseInfo> getExpense(@RequestParam(value = "accType", required = false) String accType,
								   @RequestParam(value = "accName", required = false) String accName,
								   @RequestParam(value = "startDate", required = false) String startDate,
								   @RequestParam(value = "endDate", required = false) String endDate){
	  System.out.println("request param for getExpense call: \n accType  :"+accType+" accName:"+accName+ "  startDate:"+startDate+"  endDate:"+endDate);
	  ResponseInfo responseInfo =  null;
	 // ObjectMapper mapper  = new ObjectMapper();
	  responseInfo = expenseServiceMgr.getExpense(accType,accName, startDate, endDate);
	  if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
		  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
	  }
	  else{
		  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
	  }
	}
	@RequestMapping(value="getacctypeandname",method = RequestMethod.GET)
	public ResponseEntity<ResponseInfo> getAccountTypeAndName(@RequestParam(value = "accType", required = false) String accType){
		System.out.println("getAccountTypeAndNamec method called");
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.getAccountInfo(accType);
		if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
			  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
		}
		else{
			  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
		}
	}
	//http://websystique.com/springmvc/spring-mvc-4-file-download-example/
	@RequestMapping(value="/download",method = RequestMethod.GET)
	public void downloadExpenseFile(HttpServletResponse response){
		File file = null;
		boolean status;
		String accName[] = {"acc1","acc2"};
		String emailId =   "ravi.swd.rohit@gmail.com";            // UserContextUtils.getUser().getUsername();
		String fileName = emailId.substring(0, emailId.indexOf("@")) + ".pdf";
		System.out.println("file name is:"+fileName);
		status = expenseServiceMgr.createExpFileToDownload("personalexp",accName, null, null, fileName);
		if(status){
			try {
			file = new File("C:/Project_Work/Other_learn/munsiji-root/munsiji-service/src/main/resources/pdf/"+fileName);  // for EXTERNAL_FILE_PATH
	        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
	        if(mimeType==null){
	            mimeType = "application/octet-stream";
	        }
	        response.setContentType(mimeType);
	        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
	        response.setContentLength((int)file.length());
	 
	        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	        //Copy bytes from source to destination(outputstream in this example), closes both streams.
		    FileCopyUtils.copy(inputStream, response.getOutputStream());
		    
		    CustomExecutors.executeThread(new DocCleanThread(file),"delete pdf doc file");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	@RequestMapping(value="getprofile",method=RequestMethod.GET)
	public ResponseEntity<ResponseInfo> getUserProfile(){
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.getUserProfile();
		if(responseInfo.getStatusCode() == MunsijiServiceConstants.SUCCESS_STATUS_CODE){
			  return new ResponseEntity<>(responseInfo,HttpStatus.OK);
		}
		else{
			  return new ResponseEntity<>(responseInfo,HttpStatus.BAD_REQUEST);
		}
	}
   	
}
