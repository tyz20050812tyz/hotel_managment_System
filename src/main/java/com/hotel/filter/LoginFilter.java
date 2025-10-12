package com.hotel.filter;

import com.hotel.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录验证过滤器
 * 检查用户是否已登录，未登录用户重定向到登录页面
 */
public class LoginFilter implements Filter {
    
    private static final Logger logger = LogManager.getLogger(LoginFilter.class);
    
    // 不需要登录验证的URL
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
        "/login.jsp",
        "/login",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("LoginFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // 移除上下文路径，获取相对路径
        String path = requestURI.substring(contextPath.length());
        
        logger.debug("Processing request: {}", path);
        
        // 检查是否为排除的URL
        if (isExcludedUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 检查用户是否已登录
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        if (user == null) {
            // 用户未登录，重定向到登录页面
            logger.info("User not logged in, redirecting to login page. Requested URL: {}", path);
            
            // 如果是AJAX请求，返回JSON响应
            if (isAjaxRequest(httpRequest)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.getWriter().write("{\"success\":false,\"message\":\"用户未登录\",\"code\":401}");
                return;
            }
            
            // 保存原始请求URL，登录成功后可以重定向回来
            String originalUrl = httpRequest.getRequestURL().toString();
            String queryString = httpRequest.getQueryString();
            if (queryString != null) {
                originalUrl += "?" + queryString;
            }
            session = httpRequest.getSession();
            session.setAttribute("originalUrl", originalUrl);
            
            // 重定向到登录页面
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }
        
        // 用户已登录，继续处理请求
        logger.debug("User {} is logged in, proceeding with request", user.getUsername());
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("LoginFilter destroyed");
    }
    
    /**
     * 检查URL是否在排除列表中
     * @param path 请求路径
     * @return 在排除列表中返回true
     */
    private boolean isExcludedUrl(String path) {
        for (String excludeUrl : EXCLUDE_URLS) {
            if (path.startsWith(excludeUrl)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查是否为AJAX请求
     * @param request HTTP请求
     * @return 是AJAX请求返回true
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
}