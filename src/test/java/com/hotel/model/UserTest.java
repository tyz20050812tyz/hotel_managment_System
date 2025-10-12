package com.hotel.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Timestamp;

/**
 * 用户实体类测试
 */
public class UserTest {
    
    private User user;
    
    @Before
    public void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRealName("Test User");
        user.setRole(User.UserRole.STAFF);
        user.setPhone("13800138000");
        user.setEmail("test@example.com");
        user.setStatus(1);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
    }
    
    @Test
    public void testConstructor() {
        User newUser = new User("admin", "admin123", "Administrator", User.UserRole.ADMIN);
        
        assertEquals("用户名应该正确设置", "admin", newUser.getUsername());
        assertEquals("密码应该正确设置", "admin123", newUser.getPassword());
        assertEquals("真实姓名应该正确设置", "Administrator", newUser.getRealName());
        assertEquals("角色应该正确设置", User.UserRole.ADMIN, newUser.getRole());
        assertEquals("默认状态应该为1", Integer.valueOf(1), newUser.getStatus());
    }
    
    @Test
    public void testGettersAndSetters() {
        assertEquals("用户ID应该正确", Integer.valueOf(1), user.getUserId());
        assertEquals("用户名应该正确", "testuser", user.getUsername());
        assertEquals("密码应该正确", "password123", user.getPassword());
        assertEquals("真实姓名应该正确", "Test User", user.getRealName());
        assertEquals("角色应该正确", User.UserRole.STAFF, user.getRole());
        assertEquals("电话应该正确", "13800138000", user.getPhone());
        assertEquals("邮箱应该正确", "test@example.com", user.getEmail());
        assertEquals("状态应该正确", Integer.valueOf(1), user.getStatus());
        assertNotNull("创建时间不应该为null", user.getCreateTime());
    }
    
    @Test
    public void testIsActive() {
        user.setStatus(1);
        assertTrue("状态为1时应该是活跃的", user.isActive());
        
        user.setStatus(0);
        assertFalse("状态为0时应该是非活跃的", user.isActive());
        
        user.setStatus(null);
        assertFalse("状态为null时应该是非活跃的", user.isActive());
    }
    
    @Test
    public void testIsAdmin() {
        user.setRole(User.UserRole.ADMIN);
        assertTrue("管理员角色应该返回true", user.isAdmin());
        
        user.setRole(User.UserRole.STAFF);
        assertFalse("员工角色应该返回false", user.isAdmin());
    }
    
    @Test
    public void testIsStaff() {
        user.setRole(User.UserRole.STAFF);
        assertTrue("员工角色应该返回true", user.isStaff());
        
        user.setRole(User.UserRole.ADMIN);
        assertFalse("管理员角色应该返回false", user.isStaff());
    }
    
    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setUserId(1);
        
        User user2 = new User();
        user2.setUserId(1);
        
        User user3 = new User();
        user3.setUserId(2);
        
        assertEquals("相同ID的用户应该相等", user1, user2);
        assertNotEquals("不同ID的用户应该不相等", user1, user3);
        
        // 测试null情况
        User nullUser = new User();
        assertNotEquals("ID为null的用户应该不相等", user1, nullUser);
    }
    
    @Test
    public void testHashCode() {
        User user1 = new User();
        user1.setUserId(1);
        
        User user2 = new User();
        user2.setUserId(1);
        
        assertEquals("相同ID的用户hashCode应该相等", user1.hashCode(), user2.hashCode());
    }
    
    @Test
    public void testToString() {
        String result = user.toString();
        assertNotNull("toString不应该返回null", result);
        assertTrue("toString应该包含用户ID", result.contains("userId=1"));
        assertTrue("toString应该包含用户名", result.contains("username='testuser'"));
    }
    
    @Test
    public void testUserRoleEnum() {
        assertEquals("管理员描述应该正确", "管理员", User.UserRole.ADMIN.getDescription());
        assertEquals("员工描述应该正确", "员工", User.UserRole.STAFF.getDescription());
    }
}