<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>添加房间类型 - 酒店管理系统</title>
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
                                    <i class="fas fa-plus"></i> 添加房间类型
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

                            <!-- 添加表单 -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-edit"></i> 房间类型信息
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/admin/room/type/save" method="post" id="roomTypeForm">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="typeName" class="form-label">
                                            <i class="fas fa-tag text-primary"></i>
                                            类型名称 <span class="text-danger">*</span>
                                        </label>
                                                    <input type="text" class="form-control" id="typeName" name="typeName" value="${param.typeName}" required>
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
                                                        <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${param.price}" required>
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
                                                    <input type="number" min="1" max="10" class="form-control" id="bedCount" name="bedCount" value="${param.bedCount}" required>
                                                    <div class="form-text">该类型房间的床位数量</div>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label for="maxGuests" class="form-label">
                                            <i class="fas fa-users text-warning"></i>
                                            最大客人数 <span class="text-danger">*</span>
                                        </label>
                                                    <input type="number" min="1" max="20" class="form-control" id="maxGuests" name="maxGuests" value="${param.maxGuests}" required>
                                                    <div class="form-text">该类型房间可容纳的最大客人数</div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="description" class="form-label">
                                    <i class="fas fa-align-left text-secondary"></i>
                                    房间描述
                                </label>
                                            <textarea class="form-control" id="description" name="description" rows="3" placeholder="请输入房间类型的详细描述...">${param.description}</textarea>
                                            <div class="form-text">描述该类型房间的特色和优势</div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="amenities" class="form-label">
                                    <i class="fas fa-check-circle text-success"></i>
                                    房间设施
                                </label>
                                            <input type="text" class="form-control" id="amenities" name="amenities" value="${param.amenities}" placeholder="WiFi,空调,电视,冰箱,浴缸">
                                            <div class="form-text">多个设施请用英文逗号分隔，如：WiFi,空调,电视,冰箱</div>
                                        </div>

                                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                            <a href="${pageContext.request.contextPath}/admin/room/type/list" class="btn btn-secondary me-md-2">
                                                <i class="fas fa-times"></i> 取消
                                            </a>
                                            <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> 保存
                                </button>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- 帮助信息 -->
                            <div class="mt-4">
                                <div class="card border-info">
                                    <div class="card-body">
                                        <h6 class="card-title">
                                            <i class="fas fa-info-circle text-info"></i> 填写说明
                                        </h6>
                                        <ul class="mb-0">
                                            <li><strong>类型名称</strong>：建议使用具有描述性的名称，如"标准单人间"、"豪华双人间"等</li>
                                            <li><strong>价格</strong>：请输入每晚的价格，系统会根据入住天数自动计算总价</li>
                                            <li><strong>床位数</strong>：实际的床位数量，影响房间容量和价格</li>
                                            <li><strong>最大客人数</strong>：建议略大于床位数，考虑加床等情况</li>
                                            <li><strong>房间设施</strong>：列出主要设施，用逗号分隔，方便客户了解</li>
                                            <li><strong>描述</strong>：可以包含房间面积、装修风格、特色服务等信息</li>
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