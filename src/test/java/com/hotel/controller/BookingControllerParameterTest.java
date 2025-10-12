package com.hotel.controller;

import org.junit.Test;
import com.hotel.util.Utils;

/**
 * BookingController参数处理测试
 */
public class BookingControllerParameterTest {
    
    @Test
    public void testGuestsCountValidation() {
        System.out.println("\n=== 测试入住人数验证逻辑 ===");
        
        // 测试各种guestsCount值
        String[] testValues = {"", "0", "-1", "1", "2", "5", "abc", null, "  ", "10"};
        
        for (String value : testValues) {
            System.out.print("测试值: '" + value + "' -> ");
            
            // 模拟Controller中的验证逻辑
            if (Utils.isEmpty(value)) {
                System.out.println("❌ 参数为空或null - 应该显示错误: 入住人数不能为空");
                continue;
            }
            
            try {
                int guestsCount = Integer.parseInt(value.trim());
                if (guestsCount <= 0) {
                    System.out.println("❌ 入住人数 ≤ 0 - 应该显示错误: 入住人数必须为正数");
                } else {
                    System.out.println("✅ 验证通过 - 入住人数: " + guestsCount);
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ 数字格式错误 - 应该显示错误: 入住人数格式不正确");
            }
        }
        
        System.out.println("入住人数验证测试完成\n");
    }
    
    @Test
    public void testParameterProcessingFlow() {
        System.out.println("=== 测试参数处理流程 ===");
        
        // 模拟一个完整的参数验证流程
        String customerId = "1";
        String roomId = "101";
        String checkInDate = "2024-09-26";
        String checkOutDate = "2024-09-27";
        String guestsCount = "2";
        String specialRequests = "测试预订";
        
        System.out.println("模拟参数:");
        System.out.println("- customerId: " + customerId);
        System.out.println("- roomId: " + roomId);
        System.out.println("- checkInDate: " + checkInDate);
        System.out.println("- checkOutDate: " + checkOutDate);
        System.out.println("- guestsCount: " + guestsCount);
        System.out.println("- specialRequests: " + specialRequests);
        
        System.out.println("\n验证结果:");
        
        // 验证每个必填参数
        boolean allValid = true;
        
        if (Utils.isEmpty(customerId)) {
            System.out.println("❌ 客户不能为空");
            allValid = false;
        } else {
            System.out.println("✅ 客户ID验证通过");
        }
        
        if (Utils.isEmpty(roomId)) {
            System.out.println("❌ 房间不能为空");
            allValid = false;
        } else {
            System.out.println("✅ 房间ID验证通过");
        }
        
        if (Utils.isEmpty(checkInDate)) {
            System.out.println("❌ 入住日期不能为空");
            allValid = false;
        } else {
            System.out.println("✅ 入住日期验证通过");
        }
        
        if (Utils.isEmpty(checkOutDate)) {
            System.out.println("❌ 退房日期不能为空");
            allValid = false;
        } else {
            System.out.println("✅ 退房日期验证通过");
        }
        
        if (Utils.isEmpty(guestsCount)) {
            System.out.println("❌ 入住人数不能为空");
            allValid = false;
        } else {
            try {
                int count = Integer.parseInt(guestsCount);
                if (count <= 0) {
                    System.out.println("❌ 入住人数必须为正数");
                    allValid = false;
                } else {
                    System.out.println("✅ 入住人数验证通过: " + count + "人");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ 入住人数格式错误");
                allValid = false;
            }
        }
        
        System.out.println("\n总体验证结果: " + (allValid ? "✅ 所有参数验证通过" : "❌ 存在参数错误"));
        System.out.println("参数处理流程测试完成\n");
    }
    
    @Test
    public void testErrorMessageFormat() {
        System.out.println("=== 测试错误信息格式 ===");
        
        // 模拟各种错误情况及其对应的错误信息
        String[][] errorCases = {
            {"empty_customer", "客户不能为空"},
            {"empty_room", "房间不能为空"},
            {"empty_checkin", "入住日期不能为空"},
            {"empty_checkout", "退房日期不能为空"},
            {"empty_guests", "入住人数不能为空"},
            {"invalid_guests_zero", "入住人数必须为正数"},
            {"invalid_guests_negative", "入住人数必须为正数"},
            {"invalid_date_order", "入住日期不能晚于退房日期"}
        };
        
        System.out.println("预期的错误信息:");
        for (String[] errorCase : errorCases) {
            System.out.println("- " + errorCase[0] + ": " + errorCase[1]);
        }
        
        System.out.println("\n这些错误信息应该在前端正确显示给用户");
        System.out.println("错误信息格式测试完成\n");
    }
}