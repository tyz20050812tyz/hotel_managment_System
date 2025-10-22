# 过滤器模式 UML 图

本文档展示了酒店管理系统中过滤器模式的设计和实现。

---

## 1. 过滤器模式类图

### 1.1 核心过滤器类图

```mermaid
classDiagram
    class Filter {
        <<interface>>
        +init(FilterConfig) void
        +doFilter(ServletRequest, ServletResponse, FilterChain) void
        +destroy() void
    }
    
    class FilterConfig {
        <<interface>>
        +getFilterName() String
        +getInitParameter(String) String
        +getInitParameterNames() Enumeration
        +getServletContext() ServletContext
    }
    
    class FilterChain {
        <<interface>>
        +doFilter(ServletRequest, ServletResponse) void
    }
    
    class LoginFilter {
        -Logger logger
        -List~String~ EXCLUDE_URLS
        +init(FilterConfig) void
        +doFilter(ServletRequest, ServletResponse, FilterChain) void
        +destroy() void
        -isExcludedUrl(String) boolean
        -isAjaxRequest(HttpServletRequest) boolean
    }
    
    class CharacterEncodingFilter {
        -String encoding
        -boolean forceEncoding
        +init(FilterConfig) void
        +doFilter(ServletRequest, ServletResponse, FilterChain) void
        +destroy() void
    }
    
    class ServletRequest {
        <<interface>>
        +getParameter(String) String
        +setAttribute(String, Object) void
        +getAttribute(String) Object
    }
    
    class ServletResponse {
        <<interface>>
        +setCharacterEncoding(String) void
        +getWriter() PrintWriter
    }
    
    class HttpServletRequest {
        <<interface>>
        +getSession() HttpSession
        +getRequestURI() String
        +getContextPath() String
        +getHeader(String) String
    }
    
    class HttpServletResponse {
        <<interface>>
        +sendRedirect(String) void
        +setStatus(int) void
        +setContentType(String) void
    }
    
    Filter <|.. LoginFilter : implements
    Filter <|.. CharacterEncodingFilter : implements
    Filter --> FilterConfig : uses
    Filter --> FilterChain : uses
    Filter --> ServletRequest : uses
    Filter --> ServletResponse : uses
    ServletRequest <|-- HttpServletRequest : extends
    ServletResponse <|-- HttpServletResponse : extends
    LoginFilter --> HttpServletRequest : uses
    LoginFilter --> HttpServletResponse : uses
    CharacterEncodingFilter --> HttpServletRequest : uses
    CharacterEncodingFilter --> HttpServletResponse : uses
```

### 1.2 过滤器详细设计

```mermaid
classDiagram
    class LoginFilter {
        -Logger logger
        -EXCLUDE_URLS ["/login.jsp", "/login", "/css/", "/js/"]
        +init(FilterConfig) void
        +doFilter(ServletRequest, ServletResponse, FilterChain) void
        +destroy() void
        -isExcludedUrl(String path) boolean
        -isAjaxRequest(HttpServletRequest) boolean
    }
    
    class CharacterEncodingFilter {
        -String encoding
        -boolean forceEncoding
        +init(FilterConfig) void
        +doFilter(ServletRequest, ServletResponse, FilterChain) void
        +destroy() void
    }
    
    class HttpSession {
        <<interface>>
        +getAttribute(String) Object
        +setAttribute(String, Object) void
        +invalidate() void
    }
    
    class User {
        -Integer userId
        -String username
        -String password
        -UserRole role
        +isAdmin() boolean
        +isStaff() boolean
    }
    
    LoginFilter --> HttpSession : uses
    LoginFilter --> User : validates
    
    note for LoginFilter "职责:\n1. 验证用户登录状态\n2. 拦截未登录请求\n3. 处理排除URL\n4. 支持AJAX请求"
    note for CharacterEncodingFilter "职责:\n1. 统一设置字符编码\n2. 防止中文乱码\n3. 支持配置参数"
```

---

## 2. 过滤器链执行流程

### 2.1 过滤器链顺序图

