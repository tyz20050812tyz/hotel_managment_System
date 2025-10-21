<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="zh-CN">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>酒店管理系统 - 登录</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet">
        </head>

        <body class="bg-light">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-4">
                        <div class="login-container">
                            <div class="text-center mb-4">
                                <img src="${pageContext.request.contextPath}/images/logo.png" alt="酒店管理系统" class="logo">
                                <h2 class="mt-3">酒店管理系统</h2>
                                <p class="text-muted">请输入您的账号和密码</p>
                            </div>

                            <!-- 错误消息显示 -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger" role="alert">
                                    <i class="fas fa-exclamation-triangle"></i> ${error}
                                </div>
                            </c:if>

                            <!-- 成功消息显示 -->
                            <c:if test="${not empty success}">
                                <div class="alert alert-success" role="alert">
                                    <i class="fas fa-check-circle"></i> ${success}
                                </div>
                            </c:if>

                            <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">用户名</label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                    <i class="fas fa-user"></i>
                                </span>
                                        <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名" required value="${param.username}">
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">密码</label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                        <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required>
                                        <button class="btn btn-outline-secondary" type="button" id="togglePassword" title="显示密码">
                                    <i class="fas fa-eye"></i>
                                </button>
                                    </div>
                                </div>

                                <div class="mb-3 form-check">
                                    <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                                    <label class="form-check-label" for="rememberMe">
                                记住我
                            </label>
                                </div>

                                <button type="submit" class="btn btn-primary w-100" id="loginBtn">
                            <i class="fas fa-sign-in-alt"></i>
                            登录
                        </button>
                            </form>

                            <div class="mt-4 text-center">
                                <small class="text-muted">
                            © 2025 酒店管理系统by佟雨泽. 保留所有权利。
                        </small>
                            </div>

                            <!-- 演示账号信息 -->
                            <div class="mt-3">
                                <div class="card">
                                    <div class="card-header">
                                        <small class="text-muted">演示账号</small>
                                    </div>
                                    <div class="card-body py-2">
                                        <small>
                                    <strong>管理员:</strong> admin / admin<br>
                                    <strong>员工:</strong> staff001 / hello
                                </small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Scripts -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/js/all.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/login.js"></script>
        </body>

        </html>