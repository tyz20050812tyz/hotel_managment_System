package com.hotel.service;

import com.hotel.service.impl.*;

/**
 * Service工厂类
 * 使用工厂模式创建Service对象
 */
public class ServiceFactory {
    
    // 单例实例缓存
    private static volatile UserService userServiceInstance;
    private static volatile CustomerService customerServiceInstance;
    private static volatile RoomService roomServiceInstance;
    private static volatile BookingService bookingServiceInstance;
    private static volatile MessageService messageServiceInstance;
    
    /**
     * 创建UserService实例
     * @return UserService实例
     */
    public static UserService createUserService() {
        if (userServiceInstance == null) {
            synchronized (ServiceFactory.class) {
                if (userServiceInstance == null) {
                    userServiceInstance = new UserServiceImpl();
                }
            }
        }
        return userServiceInstance;
    }
    
    /**
     * 创建CustomerService实例
     * @return CustomerService实例
     */
    public static CustomerService createCustomerService() {
        if (customerServiceInstance == null) {
            synchronized (ServiceFactory.class) {
                if (customerServiceInstance == null) {
                    customerServiceInstance = new CustomerServiceImpl();
                }
            }
        }
        return customerServiceInstance;
    }
    
    /**
     * 创建RoomService实例
     * @return RoomService实例
     */
    public static RoomService createRoomService() {
        if (roomServiceInstance == null) {
            synchronized (ServiceFactory.class) {
                if (roomServiceInstance == null) {
                    roomServiceInstance = new RoomServiceImpl();
                }
            }
        }
        return roomServiceInstance;
    }
    
    /**
     * 创建BookingService实例
     * @return BookingService实例
     */
    public static BookingService createBookingService() {
        if (bookingServiceInstance == null) {
            synchronized (ServiceFactory.class) {
                if (bookingServiceInstance == null) {
                    bookingServiceInstance = new BookingServiceImpl();
                }
            }
        }
        return bookingServiceInstance;
    }
    
    /**
     * 创建MessageService实例
     * @return MessageService实例
     */
    public static MessageService createMessageService() {
        if (messageServiceInstance == null) {
            synchronized (ServiceFactory.class) {
                if (messageServiceInstance == null) {
                    messageServiceInstance = new MessageServiceImpl();
                }
            }
        }
        return messageServiceInstance;
    }
    
    /**
     * 重置所有Service实例（主要用于测试）
     */
    public static void resetInstances() {
        userServiceInstance = null;
        customerServiceInstance = null;
        roomServiceInstance = null;
        bookingServiceInstance = null;
        messageServiceInstance = null;
    }
}