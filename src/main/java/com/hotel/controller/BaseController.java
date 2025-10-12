package com.hotel.controller;

import com.hotel.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 基础Controller模板
 * 使用模板方法模式定义通用的请求处理流程
 */
public abstract class BaseController extends HttpServlet {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final ObjectMapper objectMapper = new ObjectMapper();
    
    // 常量定义
    protected static final String SESSION_USER = "user";
    protected static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    protected static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";
    protected static final String ENCODING_UTF8 = "UTF-8";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * 模板方法：定义请求处理的通用流程
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求和响应的编码
        request.setCharacterEncoding(ENCODING_UTF8);
        response.setCharacterEncoding(ENCODING_UTF8);
        
        try {
            // 1. 参数验证
            if (!validateParameters(request)) {
                handleValidationError(request, response);
                return;
            }
            
            // 2. 权限检查
            if (!checkPermission(request)) {
                handlePermissionError(request, response);
                return;
            }
            
            // 3. 业务处理（由子类实现）
            String result = handleBusinessLogic(request, response);
            
            // 4. 结果处理
            handleResult(request, response, result);
            
        } catch (Exception e) {
            logger.error("Error processing request", e);
            handleException(request, response, e);
        }
    }
    
    /**
     * 抽象方法：处理具体的业务逻辑，由子类实现
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 处理结果
     * @throws Exception 处理异常
     */
    protected abstract String handleBusinessLogic(HttpServletRequest request, 
                                                 HttpServletResponse response) throws Exception;
    
    /**
     * 钩子方法：参数验证，子类可以重写
     * @param request HTTP请求
     * @return 验证通过返回true
     */
    protected boolean validateParameters(HttpServletRequest request) {
        return true; // 默认验证通过
    }
    
    /**
     * 钩子方法：权限检查，子类可以重写
     * @param request HTTP请求
     * @return 有权限返回true
     */
    protected boolean checkPermission(HttpServletRequest request) {
        return true; // 默认有权限
    }
    
    /**
     * 钩子方法：处理参数验证错误
     * @param request HTTP请求
     * @param response HTTP响应
     */
    protected void handleValidationError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJsonResponse(response, createErrorResponse("参数验证失败", 400));
    }
    
    /**
     * 钩子方法：处理权限错误
     * @param request HTTP请求
     * @param response HTTP响应
     */
    protected void handlePermissionError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        writeJsonResponse(response, createErrorResponse("权限不足", 403));
    }
    
    /**
     * 钩子方法：处理业务处理结果
     * @param request HTTP请求
     * @param response HTTP响应
     * @param result 处理结果
     */
    protected void handleResult(HttpServletRequest request, HttpServletResponse response, String result) 
            throws ServletException, IOException {
        if (result != null) {
            if (result.startsWith("redirect:")) {
                // 重定向
                String redirectUrl = result.substring(9);
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else if (result.startsWith("forward:")) {
                // 转发
                String forwardUrl = result.substring(8);
                request.getRequestDispatcher(forwardUrl).forward(request, response);
            } else if (result.startsWith("json:")) {
                // JSON响应
                String jsonData = result.substring(5);
                writeJsonResponse(response, jsonData);
            } else {
                // 默认转发到JSP页面
                request.getRequestDispatcher(result).forward(request, response);
            }
        }
    }
    
    /**
     * 钩子方法：处理异常
     * @param request HTTP请求
     * @param response HTTP响应
     * @param e 异常对象
     */
    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) 
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJsonResponse(response, createErrorResponse("服务器内部错误", 500));
    }
    
    // 工具方法
    
    /**
     * 获取当前登录用户
     * @param request HTTP请求
     * @return 用户对象，未登录返回null
     */
    protected User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(SESSION_USER);
        }
        return null;
    }
    
    /**
     * 检查用户是否已登录
     * @param request HTTP请求
     * @return 已登录返回true
     */
    protected boolean isUserLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    /**
     * 检查用户是否为管理员
     * @param request HTTP请求
     * @return 是管理员返回true
     */
    protected boolean isAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && user.getRole() == User.UserRole.ADMIN;
    }
    
    /**
     * 写入JSON响应
     * @param response HTTP响应
     * @param jsonData JSON数据
     */
    protected void writeJsonResponse(HttpServletResponse response, String jsonData) 
            throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }
    
    /**
     * 创建成功响应JSON
     * @param data 数据
     * @return JSON字符串
     */
    protected String createSuccessResponse(Object data) {
        try {
            ResponseResult result = new ResponseResult(true, "操作成功", data);
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("Error creating success response", e);
            return "{\"success\":true,\"message\":\"操作成功\"}";
        }
    }
    
    /**
     * 创建错误响应JSON
     * @param message 错误消息
     * @param code 错误代码
     * @return JSON字符串
     */
    protected String createErrorResponse(String message, int code) {
        try {
            ResponseResult result = new ResponseResult(false, message, code);
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("Error creating error response", e);
            return "{\"success\":false,\"message\":\"" + message + "\"}";
        }
    }
    
    /**
     * 获取请求参数（安全处理null值）
     * @param request HTTP请求
     * @param name 参数名
     * @return 参数值，不存在返回空字符串
     */
    protected String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null ? value.trim() : "";
    }
    
    /**
     * 获取整数类型参数
     * @param request HTTP请求
     * @param name 参数名
     * @param defaultValue 默认值
     * @return 参数值
     */
    protected int getIntParameter(HttpServletRequest request, String name, int defaultValue) {
        String value = getParameter(request, name);
        if (value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer parameter: {} = {}", name, value);
            return defaultValue;
        }
    }
    
    /**
     * 响应结果类
     */
    public static class ResponseResult {
        private boolean success;
        private String message;
        private Object data;
        private int code;
        
        public ResponseResult(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.code = success ? 200 : 400;
        }
        
        public ResponseResult(boolean success, String message, int code) {
            this.success = success;
            this.message = message;
            this.code = code;
        }
        
        // Getter方法
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
        public int getCode() { return code; }
    }
}