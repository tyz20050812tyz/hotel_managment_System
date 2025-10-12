<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>客户管理 - 酒店管理系统</title>
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
                                    <i class="fas fa-users"></i> 客户管理
                                    <c:if test="${not empty pageTitle}"> - ${pageTitle}</c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <a href="${pageContext.request.contextPath}/admin/customer/add" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> 添加客户
                                    </a>
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

                            <!-- 搜索栏 -->
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <form method="get" action="${pageContext.request.contextPath}/admin/customer/search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="keyword" placeholder="搜索客户姓名、手机号、邮箱..." value="${keyword}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                    <i class="fas fa-search"></i> 搜索
                                </button>
                                            <c:if test="${searchMode}">
                                                <a href="${pageContext.request.contextPath}/admin/customer/list" class="btn btn-outline-secondary">
                                                    <i class="fas fa-times"></i> 清除
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- 客户列表 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-list"></i> 客户列表
                                        <c:if test="${not empty totalCount}">
                                            <span class="badge bg-primary">${totalCount}</span>
                                        </c:if>
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${empty customers}">
                                            <div class="text-center py-4">
                                                <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                                <p class="text-muted">暂无客户数据</p>
                                                <a href="${pageContext.request.contextPath}/admin/customer/add" class="btn btn-primary">
                                                    <i class="fas fa-plus"></i> 添加第一个客户
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table class="table table-striped table-hover">
                                                    <thead class="table-dark">
                                                        <tr>
                                                            <th>客户ID</th>
                                                            <th>姓名</th>
                                                            <th>手机号</th>
                                                            <th>邮箱</th>
                                                            <th>VIP等级</th>
                                                            <th>注册时间</th>
                                                            <th class="text-center">操作</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="customer" items="${customers}">
                                                            <tr>
                                                                <td>${customer.customerId}</td>
                                                                <td>
                                                                    <strong>${customer.name}</strong>
                                                                    <c:if test="${customer.vip}">
                                                                        <span class="badge bg-warning text-dark ms-1">VIP</span>
                                                                    </c:if>
                                                                </td>
                                                                <td>${customer.phone}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty customer.email}">
                                                                            ${customer.email}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">未设置</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${customer.vip}">
                                                                            <span class="badge bg-warning text-dark">
                                                                    VIP${customer.vipLevel}
                                                                </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">普通</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd HH:mm" />
                                                                </td>
                                                                <td class="text-center">
                                                                    <div class="btn-group btn-group-sm" role="group">
                                                                        <a href="${pageContext.request.contextPath}/admin/customer/detail?id=${customer.customerId}" class="btn btn-info" title="查看详情">
                                                                            <i class="fas fa-eye"></i>
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/admin/customer/edit?id=${customer.customerId}" class="btn btn-warning" title="编辑">
                                                                            <i class="fas fa-edit"></i>
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/admin/customer/delete?id=${customer.customerId}" class="btn btn-danger btn-delete" data-name="${customer.name}" title="删除">
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
                                                <nav aria-label="客户列表分页">
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
                </script>
            </body>

            </html>