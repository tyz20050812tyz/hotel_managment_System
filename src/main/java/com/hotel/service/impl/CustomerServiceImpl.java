package com.hotel.service.impl;

import com.hotel.dao.CustomerDAO;
import com.hotel.dao.DAOFactory;
import com.hotel.model.Customer;
import com.hotel.service.CustomerService;
import com.hotel.service.CustomerService.CustomerStatistics;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 客户业务逻辑实现类
 */
public class CustomerServiceImpl implements CustomerService {
    
    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);
    private final CustomerDAO customerDAO;
    
    public CustomerServiceImpl() {
        this.customerDAO = DAOFactory.createCustomerDAO();
    }

    @Override
    public Integer createCustomer(Customer customer) {
        if (customer == null) {
            logger.warn("Cannot create customer: customer is null");
            return null;
        }
        
        // 验证客户信息
        String validationError = validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Cannot create customer: validation failed - {}", validationError);
            return null;
        }
        
        // 检查身份证号是否已存在（仅在身份证号不为空时检查）
        if (customer.getIdCard() != null && !customer.getIdCard().trim().isEmpty()) {
            if (isIdCardExists(customer.getIdCard())) {
                logger.warn("Cannot create customer: ID card already exists: {}", customer.getIdCard());
                return null;
            }
        }
        
        try {
            // 设置默认VIP等级
            if (customer.getVipLevel() == null) {
                customer.setVipLevel(0);
            }
            
            Integer customerId = customerDAO.insert(customer);
            if (customerId != null) {
                logger.info("Customer created successfully: {} with ID: {}", customer.getName(), customerId);
            }
            return customerId;
            
        } catch (Exception e) {
            logger.error("Error creating customer: " + customer.getName(), e);
            return null;
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        if (customer == null || customer.getCustomerId() == null) {
            logger.warn("Cannot update customer: customer or customer ID is null");
            return false;
        }
        
        // 验证客户信息
        String validationError = validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Cannot update customer: validation failed - {}", validationError);
            return false;
        }
        
        try {
            // 检查客户是否存在
            Customer existingCustomer = customerDAO.findById(customer.getCustomerId());
            if (existingCustomer == null) {
                logger.warn("Cannot update customer: customer not found with ID: {}", customer.getCustomerId());
                return false;
            }
            
            // 如果身份证号发生变化，检查新身份证号是否已存在
            if (!existingCustomer.getIdCard().equals(customer.getIdCard())) {
                if (isIdCardExists(customer.getIdCard(), customer.getCustomerId())) {
                    logger.warn("Cannot update customer: new ID card already exists: {}", customer.getIdCard());
                    return false;
                }
            }
            
            boolean success = customerDAO.update(customer);
            if (success) {
                logger.info("Customer updated successfully: {}", customer.getName());
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error updating customer: " + customer.getName(), e);
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        if (customerId == null) {
            logger.warn("Cannot delete customer: customer ID is null");
            return false;
        }
        
        try {
            Customer customer = customerDAO.findById(customerId);
            if (customer == null) {
                logger.warn("Cannot delete customer: customer not found with ID: {}", customerId);
                return false;
            }
            
            // 这里可以添加业务规则检查，比如客户是否有未完成的预订
            // 暂时简单删除
            
            boolean success = customerDAO.delete(customerId);
            if (success) {
                logger.info("Customer deleted successfully: {}", customer.getName());
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error deleting customer with ID: " + customerId, e);
            return false;
        }
    }

    @Override
    public Customer findCustomerById(Integer customerId) {
        if (customerId == null) {
            return null;
        }
        
        try {
            return customerDAO.findById(customerId);
        } catch (Exception e) {
            logger.error("Error finding customer by ID: " + customerId, e);
            return null;
        }
    }

    @Override
    public Customer findCustomerByIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return null;
        }
        
        try {
            return customerDAO.findByIdCard(idCard.trim());
        } catch (Exception e) {
            logger.error("Error finding customer by ID card: " + idCard, e);
            return null;
        }
    }

    @Override
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        
        try {
            return customerDAO.findByPhone(phone.trim());
        } catch (Exception e) {
            logger.error("Error finding customer by phone: " + phone, e);
            return null;
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        try {
            return customerDAO.findAll();
        } catch (Exception e) {
            logger.error("Error finding all customers", e);
            return null;
        }
    }

    @Override
    public List<Customer> findCustomersByVipLevel(Integer vipLevel) {
        if (vipLevel == null) {
            return null;
        }
        
        try {
            return customerDAO.findByVipLevel(vipLevel);
        } catch (Exception e) {
            logger.error("Error finding customers by VIP level: " + vipLevel, e);
            return null;
        }
    }

    @Override
    public List<Customer> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAllCustomers();
        }
        
        try {
            return customerDAO.findByNameLike(name.trim());
        } catch (Exception e) {
            logger.error("Error searching customers by name: " + name, e);
            return null;
        }
    }

    @Override
    public boolean upgradeVipLevel(Integer customerId, Integer newLevel) {
        if (customerId == null || newLevel == null) {
            logger.warn("Cannot upgrade VIP level: customer ID or new level is null");
            return false;
        }
        
        if (newLevel < 0 || newLevel > 5) {
            logger.warn("Cannot upgrade VIP level: invalid level: {}", newLevel);
            return false;
        }
        
        try {
            Customer customer = customerDAO.findById(customerId);
            if (customer == null) {
                logger.warn("Cannot upgrade VIP level: customer not found with ID: {}", customerId);
                return false;
            }
            
            // 只能升级，不能降级
            if (newLevel <= customer.getVipLevel()) {
                logger.warn("Cannot upgrade VIP level: new level ({}) is not higher than current level ({})", 
                          newLevel, customer.getVipLevel());
                return false;
            }
            
            customer.setVipLevel(newLevel);
            boolean success = customerDAO.update(customer);
            
            if (success) {
                logger.info("Customer VIP level upgraded successfully: {} -> VIP{}", 
                          customer.getName(), newLevel);
            }
            return success;
            
        } catch (Exception e) {
            logger.error("Error upgrading VIP level for customer ID: " + customerId, e);
            return false;
        }
    }

    @Override
    public boolean isIdCardExists(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        
        try {
            return customerDAO.existsByIdCard(idCard.trim());
        } catch (Exception e) {
            logger.error("Error checking if ID card exists: " + idCard, e);
            return false;
        }
    }

    @Override
    public boolean isIdCardExists(String idCard, Integer excludeCustomerId) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        
        try {
            Customer customer = customerDAO.findByIdCard(idCard.trim());
            if (customer == null) {
                return false;
            }
            
            // 如果找到的客户就是要排除的客户，则认为不存在
            return !customer.getCustomerId().equals(excludeCustomerId);
            
        } catch (Exception e) {
            logger.error("Error checking if ID card exists (excluding customer): " + idCard, e);
            return false;
        }
    }

    @Override
    public String validateCustomer(Customer customer) {
        if (customer == null) {
            return "客户信息不能为空";
        }
        
        // 验证姓名
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return "客户姓名不能为空";
        }
        if (customer.getName().trim().length() > 50) {
            return "客户姓名长度不能超过50个字符";
        }
        
        // 验证身份证号（可选）
        if (customer.getIdCard() != null && !customer.getIdCard().trim().isEmpty()) {
            if (!Utils.validateIdCard(customer.getIdCard().trim())) {
                return "身份证号格式不正确";
            }
        }
        
        // 验证电话
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            return "联系电话不能为空";
        }
        if (!Utils.validatePhone(customer.getPhone().trim())) {
            return "联系电话格式不正确";
        }
        
        // 验证邮箱（可选）
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            if (!Utils.validateEmail(customer.getEmail().trim())) {
                return "邮箱格式不正确";
            }
        }
        
        // 验证VIP等级
        if (customer.getVipLevel() != null) {
            if (customer.getVipLevel() < 0 || customer.getVipLevel() > 5) {
                return "VIP等级必须在0-5之间";
            }
        }
        
        return null; // 验证通过
    }

    @Override
    public CustomerStatistics getCustomerStatistics() {
        try {
            CustomerStatistics statistics = new CustomerStatistics();
            
            statistics.setTotalCustomers(customerDAO.count());
            statistics.setRegularCustomers(customerDAO.countByVipLevel(0));
            statistics.setVip1Count(customerDAO.countByVipLevel(1));
            statistics.setVip2Count(customerDAO.countByVipLevel(2));
            statistics.setVip3Count(customerDAO.countByVipLevel(3));
            statistics.setVip4Count(customerDAO.countByVipLevel(4));
            statistics.setVip5Count(customerDAO.countByVipLevel(5));
            
            // 计算总VIP客户数
            long vipTotal = statistics.getVip1Count() + statistics.getVip2Count() + 
                           statistics.getVip3Count() + statistics.getVip4Count() + 
                           statistics.getVip5Count();
            statistics.setVipCustomers(vipTotal);
            
            return statistics;
            
        } catch (Exception e) {
            logger.error("Error getting customer statistics", e);
            return new CustomerStatistics();
        }
    }
}