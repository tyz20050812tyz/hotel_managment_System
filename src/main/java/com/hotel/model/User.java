package com.hotel.model;

import java.sql.Timestamp;

/**
 * 用户实体类
 * 对应数据库表：users
 */
public class User {
    private Integer userId;          // 用户ID
    private String username;         // 用户名
    private String password;         // 密码(加密存储)
    private String realName;         // 真实姓名
    private UserRole role;           // 用户角色
    private String phone;            // 联系电话
    private String email;            // 邮箱地址
    private Integer status;          // 状态(1:正常,0:禁用)
    private Timestamp createTime;    // 创建时间

    /**
     * 用户角色枚举
     */
    public enum UserRole {
        ADMIN("管理员"),
        STAFF("员工");

        private final String description;

        UserRole(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 默认构造函数
    public User() {}

    // 构造函数（不包含ID和创建时间）
    public User(String username, String password, String realName, UserRole role) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.role = role;
        this.status = 1; // 默认状态为正常
    }

    // Getter 和 Setter 方法
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    // 业务方法
    public boolean isActive() {
        return status != null && status == 1;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isStaff() {
        return role == UserRole.STAFF;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", role=" + role +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}