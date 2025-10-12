-- 酒店管理系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hotel_management 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE hotel_management;

-- 1. 用户表 (users)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(加密存储)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    role ENUM('ADMIN', 'STAFF') NOT NULL COMMENT '用户角色',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    status TINYINT(1) DEFAULT 1 COMMENT '状态(1:正常,0:禁用)',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_username (username),
    INDEX idx_role (role)
) COMMENT '用户表';

-- 2. 客户表 (customers)
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '客户ID',
    name VARCHAR(50) NOT NULL COMMENT '客户姓名',
    id_card VARCHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    address VARCHAR(200) COMMENT '联系地址',
    vip_level TINYINT(1) DEFAULT 0 COMMENT 'VIP等级(0:普通,1-5:VIP等级)',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    INDEX idx_id_card (id_card),
    INDEX idx_phone (phone),
    INDEX idx_vip_level (vip_level)
) COMMENT '客户表';

-- 3. 房间类型表 (room_types)
CREATE TABLE room_types (
    type_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '类型ID',
    type_name VARCHAR(50) NOT NULL COMMENT '房间类型名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格/晚',
    bed_count TINYINT(1) NOT NULL COMMENT '床位数',
    max_guests TINYINT(1) NOT NULL COMMENT '最大入住人数',
    description TEXT COMMENT '房间描述',
    amenities TEXT COMMENT '房间设施',
    INDEX idx_type_name (type_name)
) COMMENT '房间类型表';

-- 4. 房间表 (rooms)
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '房间ID',
    room_number VARCHAR(10) NOT NULL UNIQUE COMMENT '房间号',
    type_id INT NOT NULL COMMENT '房间类型ID',
    floor TINYINT(2) NOT NULL COMMENT '楼层',
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'CLEANING', 'OUT_OF_ORDER') 
           DEFAULT 'AVAILABLE' COMMENT '房间状态',
    last_cleaned TIMESTAMP NULL COMMENT '最后清洁时间',
    FOREIGN KEY (type_id) REFERENCES room_types(type_id) ON DELETE RESTRICT,
    INDEX idx_room_number (room_number),
    INDEX idx_type_id (type_id),
    INDEX idx_status (status),
    INDEX idx_floor (floor)
) COMMENT '房间表';

-- 5. 预订表 (bookings)
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '预订ID',
    customer_id INT NOT NULL COMMENT '客户ID',
    room_id INT NOT NULL COMMENT '房间ID',
    check_in_date DATE NOT NULL COMMENT '入住日期',
    check_out_date DATE NOT NULL COMMENT '退房日期',
    guests_count TINYINT(1) NOT NULL COMMENT '入住人数',
    total_price DECIMAL(10,2) NOT NULL COMMENT '总价格',
    status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') 
           DEFAULT 'PENDING' COMMENT '预订状态',
    special_requests TEXT COMMENT '特殊要求',
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '预订时间',
    created_by INT COMMENT '创建人(员工ID)',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_customer_id (customer_id),
    INDEX idx_room_id (room_id),
    INDEX idx_check_in_date (check_in_date),
    INDEX idx_check_out_date (check_out_date),
    INDEX idx_status (status),
    INDEX idx_booking_time (booking_time)
) COMMENT '预订表';

-- 插入初始数据

-- 1. 插入管理员用户
INSERT INTO users (username, password, real_name, role, phone, email) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', '系统管理员', 'ADMIN', '13800138000', 'admin@hotel.com'),
('staff001', '25d55ad283aa400af464c76d713c07ad', '张小明', 'STAFF', '13800138001', 'zhang@hotel.com'),
('staff002', '5e8667a439c68f5145dd2fcbecf02209', '李小红', 'STAFF', '13800138002', 'li@hotel.com');

-- 2. 插入房间类型
INSERT INTO room_types (type_name, price, bed_count, max_guests, description, amenities) VALUES
('标准单人间', 188.00, 1, 1, '舒适的单人间，适合商务出行', '空调,电视,免费WiFi,独立卫浴,24小时热水'),
('标准双人间', 268.00, 1, 2, '温馨的双人间，适合情侣和夫妻', '空调,电视,免费WiFi,独立卫浴,24小时热水,迷你冰箱'),
('豪华套房', 588.00, 2, 4, '豪华套房，客厅卧室分离', '空调,电视,免费WiFi,独立卫浴,24小时热水,迷你冰箱,沙发,书桌'),
('总统套房', 1288.00, 3, 6, '顶级奢华体验，专属服务', '所有豪华设施,管家服务,景观阳台,按摩浴缸,高端音响');

