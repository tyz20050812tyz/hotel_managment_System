package com.hotel.controller;

import com.hotel.util.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 客户身份证验证测试
 */
public class CustomerIdCardValidationTest {

    @Test
    public void testValidIdCards() {
        // 测试有效的身份证号码
        assertTrue("有效身份证号应该通过验证", Utils.validateIdCard("110101199001011234"));
        assertTrue("有效身份证号应该通过验证", Utils.validateIdCard("320101199001011234")); 
        assertTrue("包含X的有效身份证号应该通过验证", Utils.validateIdCard("44010119900101123X"));
        assertTrue("有效身份证号应该通过验证", Utils.validateIdCard("440101199001011239"));
    }

    @Test
    public void testInvalidIdCards() {
        // 测试无效的身份证号码
        assertFalse("空字符串应该验证失败", Utils.validateIdCard(""));
        assertFalse("null应该验证失败", Utils.validateIdCard(null));
        assertFalse("空白字符串应该验证失败", Utils.validateIdCard("   "));
        
        // 长度不对
        assertFalse("长度不足的身份证号应该验证失败", Utils.validateIdCard("12345"));
        assertFalse("长度超出的身份证号应该验证失败", Utils.validateIdCard("1234567890123456789"));
        
        // 格式不对
        assertFalse("以0开头的身份证号应该验证失败", Utils.validateIdCard("01010119900101123X"));
        assertFalse("无效月份的身份证号应该验证失败", Utils.validateIdCard("110101199013011234"));
        assertFalse("无效日期的身份证号应该验证失败", Utils.validateIdCard("110101199001321234"));
        assertFalse("包含非法字符的身份证号应该验证失败", Utils.validateIdCard("11010119900101123A"));
    }

    @Test
    public void testIdCardFormat() {
        // 测试身份证号码格式：18位，由六位地址码、八位出生日期码（YYYYMMDD格式）、三位顺序码和一位校验码组成
        String validIdCard = "110101199001011234";
        
        assertTrue("身份证号应该是18位", validIdCard.length() == 18);
        
        // 检查地址码（前6位）
        String areaCode = validIdCard.substring(0, 6);
        assertTrue("地址码应该是6位数字", areaCode.matches("\\d{6}"));
        assertFalse("地址码不应该以0开头", areaCode.startsWith("0"));
        
        // 检查出生日期码（第7-14位，YYYYMMDD格式）
        String birthDate = validIdCard.substring(6, 14);
        assertTrue("出生日期码应该是8位数字", birthDate.matches("\\d{8}"));
        
        // 检查年份
        String year = birthDate.substring(0, 4);
        assertTrue("年份应该在1800-2099之间", year.matches("(18|19|20)\\d{2}"));
        
        // 检查月份
        String month = birthDate.substring(4, 6);
        assertTrue("月份应该在01-12之间", month.matches("(0[1-9]|1[0-2])"));
        
        // 检查日期
        String day = birthDate.substring(6, 8);
        assertTrue("日期应该在01-31之间", day.matches("(0[1-9]|[12][0-9]|3[01])"));
        
        // 检查顺序码（第15-17位）
        String sequenceCode = validIdCard.substring(14, 17);
        assertTrue("顺序码应该是3位数字", sequenceCode.matches("\\d{3}"));
        
        // 检查校验码（第18位）
        String checkCode = validIdCard.substring(17, 18);
        assertTrue("校验码应该是数字或X", checkCode.matches("[0-9Xx]"));
    }

    @Test
    public void testIsEmpty() {
        // 测试工具方法
        assertTrue("空字符串应该被认为是空的", Utils.isEmpty(""));
        assertTrue("null应该被认为是空的", Utils.isEmpty(null));
        assertTrue("空白字符串应该被认为是空的", Utils.isEmpty("   "));
        assertFalse("非空字符串不应该被认为是空的", Utils.isEmpty("test"));
    }
}