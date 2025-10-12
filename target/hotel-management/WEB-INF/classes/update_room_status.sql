-- 更新房间状态枚举，添加OUT_OF_ORDER状态
USE hotel_management;

-- 修改房间表的状态字段，添加OUT_OF_ORDER选项
ALTER TABLE rooms 
MODIFY COLUMN status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'CLEANING', 'OUT_OF_ORDER') 
DEFAULT 'AVAILABLE' 
COMMENT '房间状态';

-- 查看修改后的表结构
DESCRIBE rooms;