<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>预订详情 - 酒店管理系统</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
            </head>

            <body>
                <!-- 导航栏 -->
                <nav class="navbar navbar-expand-lg navbar-dark">
                    <div class="container-fluid">
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/index">
                            <i class="fas fa-hotel"></i> 酒店管理系统
                        </a>

                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>

                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav me-auto">
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/index">
                                        <i class="fas fa-tachometer-alt"></i> 首页
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">
                                        <i class="fas fa-users"></i> 客户管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
                                        <i class="fas fa-bed"></i> 房间管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">
                                        <i class="fas fa-calendar-check"></i> 预订管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">
                                        <i class="fas fa-user-cog"></i> 用户管理
                                    </a>
                                </li>
                            </ul>

                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                        <i class="fas fa-user"></i>
                                        <c:out value="${sessionScope.currentUser.realName}" default="${sessionScope.currentUser.username}" />
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/user/profile">个人资料</a></li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">退出登录</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid">
                    <div class="row">
                        <!-- 侧边栏 -->
                        <nav class="col-md-2 d-md-block sidebar">
                            <div class="position-sticky pt-3">
                                <ul class="nav flex-column">
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/index">
                                            <i class="fas fa-tachometer-alt"></i> 仪表板
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">
                                            <i class="fas fa-users"></i> 客户管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
                                            <i class="fas fa-bed"></i> 房间管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">
                                            <i class="fas fa-calendar-check"></i> 预订管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/today-checkins">
                                            <i class="fas fa-sign-in-alt"></i> 今日入住
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/today-checkouts">
                                            <i class="fas fa-sign-out-alt"></i> 今日退房
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">
                                            <i class="fas fa-user-cog"></i> 用户管理
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </nav>

                        <!-- 主要内容 -->
                        <main class="col-md-10 ms-sm-auto px-md-4">
                            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                                <h1 class="h2">
                                    <i class="fas fa-info-circle"></i> 预订详情 #${booking.bookingId}
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回列表
                                        </a>
                                        <c:if test="${booking.status == 'PENDING' || booking.status == 'CONFIRMED'}">
                                            <a href="${pageContext.request.contextPath}/admin/booking/edit?id=${booking.bookingId}" class="btn btn-warning">
                                                <i class="fas fa-edit"></i> 编辑
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>

                            <c:if test="${not empty booking}">
                                <!-- 预订基本信息 -->
                                <div class="row">
                                    <div class="col-md-8">
                                        <div class="card mb-4">
                                            <div class="card-header">
                                                <h5 class="card-title mb-0">
                                                    <i class="fas fa-calendar-check"></i> 预订信息
                                                </h5>
                                            </div>
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">预订编号:</td>
                                                                <td>${booking.bookingId}</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">入住日期:</td>
                                                                <td>
                                                                    <fmt:formatDate value="${booking.checkInDate}" pattern="yyyy年MM月dd日" />
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">退房日期:</td>
                                                                <td>
                                                                    <fmt:formatDate value="${booking.checkOutDate}" pattern="yyyy年MM月dd日" />
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">住宿天数:</td>
                                                                <td>${booking.stayDays} 天</td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">入住人数:</td>
                                                                <td>${booking.guestsCount} 人</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">总价格:</td>
                                                                <td class="text-success fw-bold">¥
                                                                    <fmt:formatNumber value="${booking.totalPrice}" pattern="#,##0.00" />
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">预订状态:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${booking.status == 'PENDING'}">
                                                                            <span class="badge bg-warning text-dark">待确认</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.status == 'CONFIRMED'}">
                                                                            <span class="badge bg-info">已确认</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.status == 'CHECKED_IN'}">
                                                                            <span class="badge bg-success">已入住</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.status == 'CHECKED_OUT'}">
                                                                            <span class="badge bg-secondary">已退房</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.status == 'CANCELLED'}">
                                                                            <span class="badge bg-danger">已取消</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-light text-dark">${booking.status}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">预订时间:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.bookingTime}">
                                                                            <fmt:formatDate value="${booking.bookingTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未记录</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>

                                                <c:if test="${not empty booking.specialRequests}">
                                                    <div class="mt-3">
                                                        <h6 class="fw-bold text-muted">特殊要求:</h6>
                                                        <div class="bg-light p-3 rounded">
                                                            <c:out value="${booking.specialRequests}" />
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-4">
                                        <!-- 状态操作面板 -->
                                        <div class="card mb-4">
                                            <div class="card-header">
                                                <h5 class="card-title mb-0">
                                                    <i class="fas fa-cogs"></i> 状态操作
                                                </h5>
                                            </div>
                                            <div class="card-body">
                                                <div class="d-grid gap-2">
                                                    <c:if test="${booking.status == 'PENDING'}">
                                                        <a href="${pageContext.request.contextPath}/admin/booking/confirm?id=${booking.bookingId}" class="btn btn-success" onclick="return confirm('确定要确认此预订吗？')">
                                                            <i class="fas fa-check"></i> 确认预订
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/admin/booking/cancel?id=${booking.bookingId}" class="btn btn-danger" onclick="return confirm('确定要取消此预订吗？')">
                                                            <i class="fas fa-times"></i> 取消预订
                                                        </a>
                                                    </c:if>

                                                    <c:if test="${booking.status == 'CONFIRMED'}">
                                                        <a href="${pageContext.request.contextPath}/admin/booking/checkin?id=${booking.bookingId}" class="btn btn-primary" onclick="return confirm('确定要办理入住吗？')">
                                                            <i class="fas fa-sign-in-alt"></i> 办理入住
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/admin/booking/cancel?id=${booking.bookingId}" class="btn btn-danger" onclick="return confirm('确定要取消此预订吗？')">
                                                            <i class="fas fa-times"></i> 取消预订
                                                        </a>
                                                    </c:if>

                                                    <c:if test="${booking.status == 'CHECKED_IN'}">
                                                        <a href="${pageContext.request.contextPath}/admin/booking/checkout?id=${booking.bookingId}" class="btn btn-warning" onclick="return confirm('确定要办理退房吗？')">
                                                            <i class="fas fa-sign-out-alt"></i> 办理退房
                                                        </a>
                                                    </c:if>

                                                    <c:if test="${booking.status == 'PENDING' || booking.status == 'CANCELLED'}">
                                                        <a href="${pageContext.request.contextPath}/admin/booking/delete?id=${booking.bookingId}" class="btn btn-outline-danger" onclick="return confirm('确定要删除此预订吗？删除后无法恢复！')">
                                                            <i class="fas fa-trash"></i> 删除预订
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- 客户信息 -->
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="card mb-4">
                                            <div class="card-header">
                                                <h5 class="card-title mb-0">
                                                    <i class="fas fa-user"></i> 客户信息
                                                </h5>
                                            </div>
                                            <div class="card-body">
                                                <c:choose>
                                                    <c:when test="${not empty booking.customer}">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">客户姓名:</td>
                                                                <td>
                                                                    ${booking.customer.name}
                                                                    <c:if test="${booking.customer.vip}">
                                                                        <span class="badge bg-warning text-dark ms-2">VIP</span>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">联系电话:</td>
                                                                <td>${booking.customer.phone}</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">身份证号:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.customer.idCard}">
                                                                            ${booking.customer.idCard}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未提供</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold text-muted">邮箱地址:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.customer.email}">
                                                                            ${booking.customer.email}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未提供</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="text-muted">客户ID: ${booking.customerId}</p>
                                                        <p class="text-warning">客户信息未关联，请联系管理员。</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="card mb-4">
                                            <div class="card-header">
                                                <h5 class="card-title mb-0">
                                                    <i class="fas fa-bed"></i> 房间信息
                                                </h5>
                                            </div>
                                            <div class="card-body">
                                                <c:choose>
                                                    <c:when test="${not empty booking.room}">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">房间号:</td>
                                                                <td class="fw-bold">${booking.room.roomNumber}</td>
                                                            </tr>
                                                            <c:if test="${not empty booking.room.roomType}">
                                                                <tr>
                                                                    <td class="fw-bold text-muted">房间类型:</td>
                                                                    <td>${booking.room.roomType.typeName}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="fw-bold text-muted">房间价格:</td>
                                                                    <td>¥
                                                                        <fmt:formatNumber value="${booking.room.roomType.price}" pattern="#,##0.00" />/晚</td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="fw-bold text-muted">房间描述:</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty booking.room.roomType.description}">
                                                                                ${booking.room.roomType.description}
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">暂无描述</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                            <tr>
                                                                <td class="fw-bold text-muted">房间状态:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${booking.room.status == 'AVAILABLE'}">
                                                                            <span class="badge bg-success">可用</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.room.status == 'OCCUPIED'}">
                                                                            <span class="badge bg-danger">已占用</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.room.status == 'MAINTENANCE'}">
                                                                            <span class="badge bg-warning text-dark">维护中</span>
                                                                        </c:when>
                                                                        <c:when test="${booking.room.status == 'CLEANING'}">
                                                                            <span class="badge bg-info">清洁中</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-light text-dark">${booking.room.status}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="text-muted">房间ID: ${booking.roomId}</p>
                                                        <p class="text-warning">房间信息未关联，请联系管理员。</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- 创建信息 -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="card">
                                            <div class="card-header">
                                                <h5 class="card-title mb-0">
                                                    <i class="fas fa-info"></i> 创建信息
                                                </h5>
                                            </div>
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">创建人:</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.creator}">
                                                                            ${booking.creator.realName} (${booking.creator.username})
                                                                        </c:when>
                                                                        <c:when test="${not empty booking.createdBy}">
                                                                            用户ID: ${booking.createdBy}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未记录</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <table class="table table-sm table-borderless">
                                                            <tr>
                                                                <td class="fw-bold text-muted">预订编号:</td>
                                                                <td class="font-monospace">#${booking.bookingId}</td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${empty booking}">
                                <div class="alert alert-warning" role="alert">
                                    <h4 class="alert-heading">预订不存在</h4>
                                    <p>抱歉，未找到指定的预订信息。</p>
                                    <hr>
                                    <p class="mb-0">
                                        <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-primary">
                                            <i class="fas fa-arrow-left"></i> 返回预订列表
                                        </a>
                                    </p>
                                </div>
                            </c:if>
                        </main>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="${pageContext.request.contextPath}/js/main.js"></script>
            </body>

            </html>