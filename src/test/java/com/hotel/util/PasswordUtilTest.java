package com.hotel.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 密码工具测试类
 */
public class PasswordUtilTest {
    
    @Test
    public void testEncrypt() {
        String password = "test123";
        String encrypted = PasswordUtil.encrypt(password);
        
        assertNotNull("加密结果不应该为null", encrypted);
        assertNotEquals("加密后的密码应该与原密码不同", password, encrypted);
        assertEquals("相同密码加密结果应该相同", encrypted, PasswordUtil.encrypt(password));
    }
    
    @Test
    public void testEncryptNull() {
        String result = PasswordUtil.encrypt(null);
        assertNull("null值加密应该返回null", result);
    }
    
    @Test
    public void testEncryptEmpty() {
        String result = PasswordUtil.encrypt("");
        assertNull("空字符串加密应该返回null", result);
    }
    
    @Test
    public void testVerify() {
        String password = "test123";
        String encrypted = PasswordUtil.encrypt(password);
        
        assertTrue("密码验证应该成功", PasswordUtil.verify(password, encrypted));
        assertFalse("错误密码验证应该失败", PasswordUtil.verify("wrong", encrypted));
    }
    
    @Test
    public void testVerifyNull() {
        assertFalse("null密码验证应该失败", PasswordUtil.verify(null, "encrypted"));
        assertFalse("null加密密码验证应该失败", PasswordUtil.verify("password", null));
        assertFalse("都为null的验证应该失败", PasswordUtil.verify(null, null));
    }
    
    @Test
    public void testGenerateRandomPassword() {
        String password1 = PasswordUtil.generateRandomPassword(8);
        String password2 = PasswordUtil.generateRandomPassword(8);
        
        assertNotNull("生成的密码不应该为null", password1);
        assertEquals("密码长度应该正确", 8, password1.length());
        assertNotEquals("两次生成的密码应该不同", password1, password2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRandomPasswordInvalidLength() {
        PasswordUtil.generateRandomPassword(3);
    }
    
    @Test
    public void testCheckPasswordStrength() {
        assertEquals("空密码强度应该为0", 0, PasswordUtil.checkPasswordStrength(""));
        assertEquals("简单密码强度应该较低", 1, PasswordUtil.checkPasswordStrength("password"));
        assertEquals("复杂密码强度应该较高", 5, PasswordUtil.checkPasswordStrength("Password123!"));
        
        // 测试各种强度
        assertTrue("长密码应该有更高强度", 
                   PasswordUtil.checkPasswordStrength("verylongpassword") > 
                   PasswordUtil.checkPasswordStrength("short"));
    }
    
    @Test
    public void testCheckPasswordStrengthNull() {
        assertEquals("null密码强度应该为0", 0, PasswordUtil.checkPasswordStrength(null));
    }
}