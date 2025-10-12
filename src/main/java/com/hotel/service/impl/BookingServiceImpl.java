package com.hotel.service.impl;

import com.hotel.dao.BookingDAO;
import com.hotel.dao.DAOFactory;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.service.BookingService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

/**
 * 预订服务实现类
 */
public class BookingServiceImpl implements BookingService {
    
    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    
    public BookingServiceImpl() {
        this.bookingDAO = DAOFactory.createBookingDAO();
        this.roomDAO = DAOFactory.createRoomDAO();
    }
    
    @Override
    public Integer createBooking(Booking booking) {
        // 参数验证
        if (booking == null) {
            throw new IllegalArgumentException("预订对象不能为空");
        }
        
        if (booking.getCustomerId() == null) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        if (booking.getRoomId() == null) {
            throw new IllegalArgumentException("房间ID不能为空");
        }
        
        if (booking.getCheckInDate() == null) {
            throw new IllegalArgumentException("入住日期不能为空");
        }
        
        if (booking.getCheckOutDate() == null) {
            throw new IllegalArgumentException("退房日期不能为空");
        }
        
        if (booking.getCheckInDate().after(booking.getCheckOutDate())) {
            throw new IllegalArgumentException("入住日期不能晚于退房日期");
        }
        
        // 添加入住日期不能早于今天的验证
        if (booking.getCheckInDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("入住日期不能早于今天");
        }
        
        if (booking.getGuestsCount() == null || booking.getGuestsCount() <= 0) {
            throw new IllegalArgumentException("入住人数必须为正数");
        }
        
        // 检查房间是否存在
        Room room = roomDAO.findById(booking.getRoomId());
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }
        
        // 检查房间在指定时间段是否可用
        if (!isRoomAvailable(booking.getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new IllegalArgumentException("房间在指定时间段不可用");
        }
        
        // 设置默认值
        if (booking.getStatus() == null) {
            booking.setStatus(Booking.BookingStatus.PENDING);
        }
        
        if (booking.getBookingTime() == null) {
            booking.setBookingTime(new Timestamp(System.currentTimeMillis()));
        }
        
        return bookingDAO.insert(booking);
    }
    
    @Override
    public boolean deleteBooking(Integer bookingId) {
        if (bookingId == null || bookingId <= 0) {
            throw new IllegalArgumentException("预订ID无效");
        }
        
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            return false;
        }
        
        // 只有待确认和已取消的预订才能删除
        if (booking.getStatus() != Booking.BookingStatus.PENDING && 
            booking.getStatus() != Booking.BookingStatus.CANCELLED) {
            throw new IllegalStateException("只有待确认和已取消的预订才能删除");
        }
        
