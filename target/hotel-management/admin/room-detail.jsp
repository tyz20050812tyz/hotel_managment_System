<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>房间详情 - 酒店管理系统</title>
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/room/list">
                                        <i class="fas fa-bed"></i> 房间管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
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
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/admin/room/list">
                                            <i class="fas fa-bed"></i> 房间管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/type/list">
                                            <i class="fas fa-list"></i> 房间类型
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
                                            <i class="fas fa-calendar-check"></i> 预订管理
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
                                    <i class="fas fa-bed"></i> 房间详情
                                    <c:if test="${not empty room}"> - ${room.roomNumber}</c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/room/list" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回列表
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/room/edit?id=${room.roomId}" class="btn btn-warning">
                                            <i class="fas fa-edit"></i> 编辑房间
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <!-- 房间详情内容 -->
                            <c:choose>
                                <c:when test="${empty room}">
                                    <div class="alert alert-danger" role="alert">
                                        <i class="fas fa-exclamation-triangle"></i> 房间信息不存在或已被删除
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="row">
                                        <!-- 基本信息 -->
                                        <div class="col-md-6 mb-4">
                                            <div class="card">
                                                <div class="card-header">
                                                    <h5 class="card-title mb-0">
                                                        <i class="fas fa-info-circle"></i> 基本信息
                                                    </h5>
                                                </div>
                                                <div class="card-body">
                                                    <table class="table table-borderless">
                                                        <tr>
                                                            <td class="fw-bold text-muted">房间ID:</td>
                                                            <td>${room.roomId}</td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">房间号:</td>
                                                            <td><strong class="fs-5">${room.roomNumber}</strong></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">楼层:</td>
                                                            <td>${room.floor}楼</td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">房间类型:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty room.roomType}">
                                                                        <span class="badge bg-primary">${room.roomType.typeName}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">未知类型</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">当前状态:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${room.status == 'AVAILABLE'}">
                                                                        <span class="badge bg-success fs-6">可用</span>
                                                                    </c:when>
                                                                    <c:when test="${room.status == 'OCCUPIED'}">
                                                                        <span class="badge bg-danger fs-6">已入住</span>
                                                                    </c:when>
                                                                    <c:when test="${room.status == 'CLEANING'}">
                                                                        <span class="badge bg-warning text-dark fs-6">清洁中</span>
                                                                    </c:when>
                                                                    <c:when test="${room.status == 'MAINTENANCE'}">
                                                                        <span class="badge bg-info fs-6">维护中</span>
                                                                    </c:when>

                                                                    <c:otherwise>
                                                                        <span class="badge bg-secondary fs-6">${room.status}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">最后清洁:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty room.lastCleaned}">
                                                                        <fmt:formatDate value="${room.lastCleaned}" pattern="yyyy-MM-dd HH:mm" />
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
                                        </div>

                                        <!-- 房间类型详情 -->
                                        <div class="col-md-6 mb-4">
                                            <div class="card">
                                                <div class="card-header">
                                                    <h5 class="card-title mb-0">
                                                        <i class="fas fa-home"></i> 房间类型详情
                                                    </h5>
                                                </div>
                                                <div class="card-body">
                                                    <c:choose>
                                                        <c:when test="${not empty room.roomType}">
                                                            <table class="table table-borderless">
                                                                <tr>
                                                                    <td class="fw-bold text-muted">类型名称:</td>
                                                                    <td>${room.roomType.typeName}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="fw-bold text-muted">价格:</td>
                                                                    <td>
                                                                        <strong class="text-success fs-5">
                                                                ¥<fmt:formatNumber value="${room.roomType.price}" pattern="#,##0.00"/>
                                                            </strong>
                                                                        <small class="text-muted">/晚</small>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="fw-bold text-muted">床位数:</td>
                                                                    <td>${room.roomType.bedCount}张床</td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="fw-bold text-muted">最大入住人数:</td>
                                                                    <td>${room.roomType.maxGuests}人</td>
                                                                </tr>

                                                                <tr>
                                                                    <td class="fw-bold text-muted">类型描述:</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty room.roomType.description}">
                                                                                ${room.roomType.description}
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">暂无描述</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-center py-4">
                                                                <i class="fas fa-exclamation-triangle fa-2x text-warning mb-2"></i>
                                                                <p class="text-muted">房间类型信息缺失</p>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 状态管理操作 -->
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="card">
                                                <div class="card-header">
                                                    <h5 class="card-title mb-0">
                                                        <i class="fas fa-cogs"></i> 状态管理
                                                    </h5>
                                                </div>
                                                <div class="card-body">
                                                    <div class="row g-2">
                                                        <div class="col-md-2">
                                                            <button type="button" class="btn btn-success w-100" onclick="changeRoomStatus('AVAILABLE')" ${room.status=='AVAILABLE' ? 'disabled' : ''}>
                                                    <i class="fas fa-check"></i> 设为可用
                                                </button>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <button type="button" class="btn btn-danger w-100" onclick="changeRoomStatus('OCCUPIED')" ${room.status=='OCCUPIED' ? 'disabled' : ''}>
                                                    <i class="fas fa-user"></i> 设为入住
                                                </button>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <button type="button" class="btn btn-warning w-100" onclick="changeRoomStatus('CLEANING')" ${room.status=='CLEANING' ? 'disabled' : ''}>
                                                    <i class="fas fa-broom"></i> 开始清洁
                                                </button>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <button type="button" class="btn btn-info w-100" onclick="changeRoomStatus('MAINTENANCE')" ${room.status=='MAINTENANCE' ? 'disabled' : ''}>
                                                    <i class="fas fa-tools"></i> 开始维护
                                                </button>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </main>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="${pageContext.request.contextPath}/js/main.js"></script>
                <script>
                    // 页面初始化
                    document.addEventListener('DOMContentLoaded', function() {
                        HotelManagement.init('${pageContext.request.contextPath}');
                    });

                    // 更改房间状态
                    function changeRoomStatus(status) {
                        if (confirm('确定要更改房间状态吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/status/change?id=${room.roomId}&status=' + status;
                        }
                    }
                </script>
            </body>

            </html>