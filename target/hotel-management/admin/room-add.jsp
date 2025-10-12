<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <title>添加房间 - 酒店管理系统</title>
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
                        <i class="fas fa-plus"></i>
                        添加房间
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <a href="${pageContext.request.contextPath}/admin/room/list" class="btn btn-outline-secondary">
                                <i class="fas fa-arrow-left"></i> 返回列表
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/room/type/list" class="btn btn-outline-info">
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
                    <c:remove var="message" scope="session"/>
                    <c:remove var="messageType" scope="session"/>
                </c:if>

                <!-- 错误信息 -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${empty roomTypes}">
                        <div class="alert alert-warning" role="alert">
                            <i class="fas fa-exclamation-triangle"></i>
                            暂无房间类型，请先
                            <a href="${pageContext.request.contextPath}/admin/room/type/add" class="alert-link">添加房间类型</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- 添加房间表单 -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title mb-0">
                                    <i class="fas fa-info-circle"></i>
                                    房间基本信息
                                </h5>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/admin/room/save" class="needs-validation" novalidate>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="roomNumber" class="form-label">
                                                    <i class="fas fa-door-open"></i>
                                                    房间号 <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="roomNumber" name="roomNumber" required
                                                       placeholder="请输入房间号（如：101）" maxlength="10">
                                                <div class="invalid-feedback">
                                                    请输入房间号
                                                </div>
                                                <small class="form-text text-muted">房间号必须唯一，建议使用楼层+房间编号格式</small>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="typeId" class="form-label">
                                                    <i class="fas fa-bed"></i>
                                                    房间类型 <span class="text-danger">*</span>
                                                </label>
                                                <select class="form-select" id="typeId" name="typeId" required onchange="showTypeInfo()">
                                                    <option value="">请选择房间类型</option>
                                                    <c:forEach var="roomType" items="${roomTypes}">
                                                        <option value="${roomType.typeId}" 
                                                                data-price="${roomType.price}"
                                                                data-beds="${roomType.bedCount}"
                                                                data-guests="${roomType.maxGuests}"
                                                                data-description="${roomType.description}">
                                                            ${roomType.typeName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <div class="invalid-feedback">
                                                    请选择房间类型
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="floor" class="form-label">
                                                    <i class="fas fa-building"></i>
                                                    楼层 <span class="text-danger">*</span>
                                                </label>
                                                <input type="number" class="form-control" id="floor" name="floor" required
                                                       min="1" max="50" placeholder="请输入楼层">
                                                <div class="invalid-feedback">
                                                    请输入有效的楼层（1-50）
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="status" class="form-label">
                                                    <i class="fas fa-info-circle"></i>
                                                    房间状态
                                                </label>
                                                <select class="form-select" id="status" name="status">
                                                    <option value="AVAILABLE">可用</option>
                                                    <option value="CLEANING">清洁中</option>
                                                    <option value="MAINTENANCE">维护中</option>
                                                    <option value="OUT_OF_ORDER">故障</option>
                                                </select>
                                                <small class="form-text text-muted">新房间默认状态为"可用"</small>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 房间类型信息显示 -->
                                    <div class="row" id="typeInfoArea" style="display: none;">
                                        <div class="col-md-12">
                                            <div class="card border-info">
                                                <div class="card-header bg-light">
                                                    <h6 class="card-title mb-0">
                                                        <i class="fas fa-info"></i>
                                                        选中房间类型信息
                                                    </h6>
                                                </div>
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-md-3">
                                                            <strong>价格：</strong>
                                                            <span id="typePrice" class="text-primary">-</span>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <strong>床位数：</strong>
                                                            <span id="typeBeds">-</span>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <strong>最大客人数：</strong>
                                                            <span id="typeGuests">-</span>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <strong>房间类型：</strong>
                                                            <span id="typeName">-</span>
                                                        </div>
                                                    </div>
                                                    <div class="row mt-2">
                                                        <div class="col-md-12">
                                                            <strong>描述：</strong>
                                                            <span id="typeDescription" class="text-muted">-</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                                        <a href="${pageContext.request.contextPath}/admin/room/list" class="btn btn-secondary me-md-2">
                                            <i class="fas fa-times"></i> 取消
                                        </a>
                                        <button type="reset" class="btn btn-outline-secondary me-md-2">
                                            <i class="fas fa-redo"></i> 重置
                                        </button>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> 保存房间
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- 提示信息 -->
                <div class="mt-4">
                    <div class="card border-info">
                        <div class="card-body">
                            <h6 class="card-title">
                                <i class="fas fa-info-circle text-info"></i>
                                温馨提示
                            </h6>
                            <ul class="mb-0">
                                <li>标有 <span class="text-danger">*</span> 的字段为必填项</li>
                                <li>房间号必须唯一，不能重复</li>
                                <li>建议房间号格式：楼层+房间编号（如：101, 201, 301）</li>
                                <li>楼层范围：1-50层</li>
                                <li>新添加的房间默认状态为"可用"</li>
                                <li>如果没有房间类型，请先添加房间类型</li>
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

        // 显示房间类型信息
        function showTypeInfo() {
            const typeSelect = document.getElementById('typeId');
            const typeInfoArea = document.getElementById('typeInfoArea');
            const selectedOption = typeSelect.options[typeSelect.selectedIndex];
            
            if (selectedOption.value) {
                const price = selectedOption.getAttribute('data-price');
                const beds = selectedOption.getAttribute('data-beds');
                const guests = selectedOption.getAttribute('data-guests');
                const description = selectedOption.getAttribute('data-description');
                const typeName = selectedOption.text;
                
                document.getElementById('typePrice').textContent = '¥' + parseFloat(price).toFixed(2) + '/晚';
                document.getElementById('typeBeds').textContent = beds + '张';
                document.getElementById('typeGuests').textContent = guests + '人';
                document.getElementById('typeName').textContent = typeName;
                document.getElementById('typeDescription').textContent = description || '暂无描述';
                
                typeInfoArea.style.display = 'block';
            } else {
                typeInfoArea.style.display = 'none';
            }
        }

        // 房间号格式验证
        document.getElementById('roomNumber').addEventListener('input', function() {
            const roomNumber = this.value.trim();
            
            if (roomNumber && roomNumber.length < 2) {
                this.setCustomValidity('房间号长度至少2位');
            } else if (roomNumber && !/^[A-Za-z0-9]+$/.test(roomNumber)) {
                this.setCustomValidity('房间号只能包含字母和数字');
            } else {
                this.setCustomValidity('');
            }
        });

        // 楼层验证
        document.getElementById('floor').addEventListener('input', function() {
            const floor = parseInt(this.value);
            
            if (isNaN(floor) || floor < 1 || floor > 50) {
                this.setCustomValidity('楼层必须在1-50之间');
            } else {
                this.setCustomValidity('');
                
                // 自动建议房间号格式
                const roomNumber = document.getElementById('roomNumber');
                if (!roomNumber.value) {
                    const suggestedRoomNumber = floor.toString().padStart(2, '0') + '01';
                    roomNumber.placeholder = '建议格式：' + suggestedRoomNumber;
                }
            }
        });
    </script>
</body>
</html>
