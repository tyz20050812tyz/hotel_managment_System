package com.hotel.dao;

import com.hotel.model.Message;
import java.util.List;

/**
 * 留言数据访问对象接口
 */
public interface MessageDAO {
    
    /**
     * 创建新留言
     * @param message 留言对象
     * @return 创建成功返回留言ID，失败返回null
     */
    Integer create(Message message);
    
    /**
     * 根据ID查找留言
     * @param messageId 留言ID
     * @return 留言对象，不存在返回null
     */
    Message findById(Integer messageId);
    
    /**
     * 获取用户收到的留言列表
     * @param receiverId 接收者ID
     * @return 留言列表
     */
    List<Message> findReceivedMessages(Integer receiverId);
    
    /**
     * 获取用户发送的留言列表
     * @param senderId 发送者ID
     * @return 留言列表
     */
    List<Message> findSentMessages(Integer senderId);
    
    /**
     * 获取用户未读留言列表
     * @param receiverId 接收者ID
     * @return 未读留言列表
     */
    List<Message> findUnreadMessages(Integer receiverId);
    
    /**
     * 获取用户未读留言数量
     * @param receiverId 接收者ID
     * @return 未读留言数量
     */
    int getUnreadCount(Integer receiverId);
    
    /**
     * 获取两个用户之间的对话
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 对话留言列表
     */
    List<Message> findConversation(Integer userId1, Integer userId2);
    
    /**
     * 根据类型获取留言列表
     * @param receiverId 接收者ID
     * @param messageType 留言类型
     * @return 留言列表
     */
    List<Message> findByType(Integer receiverId, Message.MessageType messageType);
    
    /**
     * 根据优先级获取留言列表
     * @param receiverId 接收者ID
     * @param priority 优先级
     * @return 留言列表
     */
    List<Message> findByPriority(Integer receiverId, Message.Priority priority);
    
    /**
     * 搜索留言（根据主题或内容）
     * @param userId 用户ID（接收者或发送者）
     * @param keyword 搜索关键词
     * @return 匹配的留言列表
     */
    List<Message> searchMessages(Integer userId, String keyword);
    
    /**
     * 标记留言为已读
     * @param messageId 留言ID
     * @return 更新成功返回true
     */
    boolean markAsRead(Integer messageId);
    
    /**
     * 批量标记留言为已读
     * @param messageIds 留言ID列表
     * @return 更新成功返回true
     */
    boolean markAsReadBatch(List<Integer> messageIds);
    
    /**
     * 标记留言为删除
     * @param messageId 留言ID
     * @return 删除成功返回true
     */
    boolean markAsDeleted(Integer messageId);
    
    /**
     * 批量删除留言
     * @param messageIds 留言ID列表
     * @return 删除成功返回true
     */
    boolean batchDelete(List<Integer> messageIds);
    
    /**
     * 物理删除留言（不可恢复）
     * @param messageId 留言ID
     * @return 删除成功返回true
     */
    boolean delete(Integer messageId);
    
    /**
     * 获取留言回复列表
     * @param parentMessageId 父留言ID
     * @return 回复列表
     */
    List<Message> findReplies(Integer parentMessageId);
    
    /**
     * 获取系统留言列表
     * @param receiverId 接收者ID
     * @return 系统留言列表
     */
    List<Message> findSystemMessages(Integer receiverId);
    
    /**
     * 获取管理员留言列表
     * @param receiverId 接收者ID
     * @return 管理员留言列表
     */
    List<Message> findAdminMessages(Integer receiverId);
    
    /**
     * 分页获取收到的留言
     * @param receiverId 接收者ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 留言列表
     */
    List<Message> findReceivedMessagesWithPaging(Integer receiverId, int offset, int limit);
    
    /**
     * 分页获取发送的留言
     * @param senderId 发送者ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 留言列表
     */
    List<Message> findSentMessagesWithPaging(Integer senderId, int offset, int limit);
    
    /**
     * 获取用户收到的留言总数
     * @param receiverId 接收者ID
     * @return 留言总数
     */
    int getReceivedMessageCount(Integer receiverId);
    
    /**
     * 获取用户发送的留言总数
     * @param senderId 发送者ID
     * @return 留言总数
     */
    int getSentMessageCount(Integer senderId);
}