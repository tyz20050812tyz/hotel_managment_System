package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;
import com.hotel.service.ServiceFactory;
import org.junit.Test;
import java.util.List;

/**
 * 测试入住功能
 */
public class CheckInTest {
    
    @Test
    public void testCheckInFunctionality() {
        System.out.println("\n=== 测试入住功能 ===");
        
        try {
            BookingService bookingService = ServiceFactory.createBookingService();
            
            // 1. 找一个状态为 CONFIRMED 的预订
            System.out.println("1. 查找已确认的预订:");
            List<Booking> allBookings = bookingService.getAllBookings();
            Booking confirmedBooking = null;
            
            for (Booking booking : allBookings) {
                System.out.println("  - 预订#" + booking.getBookingId() + 
                                 " 状态:" + booking.getStatus() + 
                                 " 客户ID:" + booking.getCustomerId() +
                                 " 房间ID:" + booking.getRoomId());
                
                if (booking.getStatus() == Booking.BookingStatus.CONFIRMED && confirmedBooking == null) {
                    confirmedBooking = booking;
                }
            }
            
            if (confirmedBooking == null) {
                System.out.println("❌ 没有找到已确认的预订用于测试");
                return;
            }
            
            System.out.println("✅ 找到测试预订: #" + confirmedBooking.getBookingId());
            
            // 2. 测试是否可以入住
            System.out.println("\n2. 检查预订是否可以入住:");
            boolean canCheckIn = confirmedBooking.canCheckIn();
            System.out.println("  - 预订#" + confirmedBooking.getBookingId() + " 可以入住: " + canCheckIn);
            
            if (!canCheckIn) {
                System.out.println("❌ 预订不满足入住条件");
                return;
            }
            
            // 3. 执行入住操作
            System.out.println("\n3. 执行入住操作:");
            try {
                boolean checkInResult = bookingService.checkIn(confirmedBooking.getBookingId());
                System.out.println("  - 入住操作结果: " + (checkInResult ? "成功" : "失败"));
                
                if (checkInResult) {
                    // 4. 验证状态是否更新
                    System.out.println("\n4. 验证预订状态更新:");
                    Booking updatedBooking = bookingService.getBookingById(confirmedBooking.getBookingId());
                    if (updatedBooking != null) {
                        System.out.println("  - 更新后状态: " + updatedBooking.getStatus());
                        
                        if (updatedBooking.getStatus() == Booking.BookingStatus.CHECKED_IN) {
                            System.out.println("✅ 入住功能测试成功！预订状态已更新为已入住");
                        } else {
                            System.out.println("❌ 预订状态未正确更新");
                        }
                    } else {
                        System.out.println("❌ 无法获取更新后的预订信息");
                    }
                } else {
                    System.out.println("❌ 入住操作失败");
                }
                
            } catch (Exception e) {
                System.out.println("❌ 入住操作异常: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.out.println("❌ 测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCheckInWithInvalidBooking() {
        System.out.println("\n=== 测试无效预订入住 ===");
        
        try {
            BookingService bookingService = ServiceFactory.createBookingService();
            
            // 测试不存在的预订ID
            System.out.println("1. 测试不存在的预订ID:");
            try {
                boolean result = bookingService.checkIn(99999);
                System.out.println("  - 结果: " + result + " (应该为false)");
            } catch (Exception e) {
                System.out.println("  - 异常: " + e.getMessage());
            }
            
            // 测试已入住的预订
            System.out.println("\n2. 查找已入住的预订:");
            List<Booking> allBookings = bookingService.getAllBookings();
            Booking checkedInBooking = null;
            
            for (Booking booking : allBookings) {
                if (booking.getStatus() == Booking.BookingStatus.CHECKED_IN) {
                    checkedInBooking = booking;
                    break;
                }
            }
            
            if (checkedInBooking != null) {
                System.out.println("  - 找到已入住预订: #" + checkedInBooking.getBookingId());
                try {
                    boolean result = bookingService.checkIn(checkedInBooking.getBookingId());
                    System.out.println("  - 重复入住结果: " + result + " (应该为false或抛出异常)");
                } catch (IllegalStateException e) {
                    System.out.println("  - 正确抛出异常: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("  - 意外异常: " + e.getMessage());
                }
            } else {
                System.out.println("  - 没有找到已入住的预订");
            }
            
        } catch (Exception e) {
            System.out.println("❌ 测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}