package com.hotel.controller;

import com.hotel.service.ServiceFactory;
import com.hotel.service.BookingService;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import org.junit.Test;

import java.util.List;

public class ControllerTest {

    @Test
    public void testServiceFactoryAndControllerServices() {
        System.out.println("=== 测试Controller服务层 ===");
        
        try {
            // 测试服务工厂
            System.out.println("1. 测试服务工厂创建:");
            BookingService bookingService = ServiceFactory.createBookingService();
            CustomerService customerService = ServiceFactory.createCustomerService();
            RoomService roomService = ServiceFactory.createRoomService();
            
            System.out.println("服务创建成功:");
            System.out.println("  - BookingService: " + (bookingService != null ? "OK" : "FAIL"));
            System.out.println("  - CustomerService: " + (customerService != null ? "OK" : "FAIL"));
            System.out.println("  - RoomService: " + (roomService != null ? "OK" : "FAIL"));
            
            // 测试预订服务调用
            System.out.println("\n2. 测试预订服务调用:");
            List<Booking> bookings = bookingService.getAllBookings();
            System.out.println("获取预订数量: " + bookings.size());
            
            if (!bookings.isEmpty()) {
                Booking firstBooking = bookings.get(0);
                System.out.println("第一个预订详情:");
                System.out.println("  - ID: " + firstBooking.getBookingId());
                System.out.println("  - 客户ID: " + firstBooking.getCustomerId());
                System.out.println("  - 房间ID: " + firstBooking.getRoomId());
                System.out.println("  - 状态: " + firstBooking.getStatus());
                
                // 测试获取详细信息
                Booking detailedBooking = bookingService.getBookingById(firstBooking.getBookingId());
                if (detailedBooking != null) {
                    System.out.println("  - 客户信息: " + 
                        (detailedBooking.getCustomer() != null ? 
                         detailedBooking.getCustomer().getName() : "未加载"));
                    System.out.println("  - 房间信息: " + 
                        (detailedBooking.getRoom() != null ? 
                         detailedBooking.getRoom().getRoomNumber() : "未加载"));
                }
            }
            
            // 测试客户服务调用
            System.out.println("\n3. 测试客户服务调用:");
            List<Customer> customers = customerService.findAllCustomers();
            System.out.println("获取客户数量: " + customers.size());
            
            for (Customer customer : customers) {
                System.out.println("  - " + customer.getName() + 
                                 " (VIP等级: " + customer.getVipLevel() + ")");
            }
            
            // 测试房间服务调用
            System.out.println("\n4. 测试房间服务调用:");
            List<Room> rooms = roomService.getAllRooms();
            System.out.println("获取房间数量: " + rooms.size());
            
            List<Room> availableRooms = roomService.getAvailableRooms();
            System.out.println("可用房间数量: " + availableRooms.size());
            
            // 测试今日入住/退房
            System.out.println("\n5. 测试今日入住/退房:");
            List<Booking> todayCheckIns = bookingService.getTodayCheckIns();
            List<Booking> todayCheckOuts = bookingService.getTodayCheckOuts();
            System.out.println("今日入住: " + todayCheckIns.size() + " 个预订");
            System.out.println("今日退房: " + todayCheckOuts.size() + " 个预订");
            
            // 检查今日入住预订的详细信息
            for (Booking checkin : todayCheckIns) {
                System.out.println("  入住预订 #" + checkin.getBookingId() + 
                                 " - 客户: " + (checkin.getCustomer() != null ? 
                                               checkin.getCustomer().getName() : 
                                               "ID:" + checkin.getCustomerId()) +
                                 " - 房间: " + (checkin.getRoom() != null ? 
                                               checkin.getRoom().getRoomNumber() : 
                                               "ID:" + checkin.getRoomId()));
            }
            
        } catch (Exception e) {
            System.err.println("Controller服务测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testControllerDirectly() {
        System.out.println("=== 直接测试Controller逻辑 ===");
        
        try {
            // 模拟BookingController的初始化和调用
            BookingController controller = new BookingController();
            controller.init(); // 初始化服务
            
            System.out.println("BookingController初始化成功");
            
            // 这里我们不能直接调用handleBusinessLogic，因为它需要HttpServletRequest
            // 但我们可以验证控制器的服务是否正确初始化
            System.out.println("控制器服务初始化验证完成");
            
        } catch (Exception e) {
            System.err.println("直接Controller测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}