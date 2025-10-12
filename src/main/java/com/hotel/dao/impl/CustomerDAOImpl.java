package com.hotel.dao.impl;

import com.hotel.dao.CustomerDAO;
import com.hotel.model.Customer;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户数据访问实现类
 */
public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = LogManager.getLogger(CustomerDAOImpl.class);
    
    // SQL语句常量
    private static final String INSERT_SQL = 
        "INSERT INTO customers (name, id_card, phone, email, address, vip_level) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String DELETE_SQL = 
        "DELETE FROM customers WHERE customer_id = ?";
    
    private static final String UPDATE_SQL = 
        "UPDATE customers SET name = ?, id_card = ?, phone = ?, email = ?, address = ?, vip_level = ? WHERE customer_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers WHERE customer_id = ?";
    
    private static final String SELECT_BY_ID_CARD_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers WHERE id_card = ?";
    
    private static final String SELECT_BY_PHONE_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers WHERE phone = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers ORDER BY create_time DESC";
    
    private static final String SELECT_BY_VIP_LEVEL_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers WHERE vip_level = ? ORDER BY create_time DESC";
    
    private static final String SELECT_BY_NAME_LIKE_SQL = 
        "SELECT customer_id, name, id_card, phone, email, address, vip_level, create_time FROM customers WHERE name LIKE ? ORDER BY create_time DESC";
    
    private static final String EXISTS_BY_ID_CARD_SQL = 
        "SELECT COUNT(*) FROM customers WHERE id_card = ?";
    
    private static final String COUNT_SQL = 
        "SELECT COUNT(*) FROM customers";
    
    private static final String COUNT_BY_VIP_LEVEL_SQL = 
        "SELECT COUNT(*) FROM customers WHERE vip_level = ?";

    @Override
    public Integer insert(Customer customer) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getIdCard());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getVipLevel() != null ? customer.getVipLevel() : 0);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer customerId = rs.getInt(1);
                    logger.info("Customer inserted successfully with ID: {}", customerId);
                    return customerId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error inserting customer: " + customer.getName(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public boolean delete(Integer customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, customerId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Customer deleted successfully with ID: {}", customerId);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error deleting customer with ID: " + customerId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean update(Customer customer) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getIdCard());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getVipLevel() != null ? customer.getVipLevel() : 0);
            stmt.setInt(7, customer.getCustomerId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Customer updated successfully with ID: {}", customer.getCustomerId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error updating customer with ID: " + customer.getCustomerId(), e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public Customer findById(Integer customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, customerId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding customer by ID: " + customerId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public Customer findByIdCard(String idCard) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_CARD_SQL);
            stmt.setString(1, idCard);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding customer by ID card: " + idCard, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public Customer findByPhone(String phone) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_PHONE_SQL);
            stmt.setString(1, phone);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding customer by phone: " + phone, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<Customer> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all customers", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return customers;
    }

    @Override
    public List<Customer> findByVipLevel(Integer vipLevel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_VIP_LEVEL_SQL);
            stmt.setInt(1, vipLevel);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding customers by VIP level: " + vipLevel, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return customers;
    }

    @Override
    public List<Customer> findByNameLike(String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_NAME_LIKE_SQL);
            stmt.setString(1, "%" + name + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding customers by name like: " + name, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return customers;
    }

    @Override
    public boolean existsByIdCard(String idCard) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(EXISTS_BY_ID_CARD_SQL);
            stmt.setString(1, idCard);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if ID card exists: " + idCard, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return false;
    }

    @Override
    public long count() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_SQL);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting customers", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByVipLevel(Integer vipLevel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_VIP_LEVEL_SQL);
            stmt.setInt(1, vipLevel);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting customers by VIP level: " + vipLevel, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }
    
    /**
     * 将ResultSet映射为Customer对象
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setIdCard(rs.getString("id_card"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setVipLevel(rs.getInt("vip_level"));
        customer.setCreateTime(rs.getTimestamp("create_time"));
        return customer;
    }
    
    /**
     * 关闭数据库资源
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Error closing ResultSet", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Error closing PreparedStatement", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error closing Connection", e);
            }
        }
    }
}