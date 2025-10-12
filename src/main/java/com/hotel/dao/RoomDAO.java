package com.hotel.dao;

import com.hotel.model.Room;
import java.util.List;

/**
 * 房间数据访问接口
 */
public interface RoomDAO {
    
    /**
     * 插入新房间
     * @param room 房间对象
     * @return 插入成功返回生成的房间ID，失败返回null
     */
    Integer insert(Room room);
    
    /**
     * 根据房间ID删除房间
     * @param roomId 房间ID
     * @return 删除成功返回true
     */
    boolean delete(Integer roomId);
    
    /**
     * 更新房间信息
     * @param room 房间对象
     * @return 更新成功返回true
     */
    boolean update(Room room);
    
    /**
     * 根据房间ID查找房间
     * @param roomId 房间ID
     * @return 房间对象，不存在返回null
     */
    Room findById(Integer roomId);
    
    /**
     * 根据房间号查找房间
     * @param roomNumber 房间号
     * @return 房间对象，不存在返回null
     */
    Room findByRoomNumber(String roomNumber);
    
    /**
     * 查找所有房间
     * @return 房间列表
     */
    List<Room> findAll();
    
    /**
     * 根据房间状态查找房间
     * @param status 房间状态
     * @return 房间列表
     */
    List<Room> findByStatus(Room.RoomStatus status);
    
    /**
     * 根据房间类型ID查找房间
     * @param typeId 房间类型ID
     * @return 房间列表
     */
    List<Room> findByTypeId(Integer typeId);
    
    /**
     * 根据楼层查找房间
     * @param floor 楼层
     * @return 房间列表
     */
    List<Room> findByFloor(Integer floor);
    
    /**
     * 查找可用房间
     * @return 可用房间列表
     */
    List<Room> findAvailableRooms();
    
    /**
     * 根据房间类型查找可用房间
     * @param typeId 房间类型ID
     * @return 可用房间列表
     */
    List<Room> findAvailableRoomsByType(Integer typeId);
    
    /**
     * 检查房间号是否已存在
     * @param roomNumber 房间号
     * @return 存在返回true
     */
    boolean existsByRoomNumber(String roomNumber);
    
    /**
     * 统计房间总数
     * @return 房间总数
     */
    long count();
    
    /**
     * 根据状态统计房间数量
     * @param status 房间状态
     * @return 该状态的房间数量
     */
    long countByStatus(Room.RoomStatus status);
    
    /**
     * 根据房间类型统计房间数量
     * @param typeId 房间类型ID
     * @return 该类型的房间数量
     */
    long countByTypeId(Integer typeId);
}