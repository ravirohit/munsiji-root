package com.munsiji.commonUtil;

import org.springframework.security.core.userdetails.User;

import com.munsiji.persistance.resource.UserDetails;

public class UserContextUtils {
	
	private static final ThreadLocal<UserDetails> CONTEXT = new ThreadLocal<>();

    public static void setUser(UserDetails user) {
        CONTEXT.set(user);
    }

    public static UserDetails getUser() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
