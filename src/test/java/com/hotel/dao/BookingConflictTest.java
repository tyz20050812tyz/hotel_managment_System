package com.hotel.dao;

import org.junit.Test;
import java.sql.Date;
import java.time.LocalDate;

/**
 * 预订冲突检查测试
 */
public class BookingConflictTest {
    
    @Test
    public void testConflictLogic() {
        System.out.println("=== 测试预订日期冲突逻辑 ===");
        
        // 测试场景：现有预订 2025-09-26 到 2025-09-28
        LocalDate existingCheckIn = LocalDate.of(2025, 9, 26);
        LocalDate existingCheckOut = LocalDate.of(2025, 9, 28);
        
        System.out.println("现有预订：" + existingCheckIn + " 到 " + existingCheckOut);
        System.out.println();
        
        // 测试各种新预订情况
        LocalDate[][] testCases = {
            // 无冲突情况
            {LocalDate.of(2025, 9, 24), LocalDate.of(2025, 9, 26)}, // 早于现有预订
            {LocalDate.of(2025, 9, 28), LocalDate.of(2025, 9, 30)}, // 晚于现有预订
            
            // 冲突情况
            {LocalDate.of(2025, 9, 25), LocalDate.of(2025, 9, 27)}, // 跨越入住日期
            {LocalDate.of(2025, 9, 27), LocalDate.of(2025, 9, 29)}, // 跨越退房日期
            {LocalDate.of(2025, 9, 26), LocalDate.of(2025, 9, 28)}, // 完全重叠
            {LocalDate.of(2025, 9, 25), LocalDate.of(2025, 9, 29)}, // 完全包含现有预订
            {LocalDate.of(2025, 9, 27), LocalDate.of(2025, 9, 27)}, // 在现有预订期间内
        };
        
        String[] descriptions = {
            "早于现有预订（应该无冲突）",
            "晚于现有预订（应该无冲突）", 
            "跨越入住日期（应该有冲突）",
            "跨越退房日期（应该有冲突）",
            "完全重叠（应该有冲突）",
            "完全包含现有预订（应该有冲突）",
            "在现有预订期间内（应该有冲突）"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            LocalDate newCheckIn = testCases[i][0];
            LocalDate newCheckOut = testCases[i][1];
            
            System.out.println("测试 " + (i + 1) + ": " + descriptions[i]);
            System.out.println("新预订：" + newCheckIn + " 到 " + newCheckOut);
            
            // 使用修复后的SQL逻辑检查冲突
            // NOT (existing_checkout <= new_checkin OR existing_checkin >= new_checkout)
            boolean hasConflict = !(existingCheckOut.compareTo(newCheckIn) <= 0 || 
                                   existingCheckIn.compareTo(newCheckOut) >= 0);
            
            System.out.println("冲突检查结果: " + (hasConflict ? "有冲突 ❌" : "无冲突 ✅"));
            
            // 验证预期结果
            boolean expectedConflict = i >= 2; // 前两个应该无冲突，后面的应该有冲突
            
            if (hasConflict == expectedConflict) {
                System.out.println("✅ 检查结果正确");
            } else {
                System.out.println("❌ 检查结果错误，期望: " + (expectedConflict ? "有冲突" : "无冲突"));
            }
            
            System.out.println();
        }
        
        System.out.println("冲突逻辑测试完成");
    }
    
    @Test
    public void testSQLLogicExplanation() {
        System.out.println("\n=== SQL冲突检查逻辑说明 ===");
        System.out.println();
        System.out.println("修复前的问题SQL：");
        System.out.println("WHERE ... AND ((b.check_in_date < ? AND b.check_out_date > ?) OR ...");
        System.out.println("问题：逻辑过于复杂且有错误，导致误报冲突");
        System.out.println();
        
        System.out.println("修复后的正确SQL：");
        System.out.println("WHERE ... AND NOT (b.check_out_date <= ? OR b.check_in_date >= ?)");
        System.out.println("逻辑：使用德摩根定律，检查\"不是无冲突\"即为\"有冲突\"");
        System.out.println();
        
        System.out.println("无冲突的情况：");
        System.out.println("1. 现有预订在新预订之前结束：existing_checkout <= new_checkin");
        System.out.println("2. 现有预订在新预订之后开始：existing_checkin >= new_checkout");
        System.out.println();
        
        System.out.println("有冲突 = NOT(无冲突) = NOT(情况1 OR 情况2)");
        System.out.println("参数绑定：? = new_checkin, ? = new_checkout");
        System.out.println();
        
        System.out.println("这样的逻辑更简洁、正确，避免了复杂的多条件判断");
    }
}