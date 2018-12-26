package com.munsiji.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.munsiji.commonUtil.MunsijiServiceConstants;
import com.munsiji.commonUtil.UserContextUtils;
import com.munsiji.persistance.daoImp.UserDetailDaoImp;
import com.munsiji.persistance.resource.UserDetails;

@Component
public class SessionManager implements HandlerInterceptor{

	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	
	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3)
			throws Exception {
		try{
			UserContextUtils.clear();
		}
		catch(Exception e){
			System.out.println("exception occur in the afterCompletion method called");
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		boolean flag = true;
		List<UserDetails> userList = null;
		String endpointUrl = req.getRequestURI();
		String queryStr = req.getQueryString();
		List<GrantAuthCol> list= new ArrayList<>();
		if(endpointUrl.contains("login") || endpointUrl.contains("register") || endpointUrl.contains("download") || 
				(endpointUrl.contains("forgetpassword") && ((queryStr != null) && (queryStr.contains("emailId"))))){
			return flag;
		}
		if((req.getHeader("auth-key") == null)||(req.getHeader("auth-key").trim() == "")){
			res.setStatus(MunsijiServiceConstants.BAD_REQUEST_ERROR_CODE);
			res.sendError(MunsijiServiceConstants.BAD_REQUEST_ERROR_CODE, "Bad Request");
			return false;
		}
		String key = new StringBuffer("|%").append(req.getHeader("auth-key")).append("%|").toString();
		System.out.println("calling from filter");
		userList = userDetailDaoImp.getUserInfo(null,null,key);
		System.out.println("called from filter");
		if(userList.size() == 0){
			flag = false;
			res.setStatus(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE);
			res.sendError(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE, "User is not Authorized");
			res.setContentType("application/json;charset=UTF-8");
		}
		else{
			UserDetails userDetails = userList.get(0);
			User user = new User(userDetails.getEmailId(),req.getHeader("auth-key"),list);
			UserContextUtils.setUser(user);
		}
		//Thread.sleep(5000);
		return flag;
	}
	

}
