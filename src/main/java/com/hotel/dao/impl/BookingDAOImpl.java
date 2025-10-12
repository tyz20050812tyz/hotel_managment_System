package com.hotel.dao.impl;

import com.hotel.dao.BookingDAO;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 预订数据访问实现类
 */
public class BookingDAOImpl implements BookingDAO {
    private static final Logger logger = LogManager.getLogger(BookingDAOImpl.class);
    
    // SQL语句常量
    private static final String INSERT_SQL = 
        "INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date, guests_count, total_price, status, special_requests, booking_time, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String DELETE_SQL = 
        "DELETE FROM bookings WHERE booking_id = ?";
    
    private static final String UPDATE_SQL = 
        "UPDATE bookings SET customer_id = ?, room_id = ?, check_in_date = ?, check_out_date = ?, guests_count = ?, total_price = ?, status = ?, special_requests = ?, created_by = ? WHERE booking_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.booking_id = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "ORDER BY b.booking_time DESC";
    
    private static final String SELECT_BY_CUSTOMER_ID_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.customer_id = ? ORDER BY b.booking_time DESC";
    
    private static final String SELECT_BY_ROOM_ID_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.room_id = ? ORDER BY b.check_in_date";
    
    private static final String SELECT_BY_STATUS_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.status = ? ORDER BY b.booking_time DESC";
    
    private static final String SELECT_BY_CHECK_IN_DATE_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.check_in_date = ? ORDER BY b.booking_time";
    
    private static final String SELECT_BY_CHECK_OUT_DATE_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.check_out_date = ? ORDER BY b.booking_time";
    
    private static final String SELECT_BY_DATE_RANGE_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.check_in_date >= ? AND b.check_out_date <= ? ORDER BY b.check_in_date";
    
    private static final String SELECT_CONFLICTING_BOOKINGS_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by " +
        "FROM bookings b " +
        "WHERE b.room_id = ? AND b.status NOT IN ('CANCELLED', 'CHECKED_OUT') AND " +
        "NOT (b.check_out_date <= ? OR b.check_in_date >= ?) " +
        "ORDER BY b.check_in_date";
    
    private static final String SELECT_BY_CREATED_BY_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.created_by = ? ORDER BY b.booking_time DESC";
    
    private static final String SELECT_TODAY_CHECK_INS_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.check_in_date = CURDATE() ORDER BY b.booking_time";
    
    private static final String SELECT_TODAY_CHECK_OUTS_SQL = 
        "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, b.guests_count, b.total_price, b.status, b.special_requests, b.booking_time, b.created_by, " +
        "c.name as customer_name, c.id_card, c.phone as customer_phone, c.email as customer_email, c.address, c.vip_level, " +
        "r.room_number, r.type_id, r.floor, r.status as room_status, " +
        "u.username, u.real_name " +
        "FROM bookings b " +
        "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
        "LEFT JOIN rooms r ON b.room_id = r.room_id " +
        "LEFT JOIN users u ON b.created_by = u.user_id " +
        "WHERE b.check_out_date = CURDATE() ORDER BY b.booking_time";
    
    private static final String COUNT_SQL = 
        "SELECT COUNT(*) FROM bookings";
    
    private static final String COUNT_BY_STATUS_SQL = 
        "SELECT COUNT(*) FROM bookings WHERE status = ?";
    
    private static final String COUNT_BY_CUSTOMER_ID_SQL = 
        "SELECT COUNT(*) FROM bookings WHERE customer_id = ?";
    
    private static final String COUNT_BY_DATE_RANGE_SQL = 
        "SELECT COUNT(*) FROM bookings WHERE check_in_date >= ? AND check_out_date <= ?";

