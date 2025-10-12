package com.hotel.service.impl;

import com.hotel.dao.DAOFactory;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;
import com.hotel.service.RoomService;
import com.hotel.util.Utils;

import java.util.List;

/**
 * 房间服务实现类
 */
public class RoomServiceImpl implements RoomService {
    
    private final RoomDAO roomDAO;
    
    public RoomServiceImpl() {
        this.roomDAO = DAOFactory.createRoomDAO();
    }
    
    @Override
    public Integer addRoom(Room room) {
        // 参数验证
        if (room == null) {
            throw new IllegalArgumentException("房间对象不能为空");
        }
        
        if (Utils.isEmpty(room.getRoomNumber())) {
            throw new IllegalArgumentException("房间号不能为空");
        }
        
        if (room.getTypeId() == null) {
            throw new IllegalArgumentException("房间类型不能为空");
        }
        
        if (room.getFloor() == null || room.getFloor() <= 0) {
            throw new IllegalArgumentException("楼层必须为正数");
        }
        
        // 检查房间号是否已存在
        if (roomDAO.existsByRoomNumber(room.getRoomNumber())) {
            throw new IllegalArgumentException("房间号已存在: " + room.getRoomNumber());
        }
        
        // 设置默认状态
        if (room.getStatus() == null) {
            room.setStatus(Room.RoomStatus.AVAILABLE);
        }
        
        return roomDAO.insert(room);
    }
    
    @Override
    public boolean deleteRoom(Integer roomId) {
        if (roomId == null || roomId <= 0) {
            throw new IllegalArgumentException("房间ID无效");
        }
        
        // 检查房间是否存在
        Room room = roomDAO.findById(roomId);
        if (room == null) {
            return false;
        }
        
        // 检查房间是否可以删除（只有可用状态的房间才能删除）
        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new IllegalStateException("只有可用状态的房间才能删除");
        }
        
        return roomDAO.delete(roomId);
    }
    
    @Override
    public boolean updateRoom(Room room) {
        if (room == null || room.getRoomId() == null) {
            throw new IllegalArgumentException("房间ID不能为空");
        }
        
        // 检查房间是否存在
        Room existingRoom = roomDAO.findById(room.getRoomId());
        if (existingRoom == null) {
            return false;
        }
        
        // 如果更新了房间号，检查新房间号是否已被其他房间使用
        if (!Utils.isEmpty(room.getRoomNumber()) && 
            !room.getRoomNumber().equals(existingRoom.getRoomNumber())) {
            if (roomDAO.existsByRoomNumber(room.getRoomNumber())) {
                throw new IllegalArgumentException("房间号已存在: " + room.getRoomNumber());
            }
        }
        
        return roomDAO.update(room);
    }
    
    @Override
    public Room getRoomById(Integer roomId) {
        if (roomId == null || roomId <= 0) {
            return null;
        }
        return roomDAO.findById(roomId);
    }
    
    @Override
    public Room getRoomByNumber(String roomNumber) {
        if (Utils.isEmpty(roomNumber)) {
            return null;
        }
        return roomDAO.findByRoomNumber(roomNumber);
    }
    
    @Override
    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }
    
    @Override
    public List<Room> getRoomsByStatus(Room.RoomStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("房间状态不能为空");
        }
        return roomDAO.findByStatus(status);
    }
    
    @Override
    public List<Room> getRoomsByType(Integer typeId) {
        if (typeId == null || typeId <= 0) {
            throw new IllegalArgumentException("房间类型ID无效");
        }
        return roomDAO.findByTypeId(typeId);
    }
    
    @Override
    public List<Room> getRoomsByFloor(Integer floor) {
        if (floor == null || floor <= 0) {
            throw new IllegalArgumentException("楼层必须为正数");
        }
        return roomDAO.findByFloor(floor);
    }
    
    @Override
    public List<Room> getAvailableRooms() {
        return roomDAO.findAvailableRooms();
    }
    
    @Override
    public List<Room> getAvailableRoomsByType(Integer typeId) {
        if (typeId == null || typeId <= 0) {
            throw new IllegalArgumentException("房间类型ID无效");
        }
        return roomDAO.findAvailableRoomsByType(typeId);
    }
    
    @Override
    public boolean isRoomNumberExists(String roomNumber) {
        if (Utils.isEmpty(roomNumber)) {
            return false;
        }
        return roomDAO.existsByRoomNumber(roomNumber);
    }
    
    @Override
    public boolean isRoomAvailable(Integer roomId) {
        Room room = getRoomById(roomId);
        return room != null && room.isAvailable();
    }
    
    @Override
    public boolean checkInRoom(Integer roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        if (!room.canBeBooked()) {
            throw new IllegalStateException("房间状态不允许入住");
        }
        
        room.checkIn();
        return roomDAO.update(room);
    }
    
    @Override
    public boolean checkOutRoom(Integer roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        if (!room.isOccupied()) {
            throw new IllegalStateException("房间不是已入住状态");
        }
        
        room.checkOut();
        return roomDAO.update(room);
    }
    
    @Override
    public boolean finishRoomCleaning(Integer roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        if (!room.needsCleaning()) {
            throw new IllegalStateException("房间不是清洁中状态");
        }
        
        room.finishCleaning();
        return roomDAO.update(room);
    }
    
    @Override
    public boolean startRoomMaintenance(Integer roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        if (room.isOccupied()) {
            throw new IllegalStateException("已入住的房间不能开始维护");
        }
        
        room.startMaintenance();
        return roomDAO.update(room);
    }
    
    @Override
    public boolean finishRoomMaintenance(Integer roomId) {
        Room room = getRoomById(roomId);
        if (room == null) {
            return false;
        }
        
        if (!room.isUnderMaintenance()) {
            throw new IllegalStateException("房间不是维护中状态");
        }
        
        room.finishMaintenance();
        return roomDAO.update(room);
    }
    
    @Override
    public long getTotalRoomCount() {
        return roomDAO.count();
    }
    
    @Override
    public long getRoomCountByStatus(Room.RoomStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("房间状态不能为空");
        }
        return roomDAO.countByStatus(status);
    }
    
    @Override
    public long getRoomCountByType(Integer typeId) {
        if (typeId == null || typeId <= 0) {
            throw new IllegalArgumentException("房间类型ID无效");
        }
        return roomDAO.countByTypeId(typeId);
    }
}