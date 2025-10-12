-- 添加留言功能数据库表
-- 执行时间：2025-10-12

USE hotel_management;

-- 创建留言表
CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '留言ID',
    sender_id INT NOT NULL COMMENT '发送者ID',
    receiver_id INT NOT NULL COMMENT '接收者ID', 
    subject VARCHAR(100) NOT NULL COMMENT '主题',
    content TEXT NOT NULL COMMENT '留言内容',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    send_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    read_time TIMESTAMP NULL COMMENT '阅读时间',
    message_type ENUM('SYSTEM', 'USER', 'ADMIN') DEFAULT 'USER' COMMENT '留言类型',
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') DEFAULT 'NORMAL' COMMENT '优先级',
    parent_message_id INT NULL COMMENT '回复的留言ID（用于留言回复功能）',
    attachment_path VARCHAR(255) NULL COMMENT '附件路径',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE, 
    FOREIGN KEY (parent_message_id) REFERENCES messages(message_id) ON DELETE CASCADE,
    INDEX idx_sender_id (sender_id),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_send_time (send_time),
    INDEX idx_is_read (is_read),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_parent_message_id (parent_message_id)
) COMMENT '留言表';

-- 插入测试数据
INSERT INTO messages (sender_id, receiver_id, subject, content, message_type, priority) VALUES
(1, 2, '欢迎加入酒店管理系统', '欢迎您加入我们的酒店管理团队！如有任何问题，请随时联系我。', 'ADMIN', 'NORMAL'),
(2, 1, '系统使用反馈', '系统整体运行良好，但建议在客户管理模块增加更多筛选条件。', 'USER', 'NORMAL'),
(1, 3, '新功能通知', '系统已更新留言功能，现在可以方便地进行内部沟通了。', 'SYSTEM', 'HIGH'),
(3, 1, '工作安排询问', '请问下周的工作安排是什么？有什么特别需要注意的事项吗？', 'USER', 'NORMAL'),
(1, 2, '系统维护通知', '系统将在本周末进行维护升级，届时可能会有短暂的服务中断。', 'ADMIN', 'HIGH');

-- 显示创建结果
SHOW TABLES LIKE 'messages';
DESCRIBE messages;