```mermaid
sequenceDiagram
    participant C as Client
    participant CE as CharacterEncodingFilter
    participant LF as LoginFilter
    participant FC as FilterChain
    participant S as Servlet
    
    C->>CE: HTTP请求
    CE->>CE: 设置字符编码UTF-8
    CE->>LF: chain.doFilter()
    LF->>LF: 检查是否排除URL
    alt 排除的URL
        LF->>FC: chain.doFilter()
        FC->>S: 执行Servlet
    else 需要验证的URL
        LF->>LF: 检查Session中的User
        alt 用户已登录
            LF->>FC: chain.doFilter()
            FC->>S: 执行Servlet
        else 用户未登录
            alt AJAX请求
                LF->>C: 返回JSON错误(401)
            else 普通请求
                LF->>C: 重定向到登录页
            end
        end
    end
    S-->>C: 响应结果
```

### 2.2 登录验证过滤器流程图

```mermaid
graph TB
    A[请求到达LoginFilter] --> B{是否为排除URL?}
    B -->|是| C[放行请求]
    B -->|否| D{Session中是否有User?}
    D -->|有| E[用户已登录]
    D -->|无| F[用户未登录]
    E --> C
    F --> G{是否为AJAX请求?}
    G -->|是| H[返回JSON 401错误]
    G -->|否| I[保存原始URL到Session]
    I --> J[重定向到登录页面]
    C --> K[继续过滤器链]
    K --> L[执行Servlet]
    H --> M[结束]
    J --> M
    L --> M
```

### 2.3 字符编码过滤器流程图

```mermaid
graph TB
    A[请求到达CharacterEncodingFilter] --> B{请求编码是否为空?}
    B -->|是| C[设置请求编码为UTF-8]
    B -->|否| D{是否强制编码?}
    D -->|是| C
    D -->|否| E[保持原编码]
    C --> F{响应编码是否为空或ISO-8859-1?}
    E --> F
    F -->|是| G[设置响应编码为UTF-8]
    F -->|否| H{是否强制编码?}
    H -->|是| G
    H -->|否| I[保持原编码]
    G --> J[继续过滤器链]
    I --> J
    J --> K[结束]
```

---

## 3. 过滤器配置

### 3.1 Web.xml配置关系图

```mermaid
graph TB
    A[web.xml配置] --> B[CharacterEncodingFilter]
    A --> C[LoginFilter]
    
    B --> D[初始化参数:<br/>encoding=UTF-8<br/>forceEncoding=true]
    B --> E[URL映射: /*]
    
    C --> F[URL映射: /*]
    
    E --> G[过滤器链执行顺序]
    F --> G
    
    G --> H[1. CharacterEncodingFilter<br/>2. LoginFilter<br/>3. 目标Servlet]
    
    style A fill:#e1f5ff
    style G fill:#fff4e1
    style H fill:#e8f5e9
```

### 3.2 过滤器与Servlet映射关系

```mermaid
graph LR
    subgraph 过滤器层
        A[CharacterEncodingFilter]
        B[LoginFilter]
    end
    
    subgraph Servlet层
        C[LoginController]
        D[BookingController]
        E[CustomerController]
        F[RoomController]
        G[UserController]
    end
    
    A --> B
    B --> C
    B --> D
    B --> E
    B --> F
    B --> G
    
    style A fill:#bbdefb
    style B fill:#c5e1a5
    style C fill:#fff9c4
    style D fill:#fff9c4
    style E fill:#fff9c4
    style F fill:#fff9c4
    style G fill:#fff9c4
```

---

## 4. 过滤器模式优势

### 4.1 设计优势图

```mermaid
mindmap
    root((过滤器模式))
        职责分离
            字符编码统一处理
            登录验证集中管理
            跨域请求处理
        可扩展性
            易于添加新过滤器
            过滤器可独立开发
            不影响业务逻辑
        可维护性
            单一职责原则
            代码重用
            配置灵活
        性能优化
            预处理请求
            统一异常处理
            减少重复代码
```

### 4.2 过滤器责任链模式

```mermaid
graph LR
    A[请求] --> B[过滤器1:<br/>CharacterEncodingFilter]
    B --> C[过滤器2:<br/>LoginFilter]
    C --> D[过滤器3:<br/>自定义过滤器...]
    D --> E[目标Servlet]
    E --> F[响应]
    
    F -.反向处理.-> D
    D -.反向处理.-> C
    C -.反向处理.-> B
    B -.反向处理.-> A
    
    style B fill:#e3f2fd
    style C fill:#f3e5f5
    style D fill:#e8f5e9
    style E fill:#fff3e0
```

