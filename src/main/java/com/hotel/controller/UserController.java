package com.hotel.controller;

import com.hotel.model.User;
import com.hotel.service.ServiceFactory;
import com.hotel.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户管理控制器
 */
public class UserController extends BaseController {
    
    private final UserService userService;
    
    public UserController() {
        this.userService = ServiceFactory.createUserService();
    }

    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getPathInfo();
        String method = request.getMethod();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            return handleUserList(request, response);
        }
        
        switch (pathInfo) {
            case "/list":
                return handleUserList(request, response);
            case "/add":
                return handleAddUser(request, response, method);
            case "/edit":
                return handleEditUser(request, response, method);
            case "/delete":
                return handleDeleteUser(request, response);
            case "/toggle-status":
                return handleToggleStatus(request, response);
            case "/check-username":
                return handleCheckUsername(request, response);
            case "/change-password":
                return handleChangePassword(request, response, method);
            case "/check-session":
                return handleCheckSession(request, response);
            default:
                return "/admin/error/404.jsp";
        }
    }
    
    /**
     * 处理用户列表
     */
    private String handleUserList(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userService.findAllUsers();
        UserService.UserStatistics statistics = userService.getUserStatistics();
        
        request.setAttribute("users", users);
        request.setAttribute("statistics", statistics);
        
        return "/admin/user-list.jsp";
    }
    
    /**
     * 处理添加用户
     */
    private String handleAddUser(HttpServletRequest request, HttpServletResponse response, String method) {
        if ("GET".equals(method)) {
            return "/admin/user-add.jsp";
        }
        
        // POST请求 - 创建用户
        String username = getParameter(request, "username");
        String password = getParameter(request, "password");
        String realName = getParameter(request, "realName");
        String roleStr = getParameter(request, "role");
        String phone = getParameter(request, "phone");
        String email = getParameter(request, "email");
        
        try {
            User user = new User();
            user.setUsername(username);
            user.setRealName(realName);
            user.setRole(User.UserRole.valueOf(roleStr));
            user.setPhone(phone.isEmpty() ? null : phone);
            user.setEmail(email.isEmpty() ? null : email);
            
            Integer userId = userService.createUser(user, password);
            
            if (userId != null) {
                request.setAttribute("success", "用户创建成功");
                return "redirect:/admin/user/list";
            } else {
                request.setAttribute("error", "用户创建失败，请检查用户名是否已存在");
                request.setAttribute("user", user);
                return "/admin/user-add.jsp";
            }
            
        } catch (Exception e) {
            logger.error("Error creating user", e);
            request.setAttribute("error", "用户创建失败：" + e.getMessage());
            return "/admin/user-add.jsp";
        }
    }
    
    /**
     * 处理编辑用户
     */
    private String handleEditUser(HttpServletRequest request, HttpServletResponse response, String method) {
        String userIdStr = getParameter(request, "id");
        
        if (userIdStr.isEmpty()) {
            return "redirect:/admin/user/list";
        }
        
        int userId = getIntParameter(request, "id", 0);
        
        if ("GET".equals(method)) {
            User user = userService.findUserById(userId);
            if (user == null) {
                request.setAttribute("error", "用户不存在");
                return "redirect:/admin/user/list";
            }
            
            request.setAttribute("user", user);
            request.setAttribute("isEdit", true);
            return "/admin/user-edit.jsp";
        }
        
        // POST请求 - 更新用户
        String username = getParameter(request, "username");
        String realName = getParameter(request, "realName");
        String roleStr = getParameter(request, "role");
        String phone = getParameter(request, "phone");
        String email = getParameter(request, "email");
        
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                request.setAttribute("error", "用户不存在");
                return "redirect:/admin/user/list";
            }
            
            user.setUsername(username);
            user.setRealName(realName);
            user.setRole(User.UserRole.valueOf(roleStr));
            user.setPhone(phone.isEmpty() ? null : phone);
            user.setEmail(email.isEmpty() ? null : email);
            
            boolean success = userService.updateUser(user);
            
            if (success) {
                request.setAttribute("success", "用户更新成功");
                return "redirect:/admin/user/list";
            } else {
                request.setAttribute("error", "用户更新失败");
                request.setAttribute("user", user);
                request.setAttribute("isEdit", true);
                return "/admin/user-edit.jsp";
            }
            
        } catch (Exception e) {
            logger.error("Error updating user", e);
            request.setAttribute("error", "用户更新失败：" + e.getMessage());
            return "/admin/user-edit.jsp";
        }
    }
    
    /**
     * 处理删除用户
     */
    private String handleDeleteUser(HttpServletRequest request, HttpServletResponse response) {
        int userId = getIntParameter(request, "id", 0);
        
        if (userId == 0) {
            return "json:" + createErrorResponse("无效的用户ID", 400);
        }
        
        // 检查是否删除自己
        User currentUser = getCurrentUser(request);
        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            return "json:" + createErrorResponse("不能删除自己的账户", 400);
        }
        
        boolean success = userService.deleteUser(userId);
        
        if (success) {
            return "json:" + createSuccessResponse("用户删除成功");
        } else {
            return "json:" + createErrorResponse("用户删除失败", 500);
        }
    }
    
    /**
     * 处理启用/禁用用户
     */
    private String handleToggleStatus(HttpServletRequest request, HttpServletResponse response) {
        int userId = getIntParameter(request, "id", 0);
        boolean enabled = "true".equals(getParameter(request, "enabled"));
        
        if (userId == 0) {
            return "json:" + createErrorResponse("无效的用户ID", 400);
        }
        
        // 检查是否操作自己
        User currentUser = getCurrentUser(request);
        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            return "json:" + createErrorResponse("不能禁用自己的账户", 400);
        }
        
        boolean success = userService.toggleUserStatus(userId, enabled);
        
        if (success) {
            String message = enabled ? "用户已启用" : "用户已禁用";
            return "json:" + createSuccessResponse(message);
        } else {
            return "json:" + createErrorResponse("操作失败", 500);
        }
    }
    
    /**
     * 检查用户名是否可用
     */
    private String handleCheckUsername(HttpServletRequest request, HttpServletResponse response) {
        String username = getParameter(request, "username");
        
        if (username.isEmpty()) {
            return "json:" + createErrorResponse("用户名不能为空", 400);
        }
        
        boolean available = userService.isUsernameAvailable(username);
        
        if (available) {
            return "json:" + createSuccessResponse("用户名可用");
        } else {
            return "json:" + createErrorResponse("用户名已存在", 400);
        }
    }
    
    /**
     * 处理修改密码
     */
    private String handleChangePassword(HttpServletRequest request, HttpServletResponse response, String method) {
        if ("GET".equals(method)) {
            return "/admin/change-password.jsp";
        }
        
        // POST请求
        String oldPassword = getParameter(request, "oldPassword");
        String newPassword = getParameter(request, "newPassword");
        String confirmPassword = getParameter(request, "confirmPassword");
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "新密码和确认密码不一致");
            return "/admin/change-password.jsp";
        }
        
        User currentUser = getCurrentUser(request);
        boolean success = userService.changePassword(currentUser.getUserId(), oldPassword, newPassword);
        
        if (success) {
            request.setAttribute("success", "密码修改成功");
            return "/admin/change-password.jsp";
        } else {
            request.setAttribute("error", "密码修改失败，请检查原密码是否正确");
            return "/admin/change-password.jsp";
        }
    }
    
    /**
     * 检查会话状态
     */
    private String handleCheckSession(HttpServletRequest request, HttpServletResponse response) {
        User currentUser = getCurrentUser(request);
        
        if (currentUser != null) {
            return "json:" + createSuccessResponse("会话有效");
        } else {
            return "json:" + createErrorResponse("会话已过期", 401);
        }
    }
    
    @Override
    protected boolean validateParameters(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String method = request.getMethod();
        
        if ("POST".equals(method) && ("/add".equals(pathInfo) || "/edit".equals(pathInfo))) {
            String username = getParameter(request, "username");
            String password = getParameter(request, "password");
            String realName = getParameter(request, "realName");
            String role = getParameter(request, "role");
            
            if (username.isEmpty()) {
                request.setAttribute("error", "用户名不能为空");
                return false;
            }
            
            if ("/add".equals(pathInfo) && password.isEmpty()) {
                request.setAttribute("error", "密码不能为空");
                return false;
            }
            
            if (realName.isEmpty()) {
                request.setAttribute("error", "真实姓名不能为空");
                return false;
            }
            
            if (role.isEmpty()) {
                request.setAttribute("error", "用户角色不能为空");
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    protected boolean checkPermission(HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        
        // 检查是否登录
        if (currentUser == null) {
            return false;
        }
        
        // 用户管理需要管理员权限
        return userService.hasPermission(currentUser, User.UserRole.ADMIN);
    }
    
    @Override
    protected void handlePermissionError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("error", "权限不足，只有管理员可以管理用户");
        request.getRequestDispatcher("/admin/error/403.jsp").forward(request, response);
    }
}