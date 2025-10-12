-- 数据库迁移脚本：添加 OUT_OF_ORDER 房间状态
-- 执行时间：2025-10-12

USE hotel_management;

-- 修改 rooms 表的 status 字段，添加 OUT_OF_ORDER 状态
ALTER TABLE rooms 
MODIFY COLUMN status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'CLEANING', 'OUT_OF_ORDER') 
DEFAULT 'AVAILABLE' 
COMMENT '房间状态：AVAILABLE=可用，OCCUPIED=已入住，MAINTENANCE=维护中，CLEANING=清洁中，OUT_OF_ORDER=故障';

-- 显示修改结果
DESCRIBE rooms;