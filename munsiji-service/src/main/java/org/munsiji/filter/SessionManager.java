package org.munsiji.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.munsiji.commonUtil.MunsijiServiceConstants;
import org.munsiji.commonUtil.UserContextUtils;
import org.munsiji.persistance.daoImp.UserDetailDaoImp;
import org.munsiji.persistance.resource.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SessionManager implements HandlerInterceptor{

	@Autowired
	UserDetailDaoImp userDetailDaoImp;
	
	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("aftercompletioncalled");
		UserContextUtils.clear();
		
		
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3)
			throws Exception {
		System.out.println("postHandle called");
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		boolean flag = true;
		List<UserDetails> userList = null;
		System.out.println("preHandle called");
		List<GrantAuthCol> list= new ArrayList<>();
		if((req.getHeader("auth-key") == null)){
			res.setStatus(MunsijiServiceConstants.BAD_REQUEST_ERROR_CODE);
			res.sendError(MunsijiServiceConstants.BAD_REQUEST_ERROR_CODE, "Bad Request");
			return false;
		}
		
		if(req.getHeader("auth-key").equals("login") || req.getHeader("auth-key").equals("register")){
			return flag;
		}
		System.out.println(req.getHeader("auth-key"));
		System.out.println(userDetailDaoImp);
		userList = userDetailDaoImp.getUserInfo(null,null,req.getHeader("auth-key")); 
		if(userList.size() == 0){
			flag = false;
			res.setStatus(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE);
			res.sendError(MunsijiServiceConstants.AUTHORIZATION_ERROR_CODE, "User is not Authorized");
			res.setContentType("application/json;charset=UTF-8");
		}
		else{
			UserDetails userDetails = userList.get(0);
			User user = new User(userDetails.getEmailId(),userDetails.getPassword(),list);
			UserContextUtils.setUser(user);
		}
		//Thread.sleep(5000);
		return flag;
	}
	

}
