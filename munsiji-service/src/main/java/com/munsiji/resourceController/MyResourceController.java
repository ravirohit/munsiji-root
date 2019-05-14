package com.munsiji.resourceController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munsiji.commonUtil.MunsijiServiceConstants;
import com.munsiji.commonUtil.ServerSentEventUtil;
import com.munsiji.commonUtil.ThreadPoolExecutorUtil;
import com.munsiji.customthread.CustomExecutors;
import com.munsiji.customthread.DocCleanThread;
import com.munsiji.hibernateUtil.HibernateCfg;
import com.munsiji.model.Login;
import com.munsiji.model.PwdReset;
import com.munsiji.persistance.resource.Test;
import com.munsiji.persistance.resource.UserDetails;
import com.munsiji.reqresObject.EnumTest;
import com.munsiji.reqresObject.ResponseInfo;
import com.munsiji.reqresObject.TestEnumReq;
import com.munsiji.reqresObject.UserAccountReq;
import com.munsiji.reqresObject.UserDetailReq;
import com.munsiji.reqresObject.UserExpenseReq;
import com.munsiji.servicemanager.ExpenseServiceMgr;
import com.munsiji.servicemanager.UserAccountMgr;

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
	static AtomicInteger i = new AtomicInteger(0);
	private static List<Integer> list = new ArrayList<>();
	private static int b =0;
	
	@PostConstruct
	public void init(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@GetMapping("/streamssemvc") //http://localhost:8080/munsiji-service/rest/myapp/streamssemvc?reqKey=rohit
	public SseEmitter streamSseMvc(@RequestParam(value = "reqKey", required = false) String reqKey) {
		System.out.println("server sent event resource called:"+reqKey);
	    SseEmitter emitter = new SseEmitter(0L); // 0 or < 0 means this see timeout will for infinite time. else it's value is in millisecond
	    ServerSentEventUtil.setEmitter(reqKey, emitter);
	    emitter.onCompletion(() -> {
	    	try {
	    		if(ServerSentEventUtil.getEmitter(reqKey) != null)
	    		   ServerSentEventUtil.removeEmitter(reqKey);
	    		System.out.println("on completion method get called ......");
	    	} catch (Exception e) {
	    		System.out.println("Exception occcurrrrrrrred");
				e.printStackTrace();
			}
	    });
	    ThreadPoolExecutorUtil.getExecutorService().execute(() -> {
	        try {
	            for (int i = 0; true; i++) {
	            	System.out.println("---- inside of emitter");
	                Thread.sleep(5000);
	                emitter.send("New message:"+i);
	            }
	        } catch (Exception ex) {
	            System.out.println("------ Connection has been reset from client end -------");
	        }
	    });
	    return emitter;
	}
	@GetMapping("/deletesseevent")
	public void deleteSseEventForCurrentThread(@RequestParam(value = "reqKey", required = false) String reqKey){
		System.out.println("==================="+reqKey);
		SseEmitter emitter = ServerSentEventUtil.getEmitter(reqKey);
		if(ServerSentEventUtil.getEmitter(reqKey) != null){
		   emitter.complete();
		   ServerSentEventUtil.removeEmitter(reqKey);
		}
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
	@RequestMapping(value="checkdbindex", method = RequestMethod.GET)
    public String getTest(@RequestParam(value = "reqKey", required = false) String reqKey) {
		Session session = hibernateCfg.getSession();        
        for(int i = 0; i < 30000; i++)
        {
        	Random rn = new Random();
        	Integer id = rn.nextInt(Integer.MAX_VALUE);
        	if(!list.contains(id)){
        	  Test test = new Test(id, "email"+id,"name"+id);
        	  list.add(id);
        	  session.save(test);
        	}
        	
        }
        System.out.println("save statement exeuted");
        session.flush();
        System.out.println("save data flushed");
		
        return "added value";
    }
	@RequestMapping(value="holdreqthread",method=RequestMethod.GET)
	public String getNotification() throws InterruptedException{
		System.out.println("notification method get called:"+i.getAndIncrement());
		Thread.sleep(10000);
		System.out.println("waiting time completed");
		
		return "new notification to the client";
	}
	@RequestMapping(value="holdreqthreadinfite",method=RequestMethod.GET)
	public String holdReqThreadInfinte() throws InterruptedException{
		System.out.println("infinite hold request method called:"+i.getAndIncrement());
		Thread.sleep(60000);
		System.out.println("Waiting time completed");
		return "hold req infinite time";
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
	@RequestMapping(value="registeruser",method = RequestMethod.POST)   // done
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
	@RequestMapping(value="forgetpassword",method=RequestMethod.GET)
	public ResponseEntity<ResponseInfo> passwordReset(@RequestParam(value = "emailId", required = false) String emailId,
													  @RequestParam(required = false) String newPwd){
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
	@RequestMapping(value="resetpassword",method=RequestMethod.POST)
	public ResponseEntity<ResponseInfo> resetPwd(@RequestBody PwdReset pwdReset){
		System.out.println("pwdreset resource method get called:");
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.pwdReset(pwdReset);
		return new ResponseEntity<>(responseInfo,HttpStatus.OK);
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
		responseInfo = expenseServiceMgr.addExpense(userExpenseReq);
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
	  ResponseInfo responseInfo =  null;
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
	public void downloadExpenseFile(@RequestParam(value="email",required = false)String emailId,HttpServletResponse response){
		File file = null;
		boolean status;
		String accName[] = {"acc1","acc2"};
		//String emailId =   "ravi.swd.rohit@gmail.com";            // UserContextUtils.getUser().getUsername();
		String fileName = null;
		if(emailId.indexOf("@") != -1){
			fileName = new StringBuffer(emailId.substring(0, emailId.indexOf("@"))).append(".pdf").toString();
		}
		else{
			fileName = new StringBuffer(emailId).append(".pdf").toString();
		}
		
		System.out.println("file name is:"+fileName);
		status = expenseServiceMgr.createExpFileToDownload("personalexp",accName, null, null, fileName,emailId);
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
	@RequestMapping(value = "changeaccountstate",method=RequestMethod.POST)
	public ResponseEntity<ResponseInfo> changeAccountState(@RequestBody Map<String,Boolean> map){
		System.out.println("changeAccountState called:"+map);
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.changeAccountState(map);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}
	@RequestMapping(value = "editprofile",method=RequestMethod.POST)
	public ResponseEntity<ResponseInfo> editProfile(@RequestBody Map<String,?> map){
		System.out.println("edit acccount api called:"+map);
		ResponseInfo responseInfo = null;
		responseInfo = userAccountMgr.editProfile(map);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}
   	
}

