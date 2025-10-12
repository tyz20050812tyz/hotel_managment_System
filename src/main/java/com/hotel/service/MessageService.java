package com.hotel.service;

import com.hotel.model.Message;
import com.hotel.model.User;
import java.util.List;

/**
 * 留言服务接口
 */
public interface MessageService {
    
    /**
     * 发送留言
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param subject 主题
     * @param content 内容
     * @param messageType 留言类型
     * @param priority 优先级
     * @return 留言ID，失败返回null
     */
    Integer sendMessage(Integer senderId, Integer receiverId, String subject, String content, 
                       Message.MessageType messageType, Message.Priority priority);
    
    /**
     * 发送简单留言（默认类型和优先级）
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param subject 主题
     * @param content 内容
     * @return 留言ID，失败返回null
     */
    Integer sendMessage(Integer senderId, Integer receiverId, String subject, String content);
    
    /**
     * 回复留言
     * @param senderId 发送者ID
     * @param parentMessageId 父留言ID
     * @param subject 主题
     * @param content 内容
     * @return 留言ID，失败返回null
     */
    Integer replyMessage(Integer senderId, Integer parentMessageId, String subject, String content);
    
    /**
     * 获取留言详情
     * @param messageId 留言ID
     * @return 留言对象
     */
    Message getMessage(Integer messageId);
    
    /**
     * 获取用户收件箱
     * @param userId 用户ID
     * @return 收到的留言列表
     */
    List<Message> getInbox(Integer userId);
    
    /**
     * 获取用户发件箱
     * @param userId 用户ID
     * @return 发送的留言列表
     */
    List<Message> getOutbox(Integer userId);
    
    /**
     * 获取未读留言
     * @param userId 用户ID
     * @return 未读留言列表
     */
    List<Message> getUnreadMessages(Integer userId);
    
    /**
     * 获取未读留言数量
     * @param userId 用户ID
     * @return 未读留言数量
     */
    int getUnreadCount(Integer userId);
    
    /**
     * 获取两个用户之间的对话
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 对话留言列表
     */
    List<Message> getConversation(Integer userId1, Integer userId2);
    
    /**
     * 搜索留言
     * @param userId 用户ID
     * @param keyword 搜索关键词
     * @return 匹配的留言列表
     */
    List<Message> searchMessages(Integer userId, String keyword);
    
    /**
     * 标记留言为已读
     * @param messageId 留言ID
     * @param userId 当前用户ID（权限验证）
     * @return 操作是否成功
     */
    boolean markAsRead(Integer messageId, Integer userId);
    
    /**
     * 批量标记为已读
     * @param messageIds 留言ID列表
     * @param userId 当前用户ID（权限验证）
     * @return 操作是否成功
     */
    boolean markAsReadBatch(List<Integer> messageIds, Integer userId);
    
    /**
     * 删除留言
     * @param messageId 留言ID
     * @param userId 当前用户ID（权限验证）
     * @return 操作是否成功
     */
    boolean deleteMessage(Integer messageId, Integer userId);
    
    /**
     * 批量删除留言
     * @param messageIds 留言ID列表
     * @param userId 当前用户ID（权限验证）
     * @return 操作是否成功
     */
    boolean batchDeleteMessages(List<Integer> messageIds, Integer userId);
    
    /**
     * 获取系统留言
     * @param userId 用户ID
     * @return 系统留言列表
     */
    List<Message> getSystemMessages(Integer userId);
    
    /**
     * 获取管理员留言
     * @param userId 用户ID
     * @return 管理员留言列表
     */
    List<Message> getAdminMessages(Integer userId);
    
    /**
     * 获取留言回复列表
     * @param parentMessageId 父留言ID
     * @return 回复列表
     */
    List<Message> getReplies(Integer parentMessageId);
    
    /**
     * 发送系统通知
     * @param receiverId 接收者ID
     * @param subject 主题
     * @param content 内容
     * @param priority 优先级
     * @return 留言ID，失败返回null
     */
    Integer sendSystemNotification(Integer receiverId, String subject, String content, Message.Priority priority);
    
    /**
     * 发送管理员通知
     * @param senderId 发送者ID（管理员）
     * @param receiverId 接收者ID
     * @param subject 主题
     * @param content 内容
     * @param priority 优先级
     * @return 留言ID，失败返回null
     */
    Integer sendAdminNotification(Integer senderId, Integer receiverId, String subject, String content, Message.Priority priority);
    
    /**
     * 群发留言给所有用户
     * @param senderId 发送者ID
     * @param subject 主题
     * @param content 内容
     * @param messageType 留言类型
     * @param priority 优先级
     * @return 成功发送的数量
     */
    int broadcastMessage(Integer senderId, String subject, String content, Message.MessageType messageType, Message.Priority priority);
    
    /**
     * 群发留言给指定角色的用户
     * @param senderId 发送者ID
     * @param subject 主题
     * @param content 内容
     * @param targetRole 目标角色
     * @param messageType 留言类型
     * @param priority 优先级
     * @return 成功发送的数量
     */
    int broadcastToRole(Integer senderId, String subject, String content, User.UserRole targetRole, 
                       Message.MessageType messageType, Message.Priority priority);
    
    /**
     * 分页获取收件箱
     * @param userId 用户ID
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 留言列表
     */
    List<Message> getInboxWithPaging(Integer userId, int page, int size);
    
    /**
     * 分页获取发件箱
     * @param userId 用户ID
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 留言列表
     */
    List<Message> getOutboxWithPaging(Integer userId, int page, int size);
    
    /**
     * 获取收件箱总数
     * @param userId 用户ID
     * @return 总数
     */
    int getInboxCount(Integer userId);
    
    /**
     * 获取发件箱总数
     * @param userId 用户ID
     * @return 总数
     */
    int getOutboxCount(Integer userId);
    
    /**
     * 验证留言参数
     * @param message 留言对象
     * @return 验证错误信息，null表示验证通过
     */
    String validateMessage(Message message);
    
    /**
     * 检查用户是否可以查看留言
     * @param messageId 留言ID
     * @param userId 用户ID
     * @return true表示可以查看
     */
    boolean canViewMessage(Integer messageId, Integer userId);
    
    /**
     * 检查用户是否可以删除留言
     * @param messageId 留言ID
     * @param userId 用户ID
     * @return true表示可以删除
     */
    boolean canDeleteMessage(Integer messageId, Integer userId);
}