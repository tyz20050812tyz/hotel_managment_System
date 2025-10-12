package com.hotel.model;

import java.sql.Timestamp;

/**
 * 房间实体类
 * 对应数据库表：rooms
 */
public class Room {
    private Integer roomId;          // 房间ID
    private String roomNumber;       // 房间号
    private Integer typeId;          // 房间类型ID
    private Integer floor;           // 楼层
    private RoomStatus status;       // 房间状态
    private Timestamp lastCleaned;   // 最后清洁时间
    
    // 关联的房间类型对象（用于连接查询）
    private RoomType roomType;

    /**
     * 房间状态枚举
     */
    public enum RoomStatus {
        AVAILABLE("可用"),
        OCCUPIED("已入住"),
        MAINTENANCE("维护中"),
        CLEANING("清洁中"),
        OUT_OF_ORDER("故障");

        private final String description;

        RoomStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 默认构造函数
    public Room() {}

    // 构造函数
    public Room(String roomNumber, Integer typeId, Integer floor) {
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.floor = floor;
        this.status = RoomStatus.AVAILABLE; // 默认状态为可用
    }

    // 构造函数
    public Room(String roomNumber, Integer typeId, Integer floor, RoomStatus status) {
        this(roomNumber, typeId, floor);
        this.status = status;
    }

    // Getter 和 Setter 方法
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Timestamp getLastCleaned() {
        return lastCleaned;
    }

    public void setLastCleaned(Timestamp lastCleaned) {
        this.lastCleaned = lastCleaned;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    // 业务方法
    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == RoomStatus.OCCUPIED;
    }

    public boolean needsCleaning() {
        return status == RoomStatus.CLEANING;
    }

    public boolean isUnderMaintenance() {
        return status == RoomStatus.MAINTENANCE;
    }

    public boolean isOutOfOrder() {
        return status == RoomStatus.OUT_OF_ORDER;
    }

    /**
     * 检查房间是否可以预订
     * @return true如果房间状态为可用
     */
    public boolean canBeBooked() {
        return isAvailable();
    }

    /**
     * 设置房间为已入住状态
     */
    public void checkIn() {
        this.status = RoomStatus.OCCUPIED;
    }

    /**
     * 设置房间为清洁中状态（退房后）
     */
    public void checkOut() {
        this.status = RoomStatus.CLEANING;
    }

    /**
     * 完成清洁，设置为可用状态
     */
    public void finishCleaning() {
        this.status = RoomStatus.AVAILABLE;
        this.lastCleaned = new Timestamp(System.currentTimeMillis());
    }

    /**
     * 设置房间为维护状态
     */
    public void startMaintenance() {
        this.status = RoomStatus.MAINTENANCE;
    }

    /**
     * 完成维护，设置为可用状态
     */
    public void finishMaintenance() {
        this.status = RoomStatus.AVAILABLE;
    }

    /**
     * 设置房间为故障状态
     */
    public void setOutOfOrder() {
        this.status = RoomStatus.OUT_OF_ORDER;
    }

    /**
     * 修复故障，设置为可用状态
     */
    public void fixOutOfOrder() {
        this.status = RoomStatus.AVAILABLE;
    }

    /**
     * 获取房间的完整显示名称
     * @return 例如："101号房间 (标准单人间)"
     */
    public String getDisplayName() {
        if (roomType != null) {
            return roomNumber + "号房间 (" + roomType.getTypeName() + ")";
        }
        return roomNumber + "号房间";
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", typeId=" + typeId +
                ", floor=" + floor +
                ", status=" + status +
                ", lastCleaned=" + lastCleaned +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return roomId != null && roomId.equals(room.roomId);
    }

    @Override
    public int hashCode() {
        return roomId != null ? roomId.hashCode() : 0;
    }
}