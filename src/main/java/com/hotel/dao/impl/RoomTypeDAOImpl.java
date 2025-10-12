package com.hotel.dao.impl;

import com.hotel.dao.RoomTypeDAO;
import com.hotel.model.RoomType;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 房间类型数据访问实现类
 */
public class RoomTypeDAOImpl implements RoomTypeDAO {
    private static final Logger logger = LogManager.getLogger(RoomTypeDAOImpl.class);
    
    // SQL语句常量
    private static final String INSERT_SQL = 
        "INSERT INTO room_types (type_name, price, bed_count, max_guests, description, amenities) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String DELETE_SQL = 
        "DELETE FROM room_types WHERE type_id = ?";
    
    private static final String UPDATE_SQL = 
        "UPDATE room_types SET type_name = ?, price = ?, bed_count = ?, max_guests = ?, description = ?, amenities = ? WHERE type_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT type_id, type_name, price, bed_count, max_guests, description, amenities FROM room_types WHERE type_id = ?";
    
    private static final String SELECT_BY_TYPE_NAME_SQL = 
        "SELECT type_id, type_name, price, bed_count, max_guests, description, amenities FROM room_types WHERE type_name = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT type_id, type_name, price, bed_count, max_guests, description, amenities FROM room_types ORDER BY price";
    
    private static final String SELECT_BY_PRICE_RANGE_SQL = 
        "SELECT type_id, type_name, price, bed_count, max_guests, description, amenities FROM room_types WHERE price BETWEEN ? AND ? ORDER BY price";
    
    private static final String SELECT_BY_MAX_GUESTS_SQL = 
        "SELECT type_id, type_name, price, bed_count, max_guests, description, amenities FROM room_types WHERE max_guests >= ? ORDER BY max_guests";
    
    private static final String EXISTS_BY_TYPE_NAME_SQL = 
        "SELECT COUNT(*) FROM room_types WHERE type_name = ?";
    
    private static final String COUNT_SQL = 
        "SELECT COUNT(*) FROM room_types";

    @Override
    public Integer insert(RoomType roomType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getPrice());
            stmt.setInt(3, roomType.getBedCount());
            stmt.setInt(4, roomType.getMaxGuests());
            stmt.setString(5, roomType.getDescription());
            stmt.setString(6, roomType.getAmenities());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer typeId = rs.getInt(1);
                    logger.info("Room type inserted successfully with ID: {}", typeId);
                    return typeId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error inserting room type: " + roomType.getTypeName(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public boolean delete(Integer typeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, typeId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Room type deleted successfully with ID: {}", typeId);
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error deleting room type with ID: " + typeId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean update(RoomType roomType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getPrice());
            stmt.setInt(3, roomType.getBedCount());
            stmt.setInt(4, roomType.getMaxGuests());
            stmt.setString(5, roomType.getDescription());
            stmt.setString(6, roomType.getAmenities());
            stmt.setInt(7, roomType.getTypeId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Room type updated successfully with ID: {}", roomType.getTypeId());
            }
            
            return success;
            
        } catch (SQLException e) {
            logger.error("Error updating room type with ID: " + roomType.getTypeId(), e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public RoomType findById(Integer typeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, typeId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoomType(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room type by ID: " + typeId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public RoomType findByTypeName(String typeName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_TYPE_NAME_SQL);
            stmt.setString(1, typeName);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoomType(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room type by type name: " + typeName, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<RoomType> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RoomType> roomTypes = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                roomTypes.add(mapResultSetToRoomType(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all room types", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return roomTypes;
    }

    @Override
    public List<RoomType> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RoomType> roomTypes = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_PRICE_RANGE_SQL);
            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                roomTypes.add(mapResultSetToRoomType(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room types by price range: " + minPrice + " - " + maxPrice, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return roomTypes;
    }

    @Override
    public List<RoomType> findByMaxGuests(Integer maxGuests) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RoomType> roomTypes = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_MAX_GUESTS_SQL);
            stmt.setInt(1, maxGuests);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                roomTypes.add(mapResultSetToRoomType(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding room types by max guests: " + maxGuests, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return roomTypes;
    }

    @Override
    public boolean existsByTypeName(String typeName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(EXISTS_BY_TYPE_NAME_SQL);
            stmt.setString(1, typeName);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if room type name exists: " + typeName, e);
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
            logger.error("Error counting room types", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    /**
     * 将ResultSet映射为RoomType对象
     */
    private RoomType mapResultSetToRoomType(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setTypeId(rs.getInt("type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setPrice(rs.getBigDecimal("price"));
        roomType.setBedCount(rs.getInt("bed_count"));
        roomType.setMaxGuests(rs.getInt("max_guests"));
        roomType.setDescription(rs.getString("description"));
        roomType.setAmenities(rs.getString("amenities"));
        return roomType;
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