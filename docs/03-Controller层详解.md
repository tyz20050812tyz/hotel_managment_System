# Controller层详解

## 1. Controller层概述

### 1.1 层次定位
Controller层位于表现层和业务层之间，是MVC架构中的C（Controller），负责接收用户请求、调用业务逻辑、控制页面流转。

### 1.2 技术实现
- 基于Servlet 4.0
- 继承HttpServlet
- 使用Filter进行统一处理

### 1.3 Controller类清单

| 类名 | 路径 | 说明 |
|------|------|------|
| BaseController | com.hotel.controller | 基础抽象类（模板方法模式） |
| LoginController | com.hotel.controller | 登录控制器 |
| LogoutController | com.hotel.controller | 登出控制器 |
| IndexController | com.hotel.controller | 首页和统计控制器 |
| CustomerController | com.hotel.controller | 客户管理控制器 |
| RoomController | com.hotel.controller | 房间管理控制器 |
| BookingController | com.hotel.controller | 预订管理控制器 |
| UserController | com.hotel.controller | 用户管理控制器 |

## 2. BaseController - 基础控制器（核心）

### 2.1 类说明
`BaseController`是所有Controller的抽象基类，使用**模板方法模式**定义了统一的请求处理流程。

**文件位置**: `src/main/java/com/hotel/controller/BaseController.java`

### 2.2 设计模式：模板方法模式

**核心思想**: 在基类中定义算法的骨架，将某些步骤的实现延迟到子类。

**请求处理流程**:
```
processRequest() [模板方法]
    ├── 1. 设置字符编码
    ├── 2. validateParameters() [钩子方法]
    │       └── 子类可选择性重写
    ├── 3. checkPermission() [钩子方法]
    │       └── 子类可选择性重写
    ├── 4. handleBusinessLogic() [抽象方法]
    │       └── 子类必须实现
    ├── 5. handleResult() [钩子方法]
    │       └── 统一结果处理
    └── 6. handleException() [钩子方法]
            └── 统一异常处理
```

### 2.3 核心方法

#### 2.3.1 模板方法
```java
private void processRequest(HttpServletRequest request, 
                            HttpServletResponse response) {
    // 1. 设置编码
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    try {
        // 2. 参数验证
        if (!validateParameters(request)) {
            handleValidationError(request, response);
            return;
        }
        
        // 3. 权限检查
        if (!checkPermission(request)) {
            handlePermissionError(request, response);
            return;
        }
        
        // 4. 业务处理（由子类实现）
        String result = handleBusinessLogic(request, response);
        
        // 5. 结果处理
        handleResult(request, response, result);
        
    } catch (Exception e) {
        logger.error("Error processing request", e);
        handleException(request, response, e);
    }
}
```

**优势**:
- 统一请求处理流程
- 减少重复代码
- 易于维护和扩展

#### 2.3.2 抽象方法（子类必须实现）
```java
protected abstract String handleBusinessLogic(
    HttpServletRequest request, 
    HttpServletResponse response) throws Exception;
```

**说明**: 子类实现具体的业务逻辑，返回值控制页面跳转。

**返回值规则**:
- `"redirect:/path"` - 重定向到指定路径
- `"forward:/path"` - 转发到指定路径
- `"json:jsonData"` - 返回JSON数据
- `"/path.jsp"` - 转发到JSP页面

#### 2.3.3 钩子方法（子类可选择性重写）

**validateParameters() - 参数验证**
```java
protected boolean validateParameters(HttpServletRequest request) {
    return true; // 默认通过，子类可重写
}
```

**checkPermission() - 权限检查**
```java
protected boolean checkPermission(HttpServletRequest request) {
    return true; // 默认通过，子类可重写
}
```

### 2.4 工具方法

#### 2.4.1 用户相关
```java
// 获取当前登录用户
protected User getCurrentUser(HttpServletRequest request)

// 检查是否已登录
protected boolean isUserLoggedIn(HttpServletRequest request)

// 检查是否为管理员
protected boolean isAdmin(HttpServletRequest request)
```

#### 2.4.2 参数获取
```java
// 安全获取字符串参数（自动trim和空值处理）
protected String getParameter(HttpServletRequest request, String name)

// 获取整数参数（带默认值）
protected int getIntParameter(HttpServletRequest request, 
                             String name, int defaultValue)
```

