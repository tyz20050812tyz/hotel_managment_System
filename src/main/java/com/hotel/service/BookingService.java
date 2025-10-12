package com.hotel.service;

import com.hotel.model.Booking;
import java.sql.Date;
import java.util.List;

/**
 * 预订服务接口
 * 提供预订相关的业务逻辑操作
 */
public interface BookingService {
    
    /**
     * 创建新预订
     * @param booking 预订对象
     * @return 创建成功返回预订ID，失败返回null
     */
    Integer createBooking(Booking booking);
    
    /**
     * 删除预订
     * @param bookingId 预订ID
     * @return 删除成功返回true
     */
    boolean deleteBooking(Integer bookingId);
    
    /**
     * 更新预订信息
     * @param booking 预订对象
     * @return 更新成功返回true
     */
    boolean updateBooking(Booking booking);
    
    /**
     * 根据预订ID查找预订
     * @param bookingId 预订ID
     * @return 预订对象，不存在返回null
     */
    Booking getBookingById(Integer bookingId);
    
    /**
     * 获取所有预订
     * @return 预订列表
     */
    List<Booking> getAllBookings();
    
    /**
     * 根据客户ID获取预订列表
     * @param customerId 客户ID
     * @return 预订列表
     */
    List<Booking> getBookingsByCustomer(Integer customerId);
    
    /**
     * 根据房间ID获取预订列表
     * @param roomId 房间ID
     * @return 预订列表
     */
    List<Booking> getBookingsByRoom(Integer roomId);
    
    /**
     * 根据预订状态获取预订列表
     * @param status 预订状态
     * @return 预订列表
     */
    List<Booking> getBookingsByStatus(Booking.BookingStatus status);
    
    /**
     * 根据入住日期获取预订列表
     * @param checkInDate 入住日期
     * @return 预订列表
     */
    List<Booking> getBookingsByCheckInDate(Date checkInDate);
    
    /**
     * 根据退房日期获取预订列表
     * @param checkOutDate 退房日期
     * @return 预订列表
     */
    List<Booking> getBookingsByCheckOutDate(Date checkOutDate);
    
    /**
     * 根据日期范围获取预订列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预订列表
     */
    List<Booking> getBookingsByDateRange(Date startDate, Date endDate);
    
    /**
     * 检查房间在指定日期范围内是否可用
     * @param roomId 房间ID
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @return 可用返回true
     */
    boolean isRoomAvailable(Integer roomId, Date checkInDate, Date checkOutDate);
    
    /**
     * 获取指定房间在指定日期范围内的冲突预订
     * @param roomId 房间ID
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @return 冲突预订列表
     */
    List<Booking> getConflictingBookings(Integer roomId, Date checkInDate, Date checkOutDate);
    
    /**
     * 根据创建人获取预订列表
     * @param createdBy 创建人ID
     * @return 预订列表
     */
    List<Booking> getBookingsByCreator(Integer createdBy);
    
    /**
     * 获取今日入住的预订
     * @return 今日入住预订列表
     */
    List<Booking> getTodayCheckIns();
    
    /**
     * 获取今日退房的预订
     * @return 今日退房预订列表
     */
    List<Booking> getTodayCheckOuts();
    
    /**
     * 确认预订
     * @param bookingId 预订ID
     * @return 操作成功返回true
     */
    boolean confirmBooking(Integer bookingId);
    
    /**
     * 办理入住
     * @param bookingId 预订ID
     * @return 操作成功返回true
     */
    boolean checkIn(Integer bookingId);
    
    /**
     * 办理退房
     * @param bookingId 预订ID
     * @return 操作成功返回true
     */
    boolean checkOut(Integer bookingId);
    
    /**
     * 取消预订
     * @param bookingId 预订ID
     * @return 操作成功返回true
     */
    boolean cancelBooking(Integer bookingId);
    
    /**
     * 检查预订是否可以取消
     * @param bookingId 预订ID
     * @return 可以取消返回true
     */
    boolean canCancelBooking(Integer bookingId);
    
    /**
     * 检查预订是否可以办理入住
     * @param bookingId 预订ID
     * @return 可以入住返回true
     */
    boolean canCheckIn(Integer bookingId);
    
    /**
     * 检查预订是否可以办理退房
     * @param bookingId 预订ID
     * @return 可以退房返回true
     */
    boolean canCheckOut(Integer bookingId);
    
    /**
     * 统计预订总数
     * @return 预订总数
     */
    long getTotalBookingCount();
    
    /**
     * 根据状态统计预订数量
     * @param status 预订状态
     * @return 该状态的预订数量
     */
    long getBookingCountByStatus(Booking.BookingStatus status);
    
    /**
     * 根据客户统计预订数量
     * @param customerId 客户ID
     * @return 该客户的预订数量
     */
    long getBookingCountByCustomer(Integer customerId);
    
    /**
     * 统计指定日期范围内的预订数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预订数量
     */
    long getBookingCountByDateRange(Date startDate, Date endDate);
}