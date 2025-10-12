<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>消息详情 - 酒店管理系统</title>
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
                                    <a href="${pageContext.request.contextPath}/admin/message/unread" class="list-group-item list-group-item-action">
                                        <i class="fas fa-envelope-open"></i> 未读消息
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
                                        <i class="fas fa-envelope-open"></i> 消息详情
                                    </h5>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/admin/message/reply?id=${message.messageId}" class="btn btn-sm btn-primary">
                                            <i class="fas fa-reply"></i> 回复
                                        </a>
                                        <c:if test="${message.receiverId == sessionScope.user.userId}">
                                            <a href="${pageContext.request.contextPath}/admin/message/inbox" class="btn btn-sm btn-outline-secondary">
                                                <i class="fas fa-arrow-left"></i> 返回收件箱
                                            </a>
                                        </c:if>
                                        <c:if test="${message.senderId == sessionScope.user.userId}">
                                            <a href="${pageContext.request.contextPath}/admin/message/outbox" class="btn btn-sm btn-outline-secondary">
                                                <i class="fas fa-arrow-left"></i> 返回发件箱
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <!-- 消息头部信息 -->
                                    <div class="message-header mb-4">
                                        <div class="row">
                                            <div class="col-md-8">
                                                <h4 class="message-subject mb-3">
                                                    ${message.subject}
                                                    <c:if test="${not message.isRead && message.receiverId == sessionScope.user.userId}">
                                                        <span class="badge bg-primary ms-2">新消息</span>
                                                    </c:if>
                                                    <span class="${message.typeClass} ms-2">${message.messageType.description}</span>
                                                    <c:if test="${message.priority.name() != 'NORMAL'}">
                                                        <span class="badge ${message.priorityClass} ms-1">${message.priority.description}</span>
                                                    </c:if>
                                                </h4>
                                            </div>
                                            <div class="col-md-4 text-end">
                                                <div class="dropdown">
                                                    <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                            <i class="fas fa-ellipsis-v"></i> 操作
                                        </button>
                                                    <ul class="dropdown-menu">
                                                        <li>
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/reply?id=${message.messageId}">
                                                                <i class="fas fa-reply"></i> 回复
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/conversation?userId=${message.receiverId == sessionScope.user.userId ? message.senderId : message.receiverId}">
                                                                <i class="fas fa-comments"></i> 查看对话
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

                                        <div class="message-meta bg-light p-3 rounded">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <p class="mb-2">
                                                        <strong><i class="fas fa-user"></i> 发送者：</strong>
                                                        <span class="text-primary">${message.senderDisplayName}</span>
                                                    </p>
                                                    <p class="mb-2">
                                                        <strong><i class="fas fa-user-check"></i> 接收者：</strong>
                                                        <span class="text-success">${message.receiverDisplayName}</span>
                                                    </p>
                                                </div>
                                                <div class="col-md-6">
                                                    <p class="mb-2">
                                                        <strong><i class="fas fa-clock"></i> 发送时间：</strong>
                                                        <fmt:formatDate value="${message.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                    </p>
                                                    <c:if test="${message.isRead && message.readTime != null}">
                                                        <p class="mb-2">
                                                            <strong><i class="fas fa-eye"></i> 阅读时间：</strong>
                                                            <fmt:formatDate value="${message.readTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                        </p>
                                                    </c:if>
                                                    <c:if test="${not message.isRead && message.receiverId == sessionScope.user.userId}">
                                                        <p class="mb-2 text-warning">
                                                            <i class="fas fa-envelope"></i> 未读消息
                                                        </p>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 消息内容 -->
                                    <div class="message-content">
                                        <div class="card">
                                            <div class="card-body">
                                                <div class="message-text" style="white-space: pre-wrap; line-height: 1.6;">
                                                    ${message.content}
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 回复列表 -->
                                    <c:if test="${not empty replies}">
                                        <div class="replies-section mt-4">
                                            <h6 class="border-bottom pb-2 mb-3">
                                                <i class="fas fa-comments"></i> 回复 (${replies.size()})
                                            </h6>
                                            <c:forEach var="reply" items="${replies}">
                                                <div class="reply-item mb-3">
                                                    <div class="card border-start border-4 border-info">
                                                        <div class="card-body">
                                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                                <div>
                                                                    <h6 class="mb-1">
                                                                        <a href="${pageContext.request.contextPath}/admin/message/detail?id=${reply.messageId}" class="text-decoration-none">
                                                                ${reply.subject}
                                                            </a>
                                                                        <c:if test="${not reply.isRead && reply.receiverId == sessionScope.user.userId}">
                                                                            <span class="badge bg-primary ms-2">新</span>
                                                                        </c:if>
                                                                    </h6>
                                                                    <p class="mb-1 text-muted">
                                                                        <i class="fas fa-user"></i> ${reply.senderDisplayName}
                                                                        <small class="ms-2">
                                                                <i class="fas fa-clock"></i>
                                                                <fmt:formatDate value="${reply.sendTime}" pattern="MM-dd HH:mm" />
                                                            </small>
                                                                    </p>
                                                                </div>
                                                                <div class="dropdown">
                                                                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            <i class="fas fa-ellipsis-v"></i>
                                                        </button>
                                                                    <ul class="dropdown-menu">
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/detail?id=${reply.messageId}">
                                                                                <i class="fas fa-eye"></i> 查看详情
                                                                            </a>
                                                                        </li>
                                                                        <li>
                                                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/message/reply?id=${reply.messageId}">
                                                                                <i class="fas fa-reply"></i> 回复
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                            </div>
                                                            <div class="reply-content text-muted" style="white-space: pre-wrap;">
                                                                ${reply.content}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </div>
                                <div class="card-footer">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/admin/message/reply?id=${message.messageId}" class="btn btn-primary">
                                                <i class="fas fa-reply"></i> 回复此消息
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/message/conversation?userId=${message.receiverId == sessionScope.user.userId ? message.senderId : message.receiverId}" class="btn btn-outline-info">
                                                <i class="fas fa-comments"></i> 查看完整对话
                                            </a>
                                        </div>
                                        <div>
                                            <button type="button" class="btn btn-outline-danger" onclick="deleteMessage(${message.messageId})">
                                    <i class="fas fa-trash"></i> 删除
                                </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script>
                    // 删除消息
                    function deleteMessage(messageId) {
                        Swal.fire({
                            title: '确认删除',
                            text: '确定要删除这条消息吗？删除后将无法恢复。',
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
                                            Swal.fire('成功', '消息已删除', 'success').then(() => {
                                                // 根据用户角色返回不同页面
                                                const isReceiver = $ {
                                                    message.receiverId == sessionScope.user.userId
                                                };
                                                if (isReceiver) {
                                                    window.location.href = '${pageContext.request.contextPath}/admin/message/inbox';
                                                } else {
                                                    window.location.href = '${pageContext.request.contextPath}/admin/message/outbox';
                                                }
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

                    // 自动标记为已读（如果是接收者且未读）
                    <
                    c: if test = "${not message.isRead && message.receiverId == sessionScope.user.userId}" >
                        // 页面加载后自动标记为已读
                        window.addEventListener('load', function() {
                            fetch('${pageContext.request.contextPath}/admin/message/markRead', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'id=${message.messageId}'
                            }).catch(error => {
                                console.error('Mark as read error:', error);
                            });
                        }); <
                    /c:if>
                </script>
            </body>

            </html>