package com.hotel.dao.impl;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;
import com.hotel.model.RoomType;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 房间数据访问实现类
 */
public class RoomDAOImpl implements RoomDAO {
    private static final Logger logger = LogManager.getLogger(RoomDAOImpl.class);
    
    // SQL语句常量
    private static final String INSERT_SQL = 
        "INSERT INTO rooms (room_number, type_id, floor, status, last_cleaned) VALUES (?, ?, ?, ?, ?)";
    
    private static final String DELETE_SQL = 
        "DELETE FROM rooms WHERE room_id = ?";
    
    private static final String UPDATE_SQL = 
        "UPDATE rooms SET room_number = ?, type_id = ?, floor = ?, status = ?, last_cleaned = ? WHERE room_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.room_id = ?";
    
    private static final String SELECT_BY_ROOM_NUMBER_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.room_number = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id ORDER BY r.floor, r.room_number";
    
    private static final String SELECT_BY_STATUS_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.status = ? ORDER BY r.floor, r.room_number";
    
    private static final String SELECT_BY_TYPE_ID_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.type_id = ? ORDER BY r.floor, r.room_number";
    
    private static final String SELECT_BY_FLOOR_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.floor = ? ORDER BY r.room_number";
    
    private static final String SELECT_AVAILABLE_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.status = 'AVAILABLE' ORDER BY r.floor, r.room_number";
    
    private static final String SELECT_AVAILABLE_BY_TYPE_SQL = 
        "SELECT r.room_id, r.room_number, r.type_id, r.floor, r.status, r.last_cleaned, " +
        "rt.type_name, rt.price, rt.bed_count, rt.max_guests, rt.description, rt.amenities " +
        "FROM rooms r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.status = 'AVAILABLE' AND r.type_id = ? ORDER BY r.floor, r.room_number";
    
    private static final String EXISTS_BY_ROOM_NUMBER_SQL = 
        "SELECT COUNT(*) FROM rooms WHERE room_number = ?";
    
    private static final String COUNT_SQL = 
        "SELECT COUNT(*) FROM rooms";
    
    private static final String COUNT_BY_STATUS_SQL = 
        "SELECT COUNT(*) FROM rooms WHERE status = ?";
    
    private static final String COUNT_BY_TYPE_ID_SQL = 
        "SELECT COUNT(*) FROM rooms WHERE type_id = ?";

    @Override
    public Integer insert(Room room) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getTypeId());
            stmt.setInt(3, room.getFloor());
            stmt.setString(4, room.getStatus().name());
            stmt.setTimestamp(5, room.getLastCleaned());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer roomId = rs.getInt(1);
                    logger.info("Room inserted successfully with ID: {}", roomId);
                    return roomId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error inserting room: " + room.getRoomNumber(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public boolean delete(Integer roomId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, roomId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Room deleted successfully with ID: {}", roomId);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error deleting room with ID: " + roomId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean update(Room room) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getTypeId());
            stmt.setInt(3, room.getFloor());
            stmt.setString(4, room.getStatus().name());
            stmt.setTimestamp(5, room.getLastCleaned());
            stmt.setInt(6, room.getRoomId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Room updated successfully with ID: {}", room.getRoomId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error updating room with ID: " + room.getRoomId(), e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public Room findById(Integer roomId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, roomId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room by ID: " + roomId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public Room findByRoomNumber(String roomNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ROOM_NUMBER_SQL);
            stmt.setString(1, roomNumber);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room by room number: " + roomNumber, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<Room> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all rooms", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public List<Room> findByStatus(Room.RoomStatus status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_STATUS_SQL);
            stmt.setString(1, status.name());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding rooms by status: " + status, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public List<Room> findByTypeId(Integer typeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_TYPE_ID_SQL);
            stmt.setInt(1, typeId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding rooms by type ID: " + typeId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public List<Room> findByFloor(Integer floor) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_FLOOR_SQL);
            stmt.setInt(1, floor);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding rooms by floor: " + floor, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public List<Room> findAvailableRooms() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_AVAILABLE_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding available rooms", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public List<Room> findAvailableRoomsByType(Integer typeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_AVAILABLE_BY_TYPE_SQL);
            stmt.setInt(1, typeId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding available rooms by type ID: " + typeId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return rooms;
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(EXISTS_BY_ROOM_NUMBER_SQL);
            stmt.setString(1, roomNumber);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if room number exists: " + roomNumber, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return false;
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
            logger.error("Error counting rooms", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByStatus(Room.RoomStatus status) {
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
            logger.error("Error counting rooms by status: " + status, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public long countByTypeId(Integer typeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_BY_TYPE_ID_SQL);
            stmt.setInt(1, typeId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting rooms by type ID: " + typeId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    /**
     * 将ResultSet映射为Room对象
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setTypeId(rs.getInt("type_id"));
        room.setFloor(rs.getInt("floor"));
        room.setStatus(Room.RoomStatus.valueOf(rs.getString("status")));
        room.setLastCleaned(rs.getTimestamp("last_cleaned"));
        
        // 如果关联查询了房间类型信息
        if (rs.getString("type_name") != null) {
            RoomType roomType = new RoomType();
            roomType.setTypeId(room.getTypeId());
            roomType.setTypeName(rs.getString("type_name"));
            roomType.setPrice(rs.getBigDecimal("price"));
            roomType.setBedCount(rs.getInt("bed_count"));
            roomType.setMaxGuests(rs.getInt("max_guests"));
            roomType.setDescription(rs.getString("description"));
            roomType.setAmenities(rs.getString("amenities"));
            room.setRoomType(roomType);
        }
        
        return room;
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