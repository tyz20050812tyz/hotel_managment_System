package com.hotel.service.impl;

import com.hotel.dao.DAOFactory;
import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import com.hotel.service.UserService;
import com.hotel.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 用户业务逻辑实现类
 */
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = DAOFactory.createUserDAO();
    }

    @Override
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            logger.warn("Authentication failed: username or password is empty");
            return null;
        }
        
        try {
            String encryptedPassword = PasswordUtil.encrypt(password);
            User user = userDAO.findByUsernameAndPassword(username.trim(), encryptedPassword);
            
            if (user != null && user.isActive()) {
                logger.info("User authenticated successfully: {}", username);
                return user;
            } else {
                logger.warn("Authentication failed for user: {}", username);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during authentication for user: " + username, e);
            return null;
        }
    }

    @Override
    public Integer createUser(User user, String password) {
        if (user == null || password == null || password.trim().isEmpty()) {
            logger.warn("Cannot create user: user or password is null/empty");
            return null;
        }
        
        // 验证用户名是否已存在
        if (userDAO.existsByUsername(user.getUsername())) {
            logger.warn("Cannot create user: username already exists: {}", user.getUsername());
            return null;
        }
        
        try {
            // 加密密码
            String encryptedPassword = PasswordUtil.encrypt(password);
            user.setPassword(encryptedPassword);
            
            // 设置默认状态
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            
            Integer userId = userDAO.insert(user);
            if (userId != null) {
                logger.info("User created successfully: {} with ID: {}", user.getUsername(), userId);
            }
            return userId;
            
        } catch (Exception e) {
            logger.error("Error creating user: " + user.getUsername(), e);
            return null;
        }
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null) {
            logger.warn("Cannot update user: user or user ID is null");
            return false;
        }
        
        try {
            // 检查用户是否存在
            User existingUser = userDAO.findById(user.getUserId());
            if (existingUser == null) {
                logger.warn("Cannot update user: user not found with ID: {}", user.getUserId());
                return false;
            }
            
            // 如果用户名发生变化，检查新用户名是否已存在
            if (!existingUser.getUsername().equals(user.getUsername())) {
                if (userDAO.existsByUsername(user.getUsername())) {
                    logger.warn("Cannot update user: new username already exists: {}", user.getUsername());
                    return false;
                }
            }
            
            // 保持原有密码（如果没有新密码）
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }
            
            boolean success = userDAO.update(user);
            if (success) {
                logger.info("User updated successfully: {}", user.getUsername());
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error updating user: " + user.getUsername(), e);
            return false;
        }
    }

    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        if (userId == null || oldPassword == null || newPassword == null ||
            oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
            logger.warn("Cannot change password: invalid parameters");
            return false;
        }
        
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                logger.warn("Cannot change password: user not found with ID: {}", userId);
                return false;
            }
            
            // 验证旧密码
            String encryptedOldPassword = PasswordUtil.encrypt(oldPassword);
            if (!encryptedOldPassword.equals(user.getPassword())) {
                logger.warn("Cannot change password: old password is incorrect for user: {}", user.getUsername());
                return false;
            }
            
            // 设置新密码
            String encryptedNewPassword = PasswordUtil.encrypt(newPassword);
            user.setPassword(encryptedNewPassword);
            
            boolean success = userDAO.update(user);
            if (success) {
                logger.info("Password changed successfully for user: {}", user.getUsername());
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error changing password for user ID: " + userId, e);
            return false;
        }
    }

    @Override
    public boolean deleteUser(Integer userId) {
        if (userId == null) {
            logger.warn("Cannot delete user: user ID is null");
            return false;
        }
        
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                logger.warn("Cannot delete user: user not found with ID: {}", userId);
                return false;
            }
            
            boolean success = userDAO.delete(userId);
            if (success) {
                logger.info("User deleted successfully: {}", user.getUsername());
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error deleting user with ID: " + userId, e);
            return false;
        }
    }

    @Override
    public User findUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        
        try {
            return userDAO.findById(userId);
        } catch (Exception e) {
            logger.error("Error finding user by ID: " + userId, e);
            return null;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        try {
            return userDAO.findByUsername(username.trim());
        } catch (Exception e) {
            logger.error("Error finding user by username: " + username, e);
            return null;
        }
    }

    @Override
    public List<User> findAllUsers() {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            logger.error("Error finding all users", e);
            return null;
        }
    }

    @Override
    public List<User> findUsersByRole(User.UserRole role) {
        if (role == null) {
            return null;
        }
        
        try {
            return userDAO.findByRole(role);
        } catch (Exception e) {
            logger.error("Error finding users by role: " + role, e);
            return null;
        }
    }

    @Override
    public boolean toggleUserStatus(Integer userId, boolean enabled) {
        if (userId == null) {
            logger.warn("Cannot toggle user status: user ID is null");
            return false;
        }
        
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                logger.warn("Cannot toggle user status: user not found with ID: {}", userId);
                return false;
            }
            
            user.setStatus(enabled ? 1 : 0);
            boolean success = userDAO.update(user);
            
            if (success) {
                logger.info("User status toggled successfully: {} -> {}", 
                          user.getUsername(), enabled ? "enabled" : "disabled");
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error toggling user status for ID: " + userId, e);
            return false;
        }
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        try {
            return !userDAO.existsByUsername(username.trim());
        } catch (Exception e) {
            logger.error("Error checking username availability: " + username, e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(User user, User.UserRole requiredRole) {
        if (user == null || requiredRole == null) {
            return false;
        }
        
        // 管理员拥有所有权限
        if (user.getRole() == User.UserRole.ADMIN) {
            return true;
        }
        
        // 员工只能访问员工权限
        if (user.getRole() == User.UserRole.STAFF && requiredRole == User.UserRole.STAFF) {
            return true;
        }
        
        return false;
    }

    @Override
    public UserStatistics getUserStatistics() {
        try {
            UserStatistics statistics = new UserStatistics();
            
            statistics.setTotalUsers(userDAO.count());
            statistics.setAdminCount(userDAO.countByRole(User.UserRole.ADMIN));
            statistics.setStaffCount(userDAO.countByRole(User.UserRole.STAFF));
            statistics.setActiveUsers(userDAO.findByStatus(1).size());
            statistics.setInactiveUsers(userDAO.findByStatus(0).size());
            
            return statistics;
            
        } catch (Exception e) {
            logger.error("Error getting user statistics", e);
            return new UserStatistics();
        }
    }
}