package com.hotel.util;

import com.hotel.dao.DAOFactory;
import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() {
        System.out.println("=== 测试数据库连接 ===");
        
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            System.out.println("连接池初始化成功");
            
            // 测试获取连接
            Connection conn = pool.getConnection();
            System.out.println("获取数据库连接成功");
            
            // 测试连接有效性
            boolean isValid = conn.isValid(5);
            System.out.println("连接有效性测试: " + (isValid ? "成功" : "失败"));
            
            // 执行简单查询
            PreparedStatement stmt = conn.prepareStatement("SELECT 1 as test");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("查询测试成功，结果: " + rs.getInt("test"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            System.out.println("数据库连接测试完成");
            
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testUserTableOperations() {
        System.out.println("=== 测试用户表操作 ===");
        
        try {
            UserDAO userDAO = DAOFactory.createUserDAO();
            
            // 1. 测试查询所有用户
            System.out.println("1. 查询所有用户:");
            List<User> users = userDAO.findAll();
            System.out.println("用户总数: " + users.size());
            for (User user : users) {
                System.out.println("  - " + user.getUsername() + " (" + user.getRealName() + ")");
            }
            
            // 2. 测试根据用户名查询
            System.out.println("\n2. 根据用户名查询:");
            User admin = userDAO.findByUsername("admin");
            if (admin != null) {
                System.out.println("找到管理员: " + admin.getRealName());
            } else {
                System.out.println("未找到admin用户");
            }
            
            // 3. 测试用户认证
            System.out.println("\n3. 测试用户认证:");
            User authUser = userDAO.findByUsernameAndPassword("admin", "21232f297a57a5a743894a0e4a801fc3");
            if (authUser != null) {
                System.out.println("用户认证成功: " + authUser.getRealName());
            } else {
                System.out.println("用户认证失败");
            }
            
            // 4. 测试插入新用户
            System.out.println("\n4. 测试插入新用户:");
            User newUser = new User();
            newUser.setUsername("test_user_" + System.currentTimeMillis());
            newUser.setPassword("test123");
            newUser.setRealName("测试用户");
            newUser.setRole(User.UserRole.STAFF);
            newUser.setPhone("13800000000");
            newUser.setEmail("test@hotel.com");
            
            Integer userId = userDAO.insert(newUser);
            if (userId != null) {
                System.out.println("插入用户成功，ID: " + userId);
                
                // 5. 测试更新用户
                System.out.println("\n5. 测试更新用户:");
                User updateUser = userDAO.findById(userId);
                updateUser.setRealName("更新后的测试用户");
                boolean updateResult = userDAO.update(updateUser);
                System.out.println("更新结果: " + (updateResult ? "成功" : "失败"));
                
                // 6. 测试删除用户
                System.out.println("\n6. 测试删除用户:");
                boolean deleteResult = userDAO.delete(userId);
                System.out.println("删除结果: " + (deleteResult ? "成功" : "失败"));
            } else {
                System.out.println("插入用户失败");
            }
            
        } catch (Exception e) {
            System.err.println("用户表操作测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testRoomOperations() {
        System.out.println("=== 测试房间表操作 ===");
        
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            
            // 测试查询房间类型
            System.out.println("1. 查询房间类型:");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM room_types");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("  - " + rs.getString("type_name") + " (¥" + rs.getBigDecimal("price") + "/晚)");
            }
            rs.close();
            stmt.close();
            
            // 测试查询房间
            System.out.println("\n2. 查询所有房间:");
            stmt = conn.prepareStatement(
                "SELECT r.room_number, r.floor, r.status, rt.type_name " +
                "FROM rooms r " +
                "LEFT JOIN room_types rt ON r.type_id = rt.type_id " +
                "ORDER BY r.room_number"
            );
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("  - 房间" + rs.getString("room_number") + 
                                 " (" + rs.getString("type_name") + ", " + 
                                 rs.getString("status") + ")");
            }
            rs.close();
            stmt.close();
            
            // 测试查询预订
            System.out.println("\n3. 查询所有预订:");
            stmt = conn.prepareStatement(
                "SELECT b.booking_id, c.name, r.room_number, b.check_in_date, b.check_out_date, b.status " +
                "FROM bookings b " +
                "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                "LEFT JOIN rooms r ON b.room_id = r.room_id " +
                "ORDER BY b.booking_time DESC"
            );
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("  - 预订#" + rs.getInt("booking_id") + 
                                 " " + rs.getString("name") + 
                                 " 房间" + rs.getString("room_number") + 
                                 " (" + rs.getDate("check_in_date") + " 至 " + 
                                 rs.getDate("check_out_date") + ") " +
                                 rs.getString("status"));
            }
            rs.close();
            stmt.close();
            
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("房间操作测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}