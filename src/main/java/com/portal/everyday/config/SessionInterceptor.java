package com.portal.everyday.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false); // don't create new session
        String uri = request.getRequestURI(); // like /user/userHome or /admin/adminHome

        // Check admin routes
        if (uri.startsWith("/admin")) {
            boolean isAdminLoggedIn = session != null && session.getAttribute("adminKey") != null;
            if (!isAdminLoggedIn) {
            	// create session and add message
                request.getSession(true).setAttribute("mssg", "Un-authorised access, pls do login 😢😢😢");
                response.sendRedirect(request.getContextPath() + "/admin/adminLogin");
                return false;
            }
        }

        // Check user routes
        else if (uri.startsWith("/user")) {
            boolean isUserLoggedIn = session != null && session.getAttribute("userKey") != null;
            if (!isUserLoggedIn) {
            	// create session and add message
            	request.getSession(true).setAttribute("mssg", "Please log in first to access your dashboard.😢😢");
                response.sendRedirect(request.getContextPath() + "/user/userLoginForm");
                return false;
            }
        }

        return true; // allow request to continue
    }
}
