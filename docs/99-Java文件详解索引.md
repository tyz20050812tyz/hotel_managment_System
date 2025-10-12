# Java文件完整详解索引

## 📋 文档说明
本文档提供了项目中所有Java文件的详细索引和核心功能说明，帮助你快速了解每个类的作用和实现。

---

## 📂 一、Controller层 (8个文件)

### 1. BaseController.java ⭐⭐⭐
**路径**: `src/main/java/com/hotel/controller/BaseController.java`

**类型**: 抽象基类

**核心功能**:
- 使用**模板方法模式**定义请求处理流程
- 统一处理GET/POST请求
- 提供参数验证、权限检查钩子方法
- 统一异常处理
- 提供工具方法（获取当前用户、参数解析、JSON响应）

**关键方法**:
```java
// 模板方法
private void processRequest(HttpServletRequest req, HttpServletResponse resp)

// 抽象方法（子类必须实现）
protected abstract String handleBusinessLogic(HttpServletRequest req, HttpServletResponse resp)

// 钩子方法（子类可选重写）
protected boolean validateParameters(HttpServletRequest request)
protected boolean checkPermission(HttpServletRequest request)

// 工具方法
protected User getCurrentUser(HttpServletRequest request)
protected String getParameter(HttpServletRequest request, String name)
protected int getIntParameter(HttpServletRequest request, String name, int defaultValue)
protected String createSuccessResponse(Object data)
protected String createErrorResponse(String message, int code)
```

**设计模式**: 模板方法模式

**重要性**: ⭐⭐⭐ 核心基类，所有Controller的基础

---

### 2. LoginController.java
**路径**: `src/main/java/com/hotel/controller/LoginController.java`

**URL映射**: `/login`

**核心功能**:
- 用户登录验证
- Session创建和管理
- 记住我功能
- 登录失败提示

**关键方法**:
```java
handleGetRequest() - 显示登录页面
handlePostRequest() - 处理登录提交
validateParameters() - 验证用户名和密码
```

**业务流程**:
```
1. 接收用户名和密码
2. 调用UserService.authenticate()验证
3. 验证成功：创建Session，重定向到首页
4. 验证失败：显示错误信息，返回登录页
```

---

### 3. LogoutController.java
**路径**: `src/main/java/com/hotel/controller/LogoutController.java`

**URL映射**: `/logout`

**核心功能**:
- 销毁用户Session
- 记录登出日志
- 重定向到登录页

**关键方法**:
```java
handleBusinessLogic() - 处理登出逻辑
```

---

### 4. IndexController.java
**路径**: `src/main/java/com/hotel/controller/IndexController.java`

**URL映射**: `/admin/index`

**核心功能**:
- 管理后台首页
- 统计仪表板数据
- 今日入住/退房统计
- 房间入住率计算
- VIP客户统计

**关键方法**:
```java
showAdminDashboard() - 显示管理后台首页
getStatistics() - 获取统计数据
getRoomStatusStatistics() - 房间状态统计
getVipLevelStatistics() - VIP等级统计
```

**统计指标**:
- 总用户数、管理员数
- 总客户数、VIP客户数
- 总房间数、可用房间数、入住房间数
- 房间入住率
- 总预订数、今日入住/退房数

---

### 5. CustomerController.java ⭐⭐
**路径**: `src/main/java/com/hotel/controller/CustomerController.java`

**URL映射**: `/admin/customer/*`

**核心功能**:
- 客户信息CRUD操作
- 客户列表分页展示
- 客户搜索（姓名/VIP等级）
- 客户详情查看

**URL路由**:
| URL | 方法 | 功能 |
|-----|------|------|
| /admin/customer/list | GET | 客户列表 |
| /admin/customer/add | GET | 显示添加表单 |
| /admin/customer/edit?id=X | GET | 显示编辑表单 |
| /admin/customer/detail?id=X | GET | 客户详情 |
| /admin/customer/save | POST | 保存客户 |
| /admin/customer/delete | POST | 删除客户 |

**关键方法**:
```java
listCustomers() - 客户列表（分页、搜索）
showAddForm() - 显示添加表单
showEditForm() - 显示编辑表单
showCustomerDetail() - 显示客户详情
saveCustomer() - 保存客户（新增/更新）
deleteCustomer() - 删除客户
```

