package com.hotel.service;

import com.hotel.model.Customer;
import java.util.List;

/**
 * 客户业务逻辑接口
 */
public interface CustomerService {
    
    /**
     * 创建新客户
     * @param customer 客户对象
     * @return 创建成功返回客户ID，失败返回null
     */
    Integer createCustomer(Customer customer);
    
    /**
     * 更新客户信息
     * @param customer 客户对象
     * @return 更新成功返回true
     */
    boolean updateCustomer(Customer customer);
    
    /**
     * 删除客户
     * @param customerId 客户ID
     * @return 删除成功返回true
     */
    boolean deleteCustomer(Integer customerId);
    
    /**
     * 根据客户ID查找客户
     * @param customerId 客户ID
     * @return 客户对象，不存在返回null
     */
    Customer findCustomerById(Integer customerId);
    
    /**
     * 根据身份证号查找客户
     * @param idCard 身份证号
     * @return 客户对象，不存在返回null
     */
    Customer findCustomerByIdCard(String idCard);
    
    /**
     * 根据电话查找客户
     * @param phone 电话号码
     * @return 客户对象，不存在返回null
     */
    Customer findCustomerByPhone(String phone);
    
    /**
     * 查找所有客户
     * @return 客户列表
     */
    List<Customer> findAllCustomers();
    
    /**
     * 根据VIP等级查找客户
     * @param vipLevel VIP等级
     * @return 客户列表
     */
    List<Customer> findCustomersByVipLevel(Integer vipLevel);
    
    /**
     * 根据姓名模糊查找客户
     * @param name 姓名关键词
     * @return 客户列表
     */
    List<Customer> searchCustomersByName(String name);
    
    /**
     * 升级客户VIP等级
     * @param customerId 客户ID
     * @param newLevel 新的VIP等级
     * @return 升级成功返回true
     */
    boolean upgradeVipLevel(Integer customerId, Integer newLevel);
    
    /**
     * 检查身份证号是否已存在
     * @param idCard 身份证号
     * @return 存在返回true
     */
    boolean isIdCardExists(String idCard);
    
    /**
     * 检查身份证号是否已存在（排除指定客户）
     * @param idCard 身份证号
     * @param excludeCustomerId 排除的客户ID
     * @return 存在返回true
     */
    boolean isIdCardExists(String idCard, Integer excludeCustomerId);
    
    /**
     * 验证客户信息
     * @param customer 客户对象
     * @return 验证通过返回null，失败返回错误消息
     */
    String validateCustomer(Customer customer);
    
    /**
     * 获取客户统计信息
     * @return 统计信息对象
     */
    CustomerStatistics getCustomerStatistics();
    
    /**
     * 客户统计信息内部类
     */
    class CustomerStatistics {
        private long totalCustomers;
        private long regularCustomers;
        private long vipCustomers;
        private long vip1Count;
        private long vip2Count;
        private long vip3Count;
        private long vip4Count;
        private long vip5Count;
        
        // Getter和Setter方法
        public long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
        
        public long getRegularCustomers() { return regularCustomers; }
        public void setRegularCustomers(long regularCustomers) { this.regularCustomers = regularCustomers; }
        
        public long getVipCustomers() { return vipCustomers; }
        public void setVipCustomers(long vipCustomers) { this.vipCustomers = vipCustomers; }
        
        public long getVip1Count() { return vip1Count; }
        public void setVip1Count(long vip1Count) { this.vip1Count = vip1Count; }
        
        public long getVip2Count() { return vip2Count; }
        public void setVip2Count(long vip2Count) { this.vip2Count = vip2Count; }
        
        public long getVip3Count() { return vip3Count; }
        public void setVip3Count(long vip3Count) { this.vip3Count = vip3Count; }
        
        public long getVip4Count() { return vip4Count; }
        public void setVip4Count(long vip4Count) { this.vip4Count = vip4Count; }
        
        public long getVip5Count() { return vip5Count; }
        public void setVip5Count(long vip5Count) { this.vip5Count = vip5Count; }
    }
}