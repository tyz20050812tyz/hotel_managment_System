package com.hotel.dao;

import com.hotel.model.RoomType;
import java.math.BigDecimal;
import java.util.List;

/**
 * 房间类型数据访问接口
 */
public interface RoomTypeDAO {
    
    /**
     * 插入新房间类型
     * @param roomType 房间类型对象
     * @return 插入成功返回生成的类型ID，失败返回null
     */
    Integer insert(RoomType roomType);
    
    /**
     * 根据类型ID删除房间类型
     * @param typeId 类型ID
     * @return 删除成功返回true
     */
    boolean delete(Integer typeId);
    
    /**
     * 更新房间类型信息
     * @param roomType 房间类型对象
     * @return 更新成功返回true
     */
    boolean update(RoomType roomType);
    
    /**
     * 根据类型ID查找房间类型
     * @param typeId 类型ID
     * @return 房间类型对象，不存在返回null
     */
    RoomType findById(Integer typeId);
    
    /**
     * 根据类型名称查找房间类型
     * @param typeName 类型名称
     * @return 房间类型对象，不存在返回null
     */
    RoomType findByTypeName(String typeName);
    
    /**
     * 查找所有房间类型
     * @return 房间类型列表
     */
    List<RoomType> findAll();
    
    /**
     * 根据价格范围查找房间类型
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 房间类型列表
     */
    List<RoomType> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 根据最大入住人数查找房间类型
     * @param maxGuests 最大入住人数
     * @return 房间类型列表
     */
    List<RoomType> findByMaxGuests(Integer maxGuests);
    
    /**
     * 检查类型名称是否已存在
     * @param typeName 类型名称
     * @return 存在返回true
     */
    boolean existsByTypeName(String typeName);
    
    /**
     * 统计房间类型总数
     * @return 房间类型总数
     */
    long count();
}