#### 2.4.3 JSON响应
```java
// 写入JSON响应
protected void writeJsonResponse(HttpServletResponse response, 
                                 String jsonData)

// 创建成功响应
protected String createSuccessResponse(Object data)

// 创建错误响应
protected String createErrorResponse(String message, int code)
```

### 2.5 响应结果类
```java
public static class ResponseResult {
    private boolean success;  // 是否成功
    private String message;   // 消息
    private Object data;      // 数据
    private int code;         // 状态码
}
```

## 3. LoginController - 登录控制器

### 3.1 功能说明
处理用户登录和身份认证。

**文件位置**: `src/main/java/com/hotel/controller/LoginController.java`

**URL映射**: `/login`

### 3.2 请求处理

#### GET请求 - 显示登录页面
```java
private String handleGetRequest(HttpServletRequest request, 
                                HttpServletResponse response) {
    // 检查是否已登录
    if (isUserLoggedIn(request)) {
        return "redirect:/admin/index.jsp";
    }
    return "forward:/login.jsp";
}
```

#### POST请求 - 执行登录
```java
private String handlePostRequest(HttpServletRequest request, 
                                 HttpServletResponse response) {
    String username = getParameter(request, "username");
    String password = getParameter(request, "password");
    
    // 调用Service层验证
    User user = userService.authenticate(username, password);
    
    if (user != null) {
        // 登录成功
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60); // 30分钟
        
        logger.info("User logged in: {}", user.getUsername());
        return "redirect:/admin/index";
    } else {
        // 登录失败
        request.setAttribute("error", "用户名或密码错误");
        return "forward:/login.jsp";
    }
}
```

### 3.3 参数验证
```java
@Override
protected boolean validateParameters(HttpServletRequest request) {
    if ("POST".equals(request.getMethod())) {
        String username = getParameter(request, "username");
        String password = getParameter(request, "password");
        
        if (username.isEmpty()) {
            request.setAttribute("error", "请输入用户名");
            return false;
        }
        
        if (password.isEmpty()) {
            request.setAttribute("error", "请输入密码");
            return false;
        }
    }
    return true;
}
```

### 3.4 登录流程图
```
用户访问 /login (GET)
    ↓
检查是否已登录
    ├── 是 → 重定向到首页
    └── 否 → 显示登录页面
             ↓
        用户提交表单 (POST)
             ↓
        参数验证
             ↓
        调用UserService.authenticate()
             ├── 成功 → 创建Session → 重定向到首页
             └── 失败 → 显示错误信息 → 返回登录页
```

## 4. LogoutController - 登出控制器

### 4.1 功能说明
处理用户登出，销毁会话。

**文件位置**: `src/main/java/com/hotel/controller/LogoutController.java`

**URL映射**: `/logout`

### 4.2 核心代码
```java
@Override
protected String handleBusinessLogic(HttpServletRequest request, 
                                     HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    
    if (session != null) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("User logged out: {}", user.getUsername());
        }
        session.invalidate(); // 销毁会话
    }
    
    return "redirect:/login.jsp";
}
```

### 4.3 登出流程
```
用户点击登出按钮
    ↓
发送请求到 /logout
    ↓
获取当前Session
    ↓
记录日志
    ↓
销毁Session
    ↓
重定向到登录页
```

## 5. IndexController - 首页控制器

### 5.1 功能说明
处理首页展示和统计数据展示。

**文件位置**: `src/main/java/com/hotel/controller/IndexController.java`

**URL映射**: `/admin/index`

### 5.2 统计数据

#### 5.2.1 基础统计
```java
private Map<String, Object> getStatistics() throws Exception {
    Map<String, Object> stats = new HashMap<>();
    
    // 用户统计
    stats.put("totalUsers", allUsers.size());
    stats.put("adminUsers", 管理员数量);
    
    // 客户统计
    stats.put("totalCustomers", allCustomers.size());
    stats.put("vipCustomers", VIP客户数量);
    
    // 房间统计
    stats.put("totalRooms", allRooms.size());
    stats.put("availableRooms", 可用房间数);
    stats.put("occupiedRooms", 已入住房间数);
    stats.put("occupancyRate", 入住率);
    
    // 预订统计
    stats.put("totalBookings", 总预订数);
    stats.put("confirmedBookings", 已确认预订数);
    stats.put("checkedInBookings", 已入住预订数);
    stats.put("todayCheckIns", 今日入住数);
    stats.put("todayCheckOuts", 今日退房数);
    
    return stats;
}
```