-- 3. 插入房间信息
INSERT INTO rooms (room_number, type_id, floor, status) VALUES
-- 1楼标准单人间
('101', 1, 1, 'AVAILABLE'),
('102', 1, 1, 'AVAILABLE'),
('103', 1, 1, 'AVAILABLE'),
-- 1楼标准双人间
('111', 2, 1, 'AVAILABLE'),
('112', 2, 1, 'AVAILABLE'),
('113', 2, 1, 'AVAILABLE'),

-- 2楼标准单人间
('201', 1, 2, 'AVAILABLE'),
('202', 1, 2, 'OCCUPIED'),
('203', 1, 2, 'AVAILABLE'),
-- 2楼标准双人间
('211', 2, 2, 'AVAILABLE'),
('212', 2, 2, 'AVAILABLE'),
('213', 2, 2, 'CLEANING'),

-- 3楼豪华套房
('301', 3, 3, 'AVAILABLE'),
('302', 3, 3, 'AVAILABLE'),
('303', 3, 3, 'MAINTENANCE'),

-- 顶楼总统套房
('401', 4, 4, 'OCCUPIED'),
('402', 4, 4, 'CLEANING');

-- 添加更多测试房间
-- 超级豪华总统套房
INSERT INTO room_types (type_name, price, bed_count, max_guests, description, amenities) VALUES
('超级豪华总统套房', 100000.00, 5, 10, '顶级奋华体验，全方位的精品服务', '所有豪华设施,管家服务,景观阳台,按摩浴缸,高端音响,私人健身房,红酒窖,书房');

INSERT INTO rooms (room_number, type_id, floor, status) VALUES
-- 5楼超级豪华总统套房
('1001', 5, 10, 'OUT_OF_ORDER'),  -- 故障状态
('504', 5, 5, 'CLEANING'),
('505', 2, 5, 'AVAILABLE');

-- 4. 插入测试客户数据
INSERT INTO customers (name, id_card, phone, email, address, vip_level) VALUES
('王大明', '110101199001011234', '13901234567', 'wangdaming@email.com', '北京市朝阳区建国路100号', 2),
('李小芳', '110101199202025678', '13901234568', 'lixiaofang@email.com', '北京市海淀区中关村大街200号', 1),
('张三', '110101199303036789', '13901234569', 'zhangsan@email.com', '北京市西城区西单大街300号', 0),
('赵四', '110101199404047890', '13901234570', 'zhaosi@email.com', '北京市东城区王府井大街400号', 3);

-- 5. 插入测试预订数据
INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date, guests_count, total_price, status, created_by) VALUES
(1, 2, '2025-09-26', '2025-09-28', 1, 376.00, 'CHECKED_IN', 2),
(2, 12, '2025-09-25', '2025-09-27', 2, 536.00, 'CONFIRMED', 2),
(3, 13, '2025-09-28', '2025-09-30', 3, 1176.00, 'PENDING', 3),
(4, 16, '2025-10-01', '2025-10-03', 2, 2576.00, 'CONFIRMED', 2);

-- 创建视图：可用房间视图
CREATE VIEW available_rooms_view AS
SELECT 
    r.room_id,
    r.room_number,
    r.floor,
    rt.type_name,
    rt.price,
    rt.bed_count,
    rt.max_guests,
    rt.description,
    rt.amenities
FROM rooms r
JOIN room_types rt ON r.type_id = rt.type_id
WHERE r.status = 'AVAILABLE';

-- 创建视图：预订统计视图
CREATE VIEW booking_statistics_view AS
SELECT 
    DATE(b.booking_time) as booking_date,
    COUNT(*) as total_bookings,
    SUM(b.total_price) as total_revenue,
    AVG(b.total_price) as avg_booking_value
FROM bookings b
WHERE b.status != 'CANCELLED'
GROUP BY DATE(b.booking_time)
ORDER BY booking_date DESC;