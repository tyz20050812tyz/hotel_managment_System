package com.hotel.dao;

import com.hotel.model.User;
import java.util.List;

/**
 * 用户数据访问接口
 */
public interface UserDAO {
    
    /**
     * 插入新用户
     * @param user 用户对象
     * @return 插入成功返回生成的用户ID，失败返回null
     */
    Integer insert(User user);
    
    /**
     * 根据用户ID删除用户
     * @param userId 用户ID
     * @return 删除成功返回true
     */
    boolean delete(Integer userId);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true
     */
    boolean update(User user);
    
    /**
     * 根据用户ID查找用户
     * @param userId 用户ID
     * @return 用户对象，不存在返回null
     */
    User findById(Integer userId);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象，不存在返回null
     */
    User findByUsername(String username);
    
    /**
     * 根据用户名和密码查找用户（用于登录验证）
     * @param username 用户名
     * @param password 密码（已加密）
     * @return 用户对象，不存在返回null
     */
    User findByUsernameAndPassword(String username, String password);
    
    /**
     * 查找所有用户
     * @return 用户列表
     */
    List<User> findAll();
    
    /**
     * 根据角色查找用户
     * @param role 用户角色
     * @return 用户列表
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * 根据状态查找用户
     * @param status 用户状态（1:正常,0:禁用）
     * @return 用户列表
     */
    List<User> findByStatus(Integer status);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 存在返回true
     */
    boolean existsByUsername(String username);
    
    /**
     * 统计用户总数
     * @return 用户总数
     */
    long count();
    
    /**
     * 根据角色统计用户数量
     * @param role 用户角色
     * @return 该角色的用户数量
     */
    long countByRole(User.UserRole role);
}