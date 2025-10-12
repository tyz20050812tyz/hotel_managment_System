package com.hotel.model;

import java.math.BigDecimal;

/**
 * 房间类型实体类
 * 对应数据库表：room_types
 */
public class RoomType {
    private Integer typeId;          // 类型ID
    private String typeName;         // 房间类型名称
    private BigDecimal price;        // 价格/晚
    private Integer bedCount;        // 床位数
    private Integer maxGuests;       // 最大入住人数
    private String description;      // 房间描述
    private String amenities;        // 房间设施

    // 默认构造函数
    public RoomType() {}

    // 构造函数
    public RoomType(String typeName, BigDecimal price, Integer bedCount, Integer maxGuests) {
        this.typeName = typeName;
        this.price = price;
        this.bedCount = bedCount;
        this.maxGuests = maxGuests;
    }

    // 构造函数
    public RoomType(String typeName, BigDecimal price, Integer bedCount, Integer maxGuests, 
                   String description, String amenities) {
        this(typeName, price, bedCount, maxGuests);
        this.description = description;
        this.amenities = amenities;
    }

    // Getter 和 Setter 方法
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    // 业务方法
    /**
     * 获取设施列表
     * @return 设施数组
     */
    public String[] getAmenitiesList() {
        if (amenities == null || amenities.trim().isEmpty()) {
            return new String[0];
        }
        return amenities.split(",");
    }

    /**
     * 设置设施列表
     * @param amenitiesList 设施数组
     */
    public void setAmenitiesList(String[] amenitiesList) {
        if (amenitiesList == null || amenitiesList.length == 0) {
            this.amenities = "";
        } else {
            this.amenities = String.join(",", amenitiesList);
        }
    }

    /**
     * 检查是否为豪华房型
     * @return true如果价格大于500元
     */
    public boolean isLuxury() {
        return price != null && price.compareTo(new BigDecimal("500")) > 0;
    }

    /**
     * 检查是否适合家庭入住
     * @return true如果最大入住人数大于等于3
     */
    public boolean isFamilyFriendly() {
        return maxGuests != null && maxGuests >= 3;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", price=" + price +
                ", bedCount=" + bedCount +
                ", maxGuests=" + maxGuests +
                ", description='" + description + '\'' +
                ", amenities='" + amenities + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoomType roomType = (RoomType) obj;
        return typeId != null && typeId.equals(roomType.typeId);
    }

    @Override
    public int hashCode() {
        return typeId != null ? typeId.hashCode() : 0;
    }
}