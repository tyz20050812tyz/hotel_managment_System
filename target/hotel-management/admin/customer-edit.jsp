<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>编辑客户 - 酒店管理系统</title>
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
                                    <i class="fas fa-user-edit"></i> 编辑客户
                                    <c:if test="${not empty customer}">
                                        - ${customer.name}
                                    </c:if>
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/customer/list" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回列表
                                        </a>
                                        <c:if test="${not empty customer}">
                                            <a href="${pageContext.request.contextPath}/admin/customer/detail?id=${customer.customerId}" class="btn btn-outline-info">
                                                <i class="fas fa-eye"></i> 查看详情
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

                            <!-- 错误信息 -->
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    ${errorMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <c:choose>
                                <c:when test="${empty customer}">
                                    <div class="alert alert-warning" role="alert">
                                        <i class="fas fa-exclamation-triangle"></i> 客户信息不存在或已被删除
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 编辑客户表单 -->
                                    <div class="card">
                                        <div class="card-header">
                                            <h5 class="card-title mb-0">
                                                <i class="fas fa-info-circle"></i> 客户基本信息
                                                <small class="text-muted ms-2">
                                        注册时间：<fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </small>
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <form method="post" action="${pageContext.request.contextPath}/admin/customer/save" class="needs-validation" novalidate>
                                                <input type="hidden" name="customerId" value="${customer.customerId}">

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="name" class="form-label">
                                                    <i class="fas fa-user"></i>
                                                    客户姓名 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="text" class="form-control" id="name" name="name" required value="${customer.name}" placeholder="请输入客户姓名" maxlength="50">
                                                            <div class="invalid-feedback">
                                                                请输入客户姓名
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="phone" class="form-label">
                                                    <i class="fas fa-phone"></i>
                                                    联系电话 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="tel" class="form-control" id="phone" name="phone" required value="${customer.phone}" placeholder="请输入手机号码" pattern="^1[3-9]\d{9}$" maxlength="11">
                                                            <div class="invalid-feedback">
                                                                请输入正确的手机号码
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="email" class="form-label">
                                                    <i class="fas fa-envelope"></i>
                                                    电子邮箱
                                                </label>
                                                            <input type="email" class="form-control" id="email" name="email" value="${customer.email}" placeholder="请输入邮箱地址" maxlength="100">
                                                            <div class="invalid-feedback">
                                                                请输入正确的邮箱地址
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="idCard" class="form-label">
                                                    <i class="fas fa-id-card"></i>
                                                    身份证号 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="text" class="form-control" id="idCard" name="idCard" required value="${customer.idCard}" placeholder="请输入18位身份证号码" pattern="^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$" maxlength="18">
                                                            <div class="invalid-feedback">
                                                                请输入正确的18位身份证号码
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="address" class="form-label">
                                            <i class="fas fa-map-marker-alt"></i>
                                            联系地址
                                        </label>
                                                    <textarea class="form-control" id="address" name="address" rows="3" placeholder="请输入详细地址" maxlength="200">${customer.address}</textarea>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" id="isVip" name="isVip" value="1" ${customer.vip ? 'checked' : ''} onchange="toggleVipLevel()">
                                                                <label class="form-check-label" for="isVip">
                                                        <i class="fas fa-crown text-warning"></i>
                                                        VIP客户
                                                    </label>
                                                            </div>
                                                            <small class="form-text text-muted">勾选此项表示该客户为VIP客户</small>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3" id="vipLevelGroup" <c:if test="${not customer.vip}">style="display: none;"</c:if>>
                                                            <label for="vipLevel" class="form-label">
                                                    <i class="fas fa-star"></i>
                                                    VIP等级
                                                </label>
                                                            <select class="form-select" id="vipLevel" name="vipLevel">
                                                    <option value="1" ${customer.vipLevel == 1 ? 'selected' : ''}>VIP1 (9.5折)</option>
                                                    <option value="2" ${customer.vipLevel == 2 ? 'selected' : ''}>VIP2 (9折)</option>
                                                    <option value="3" ${customer.vipLevel == 3 ? 'selected' : ''}>VIP3 (8.5折)</option>
                                                    <option value="4" ${customer.vipLevel == 4 ? 'selected' : ''}>VIP4 (8折)</option>
                                                    <option value="5" ${customer.vipLevel == 5 ? 'selected' : ''}>VIP5 (7.5折)</option>
                                                </select>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                                    <a href="${pageContext.request.contextPath}/admin/customer/list" class="btn btn-secondary me-md-2">
                                                        <i class="fas fa-times"></i> 取消
                                                    </a>
                                                    <button type="reset" class="btn btn-outline-secondary me-md-2" onclick="resetForm()">
                                            <i class="fas fa-redo"></i> 重置
                                        </button>
                                                    <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> 更新客户
                                        </button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>

                                    <!-- 客户统计信息 -->
                                    <div class="row mt-4">
                                        <div class="col-md-12">
                                            <div class="card border-info">
                                                <div class="card-header bg-info text-white">
                                                    <h6 class="card-title mb-0">
                                                        <i class="fas fa-chart-bar"></i> 客户统计信息
                                                    </h6>
                                                </div>
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-md-3">
                                                            <div class="text-center">
                                                                <i class="fas fa-calendar-alt fa-2x text-info mb-2"></i>
                                                                <h6>注册时间</h6>
                                                                <p class="text-muted">
                                                                    <fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd" />
                                                                </p>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="text-center">
                                                                <i class="fas fa-crown fa-2x text-warning mb-2"></i>
                                                                <h6>会员状态</h6>
                                                                <p class="text-muted">
                                                                    <c:choose>
                                                                        <c:when test="${customer.vip}">
                                                                            <span class="badge bg-warning text-dark">VIP${customer.vipLevel}</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">普通会员</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="text-center">
                                                                <i class="fas fa-id-card fa-2x text-success mb-2"></i>
                                                                <h6>客户ID</h6>
                                                                <p class="text-muted">${customer.customerId}</p>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="text-center">
                                                                <i class="fas fa-percentage fa-2x text-primary mb-2"></i>
                                                                <h6>享受折扣</h6>
                                                                <p class="text-muted">
                                                                    <c:choose>
                                                                        <c:when test="${customer.vip}">
                                                                            <c:choose>
                                                                                <c:when test="${customer.vipLevel == 1}">9.5折</c:when>
                                                                                <c:when test="${customer.vipLevel == 2}">9折</c:when>
                                                                                <c:when test="${customer.vipLevel == 3}">8.5折</c:when>
                                                                                <c:when test="${customer.vipLevel == 4}">8折</c:when>
                                                                                <c:when test="${customer.vipLevel == 5}">7.5折</c:when>
                                                                                <c:otherwise>无折扣</c:otherwise>
                                                                            </c:choose>
                                                                        </c:when>
                                                                        <c:otherwise>无折扣</c:otherwise>
                                                                    </c:choose>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <!-- 提示信息 -->
                            <div class="mt-4">
                                <div class="card border-info">
                                    <div class="card-body">
                                        <h6 class="card-title">
                                            <i class="fas fa-info-circle text-info"></i> 温馨提示
                                        </h6>
                                        <ul class="mb-0">
                                            <li>标有 <span class="text-danger">*</span> 的字段为必填项</li>
                                            <li>手机号码格式：1开头的11位数字</li>
                                            <li>邮箱地址格式：example@domain.com</li>
                                            <li>身份证号码：18位中国居民身份证号码（由六位地址码、八位生日期、三位顺序码和一位校验码组成）</li>
                                            <li>VIP客户享受不同等级的价格折扣优惠</li>
                                            <li>修改客户信息后将立即生效</li>
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

                    // 切换VIP等级显示
                    function toggleVipLevel() {
                        const isVip = document.getElementById('isVip').checked;
                        const vipLevelGroup = document.getElementById('vipLevelGroup');

                        if (isVip) {
                            vipLevelGroup.style.display = 'block';
                        } else {
                            vipLevelGroup.style.display = 'none';
                            document.getElementById('vipLevel').value = '1';
                        }
                    }

                    // 重置表单
                    function resetForm() {
                        // 重置VIP状态显示
                        setTimeout(function() {
                            toggleVipLevel();
                        }, 100);
                    }

                    // 手机号码实时验证
                    document.getElementById('phone').addEventListener('input', function() {
                        const phone = this.value;
                        const pattern = /^1[3-9]\d{9}$/;

                        if (phone && !pattern.test(phone)) {
                            this.setCustomValidity('请输入正确的手机号码');
                        } else {
                            this.setCustomValidity('');
                        }
                    });

                    // 身份证号码实时验证（18位中国居民身份证格式）
                    document.getElementById('idCard').addEventListener('input', function() {
                        const idCard = this.value;
                        // 中国居民身份证号码格式：18位，由六位地址码、八位生日期、三位顺序码和一位校验码组成
                        const pattern = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;

                        if (!idCard) {
                            this.setCustomValidity('身份证号不能为空');
                        } else if (!pattern.test(idCard)) {
                            this.setCustomValidity('请输入18位有效的中国居民身份证号码');
                        } else {
                            this.setCustomValidity('');
                        }
                    });
                </script>
            </body>

            </html>