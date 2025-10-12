package com.hotel.model;

import java.sql.Timestamp;

/**
 * 留言实体类
 * 对应数据库表：messages
 */
public class Message {
    private Integer messageId;          // 留言ID
    private Integer senderId;           // 发送者ID
    private Integer receiverId;         // 接收者ID
    private String subject;             // 主题
    private String content;             // 留言内容
    private Boolean isRead;             // 是否已读
    private Timestamp sendTime;         // 发送时间
    private Timestamp readTime;         // 阅读时间
    private MessageType messageType;    // 留言类型
    private Priority priority;          // 优先级
    private Integer parentMessageId;    // 回复的留言ID
    private String attachmentPath;      // 附件路径
    private Boolean isDeleted;          // 是否删除
    
    // 关联的用户对象（用于连接查询）
    private User sender;                // 发送者
    private User receiver;              // 接收者
    private Message parentMessage;      // 父留言（回复的留言）

    /**
     * 留言类型枚举
     */
    public enum MessageType {
        SYSTEM("系统消息"),
        USER("用户消息"),
        ADMIN("管理员消息");

        private final String description;

        MessageType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 优先级枚举
     */
    public enum Priority {
        LOW("低"),
        NORMAL("普通"),
        HIGH("高"),
        URGENT("紧急");

        private final String description;

        Priority(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 默认构造函数
    public Message() {}

    // 构造函数
    public Message(Integer senderId, Integer receiverId, String subject, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        this.content = content;
        this.isRead = false;
        this.messageType = MessageType.USER;
        this.priority = Priority.NORMAL;
        this.isDeleted = false;
    }

    // Getter 和 Setter 方法
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Integer getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(Integer parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    // 业务方法
    public boolean isUnread() {
        return !isRead;
    }

    public boolean isSystemMessage() {
        return messageType == MessageType.SYSTEM;
    }

    public boolean isAdminMessage() {
        return messageType == MessageType.ADMIN;
    }

    public boolean isUserMessage() {
        return messageType == MessageType.USER;
    }

    public boolean isHighPriority() {
        return priority == Priority.HIGH || priority == Priority.URGENT;
    }

    public boolean isReply() {
        return parentMessageId != null;
    }

    /**
     * 标记为已读
     */
    public void markAsRead() {
        this.isRead = true;
        this.readTime = new Timestamp(System.currentTimeMillis());
    }

    /**
     * 标记为删除
     */
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    /**
     * 获取发送者显示名称
     */
    public String getSenderDisplayName() {
        if (sender != null) {
            return sender.getRealName();
        }
        return "未知用户";
    }

    /**
     * 获取接收者显示名称
     */
    public String getReceiverDisplayName() {
        if (receiver != null) {
            return receiver.getRealName();
        }
        return "未知用户";
    }

    /**
     * 获取优先级样式类
     */
    public String getPriorityClass() {
        switch (priority) {
            case URGENT:
                return "text-danger";
            case HIGH:
                return "text-warning";
            case LOW:
                return "text-muted";
            default:
                return "text-primary";
        }
    }

    /**
     * 获取留言类型样式类
     */
    public String getTypeClass() {
        switch (messageType) {
            case SYSTEM:
                return "badge bg-info";
            case ADMIN:
                return "badge bg-warning";
            default:
                return "badge bg-primary";
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", subject='" + subject + '\'' +
                ", isRead=" + isRead +
                ", sendTime=" + sendTime +
                ", messageType=" + messageType +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return messageId != null && messageId.equals(message.messageId);
    }

    @Override
    public int hashCode() {
        return messageId != null ? messageId.hashCode() : 0;
    }
}