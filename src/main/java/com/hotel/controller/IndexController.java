package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.service.BookingService;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import com.hotel.service.ServiceFactory;
import com.hotel.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 */
public class IndexController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(IndexController.class);
    private UserService userService;
    private CustomerService customerService;
    private RoomService roomService;
    private BookingService bookingService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = ServiceFactory.createUserService();
        this.customerService = ServiceFactory.createCustomerService();
        this.roomService = ServiceFactory.createRoomService();
        this.bookingService = ServiceFactory.createBookingService();
        logger.info("IndexController initialized");
    }
    
    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getRequestURI();
        
        if (pathInfo.contains("/admin/")) {
            return showAdminDashboard(request);
        } else {
            return showIndexPage(request);
        }
    }
    
    private String showIndexPage(HttpServletRequest request) throws Exception {
        // 首页显示基本信息
        return "/index.jsp";
    }
    
    private String showAdminDashboard(HttpServletRequest request) throws Exception {
        try {
            // 获取统计数据
            Map<String, Object> statistics = getStatistics();
            
            // 获取今日入住/退房信息
            List<Booking> todayCheckIns = bookingService.getTodayCheckIns();
            List<Booking> todayCheckOuts = bookingService.getTodayCheckOuts();
            
            // 获取房间状态统计
            Map<String, Long> roomStatusStats = getRoomStatusStatistics();
            
            // 获取客户VIP级别统计
            Map<String, Long> vipLevelStats = getVipLevelStatistics();
            
            // 设置请求属性
            request.setAttribute("statistics", statistics);
            request.setAttribute("todayCheckIns", todayCheckIns);
            request.setAttribute("todayCheckOuts", todayCheckOuts);
            request.setAttribute("roomStatusStats", roomStatusStats);
            request.setAttribute("vipLevelStats", vipLevelStats);
            
            return "/admin/index.jsp";
            
        } catch (Exception e) {
            logger.error("获取仪表板数据失败", e);
            request.setAttribute("error", "获取数据失败: " + e.getMessage());
            return "/admin/index.jsp";
        }
    }
    
    private Map<String, Object> getStatistics() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        
        // 用户统计
        List<User> allUsers = userService.findAllUsers();
        stats.put("totalUsers", allUsers.size());
        stats.put("adminUsers", allUsers.stream().mapToLong(u -> 
            u.getRole() == User.UserRole.ADMIN ? 1 : 0).sum());
        
        // 客户统计
        List<Customer> allCustomers = customerService.findAllCustomers();
        stats.put("totalCustomers", allCustomers.size());
        stats.put("vipCustomers", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() > 0 ? 1 : 0).sum());
        
        // 房间统计
        List<Room> allRooms = roomService.getAllRooms();
        stats.put("totalRooms", allRooms.size());
        long availableRooms = allRooms.stream().mapToLong(r -> 
            r.getStatus() == Room.RoomStatus.AVAILABLE ? 1 : 0).sum();
        stats.put("availableRooms", availableRooms);
        stats.put("occupiedRooms", allRooms.stream().mapToLong(r -> 
            r.getStatus() == Room.RoomStatus.OCCUPIED ? 1 : 0).sum());
        
        // 计算入住率
        if (allRooms.size() > 0) {
            BigDecimal occupancyRate = BigDecimal.valueOf(allRooms.size() - availableRooms)
                .divide(BigDecimal.valueOf(allRooms.size()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            stats.put("occupancyRate", occupancyRate.setScale(2, RoundingMode.HALF_UP));
        } else {
            stats.put("occupancyRate", BigDecimal.ZERO);
        }
        
        // 预订统计
        List<Booking> allBookings = bookingService.getAllBookings();
        stats.put("totalBookings", allBookings.size());
        stats.put("confirmedBookings", allBookings.stream().mapToLong(b -> 
            b.getStatus() == Booking.BookingStatus.CONFIRMED ? 1 : 0).sum());
        stats.put("checkedInBookings", allBookings.stream().mapToLong(b -> 
            b.getStatus() == Booking.BookingStatus.CHECKED_IN ? 1 : 0).sum());
        
        // 今日统计
        stats.put("todayCheckIns", bookingService.getTodayCheckIns().size());
        stats.put("todayCheckOuts", bookingService.getTodayCheckOuts().size());
        
        return stats;
    }
    
    private Map<String, Long> getRoomStatusStatistics() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        List<Room> allRooms = roomService.getAllRooms();
        
        for (Room.RoomStatus status : Room.RoomStatus.values()) {
            long count = allRooms.stream().mapToLong(r -> 
                r.getStatus() == status ? 1 : 0).sum();
            stats.put(status.name(), count);
        }
        
        return stats;
    }
    
    private Map<String, Long> getVipLevelStatistics() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        List<Customer> allCustomers = customerService.findAllCustomers();
        
        // 统计不同VIP等级的客户数量
        stats.put("REGULAR", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() == null || c.getVipLevel() == 0 ? 1 : 0).sum());
        stats.put("VIP1", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() == 1 ? 1 : 0).sum());
        stats.put("VIP2", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() == 2 ? 1 : 0).sum());
        stats.put("VIP3", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() == 3 ? 1 : 0).sum());
        stats.put("VIP4", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() == 4 ? 1 : 0).sum());
        stats.put("VIP5", allCustomers.stream().mapToLong(c -> 
            c.getVipLevel() != null && c.getVipLevel() == 5 ? 1 : 0).sum());
        
        return stats;
    }
}