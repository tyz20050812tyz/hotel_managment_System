<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>用户管理 - 酒店管理系统</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
            </head>

            <body>
                <!-- 导航栏 (简化版，实际项目中可以提取为公共组件) -->
                <nav class="navbar navbar-expand-lg navbar-dark">
                    <div class="container-fluid">
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/index">
                            <i class="fas fa-hotel"></i> 酒店管理系统
                        </a>
                        <div class="navbar-nav ms-auto">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/index">
                                <i class="fas fa-home"></i> 首页
                            </a>
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt"></i> 退出
                            </a>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid mt-4">
                    <div class="row">
                        <div class="col">
                            <!-- 页面标题 -->
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <div>
                                    <h2><i class="fas fa-users text-primary"></i> 用户管理</h2>
                                    <nav aria-label="breadcrumb">
                                        <ol class="breadcrumb">
                                            <li class="breadcrumb-item">
                                                <a href="${pageContext.request.contextPath}/admin/index">首页</a>
                                            </li>
                                            <li class="breadcrumb-item active">用户管理</li>
                                        </ol>
                                    </nav>
                                </div>
                                <div>
                                    <a href="${pageContext.request.contextPath}/admin/user/add" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> 添加用户
                                    </a>
                                </div>
                            </div>

                            <!-- 统计信息 -->
                            <c:if test="${not empty statistics}">
                                <div class="row mb-4">
                                    <div class="col-md-3">
                                        <div class="card text-center">
                                            <div class="card-body">
                                                <h3 class="text-primary">${statistics.totalUsers}</h3>
                                                <p class="text-muted mb-0">总用户数</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="card text-center">
                                            <div class="card-body">
                                                <h3 class="text-danger">${statistics.adminCount}</h3>
                                                <p class="text-muted mb-0">管理员</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="card text-center">
                                            <div class="card-body">
                                                <h3 class="text-success">${statistics.staffCount}</h3>
                                                <p class="text-muted mb-0">员工</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="card text-center">
                                            <div class="card-body">
                                                <h3 class="text-info">${statistics.activeUsers}</h3>
                                                <p class="text-muted mb-0">活跃用户</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <!-- 用户列表 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-list"></i> 用户列表
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger" role="alert">
                                            <i class="fas fa-exclamation-triangle"></i> ${error}
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty success}">
                                        <div class="alert alert-success" role="alert">
                                            <i class="fas fa-check-circle"></i> ${success}
                                        </div>
                                    </c:if>

                                    <div class="table-responsive">
                                        <table class="table table-hover" id="userTable">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>用户名</th>
                                                    <th>真实姓名</th>
                                                    <th>角色</th>
                                                    <th>联系电话</th>
                                                    <th>邮箱</th>
                                                    <th>状态</th>
                                                    <th>创建时间</th>
                                                    <th>操作</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty users}">
                                                        <c:forEach var="user" items="${users}">
                                                            <tr>
                                                                <td>${user.userId}</td>
                                                                <td>
                                                                    <strong>${user.username}</strong>
                                                                </td>
                                                                <td>${user.realName}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${user.role == 'ADMIN'}">
                                                                            <span class="badge bg-danger">
                                                                    <i class="fas fa-crown"></i> 管理员
                                                                </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-info">
                                                                    <i class="fas fa-user"></i> 员工
                                                                </span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${user.phone}</td>
                                                                <td>${user.email}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${user.status == 1}">
                                                                            <span class="badge bg-success">
                                                                    <i class="fas fa-check-circle"></i> 正常
                                                                </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">
                                                                    <i class="fas fa-ban"></i> 禁用
                                                                </span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm" />
                                                                </td>
                                                                <td>
                                                                    <div class="btn-group btn-group-sm" role="group">
                                                                        <a href="${pageContext.request.contextPath}/admin/user/edit?id=${user.userId}" class="btn btn-outline-primary" title="编辑">
                                                                            <i class="fas fa-edit"></i>
                                                                        </a>

                                                                        <c:if test="${user.userId != sessionScope.user.userId}">
                                                                            <button type="button" class="btn btn-outline-warning btn-toggle-status" data-user-id="${user.userId}" data-enabled="${user.status != 1}" title="${user.status == 1 ? '禁用' : '启用'}">
                                                                                <i class="fas ${user.status == 1 ? 'fa-ban' : 'fa-check'}"></i>
                                                                            </button>

                                                                            <button type="button" class="btn btn-outline-danger btn-delete-user" data-user-id="${user.userId}" data-username="${user.username}" title="删除">
                                                                                <i class="fas fa-trash"></i>
                                                                            </button>
                                                                        </c:if>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="9" class="text-center text-muted py-4">
                                                                <i class="fas fa-inbox fa-3x mb-3"></i>
                                                                <br>暂无用户数据
                                                            </td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
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
                    /**
                     * 切换用户状态
                     */
                    function toggleUserStatus(userId, enabled) {
                        const action = enabled ? '启用' : '禁用';

                        if (!confirm(`确定要${action}此用户吗？`)) {
                            return;
                        }

                        HotelManagement.ajax({
                            url: '/user/toggle-status',
                            method: 'POST',
                            data: {
                                id: userId,
                                enabled: enabled
                            },
                            success: function(response) {
                                if (response.success) {
                                    HotelManagement.showAlert(response.message, 'success');
                                    setTimeout(() => {
                                        location.reload();
                                    }, 1000);
                                } else {
                                    HotelManagement.showAlert(response.message, 'danger');
                                }
                            }
                        });
                    }

                    /**
                     * 删除用户
                     */
                    function deleteUser(userId, username) {
                        if (!confirm(`确定要删除用户 "${username}" 吗？\n此操作不可撤销！`)) {
                            return;
                        }

                        HotelManagement.ajax({
                            url: '/user/delete',
                            method: 'POST',
                            data: {
                                id: userId
                            },
                            success: function(response) {
                                if (response.success) {
                                    HotelManagement.showAlert(response.message, 'success');
                                    setTimeout(() => {
                                        location.reload();
                                    }, 1000);
                                } else {
                                    HotelManagement.showAlert(response.message, 'danger');
                                }
                            }
                        });
                    }

                    // 初始化数据表格
                    $(document).ready(function() {
                        if ($('#userTable tbody tr').length > 1) {
                            HotelManagement.initDataTable('#userTable', {
                                order: [
                                    [0, 'desc']
                                ],
                                columnDefs: [{
                                        orderable: false,
                                        targets: [8]
                                    } // 操作列不可排序
                                ]
                            });
                        }

                        // 绑定切换状态按钮事件
                        $(document).on('click', '.btn-toggle-status', function() {
                            const userId = $(this).data('user-id');
                            const enabled = $(this).data('enabled');
                            toggleUserStatus(userId, enabled);
                        });

                        // 绑定删除用户按钮事件
                        $(document).on('click', '.btn-delete-user', function() {
                            const userId = $(this).data('user-id');
                            const username = $(this).data('username');
                            deleteUser(userId, username);
                        });
                    });
                </script>
            </body>

            </html>