#### 5.2.2 入住率计算
```java
BigDecimal occupancyRate = BigDecimal.valueOf(已入住房间数)
    .divide(BigDecimal.valueOf(总房间数), 4, RoundingMode.HALF_UP)
    .multiply(BigDecimal.valueOf(100));
```

#### 5.2.3 房间状态统计
```java
private Map<String, Long> getRoomStatusStatistics() {
    Map<String, Long> stats = new HashMap<>();
    
    for (Room.RoomStatus status : Room.RoomStatus.values()) {
        long count = 统计该状态的房间数;
        stats.put(status.name(), count);
    }
    
    return stats;
}
```

#### 5.2.4 VIP等级统计
```java
private Map<String, Long> getVipLevelStatistics() {
    Map<String, Long> stats = new HashMap<>();
    
    stats.put("REGULAR", 普通客户数);
    stats.put("VIP1", VIP1级别客户数);
    stats.put("VIP2", VIP2级别客户数);
    // ... VIP3-VIP5
    
    return stats;
}
```

### 5.3 数据展示
控制器将统计数据存入request属性，由JSP页面展示：
```java
request.setAttribute("statistics", statistics);
request.setAttribute("todayCheckIns", todayCheckIns);
request.setAttribute("todayCheckOuts", todayCheckOuts);
request.setAttribute("roomStatusStats", roomStatusStats);
request.setAttribute("vipLevelStats", vipLevelStats);
```

## 6. CustomerController - 客户管理控制器

### 6.1 功能说明
处理客户信息的增删改查操作。

**文件位置**: `src/main/java/com/hotel/controller/CustomerController.java`

**URL映射**: `/admin/customer/*`

### 6.2 URL路由

| URL路径 | HTTP方法 | 功能说明 |
|---------|----------|----------|
| /admin/customer/list | GET | 客户列表（分页、搜索） |
| /admin/customer/add | GET | 显示添加表单 |
| /admin/customer/edit?id=X | GET | 显示编辑表单 |
| /admin/customer/detail?id=X | GET | 显示客户详情 |
| /admin/customer/save | POST | 保存客户（新增/更新） |
| /admin/customer/delete | POST | 删除客户 |

### 6.3 核心功能实现

#### 6.3.1 客户列表（带分页和搜索）
```java
private String listCustomers(HttpServletRequest request) {
    // 获取分页参数
    int page = getIntParameter(request, "page", 1);
    int size = getIntParameter(request, "size", 10);
    String search = getParameter(request, "search");
    String vipLevel = getParameter(request, "vipLevel");
    
    // 查询客户
    List<Customer> customers;
    if (!Utils.isEmpty(search)) {
        customers = customerService.searchCustomersByName(search);
    } else if (!Utils.isEmpty(vipLevel)) {
        customers = customerService.findCustomersByVipLevel(vipLevel);
    } else {
        customers = customerService.findAllCustomers();
    }
    
    // 分页处理
    int total = customers.size();
    int startIndex = (page - 1) * size;
    int endIndex = Math.min(startIndex + size, total);
    customers = customers.subList(startIndex, endIndex);
    
    // 设置属性
    request.setAttribute("customers", customers);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", (total + size - 1) / size);
    
    return "/admin/customer-list.jsp";
}
```

#### 6.3.2 保存客户（新增/更新）
```java
private String saveCustomer(HttpServletRequest request) {
    // 获取参数
    String idStr = getParameter(request, "customerId");
    String name = getParameter(request, "name");
    String phone = getParameter(request, "phone");
    String email = getParameter(request, "email");
    String idCard = getParameter(request, "idCard");
    String address = getParameter(request, "address");
    String vipLevelStr = getParameter(request, "vipLevel");
    
    // 参数验证
    if (Utils.isEmpty(name)) {
        request.setAttribute("errorMessage", "姓名不能为空");
        return showAddForm(request);
    }
    
    if (!Utils.validatePhone(phone)) {
        request.setAttribute("errorMessage", "手机号格式不正确");
        return showAddForm(request);
    }
    
    if (!Utils.validateIdCard(idCard)) {
        request.setAttribute("errorMessage", "身份证号格式不正确");
        return showAddForm(request);
    }
    
    // 构建对象
    Customer customer = new Customer();
    customer.setName(name);
    customer.setPhone(phone);
    customer.setEmail(email);
    customer.setIdCard(idCard);
    customer.setAddress(address);
    customer.setVipLevel(Integer.parseInt(vipLevelStr));
    
    // 保存
    boolean success;
    if (!Utils.isEmpty(idStr)) {
        // 更新
        customer.setCustomerId(Integer.parseInt(idStr));
        success = customerService.updateCustomer(customer);
    } else {
        // 新增
        Integer customerId = customerService.createCustomer(customer);
        success = (customerId != null);
    }
    
    if (success) {
        return "redirect:/admin/customer/list";
    } else {
        request.setAttribute("errorMessage", "保存失败");
        return showAddForm(request);
    }
}
```

