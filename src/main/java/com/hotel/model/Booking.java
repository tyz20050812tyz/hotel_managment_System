package com.hotel.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 预订实体类
 * 对应数据库表：bookings
 */
public class Booking {
    private Integer bookingId;          // 预订ID
    private Integer customerId;         // 客户ID
    private Integer roomId;             // 房间ID
    private Date checkInDate;           // 入住日期
    private Date checkOutDate;          // 退房日期
    private Integer guestsCount;        // 入住人数
    private BigDecimal totalPrice;      // 总价格
    private BookingStatus status;       // 预订状态
    private String specialRequests;     // 特殊要求
    private Timestamp bookingTime;      // 预订时间
    private Integer createdBy;          // 创建人(员工ID)

    // 关联对象（用于连接查询）
    private Customer customer;          // 客户信息
    private Room room;                  // 房间信息
    private User creator;               // 创建人信息

    /**
     * 预订状态枚举
     */
    public enum BookingStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        CHECKED_IN("已入住"),
        CHECKED_OUT("已退房"),
        CANCELLED("已取消");

        private final String description;

        BookingStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 默认构造函数
    public Booking() {}

    // 构造函数
    public Booking(Integer customerId, Integer roomId, Date checkInDate, Date checkOutDate, 
                  Integer guestsCount, BigDecimal totalPrice) {
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestsCount = guestsCount;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.PENDING; // 默认状态为待确认
    }

    // Getter 和 Setter 方法
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(Integer guestsCount) {
        this.guestsCount = guestsCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    // 业务方法
    /**
     * 计算住宿天数
     * @return 住宿天数
     */
    public long getStayDays() {
        if (checkInDate == null || checkOutDate == null) {
            return 0;
        }
        LocalDate checkIn = checkInDate.toLocalDate();
        LocalDate checkOut = checkOutDate.toLocalDate();
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /**
     * 检查预订是否可以取消
     * @return true如果状态为待确认或已确认
     */
    public boolean canBeCancelled() {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED;
    }

    /**
     * 检查预订是否可以办理入住
     * @return true如果状态为已确认且入住日期是今天或之前
     */
    public boolean canCheckIn() {
        if (status != BookingStatus.CONFIRMED) {
            return false;
        }
        LocalDate today = LocalDate.now();
        LocalDate checkIn = checkInDate.toLocalDate();
        return !checkIn.isAfter(today);
    }

    /**
     * 检查预订是否可以办理退房
     * @return true如果状态为已入住
     */
    public boolean canCheckOut() {
        return status == BookingStatus.CHECKED_IN;
    }

    /**
     * 检查预订是否已完成
     * @return true如果状态为已退房
     */
    public boolean isCompleted() {
        return status == BookingStatus.CHECKED_OUT;
    }

    /**
     * 检查预订是否已取消
     * @return true如果状态为已取消
     */
    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    /**
     * 检查预订是否正在进行中
     * @return true如果状态为已入住
     */
    public boolean isActive() {
        return status == BookingStatus.CHECKED_IN;
    }

    /**
     * 确认预订
     */
    public void confirm() {
        if (status == BookingStatus.PENDING) {
            this.status = BookingStatus.CONFIRMED;
        }
    }

    /**
     * 办理入住
     */
    public void checkIn() {
        if (status == BookingStatus.CONFIRMED) {
            this.status = BookingStatus.CHECKED_IN;
        }
    }

    /**
     * 办理退房
     */
    public void checkOut() {
        if (status == BookingStatus.CHECKED_IN) {
            this.status = BookingStatus.CHECKED_OUT;
        }
    }

    /**
     * 取消预订
     */
    public void cancel() {
        if (canBeCancelled()) {
            this.status = BookingStatus.CANCELLED;
        }
    }

    /**
     * 获取预订的显示信息
     * @return 预订显示信息
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("预订编号: ").append(bookingId);
        if (customer != null) {
            sb.append(", 客户: ").append(customer.getName());
        }
        if (room != null) {
            sb.append(", 房间: ").append(room.getRoomNumber());
        }
        sb.append(", 入住: ").append(checkInDate);
        sb.append(", 退房: ").append(checkOutDate);
        sb.append(", 状态: ").append(status.getDescription());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", roomId=" + roomId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", guestsCount=" + guestsCount +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", specialRequests='" + specialRequests + '\'' +
                ", bookingTime=" + bookingTime +
                ", createdBy=" + createdBy +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return bookingId != null && bookingId.equals(booking.bookingId);
    }

    @Override
    public int hashCode() {
        return bookingId != null ? bookingId.hashCode() : 0;
    }
}