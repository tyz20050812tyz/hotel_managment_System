<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>房间管理 - 酒店管理系统</title>
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
                                    <i class="fas fa-bed"></i> 房间管理
                                    <c:if test="${not empty pageTitle}"> - ${pageTitle}</c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/room/add" class="btn btn-primary">
                                            <i class="fas fa-plus"></i> 添加房间
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/room/type/list" class="btn btn-outline-primary">
                                            <i class="fas fa-list"></i> 房间类型
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
                                <div class="col-md-6">
                                    <form method="get" action="${pageContext.request.contextPath}/admin/room/search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="keyword" placeholder="搜索房间号..." value="${keyword}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                    <i class="fas fa-search"></i> 搜索
                                </button>
                                            <c:if test="${searchMode}">
                                                <a href="${pageContext.request.contextPath}/admin/room/list" class="btn btn-outline-secondary">
                                                    <i class="fas fa-times"></i> 清除
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <select class="form-select" onchange="filterByType(this.value)">
                                    <option value="">所有房间类型</option>
                                    <c:forEach var="roomType" items="${roomTypes}">
                                        <option value="${roomType.typeId}" ${selectedTypeId == roomType.typeId ? 'selected' : ''}>
                                            ${roomType.typeName}
                                        </option>
                                    </c:forEach>
                                </select>
                                        </div>
                                        <div class="col-md-6">
                                            <select class="form-select" onchange="filterByStatus(this.value)">
                                    <option value="">所有状态</option>
                                    <option value="AVAILABLE" ${selectedStatus == 'AVAILABLE' ? 'selected' : ''}>可用</option>
                                    <option value="OCCUPIED" ${selectedStatus == 'OCCUPIED' ? 'selected' : ''}>已入住</option>
                                    <option value="CLEANING" ${selectedStatus == 'CLEANING' ? 'selected' : ''}>清洁中</option>
                                    <option value="MAINTENANCE" ${selectedStatus == 'MAINTENANCE' ? 'selected' : ''}>维护中</option>
                                    <option value="OUT_OF_ORDER" ${selectedStatus == 'OUT_OF_ORDER' ? 'selected' : ''}>故障</option>
                                </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 房间列表 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-list"></i> 房间列表
                                        <c:if test="${not empty totalCount}">
                                            <span class="badge bg-primary">${totalCount}</span>
                                        </c:if>
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${empty rooms}">
                                            <div class="text-center py-4">
                                                <i class="fas fa-bed fa-3x text-muted mb-3"></i>
                                                <p class="text-muted">暂无房间数据</p>
                                                <a href="${pageContext.request.contextPath}/admin/room/add" class="btn btn-primary">
                                                    <i class="fas fa-plus"></i> 添加第一个房间
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table class="table table-striped table-hover">
                                                    <thead class="table-dark">
                                                        <tr>
                                                            <th>房间ID</th>
                                                            <th>房间号</th>
                                                            <th>房间类型</th>
                                                            <th>楼层</th>
                                                            <th>价格</th>
                                                            <th>状态</th>
                                                            <th>最后清洁</th>
                                                            <th class="text-center">操作</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="room" items="${rooms}">
                                                            <tr>
                                                                <td>${room.roomId}</td>
                                                                <td>
                                                                    <strong>${room.roomNumber}</strong>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty room.roomType}">
                                                                            ${room.roomType.typeName}
                                                                            <small class="text-muted d-block">
                                                                    ${room.roomType.bedCount}床 | 最多${room.roomType.maxGuests}人
                                                                </small>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未知类型</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${room.floor}楼</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty room.roomType}">
                                                                            <strong>¥<fmt:formatNumber value="${room.roomType.price}" pattern="#,##0.00"/></strong>
                                                                            <small class="text-muted d-block">/晚</small>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">-</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${room.status == 'AVAILABLE'}">
                                                                            <span class="badge bg-success">可用</span>
                                                                        </c:when>
                                                                        <c:when test="${room.status == 'OCCUPIED'}">
                                                                            <span class="badge bg-danger">已入住</span>
                                                                        </c:when>
                                                                        <c:when test="${room.status == 'CLEANING'}">
                                                                            <span class="badge bg-warning text-dark">清洁中</span>
                                                                        </c:when>
                                                                        <c:when test="${room.status == 'MAINTENANCE'}">
                                                                            <span class="badge bg-info">维护中</span>
                                                                        </c:when>
                                                                        <c:when test="${room.status == 'OUT_OF_ORDER'}">
                                                                            <span class="badge bg-dark">故障</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">${room.status}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty room.lastCleaned}">
                                                                            <fmt:formatDate value="${room.lastCleaned}" pattern="MM-dd HH:mm" />
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未记录</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td class="text-center">
                                                                    <div class="btn-group btn-group-sm" role="group">
                                                                        <a href="${pageContext.request.contextPath}/admin/room/detail?id=${room.roomId}" class="btn btn-info" title="查看详情">
                                                                            <i class="fas fa-eye"></i>
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/admin/room/edit?id=${room.roomId}" class="btn btn-warning" title="编辑">
                                                                            <i class="fas fa-edit"></i>
                                                                        </a>
                                                                        <div class="btn-group btn-group-sm" role="group">
                                                                            <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" title="状态管理">
                                                                    <i class="fas fa-cog"></i>
                                                                </button>
                                                                            <ul class="dropdown-menu">
                                                                                <li>
                                                                                    <a class="dropdown-item" href="javascript:void(0)" onclick="changeRoomStatus(this, 'AVAILABLE')" data-room-id="${room.roomId}">
                                                                                        <i class="fas fa-check text-success"></i> 设为可用
                                                                                    </a>
                                                                                </li>
                                                                                <li>
                                                                                    <a class="dropdown-item" href="javascript:void(0)" onclick="changeRoomStatus(this, 'OCCUPIED')" data-room-id="${room.roomId}">
                                                                                        <i class="fas fa-user text-danger"></i> 设为入住
                                                                                    </a>
                                                                                </li>
                                                                                <li>
                                                                                    <a class="dropdown-item" href="javascript:void(0)" onclick="changeRoomStatus(this, 'CLEANING')" data-room-id="${room.roomId}">
                                                                                        <i class="fas fa-broom text-warning"></i> 开始清洁
                                                                                    </a>
                                                                                </li>
                                                                                <li>
                                                                                    <a class="dropdown-item" href="javascript:void(0)" onclick="changeRoomStatus(this, 'MAINTENANCE')" data-room-id="${room.roomId}">
                                                                                        <i class="fas fa-tools text-info"></i> 开始维护
                                                                                    </a>
                                                                                </li>
                                                                                <li>
                                                                                    <a class="dropdown-item" href="javascript:void(0)" onclick="changeRoomStatus(this, 'OUT_OF_ORDER')" data-room-id="${room.roomId}">
                                                                                        <i class="fas fa-exclamation-triangle text-dark"></i> 设为故障
                                                                                    </a>
                                                                                </li>
                                                                            </ul>
                                                                        </div>
                                                                        <a href="${pageContext.request.contextPath}/admin/room/delete?id=${room.roomId}" class="btn btn-danger btn-delete" data-name="${room.roomNumber}" title="删除">
                                                                            <i class="fas fa-trash"></i>
                                                                        </a>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <!-- 分页 -->
                                            <c:if test="${totalPages > 1}">
                                                <nav aria-label="房间列表分页">
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

                    // 根据类型筛选
                    function filterByType(typeId) {
                        if (typeId) {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/list?typeId=' + typeId;
                        } else {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/list';
                        }
                    }

                    // 根据状态筛选
                    function filterByStatus(status) {
                        if (status) {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/list?status=' + status;
                        } else {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/list';
                        }
                    }

                    // 更改房间状态
                    function changeStatus(roomId, status) {
                        if (confirm('确定要更改房间状态吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/status/change?id=' + roomId + '&status=' + status;
                        }
                    }

                    // 更改房间状态（新版本，使用data属性）
                    function changeRoomStatus(element, status) {
                        const roomId = element.getAttribute('data-room-id');
                        if (confirm('确定要更改房间状态吗？')) {
                            window.location.href = '${pageContext.request.contextPath}/admin/room/status/change?id=' + roomId + '&status=' + status;
                        }
                    }
                </script>
            </body>

            </html>
