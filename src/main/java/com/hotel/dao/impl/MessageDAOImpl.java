package com.hotel.dao.impl;

import com.hotel.dao.MessageDAO;
import com.hotel.model.Message;
import com.hotel.model.Message.MessageType;
import com.hotel.model.Message.Priority;
import com.hotel.model.User;
import com.hotel.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 留言数据访问对象实现类
 */
public class MessageDAOImpl implements MessageDAO {
    
    private static final Logger logger = LogManager.getLogger(MessageDAOImpl.class);
    
    // SQL 语句
    private static final String INSERT_SQL = 
        "INSERT INTO messages (sender_id, receiver_id, subject, content, message_type, priority, parent_message_id, attachment_path) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE m.message_id = ? AND m.is_deleted = FALSE";
    
    private static final String SELECT_RECEIVED_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE m.receiver_id = ? AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time DESC";
    
    private static final String SELECT_SENT_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE m.sender_id = ? AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time DESC";
    
    private static final String SELECT_UNREAD_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE m.receiver_id = ? AND m.is_read = FALSE AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time DESC";
    
    private static final String COUNT_UNREAD_SQL = 
        "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = FALSE AND is_deleted = FALSE";
    
    private static final String SELECT_CONVERSATION_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE ((m.sender_id = ? AND m.receiver_id = ?) OR (m.sender_id = ? AND m.receiver_id = ?)) " +
        "AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time ASC";
    
    private static final String SEARCH_MESSAGES_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE (m.sender_id = ? OR m.receiver_id = ?) " +
        "AND (m.subject LIKE ? OR m.content LIKE ?) " +
        "AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time DESC";
    
    private static final String UPDATE_READ_SQL = 
        "UPDATE messages SET is_read = TRUE, read_time = CURRENT_TIMESTAMP WHERE message_id = ?";
    
    private static final String UPDATE_DELETED_SQL = 
        "UPDATE messages SET is_deleted = TRUE WHERE message_id = ?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM messages WHERE message_id = ?";
    
    private static final String SELECT_REPLIES_SQL = 
        "SELECT m.*, " +
        "s.username AS sender_username, s.real_name AS sender_real_name, s.role AS sender_role, " +
        "r.username AS receiver_username, r.real_name AS receiver_real_name, r.role AS receiver_role " +
        "FROM messages m " +
        "LEFT JOIN users s ON m.sender_id = s.user_id " +
        "LEFT JOIN users r ON m.receiver_id = r.user_id " +
        "WHERE m.parent_message_id = ? AND m.is_deleted = FALSE " +
        "ORDER BY m.send_time ASC";

