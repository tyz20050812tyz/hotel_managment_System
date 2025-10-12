<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>酒店管理系统 - 首页</title>
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/index">
                                        <i class="fas fa-home"></i> 首页
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
                                        <i class="fas fa-calendar-check"></i> 预订管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
                                        <i class="fas fa-bed"></i> 房间管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">
                                        <i class="fas fa-users"></i> 客户管理
                                    </a>
                                </li>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">
                                            <i class="fas fa-user-cog"></i> 用户管理
                                        </a>
                                    </li>
                                </c:if>
                            </ul>

                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                        <i class="fas fa-user-circle"></i> ${sessionScope.user.realName}
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/user/change-password">
                                                <i class="fas fa-key"></i> 修改密码
                                            </a>
                                        </li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                                <i class="fas fa-sign-out-alt"></i> 退出登录
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid mt-4">
                    <div class="row">
                        <!-- 侧边栏 -->
                        <div class="col-md-3 col-lg-2">
                            <div class="sidebar">
                                <h6 class="text-muted mb-3">快速导航</h6>
                                <nav class="nav nav-pills flex-column">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/index">
                                        <i class="fas fa-tachometer-alt"></i> 仪表板
                                    </a>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/add">
                                        <i class="fas fa-plus-circle"></i> 新建预订
                                    </a>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/add">
                                        <i class="fas fa-user-plus"></i> 添加客户
                                    </a>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
                                        <i class="fas fa-list"></i> 房间状态
                                    </a>
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/user/add">
                                            <i class="fas fa-user-plus"></i> 添加用户
                                        </a>
                                    </c:if>
                                </nav>
                            </div>
                        </div>

                        <!-- 主要内容 -->
                        <div class="col-md-9 col-lg-10">
                            <!-- 欢迎信息 -->
                            <div class="row mb-4">
                                <div class="col">
                                    <div class="card">
                                        <div class="card-body">
                                            <h5 class="card-title">
                                                <i class="fas fa-sun text-warning"></i> 欢迎回来，${sessionScope.user.realName}！
                                            </h5>
                                            <p class="card-text text-muted">
                                                <i class="fas fa-calendar"></i> 今天是
                                                <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy年MM月dd日 EEEE" />
                                                <span class="ms-3">
                                        <i class="fas fa-clock"></i>
                                        <span id="currentTime"></span>
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 统计卡片 -->
                            <div class="row mb-4">
                                <div class="col-md-3 mb-3">
                                    <div class="card text-white bg-primary">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4 class="card-title">${statistics.totalRooms}</h4>
                                                    <p class="card-text">总房间数</p>
                                                </div>
                                                <div class="align-self-center">
                                                    <i class="fas fa-bed fa-2x"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3 mb-3">
                                    <div class="card text-white bg-success">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4 class="card-title">${statistics.availableRooms}</h4>
                                                    <p class="card-text">可用房间</p>
                                                </div>
                                                <div class="align-self-center">
                                                    <i class="fas fa-check-circle fa-2x"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3 mb-3">
                                    <div class="card text-white bg-warning">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4 class="card-title">${statistics.occupiedRooms}</h4>
                                                    <p class="card-text">已入住</p>
                                                </div>
                                                <div class="align-self-center">
                                                    <i class="fas fa-calendar-check fa-2x"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3 mb-3">
                                    <div class="card text-white bg-info">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <h4 class="card-title">${statistics.totalCustomers}</h4>
                                                    <p class="card-text">总客户数</p>
                                                </div>
                                                <div class="align-self-center">
                                                    <i class="fas fa-users fa-2x"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 今日概览 -->
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <i class="fas fa-calendar-day"></i> 今日入住
                                        </div>
                                        <div class="card-body">
                                            <c:choose>
                                                <c:when test="${not empty todayCheckIns}">
                                                    <div class="table-responsive">
                                                        <table class="table table-sm">
                                                            <thead>
                                                                <tr>
                                                                    <th>客户</th>
                                                                    <th>房间</th>
                                                                    <th>状态</th>
                                                                    <th>操作</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="booking" items="${todayCheckIns}" varStatus="status">
                                                                    <c:if test="${status.index < 5}">
                                                                        <!-- 只显示前5条 -->
                                                                        <tr>
                                                                            <td>${booking.customer.name}</td>
                                                                            <td>${booking.room.roomNumber}</td>
                                                                            <td>
                                                                                <c:choose>
                                                                                    <c:when test="${booking.status == 'CONFIRMED'}">
                                                                                        <span class="badge bg-warning">待入住</span>
                                                                                    </c:when>
                                                                                    <c:when test="${booking.status == 'CHECKED_IN'}">
                                                                                        <span class="badge bg-success">已入住</span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <span class="badge bg-secondary">${booking.status}</span>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td>
                                                                                <c:if test="${booking.status == 'CONFIRMED'}">
                                                                                    <a href="${pageContext.request.contextPath}/admin/booking/check-in?id=${booking.bookingId}&returnTo=dashboard" class="btn btn-sm btn-success" title="办理入住">
                                                                                        <i class="fas fa-check"></i>
                                                                                    </a>
                                                                                </c:if>
                                                                                <a href="${pageContext.request.contextPath}/admin/booking/detail?id=${booking.bookingId}" class="btn btn-sm btn-info" title="查看详情">
                                                                                    <i class="fas fa-eye"></i>
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="text-center text-muted py-4">
                                                        <i class="fas fa-calendar-times fa-3x mb-3"></i>
                                                        <br>今日暂无入住安排
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="text-center">
                                                <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-outline-primary btn-sm">
                                        查看全部预订
                                    </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <i class="fas fa-calendar-times"></i> 今日退房
                                        </div>
                                        <div class="card-body">
                                            <c:choose>
                                                <c:when test="${not empty todayCheckOuts}">
                                                    <div class="table-responsive">
                                                        <table class="table table-sm">
                                                            <thead>
                                                                <tr>
                                                                    <th>客户</th>
                                                                    <th>房间</th>
                                                                    <th>状态</th>
                                                                    <th>操作</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="booking" items="${todayCheckOuts}" varStatus="status">
                                                                    <c:if test="${status.index < 5}">
                                                                        <!-- 只显示前5条 -->
                                                                        <tr>
                                                                            <td>${booking.customer.name}</td>
                                                                            <td>${booking.room.roomNumber}</td>
                                                                            <td>
                                                                                <c:choose>
                                                                                    <c:when test="${booking.status == 'CHECKED_IN'}">
                                                                                        <span class="badge bg-danger">待退房</span>
                                                                                    </c:when>
                                                                                    <c:when test="${booking.status == 'CHECKED_OUT'}">
                                                                                        <span class="badge bg-secondary">已退房</span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <span class="badge bg-secondary">${booking.status}</span>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td>
                                                                                <c:if test="${booking.status == 'CHECKED_IN'}">
                                                                                    <a href="${pageContext.request.contextPath}/admin/booking/check-out?id=${booking.bookingId}&returnTo=dashboard" class="btn btn-sm btn-warning" title="办理退房">
                                                                                        <i class="fas fa-sign-out-alt"></i>
                                                                                    </a>
                                                                                </c:if>
                                                                                <a href="${pageContext.request.contextPath}/admin/booking/detail?id=${booking.bookingId}" class="btn btn-sm btn-info" title="查看详情">
                                                                                    <i class="fas fa-eye"></i>
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="text-center text-muted py-4">
                                                        <i class="fas fa-calendar-check fa-3x mb-3"></i>
                                                        <br>今日暂无退房安排
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="text-center">
                                                <a href="${pageContext.request.contextPath}/admin/booking/list?status=CHECKED_IN" class="btn btn-outline-primary btn-sm">
                                        查看入住客户
                                    </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 房间状态概览 -->
                            <div class="row">
                                <div class="col">
                                    <div class="card">
                                        <div class="card-header">
                                            <i class="fas fa-home"></i> 房间状态概览
                                        </div>
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-3 text-center mb-3">
                                                    <h3 class="text-success">${roomStatusStats.AVAILABLE != null ? roomStatusStats.AVAILABLE : 0}</h3>
                                                    <p class="text-muted">可用房间</p>
                                                    <div class="progress" style="height: 5px;">
                                                        <c:set var="availablePercent" value="${statistics.totalRooms > 0 ? (roomStatusStats.AVAILABLE * 100 / statistics.totalRooms) : 0}" />
                                                        <div class="progress-bar bg-success" style="width: ${availablePercent}%"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-3 text-center mb-3">
                                                    <h3 class="text-danger">${roomStatusStats.OCCUPIED != null ? roomStatusStats.OCCUPIED : 0}</h3>
                                                    <p class="text-muted">已入住</p>
                                                    <div class="progress" style="height: 5px;">
                                                        <c:set var="occupiedPercent" value="${statistics.totalRooms > 0 ? (roomStatusStats.OCCUPIED * 100 / statistics.totalRooms) : 0}" />
                                                        <div class="progress-bar bg-danger" style="width: ${occupiedPercent}%"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-3 text-center mb-3">
                                                    <h3 class="text-warning">${roomStatusStats.CLEANING != null ? roomStatusStats.CLEANING : 0}</h3>
                                                    <p class="text-muted">清洁中</p>
                                                    <div class="progress" style="height: 5px;">
                                                        <c:set var="cleaningPercent" value="${statistics.totalRooms > 0 ? (roomStatusStats.CLEANING * 100 / statistics.totalRooms) : 0}" />
                                                        <div class="progress-bar bg-warning" style="width: ${cleaningPercent}%"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-3 text-center mb-3">
                                                    <h3 class="text-info">${roomStatusStats.MAINTENANCE != null ? roomStatusStats.MAINTENANCE : 0}</h3>
                                                    <p class="text-muted">维护中</p>
                                                    <div class="progress" style="height: 5px;">
                                                        <c:set var="maintenancePercent" value="${statistics.totalRooms > 0 ? (roomStatusStats.MAINTENANCE * 100 / statistics.totalRooms) : 0}" />
                                                        <div class="progress-bar bg-info" style="width: ${maintenancePercent}%"></div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- 添加入住率显示 -->
                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="card bg-light">
                                                        <div class="card-body text-center">
                                                            <h5 class="card-title">入住率</h5>
                                                            <h2 class="text-primary">
                                                                <fmt:formatNumber value="${statistics.occupancyRate}" pattern="#0.00" />%
                                                            </h2>
                                                            <div class="progress" style="height: 20px;">
                                                                <div class="progress-bar bg-gradient" style="width: ${statistics.occupancyRate}%"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Scripts -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
                <script src="${pageContext.request.contextPath}/js/main.js"></script>

                <script>
                    // 实时时钟
                    function updateTime() {
                        const now = new Date();
                        const timeString = now.toLocaleTimeString('zh-CN', {
                            hour12: false,
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        });
                        document.getElementById('currentTime').textContent = timeString;
                    }

                    // 每秒更新时间
                    updateTime();
                    setInterval(updateTime, 1000);

                    // 页面加载动画
                    $(document).ready(function() {
                        $('.card').addClass('fade-in');

                        // 添加悬停效果
                        $('.card').hover(
                            function() {
                                $(this).addClass('shadow-lg').removeClass('shadow');
                            },
                            function() {
                                $(this).removeClass('shadow-lg').addClass('shadow');
                            }
                        );
                    });
                </script>
            </body>

            </html>