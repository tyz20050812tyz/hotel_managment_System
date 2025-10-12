<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>收件箱 - 酒店管理系统</title>
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
                                        <c:if test="${unreadCount > 0}">
                                            <span class="badge bg-danger ms-1">${unreadCount}</span>
                                        </c:if>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item active" href="${pageContext.request.contextPath}/admin/message/inbox">
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
                                    <a href="${pageContext.request.contextPath}/admin/message/inbox" class="list-group-item list-group-item-action active">
                                        <i class="fas fa-inbox"></i> 收件箱
                                        <c:if test="${unreadCount > 0}">
                                            <span class="badge bg-danger float-end">${unreadCount}</span>
                                        </c:if>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/outbox" class="list-group-item list-group-item-action">
                                        <i class="fas fa-paper-plane"></i> 发件箱
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/unread" class="list-group-item list-group-item-action">
                                        <i class="fas fa-envelope-open"></i> 未读消息
                                        <c:if test="${unreadCount > 0}">
                                            <span class="badge bg-warning float-end">${unreadCount}</span>
                                        </c:if>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/message/compose" class="list-group-item list-group-item-action">
                                        <i class="fas fa-edit"></i> 写消息
                                    </a>
                                </div>
                            </div>

                            <!-- 搜索框 -->
                            <div class="card mt-3">
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/admin/message/search" method="get">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="keyword" placeholder="搜索消息..." value="${searchKeyword}">
                                            <button class="btn btn-outline-primary" type="submit">
                                    <i class="fas fa-search"></i>
                                </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- 主要内容 -->
                        <div class="col-md-9">
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">
                                        <i class="fas fa-inbox"></i> 收件箱
                                        <c:if test="${not empty selectedType}">
                                            - ${selectedType == 'system' ? '系统消息' : (selectedType == 'admin' ? '管理员消息' : '全部消息')}
                                        </c:if>
                                    </h5>
                                    <div class="btn-group" role="group">
                                        <a href="${pageContext.request.contextPath}/admin/message/inbox" class="btn btn-sm ${empty selectedType ? 'btn-primary' : 'btn-outline-primary'}">全部</a>
                                        <a href="${pageContext.request.contextPath}/admin/message/inbox?type=system" class="btn btn-sm ${selectedType == 'system' ? 'btn-info' : 'btn-outline-info'}">系统</a>
                                        <a href="${pageContext.request.contextPath}/admin/message/inbox?type=admin" class="btn btn-sm ${selectedType == 'admin' ? 'btn-warning' : 'btn-outline-warning'}">管理员</a>
                                    </div>
                                </div>
                                <div class="card-body p-0">
                                    <c:if test="${empty messages}">
                                        <div class="text-center py-5">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">收件箱为空</p>
                                            <a href="${pageContext.request.contextPath}/admin/message/compose" class="btn btn-primary">
                                                <i class="fas fa-edit"></i> 写第一条消息
                                            </a>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty messages}">
                                        <!-- 批量操作工具栏 -->
                                        <div class="p-3 border-bottom bg-light">
                                            <div class="d-flex align-items-center">
                                                <div class="form-check me-3">
                                                    <input class="form-check-input" type="checkbox" id="selectAll">
                                                    <label class="form-check-label" for="selectAll">全选</label>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-outline-success me-2" onclick="markSelectedAsRead()">
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
                                                <div class="list-group-item ${message.isRead ? '' : 'bg-light border-start border-primary border-3'}">
                                                    <div class="d-flex align-items-center">
                                                        <div class="form-check me-3">
                                                            <input class="form-check-input message-checkbox" type="checkbox" value="${message.messageId}">
                                                        </div>
                                                        <div class="flex-grow-1">
                                                            <div class="d-flex justify-content-between align-items-start">
                                                                <div class="flex-grow-1">
                                                                    <h6 class="mb-1">
                                                                        <a href="${pageContext.request.contextPath}/admin/message/detail?id=${message.messageId}" class="text-decoration-none ${message.isRead ? 'text-muted' : 'text-dark fw-bold'}">
                                                                ${message.subject}
                                                            </a>
                                                                        <c:if test="${not message.isRead}">
                                                                            <span class="badge bg-primary ms-2">新</span>
                                                                        </c:if>
                                                                        <span class="${message.typeClass} ms-2">${message.messageType.description}</span>
                                                                        <c:if test="${message.priority.name() != 'NORMAL'}">
                                                                            <span class="badge ${message.priorityClass} ms-1">${message.priority.description}</span>
                                                                        </c:if>
                                                                    </h6>
                                                                    <p class="mb-1 text-muted">
                                                                        <i class="fas fa-user"></i> ${message.senderDisplayName}
                                                                    </p>
                                                                    <small class="text-muted">
                                                            <i class="fas fa-clock"></i>
                                                            <fmt:formatDate value="${message.sendTime}" pattern="yyyy-MM-dd HH:mm" />
                                                        </small>
                                                                </div>
                                                                <div class="dropdown">
                                                                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            <i class="fas fa-ellipsis-v"></i>
                                                        </button>
                                                                    <ul class="dropdown-menu">
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/detail?id=${message.messageId}">
                                                                                <i class="fas fa-eye"></i> 查看
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/reply?id=${message.messageId}">
                                                                                <i class="fas fa-reply"></i> 回复
                                                                            </a>
                                                                        </li>
                                                                        <c:if test="${not message.isRead}">
                                                                            <li>
                                                                                <a class="dropdown-item" href="#" onclick="markAsRead(${message.messageId})">
                                                                                    <i class="fas fa-check"></i> 标记已读
                                                                                </a>
                                                                            </li>
                                                                        </c:if>
                                                                        <li>
                                                                            <a class="dropdown-item text-danger" href="#" onclick="deleteMessage(${message.messageId})">
                                                                                <i class="fas fa-trash"></i> 删除
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <hr class="dropdown-divider">
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/conversation?userId=${message.senderId}">
                                                                                <i class="fas fa-comments"></i> 查看对话
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

                                        <!-- 分页 -->
                                        <c:if test="${totalPages > 1}">
                                            <div class="card-footer">
                                                <nav aria-label="消息分页">
                                                    <ul class="pagination justify-content-center mb-0">
                                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                            <a class="page-link" href="?page=${currentPage - 1}&size=${pageSize}${not empty selectedType ? '&type=' + selectedType : ''}">上一页</a>
                                                        </li>
                                                        <c:forEach begin="1" end="${totalPages}" var="page">
                                                            <li class="page-item ${page == currentPage ? 'active' : ''}">
                                                                <a class="page-link" href="?page=${page}&size=${pageSize}${not empty selectedType ? '&type=' + selectedType : ''}">${page}</a>
                                                            </li>
                                                        </c:forEach>
                                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                            <a class="page-link" href="?page=${currentPage + 1}&size=${pageSize}${not empty selectedType ? '&type=' + selectedType : ''}">下一页</a>
                                                        </li>
                                                    </ul>
                                                </nav>
                                                <small class="text-muted d-block text-center mt-2">
                                        显示第 ${(currentPage - 1) * pageSize + 1} - ${currentPage * pageSize > totalCount ? totalCount : currentPage * pageSize} 条，共 ${totalCount} 条消息
                                    </small>
                                            </div>
                                        </c:if>
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
                    document.getElementById('selectAll').addEventListener('change', function() {
                        const checkboxes = document.querySelectorAll('.message-checkbox');
                        checkboxes.forEach(checkbox => {
                            checkbox.checked = this.checked;
                        });
                    });

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