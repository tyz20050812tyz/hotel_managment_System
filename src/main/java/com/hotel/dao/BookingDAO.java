package com.hotel.dao;

import com.hotel.model.Booking;
import java.sql.Date;
import java.util.List;

/**
 * 预订数据访问接口
 */
public interface BookingDAO {
    
    /**
     * 插入新预订
     * @param booking 预订对象
     * @return 插入成功返回生成的预订ID，失败返回null
     */
    Integer insert(Booking booking);
    
    /**
     * 根据预订ID删除预订
     * @param bookingId 预订ID
     * @return 删除成功返回true
     */
    boolean delete(Integer bookingId);
    
    /**
     * 更新预订信息
     * @param booking 预订对象
     * @return 更新成功返回true
     */
    boolean update(Booking booking);
    
    /**
     * 根据预订ID查找预订
     * @param bookingId 预订ID
     * @return 预订对象，不存在返回null
     */
    Booking findById(Integer bookingId);
    
    /**
     * 查找所有预订
     * @return 预订列表
     */
    List<Booking> findAll();
    
    /**
     * 根据客户ID查找预订
     * @param customerId 客户ID
     * @return 预订列表
     */
    List<Booking> findByCustomerId(Integer customerId);
    
    /**
     * 根据房间ID查找预订
     * @param roomId 房间ID
     * @return 预订列表
     */
    List<Booking> findByRoomId(Integer roomId);
    
    /**
     * 根据预订状态查找预订
     * @param status 预订状态
     * @return 预订列表
     */
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    /**
     * 根据入住日期查找预订
     * @param checkInDate 入住日期
     * @return 预订列表
     */
    List<Booking> findByCheckInDate(Date checkInDate);
    
    /**
     * 根据退房日期查找预订
     * @param checkOutDate 退房日期
     * @return 预订列表
     */
    List<Booking> findByCheckOutDate(Date checkOutDate);
    
    /**
     * 根据日期范围查找预订
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预订列表
     */
    List<Booking> findByDateRange(Date startDate, Date endDate);
    
    /**
     * 查找指定房间在指定日期范围内的预订（用于检查房间可用性）
     * @param roomId 房间ID
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @return 预订列表
     */
    List<Booking> findConflictingBookings(Integer roomId, Date checkInDate, Date checkOutDate);
    
    /**
     * 根据创建人查找预订
     * @param createdBy 创建人ID
     * @return 预订列表
     */
    List<Booking> findByCreatedBy(Integer createdBy);
    
    /**
     * 查找今日入住的预订
     * @return 预订列表
     */
    List<Booking> findTodayCheckIns();
    
    /**
     * 查找今日退房的预订
     * @return 预订列表
     */
    List<Booking> findTodayCheckOuts();
    
    /**
     * 统计预订总数
     * @return 预订总数
     */
    long count();
    
    /**
     * 根据状态统计预订数量
     * @param status 预订状态
     * @return 该状态的预订数量
     */
    long countByStatus(Booking.BookingStatus status);
    
    /**
     * 根据客户统计预订数量
     * @param customerId 客户ID
     * @return 该客户的预订数量
     */
    long countByCustomerId(Integer customerId);
    
    /**
     * 统计指定日期范围内的预订数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预订数量
     */
    long countByDateRange(Date startDate, Date endDate);
}