    @Override
    public Integer insert(Booking booking) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getRoomId());
            stmt.setDate(3, booking.getCheckInDate());
            stmt.setDate(4, booking.getCheckOutDate());
            stmt.setInt(5, booking.getGuestsCount());
            stmt.setBigDecimal(6, booking.getTotalPrice());
            stmt.setString(7, booking.getStatus().name());
            stmt.setString(8, booking.getSpecialRequests());
            stmt.setTimestamp(9, booking.getBookingTime() != null ? booking.getBookingTime() : new Timestamp(System.currentTimeMillis()));
            stmt.setInt(10, booking.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer bookingId = rs.getInt(1);
                    logger.info("Booking inserted successfully with ID: {}", bookingId);
                    return bookingId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error inserting booking for customer ID: " + booking.getCustomerId(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public boolean delete(Integer bookingId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, bookingId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Booking deleted successfully with ID: {}", bookingId);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error deleting booking with ID: " + bookingId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean update(Booking booking) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getRoomId());
            stmt.setDate(3, booking.getCheckInDate());
            stmt.setDate(4, booking.getCheckOutDate());
            stmt.setInt(5, booking.getGuestsCount());
            stmt.setBigDecimal(6, booking.getTotalPrice());
            stmt.setString(7, booking.getStatus().name());
            stmt.setString(8, booking.getSpecialRequests());
            stmt.setInt(9, booking.getCreatedBy());
            stmt.setInt(10, booking.getBookingId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Booking updated successfully with ID: {}", booking.getBookingId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error updating booking with ID: " + booking.getBookingId(), e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public Booking findById(Integer bookingId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, bookingId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding booking by ID: " + bookingId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<Booking> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all bookings", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByCustomerId(Integer customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_CUSTOMER_ID_SQL);
            stmt.setInt(1, customerId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by customer ID: " + customerId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByRoomId(Integer roomId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ROOM_ID_SQL);
            stmt.setInt(1, roomId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by room ID: " + roomId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByStatus(Booking.BookingStatus status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_STATUS_SQL);
            stmt.setString(1, status.name());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by status: " + status, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByCheckInDate(Date checkInDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_CHECK_IN_DATE_SQL);
            stmt.setDate(1, checkInDate);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by check-in date: " + checkInDate, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByCheckOutDate(Date checkOutDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_CHECK_OUT_DATE_SQL);
            stmt.setDate(1, checkOutDate);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by check-out date: " + checkOutDate, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByDateRange(Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_DATE_RANGE_SQL);
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by date range: " + startDate + " - " + endDate, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findConflictingBookings(Integer roomId, Date checkInDate, Date checkOutDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_CONFLICTING_BOOKINGS_SQL);
            stmt.setInt(1, roomId);
            stmt.setDate(2, checkInDate);
            stmt.setDate(3, checkOutDate);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBookingSimple(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding conflicting bookings for room ID: " + roomId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findByCreatedBy(Integer createdBy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_CREATED_BY_SQL);
            stmt.setInt(1, createdBy);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding bookings by created by: " + createdBy, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findTodayCheckIns() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_TODAY_CHECK_INS_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding today's check-ins", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public List<Booking> findTodayCheckOuts() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_TODAY_CHECK_OUTS_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding today's check-outs", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return bookings;
    }

    @Override
    public long count() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_SQL);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting bookings", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByStatus(Booking.BookingStatus status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_STATUS_SQL);
            stmt.setString(1, status.name());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting bookings by status: " + status, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByCustomerId(Integer customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_CUSTOMER_ID_SQL);
            stmt.setInt(1, customerId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting bookings by customer ID: " + customerId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByDateRange(Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_DATE_RANGE_SQL);
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting bookings by date range: " + startDate + " - " + endDate, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    /**
     * 将ResultSet映射为Booking对象（包含关联信息）
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckInDate(rs.getDate("check_in_date"));
        booking.setCheckOutDate(rs.getDate("check_out_date"));
        booking.setGuestsCount(rs.getInt("guests_count"));
        booking.setTotalPrice(rs.getBigDecimal("total_price"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        booking.setSpecialRequests(rs.getString("special_requests"));
        booking.setBookingTime(rs.getTimestamp("booking_time"));
        booking.setCreatedBy(rs.getInt("created_by"));
        
        // 如果关联查询了客户信息
        if (rs.getString("customer_name") != null) {
            Customer customer = new Customer();
            customer.setCustomerId(booking.getCustomerId());
            customer.setName(rs.getString("customer_name"));
            customer.setIdCard(rs.getString("id_card"));
            customer.setPhone(rs.getString("customer_phone"));
            customer.setEmail(rs.getString("customer_email"));
            customer.setAddress(rs.getString("address"));
            customer.setVipLevel(rs.getInt("vip_level"));
            booking.setCustomer(customer);
        }
        
        // 如果关联查询了房间信息
        if (rs.getString("room_number") != null) {
            Room room = new Room();
            room.setRoomId(booking.getRoomId());
            room.setRoomNumber(rs.getString("room_number"));
            room.setTypeId(rs.getInt("type_id"));
            room.setFloor(rs.getInt("floor"));
            room.setStatus(Room.RoomStatus.valueOf(rs.getString("room_status")));
            booking.setRoom(room);
        }
        
        // 如果关联查询了创建人信息
        if (rs.getString("username") != null) {
            User creator = new User();
            creator.setUserId(booking.getCreatedBy());
            creator.setUsername(rs.getString("username"));
            creator.setRealName(rs.getString("real_name"));
            booking.setCreator(creator);
        }
        
        return booking;
    }

    /**
     * 将ResultSet映射为简单Booking对象（不包含关联信息）
     */
    private Booking mapResultSetToBookingSimple(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckInDate(rs.getDate("check_in_date"));
        booking.setCheckOutDate(rs.getDate("check_out_date"));
        booking.setGuestsCount(rs.getInt("guests_count"));
        booking.setTotalPrice(rs.getBigDecimal("total_price"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        booking.setSpecialRequests(rs.getString("special_requests"));
        booking.setBookingTime(rs.getTimestamp("booking_time"));
        booking.setCreatedBy(rs.getInt("created_by"));
        return booking;
    }

    /**
     * 关闭数据库资源
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.error("Error closing database resources", e);
        }
    }
}