<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>写消息 - 酒店管理系统</title>
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
                                        <i class="fas fa-home"></i> 首页
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/booking/list">
                                        <i class="fas fa-calendar-check"></i> 预订管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/room/list">
                                        <i class="fas fa-bed"></i> 房间管理
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/customer/list">
                                        <i class="fas fa-users"></i> 客户管理
                                    </a>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle active" href="#" id="messageDropdown" role="button" data-bs-toggle="dropdown">
                                        <i class="fas fa-envelope"></i> 留言
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/inbox">
                                                <i class="fas fa-inbox"></i> 收件箱
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/outbox">
                                                <i class="fas fa-paper-plane"></i> 发件箱
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/unread">
                                                <i class="fas fa-envelope-open"></i> 未读消息
                                            </a>
                                        </li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                            <a class="dropdown-item active" href="${pageContext.request.contextPath}/admin/message/compose">
                                                <i class="fas fa-edit"></i> 写消息
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/user/list">
                                            <i class="fas fa-user-cog"></i> 用户管理
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                        <i class="fas fa-user-circle"></i> ${sessionScope.user.realName}
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                                <i class="fas fa-sign-out-alt"></i> 退出登录
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid mt-4">
                    <div class="row">
                        <!-- 侧边栏 -->
                        <div class="col-md-3">
                            <div class="card">
                                <div class="card-header">
                                    <h6 class="mb-0"><i class="fas fa-envelope"></i> 留言功能</h6>
                                </div>
                                <div class="list-group list-group-flush">
                                    <a href="${pageContext.request.contextPath}/admin/message/inbox" class="list-group-item list-group-item-action">
                                        <i class="fas fa-inbox"></i> 收件箱
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/outbox" class="list-group-item list-group-item-action">
                                        <i class="fas fa-paper-plane"></i> 发件箱
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/unread" class="list-group-item list-group-item-action">
                                        <i class="fas fa-envelope-open"></i> 未读消息
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/compose" class="list-group-item list-group-item-action active">
                                        <i class="fas fa-edit"></i> 写消息
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- 主要内容 -->
                        <div class="col-md-9">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-edit"></i>
                                        <c:choose>
                                            <c:when test="${not empty parentMessage}">回复消息</c:when>
                                            <c:otherwise>写消息</c:otherwise>
                                        </c:choose>
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <!-- 原消息信息（回复时显示） -->
                                    <c:if test="${not empty parentMessage}">
                                        <div class="alert alert-info mb-4">
                                            <h6><i class="fas fa-reply"></i> 回复消息</h6>
                                            <div class="border-start border-3 border-info ps-3">
                                                <strong>原消息：</strong> ${parentMessage.subject}<br>
                                                <strong>发送者：</strong> ${parentMessage.senderDisplayName}<br>
                                                <strong>发送时间：</strong>
                                                <fmt:formatDate value="${parentMessage.sendTime}" pattern="yyyy-MM-dd HH:mm" /><br>
                                                <div class="mt-2">
                                                    <small class="text-muted">${parentMessage.content}</small>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>

                                    <form id="messageForm" method="post" action="${pageContext.request.contextPath}/admin/message/send">
                                        <c:if test="${not empty parentMessage}">
                                            <input type="hidden" name="parentMessageId" value="${parentMessage.messageId}">
                                        </c:if>

                                        <div class="row mb-3">
                                            <label for="receiverId" class="col-sm-2 col-form-label">收件人 <span class="text-danger">*</span></label>
                                            <div class="col-sm-10">
                                                <select class="form-select" id="receiverId" name="receiverId" required>
                                        <option value="">请选择收件人</option>
                                        <c:forEach var="user" items="${users}">
                                            <option value="${user.userId}" ${user.userId == receiverId ? 'selected' : ''}>
                                                ${user.realName} (${user.username}) - ${user.role.description}
                                            </option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="subject" class="col-sm-2 col-form-label">主题 <span class="text-danger">*</span></label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" id="subject" name="subject" value="${defaultSubject}" placeholder="请输入消息主题" maxlength="100" required>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="priority" class="col-sm-2 col-form-label">优先级</label>
                                            <div class="col-sm-10">
                                                <select class="form-select" id="priority" name="priority">
                                        <option value="LOW">低</option>
                                        <option value="NORMAL" selected>普通</option>
                                        <option value="HIGH">高</option>
                                        <option value="URGENT">紧急</option>
                                    </select>
                                            </div>
                                        </div>

                                        <div class="row mb-4">
                                            <label for="content" class="col-sm-2 col-form-label">内容 <span class="text-danger">*</span></label>
                                            <div class="col-sm-10">
                                                <textarea class="form-control" id="content" name="content" rows="8" placeholder="请输入消息内容..." maxlength="10000" required></textarea>
                                                <div class="form-text">最多10000个字符</div>
                                            </div>
                                        </div>

                                        <!-- 管理员群发功能 -->
                                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                            <div class="row mb-3">
                                                <div class="col-sm-2"></div>
                                                <div class="col-sm-10">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" id="isBroadcast" onchange="toggleBroadcast()">
                                                        <label class="form-check-label" for="isBroadcast">
                                                <i class="fas fa-bullhorn"></i> 群发消息
                                            </label>
                                                    </div>
                                                </div>
                                            </div>

                                            <div id="broadcastOptions" class="row mb-3" style="display: none;">
                                                <label for="targetRole" class="col-sm-2 col-form-label">目标用户</label>
                                                <div class="col-sm-10">
                                                    <select class="form-select" id="targetRole" name="targetRole">
                                            <option value="">所有用户</option>
                                            <option value="ADMIN">管理员</option>
                                            <option value="STAFF">员工</option>
                                        </select>
                                                </div>
                                            </div>
                                        </c:if>

                                        <div class="row">
                                            <div class="col-sm-2"></div>
                                            <div class="col-sm-10">
                                                <button type="submit" class="btn btn-primary me-2">
                                        <i class="fas fa-paper-plane"></i> 发送
                                    </button>
                                                <button type="button" class="btn btn-secondary me-2" onclick="saveDraft()">
                                        <i class="fas fa-save"></i> 保存草稿
                                    </button>
                                                <a href="${pageContext.request.contextPath}/admin/message/inbox" class="btn btn-outline-secondary">
                                                    <i class="fas fa-times"></i> 取消
                                                </a>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script>
                    // 群发功能切换
                    function toggleBroadcast() {
                        const isBroadcast = document.getElementById('isBroadcast').checked;
                        const broadcastOptions = document.getElementById('broadcastOptions');
                        const receiverSelect = document.getElementById('receiverId');

                        if (isBroadcast) {
                            broadcastOptions.style.display = 'flex';
                            receiverSelect.disabled = true;
                            receiverSelect.removeAttribute('required');
                        } else {
                            broadcastOptions.style.display = 'none';
                            receiverSelect.disabled = false;
                            receiverSelect.setAttribute('required', 'required');
                        }
                    }

                    // 表单提交处理
                    document.getElementById('messageForm').addEventListener('submit', function(e) {
                        e.preventDefault();

                        const formData = new FormData(this);
                        const isBroadcast = document.getElementById('isBroadcast').checked;

                        // 验证表单
                        if (!isBroadcast && !formData.get('receiverId')) {
                            Swal.fire('错误', '请选择收件人', 'error');
                            return;
                        }

                        if (!formData.get('subject')) {
                            Swal.fire('错误', '请输入主题', 'error');
                            return;
                        }

                        if (!formData.get('content')) {
                            Swal.fire('错误', '请输入内容', 'error');
                            return;
                        }

                        // 确定提交URL
                        let submitUrl = '${pageContext.request.contextPath}/admin/message/send';
                        if (isBroadcast) {
                            submitUrl = '${pageContext.request.contextPath}/admin/message/broadcast';
                        }

                        // 显示发送中提示
                        Swal.fire({
                            title: '发送中...',
                            text: '请稍候',
                            allowOutsideClick: false,
                            showConfirmButton: false,
                            willOpen: () => {
                                Swal.showLoading();
                            }
                        });

                        // 提交表单
                        fetch(submitUrl, {
                                method: 'POST',
                                body: new URLSearchParams(formData)
                            })
                            .then(response => response.json())
                            .then(data => {
                                Swal.close();
                                if (data.success) {
                                    Swal.fire({
                                        title: '成功',
                                        text: isBroadcast ? data.message : '消息发送成功',
                                        icon: 'success',
                                        confirmButtonText: '确定'
                                    }).then(() => {
                                        window.location.href = '${pageContext.request.contextPath}/admin/message/inbox';
                                    });
                                } else {
                                    Swal.fire('错误', data.message || '发送失败', 'error');
                                }
                            })
                            .catch(error => {
                                Swal.close();
                                console.error('Error:', error);
                                Swal.fire('错误', '网络错误，请重试', 'error');
                            });
                    });

                    // 保存草稿功能
                    function saveDraft() {
                        const formData = new FormData(document.getElementById('messageForm'));

                        // 这里可以实现保存草稿功能
                        // 暂时只显示提示
                        Swal.fire('提示', '草稿功能将在后续版本中实现', 'info');
                    }

                    // 字符计数功能
                    document.getElementById('content').addEventListener('input', function() {
                        const maxLength = 10000;
                        const currentLength = this.value.length;
                        const remaining = maxLength - currentLength;

                        // 可以在这里添加字符计数显示
                        if (remaining < 100) {
                            this.style.borderColor = remaining < 0 ? 'red' : 'orange';
                        } else {
                            this.style.borderColor = '';
                        }
                    });

                    // 快捷键支持
                    document.addEventListener('keydown', function(e) {
                        if (e.ctrlKey && e.key === 'Enter') {
                            document.getElementById('messageForm').dispatchEvent(new Event('submit'));
                        }
                    });
                </script>
            </body>

            </html>