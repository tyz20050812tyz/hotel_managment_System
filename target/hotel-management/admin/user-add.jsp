<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="zh-CN">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta name="contextPath" content="${pageContext.request.contextPath}">
            <title>添加用户 - 酒店管理系统</title>
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
                                    <i class="fas fa-calendar-check"></i> 预订管理
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/admin/user/list">
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
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
                                        <i class="fas fa-calendar-check"></i> 预订管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/user/list">
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
                                <i class="fas fa-user-plus"></i> 添加用户
                            </h1>
                            <div class="btn-toolbar mb-2 mb-md-0">
                                <a href="${pageContext.request.contextPath}/admin/user/list" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left"></i> 返回列表
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

                        <!-- 错误信息 -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <!-- 添加用户表单 -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title mb-0">
                                    <i class="fas fa-info-circle"></i> 用户基本信息
                                </h5>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/admin/user/add" class="needs-validation" novalidate>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="username" class="form-label">
                                            <i class="fas fa-user"></i>
                                            用户名 <span class="text-danger">*</span>
                                        </label>
                                                <input type="text" class="form-control" id="username" name="username" required placeholder="请输入用户名" maxlength="50" value="${user.username}">
                                                <div class="invalid-feedback">
                                                    请输入用户名
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="password" class="form-label">
                                            <i class="fas fa-lock"></i>
                                            密码 <span class="text-danger">*</span>
                                        </label>
                                                <input type="password" class="form-control" id="password" name="password" required placeholder="请输入密码" minlength="6" maxlength="20">
                                                <div class="invalid-feedback">
                                                    密码长度需要6-20位
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="realName" class="form-label">
                                            <i class="fas fa-id-card"></i>
                                            真实姓名 <span class="text-danger">*</span>
                                        </label>
                                                <input type="text" class="form-control" id="realName" name="realName" required placeholder="请输入真实姓名" maxlength="50" value="${user.realName}">
                                                <div class="invalid-feedback">
                                                    请输入真实姓名
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="role" class="form-label">
                                            <i class="fas fa-user-tag"></i>
                                            用户角色 <span class="text-danger">*</span>
                                        </label>
                                                <select class="form-select" id="role" name="role" required>
                                            <option value="">请选择角色</option>
                                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>管理员</option>
                                            <option value="STAFF" ${user.role == 'STAFF' ? 'selected' : ''}>员工</option>
                                        </select>
                                                <div class="invalid-feedback">
                                                    请选择用户角色
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="phone" class="form-label">
                                            <i class="fas fa-phone"></i>
                                            联系电话
                                        </label>
                                                <input type="tel" class="form-control" id="phone" name="phone" placeholder="请输入手机号码" pattern="^1[3-9]\d{9}$" maxlength="11" value="${user.phone}">
                                                <div class="invalid-feedback">
                                                    请输入正确的手机号码
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="email" class="form-label">
                                            <i class="fas fa-envelope"></i>
                                            电子邮箱
                                        </label>
                                                <input type="email" class="form-control" id="email" name="email" placeholder="请输入邮箱地址" maxlength="100" value="${user.email}">
                                                <div class="invalid-feedback">
                                                    请输入正确的邮箱地址
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="d-flex justify-content-between">
                                                <button type="button" class="btn btn-secondary" onclick="history.back()">
                                            <i class="fas fa-times"></i> 取消
                                        </button>
                                                <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> 保存用户
                                        </button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- Bootstrap Bundle with Popper -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

            <script>
                // 表单验证
                (function() {
                    'use strict';

                    // 获取表单
                    var form = document.querySelector('.needs-validation');

                    // 添加提交事件监听器
                    form.addEventListener('submit', function(event) {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }

                        form.classList.add('was-validated');
                    }, false);

                    // 用户名重复检查
                    var usernameInput = document.getElementById('username');
                    var usernameTimeout;

                    usernameInput.addEventListener('input', function() {
                        clearTimeout(usernameTimeout);
                        var username = this.value.trim();

                        if (username.length >= 3) {
                            usernameTimeout = setTimeout(function() {
                                checkUsername(username);
                            }, 500);
                        }
                    });

                    function checkUsername(username) {
                        fetch('${pageContext.request.contextPath}/admin/user/check-username?username=' + encodeURIComponent(username))
                            .then(response => response.json())
                            .then(data => {
                                var usernameInput = document.getElementById('username');
                                if (data.success) {
                                    usernameInput.setCustomValidity('');
                                } else {
                                    usernameInput.setCustomValidity('用户名已存在');
                                }
                            })
                            .catch(error => {
                                console.error('检查用户名失败:', error);
                            });
                    }
                })();
            </script>
        </body>

        </html>