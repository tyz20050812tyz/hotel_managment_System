package com.hotel.dao;

import com.hotel.dao.impl.*;

/**
 * DAO工厂类
 * 使用工厂模式创建DAO对象
 */
public class DAOFactory {
    
    // 单例实例缓存
    private static volatile UserDAO userDAOInstance;
    private static volatile CustomerDAO customerDAOInstance;
    private static volatile RoomDAO roomDAOInstance;
    private static volatile RoomTypeDAO roomTypeDAOInstance;
    private static volatile BookingDAO bookingDAOInstance;
    
    /**
     * 创建UserDAO实例
     * @return UserDAO实例
     */
    public static UserDAO createUserDAO() {
        if (userDAOInstance == null) {
            synchronized (DAOFactory.class) {
                if (userDAOInstance == null) {
                    userDAOInstance = new UserDAOImpl();
                }
            }
        }
        return userDAOInstance;
    }
    
    /**
     * 创建CustomerDAO实例
     * @return CustomerDAO实例
     */
    public static CustomerDAO createCustomerDAO() {
        if (customerDAOInstance == null) {
            synchronized (DAOFactory.class) {
                if (customerDAOInstance == null) {
                    customerDAOInstance = new CustomerDAOImpl();
                }
            }
        }
        return customerDAOInstance;
    }
    
    /**
     * 创建RoomDAO实例
     * @return RoomDAO实例
     */
    public static RoomDAO createRoomDAO() {
        if (roomDAOInstance == null) {
            synchronized (DAOFactory.class) {
                if (roomDAOInstance == null) {
                    roomDAOInstance = new RoomDAOImpl();
                }
            }
        }
        return roomDAOInstance;
    }
    
    /**
     * 创建RoomTypeDAO实例
     * @return RoomTypeDAO实例
     */
    public static RoomTypeDAO createRoomTypeDAO() {
        if (roomTypeDAOInstance == null) {
            synchronized (DAOFactory.class) {
                if (roomTypeDAOInstance == null) {
                    roomTypeDAOInstance = new RoomTypeDAOImpl();
                }
            }
        }
        return roomTypeDAOInstance;
    }
    
    /**
     * 创建BookingDAO实例
     * @return BookingDAO实例
     */
    public static BookingDAO createBookingDAO() {
        if (bookingDAOInstance == null) {
            synchronized (DAOFactory.class) {
                if (bookingDAOInstance == null) {
                    bookingDAOInstance = new BookingDAOImpl();
                }
            }
        }
        return bookingDAOInstance;
    }
    
    /**
     * 重置所有DAO实例（主要用于测试）
     */
    public static void resetInstances() {
        userDAOInstance = null;
        customerDAOInstance = null;
        roomDAOInstance = null;
        roomTypeDAOInstance = null;
        bookingDAOInstance = null;
    }
}