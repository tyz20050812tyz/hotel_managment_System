package com.hotel.dao;

import com.hotel.model.Customer;
import java.util.List;

/**
 * 客户数据访问接口
 */
public interface CustomerDAO {
    
    /**
     * 插入新客户
     * @param customer 客户对象
     * @return 插入成功返回生成的客户ID，失败返回null
     */
    Integer insert(Customer customer);
    
    /**
     * 根据客户ID删除客户
     * @param customerId 客户ID
     * @return 删除成功返回true
     */
    boolean delete(Integer customerId);
    
    /**
     * 更新客户信息
     * @param customer 客户对象
     * @return 更新成功返回true
     */
    boolean update(Customer customer);
    
    /**
     * 根据客户ID查找客户
     * @param customerId 客户ID
     * @return 客户对象，不存在返回null
     */
    Customer findById(Integer customerId);
    
    /**
     * 根据身份证号查找客户
     * @param idCard 身份证号
     * @return 客户对象，不存在返回null
     */
    Customer findByIdCard(String idCard);
    
    /**
     * 根据电话查找客户
     * @param phone 电话号码
     * @return 客户对象，不存在返回null
     */
    Customer findByPhone(String phone);
    
    /**
     * 查找所有客户
     * @return 客户列表
     */
    List<Customer> findAll();
    
    /**
     * 根据VIP等级查找客户
     * @param vipLevel VIP等级
     * @return 客户列表
     */
    List<Customer> findByVipLevel(Integer vipLevel);
    
    /**
     * 根据姓名模糊查找客户
     * @param name 姓名关键词
     * @return 客户列表
     */
    List<Customer> findByNameLike(String name);
    
    /**
     * 检查身份证号是否已存在
     * @param idCard 身份证号
     * @return 存在返回true
     */
    boolean existsByIdCard(String idCard);
    
    /**
     * 统计客户总数
     * @return 客户总数
     */
    long count();
    
    /**
     * 根据VIP等级统计客户数量
     * @param vipLevel VIP等级
     * @return 该等级的客户数量
     */
    long countByVipLevel(Integer vipLevel);
}