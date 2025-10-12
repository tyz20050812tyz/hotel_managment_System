<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑预订 - 酒店管理系统</title>
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
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/index">首页</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">客户管理</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">房间管理</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">预订管理</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">用户管理</a></li>
            </ul>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- 侧边栏 -->
            <nav class="col-md-2 sidebar">
                <ul class="nav flex-column">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/index">仪表板</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">客户管理</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">房间管理</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">预订管理</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">用户管理</a></li>
                </ul>
            </nav>

            <!-- 主要内容 -->
            <main class="col-md-10">
                <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">编辑预订 <c:if test="${not empty booking}">- #${booking.bookingId}</c:if></h1>
                    <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-outline-secondary">返回列表</a>
                </div>

                <!-- 消息提示 -->
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-${sessionScope.messageType} alert-dismissible fade show">
                        ${sessionScope.message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="message" scope="session"/>
                    <c:remove var="messageType" scope="session"/>
                </c:if>

                <c:choose>
                    <c:when test="${empty booking}">
                        <div class="alert alert-warning">预订信息不存在或已被删除</div>
                    </c:when>
                    <c:otherwise>
                        <!-- 编辑表单 -->
                        <div class="card">
                            <div class="card-header">
                                <h5>编辑预订信息 <span class="badge bg-primary">ID: ${booking.bookingId}</span></h5>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/admin/booking/save" class="needs-validation" novalidate>
                                    <input type="hidden" name="bookingId" value="${booking.bookingId}">
                                    
                                    <div class="row">
                                        <div class="col-md-6">
                                            <label for="customerId" class="form-label">选择客户 *</label>
                                            <select class="form-select" id="customerId" name="customerId" required>
                                                <option value="">请选择客户</option>
                                                <c:forEach var="customer" items="${customers}">
                                                    <option value="${customer.customerId}" ${customer.customerId == booking.customerId ? 'selected' : ''}>
                                                        ${customer.name} - ${customer.phone}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="roomId" class="form-label">选择房间 *</label>
                                            <select class="form-select" id="roomId" name="roomId" required>
                                                <option value="">请选择房间</option>
                                                <c:forEach var="room" items="${rooms}">
                                                    <option value="${room.roomId}" ${room.roomId == booking.roomId ? 'selected' : ''}>
                                                        ${room.roomNumber} - ${room.roomType.typeName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col-md-6">
                                            <label for="checkInDate" class="form-label">入住日期 *</label>
                                            <input type="date" class="form-control" id="checkInDate" name="checkInDate" required
                                                   value="<fmt:formatDate value='${booking.checkInDate}' pattern='yyyy-MM-dd'/>">
                                        </div>
                                        <div class="col-md-6">
                                            <label for="checkOutDate" class="form-label">退房日期 *</label>
                                            <input type="date" class="form-control" id="checkOutDate" name="checkOutDate" required
                                                   value="<fmt:formatDate value='${booking.checkOutDate}' pattern='yyyy-MM-dd'/>">
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col-md-6">
                                            <label for="guestsCount" class="form-label">客人数量 *</label>
                                            <input type="number" class="form-control" id="guestsCount" name="guestsCount" 
                                                   min="1" max="10" value="${booking.guestsCount}" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">预计总价</label>
                                            <div class="input-group">
                                                <span class="input-group-text">¥</span>
                                                <input type="text" class="form-control" readonly
                                                       value="<fmt:formatNumber value='${booking.totalPrice}' pattern='#,##0.00'/>">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mt-3">
                                        <label for="specialRequests" class="form-label">特殊要求</label>
                                        <textarea class="form-control" id="specialRequests" name="specialRequests" rows="3"
                                                  maxlength="500">${booking.specialRequests}</textarea>
                                    </div>

                                    <div class="d-flex justify-content-end mt-4">
                                        <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-secondary me-2">取消</a>
                                        <button type="submit" class="btn btn-primary">更新预订</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- 预订状态信息 -->
                        <div class="card mt-4">
                            <div class="card-header bg-info text-white">
                                <h6>预订状态信息</h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-3">
                                        <strong>当前状态：</strong><br>
                                        <span class="badge bg-${booking.status == 'PENDING' ? 'warning' : booking.status == 'CONFIRMED' ? 'info' : 'success'}">
                                            ${booking.status == 'PENDING' ? '待确认' : booking.status == 'CONFIRMED' ? '已确认' : '其他'}
                                        </span>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>创建时间：</strong><br>
                                        <fmt:formatDate value="${booking.bookingTime}" pattern="yyyy-MM-dd HH:mm"/>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>客户信息：</strong><br>
                                        <c:if test="${not empty booking.customer}">
                                            ${booking.customer.name}
                                        </c:if>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>房间信息：</strong><br>
                                        <c:if test="${not empty booking.room}">
                                            ${booking.room.roomNumber}
                                        </c:if>
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
        document.addEventListener('DOMContentLoaded', function() {
            HotelManagement.init('${pageContext.request.contextPath}');
        });
    </script>
</body>
</html>
