package com.hotel.service;

import com.hotel.dao.CustomerDAO;
import com.hotel.dao.DAOFactory;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.service.impl.BookingServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class BookingServiceTest {

    @Test
    public void testBookingServiceOperations() {
        System.out.println("=== 测试预订服务操作 ===");
        
        try {
            BookingServiceImpl bookingService = new BookingServiceImpl();
            
            // 1. 测试获取所有预订
            System.out.println("1. 获取所有预订:");
            List<Booking> allBookings = bookingService.getAllBookings();
            System.out.println("预订总数: " + allBookings.size());
            
            for (Booking booking : allBookings) {
                System.out.println("  - 预订#" + booking.getBookingId() + 
                                 " 客户ID:" + booking.getCustomerId() +
                                 " 房间ID:" + booking.getRoomId() +
                                 " 状态:" + booking.getStatus());
            }
            
            // 2. 测试根据ID获取预订
            System.out.println("\n2. 根据ID获取预订:");
            if (!allBookings.isEmpty()) {
                Booking firstBooking = bookingService.getBookingById(allBookings.get(0).getBookingId());
                if (firstBooking != null) {
                    System.out.println("找到预订: #" + firstBooking.getBookingId());
                    if (firstBooking.getCustomer() != null) {
                        System.out.println("  客户: " + firstBooking.getCustomer().getName());
                    }
                    if (firstBooking.getRoom() != null) {
                        System.out.println("  房间: " + firstBooking.getRoom().getRoomNumber());
                    }
                } else {
                    System.out.println("未找到预订");
                }
            }
            
            // 3. 测试获取今日入住
            System.out.println("\n3. 获取今日入住:");
            List<Booking> todayCheckIns = bookingService.getTodayCheckIns();
            System.out.println("今日入住数量: " + todayCheckIns.size());
            for (Booking booking : todayCheckIns) {
                System.out.println("  - 预订#" + booking.getBookingId() + 
                                 " 入住日期:" + booking.getCheckInDate());
            }
            
            // 4. 测试获取今日退房
            System.out.println("\n4. 获取今日退房:");
            List<Booking> todayCheckOuts = bookingService.getTodayCheckOuts();
            System.out.println("今日退房数量: " + todayCheckOuts.size());
            for (Booking booking : todayCheckOuts) {
                System.out.println("  - 预订#" + booking.getBookingId() + 
                                 " 退房日期:" + booking.getCheckOutDate());
            }
            
            // 5. 测试根据状态获取预订
            System.out.println("\n5. 根据状态获取预订:");
            List<Booking> confirmedBookings = bookingService.getBookingsByStatus(Booking.BookingStatus.CONFIRMED);
            System.out.println("已确认预订数量: " + confirmedBookings.size());
            
            // 6. 测试创建新预订（如果有足够的数据）
            System.out.println("\n6. 测试创建新预订:");
            try {
                Booking newBooking = new Booking();
                newBooking.setCustomerId(1); // 假设客户ID=1存在
                newBooking.setRoomId(1);     // 假设房间ID=1存在
                newBooking.setCheckInDate(Date.valueOf("2025-12-01"));
                newBooking.setCheckOutDate(Date.valueOf("2025-12-03"));
                newBooking.setGuestsCount(2);
                newBooking.setTotalPrice(new BigDecimal("500.00"));
                newBooking.setStatus(Booking.BookingStatus.PENDING);
                newBooking.setCreatedBy(1); // 假设用户ID=1存在
                
                Integer bookingId = bookingService.createBooking(newBooking);
                if (bookingId != null) {
                    System.out.println("创建预订成功，ID: " + bookingId);
                    
                    // 测试删除刚创建的预订
                    boolean deleteResult = bookingService.deleteBooking(bookingId);
                    System.out.println("删除测试预订结果: " + (deleteResult ? "成功" : "失败"));
                } else {
                    System.out.println("创建预订失败");
                }
                
            } catch (Exception e) {
                System.out.println("创建预订时出错: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("预订服务测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCustomerOperations() {
        System.out.println("=== 测试客户操作 ===");
        
        try {
            CustomerDAO customerDAO = DAOFactory.createCustomerDAO();
            
            // 获取所有客户
            System.out.println("1. 获取所有客户:");
            List<Customer> customers = customerDAO.findAll();
            System.out.println("客户总数: " + customers.size());
            
            for (Customer customer : customers) {
                System.out.println("  - " + customer.getName() + 
                                 " (ID:" + customer.getCustomerId() + 
                                 ", VIP:" + customer.getVipLevel() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("客户操作测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testRoomOperations() {
        System.out.println("=== 测试房间操作 ===");
        
        try {
            RoomDAO roomDAO = DAOFactory.createRoomDAO();
            
            // 获取所有房间
            System.out.println("1. 获取所有房间:");
            List<Room> rooms = roomDAO.findAll();
            System.out.println("房间总数: " + rooms.size());
            
            for (Room room : rooms) {
                System.out.println("  - 房间" + room.getRoomNumber() + 
                                 " (ID:" + room.getRoomId() + 
                                 ", 类型ID:" + room.getTypeId() + 
                                 ", 状态:" + room.getStatus() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("房间操作测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}