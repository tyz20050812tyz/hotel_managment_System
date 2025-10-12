package com.hotel.service.impl;

import com.hotel.dao.MessageDAO;
import com.hotel.dao.UserDAO;
import com.hotel.dao.impl.MessageDAOImpl;
import com.hotel.dao.impl.UserDAOImpl;
import com.hotel.model.Message;
import com.hotel.model.User;
import com.hotel.service.MessageService;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言服务实现类
 */
public class MessageServiceImpl implements MessageService {
    
    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);
    
    private final MessageDAO messageDAO;
    private final UserDAO userDAO;
    
    public MessageServiceImpl() {
        this.messageDAO = new MessageDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public Integer sendMessage(Integer senderId, Integer receiverId, String subject, String content, 
                              Message.MessageType messageType, Message.Priority priority) {
        // 参数验证
        if (senderId == null || receiverId == null) {
            logger.error("发送者ID或接收者ID不能为空");
            return null;
        }
        
        if (Utils.isEmpty(subject)) {
            logger.error("留言主题不能为空");
            return null;
        }
        
        if (Utils.isEmpty(content)) {
            logger.error("留言内容不能为空");
            return null;
        }
        
        // 验证用户是否存在
        User sender = userDAO.findById(senderId);
        User receiver = userDAO.findById(receiverId);
        
        if (sender == null) {
            logger.error("发送者不存在: {}", senderId);
            return null;
        }
        
        if (receiver == null) {
            logger.error("接收者不存在: {}", receiverId);
            return null;
        }
        
        // 创建留言对象
        Message message = new Message(senderId, receiverId, subject, content);
        message.setMessageType(messageType != null ? messageType : Message.MessageType.USER);
        message.setPriority(priority != null ? priority : Message.Priority.NORMAL);
        
        // 验证留言
        String validateError = validateMessage(message);
        if (validateError != null) {
            logger.error("留言验证失败: {}", validateError);
            return null;
        }
        
        try {
            Integer messageId = messageDAO.create(message);
            if (messageId != null) {
                logger.info("留言发送成功: 发送者={}, 接收者={}, 主题={}", senderId, receiverId, subject);
            }
            return messageId;
        } catch (Exception e) {
            logger.error("发送留言失败", e);
            return null;
        }
    }

    @Override
    public Integer sendMessage(Integer senderId, Integer receiverId, String subject, String content) {
        return sendMessage(senderId, receiverId, subject, content, Message.MessageType.USER, Message.Priority.NORMAL);
    }

    @Override
    public Integer replyMessage(Integer senderId, Integer parentMessageId, String subject, String content) {
        if (parentMessageId == null) {
            logger.error("父留言ID不能为空");
            return null;
        }
        
        // 获取父留言
        Message parentMessage = messageDAO.findById(parentMessageId);
        if (parentMessage == null) {
            logger.error("父留言不存在: {}", parentMessageId);
            return null;
        }
        
        // 回复给原发送者
        Integer receiverId = parentMessage.getSenderId();
        
        // 创建回复留言
        Message replyMessage = new Message(senderId, receiverId, subject, content);
        replyMessage.setParentMessageId(parentMessageId);
        replyMessage.setMessageType(Message.MessageType.USER);
        replyMessage.setPriority(parentMessage.getPriority()); // 继承父留言的优先级
        
        try {
            Integer messageId = messageDAO.create(replyMessage);
            if (messageId != null) {
                logger.info("回复留言成功: 发送者={}, 父留言={}", senderId, parentMessageId);
            }
            return messageId;
        } catch (Exception e) {
            logger.error("回复留言失败", e);
            return null;
        }
    }

    @Override
    public Message getMessage(Integer messageId) {
        if (messageId == null) {
            return null;
        }
        
        try {
            return messageDAO.findById(messageId);
        } catch (Exception e) {
            logger.error("获取留言失败: {}", messageId, e);
            return null;
        }
    }

    @Override
    public List<Message> getInbox(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findReceivedMessages(userId);
        } catch (Exception e) {
            logger.error("获取收件箱失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> getOutbox(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findSentMessages(userId);
        } catch (Exception e) {
            logger.error("获取发件箱失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> getUnreadMessages(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findUnreadMessages(userId);
        } catch (Exception e) {
            logger.error("获取未读留言失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getUnreadCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        
        try {
            return messageDAO.getUnreadCount(userId);
        } catch (Exception e) {
            logger.error("获取未读留言数量失败: {}", userId, e);
            return 0;
        }
    }

    @Override
    public List<Message> getConversation(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findConversation(userId1, userId2);
        } catch (Exception e) {
            logger.error("获取对话失败: {} 与 {}", userId1, userId2, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> searchMessages(Integer userId, String keyword) {
        if (userId == null || Utils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.searchMessages(userId, keyword.trim());
        } catch (Exception e) {
            logger.error("搜索留言失败: 用户={}, 关键词={}", userId, keyword, e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean markAsRead(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            return false;
        }
        
        // 权限验证：只有接收者可以标记为已读
        if (!canViewMessage(messageId, userId)) {
            logger.error("用户无权限标记留言为已读: 用户={}, 留言={}", userId, messageId);
            return false;
        }
        
        try {
            return messageDAO.markAsRead(messageId);
        } catch (Exception e) {
            logger.error("标记留言为已读失败: {}", messageId, e);
            return false;
        }
    }

    @Override
    public boolean markAsReadBatch(List<Integer> messageIds, Integer userId) {
        if (messageIds == null || messageIds.isEmpty() || userId == null) {
            return false;
        }
        
        // 过滤出用户有权限的留言
        List<Integer> validMessageIds = new ArrayList<>();
        for (Integer messageId : messageIds) {
            if (canViewMessage(messageId, userId)) {
                validMessageIds.add(messageId);
            }
        }
        
        if (validMessageIds.isEmpty()) {
            logger.error("用户无权限标记任何留言为已读: 用户={}", userId);
            return false;
        }
        
        try {
            return messageDAO.markAsReadBatch(validMessageIds);
        } catch (Exception e) {
            logger.error("批量标记留言为已读失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteMessage(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            return false;
        }
        
        // 权限验证：发送者和接收者都可以删除留言
        if (!canDeleteMessage(messageId, userId)) {
            logger.error("用户无权限删除留言: 用户={}, 留言={}", userId, messageId);
            return false;
        }
        
        try {
            return messageDAO.markAsDeleted(messageId);
        } catch (Exception e) {
            logger.error("删除留言失败: {}", messageId, e);
            return false;
        }
    }

    @Override
    public boolean batchDeleteMessages(List<Integer> messageIds, Integer userId) {
        if (messageIds == null || messageIds.isEmpty() || userId == null) {
            return false;
        }
        
        // 过滤出用户有权限的留言
        List<Integer> validMessageIds = new ArrayList<>();
        for (Integer messageId : messageIds) {
            if (canDeleteMessage(messageId, userId)) {
                validMessageIds.add(messageId);
            }
        }
        
        if (validMessageIds.isEmpty()) {
            logger.error("用户无权限删除任何留言: 用户={}", userId);
            return false;
        }
        
        try {
            return messageDAO.batchDelete(validMessageIds);
        } catch (Exception e) {
            logger.error("批量删除留言失败", e);
            return false;
        }
    }

    @Override
    public List<Message> getSystemMessages(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findSystemMessages(userId);
        } catch (Exception e) {
            logger.error("获取系统留言失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> getAdminMessages(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findAdminMessages(userId);
        } catch (Exception e) {
            logger.error("获取管理员留言失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> getReplies(Integer parentMessageId) {
        if (parentMessageId == null) {
            return new ArrayList<>();
        }
        
        try {
            return messageDAO.findReplies(parentMessageId);
        } catch (Exception e) {
            logger.error("获取留言回复失败: {}", parentMessageId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Integer sendSystemNotification(Integer receiverId, String subject, String content, Message.Priority priority) {
        // 系统通知使用系统管理员ID（假设为1）
        Integer systemUserId = 1;
        return sendMessage(systemUserId, receiverId, subject, content, Message.MessageType.SYSTEM, priority);
    }

    @Override
    public Integer sendAdminNotification(Integer senderId, Integer receiverId, String subject, String content, Message.Priority priority) {
        // 验证发送者是否为管理员
        User sender = userDAO.findById(senderId);
        if (sender == null || sender.getRole() != User.UserRole.ADMIN) {
            logger.error("非管理员用户尝试发送管理员通知: {}", senderId);
            return null;
        }
        
        return sendMessage(senderId, receiverId, subject, content, Message.MessageType.ADMIN, priority);
    }

    @Override
    public int broadcastMessage(Integer senderId, String subject, String content, Message.MessageType messageType, Message.Priority priority) {
        if (senderId == null || Utils.isEmpty(subject) || Utils.isEmpty(content)) {
            return 0;
        }
        
        // 获取所有用户
        List<User> allUsers = userDAO.findAll();
        int successCount = 0;
        
        for (User user : allUsers) {
            // 不给自己发送
            if (!user.getUserId().equals(senderId)) {
                Integer messageId = sendMessage(senderId, user.getUserId(), subject, content, messageType, priority);
                if (messageId != null) {
                    successCount++;
                }
            }
        }
        
        logger.info("群发留言完成: 发送者={}, 成功={}/{}", senderId, successCount, allUsers.size() - 1);
        return successCount;
    }

    @Override
    public int broadcastToRole(Integer senderId, String subject, String content, User.UserRole targetRole, 
                              Message.MessageType messageType, Message.Priority priority) {
        if (senderId == null || targetRole == null || Utils.isEmpty(subject) || Utils.isEmpty(content)) {
            return 0;
        }
        
        // 获取指定角色的用户
        List<User> users = userDAO.findByRole(targetRole);
        int successCount = 0;
        
        for (User user : users) {
            // 不给自己发送
            if (!user.getUserId().equals(senderId)) {
                Integer messageId = sendMessage(senderId, user.getUserId(), subject, content, messageType, priority);
                if (messageId != null) {
                    successCount++;
                }
            }
        }
        
        logger.info("角色群发留言完成: 发送者={}, 目标角色={}, 成功={}/{}", senderId, targetRole, successCount, users.size());
        return successCount;
    }

    @Override
    public List<Message> getInboxWithPaging(Integer userId, int page, int size) {
        if (userId == null || page < 1 || size < 1) {
            return new ArrayList<>();
        }
        
        int offset = (page - 1) * size;
        
        try {
            return messageDAO.findReceivedMessagesWithPaging(userId, offset, size);
        } catch (Exception e) {
            logger.error("分页获取收件箱失败: 用户={}, 页码={}, 大小={}", userId, page, size, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> getOutboxWithPaging(Integer userId, int page, int size) {
        if (userId == null || page < 1 || size < 1) {
            return new ArrayList<>();
        }
        
        int offset = (page - 1) * size;
        
        try {
            return messageDAO.findSentMessagesWithPaging(userId, offset, size);
        } catch (Exception e) {
            logger.error("分页获取发件箱失败: 用户={}, 页码={}, 大小={}", userId, page, size, e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getInboxCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        
        try {
            return messageDAO.getReceivedMessageCount(userId);
        } catch (Exception e) {
            logger.error("获取收件箱总数失败: {}", userId, e);
            return 0;
        }
    }

    @Override
    public int getOutboxCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        
        try {
            return messageDAO.getSentMessageCount(userId);
        } catch (Exception e) {
            logger.error("获取发件箱总数失败: {}", userId, e);
            return 0;
        }
    }

    @Override
    public String validateMessage(Message message) {
        if (message == null) {
            return "留言对象不能为空";
        }
        
        if (message.getSenderId() == null) {
            return "发送者ID不能为空";
        }
        
        if (message.getReceiverId() == null) {
            return "接收者ID不能为空";
        }
        
        if (message.getSenderId().equals(message.getReceiverId())) {
            return "不能给自己发送留言";
        }
        
        if (Utils.isEmpty(message.getSubject())) {
            return "留言主题不能为空";
        }
        
        if (message.getSubject().length() > 100) {
            return "留言主题不能超过100个字符";
        }
        
        if (Utils.isEmpty(message.getContent())) {
            return "留言内容不能为空";
        }
        
        if (message.getContent().length() > 10000) {
            return "留言内容不能超过10000个字符";
        }
        
        return null; // 验证通过
    }

    @Override
    public boolean canViewMessage(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            return false;
        }
        
        Message message = messageDAO.findById(messageId);
        if (message == null) {
            return false;
        }
        
        // 发送者和接收者都可以查看
        return message.getSenderId().equals(userId) || message.getReceiverId().equals(userId);
    }

    @Override
    public boolean canDeleteMessage(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            return false;
        }
        
        Message message = messageDAO.findById(messageId);
        if (message == null) {
            return false;
        }
        
        // 发送者和接收者都可以删除
        return message.getSenderId().equals(userId) || message.getReceiverId().equals(userId);
    }
}