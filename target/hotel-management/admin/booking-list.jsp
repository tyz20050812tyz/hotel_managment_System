<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>预订管理 - 酒店管理系统</title>
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/booking/list">
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
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/booking/list">
                                            <i class="fas fa-calendar-check"></i> 预订管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/booking/today-checkins">
                                            <i class="fas fa-sign-in-alt"></i> 今日入住
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/booking/today-checkouts">
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
                                    <i class="fas fa-calendar-check"></i> 预订管理
                                    <c:if test="${not empty pageTitle}"> - ${pageTitle}</c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/booking/add" class="btn btn-primary">
                                            <i class="fas fa-plus"></i> 创建预订
                                        </a>
                                        <a href="${pageContext.request.contextPath}/booking/today-checkins" class="btn btn-outline-success">
                                            <i class="fas fa-sign-in-alt"></i> 今日入住
                                        </a>
                                        <a href="${pageContext.request.contextPath}/booking/today-checkouts" class="btn btn-outline-warning">
                                            <i class="fas fa-sign-out-alt"></i> 今日退房
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <!-- 消息提示 -->
                            <c:if test="${not empty sessionScope.message}">
                                <div class="alert alert-${sessionScope.messageType} alert-dismissible fade show" role="alert">
                                    ${sessionScope.message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                                <c:remove var="message" scope="session" />
                                <c:remove var="messageType" scope="session" />
                            </c:if>

                            <!-- 筛选和搜索栏 -->
                            <div class="row mb-3">
                                <div class="col-md-8">
                                    <form method="get" action="${pageContext.request.contextPath}/booking/search">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <input type="date" class="form-control" name="dateFrom" placeholder="开始日期" value="${dateFrom}">
                                            </div>
                                            <div class="col-md-4">
                                                <input type="date" class="form-control" name="dateTo" placeholder="结束日期" value="${dateTo}">
                                            </div>
                                            <div class="col-md-4">
                                                <div class="input-group">
                                                    <button class="btn btn-outline-secondary" type="submit">
                                            <i class="fas fa-search"></i> 搜索
                                        </button>
                                                    <c:if test="${searchMode}">
                                                        <a href="${pageContext.request.contextPath}/booking/list" class="btn btn-outline-secondary">
                                                            <i class="fas fa-times"></i> 清除
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-4">
                                    <select class="form-select" onchange="filterByStatus(this.value)">
                            <option value="">所有状态</option>
                            <option value="PENDING" ${selectedStatus == 'PENDING' ? 'selected' : ''}>待确认</option>
                            <option value="CONFIRMED" ${selectedStatus == 'CONFIRMED' ? 'selected' : ''}>已确认</option>
                            <option value="CHECKED_IN" ${selectedStatus == 'CHECKED_IN' ? 'selected' : ''}>已入住</option>
                            <option value="CHECKED_OUT" ${selectedStatus == 'CHECKED_OUT' ? 'selected' : ''}>已退房</option>
                            <option value="CANCELLED" ${selectedStatus == 'CANCELLED' ? 'selected' : ''}>已取消</option>
                        </select>
                                </div>
                            </div>

                            <!-- 预订列表 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-list"></i> 预订列表
                                        <c:if test="${not empty totalCount}">
                                            <span class="badge bg-primary">${totalCount}</span>
                                        </c:if>
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${empty bookings}">
                                            <div class="text-center py-4">
                                                <i class="fas fa-calendar-check fa-3x text-muted mb-3"></i>
                                                <p class="text-muted">
                                                    <c:choose>
                                                        <c:when test="${todayCheckIns}">今日暂无入住安排</c:when>
                                                        <c:when test="${todayCheckOuts}">今日暂无退房安排</c:when>
                                                        <c:otherwise>暂无预订数据</c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <c:if test="${not todayCheckIns and not todayCheckOuts}">
                                                    <a href="${pageContext.request.contextPath}/booking/add" class="btn btn-primary">
                                                        <i class="fas fa-plus"></i> 创建第一个预订
                                                    </a>
                                                </c:if>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table class="table table-striped table-hover">
                                                    <thead class="table-dark">
                                                        <tr>
                                                            <th>预订ID</th>
                                                            <th>客户信息</th>
                                                            <th>房间信息</th>
                                                            <th>入住日期</th>
                                                            <th>退房日期</th>
                                                            <th>客人数</th>
                                                            <th>总价格</th>
                                                            <th>状态</th>
                                                            <th class="text-center">操作</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="booking" items="${bookings}">
                                                            <tr>
                                                                <td>${booking.bookingId}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.customer}">
                                                                            <strong>${booking.customer.name}</strong>
                                                                            <c:if test="${booking.customer.vip}">
                                                                                <span class="badge bg-warning text-dark ms-1">VIP</span>
                                                                            </c:if>
                                                                            <small class="text-muted d-block">${booking.customer.phone}</small>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">客户ID: ${booking.customerId}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty booking.room}">
                                                                            <strong>${booking.room.roomNumber}</strong>
                                                                            <c:if test="${not empty booking.room.roomType}">
                                                                                <small class="text-muted d-block">${booking.room.roomType.typeName}</small>
                                                                            </c:if>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">房间ID: ${booking.roomId}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${booking.checkInDate}" pattern="yyyy-MM-dd" />
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${booking.checkOutDate}" pattern="yyyy-MM-dd" />
                                                                </td>
                                                                <td class="text-center">${booking.guestsCount}</td>
                                                                <td>
                                                                    <strong>¥<fmt:formatNumber value="${booking.totalPrice}" pattern="#,##0.00"/></strong>
                                                                </td>
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
                                                                <td class="text-center">
                                                                    <div class="btn-group btn-group-sm" role="group">
                                                                        <a href="${pageContext.request.contextPath}/booking/detail?id=${booking.bookingId}" class="btn btn-info" title="查看详情">
                                                                            <i class="fas fa-eye"></i>
                                                                        </a>

                                                                        <c:if test="${booking.status == 'PENDING' || booking.status == 'CONFIRMED'}">
                                                                            <a href="${pageContext.request.contextPath}/booking/edit?id=${booking.bookingId}" class="btn btn-warning" title="编辑">
                                                                                <i class="fas fa-edit"></i>
                                                                            </a>
                                                                        </c:if>

                                                                        <div class="btn-group btn-group-sm" role="group">
                                                                            <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" title="状态管理">
                                                                    <i class="fas fa-cog"></i>
                                                                </button>
                                                                            <ul class="dropdown-menu">
                                                                                <c:if test="${booking.status == 'PENDING'}">
                                                                                    <li>
                                                                                        <a class="dropdown-item" href="javascript:confirmBooking(${booking.bookingId})">
                                                                                            <i class="fas fa-check text-success"></i> 确认预订
                                                                                        </a>
                                                                                    </li>
                                                                                    <li>
                                                                                        <a class="dropdown-item" href="javascript:cancelBooking(${booking.bookingId})">
                                                                                            <i class="fas fa-times text-danger"></i> 取消预订
                                                                                        </a>
                                                                                    </li>
                                                                                </c:if>
                                                                                <c:if test="${booking.status == 'CONFIRMED'}">
                                                                                    <li>
                                                                                        <a class="dropdown-item" href="javascript:checkIn(${booking.bookingId})">
                                                                                            <i class="fas fa-sign-in-alt text-success"></i> 办理入住
                                                                                        </a>
                                                                                    </li>
                                                                                    <li>
                                                                                        <a class="dropdown-item" href="javascript:cancelBooking(${booking.bookingId})">
                                                                                            <i class="fas fa-times text-danger"></i> 取消预订
                                                                                        </a>
                                                                                    </li>
                                                                                </c:if>
                                                                                <c:if test="${booking.status == 'CHECKED_IN'}">
                                                                                    <li>
                                                                                        <a class="dropdown-item" href="javascript:checkOut(${booking.bookingId})">
                                                                                            <i class="fas fa-sign-out-alt text-warning"></i> 办理退房
                                                                                        </a>
                                                                                    </li>
                                                                                </c:if>
                                                                            </ul>
                                                                        </div>

                                                                        <c:if test="${booking.status == 'PENDING' || booking.status == 'CANCELLED'}">
                                                                            <a href="${pageContext.request.contextPath}/booking/delete?id=${booking.bookingId}" class="btn btn-danger btn-delete" data-name="预订${booking.bookingId}" title="删除">
                                                                                <i class="fas fa-trash"></i>
                                                                            </a>
                                                                        </c:if>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <!-- 分页 -->
                                            <c:if test="${totalPages > 1}">
                                                <nav aria-label="预订列表分页">
                                                    <ul class="pagination justify-content-center">
                                                        <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                                                            <a class="page-link" href="?page=${currentPage - 1}&size=${pageSize}">上一页</a>
                                                        </li>

                                                        <c:forEach begin="1" end="${totalPages}" var="page">
                                                            <li class="page-item ${page == currentPage ? 'active' : ''}">
                                                                <a class="page-link" href="?page=${page}&size=${pageSize}">${page}</a>
                                                            </li>
                                                        </c:forEach>

                                                        <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                                                            <a class="page-link" href="?page=${currentPage + 1}&size=${pageSize}">下一页</a>
                                                        </li>
                                                    </ul>
                                                </nav>

                                                <div class="text-center text-muted">
                                                    显示第 ${(currentPage - 1) * pageSize + 1} - ${currentPage * pageSize > totalCount ? totalCount : currentPage * pageSize} 条， 共 ${totalCount} 条记录，第 ${currentPage} / ${totalPages} 页
                                                </div>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
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

                    // 根据状态筛选
                    function filterByStatus(status) {
                        if (status) {
                            window.location.href = '${pageContext.request.contextPath}/booking/list?status=' + status;
                        } else {
                            window.location.href = '${pageContext.request.contextPath}/booking/list';
                        }
                    }

                    // 确认预订
                    function confirmBooking(bookingId) {
                        if (confirm('确定要确认此预订吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/booking/confirm?id=' + bookingId;
                        }
                    }

                    // 取消预订
                    function cancelBooking(bookingId) {
                        if (confirm('确定要取消此预订吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/booking/cancel?id=' + bookingId;
                        }
                    }

                    // 办理入住
                    function checkIn(bookingId) {
                        if (confirm('确定要办理入住吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/booking/checkin?id=' + bookingId;
                        }
                    }

                    // 办理退房
                    function checkOut(bookingId) {
                        if (confirm('确定要办理退房吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/booking/checkout?id=' + bookingId;
                        }
                    }
                </script>
            </body>

            </html>
