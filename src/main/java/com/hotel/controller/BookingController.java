package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.service.BookingService;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import com.hotel.service.ServiceFactory;
import com.hotel.service.strategy.PriceCalculator;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 预订管理控制器
 */
public class BookingController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(BookingController.class);
    private BookingService bookingService;
    private CustomerService customerService;
    private RoomService roomService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.bookingService = ServiceFactory.createBookingService();
        this.customerService = ServiceFactory.createCustomerService();
        this.roomService = ServiceFactory.createRoomService();
        logger.info("BookingController initialized");
    }
    
    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "redirect:/admin/booking/list";
        }
        
        String action = pathInfo.substring(1); // 去掉开头的"/"
        String method = request.getMethod();
        
        if ("GET".equals(method)) {
            return handleGetRequest(request, action);
        } else if ("POST".equals(method)) {
            return handlePostRequest(request, action);
        }
        
        throw new IllegalArgumentException("不支持的HTTP方法: " + method);
    }
    
    private String handleGetRequest(HttpServletRequest request, String action) throws Exception {
        switch (action) {
            case "list":
                return listBookings(request);
            case "add":
                return showAddForm(request);
            case "edit":
                return showEditForm(request);
            case "detail":
                return showBookingDetail(request);
            case "search":
                return searchBookings(request);
            case "todayCheckIn":
            case "today-checkins":
                return todayCheckIn(request);
            case "todayCheckOut":
            case "today-checkouts":
                return todayCheckOut(request);
            case "checkin":
            case "check-in":
                return checkIn(request);
            case "checkout":
            case "check-out":
                return checkOut(request);
            case "confirm":
                return confirmBooking(request);
            case "cancel":
                return cancelBooking(request);
            case "delete":
                return deleteBooking(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String handlePostRequest(HttpServletRequest request, String action) throws Exception {
        switch (action) {
            case "save":
                return saveBooking(request);
            case "delete":
                return deleteBooking(request);
            case "checkIn":
            case "checkin":
            case "check-in":
                return checkIn(request);
            case "checkOut":
            case "checkout":
            case "check-out":
                return checkOut(request);
            case "confirm":
                return confirmBooking(request);
            case "cancel":
                return cancelBooking(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String listBookings(HttpServletRequest request) throws Exception {
        String pageStr = getParameter(request, "page");
        String sizeStr = getParameter(request, "size");
        String status = getParameter(request, "status");
        String customerIdStr = getParameter(request, "customerId");
        
        // 处理操作结果消息
        handleOperationMessage(request);
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        
        List<Booking> bookings;
        
        if (!Utils.isEmpty(status)) {
            bookings = bookingService.getBookingsByStatus(Booking.BookingStatus.valueOf(status));
        } else if (!Utils.isEmpty(customerIdStr)) {
            int customerId = Integer.parseInt(customerIdStr);
            bookings = bookingService.getBookingsByCustomer(customerId);
        } else {
            bookings = bookingService.getAllBookings();
        }
        
        // 简单分页
        int total = bookings.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        if (startIndex < total) {
            bookings = bookings.subList(startIndex, endIndex);
        } else {
            bookings.clear();
        }
        
        request.setAttribute("bookings", bookings);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", total);
        request.setAttribute("totalPages", (total + size - 1) / size);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("selectedCustomerId", customerIdStr);
        
        return "/admin/booking-list.jsp";
    }
    
    private String showAddForm(HttpServletRequest request) throws Exception {
        List<Customer> customers = customerService.findAllCustomers();
        List<Room> availableRooms = roomService.getAvailableRooms();
        request.setAttribute("customers", customers);
        request.setAttribute("availableRooms", availableRooms);
        return "/admin/booking-add.jsp";
    }
    
    private String showEditForm(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("预订ID不能为空");
        }
        
        int bookingId = Integer.parseInt(idStr);
        Booking booking = bookingService.getBookingById(bookingId);
        
        if (booking == null) {
            throw new IllegalArgumentException("预订不存在");
        }
        
        List<Customer> customers = customerService.findAllCustomers();
        List<Room> availableRooms = roomService.getAvailableRooms();
        
        request.setAttribute("booking", booking);
        request.setAttribute("customers", customers);
        request.setAttribute("availableRooms", availableRooms);
        
        return "/admin/booking-edit.jsp";
    }
    
    private String showBookingDetail(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("预订ID不能为空");
        }
        
        int bookingId = Integer.parseInt(idStr);
        Booking booking = bookingService.getBookingById(bookingId);
        
        if (booking == null) {
            throw new IllegalArgumentException("预订不存在");
        }
        
        // 获取关联的客户信息
        if (booking.getCustomerId() != null) {
            Customer customer = customerService.findCustomerById(booking.getCustomerId());
            booking.setCustomer(customer);
        }
        
        // 获取关联的房间信息
        if (booking.getRoomId() != null) {
            Room room = roomService.getRoomById(booking.getRoomId());
            booking.setRoom(room);
        }
        
        request.setAttribute("booking", booking);
        return "/admin/booking-detail.jsp";
    }
    
    private String saveBooking(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "bookingId");
        String customerIdStr = getParameter(request, "customerId");
        String roomIdStr = getParameter(request, "roomId");
        String checkInDateStr = getParameter(request, "checkInDate");
        String checkOutDateStr = getParameter(request, "checkOutDate");
        String guestsCountStr = getParameter(request, "guestsCount");
        String statusStr = getParameter(request, "status");
        String specialRequests = getParameter(request, "specialRequests");
        
        // 参数验证
        if (Utils.isEmpty(customerIdStr)) {
            request.setAttribute("errorMessage", "客户不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(roomIdStr)) {
            request.setAttribute("errorMessage", "房间不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(checkInDateStr)) {
            request.setAttribute("errorMessage", "入住日期不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(checkOutDateStr)) {
            request.setAttribute("errorMessage", "退房日期不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(guestsCountStr)) {
            request.setAttribute("errorMessage", "入住人数不能为空");
            return showAddForm(request);
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            int roomId = Integer.parseInt(roomIdStr);
            int guestsCount = Integer.parseInt(guestsCountStr);
            
            // 验证入住人数
            if (guestsCount <= 0) {
                request.setAttribute("errorMessage", "入住人数必须为正数");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date checkInUtilDate = sdf.parse(checkInDateStr);
            java.util.Date checkOutUtilDate = sdf.parse(checkOutDateStr);
            
            Date checkInDate = new Date(checkInUtilDate.getTime());
            Date checkOutDate = new Date(checkOutUtilDate.getTime());
            
            // 检查日期合理性
            if (checkInDate.after(checkOutDate)) {
                request.setAttribute("errorMessage", "入住日期不能晚于退房日期");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
            Booking booking = new Booking();
            booking.setCustomerId(customerId);
            booking.setRoomId(roomId);
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);
            booking.setGuestsCount(guestsCount);
            booking.setSpecialRequests(specialRequests);
            
            // 获取客户和房间信息以计算价格
            Customer customer = customerService.findCustomerById(customerId);
            Room room = roomService.getRoomById(roomId);
            
            if (customer == null) {
                request.setAttribute("errorMessage", "客户信息不存在");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
            if (room == null) {
                request.setAttribute("errorMessage", "房间信息不存在");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
            // 设置关联对象（用于价格计算）
            booking.setCustomer(customer);
            booking.setRoom(room);
            
            // 计算总价格
            PriceCalculator priceCalculator = new PriceCalculator();
            priceCalculator.setStrategy(customer);
            BigDecimal totalPrice = priceCalculator.calculate(booking);
            booking.setTotalPrice(totalPrice);
            
            if (!Utils.isEmpty(statusStr)) {
                booking.setStatus(Booking.BookingStatus.valueOf(statusStr));
            } else {
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
            }
            
            // 设置创建用户
            User currentUser = getCurrentUser(request);
            if (currentUser != null) {
                booking.setCreatedBy(currentUser.getUserId());
            }
            
            boolean success;
            if (!Utils.isEmpty(idStr)) {
                // 更新预订
                booking.setBookingId(Integer.parseInt(idStr));
                success = bookingService.updateBooking(booking);
            } else {
                // 新增预订
                Integer bookingId = bookingService.createBooking(booking);
                success = bookingId != null;
            }
            
            if (success) {
                String redirectPath = getRedirectPath(request, "/list");
                String messageKey = Utils.isEmpty(idStr) ? "add_success" : "update_success";
                return "redirect:" + redirectPath + "?message=" + messageKey;
            } else {
                request.setAttribute("errorMessage", "操作失败");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
        } catch (Exception e) {
            logger.error("保存预订失败", e);
            request.setAttribute("errorMessage", "保存失败: " + e.getMessage());
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
    }
    
    private String deleteBooking(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("预订ID不能为空", 400);
        }
        
        try {
            int bookingId = Integer.parseInt(idStr);
            boolean success = bookingService.deleteBooking(bookingId);
            
            // 判断请求方法，GET请求返回重定向，POST请求返回JSON
            String method = request.getMethod();
            if ("GET".equals(method)) {
                // 根据请求路径决定重定向目标
                String redirectPath = getRedirectPath(request, "/list");
                if (success) {
                    return "redirect:" + redirectPath + "?message=delete_success";
                } else {
                    return "redirect:" + redirectPath + "?error=delete_failed";
                }
            } else {
                // POST请求返回JSON响应
                if (success) {
                    return "json:" + createSuccessResponse("删除成功");
                } else {
                    return "json:" + createErrorResponse("删除失败", 500);
                }
            }
            
        } catch (NumberFormatException e) {
            if ("GET".equals(request.getMethod())) {
                String redirectPath = getRedirectPath(request, "/list");
                return "redirect:" + redirectPath + "?error=invalid_id";
            } else {
                return "json:" + createErrorResponse("预订ID格式错误", 400);
            }
        }
    }
    
    private String checkIn(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("预订ID不能为空", 400);
        }
        
        try {
            int bookingId = Integer.parseInt(idStr);
            boolean success = bookingService.checkIn(bookingId);
            
            // 判断请求方法，GET请求返回重定向，POST请求返回JSON
            String method = request.getMethod();
            if ("GET".equals(method)) {
                // 检查是否有指定的返回目标
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                
                if (success) {
                    return "redirect:" + redirectPath + "?message=checkin_success";
                } else {
                    return "redirect:" + redirectPath + "?error=checkin_failed";
                }
            } else {
                // POST请求返回JSON响应
                if (success) {
                    return "json:" + createSuccessResponse("入住成功");
                } else {
                    return "json:" + createErrorResponse("入住失败", 500);
                }
            }
            
        } catch (NumberFormatException e) {
            if ("GET".equals(request.getMethod())) {
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                return "redirect:" + redirectPath + "?error=invalid_id";
            } else {
                return "json:" + createErrorResponse("预订ID格式错误", 400);
            }
        } catch (IllegalStateException e) {
            if ("GET".equals(request.getMethod())) {
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                return "redirect:" + redirectPath + "?error=" + URLEncoder.encode(e.getMessage(), "UTF-8");
            } else {
                return "json:" + createErrorResponse(e.getMessage(), 400);
            }
        }
    }
    
    private String checkOut(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("预订ID不能为空", 400);
        }
        
        try {
            int bookingId = Integer.parseInt(idStr);
            boolean success = bookingService.checkOut(bookingId);
            
            // 判断请求方法，GET请求返回重定向，POST请求返回JSON
            String method = request.getMethod();
            if ("GET".equals(method)) {
                // 检查是否有指定的返回目标
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                
                if (success) {
                    return "redirect:" + redirectPath + "?message=checkout_success";
                } else {
                    return "redirect:" + redirectPath + "?error=checkout_failed";
                }
            } else {
                // POST请求返回JSON响应
                if (success) {
                    return "json:" + createSuccessResponse("退房成功");
                } else {
                    return "json:" + createErrorResponse("退房失败", 500);
                }
            }
            
        } catch (NumberFormatException e) {
            if ("GET".equals(request.getMethod())) {
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                return "redirect:" + redirectPath + "?error=invalid_id";
            } else {
                return "json:" + createErrorResponse("预订ID格式错误", 400);
            }
        } catch (IllegalStateException e) {
            if ("GET".equals(request.getMethod())) {
                String returnTo = getParameter(request, "returnTo");
                String redirectPath;
                if ("dashboard".equals(returnTo)) {
                    redirectPath = "/admin/index";
                } else {
                    redirectPath = getRedirectPath(request, "/list");
                }
                return "redirect:" + redirectPath + "?error=" + URLEncoder.encode(e.getMessage(), "UTF-8");
            } else {
                return "json:" + createErrorResponse(e.getMessage(), 400);
            }
        }
    }
    
    private String cancelBooking(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("预订ID不能为空", 400);
        }
        
        try {
            int bookingId = Integer.parseInt(idStr);
            boolean success = bookingService.cancelBooking(bookingId);
            
            // 判断请求方法，GET请求返回重定向，POST请求返回JSON
            String method = request.getMethod();
            if ("GET".equals(method)) {
                // 根据请求路径决定重定向目标
                String redirectPath = getRedirectPath(request, "/list");
                if (success) {
                    return "redirect:" + redirectPath + "?message=cancel_success";
                } else {
                    return "redirect:" + redirectPath + "?error=cancel_failed";
                }
            } else {
                // POST请求返回JSON响应
                if (success) {
                    return "json:" + createSuccessResponse("取消成功");
                } else {
                    return "json:" + createErrorResponse("取消失败", 500);
                }
            }
            
        } catch (NumberFormatException e) {
            if ("GET".equals(request.getMethod())) {
                String redirectPath = getRedirectPath(request, "/list");
                return "redirect:" + redirectPath + "?error=invalid_id";
            } else {
                return "json:" + createErrorResponse("预订ID格式错误", 400);
            }
        }
    }
    
    private String confirmBooking(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("预订ID不能为空", 400);
        }
        
        try {
            int bookingId = Integer.parseInt(idStr);
            boolean success = bookingService.confirmBooking(bookingId);
            
            // 判断请求方法，GET请求返回重定向，POST请求返回JSON
            String method = request.getMethod();
            if ("GET".equals(method)) {
                String redirectPath = getRedirectPath(request, "/list");
                if (success) {
                    return "redirect:" + redirectPath + "?message=confirm_success";
                } else {
                    return "redirect:" + redirectPath + "?error=confirm_failed";
                }
            } else {
                // POST请求返回JSON响应
                if (success) {
                    return "json:" + createSuccessResponse("确认成功");
                } else {
                    return "json:" + createErrorResponse("确认失败", 500);
                }
            }
            
        } catch (NumberFormatException e) {
            if ("GET".equals(request.getMethod())) {
                String redirectPath = getRedirectPath(request, "/list");
                return "redirect:" + redirectPath + "?error=invalid_id";
            } else {
                return "json:" + createErrorResponse("预订ID格式错误", 400);
            }
        } catch (IllegalStateException e) {
            if ("GET".equals(request.getMethod())) {
                String redirectPath = getRedirectPath(request, "/list");
                return "redirect:" + redirectPath + "?error=" + URLEncoder.encode(e.getMessage(), "UTF-8");
            } else {
                return "json:" + createErrorResponse(e.getMessage(), 400);
            }
        }
    }
    
    private String todayCheckIn(HttpServletRequest request) throws Exception {
        List<Booking> bookings = bookingService.getTodayCheckIns();
        request.setAttribute("bookings", bookings);
        request.setAttribute("title", "今日入住");
        request.setAttribute("pageTitle", "今日入住");
        request.setAttribute("todayCheckIns", true);
        return "/admin/booking-list.jsp";
    }
    
    private String todayCheckOut(HttpServletRequest request) throws Exception {
        List<Booking> bookings = bookingService.getTodayCheckOuts();
        request.setAttribute("bookings", bookings);
        request.setAttribute("title", "今日退房");
        request.setAttribute("pageTitle", "今日退房");
        request.setAttribute("todayCheckOuts", true);
        return "/admin/booking-list.jsp";
    }
    
    /**
     * 搜索预订
     * @param request HTTP请求
     * @return 返回结果
     * @throws Exception 异常
     */
    private String searchBookings(HttpServletRequest request) throws Exception {
        String dateFromStr = getParameter(request, "dateFrom");
        String dateToStr = getParameter(request, "dateTo");
        String status = getParameter(request, "status");
        String customerIdStr = getParameter(request, "customerId");
        
        // 处理操作结果消息
        handleOperationMessage(request);
        
        List<Booking> bookings = new ArrayList<>();
        
        try {
            // 按日期范围搜索
            if (!Utils.isEmpty(dateFromStr) && !Utils.isEmpty(dateToStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateFromUtil = sdf.parse(dateFromStr);
                java.util.Date dateToUtil = sdf.parse(dateToStr);
                
                Date dateFrom = new Date(dateFromUtil.getTime());
                Date dateTo = new Date(dateToUtil.getTime());
                
                bookings = bookingService.getBookingsByDateRange(dateFrom, dateTo);
                
                request.setAttribute("dateFrom", dateFromStr);
                request.setAttribute("dateTo", dateToStr);
                request.setAttribute("searchMode", true);
            } 
            // 按状态搜索
            else if (!Utils.isEmpty(status)) {
                bookings = bookingService.getBookingsByStatus(Booking.BookingStatus.valueOf(status));
                request.setAttribute("selectedStatus", status);
                request.setAttribute("searchMode", true);
            }
            // 按客户搜索
            else if (!Utils.isEmpty(customerIdStr)) {
                int customerId = Integer.parseInt(customerIdStr);
                bookings = bookingService.getBookingsByCustomer(customerId);
                request.setAttribute("selectedCustomerId", customerIdStr);
                request.setAttribute("searchMode", true);
            }
            // 无搜索条件，返回所有预订
            else {
                bookings = bookingService.getAllBookings();
            }
            
        } catch (Exception e) {
            logger.error("搜索预订失败", e);
            request.setAttribute("errorMessage", "搜索失败: " + e.getMessage());
            bookings = new ArrayList<>();
        }
        
        // 简单分页处理
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        int total = bookings.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        if (startIndex < total) {
            bookings = bookings.subList(startIndex, endIndex);
        } else {
            bookings.clear();
        }
        
        request.setAttribute("bookings", bookings);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", total);
        request.setAttribute("totalPages", (total + size - 1) / size);
        
        return "/admin/booking-list.jsp";
    }
    
    /**
     * 根据请求路径获取重定向路径
     * @param request HTTP请求
     * @param action 目标动作
     * @return 重定向路径
     */
    private String getRedirectPath(HttpServletRequest request, String action) {
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/admin/booking")) {
            return "/admin/booking" + action;
        } else if (servletPath.startsWith("/booking")) {
            return "/booking" + action;
        } else {
            // 默认返回 admin 路径
            return "/admin/booking" + action;
        }
    }
    
    /**
     * 处理操作结果消息
     * @param request HTTP请求
     */
    private void handleOperationMessage(HttpServletRequest request) {
        String message = getParameter(request, "message");
        String error = getParameter(request, "error");
        
        if (!Utils.isEmpty(message)) {
            String messageText = getMessageText(message);
            request.getSession().setAttribute("message", messageText);
            request.getSession().setAttribute("messageType", "success");
        } else if (!Utils.isEmpty(error)) {
            String errorText = getErrorText(error);
            request.getSession().setAttribute("message", errorText);
            request.getSession().setAttribute("messageType", "danger");
        }
    }
    
    /**
     * 获取成功消息文本
     * @param messageKey 消息键
     * @return 消息文本
     */
    private String getMessageText(String messageKey) {
        switch (messageKey) {
            case "cancel_success":
                return "预订取消成功！";
            case "confirm_success":
                return "预订确认成功！";
            case "checkin_success":
                return "办理入住成功！";
            case "checkout_success":
                return "办理退房成功！";
            case "delete_success":
                return "预订删除成功！";
            case "add_success":
                return "预订添加成功！";
            case "update_success":
                return "预订更新成功！";
            case "success":
                return "操作成功！";
            default:
                return "操作成功！";
        }
    }
    
    /**
     * 获取错误消息文本
     * @param errorKey 错误键
     * @return 错误消息文本
     */
    private String getErrorText(String errorKey) {
        switch (errorKey) {
            case "cancel_failed":
                return "取消预订失败，请稍后重试。";
            case "confirm_failed":
                return "确认预订失败，请稍后重试。";
            case "checkin_failed":
                return "办理入住失败，请稍后重试。";
            case "checkout_failed":
                return "办理退房失败，请稍后重试。";
            case "delete_failed":
                return "删除预订失败，请稍后重试。";
            case "invalid_id":
                return "预订ID无效，请检查后重试。";
            default:
                try {
                    // 尝试解码URL编码的错误消息
                    return java.net.URLDecoder.decode(errorKey, "UTF-8");
                } catch (Exception e) {
                    return "操作失败，请稍后重试。";
                }
        }
    }
}