---

## 5. 实际应用场景

### 5.1 登录验证场景

```mermaid
sequenceDiagram
    participant U as 用户
    participant B as 浏览器
    participant LF as LoginFilter
    participant S as BookingController
    participant DB as 数据库
    
    U->>B: 访问预订管理页面
    B->>LF: GET /admin/booking/list
    LF->>LF: 检查Session
    alt 未登录
        LF->>B: 302重定向到/login.jsp
        B->>U: 显示登录页面
        U->>B: 输入用户名密码
        B->>S: POST /login
        S->>DB: 验证用户
        DB-->>S: 返回用户信息
        S->>S: 创建Session
        S->>B: 重定向到原始URL
        B->>LF: GET /admin/booking/list
        LF->>LF: Session中有User
        LF->>S: 继续请求
        S-->>B: 返回预订列表
        B-->>U: 显示页面
    else 已登录
        LF->>S: 继续请求
        S-->>B: 返回预订列表
        B-->>U: 显示页面
    end
```

### 5.2 字符编码处理场景

```mermaid
sequenceDiagram
    participant C as Client
    participant CE as CharacterEncodingFilter
    participant S as Servlet
    
    C->>CE: 提交表单(含中文)
    CE->>CE: 设置request编码为UTF-8
    CE->>CE: 设置response编码为UTF-8
    CE->>S: 转发请求
    S->>S: 处理中文数据
    Note over S: 可以正确读取中文参数
    S-->>CE: 返回响应(含中文)
    CE-->>C: 返回UTF-8编码的响应
    Note over C: 浏览器正确显示中文
```

---

## 6. 过滤器生命周期

```mermaid
stateDiagram-v2
    [*] --> 创建: Web容器启动
    创建 --> 初始化: 调用init()
    初始化 --> 就绪: 初始化完成
    就绪 --> 过滤: 接收请求
    过滤 --> 就绪: 请求处理完成
    就绪 --> 销毁: Web容器关闭
    销毁 --> [*]: 调用destroy()
    
    note right of 初始化
        读取配置参数
        初始化资源
        设置日志
    end note
    
    note right of 过滤
        执行doFilter()
        处理请求/响应
        调用chain.doFilter()
    end note
    
    note right of 销毁
        释放资源
        清理缓存
        关闭连接
    end note
```

---

## 7. 总结

### 7.1 过滤器模式的关键特点

1. **责任链模式**：多个过滤器形成链式结构，按顺序执行
2. **可插拔性**：可以灵活添加或移除过滤器
3. **透明性**：对业务逻辑透明，不影响Servlet代码
4. **双向处理**：可以在请求前和响应后都进行处理

### 7.2 本项目中的过滤器应用

| 过滤器 | 职责 | 执行顺序 |
|--------|------|----------|
| CharacterEncodingFilter | 统一字符编码为UTF-8 | 1 |
| LoginFilter | 验证用户登录状态 | 2 |

### 7.3 过滤器模式 vs 拦截器模式

```mermaid
graph TB
    subgraph 过滤器Filter
        A1[Servlet规范]
        A2[容器级别]
        A3[处理所有请求]
        A4[粗粒度控制]
    end
    
    subgraph 拦截器Interceptor
        B1[Spring框架]
        B2[框架级别]
        B3[只处理Controller]
        B4[细粒度控制]
    end
    
    style A1 fill:#e1f5ff
    style A2 fill:#e1f5ff
    style A3 fill:#e1f5ff
    style A4 fill:#e1f5ff
    style B1 fill:#f3e5f5
    style B2 fill:#f3e5f5
    style B3 fill:#f3e5f5
    style B4 fill:#f3e5f5
```

---

## 附录：相关文件

- **LoginFilter实现**: `src/main/java/com/hotel/filter/LoginFilter.java`
- **CharacterEncodingFilter实现**: `src/main/java/com/hotel/filter/CharacterEncodingFilter.java`
- **配置文件**: `src/main/webapp/WEB-INF/web.xml`
