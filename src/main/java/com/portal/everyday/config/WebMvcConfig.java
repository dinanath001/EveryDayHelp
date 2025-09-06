package com.portal.everyday.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/admin/**", "/user/**") // apply to all admin routes
                .excludePathPatterns(
                        "/admin/adminLogin",      // allow login
                        "/admin/adminLogout",     // allow logout
                        "/user/userLoginForm",      // allow login->GetMapping
                        "/user/userLogin",       //allow user Login-->PostMapp
                        "/user/userLogout",     // allow logout
                        "/user/userAdd",       //allow user to open User-SignUp form-->(GETmap)
                        "/user/userSignup",     //allow user to Get -Registered via --> POStMap
                    "/css/**", "/js/**", "/images/**", "/webjars/**" // allow static resources
                );
    }
}

 /* login/logout URLs (no session needed)
 * signup URLs
 */