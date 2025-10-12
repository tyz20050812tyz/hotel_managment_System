<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="contextPath" content="${pageContext.request.contextPath}">
                <title>创建预订 - 酒店管理系统</title>
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
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">
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
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/admin/booking/list">
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
                                    <i class="fas fa-plus"></i> 创建预订
                                </h1>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-outline-secondary">
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

                            <c:choose>
                                <c:when test="${empty customers}">
                                    <div class="alert alert-warning" role="alert">
                                        <i class="fas fa-exclamation-triangle"></i> 暂无客户数据，请先
                                        <a href="${pageContext.request.contextPath}/admin/customer/add" class="alert-link">添加客户</a>
                                    </div>
                                </c:when>
                                <c:when test="${empty availableRooms}">
                                    <div class="alert alert-warning" role="alert">
                                        <i class="fas fa-exclamation-triangle"></i> 暂无可用房间，请检查
                                        <a href="${pageContext.request.contextPath}/admin/room/list" class="alert-link">房间状态</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 创建预订表单 -->
                                    <div class="card">
                                        <div class="card-header">
                                            <h5 class="card-title mb-0">
                                                <i class="fas fa-info-circle"></i> 预订基本信息
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <form method="post" action="${pageContext.request.contextPath}/admin/booking/save" class="needs-validation" novalidate>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="customerId" class="form-label">
                                                    <i class="fas fa-user"></i>
                                                    选择客户 <span class="text-danger">*</span>
                                                </label>
                                                            <select class="form-select" id="customerId" name="customerId" required onchange="showCustomerInfo()">
                                                    <option value="">请选择客户</option>
                                                    <c:forEach var="customer" items="${customers}">
                                                        <option value="${customer.customerId}"
                                                                data-name="${customer.name}"
                                                                data-phone="${customer.phone}"
                                                                data-vip="${customer.vip}"
                                                                data-vip-level="${customer.vipLevel}">
                                                            ${customer.name} - ${customer.phone}
                                                            <c:if test="${customer.vip}"> (VIP${customer.vipLevel})</c:if>
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                            <div class="invalid-feedback">
                                                                请选择客户
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="roomId" class="form-label">
                                                    <i class="fas fa-bed"></i>
                                                    选择房间 <span class="text-danger">*</span>
                                                </label>
                                                            <select class="form-select" id="roomId" name="roomId" required onchange="showRoomInfo()">
                                                    <option value="">请选择房间</option>
                                                    <c:forEach var="room" items="${availableRooms}">
                                                        <option value="${room.roomId}"
                                                                data-room-number="${room.roomNumber}"
                                                                data-type-name="${room.roomType.typeName}"
                                                                data-price="${room.roomType.price}"
                                                                data-max-guests="${room.roomType.maxGuests}"
                                                                data-bed-count="${room.roomType.bedCount}">
                                                            ${room.roomNumber} - ${room.roomType.typeName} 
                                                            (¥<fmt:formatNumber value="${room.roomType.price}" pattern="#,##0"/>/晚)
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                            <div class="invalid-feedback">
                                                                请选择房间
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="checkInDate" class="form-label">
                                                    <i class="fas fa-calendar"></i>
                                                    入住日期 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="date" class="form-control" id="checkInDate" name="checkInDate" required onchange="calculateTotal()">
                                                            <div class="invalid-feedback">
                                                                请选择入住日期
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="checkOutDate" class="form-label">
                                                    <i class="fas fa-calendar-alt"></i>
                                                    退房日期 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="date" class="form-control" id="checkOutDate" name="checkOutDate" required onchange="calculateTotal()">
                                                            <div class="invalid-feedback">
                                                                请选择退房日期
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="guestsCount" class="form-label">
                                                    <i class="fas fa-users"></i>
                                                    客人数量 <span class="text-danger">*</span>
                                                </label>
                                                            <input type="number" class="form-control" id="guestsCount" name="guestsCount" min="1" max="10" value="1" required>
                                                            <div class="invalid-feedback">
                                                                请输入有效的客人数量
                                                            </div>
                                                            <small class="form-text text-muted" id="maxGuestsHint">
                                                    请先选择房间查看最大容纳人数
                                                </small>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label class="form-label">
                                                    <i class="fas fa-calculator"></i>
                                                    预计总价
                                                </label>
                                                            <div class="input-group">
                                                                <span class="input-group-text">¥</span>
                                                                <input type="text" class="form-control" id="totalPriceDisplay" readonly placeholder="0.00">
                                                                <span class="input-group-text">元</span>
                                                            </div>
                                                            <small class="form-text text-muted" id="priceCalculationInfo">
                                                    价格将自动计算（含VIP折扣）
                                                </small>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="specialRequests" class="form-label">
                                            <i class="fas fa-comment"></i>
                                            特殊要求
                                        </label>
                                                    <textarea class="form-control" id="specialRequests" name="specialRequests" rows="3" placeholder="请输入特殊要求或备注信息..." maxlength="500"></textarea>
                                                    <small class="form-text text-muted">如：加床、禁烟房、高层、靠窗等要求</small>
                                                </div>

                                                <!-- 选择信息显示区域 -->
                                                <div class="row" id="selectionInfo" style="display: none;">
                                                    <div class="col-md-6">
                                                        <div class="card border-primary" id="customerInfo" style="display: none;">
                                                            <div class="card-header bg-primary text-white">
                                                                <h6 class="card-title mb-0">
                                                                    <i class="fas fa-user"></i> 客户信息
                                                                </h6>
                                                            </div>
                                                            <div class="card-body">
                                                                <p class="mb-1"><strong>姓名：</strong><span id="customerName">-</span></p>
                                                                <p class="mb-1"><strong>电话：</strong><span id="customerPhone">-</span></p>
                                                                <p class="mb-0"><strong>会员：</strong><span id="customerVip">-</span></p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="card border-success" id="roomInfo" style="display: none;">
                                                            <div class="card-header bg-success text-white">
                                                                <h6 class="card-title mb-0">
                                                                    <i class="fas fa-bed"></i> 房间信息
                                                                </h6>
                                                            </div>
                                                            <div class="card-body">
                                                                <p class="mb-1"><strong>房间号：</strong><span id="roomNumber">-</span></p>
                                                                <p class="mb-1"><strong>类型：</strong><span id="roomTypeName">-</span></p>
                                                                <p class="mb-1"><strong>价格：</strong><span id="roomPrice">-</span></p>
                                                                <p class="mb-0"><strong>容纳：</strong><span id="roomCapacity">-</span></p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                                                    <a href="${pageContext.request.contextPath}/admin/booking/list" class="btn btn-secondary me-md-2">
                                                        <i class="fas fa-times"></i> 取消
                                                    </a>
                                                    <button type="reset" class="btn btn-outline-secondary me-md-2" onclick="resetForm()">
                                            <i class="fas fa-redo"></i> 重置
                                        </button>
                                                    <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> 创建预订
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
                                            <i class="fas fa-info-circle text-info"></i> 预订须知
                                        </h6>
                                        <ul class="mb-0">
                                            <li>标有 <span class="text-danger">*</span> 的字段为必填项</li>
                                            <li>入住日期不能早于今天，退房日期必须晚于入住日期</li>
                                            <li>客人数量不能超过房间的最大容纳人数</li>
                                            <li>VIP客户将自动享受相应等级的价格折扣</li>
                                            <li>预订创建后状态为"待确认"，需要进一步确认</li>
                                            <li>总价格根据入住天数、房间价格和VIP折扣自动计算</li>
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

                        // 设置最小入住日期为今天
                        const today = new Date().toISOString().split('T')[0];
                        document.getElementById('checkInDate').min = today;
                        document.getElementById('checkOutDate').min = today;
                    });

                    // 显示客户信息
                    function showCustomerInfo() {
                        const customerSelect = document.getElementById('customerId');
                        const customerInfo = document.getElementById('customerInfo');
                        const selectionInfo = document.getElementById('selectionInfo');
                        const selectedOption = customerSelect.options[customerSelect.selectedIndex];

                        if (selectedOption.value) {
                            const name = selectedOption.getAttribute('data-name');
                            const phone = selectedOption.getAttribute('data-phone');
                            const isVip = selectedOption.getAttribute('data-vip') === 'true';
                            const vipLevel = selectedOption.getAttribute('data-vip-level');

                            document.getElementById('customerName').textContent = name;
                            document.getElementById('customerPhone').textContent = phone;
                            document.getElementById('customerVip').innerHTML = isVip ?
                                '<span class="badge bg-warning text-dark">VIP' + vipLevel + '</span>' :
                                '<span class="badge bg-secondary">普通会员</span>';

                            customerInfo.style.display = 'block';
                            selectionInfo.style.display = 'block';

                            calculateTotal();
                        } else {
                            customerInfo.style.display = 'none';
                            if (document.getElementById('roomInfo').style.display === 'none') {
                                selectionInfo.style.display = 'none';
                            }
                        }
                    }

                    // 显示房间信息
                    function showRoomInfo() {
                        const roomSelect = document.getElementById('roomId');
                        const roomInfo = document.getElementById('roomInfo');
                        const selectionInfo = document.getElementById('selectionInfo');
                        const guestsCount = document.getElementById('guestsCount');
                        const maxGuestsHint = document.getElementById('maxGuestsHint');
                        const selectedOption = roomSelect.options[roomSelect.selectedIndex];

                        if (selectedOption.value) {
                            const roomNumber = selectedOption.getAttribute('data-room-number');
                            const typeName = selectedOption.getAttribute('data-type-name');
                            const price = selectedOption.getAttribute('data-price');
                            const maxGuests = selectedOption.getAttribute('data-max-guests');
                            const bedCount = selectedOption.getAttribute('data-bed-count');

                            document.getElementById('roomNumber').textContent = roomNumber;
                            document.getElementById('roomTypeName').textContent = typeName;
                            document.getElementById('roomPrice').textContent = '¥' + parseFloat(price).toFixed(2) + '/晚';
                            document.getElementById('roomCapacity').textContent = maxGuests + '人 (' + bedCount + '床)';

                            // 更新客人数量限制
                            guestsCount.max = maxGuests;
                            maxGuestsHint.textContent = '最多容纳 ' + maxGuests + ' 人';

                            roomInfo.style.display = 'block';
                            selectionInfo.style.display = 'block';

                            calculateTotal();
                        } else {
                            roomInfo.style.display = 'none';
                            if (document.getElementById('customerInfo').style.display === 'none') {
                                selectionInfo.style.display = 'none';
                            }

                            guestsCount.max = 10;
                            maxGuestsHint.textContent = '请先选择房间查看最大容纳人数';
                        }
                    }

                    // 计算总价格
                    function calculateTotal() {
                        const checkInDate = document.getElementById('checkInDate').value;
                        const checkOutDate = document.getElementById('checkOutDate').value;
                        const roomSelect = document.getElementById('roomId');
                        const customerSelect = document.getElementById('customerId');
                        const totalPriceDisplay = document.getElementById('totalPriceDisplay');
                        const priceInfo = document.getElementById('priceCalculationInfo');

                        if (checkInDate && checkOutDate && roomSelect.value && customerSelect.value) {
                            const checkIn = new Date(checkInDate);
                            const checkOut = new Date(checkOutDate);
                            const timeDiff = checkOut.getTime() - checkIn.getTime();
                            const days = Math.ceil(timeDiff / (1000 * 3600 * 24));

                            if (days > 0) {
                                const roomOption = roomSelect.options[roomSelect.selectedIndex];
                                const customerOption = customerSelect.options[customerSelect.selectedIndex];
                                const roomPrice = parseFloat(roomOption.getAttribute('data-price'));
                                const isVip = customerOption.getAttribute('data-vip') === 'true';
                                const vipLevel = parseInt(customerOption.getAttribute('data-vip-level'));

                                let totalPrice = roomPrice * days;
                                let discount = 1.0;

                                // VIP折扣计算
                                if (isVip) {
                                    switch (vipLevel) {
                                        case 1:
                                            discount = 0.95;
                                            break;
                                        case 2:
                                            discount = 0.90;
                                            break;
                                        case 3:
                                            discount = 0.85;
                                            break;
                                        case 4:
                                            discount = 0.80;
                                            break;
                                        case 5:
                                            discount = 0.75;
                                            break;
                                    }
                                    totalPrice = totalPrice * discount;
                                }

                                totalPriceDisplay.value = totalPrice.toFixed(2);

                                let infoText = days + '晚 × ¥' + roomPrice.toFixed(2);
                                if (isVip) {
                                    infoText += ' × ' + (discount * 100).toFixed(0) + '%折扣';
                                }
                                priceInfo.textContent = infoText;
                            } else {
                                totalPriceDisplay.value = '';
                                priceInfo.textContent = '退房日期必须晚于入住日期';
                            }
                        } else {
                            totalPriceDisplay.value = '';
                            priceInfo.textContent = '价格将自动计算（含VIP折扣）';
                        }
                    }

                    // 重置表单
                    function resetForm() {
                        document.getElementById('customerInfo').style.display = 'none';
                        document.getElementById('roomInfo').style.display = 'none';
                        document.getElementById('selectionInfo').style.display = 'none';
                        document.getElementById('totalPriceDisplay').value = '';
                        document.getElementById('priceCalculationInfo').textContent = '价格将自动计算（含VIP折扣）';
                        document.getElementById('maxGuestsHint').textContent = '请先选择房间查看最大容纳人数';
                        document.getElementById('guestsCount').max = 10;
                    }

                    // 日期验证
                    document.getElementById('checkInDate').addEventListener('change', function() {
                        const checkInDate = this.value;
                        const checkOutDate = document.getElementById('checkOutDate');

                        if (checkInDate) {
                            // 设置退房日期的最小值为入住日期的下一天
                            const checkIn = new Date(checkInDate);
                            checkIn.setDate(checkIn.getDate() + 1);
                            checkOutDate.min = checkIn.toISOString().split('T')[0];

                            // 如果当前退房日期无效，清空
                            if (checkOutDate.value && checkOutDate.value <= checkInDate) {
                                checkOutDate.value = '';
                            }
                        }
                    });

                    // 客人数量验证
                    document.getElementById('guestsCount').addEventListener('input', function() {
                        const guestsCount = parseInt(this.value);
                        const maxGuests = parseInt(this.max);

                        if (guestsCount > maxGuests) {
                            this.setCustomValidity('客人数量不能超过房间最大容纳人数');
                        } else if (guestsCount < 1) {
                            this.setCustomValidity('客人数量至少为1人');
                        } else {
                            this.setCustomValidity('');
                        }
                    });
                </script>
            </body>

            </html>