**数据验证**:
- 姓名：必填
- 手机号：必填，11位，格式验证
- 身份证号：必填，18位，格式验证
- 邮箱：选填，格式验证
- VIP等级：0-5整数

---

### 6. RoomController.java ⭐⭐
**路径**: `src/main/java/com/hotel/controller/RoomController.java`

**URL映射**: `/admin/room/*`

**核心功能**:
- 房间信息CRUD
- 房间类型管理
- 房间状态管理
- 房间筛选（按类型/状态）

**URL路由**:
| URL | 方法 | 功能 |
|-----|------|------|
| /admin/room/list | GET | 房间列表 |
| /admin/room/add | GET | 显示添加表单 |
| /admin/room/edit?id=X | GET | 显示编辑表单 |
| /admin/room/detail?id=X | GET | 房间详情 |
| /admin/room/save | POST | 保存房间 |
| /admin/room/delete | POST | 删除房间 |
| /admin/room/changeStatus | POST | 修改房间状态 |
| /admin/room/type/* | GET/POST | 房间类型管理 |

**关键方法**:
```java
listRooms() - 房间列表（分页、筛选）
saveRoom() - 保存房间
deleteRoom() - 删除房间
changeRoomStatus() - 修改房间状态
saveRoomType() - 保存房间类型
```

**房间状态**:
- AVAILABLE - 空闲可用
- BOOKED - 已预订
- OCCUPIED - 入住中
- MAINTENANCE - 维护中

---

### 7. BookingController.java ⭐⭐⭐
**路径**: `src/main/java/com/hotel/controller/BookingController.java`

**URL映射**: `/admin/booking/*`

**核心功能**:
- 预订信息CRUD
- 入住办理
- 退房结算
- 预订查询（分页、筛选）
- 价格自动计算

**URL路由**:
| URL | 方法 | 功能 |
|-----|------|------|
| /admin/booking/list | GET | 预订列表 |
| /admin/booking/add | GET | 显示添加表单 |
| /admin/booking/edit?id=X | GET | 显示编辑表单 |
| /admin/booking/detail?id=X | GET | 预订详情 |
| /admin/booking/save | POST | 保存预订 |
| /admin/booking/delete | POST | 删除预订 |
| /admin/booking/checkIn | POST | 办理入住 |
| /admin/booking/checkOut | POST | 办理退房 |
| /admin/booking/cancel | POST | 取消预订 |

**关键方法**:
```java
listBookings() - 预订列表（分页、筛选）
saveBooking() - 保存预订（价格自动计算）
checkInBooking() - 办理入住
checkOutBooking() - 办理退房
cancelBooking() - 取消预订
```

**业务逻辑**:
1. **创建预订**: 检查房间可用性 → 计算价格 → 创建预订 → 更新房间状态
2. **入住办理**: 验证预订状态 → 记录入住时间 → 更新状态为CHECKED_IN
3. **退房结算**: 记录退房时间 → 更新状态为CHECKED_OUT → 更新房间状态

**预订状态流转**:
```
PENDING → CONFIRMED → CHECKED_IN → CHECKED_OUT
   ↓         ↓            ↓
CANCELLED  CANCELLED  CANCELLED
```

---

### 8. UserController.java
**路径**: `src/main/java/com/hotel/controller/UserController.java`

**URL映射**: `/admin/user/*`

**核心功能**:
- 用户信息CRUD
- 密码修改
- 用户权限管理

**关键方法**:
```java
listUsers() - 用户列表
saveUser() - 保存用户
deleteUser() - 删除用户
```

---

## 📂 二、Service层 (8个文件)

### 1. CustomerService.java / CustomerServiceImpl.java ⭐⭐
**路径**: `src/main/java/com/hotel/service/`

**核心功能**:
- 客户业务逻辑处理
- 客户数据验证
- 手机号/身份证唯一性检查

**关键方法**:
```java
// 接口方法
Integer createCustomer(Customer customer)
Customer findCustomerById(Integer customerId)
List<Customer> findAllCustomers()
boolean updateCustomer(Customer customer)
boolean deleteCustomer(Integer customerId)
List<Customer> searchCustomersByName(String name)
List<Customer> findCustomersByVipLevel(Integer vipLevel)
boolean isPhoneExists(String phone)
boolean isIdCardExists(String idCard)
String validateCustomer(Customer customer)
```

**业务规则**:
- 手机号必须唯一
- 身份证号必须唯一
- VIP等级：0-5
- 姓名、手机号、身份证必填

---

### 2. RoomService.java / RoomServiceImpl.java ⭐⭐
**路径**: `src/main/java/com/hotel/service/`

**核心功能**:
- 房间业务逻辑
- 房间状态管理
- 可用房间查询

**关键方法**:
```java
Integer createRoom(Room room)
Room getRoomById(Integer roomId)
List<Room> getAllRooms()
List<Room> getAvailableRooms()
List<Room> getRoomsByType(Integer typeId)
List<Room> getRoomsByStatus(Room.RoomStatus status)
boolean updateRoom(Room room)
boolean updateRoomStatus(Integer roomId, Room.RoomStatus status)
boolean deleteRoom(Integer roomId)
```

**业务规则**:
- 房间号必须唯一
- 只有AVAILABLE状态的房间可以预订
- 入住时状态变为OCCUPIED
- 退房时状态变为AVAILABLE

---

### 3. BookingService.java / BookingServiceImpl.java ⭐⭐⭐
**路径**: `src/main/java/com/hotel/service/`

**核心功能**:
- 预订业务逻辑
- 价格计算（策略模式）
- 预订冲突检测
- 入住/退房处理

**关键方法**:
```java
Integer createBooking(Booking booking)
Booking getBookingById(Integer bookingId)
List<Booking> getAllBookings()
List<Booking> getBookingsByCustomer(Integer customerId)
List<Booking> getBookingsByStatus(Booking.BookingStatus status)
List<Booking> getTodayCheckIns()
List<Booking> getTodayCheckOuts()
boolean updateBooking(Booking booking)
boolean checkIn(Integer bookingId)
boolean checkOut(Integer bookingId)
boolean cancelBooking(Integer bookingId)
BigDecimal calculateTotalPrice(Booking booking)
boolean isRoomAvailable(Integer roomId, Date checkIn, Date checkOut)
```

**价格计算逻辑** (策略模式):
```java
public BigDecimal calculateTotalPrice(Booking booking) {
    // 1. 获取房间类型和价格
    RoomType roomType = ...;
    
    // 2. 计算天数
    long days = ...;
    
    // 3. 获取VIP等级
    int vipLevel = ...;
    
    // 4. 选择策略
    PriceCalculationStrategy strategy;
    if (vipLevel > 0) {
        strategy = new VIPPriceStrategy();
    } else {
        strategy = new RegularPriceStrategy();
    }
    
    // 5. 计算总价
    return strategy.calculatePrice(roomType, days, vipLevel);
}
```

---

### 4. UserService.java / UserServiceImpl.java
**路径**: `src/main/java/com/hotel/service/`

**核心功能**:
- 用户认证
- 密码验证
- 用户信息管理

**关键方法**:
```java
User authenticate(String username, String password)
Integer createUser(User user)
User findUserById(Integer userId)
List<User> findAllUsers()
boolean updateUser(User user)
boolean deleteUser(Integer userId)
boolean isUsernameExists(String username)
```

**密码处理**:
- 使用MD5加密存储
- 登录时MD5比对

---

### 5. ServiceFactory.java
**路径**: `src/main/java/com/hotel/service/ServiceFactory.java`

**设计模式**: 工厂模式

**核心功能**:
- 创建Service对象
- 解耦对象创建和使用

**代码示例**:
```java
public class ServiceFactory {
    public static CustomerService createCustomerService() {
        return new CustomerServiceImpl();
    }
    
    public static RoomService createRoomService() {
        return new RoomServiceImpl();
    }
    
    public static BookingService createBookingService() {
        return new BookingServiceImpl();
    }
    
    public static UserService createUserService() {
        return new UserServiceImpl();
    }
}
```

---

### 6. PriceCalculationStrategy.java (策略接口) ⭐⭐⭐
**路径**: `src/main/java/com/hotel/service/strategy/PriceCalculationStrategy.java`

**设计模式**: 策略模式

**核心功能**:
- 定义价格计算接口

**代码**:
```java
public interface PriceCalculationStrategy {
    BigDecimal calculatePrice(RoomType roomType, int days, int vipLevel);
}
```

---

### 7. RegularPriceStrategy.java
**路径**: `src/main/java/com/hotel/service/strategy/impl/RegularPriceStrategy.java`

**策略**: 普通客户价格（无折扣）

**代码**:
```java
public class RegularPriceStrategy implements PriceCalculationStrategy {
    @Override
    public BigDecimal calculatePrice(RoomType roomType, int days, int vipLevel) {
        return roomType.getPrice().multiply(new BigDecimal(days));
    }
}
```

---

### 8. VIPPriceStrategy.java
**路径**: `src/main/java/com/hotel/service/strategy/impl/VIPPriceStrategy.java`

**策略**: VIP客户价格（享受折扣）

**VIP折扣**:
- VIP1: 9.5折
- VIP2: 9折
- VIP3: 8.5折
- VIP4: 8折
- VIP5: 7.5折

**代码**:
```java
public class VIPPriceStrategy implements PriceCalculationStrategy {
    @Override
    public BigDecimal calculatePrice(RoomType roomType, int days, int vipLevel) {
        BigDecimal basePrice = roomType.getPrice().multiply(new BigDecimal(days));
        BigDecimal discount = getDiscountByVipLevel(vipLevel);
        return basePrice.multiply(discount);
    }
    
    private BigDecimal getDiscountByVipLevel(int vipLevel) {
        switch (vipLevel) {
            case 1: return new BigDecimal("0.95");
            case 2: return new BigDecimal("0.90");
            case 3: return new BigDecimal("0.85");
            case 4: return new BigDecimal("0.80");
            case 5: return new BigDecimal("0.75");
            default: return BigDecimal.ONE;
        }
    }
}
```

---

## 📂 三、DAO层 (11个文件)

### DAO接口 (5个)

#### 1. CustomerDAO.java
**核心方法**:
```java
Integer insert(Customer customer)
Customer findById(Integer customerId)
List<Customer> findAll()
boolean update(Customer customer)
boolean delete(Integer customerId)
List<Customer> findByName(String name)
List<Customer> findByVipLevel(Integer vipLevel)
boolean existsByPhone(String phone)
boolean existsByIdCard(String idCard)
```

#### 2. RoomDAO.java
**核心方法**:
```java
Integer insert(Room room)
Room findById(Integer roomId)
List<Room> findAll()
List<Room> findByStatus(Room.RoomStatus status)
List<Room> findByTypeId(Integer typeId)
boolean update(Room room)
boolean updateStatus(Integer roomId, Room.RoomStatus status)
boolean delete(Integer roomId)
boolean existsByRoomNumber(String roomNumber)
```

#### 3. RoomTypeDAO.java
**核心方法**:
```java
Integer insert(RoomType roomType)
RoomType findById(Integer typeId)
List<RoomType> findAll()
boolean update(RoomType roomType)
boolean delete(Integer typeId)
```

#### 4. BookingDAO.java
**核心方法**:
```java
Integer insert(Booking booking)
Booking findById(Integer bookingId)
List<Booking> findAll()
List<Booking> findByCustomerId(Integer customerId)
List<Booking> findByStatus(Booking.BookingStatus status)
List<Booking> findTodayCheckIns()
List<Booking> findTodayCheckOuts()
boolean update(Booking booking)
boolean delete(Integer bookingId)
List<Booking> findConflictingBookings(Integer roomId, Date checkIn, Date checkOut)
```

#### 5. UserDAO.java
**核心方法**:
```java
Integer insert(User user)
User findById(Integer userId)
User findByUsername(String username)
List<User> findAll()
boolean update(User user)
boolean delete(Integer userId)
boolean existsByUsername(String username)
```

---

### DAO实现 (5个)

所有DAO实现类都使用JDBC和PreparedStatement进行数据库操作。

**通用模式**:
```java
@Override
public Integer insert(Entity entity) {
    String sql = "INSERT INTO table_name (...) VALUES (?)";
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        // 设置参数
        pstmt.setXxx(1, entity.getXxx());
        
        // 执行
        pstmt.executeUpdate();
        
        // 获取生成的ID
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        logger.error("Insert failed", e);
    }
    return null;
}
```

---

### DAOFactory.java
**路径**: `src/main/java/com/hotel/dao/DAOFactory.java`

**设计模式**: 工厂模式

**代码**:
```java
public class DAOFactory {
    public static CustomerDAO createCustomerDAO() {
        return new CustomerDAOImpl();
    }
    
    public static RoomDAO createRoomDAO() {
        return new RoomDAOImpl();
    }
    
    public static RoomTypeDAO createRoomTypeDAO() {
        return new RoomTypeDAOImpl();
    }
    
    public static BookingDAO createBookingDAO() {
        return new BookingDAOImpl();
    }
    
    public static UserDAO createUserDAO() {
        return new UserDAOImpl();
    }
}
```

---

## 📂 四、Model层 (5个文件)

### 1. User.java
**实体**: 系统用户

**字段**:
```java
private Integer userId;          // 用户ID
private String username;         // 用户名
private String password;         // 密码（MD5加密）
private String realName;         // 真实姓名
private UserRole role;           // 角色（ADMIN/STAFF）
private String email;            // 邮箱
private String phone;            // 电话
private UserStatus status;       // 状态（ACTIVE/INACTIVE）
private Timestamp createdAt;     // 创建时间
private Timestamp updatedAt;     // 更新时间
```

**枚举**:
```java
public enum UserRole {
    ADMIN, STAFF
}

public enum UserStatus {
    ACTIVE, INACTIVE
}
```

---

### 2. Customer.java
**实体**: 客户信息

**字段**:
```java
private Integer customerId;      // 客户ID
private String name;             // 姓名
private String phone;            // 手机号（唯一）
private String email;            // 邮箱
private String idCard;           // 身份证号（唯一）
private String address;          // 地址
private Integer vipLevel;        // VIP等级（0-5）
private Timestamp createdAt;     // 创建时间
private Timestamp updatedAt;     // 更新时间
```

---

### 3. RoomType.java
**实体**: 房间类型

**字段**:
```java
private Integer typeId;          // 类型ID
private String typeName;         // 类型名称
private BigDecimal price;        // 价格（元/天）
private Integer bedCount;        // 床位数
private Integer maxGuests;       // 最大容纳人数
private String description;      // 描述
private String amenities;        // 设施列表
private Timestamp createdAt;     // 创建时间
private Timestamp updatedAt;     // 更新时间
```

---

### 4. Room.java
**实体**: 房间信息

**字段**:
```java
private Integer roomId;          // 房间ID
private String roomNumber;       // 房间号（唯一）
private Integer typeId;          // 房间类型ID
private Integer floor;           // 楼层
private RoomStatus status;       // 状态
private String description;      // 描述
private Timestamp createdAt;     // 创建时间
private Timestamp updatedAt;     // 更新时间
```

**枚举**:
```java
public enum RoomStatus {
    AVAILABLE("空闲"),
    BOOKED("已预订"),
    OCCUPIED("入住中"),
    MAINTENANCE("维护中");
}
```

---

### 5. Booking.java
**实体**: 预订信息

**字段**:
```java
private Integer bookingId;            // 预订ID
private Integer customerId;           // 客户ID
private Integer roomId;               // 房间ID
private Date checkInDate;             // 计划入住日期
private Date checkOutDate;            // 计划退房日期
private Timestamp actualCheckIn;      // 实际入住时间
private Timestamp actualCheckOut;     // 实际退房时间
private BigDecimal totalPrice;        // 总价
private BookingStatus status;         // 状态
private String specialRequests;       // 特殊要求
private Integer createdBy;            // 创建人ID
private Timestamp createdAt;          // 创建时间
private Timestamp updatedAt;          // 更新时间
```

**枚举**:
```java
public enum BookingStatus {
    PENDING("待确认"),
    CONFIRMED("已确认"),
    CHECKED_IN("已入住"),
    CHECKED_OUT("已退房"),
    CANCELLED("已取消");
}
```

---

## 📂 五、Util工具类 (4个文件)

### 1. ConnectionPool.java ⭐⭐⭐
**路径**: `src/main/java/com/hotel/util/ConnectionPool.java`

**设计模式**: 单例模式

**核心功能**:
- 数据库连接池管理
- 连接复用
- 线程安全

**关键方法**:
```java
public static ConnectionPool getInstance()  // 获取单例
public Connection getConnection()           // 获取连接
public void releaseConnection(Connection conn)  // 释放连接
```

**单例实现**:
```java
private static volatile ConnectionPool instance;

public static ConnectionPool getInstance() {
    if (instance == null) {
        synchronized (ConnectionPool.class) {
            if (instance == null) {
                instance = new ConnectionPool();
            }
        }
    }
    return instance;
}
```

---

### 2. ConfigManager.java
**路径**: `src/main/java/com/hotel/util/ConfigManager.java`

**设计模式**: 单例模式

**核心功能**:
- 读取配置文件（db.properties）
- 提供配置访问接口

**关键方法**:
```java
public static ConfigManager getInstance()
public String getProperty(String key)
public String getProperty(String key, String defaultValue)
```

---

### 3. PasswordUtil.java
**路径**: `src/main/java/com/hotel/util/PasswordUtil.java`

**核心功能**:
- 密码MD5加密
- 密码验证

**关键方法**:
```java
public static String encryptPassword(String password)
public static boolean verifyPassword(String inputPassword, String storedPassword)
```

**MD5加密示例**:
```java
public static String encryptPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(password.getBytes());
        return bytesToHex(bytes);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("MD5加密失败", e);
    }
}
```

---

### 4. Utils.java
**路径**: `src/main/java/com/hotel/util/Utils.java`

**核心功能**:
- 数据验证工具
- 字符串处理

**关键方法**:
```java
public static boolean isEmpty(String str)
public static boolean validatePhone(String phone)
public static boolean validateEmail(String email)
public static boolean validateIdCard(String idCard)
```

**验证正则表达式**:
```java
// 手机号验证
private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

// 邮箱验证
private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

// 身份证验证
private static final String IDCARD_REGEX = "^\\d{17}[\\dXx]$";
```

---

## 📂 六、Filter过滤器 (2个文件)

### 1. CharacterEncodingFilter.java
**路径**: `src/main/java/com/hotel/filter/CharacterEncodingFilter.java`

**核心功能**:
- 统一字符编码为UTF-8
- 处理中文乱码问题

**代码**:
```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
}
```

---

### 2. LoginFilter.java
**路径**: `src/main/java/com/hotel/filter/LoginFilter.java`

**核心功能**:
- 登录状态验证
- 拦截未登录用户访问管理页面

**代码**:
```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    
    HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    
    if (user == null) {
        // 未登录，重定向到登录页
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    } else {
        // 已登录，继续处理
        chain.doFilter(request, response);
    }
}
```

---

## 📊 项目统计

### 代码文件统计
- **Controller**: 8个文件
- **Service**: 8个文件（接口4个 + 实现4个）
- **DAO**: 11个文件（接口5个 + 实现5个 + 工厂1个）
- **Model**: 5个实体类
- **Util**: 4个工具类
- **Filter**: 2个过滤器

**总计**: 43个Java文件

### 代码行数统计
- **总代码量**: 3000+ 行Java代码
- **平均每文件**: 约70行

---

## 🎯 核心技术点

### 设计模式 (6种)
1. ⭐ **单例模式**: ConnectionPool, ConfigManager
2. ⭐ **工厂模式**: DAOFactory, ServiceFactory
3. ⭐ **策略模式**: PriceCalculationStrategy
4. ⭐ **模板方法模式**: BaseController
5. ⭐ **DAO模式**: 所有DAO
6. ⭐ **观察者模式**: 房间状态变更

### 核心业务
1. ⭐ **价格计算**: 策略模式 + VIP折扣
2. ⭐ **预订管理**: 冲突检测 + 状态流转
3. ⭐ **数据验证**: 前后端双重验证
4. ⭐ **权限控制**: RBAC + Filter拦截

### 性能优化
1. ⭐ **连接池**: 数据库连接复用
2. ⭐ **PreparedStatement**: SQL预编译
3. ⭐ **分页查询**: 减少数据加载
4. ⭐ **索引优化**: 提高查询效率

---

## 📚 学习建议

### 答辩重点
1. **BaseController**: 模板方法模式的实现
2. **BookingService**: 价格计算策略模式
3. **ConnectionPool**: 单例模式和连接池
4. **CustomerService**: 数据验证逻辑

### 代码阅读顺序
```
1. Model层（理解数据结构）
   ↓
2. DAO层（理解数据访问）
   ↓
3. Service层（理解业务逻辑）
   ↓
4. Controller层（理解请求处理）
   ↓
5. Util层（理解工具类）
   ↓
6. Filter层（理解过滤器）
```

### 关键流程
1. **登录流程**: LoginController → UserService → UserDAO
2. **创建预订**: BookingController → BookingService(价格计算) → BookingDAO + RoomDAO
3. **客户管理**: CustomerController → CustomerService(验证) → CustomerDAO

---

**祝你答辩顺利！** 🎉

有任何问题都可以参考本文档快速定位到相关代码文件进行查看。
