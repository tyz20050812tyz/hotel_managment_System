<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>客户详情 - 酒店管理系统</title>
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/customer/list">
                                        <i class="fas fa-users"></i> 客户管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
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
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/admin/customer/list">
                                            <i class="fas fa-users"></i> 客户管理
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
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
                            </div>
                        </nav>

                        <!-- 主要内容 -->
                        <main class="col-md-10 ms-sm-auto px-md-4">
                            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                                <h1 class="h2">
                                    <i class="fas fa-user"></i> 客户详情
                                    <c:if test="${not empty customer}"> - ${customer.name}</c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/customer/list" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回列表
                                        </a>
                                        <c:if test="${not empty customer}">
                                            <a href="${pageContext.request.contextPath}/admin/customer/edit?id=${customer.customerId}" class="btn btn-warning">
                                                <i class="fas fa-edit"></i> 编辑客户
                                            </a>
                                        </c:if>
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

                            <!-- 客户详情内容 -->
                            <c:choose>
                                <c:when test="${empty customer}">
                                    <div class="alert alert-danger" role="alert">
                                        <i class="fas fa-exclamation-triangle"></i> 客户信息不存在或已被删除
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
                                                            <td class="fw-bold text-muted">客户ID:</td>
                                                            <td>${customer.customerId}</td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">客户姓名:</td>
                                                            <td><strong class="fs-5">${customer.name}</strong></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">联系电话:</td>
                                                            <td>
                                                                <i class="fas fa-phone text-primary"></i> ${customer.phone}
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">电子邮箱:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty customer.email}">
                                                                        <i class="fas fa-envelope text-primary"></i> ${customer.email}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">未提供</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">身份证号:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty customer.idCard}">
                                                                        <i class="fas fa-id-card text-success"></i> ${customer.idCard}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">未提供</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">联系地址:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty customer.address}">
                                                                        <i class="fas fa-map-marker-alt text-danger"></i> ${customer.address}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">未提供</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- VIP信息 -->
                                        <div class="col-md-6 mb-4">
                                            <div class="card">
                                                <div class="card-header">
                                                    <h5 class="card-title mb-0">
                                                        <i class="fas fa-crown text-warning"></i> VIP信息
                                                    </h5>
                                                </div>
                                                <div class="card-body">
                                                    <table class="table table-borderless">
                                                        <tr>
                                                            <td class="fw-bold text-muted">会员状态:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${customer.vip}">
                                                                        <span class="badge bg-warning text-dark fs-6">
                                                                <i class="fas fa-crown"></i> VIP客户
                                                            </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-secondary fs-6">普通客户</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">VIP等级:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${customer.vip}">
                                                                        <span class="badge bg-primary fs-6">VIP${customer.vipLevel}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">无等级</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">享受折扣:</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${customer.vip}">
                                                                        <c:choose>
                                                                            <c:when test="${customer.vipLevel == 1}">
                                                                                <span class="text-success fw-bold">9.5折优惠</span>
                                                                            </c:when>
                                                                            <c:when test="${customer.vipLevel == 2}">
                                                                                <span class="text-success fw-bold">9折优惠</span>
                                                                            </c:when>
                                                                            <c:when test="${customer.vipLevel == 3}">
                                                                                <span class="text-success fw-bold">8.5折优惠</span>
                                                                            </c:when>
                                                                            <c:when test="${customer.vipLevel == 4}">
                                                                                <span class="text-success fw-bold">8折优惠</span>
                                                                            </c:when>
                                                                            <c:when test="${customer.vipLevel == 5}">
                                                                                <span class="text-success fw-bold">7.5折优惠</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">无折扣</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">无折扣</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="fw-bold text-muted">注册时间:</td>
                                                            <td>
                                                                <i class="fas fa-calendar text-info"></i>
                                                                <fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 统计卡片 -->
                                    <div class="row mb-4">
                                        <div class="col-md-3">
                                            <div class="card text-center border-primary">
                                                <div class="card-body">
                                                    <i class="fas fa-id-badge fa-2x text-primary mb-2"></i>
                                                    <h6 class="card-title">客户编号</h6>
                                                    <p class="card-text fs-5 fw-bold">#${customer.customerId}</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-center border-success">
                                                <div class="card-body">
                                                    <i class="fas fa-star fa-2x text-success mb-2"></i>
                                                    <h6 class="card-title">客户等级</h6>
                                                    <p class="card-text fs-5 fw-bold">${customer.vipLevelDescription}</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-center border-warning">
                                                <div class="card-body">
                                                    <i class="fas fa-percentage fa-2x text-warning mb-2"></i>
                                                    <h6 class="card-title">折扣率</h6>
                                                    <p class="card-text fs-5 fw-bold">
                                                        <fmt:formatNumber value="${customer.discountRate * 10}" maxFractionDigits="1" />折
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-center border-info">
                                                <div class="card-body">
                                                    <i class="fas fa-clock fa-2x text-info mb-2"></i>
                                                    <h6 class="card-title">注册天数</h6>
                                                    <p class="card-text fs-5 fw-bold">
                                                        ${registrationDays}天
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 操作按钮 -->
                                    <div class="card">
                                        <div class="card-header">
                                            <h5 class="card-title mb-0">
                                                <i class="fas fa-cogs"></i> 操作
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="d-grid gap-2 d-md-block">
                                                <a href="${pageContext.request.contextPath}/admin/customer/edit?id=${customer.customerId}" class="btn btn-warning">
                                                    <i class="fas fa-edit"></i> 编辑客户信息
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/booking/list?customerId=${customer.customerId}" class="btn btn-info">
                                                    <i class="fas fa-calendar-check"></i> 查看预订记录
                                                </a>
                                                <button type="button" class="btn btn-danger" onclick="confirmDelete('${customer.customerId}')">
                                        <i class="fas fa-trash"></i> 删除客户
                                    </button>
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

                        // 设置当前时间用于计算注册天数
                        var now = new Date();
                        window.now = now;
                    });

                    // 确认删除
                    function confirmDelete(customerId) {
                        if (confirm('确定要删除这个客户吗？删除后将无法恢复！')) {
                            // 发送删除请求
                            fetch('${pageContext.request.contextPath}/admin/customer/delete', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded',
                                    },
                                    body: 'id=' + customerId
                                })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        alert('删除成功');
                                        window.location.href = '${pageContext.request.contextPath}/admin/customer/list';
                                    } else {
                                        alert('删除失败：' + data.message);
                                    }
                                })
                                .catch(error => {
                                    console.error('错误:', error);
                                    alert('删除时发生错误');
                                });
                        }
                    }
                </script>
            </body>

            </html>