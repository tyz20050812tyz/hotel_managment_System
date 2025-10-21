<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>未读消息 - 酒店管理系统</title>
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
                                            <a class="dropdown-item active" href="${pageContext.request.contextPath}/admin/message/unread">
                                                <i class="fas fa-envelope-open"></i> 未读消息
                                            </a>
                                        </li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/compose">
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
                                    <a href="${pageContext.request.contextPath}/admin/message/unread" class="list-group-item list-group-item-action active">
                                        <i class="fas fa-envelope-open"></i> 未读消息
                                        <c:if test="${not empty messages}">
                                            <span class="badge bg-warning float-end">${messages.size()}</span>
                                        </c:if>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/compose" class="list-group-item list-group-item-action">
                                        <i class="fas fa-edit"></i> 写消息
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- 主要内容 -->
                        <div class="col-md-9">
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">
                                        <i class="fas fa-envelope-open text-warning"></i> 未读消息
                                        <c:if test="${not empty messages}">
                                            <span class="badge bg-warning ms-2">${messages.size()}</span>
                                        </c:if>
                                    </h5>
                                    <div>
                                        <c:if test="${not empty messages}">
                                            <button type="button" class="btn btn-success btn-sm me-2" onclick="markAllAsRead()">
                                    <i class="fas fa-check-double"></i> 全部标记已读
                                </button>
                                        </c:if>
                                        <a href="${pageContext.request.contextPath}/admin/message/compose" class="btn btn-primary btn-sm">
                                            <i class="fas fa-edit"></i> 写消息
                                        </a>
                                    </div>
                                </div>
                                <div class="card-body p-0">
                                    <c:if test="${empty messages}">
                                        <div class="text-center py-5">
                                            <div class="mb-4">
                                                <i class="fas fa-check-circle fa-4x text-success"></i>
                                            </div>
                                            <h5 class="text-success mb-3">太棒了！</h5>
                                            <p class="text-muted mb-4">您已阅读了所有消息</p>
                                            <div>
                                                <a href="${pageContext.request.contextPath}/admin/message/inbox" class="btn btn-outline-primary me-2">
                                                    <i class="fas fa-inbox"></i> 查看收件箱
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/message/compose" class="btn btn-primary">
                                                    <i class="fas fa-edit"></i> 写新消息
                                                </a>
                                            </div>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty messages}">
                                        <!-- 批量操作工具栏 -->
                                        <div class="p-3 border-bottom bg-warning bg-opacity-10">
                                            <div class="d-flex align-items-center">
                                                <div class="form-check me-3">
                                                    <input class="form-check-input" type="checkbox" id="selectAll">
                                                    <label class="form-check-label" for="selectAll">全选</label>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-success me-2" onclick="markSelectedAsRead()">
                                        <i class="fas fa-check"></i> 标记已读
                                    </button>
                                                <button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteSelected()">
                                        <i class="fas fa-trash"></i> 删除选中
                                    </button>
                                            </div>
                                        </div>

                                        <!-- 消息列表 -->
                                        <div class="list-group list-group-flush">
                                            <c:forEach var="message" items="${messages}">
                                                <div class="list-group-item bg-light border-start border-warning border-3">
                                                    <div class="d-flex align-items-center">
                                                        <div class="form-check me-3">
                                                            <input class="form-check-input message-checkbox" type="checkbox" value="${message.messageId}">
                                                        </div>
                                                        <div class="flex-grow-1">
                                                            <div class="d-flex justify-content-between align-items-start">
                                                                <div class="flex-grow-1">
                                                                    <h6 class="mb-1 fw-bold">
                                                                        <i class="fas fa-envelope text-warning me-2"></i>
                                                                        <a href="${pageContext.request.contextPath}/admin/message/detail?id=${message.messageId}" class="text-decoration-none text-dark">
                                                                ${message.subject}
                                                            </a>
                                                                        <span class="badge bg-primary ms-2">新</span>
                                                                        <span class="${message.typeClass} ms-2">${message.messageType.description}</span>
                                                                        <c:if test="${message.priority.name() != 'NORMAL'}">
                                                                            <span class="badge ${message.priorityClass} ms-1">${message.priority.description}</span>
                                                                        </c:if>
                                                                    </h6>
                                                                    <p class="mb-1">
                                                                        <i class="fas fa-user text-primary"></i>
                                                                        <strong>${message.senderDisplayName}</strong>
                                                                    </p>
                                                                    <p class="mb-1 text-muted small" style="max-width: 500px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                                                        ${message.content}
                                                                    </p>
                                                                    <small class="text-muted">
                                                            <i class="fas fa-clock"></i>
                                                            <fmt:formatDate value="${message.sendTime}" pattern="yyyy-MM-dd HH:mm" />
                                                            <c:choose>
                                                                <c:when test="${message.priority.name() == 'URGENT'}">
                                                                    <span class="badge bg-danger ms-2">
                                                                        <i class="fas fa-exclamation-triangle"></i> 紧急
                                                                    </span>
                                                                </c:when>
                                                                <c:when test="${message.priority.name() == 'HIGH'}">
                                                                    <span class="badge bg-warning ms-2">
                                                                        <i class="fas fa-exclamation"></i> 重要
                                                                    </span>
                                                                </c:when>
                                                            </c:choose>
                                                        </small>
                                                                </div>
                                                                <div class="dropdown">
                                                                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            <i class="fas fa-ellipsis-v"></i>
                                                        </button>
                                                                    <ul class="dropdown-menu">
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/detail?id=${message.messageId}">
                                                                                <i class="fas fa-eye"></i> 查看并标记已读
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item" href="#" onclick="markAsRead(${message.messageId})">
                                                                                <i class="fas fa-check"></i> 仅标记已读
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/reply?id=${message.messageId}">
                                                                                <i class="fas fa-reply"></i> 直接回复
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <hr class="dropdown-divider">
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item text-danger" href="#" onclick="deleteMessage(${message.messageId})">
                                                                                <i class="fas fa-trash"></i> 删除
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>

                                        <!-- 底部提示 -->
                                        <div class="card-footer bg-warning bg-opacity-10">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <small class="text-muted">
                                        <i class="fas fa-info-circle"></i> 
                                        共有 ${messages.size()} 条未读消息
                                    </small>
                                                <button type="button" class="btn btn-success btn-sm" onclick="markAllAsRead()">
                                        <i class="fas fa-check-double"></i> 全部标记已读
                                    </button>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script>
                    // 全选功能
                    const selectAllCheckbox = document.getElementById('selectAll');
                    if (selectAllCheckbox) {
                        selectAllCheckbox.addEventListener('change', function() {
                            const checkboxes = document.querySelectorAll('.message-checkbox');
                            checkboxes.forEach(checkbox => {
                                checkbox.checked = this.checked;
                            });
                        });
                    }

                    // 全部标记为已读
                    function markAllAsRead() {
                        const messageCount = $ {
                            not empty messages ? messages.size() : 0
                        };
                        if (messageCount === 0) {
                            return;
                        }

                        Swal.fire({
                            title: '确认操作',
                            text: `确定要将所有 ${messageCount} 条未读消息标记为已读吗？`,
                            icon: 'question',
                            showCancelButton: true,
                            confirmButtonColor: '#28a745',
                            cancelButtonColor: '#6c757d',
                            confirmButtonText: '确定',
                            cancelButtonText: '取消'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                const allMessageIds = []; 
                                <c:forEach var="message" items="${messages}">
                                    allMessageIds.push('${message.messageId}'); 
                                </c:forEach>

                                fetch('${pageContext.request.contextPath}/admin/message/markReadBatch', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded',
                                        },
                                        body: 'messageIds=' + allMessageIds.join('&messageIds=')
                                    })
                                    .then(response => response.json())
                                    .then(data => {
                                        if (data.success) {
                                            Swal.fire('成功', '所有消息已标记为已读', 'success').then(() => {
                                                location.reload();
                                            });
                                        } else {
                                            Swal.fire('错误', data.message || '操作失败', 'error');
                                        }
                                    })
                                    .catch(error => {
                                        console.error('Error:', error);
                                        Swal.fire('错误', '网络错误', 'error');
                                    });
                            }
                        });
                    }

                    // 标记选中消息为已读
                    function markSelectedAsRead() {
                        const selectedIds = getSelectedMessageIds();
                        if (selectedIds.length === 0) {
                            Swal.fire('提示', '请选择要标记的消息', 'warning');
                            return;
                        }

                        fetch('${pageContext.request.contextPath}/admin/message/markReadBatch', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'messageIds=' + selectedIds.join('&messageIds=')
                            })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    Swal.fire('成功', '标记成功', 'success').then(() => {
                                        location.reload();
                                    });
                                } else {
                                    Swal.fire('错误', data.message || '标记失败', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                Swal.fire('错误', '网络错误', 'error');
                            });
                    }

                    // 删除选中消息
                    function deleteSelected() {
                        const selectedIds = getSelectedMessageIds();
                        if (selectedIds.length === 0) {
                            Swal.fire('提示', '请选择要删除的消息', 'warning');
                            return;
                        }

                        Swal.fire({
                            title: '确认删除',
                            text: `确定要删除选中的 ${selectedIds.length} 条消息吗？`,
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#d33',
                            cancelButtonColor: '#3085d6',
                            confirmButtonText: '删除',
                            cancelButtonText: '取消'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                fetch('${pageContext.request.contextPath}/admin/message/deleteBatch', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded',
                                        },
                                        body: 'messageIds=' + selectedIds.join('&messageIds=')
                                    })
                                    .then(response => response.json())
                                    .then(data => {
                                        if (data.success) {
                                            Swal.fire('成功', '删除成功', 'success').then(() => {
                                                location.reload();
                                            });
                                        } else {
                                            Swal.fire('错误', data.message || '删除失败', 'error');
                                        }
                                    })
                                    .catch(error => {
                                        console.error('Error:', error);
                                        Swal.fire('错误', '网络错误', 'error');
                                    });
                            }
                        });
                    }

                    // 获取选中的消息ID
                    function getSelectedMessageIds() {
                        const checkboxes = document.querySelectorAll('.message-checkbox:checked');
                        return Array.from(checkboxes).map(cb => cb.value);
                    }

                    // 标记单条消息为已读
                    function markAsRead(messageId) {
                        fetch('${pageContext.request.contextPath}/admin/message/markRead', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'id=' + messageId
                            })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    location.reload();
                                } else {
                                    Swal.fire('错误', data.message || '操作失败', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                Swal.fire('错误', '网络错误', 'error');
                            });
                    }

                    // 删除单条消息
                    function deleteMessage(messageId) {
                        Swal.fire({
                            title: '确认删除',
                            text: '确定要删除这条消息吗？',
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#d33',
                            cancelButtonColor: '#3085d6',
                            confirmButtonText: '删除',
                            cancelButtonText: '取消'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                fetch('${pageContext.request.contextPath}/admin/message/delete', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded',
                                        },
                                        body: 'id=' + messageId
                                    })
                                    .then(response => response.json())
                                    .then(data => {
                                        if (data.success) {
                                            Swal.fire('成功', '删除成功', 'success').then(() => {
                                                location.reload();
                                            });
                                        } else {
                                            Swal.fire('错误', data.message || '删除失败', 'error');
                                        }
                                    })
                                    .catch(error => {
                                        console.error('Error:', error);
                                        Swal.fire('错误', '网络错误', 'error');
                                    });
                            }
                        });
                    }
                </script>
            </body>

            </html>