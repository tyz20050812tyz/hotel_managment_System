package com.hotel.controller;

import com.hotel.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登出控制器
 */
public class LogoutController extends BaseController {

    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            User user = (User) session.getAttribute(SESSION_USER);
            if (user != null) {
                logger.info("User logged out: {} ({})", user.getUsername(), user.getRole());
            }
            
            // 销毁会话
            session.invalidate();
        }
        
        // 重定向到登录页面
        return "redirect:/login.jsp";
    }
    
    @Override
    protected boolean checkPermission(HttpServletRequest request) {
        // 登出不需要权限检查
        return true;
    }
}