package com.hotel.controller;

import com.hotel.model.User;
import com.hotel.service.ServiceFactory;
import com.hotel.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制器
 */
public class LoginController extends BaseController {
    
    private final UserService userService;
    
    public LoginController() {
        this.userService = ServiceFactory.createUserService();
    }

    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String method = request.getMethod();
        
        if ("GET".equals(method)) {
            return handleGetRequest(request, response);
        } else if ("POST".equals(method)) {
            return handlePostRequest(request, response);
        }
        
        return "forward:/login.jsp";
    }
    
    /**
     * 处理GET请求 - 显示登录页面
     */
    private String handleGetRequest(HttpServletRequest request, HttpServletResponse response) {
        // 如果用户已经登录，重定向到首页
        if (isUserLoggedIn(request)) {
            return "redirect:/admin/index.jsp";
        }
        
        return "forward:/login.jsp";
    }
    
    /**
     * 处理POST请求 - 执行登录验证
     */
    private String handlePostRequest(HttpServletRequest request, HttpServletResponse response) {
        String username = getParameter(request, "username");
        String password = getParameter(request, "password");
        String rememberMe = getParameter(request, "rememberMe");
        
        logger.info("Login attempt for username: {}", username);
        
        // 验证用户登录
        User user = userService.authenticate(username, password);
        
        if (user != null) {
            // 登录成功
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_USER, user);
            session.setMaxInactiveInterval(30 * 60); // 30分钟会话超时
            
            logger.info("User logged in successfully: {} ({})", user.getUsername(), user.getRole());
            
            // 处理记住我功能（可以通过Cookie实现）
            if ("on".equals(rememberMe)) {
                // 这里可以设置Cookie来记住用户
                logger.debug("Remember me option selected for user: {}", username);
            }
            
            // 检查是否有原始请求URL
            String originalUrl = (String) session.getAttribute("originalUrl");
            if (originalUrl != null) {
                session.removeAttribute("originalUrl");
                return "redirect:" + originalUrl;
            }
            
            // 根据用户角色重定向到不同页面
            if (user.getRole() == User.UserRole.ADMIN) {
                return "redirect:/admin/index";
            } else {
                return "redirect:/admin/index"; // 暂时都重定向到同一页面
            }
            
        } else {
            // 登录失败
            logger.warn("Login failed for username: {}", username);
            request.setAttribute("error", "用户名或密码错误，请重试");
            request.setAttribute("username", username); // 保持用户名
            return "forward:/login.jsp";
        }
    }
    
    @Override
    protected boolean validateParameters(HttpServletRequest request) {
        if ("POST".equals(request.getMethod())) {
            String username = getParameter(request, "username");
            String password = getParameter(request, "password");
            
            if (username.isEmpty()) {
                request.setAttribute("error", "请输入用户名");
                return false;
            }
            
            if (password.isEmpty()) {
                request.setAttribute("error", "请输入密码");
                return false;
            }
            
            if (username.length() > 50) {
                request.setAttribute("error", "用户名长度不能超过50个字符");
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    protected boolean checkPermission(HttpServletRequest request) {
        // 登录页面不需要权限检查
        return true;
    }
}