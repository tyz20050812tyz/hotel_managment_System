package com.hotel.model;

import java.sql.Timestamp;

/**
 * 客户实体类
 * 对应数据库表：customers
 */
public class Customer {
    private Integer customerId;      // 客户ID
    private String name;             // 客户姓名
    private String idCard;           // 身份证号
    private String phone;            // 联系电话
    private String email;            // 邮箱地址
    private String address;          // 联系地址
    private Integer vipLevel;        // VIP等级(0:普通,1-5:VIP等级)
    private Timestamp createTime;    // 注册时间

    // 默认构造函数
    public Customer() {}

    // 构造函数（不包含ID和创建时间）
    public Customer(String name, String idCard, String phone) {
        this.name = name;
        this.idCard = idCard;
        this.phone = phone;
        this.vipLevel = 0; // 默认为普通客户
    }

    // 构造函数
    public Customer(String name, String idCard, String phone, String email, String address) {
        this(name, idCard, phone);
        this.email = email;
        this.address = address;
    }

    // Getter 和 Setter 方法
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    // 业务方法
    public boolean isVip() {
        return vipLevel != null && vipLevel > 0;
    }

    public String getVipLevelDescription() {
        if (vipLevel == null || vipLevel == 0) {
            return "普通客户";
        } else if (vipLevel >= 1 && vipLevel <= 5) {
            return "VIP" + vipLevel + "级客户";
        }
        return "未知等级";
    }

    /**
     * 获取VIP折扣率
     * @return 折扣率（0.1-1.0）
     */
    public double getDiscountRate() {
        if (vipLevel == null || vipLevel == 0) {
            return 1.0; // 普通客户无折扣
        }
        // VIP等级越高，折扣越大
        return Math.max(0.7, 1.0 - (vipLevel * 0.05));
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", vipLevel=" + vipLevel +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return customerId != null && customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return customerId != null ? customerId.hashCode() : 0;
    }
}