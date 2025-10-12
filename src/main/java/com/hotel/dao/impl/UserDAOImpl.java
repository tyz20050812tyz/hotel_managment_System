package com.hotel.dao.impl;

import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问实现类
 */
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    
    // SQL语句常量
    private static final String INSERT_SQL = 
        "INSERT INTO users (username, password, real_name, role, phone, email, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String DELETE_SQL = 
        "DELETE FROM users WHERE user_id = ?";
    
    private static final String UPDATE_SQL = 
        "UPDATE users SET username = ?, password = ?, real_name = ?, role = ?, phone = ?, email = ?, status = ? WHERE user_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users WHERE user_id = ?";
    
    private static final String SELECT_BY_USERNAME_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users WHERE username = ?";
    
    private static final String SELECT_BY_USERNAME_PASSWORD_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users WHERE username = ? AND password = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users ORDER BY create_time DESC";
    
    private static final String SELECT_BY_ROLE_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users WHERE role = ? ORDER BY create_time DESC";
    
    private static final String SELECT_BY_STATUS_SQL = 
        "SELECT user_id, username, password, real_name, role, phone, email, status, create_time FROM users WHERE status = ? ORDER BY create_time DESC";
    
    private static final String EXISTS_BY_USERNAME_SQL = 
        "SELECT COUNT(*) FROM users WHERE username = ?";
    
    private static final String COUNT_SQL = 
        "SELECT COUNT(*) FROM users";
    
    private static final String COUNT_BY_ROLE_SQL = 
        "SELECT COUNT(*) FROM users WHERE role = ?";

    @Override
    public Integer insert(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRealName());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setInt(7, user.getStatus() != null ? user.getStatus() : 1);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer userId = rs.getInt(1);
                    logger.info("User inserted successfully with ID: {}", userId);
                    return userId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error inserting user: " + user.getUsername(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public boolean delete(Integer userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("User deleted successfully with ID: {}", userId);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error deleting user with ID: " + userId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean update(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRealName());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setInt(7, user.getStatus() != null ? user.getStatus() : 1);
            stmt.setInt(8, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("User updated successfully with ID: {}", user.getUserId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error updating user with ID: " + user.getUserId(), e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public User findById(Integer userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by ID: " + userId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_USERNAME_SQL);
            stmt.setString(1, username);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by username: " + username, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_USERNAME_PASSWORD_SQL);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by username and password: " + username, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<User> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return users;
    }

    @Override
    public List<User> findByRole(User.UserRole role) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ROLE_SQL);
            stmt.setString(1, role.name());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding users by role: " + role, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return users;
    }

    @Override
    public List<User> findByStatus(Integer status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_STATUS_SQL);
            stmt.setInt(1, status);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding users by status: " + status, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return users;
    }

    @Override
    public boolean existsByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(EXISTS_BY_USERNAME_SQL);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if username exists: " + username, e);
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
            logger.error("Error counting users", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByRole(User.UserRole role) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_ROLE_SQL);
            stmt.setString(1, role.name());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting users by role: " + role, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }
    
    /**
     * 将ResultSet映射为User对象
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRealName(rs.getString("real_name"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setStatus(rs.getInt("status"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        return user;
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