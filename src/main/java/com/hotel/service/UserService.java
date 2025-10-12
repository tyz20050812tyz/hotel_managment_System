package com.hotel.service;

import com.hotel.model.User;
import java.util.List;

/**
 * 用户业务逻辑接口
 */
public interface UserService {
    
    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 原始密码
     * @return 验证成功返回用户对象，失败返回null
     */
    User authenticate(String username, String password);
    
    /**
     * 创建新用户
     * @param user 用户对象
     * @param password 原始密码
     * @return 创建成功返回用户ID，失败返回null
     */
    Integer createUser(User user, String password);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true
     */
    boolean updateUser(User user);
    
    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功返回true
     */
    boolean changePassword(Integer userId, String oldPassword, String newPassword);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除成功返回true
     */
    boolean deleteUser(Integer userId);
    
    /**
     * 根据用户ID查找用户
     * @param userId 用户ID
     * @return 用户对象，不存在返回null
     */
    User findUserById(Integer userId);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象，不存在返回null
     */
    User findUserByUsername(String username);
    
    /**
     * 查找所有用户
     * @return 用户列表
     */
    List<User> findAllUsers();
    
    /**
     * 根据角色查找用户
     * @param role 用户角色
     * @return 用户列表
     */
    List<User> findUsersByRole(User.UserRole role);
    
    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param enabled true为启用，false为禁用
     * @return 操作成功返回true
     */
    boolean toggleUserStatus(Integer userId, boolean enabled);
    
    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 可用返回true
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * 验证用户权限
     * @param user 用户对象
     * @param requiredRole 需要的角色
     * @return 有权限返回true
     */
    boolean hasPermission(User user, User.UserRole requiredRole);
    
    /**
     * 获取用户统计信息
     * @return 统计信息对象
     */
    UserStatistics getUserStatistics();
    
    /**
     * 用户统计信息内部类
     */
    class UserStatistics {
        private long totalUsers;
        private long adminCount;
        private long staffCount;
        private long activeUsers;
        private long inactiveUsers;
        
        // Getter和Setter方法
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        
        public long getAdminCount() { return adminCount; }
        public void setAdminCount(long adminCount) { this.adminCount = adminCount; }
        
        public long getStaffCount() { return staffCount; }
        public void setStaffCount(long staffCount) { this.staffCount = staffCount; }
        
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
        
        public long getInactiveUsers() { return inactiveUsers; }
        public void setInactiveUsers(long inactiveUsers) { this.inactiveUsers = inactiveUsers; }
    }
}