package org.munsiji.commonUtil;

import org.springframework.security.core.userdetails.User;

public class UserContextUtils {
	
	private static final ThreadLocal<User> CONTEXT = new ThreadLocal<>();

    public static void setUser(User user) {
        CONTEXT.set(user);
    }

    public static User getUser() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
