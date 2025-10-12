package com.hotel.controller;

import org.junit.Test;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.model.RoomType;
import com.hotel.service.strategy.PriceCalculator;
import com.hotel.util.Utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

/**
 * 预订总价格计算测试
 */
public class BookingTotalPriceTest {
    
    @Test
    public void testTotalPriceCalculation() {
        System.out.println("=== 测试预订总价格计算功能 ===");
        
        // 创建房间类型
        RoomType roomType = new RoomType();
        roomType.setTypeId(1);
        roomType.setTypeName("标准间");
        roomType.setPrice(new BigDecimal("200.00")); // 每晚200元
        roomType.setMaxGuests(2);
        
        // 创建房间
        Room room = new Room();
        room.setRoomId(101);
        room.setRoomNumber("101");
        room.setRoomType(roomType);
        room.setStatus(Room.RoomStatus.AVAILABLE);
        
        // 测试普通客户
        testRegularCustomer(room);
        
        // 测试VIP客户
        testVIPCustomers(room);
        
        System.out.println("总价格计算测试完成\n");
    }
    
    private void testRegularCustomer(Room room) {
        System.out.println("\n--- 测试普通客户价格计算 ---");
        
        // 创建普通客户
        Customer regularCustomer = new Customer();
        regularCustomer.setCustomerId(1);
        regularCustomer.setName("张三");
        regularCustomer.setVipLevel(0); // 0表示普通客户
        
        // 创建预订（2晚）
        Booking booking = createBooking(regularCustomer, room, 2);
        
        // 计算价格
        PriceCalculator calculator = new PriceCalculator();
        calculator.setStrategy(regularCustomer);
        BigDecimal totalPrice = calculator.calculate(booking);
        
        System.out.println("客户: " + regularCustomer.getName() + "（普通客户）");
        System.out.println("房间: " + room.getRoomNumber() + " - " + room.getRoomType().getTypeName());
        System.out.println("每晚价格: ¥" + room.getRoomType().getPrice());
        System.out.println("住宿天数: " + booking.getStayDays() + "晚");
        System.out.println("折扣率: " + (calculator.getCurrentDiscountRate() * 100) + "%");
        System.out.println("总价格: ¥" + totalPrice);
        System.out.println("预期价格: ¥400.00 (200 × 2晚 × 无折扣)");
        
        // 验证
        BigDecimal expected = new BigDecimal("400.00");
        if (totalPrice.compareTo(expected) == 0) {
            System.out.println("✅ 普通客户价格计算正确");
        } else {
            System.out.println("❌ 普通客户价格计算错误，期望: " + expected + "，实际: " + totalPrice);
        }
    }
    
    private void testVIPCustomers(Room room) {
        System.out.println("\n--- 测试VIP客户价格计算 ---");
        
        int[] vipLevels = {1, 2, 3, 4, 5};
        double[] expectedDiscounts = {0.95, 0.90, 0.85, 0.80, 0.75};
        BigDecimal[] expectedPrices = {
            new BigDecimal("380.00"), // 200 × 2 × 0.95
            new BigDecimal("360.00"), // 200 × 2 × 0.90
            new BigDecimal("340.00"), // 200 × 2 × 0.85
            new BigDecimal("320.00"), // 200 × 2 × 0.80
            new BigDecimal("300.00")  // 200 × 2 × 0.75
        };
        
        for (int i = 0; i < vipLevels.length; i++) {
            int vipLevel = vipLevels[i];
            double expectedDiscount = expectedDiscounts[i];
            BigDecimal expectedPrice = expectedPrices[i];
            
            // 创建VIP客户
            Customer vipCustomer = new Customer();
            vipCustomer.setCustomerId(2 + i);
            vipCustomer.setName("VIP客户" + vipLevel);
            vipCustomer.setVipLevel(vipLevel); // 设置VIP等级
            
            // 创建预订（2晚）
            Booking booking = createBooking(vipCustomer, room, 2);
            
            // 计算价格
            PriceCalculator calculator = new PriceCalculator();
            calculator.setStrategy(vipCustomer);
            BigDecimal totalPrice = calculator.calculate(booking);
            
            System.out.println("\n客户: " + vipCustomer.getName() + " (VIP" + vipLevel + ")");
            System.out.println("房间: " + room.getRoomNumber() + " - " + room.getRoomType().getTypeName());
            System.out.println("每晚价格: ¥" + room.getRoomType().getPrice());
            System.out.println("住宿天数: " + booking.getStayDays() + "晚");
            System.out.println("折扣率: " + (calculator.getCurrentDiscountRate() * 100) + "%");
            System.out.println("总价格: ¥" + totalPrice);
            System.out.println("预期价格: ¥" + expectedPrice);
            
            // 验证
            if (totalPrice.compareTo(expectedPrice) == 0) {
                System.out.println("✅ VIP" + vipLevel + "客户价格计算正确");
            } else {
                System.out.println("❌ VIP" + vipLevel + "客户价格计算错误，期望: " + expectedPrice + "，实际: " + totalPrice);
            }
        }
    }
    
    private Booking createBooking(Customer customer, Room room, int days) {
        Booking booking = new Booking();
        booking.setCustomerId(customer.getCustomerId());
        booking.setRoomId(room.getRoomId());
        booking.setGuestsCount(2);
        
        // 设置日期
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(days);
        
        booking.setCheckInDate(Date.valueOf(checkInDate));
        booking.setCheckOutDate(Date.valueOf(checkOutDate));
        
        // 设置关联对象
        booking.setCustomer(customer);
        booking.setRoom(room);
        
        return booking;
    }
    
    @Test
    public void testParameterValidation() {
        System.out.println("=== 测试参数验证逻辑 ===");
        
        // 测试空值处理
        String[] testParams = {"", null, "  ", "0", "-1", "abc", "1", "5"};
        String[] paramNames = {"customerId", "roomId", "checkInDate", "checkOutDate", "guestsCount"};
        
        for (String paramName : paramNames) {
            System.out.println("\n--- 测试参数: " + paramName + " ---");
            
            for (String testValue : testParams) {
                System.out.print("值: '" + testValue + "' -> ");
                
                if (Utils.isEmpty(testValue)) {
                    System.out.println("❌ 参数为空 - 应显示: " + paramName + "不能为空");
                } else {
                    if (paramName.equals("guestsCount")) {
                        try {
                            int value = Integer.parseInt(testValue.trim());
                            if (value <= 0) {
                                System.out.println("❌ 值无效 - 应显示: 入住人数必须为正数");
                            } else {
                                System.out.println("✅ 值有效: " + value);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("❌ 格式错误 - 应显示: 入住人数格式不正确");
                        }
                    } else if (paramName.equals("customerId") || paramName.equals("roomId")) {
                        try {
                            int value = Integer.parseInt(testValue.trim());
                            if (value <= 0) {
                                System.out.println("❌ ID无效");
                            } else {
                                System.out.println("✅ ID有效: " + value);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("❌ ID格式错误");
                        }
                    } else {
                        System.out.println("✅ 参数不为空");
                    }
                }
            }
        }
        
        System.out.println("\n参数验证测试完成");
    }
}