#### 6.3.3 数据验证规则
```
1. 姓名：必填，最长100字符
2. 手机号：必填，11位数字，格式：13X/14X/15X/17X/18X/19X
3. 身份证号：必填，18位，符合中国居民身份证规范
4. 邮箱：选填，符合邮箱格式
5. VIP等级：0-5的整数
```

#### 6.3.4 删除客户
```java
private String deleteCustomer(HttpServletRequest request) {
    String idStr = getParameter(request, "id");
    
    if (Utils.isEmpty(idStr)) {
        return "json:" + createErrorResponse("客户ID不能为空", 400);
    }
    
    int customerId = Integer.parseInt(idStr);
    boolean success = customerService.deleteCustomer(customerId);
    
    if (success) {
        return "json:" + createSuccessResponse("删除成功");
    } else {
        return "json:" + createErrorResponse("删除失败", 500);
    }
}
```

### 6.4 客户详情页
```java
private String showCustomerDetail(HttpServletRequest request) {
    String idStr = getParameter(request, "id");
    int customerId = Integer.parseInt(idStr);
    
    Customer customer = customerService.findCustomerById(customerId);
    
    // 计算注册天数
    if (customer.getCreateTime() != null) {
        long daysDiff = (当前时间 - 注册时间) / (1000 * 60 * 60 * 24);
        request.setAttribute("registrationDays", daysDiff);
    }
    
    request.setAttribute("customer", customer);
    return "/admin/customer-detail.jsp";
}
```

## 7. Controller层最佳实践

### 7.1 职责划分
✅ **应该做**:
- 接收和验证请求参数
- 调用Service层方法
- 控制页面跳转
- 设置响应数据

❌ **不应该做**:
- 包含业务逻辑
- 直接操作数据库
- 复杂的数据处理
- 包含SQL语句

### 7.2 参数验证
- 前端验证（用户体验）
- 后端验证（安全保障）
- 使用工具类统一验证规则

### 7.3 异常处理
- Controller层捕获异常
- 记录详细日志
- 返回友好错误信息
- 区分用户错误和系统错误

### 7.4 日志记录
```java
// 关键操作记录INFO级别日志
logger.info("User {} created customer: {}", user.getUsername(), customer.getName());

// 异常记录ERROR级别日志
logger.error("Failed to save customer", e);

// 调试信息记录DEBUG级别日志
logger.debug("Customer validation result: {}", result);
```

### 7.5 返回值规范
```java
// 成功操作 - 重定向到列表页
return "redirect:/admin/customer/list";

// 失败操作 - 转发回表单页
return "forward:/admin/customer-add.jsp";

// AJAX请求 - 返回JSON
return "json:" + createSuccessResponse(data);
```

## 8. Controller配置（web.xml）

```xml
<!-- LoginController -->
<servlet>
    <servlet-name>LoginController</servlet-name>
    <servlet-class>com.hotel.controller.LoginController</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>LoginController</servlet-name>
    <url-pattern>/login</url-pattern>
</servlet-mapping>

<!-- CustomerController -->
<servlet>
    <servlet-name>CustomerController</servlet-name>
    <servlet-class>com.hotel.controller.CustomerController</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>CustomerController</servlet-name>
    <url-pattern>/admin/customer/*</url-pattern>
</servlet-mapping>
```

## 9. 总结

### 9.1 Controller层优势
1. **统一的请求处理流程**: BaseController模板方法
2. **清晰的职责划分**: 只负责请求转发
3. **完善的异常处理**: 统一捕获和处理
4. **灵活的返回方式**: 支持重定向、转发、JSON
5. **便于测试**: 业务逻辑在Service层

### 9.2 设计模式应用
- **模板方法模式**: BaseController
- **工厂模式**: 创建Service对象

### 9.3 安全特性
- 参数验证
- 权限检查
- SQL注入防护
- XSS防护
