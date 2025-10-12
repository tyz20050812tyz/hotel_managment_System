<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>编辑房间类型 - 酒店管理系统</title>
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
                                    <i class="fas fa-edit"></i> 编辑房间类型
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <div class="btn-group me-2">
                                        <a href="${pageContext.request.contextPath}/admin/room/type/list" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回列表
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <!-- 消息提示 -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <i class="fas fa-exclamation-triangle"></i> ${error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <!-- 编辑表单 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-edit"></i> 房间类型信息
                                        <c:if test="${not empty roomType}">
                                            <span class="badge bg-primary">#${roomType.typeId}</span>
                                        </c:if>
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/admin/room/type/save" method="post" id="roomTypeForm">
                                        <input type="hidden" name="typeId" value="${roomType.typeId}">

                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="typeName" class="form-label">
                                            <i class="fas fa-tag text-primary"></i>
                                            类型名称 <span class="text-danger">*</span>
                                        </label>
                                                    <input type="text" class="form-control" id="typeName" name="typeName" value="${not empty param.typeName ? param.typeName : roomType.typeName}" required>
                                                    <div class="form-text">如：标准单人间、豪华双人间、总统套房等</div>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="price" class="form-label">
                                            <i class="fas fa-dollar-sign text-success"></i>
                                            价格/晚 <span class="text-danger">*</span>
                                        </label>
                                                    <div class="input-group">
                                                        <span class="input-group-text">¥</span>
                                                        <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${not empty param.price ? param.price : roomType.price}" required>
                                                    </div>
                                                    <div class="form-text">单位：人民币元</div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="bedCount" class="form-label">
                                            <i class="fas fa-bed text-info"></i>
                                            床位数 <span class="text-danger">*</span>
                                        </label>
                                                    <input type="number" min="1" max="10" class="form-control" id="bedCount" name="bedCount" value="${not empty param.bedCount ? param.bedCount : roomType.bedCount}" required>
                                                    <div class="form-text">该类型房间的床位数量</div>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="maxGuests" class="form-label">
                                            <i class="fas fa-users text-warning"></i>
                                            最大客人数 <span class="text-danger">*</span>
                                        </label>
                                                    <input type="number" min="1" max="20" class="form-control" id="maxGuests" name="maxGuests" value="${not empty param.maxGuests ? param.maxGuests : roomType.maxGuests}" required>
                                                    <div class="form-text">该类型房间可容纳的最大客人数</div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="description" class="form-label">
                                    <i class="fas fa-align-left text-secondary"></i>
                                    房间描述
                                </label>
                                            <textarea class="form-control" id="description" name="description" rows="3" placeholder="请输入房间类型的详细描述...">${not empty param.description ? param.description : roomType.description}</textarea>
                                            <div class="form-text">描述该类型房间的特色和优势</div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="amenities" class="form-label">
                                    <i class="fas fa-check-circle text-success"></i>
                                    房间设施
                                </label>
                                            <input type="text" class="form-control" id="amenities" name="amenities" value="${not empty param.amenities ? param.amenities : roomType.amenities}" placeholder="WiFi,空调,电视,冰箱,浴缸">
                                            <div class="form-text">多个设施请用英文逗号分隔，如：WiFi,空调,电视,冰箱</div>
                                        </div>

                                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                            <a href="${pageContext.request.contextPath}/admin/room/type/list" class="btn btn-secondary me-md-2">
                                                <i class="fas fa-times"></i> 取消
                                            </a>
                                            <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> 保存更改
                                </button>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- 相关信息 -->
                            <c:if test="${not empty roomType}">
                                <div class="mt-4">
                                    <div class="card border-info">
                                        <div class="card-body">
                                            <h6 class="card-title">
                                                <i class="fas fa-info-circle text-info"></i> 房间类型信息
                                            </h6>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <p><strong>类型ID：</strong>${roomType.typeId}</p>
                                                    <p><strong>创建时间：</strong>
                                                        <c:choose>
                                                            <c:when test="${not empty roomType.createdAt}">
                                                                <fmt:formatDate value="${roomType.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                            </c:when>
                                                            <c:otherwise>N/A</c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                                <div class="col-md-6">
                                                    <p><strong>更新时间：</strong>
                                                        <c:choose>
                                                            <c:when test="${not empty roomType.updatedAt}">
                                                                <fmt:formatDate value="${roomType.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                            </c:when>
                                                            <c:otherwise>N/A</c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <!-- 帮助信息 -->
                            <div class="mt-4">
                                <div class="card border-warning">
                                    <div class="card-body">
                                        <h6 class="card-title">
                                            <i class="fas fa-exclamation-triangle text-warning"></i> 修改注意事项
                                        </h6>
                                        <ul class="mb-0">
                                            <li><strong>价格修改</strong>：修改价格将影响使用该类型的所有房间的价格计算</li>
                                            <li><strong>容量修改</strong>：修改床位数和最大客人数可能影响现有预订的有效性</li>
                                            <li><strong>谨慎操作</strong>：建议在非营业时间进行重要参数的修改</li>
                                            <li><strong>备份建议</strong>：重要修改前建议记录原始数据</li>
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
                    // 表单验证
                    document.getElementById('roomTypeForm').addEventListener('submit', function(e) {
                        const typeName = document.getElementById('typeName').value.trim();
                        const price = document.getElementById('price').value;
                        const bedCount = document.getElementById('bedCount').value;
                        const maxGuests = document.getElementById('maxGuests').value;

                        if (!typeName) {
                            alert('请输入类型名称');
                            e.preventDefault();
                            return;
                        }

                        if (!price || parseFloat(price) <= 0) {
                            alert('请输入有效的价格');
                            e.preventDefault();
                            return;
                        }

                        if (!bedCount || parseInt(bedCount) <= 0) {
                            alert('请输入有效的床位数');
                            e.preventDefault();
                            return;
                        }

                        if (!maxGuests || parseInt(maxGuests) <= 0) {
                            alert('请输入有效的最大客人数');
                            e.preventDefault();
                            return;
                        }

                        if (parseInt(maxGuests) < parseInt(bedCount)) {
                            if (!confirm('最大客人数小于床位数，是否继续？')) {
                                e.preventDefault();
                                return;
                            }
                        }
                    });

                    // 页面初始化
                    document.addEventListener('DOMContentLoaded', function() {
                        HotelManagement.init('${pageContext.request.contextPath}');
                    });
                </script>
            </body>

            </html>