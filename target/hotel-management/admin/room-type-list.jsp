<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <title>房间类型管理 - 酒店管理系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/index">
                <i class="fas fa-hotel"></i>
                酒店管理系统
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
                            <c:out value="${sessionScope.currentUser.realName}" default="${sessionScope.currentUser.username}"/>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/user/profile">个人资料</a></li>
                            <li><hr class="dropdown-divider"></li>
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
                            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/room/type/list">
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
                        <i class="fas fa-list"></i>
                        房间类型管理
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <a href="${pageContext.request.contextPath}/admin/room/type/add" class="btn btn-primary">
                                <i class="fas fa-plus"></i> 添加类型
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/room/list" class="btn btn-outline-secondary">
                                <i class="fas fa-bed"></i> 房间列表
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
                    <c:remove var="message" scope="session"/>
                    <c:remove var="messageType" scope="session"/>
                </c:if>

                <!-- 房间类型列表 -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-list-alt"></i>
                            房间类型列表
                            <c:if test="${not empty roomTypes}">
                                <span class="badge bg-primary">${roomTypes.size()}</span>
                            </c:if>
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty roomTypes}">
                                <div class="text-center py-4">
                                    <i class="fas fa-bed fa-3x text-muted mb-3"></i>
                                    <p class="text-muted">暂无房间类型数据</p>
                                    <a href="${pageContext.request.contextPath}/admin/room/type/add" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> 添加第一个房间类型
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="row">
                                    <c:forEach var="roomType" items="${roomTypes}">
                                        <div class="col-md-6 col-lg-4 mb-4">
                                            <div class="card h-100 shadow-sm">
                                                <div class="card-header bg-light">
                                                    <h6 class="card-title mb-0">
                                                        <i class="fas fa-bed text-primary"></i>
                                                        ${roomType.typeName}
                                                        <small class="text-muted">#${roomType.typeId}</small>
                                                    </h6>
                                                </div>
                                                <div class="card-body">
                                                    <div class="row text-center mb-3">
                                                        <div class="col-4">
                                                            <i class="fas fa-dollar-sign text-success mb-1"></i>
                                                            <small class="d-block text-muted">价格/晚</small>
                                                            <strong class="text-success">
                                                                ¥<fmt:formatNumber value="${roomType.price}" pattern="#,##0"/>
                                                            </strong>
                                                        </div>
                                                        <div class="col-4">
                                                            <i class="fas fa-bed text-info mb-1"></i>
                                                            <small class="d-block text-muted">床位数</small>
                                                            <strong class="text-info">${roomType.bedCount}张</strong>
                                                        </div>
                                                        <div class="col-4">
                                                            <i class="fas fa-users text-warning mb-1"></i>
                                                            <small class="d-block text-muted">最大客人</small>
                                                            <strong class="text-warning">${roomType.maxGuests}人</strong>
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="mb-3">
                                                        <small class="text-muted">描述：</small>
                                                        <p class="text-muted mb-0" style="min-height: 40px;">
                                                            <c:choose>
                                                                <c:when test="${not empty roomType.description}">
                                                                    ${roomType.description}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    暂无描述
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>
                                                    </div>
                                                    
                                                    <c:if test="${not empty roomType.amenities}">
                                                        <div class="mb-3">
                                                            <small class="text-muted">设施：</small>
                                                            <div class="mt-1">
                                                                <c:forEach var="amenity" items="${roomType.amenities.split(',')}" varStatus="status">
                                                                    <span class="badge bg-light text-dark me-1 mb-1">
                                                                        <i class="fas fa-check-circle text-success"></i>
                                                                        ${amenity.trim()}
                                                                    </span>
                                                                </c:forEach>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                                <div class="card-footer bg-light">
                                                    <div class="d-grid gap-2">
                                                        <div class="btn-group btn-group-sm" role="group">
                                                            <a href="${pageContext.request.contextPath}/admin/room/type/edit?id=${roomType.typeId}" 
                                                               class="btn btn-warning">
                                                                <i class="fas fa-edit"></i> 编辑
                                                            </a>
                                                            <a href="${pageContext.request.contextPath}/admin/room/type/delete?id=${roomType.typeId}" 
                                                               class="btn btn-danger btn-delete" 
                                                               data-name="${roomType.typeName}">
                                                                <i class="fas fa-trash"></i> 删除
                                                            </a>
                                                        </div>
                                                    </div>
                                                    <div class="text-center mt-2">
                                                        <small class="text-muted">
                                                            <i class="fas fa-home"></i>
                                                            类型ID: ${roomType.typeId}
                                                        </small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- 房间类型统计 -->
                <c:if test="${not empty roomTypes}">
                    <div class="row mt-4">
                        <div class="col-md-12">
                            <div class="card border-info">
                                <div class="card-header bg-info text-white">
                                    <h6 class="card-title mb-0">
                                        <i class="fas fa-chart-bar"></i>
                                        房间类型统计
                                    </h6>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="text-center">
                                                <i class="fas fa-list fa-2x text-primary mb-2"></i>
                                                <h6>类型总数</h6>
                                                <h4 class="text-primary">${roomTypes.size()}</h4>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="text-center">
                                                <i class="fas fa-dollar-sign fa-2x text-success mb-2"></i>
                                                <h6>价格范围</h6>
                                                <c:set var="minPrice" value="999999"/>
                                                <c:set var="maxPrice" value="0"/>
                                                <c:forEach var="roomType" items="${roomTypes}">
                                                    <c:if test="${roomType.price < minPrice}">
                                                        <c:set var="minPrice" value="${roomType.price}"/>
                                                    </c:if>
                                                    <c:if test="${roomType.price > maxPrice}">
                                                        <c:set var="maxPrice" value="${roomType.price}"/>
                                                    </c:if>
                                                </c:forEach>
                                                <p class="text-success mb-0">
                                                    ¥<fmt:formatNumber value="${minPrice}" pattern="#,##0"/> - 
                                                    ¥<fmt:formatNumber value="${maxPrice}" pattern="#,##0"/>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="text-center">
                                                <i class="fas fa-bed fa-2x text-info mb-2"></i>
                                                <h6>床位范围</h6>
                                                <c:set var="minBeds" value="999"/>
                                                <c:set var="maxBeds" value="0"/>
                                                <c:forEach var="roomType" items="${roomTypes}">
                                                    <c:if test="${roomType.bedCount < minBeds}">
                                                        <c:set var="minBeds" value="${roomType.bedCount}"/>
                                                    </c:if>
                                                    <c:if test="${roomType.bedCount > maxBeds}">
                                                        <c:set var="maxBeds" value="${roomType.bedCount}"/>
                                                    </c:if>
                                                </c:forEach>
                                                <p class="text-info mb-0">${minBeds} - ${maxBeds}张</p>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="text-center">
                                                <i class="fas fa-users fa-2x text-warning mb-2"></i>
                                                <h6>客人容量</h6>
                                                <c:set var="minGuests" value="999"/>
                                                <c:set var="maxGuests" value="0"/>
                                                <c:forEach var="roomType" items="${roomTypes}">
                                                    <c:if test="${roomType.maxGuests < minGuests}">
                                                        <c:set var="minGuests" value="${roomType.maxGuests}"/>
                                                    </c:if>
                                                    <c:if test="${roomType.maxGuests > maxGuests}">
                                                        <c:set var="maxGuests" value="${roomType.maxGuests}"/>
                                                    </c:if>
                                                </c:forEach>
                                                <p class="text-warning mb-0">${minGuests} - ${maxGuests}人</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- 提示信息 -->
                <div class="mt-4">
                    <div class="card border-info">
                        <div class="card-body">
                            <h6 class="card-title">
                                <i class="fas fa-info-circle text-info"></i>
                                管理说明
                            </h6>
                            <ul class="mb-0">
                                <li>房间类型定义了房间的基本属性：价格、床位数、最大客人数等</li>
                                <li>删除房间类型前，请确保没有房间使用该类型</li>
                                <li>修改房间类型价格将影响该类型所有房间的价格</li>
                                <li>设施信息使用逗号分隔，如：WiFi,空调,电视,冰箱</li>
                                <li>建议先创建房间类型，再添加具体房间</li>
                            </ul>
                        </div>
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
    </script>
</body>
</html>