    @Override
    public Integer create(Message message) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, message.getSenderId());
            stmt.setInt(2, message.getReceiverId());
            stmt.setString(3, message.getSubject());
            stmt.setString(4, message.getContent());
            stmt.setString(5, message.getMessageType().name());
            stmt.setString(6, message.getPriority().name());
            
            if (message.getParentMessageId() != null) {
                stmt.setInt(7, message.getParentMessageId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setString(8, message.getAttachmentPath());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer messageId = rs.getInt(1);
                    logger.info("Message created successfully with ID: {}", messageId);
                    return messageId;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error creating message", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public Message findById(Integer messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            stmt.setInt(1, messageId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMessage(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error finding message by ID: " + messageId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }

    @Override
    public List<Message> findReceivedMessages(Integer receiverId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_RECEIVED_SQL);
            stmt.setInt(1, receiverId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding received messages for user: " + receiverId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public List<Message> findSentMessages(Integer senderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_SENT_SQL);
            stmt.setInt(1, senderId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding sent messages for user: " + senderId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public List<Message> findUnreadMessages(Integer receiverId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_UNREAD_SQL);
            stmt.setInt(1, receiverId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding unread messages for user: " + receiverId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public int getUnreadCount(Integer receiverId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(COUNT_UNREAD_SQL);
            stmt.setInt(1, receiverId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting unread count for user: " + receiverId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    @Override
    public List<Message> findConversation(Integer userId1, Integer userId2) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_CONVERSATION_SQL);
            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding conversation between users: " + userId1 + " and " + userId2, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public List<Message> findByType(Integer receiverId, MessageType messageType) {
        String sql = SELECT_RECEIVED_SQL + " AND m.message_type = ?";
        return findMessagesByCondition(sql, receiverId, messageType.name());
    }

    @Override
    public List<Message> findByPriority(Integer receiverId, Priority priority) {
        String sql = SELECT_RECEIVED_SQL + " AND m.priority = ?";
        return findMessagesByCondition(sql, receiverId, priority.name());
    }

    @Override
    public List<Message> searchMessages(Integer userId, String keyword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SEARCH_MESSAGES_SQL);
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            String searchPattern = "%" + keyword + "%";
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error searching messages for user: " + userId + " with keyword: " + keyword, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public boolean markAsRead(Integer messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_READ_SQL);
            stmt.setInt(1, messageId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error marking message as read: " + messageId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean markAsReadBatch(List<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return true;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(UPDATE_READ_SQL);
            
            for (Integer messageId : messageIds) {
                stmt.setInt(1, messageId);
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            conn.commit();
            
            // 检查是否所有操作都成功
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            logger.error("Error batch marking messages as read", e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.error("Error rolling back batch read operation", rollbackEx);
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                logger.error("Error resetting auto-commit", e);
            }
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean markAsDeleted(Integer messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(UPDATE_DELETED_SQL);
            stmt.setInt(1, messageId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error marking message as deleted: " + messageId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean batchDelete(List<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return true;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(UPDATE_DELETED_SQL);
            
            for (Integer messageId : messageIds) {
                stmt.setInt(1, messageId);
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            conn.commit();
            
            // 检查是否所有操作都成功
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            logger.error("Error batch deleting messages", e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.error("Error rolling back batch delete operation", rollbackEx);
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                logger.error("Error resetting auto-commit", e);
            }
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean delete(Integer messageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setInt(1, messageId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error physically deleting message: " + messageId, e);
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public List<Message> findReplies(Integer parentMessageId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SELECT_REPLIES_SQL);
            stmt.setInt(1, parentMessageId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding replies for message: " + parentMessageId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }

    @Override
    public List<Message> findSystemMessages(Integer receiverId) {
        return findByType(receiverId, MessageType.SYSTEM);
    }

    @Override
    public List<Message> findAdminMessages(Integer receiverId) {
        return findByType(receiverId, MessageType.ADMIN);
    }

    @Override
    public List<Message> findReceivedMessagesWithPaging(Integer receiverId, int offset, int limit) {
        String sql = SELECT_RECEIVED_SQL + " LIMIT ? OFFSET ?";
        return findMessagesWithPaging(sql, receiverId, limit, offset);
    }

    @Override
    public List<Message> findSentMessagesWithPaging(Integer senderId, int offset, int limit) {
        String sql = SELECT_SENT_SQL + " LIMIT ? OFFSET ?";
        return findMessagesWithPaging(sql, senderId, limit, offset);
    }

    @Override
    public int getReceivedMessageCount(Integer receiverId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_deleted = FALSE";
        return getMessageCount(sql, receiverId);
    }

    @Override
    public int getSentMessageCount(Integer senderId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE sender_id = ? AND is_deleted = FALSE";
        return getMessageCount(sql, senderId);
    }

    // 私有辅助方法
    
    private List<Message> findMessagesByCondition(String sql, Integer userId, String condition) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, condition);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding messages by condition for user: " + userId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }
    
    private List<Message> findMessagesWithPaging(String sql, Integer userId, int limit, int offset) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding messages with paging for user: " + userId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return messages;
    }
    
    private int getMessageCount(String sql, Integer userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting message count for user: " + userId, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return 0;
    }

    /**
     * 将ResultSet映射为Message对象
     */
    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        
        // 基本字段
        message.setMessageId(rs.getInt("message_id"));
        message.setSenderId(rs.getInt("sender_id"));
        message.setReceiverId(rs.getInt("receiver_id"));
        message.setSubject(rs.getString("subject"));
        message.setContent(rs.getString("content"));
        message.setIsRead(rs.getBoolean("is_read"));
        message.setSendTime(rs.getTimestamp("send_time"));
        message.setReadTime(rs.getTimestamp("read_time"));
        
        // 枚举字段
        String messageTypeStr = rs.getString("message_type");
        if (messageTypeStr != null) {
            message.setMessageType(MessageType.valueOf(messageTypeStr));
        }
        
        String priorityStr = rs.getString("priority");
        if (priorityStr != null) {
            message.setPriority(Priority.valueOf(priorityStr));
        }
        
        // 可选字段
        Integer parentMessageId = rs.getInt("parent_message_id");
        if (!rs.wasNull()) {
            message.setParentMessageId(parentMessageId);
        }
        
        message.setAttachmentPath(rs.getString("attachment_path"));
        message.setIsDeleted(rs.getBoolean("is_deleted"));
        
        // 关联的用户信息
        String senderUsername = rs.getString("sender_username");
        if (senderUsername != null) {
            User sender = new User();
            sender.setUserId(message.getSenderId());
            sender.setUsername(senderUsername);
            sender.setRealName(rs.getString("sender_real_name"));
            String senderRoleStr = rs.getString("sender_role");
            if (senderRoleStr != null) {
                sender.setRole(User.UserRole.valueOf(senderRoleStr));
            }
            message.setSender(sender);
        }
        
        String receiverUsername = rs.getString("receiver_username");
        if (receiverUsername != null) {
            User receiver = new User();
            receiver.setUserId(message.getReceiverId());
            receiver.setUsername(receiverUsername);
            receiver.setRealName(rs.getString("receiver_real_name"));
            String receiverRoleStr = rs.getString("receiver_role");
            if (receiverRoleStr != null) {
                receiver.setRole(User.UserRole.valueOf(receiverRoleStr));
            }
            message.setReceiver(receiver);
        }
        
        return message;
    }

    /**
     * 关闭数据库资源
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Error closing ResultSet", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Error closing PreparedStatement", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing Connection", e);
            }
        }
    }
}