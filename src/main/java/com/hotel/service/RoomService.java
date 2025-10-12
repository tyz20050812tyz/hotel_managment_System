package com.hotel.service;

import com.hotel.model.Room;
import java.util.List;

/**
 * 房间服务接口
 * 提供房间相关的业务逻辑操作
 */
public interface RoomService {
    
    /**
     * 添加新房间
     * @param room 房间对象
     * @return 添加成功返回房间ID，失败返回null
     */
    Integer addRoom(Room room);
    
    /**
     * 删除房间
     * @param roomId 房间ID
     * @return 删除成功返回true
     */
    boolean deleteRoom(Integer roomId);
    
    /**
     * 更新房间信息
     * @param room 房间对象
     * @return 更新成功返回true
     */
    boolean updateRoom(Room room);
    
    /**
     * 根据房间ID查找房间
     * @param roomId 房间ID
     * @return 房间对象，不存在返回null
     */
    Room getRoomById(Integer roomId);
    
    /**
     * 根据房间号查找房间
     * @param roomNumber 房间号
     * @return 房间对象，不存在返回null
     */
    Room getRoomByNumber(String roomNumber);
    
    /**
     * 获取所有房间
     * @return 房间列表
     */
    List<Room> getAllRooms();
    
    /**
     * 根据房间状态获取房间列表
     * @param status 房间状态
     * @return 房间列表
     */
    List<Room> getRoomsByStatus(Room.RoomStatus status);
    
    /**
     * 根据房间类型获取房间列表
     * @param typeId 房间类型ID
     * @return 房间列表
     */
    List<Room> getRoomsByType(Integer typeId);
    
    /**
     * 根据楼层获取房间列表
     * @param floor 楼层
     * @return 房间列表
     */
    List<Room> getRoomsByFloor(Integer floor);
    
    /**
     * 获取可用房间列表
     * @return 可用房间列表
     */
    List<Room> getAvailableRooms();
    
    /**
     * 根据房间类型获取可用房间列表
     * @param typeId 房间类型ID
     * @return 可用房间列表
     */
    List<Room> getAvailableRoomsByType(Integer typeId);
    
    /**
     * 检查房间号是否已存在
     * @param roomNumber 房间号
     * @return 存在返回true
     */
    boolean isRoomNumberExists(String roomNumber);
    
    /**
     * 检查房间是否可用
     * @param roomId 房间ID
     * @return 可用返回true
     */
    boolean isRoomAvailable(Integer roomId);
    
    /**
     * 办理入住（更改房间状态为已入住）
     * @param roomId 房间ID
     * @return 操作成功返回true
     */
    boolean checkInRoom(Integer roomId);
    
    /**
     * 办理退房（更改房间状态为清洁中）
     * @param roomId 房间ID
     * @return 操作成功返回true
     */
    boolean checkOutRoom(Integer roomId);
    
    /**
     * 完成房间清洁（更改房间状态为可用）
     * @param roomId 房间ID
     * @return 操作成功返回true
     */
    boolean finishRoomCleaning(Integer roomId);
    
    /**
     * 开始房间维护（更改房间状态为维护中）
     * @param roomId 房间ID
     * @return 操作成功返回true
     */
    boolean startRoomMaintenance(Integer roomId);
    
    /**
     * 完成房间维护（更改房间状态为可用）
     * @param roomId 房间ID
     * @return 操作成功返回true
     */
    boolean finishRoomMaintenance(Integer roomId);
    
    /**
     * 统计房间总数
     * @return 房间总数
     */
    long getTotalRoomCount();
    
    /**
     * 根据状态统计房间数量
     * @param status 房间状态
     * @return 该状态的房间数量
     */
    long getRoomCountByStatus(Room.RoomStatus status);
    
    /**
     * 根据房间类型统计房间数量
     * @param typeId 房间类型ID
     * @return 该类型的房间数量
     */
    long getRoomCountByType(Integer typeId);
}