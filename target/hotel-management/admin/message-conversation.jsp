<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="zh-CN">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>对话详情 - 酒店管理系统</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
                <style>
                    .conversation-container {
                        max-height: 600px;
                        overflow-y: auto;
                    }
                    
                    .message-bubble {
                        margin-bottom: 1.5rem;
                        display: flex;
                    }
                    
                    .message-bubble.sent {
                        justify-content: flex-end;
                    }
                    
                    .message-bubble.received {
                        justify-content: flex-start;
                    }
                    
                    .message-content-wrapper {
                        max-width: 70%;
                        min-width: 200px;
                    }
                    
                    .message-bubble.sent .message-content-wrapper {
                        margin-left: auto;
                    }
                    
                    .message-bubble.received .message-content-wrapper {
                        margin-right: auto;
                    }
                    
                    .message-content {
                        padding: 1rem;
                        border-radius: 10px;
                        position: relative;
                    }
                    
                    .message-bubble.sent .message-content {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        border-bottom-right-radius: 2px;
                    }
                    
                    .message-bubble.received .message-content {
                        background: #f1f3f4;
                        color: #333;
                        border-bottom-left-radius: 2px;
                    }
                    
                    .message-header {
                        font-size: 0.85rem;
                        margin-bottom: 0.5rem;
                        opacity: 0.9;
                    }
                    
                    .message-bubble.sent .message-header {
                        text-align: right;
                        color: rgba(255, 255, 255, 0.9);
                    }
                    
                    .message-bubble.received .message-header {
                        text-align: left;
                        color: #666;
                    }
                    
                    .message-text {
                        white-space: pre-wrap;
                        word-wrap: break-word;
                        line-height: 1.5;
                    }
                    
                    .message-footer {
                        font-size: 0.75rem;
                        margin-top: 0.5rem;
                        opacity: 0.8;
                    }
                    
                    .message-bubble.sent .message-footer {
                        text-align: right;
                        color: rgba(255, 255, 255, 0.8);
                    }
                    
                    .message-bubble.received .message-footer {
                        text-align: left;
                        color: #999;
                    }
                    
                    .message-badge {
                        display: inline-block;
                        padding: 0.25rem 0.5rem;
                        border-radius: 12px;
                        font-size: 0.7rem;
                        margin-left: 0.5rem;
                    }
                    
                    .quick-reply-section {
                        position: sticky;
                        bottom: 0;
                        background: white;
                        border-top: 2px solid #e9ecef;
                        padding: 1rem;
                        box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
                    }
                    
                    .avatar {
                        width: 40px;
                        height: 40px;
                        border-radius: 50%;
                        display: inline-flex;
                        align-items: center;
                        justify-content: center;
                        font-weight: bold;
                        color: white;
                        margin: 0 0.5rem;
                    }
                    
                    .avatar.sent {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    }
                    
                    .avatar.received {
                        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                    }
                    
                    .date-divider {
                        text-align: center;
                        margin: 2rem 0 1rem;
                        position: relative;
                    }
                    
                    .date-divider::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 50%;
                        width: 100%;
                        height: 1px;
                        background: #dee2e6;
                        z-index: 0;
                    }
                    
                    .date-divider span {
                        background: white;
                        padding: 0.25rem 1rem;
                        position: relative;
                        z-index: 1;
                        color: #6c757d;
                        font-size: 0.85rem;
                    }
                    
                    .unread-indicator {
                        display: inline-block;
                        width: 8px;
                        height: 8px;
                        background: #007bff;
                        border-radius: 50%;
                        margin-left: 0.5rem;
                    }
                </style>
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

                            <!-- 对话参与者信息 -->
                            <div class="card mt-3">
                                <div class="card-header">
                                    <h6 class="mb-0"><i class="fas fa-user"></i> 对话对象</h6>
                                </div>
                                <div class="card-body">
                                    <div class="text-center mb-3">
                                        <div class="avatar received mx-auto" style="width: 60px; height: 60px; font-size: 1.5rem;">
                                            ${otherUser.realName.substring(0, 1)}
                                        </div>
                                    </div>
                                    <p class="mb-1 text-center">
                                        <strong>${otherUser.realName}</strong>
                                    </p>
                                    <p class="mb-1 text-center text-muted">
                                        <small>@${otherUser.username}</small>
                                    </p>
                                    <p class="mb-1 text-center">
                                        <span class="badge bg-info">${otherUser.role.description}</span>
                                    </p>
                                    <hr>
                                    <p class="mb-1 text-muted">
                                        <i class="fas fa-comments"></i>
                                        <small>共 ${conversation.size()} 条消息</small>
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- 对话内容 -->
                        <div class="col-md-9">
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">
                                        <i class="fas fa-comments"></i> 与 <strong>${otherUser.realName}</strong> 的对话
                                    </h5>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/admin/message/compose?receiverId=${otherUser.userId}" class="btn btn-sm btn-primary">
                                            <i class="fas fa-paper-plane"></i> 发送新消息
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/message/inbox" class="btn btn-sm btn-outline-secondary">
                                            <i class="fas fa-arrow-left"></i> 返回
                                        </a>
                                    </div>
                                </div>

                                <div class="card-body p-0">
                                    <c:choose>
                                        <c:when test="${empty conversation}">
                                            <div class="text-center py-5">
                                                <i class="fas fa-comments fa-3x text-muted mb-3"></i>
                                                <h5 class="text-muted">暂无对话记录</h5>
                                                <p class="text-muted">开始与 ${otherUser.realName} 的第一次对话吧！</p>
                                                <a href="${pageContext.request.contextPath}/admin/message/compose?receiverId=${otherUser.userId}" class="btn btn-primary">
                                                    <i class="fas fa-edit"></i> 发送消息
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="conversation-container p-4">
                                                <c:set var="lastDate" value="" />
                                                <c:forEach var="msg" items="${conversation}">
                                                    <%-- 日期分隔符 --%>
                                                        <fmt:formatDate value="${msg.sendTime}" pattern="yyyy-MM-dd" var="currentDate" />
                                                        <c:if test="${currentDate != lastDate}">
                                                            <div class="date-divider">
                                                                <span>
                                                    <fmt:formatDate value="${msg.sendTime}" pattern="yyyy年MM月dd日" />
                                                </span>
                                                            </div>
                                                            <c:set var="lastDate" value="${currentDate}" />
                                                        </c:if>

                                                        <%-- 消息气泡 --%>
                                                            <div class="message-bubble ${msg.senderId == currentUser.userId ? 'sent' : 'received'}">
                                                                <div class="message-content-wrapper">
                                                                    <div class="message-header">
                                                                        <c:choose>
                                                                            <c:when test="${msg.senderId == currentUser.userId}">
                                                                                <i class="fas fa-user"></i> 我
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <i class="fas fa-user"></i> ${otherUser.realName}
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <c:if test="${msg.priority.name() == 'HIGH'}">
                                                                            <span class="message-badge bg-warning text-dark">
                                                            <i class="fas fa-exclamation-circle"></i> 高优先级
                                                        </span>
                                                                        </c:if>
                                                                        <c:if test="${msg.priority.name() == 'URGENT'}">
                                                                            <span class="message-badge bg-danger">
                                                            <i class="fas fa-exclamation-triangle"></i> 紧急
                                                        </span>
                                                                        </c:if>
                                                                    </div>

                                                                    <div class="message-content">
                                                                        <div class="message-subject mb-2" style="font-weight: bold;">
                                                                            ${msg.subject}
                                                                        </div>
                                                                        <div class="message-text">
                                                                            ${msg.content}
                                                                        </div>
                                                                        <div class="message-footer">
                                                                            <i class="fas fa-clock"></i>
                                                                            <fmt:formatDate value="${msg.sendTime}" pattern="HH:mm" />
                                                                            <c:if test="${msg.senderId == currentUser.userId}">
                                                                                <c:choose>
                                                                                    <c:when test="${msg.isRead}">
                                                                                        <i class="fas fa-check-double ms-2" title="已读"></i>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <i class="fas fa-check ms-2" title="未读"></i>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </c:if>
                                                                            <c:if test="${msg.receiverId == currentUser.userId && !msg.isRead}">
                                                                                <span class="unread-indicator" title="未读"></span>
                                                                            </c:if>
                                                                        </div>
                                                                    </div>

                                                                    <div class="mt-2">
                                                                        <a href="${pageContext.request.contextPath}/admin/message/detail?id=${msg.messageId}" class="btn btn-sm btn-outline-secondary me-1" title="查看详情">
                                                                            <i class="fas fa-eye"></i>
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/admin/message/reply?id=${msg.messageId}" class="btn btn-sm btn-outline-primary" title="回复">
                                                                            <i class="fas fa-reply"></i>
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                </c:forEach>
                                            </div>

                                            <!-- 快速回复区域 -->
                                            <div class="quick-reply-section">
                                                <form id="quickReplyForm">
                                                    <input type="hidden" name="receiverId" value="${otherUser.userId}">
                                                    <div class="row g-2">
                                                        <div class="col-md-12">
                                                            <input type="text" class="form-control" id="quickSubject" name="subject" placeholder="消息主题" required>
                                                        </div>
                                                        <div class="col-md-9">
                                                            <textarea class="form-control" id="quickMessage" name="content" rows="2" placeholder="输入消息内容..." required></textarea>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <button type="submit" class="btn btn-primary w-100 h-100">
                                                    <i class="fas fa-paper-plane"></i> 发送
                                                </button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script>
                    // 页面加载完成后滚动到底部
                    window.addEventListener('load', function() {
                        const container = document.querySelector('.conversation-container');
                        if (container) {
                            container.scrollTop = container.scrollHeight;
                        }
                    });

                    // 快速回复表单提交
                    document.getElementById('quickReplyForm') ? .addEventListener('submit', function(e) {
                        e.preventDefault();

                        const formData = new FormData(this);
                        const data = new URLSearchParams(formData);

                        // 显示加载状态
                        const submitBtn = this.querySelector('button[type="submit"]');
                        const originalText = submitBtn.innerHTML;
                        submitBtn.disabled = true;
                        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 发送中...';

                        fetch('${pageContext.request.contextPath}/admin/message/send', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: data
                            })
                            .then(response => response.json())
                            .then(data => {
                                submitBtn.disabled = false;
                                submitBtn.innerHTML = originalText;

                                if (data.success) {
                                    Swal.fire({
                                        icon: 'success',
                                        title: '发送成功',
                                        text: '消息已发送，页面将自动刷新',
                                        timer: 1500,
                                        showConfirmButton: false
                                    }).then(() => {
                                        // 刷新页面以显示新消息
                                        window.location.reload();
                                    });
                                } else {
                                    Swal.fire('失败', data.message || '发送消息失败', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                submitBtn.disabled = false;
                                submitBtn.innerHTML = originalText;
                                Swal.fire('错误', '网络错误，请稍后重试', 'error');
                            });
                    });

                    // 自动调整文本框高度
                    const quickMessage = document.getElementById('quickMessage');
                    if (quickMessage) {
                        quickMessage.addEventListener('input', function() {
                            this.style.height = 'auto';
                            this.style.height = (this.scrollHeight) + 'px';
                        });
                    }

                    // 支持 Ctrl+Enter 快速发送
                    quickMessage ? .addEventListener('keydown', function(e) {
                        if (e.ctrlKey && e.key === 'Enter') {
                            e.preventDefault();
                            document.getElementById('quickReplyForm').dispatchEvent(new Event('submit'));
                        }
                    });
                </script>
            </body>

            </html>