        return bookingDAO.delete(bookingId);
    }
    
    @Override
    public boolean updateBooking(Booking booking) {
        if (booking == null || booking.getBookingId() == null) {
            throw new IllegalArgumentException("预订ID不能为空");
        }
        
        Booking existingBooking = bookingDAO.findById(booking.getBookingId());
        if (existingBooking == null) {
            return false;
        }
        
        // 检查是否可以修改
        if (existingBooking.getStatus() == Booking.BookingStatus.CHECKED_IN || 
            existingBooking.getStatus() == Booking.BookingStatus.CHECKED_OUT) {
            throw new IllegalStateException("已入住或已退房的预订不能修改");
        }
        
        // 如果修改了房间或日期，需要重新检查可用性
        if (!booking.getRoomId().equals(existingBooking.getRoomId()) ||
            !booking.getCheckInDate().equals(existingBooking.getCheckInDate()) ||
            !booking.getCheckOutDate().equals(existingBooking.getCheckOutDate())) {
            
            if (!isRoomAvailable(booking.getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
                throw new IllegalArgumentException("房间在指定时间段不可用");
            }
        }
        
        return bookingDAO.update(booking);
    }
    
    @Override
    public Booking getBookingById(Integer bookingId) {
        if (bookingId == null || bookingId <= 0) {
            return null;
        }
        return bookingDAO.findById(bookingId);
    }
    
    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }
    
    @Override
    public List<Booking> getBookingsByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("客户ID无效");
        }
        return bookingDAO.findByCustomerId(customerId);
    }
    
    @Override
    public List<Booking> getBookingsByRoom(Integer roomId) {
        if (roomId == null || roomId <= 0) {
            throw new IllegalArgumentException("房间ID无效");
        }
        return bookingDAO.findByRoomId(roomId);
    }
    
    @Override
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("预订状态不能为空");
        }
        return bookingDAO.findByStatus(status);
    }
    
    @Override
    public List<Booking> getBookingsByCheckInDate(Date checkInDate) {
        if (checkInDate == null) {
            throw new IllegalArgumentException("入住日期不能为空");
        }
        return bookingDAO.findByCheckInDate(checkInDate);
    }
    
    @Override
    public List<Booking> getBookingsByCheckOutDate(Date checkOutDate) {
        if (checkOutDate == null) {
            throw new IllegalArgumentException("退房日期不能为空");
        }
        return bookingDAO.findByCheckOutDate(checkOutDate);
    }
    
    @Override
    public List<Booking> getBookingsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("日期范围不能为空");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        return bookingDAO.findByDateRange(startDate, endDate);
    }
    
    @Override
    public boolean isRoomAvailable(Integer roomId, Date checkInDate, Date checkOutDate) {
        if (roomId == null || checkInDate == null || checkOutDate == null) {
            return false;
        }
        
        // 检查房间状态
        Room room = roomDAO.findById(roomId);
        if (room == null || !room.canBeBooked()) {
            return false;
        }
        
        // 检查是否有冲突的预订
        List<Booking> conflictingBookings = bookingDAO.findConflictingBookings(roomId, checkInDate, checkOutDate);
        return conflictingBookings.isEmpty();
    }
    
    @Override
    public List<Booking> getConflictingBookings(Integer roomId, Date checkInDate, Date checkOutDate) {
        if (roomId == null || checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        return bookingDAO.findConflictingBookings(roomId, checkInDate, checkOutDate);
    }
    
    @Override
    public List<Booking> getBookingsByCreator(Integer createdBy) {
        if (createdBy == null || createdBy <= 0) {
            throw new IllegalArgumentException("创建人ID无效");
        }
        return bookingDAO.findByCreatedBy(createdBy);
    }
    
    @Override
    public List<Booking> getTodayCheckIns() {
        return bookingDAO.findTodayCheckIns();
    }
    
    @Override
    public List<Booking> getTodayCheckOuts() {
        return bookingDAO.findTodayCheckOuts();
    }
    
    @Override
    public boolean confirmBooking(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalStateException("只有待确认的预订才能确认");
        }
        
        booking.confirm();
        return bookingDAO.update(booking);
    }
    
    @Override
    public boolean checkIn(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (!booking.canCheckIn()) {
            throw new IllegalStateException("预订状态不允许入住或入住日期未到");
        }
        
        // 更新预订状态
        booking.checkIn();
        boolean bookingUpdated = bookingDAO.update(booking);
        
        // 更新房间状态
        if (bookingUpdated) {
            Room room = roomDAO.findById(booking.getRoomId());
            if (room != null) {
                room.checkIn();
                roomDAO.update(room);
            }
        }
        
        return bookingUpdated;
    }
    
    @Override
    public boolean checkOut(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (!booking.canCheckOut()) {
            throw new IllegalStateException("预订状态不允许退房");
        }
        
        // 更新预订状态
        booking.checkOut();
        boolean bookingUpdated = bookingDAO.update(booking);
        
        // 更新房间状态
        if (bookingUpdated) {
            Room room = roomDAO.findById(booking.getRoomId());
            if (room != null) {
                room.checkOut();
                roomDAO.update(room);
            }
        }
        
        return bookingUpdated;
    }
    
    @Override
    public boolean cancelBooking(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (!booking.canBeCancelled()) {
            throw new IllegalStateException("预订状态不允许取消");
        }
        
        booking.cancel();
        return bookingDAO.update(booking);
    }
    
    @Override
    public boolean canCancelBooking(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        return booking != null && booking.canBeCancelled();
    }
    
    @Override
    public boolean canCheckIn(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        return booking != null && booking.canCheckIn();
    }
    
    @Override
    public boolean canCheckOut(Integer bookingId) {
        Booking booking = getBookingById(bookingId);
        return booking != null && booking.canCheckOut();
    }
    
    @Override
    public long getTotalBookingCount() {
        return bookingDAO.count();
    }
    
    @Override
    public long getBookingCountByStatus(Booking.BookingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("预订状态不能为空");
        }
        return bookingDAO.countByStatus(status);
    }
    
    @Override
    public long getBookingCountByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("客户ID无效");
        }
        return bookingDAO.countByCustomerId(customerId);
    }
    
    @Override
    public long getBookingCountByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("日期范围不能为空");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        return bookingDAO.countByDateRange(startDate, endDate);
    }
}