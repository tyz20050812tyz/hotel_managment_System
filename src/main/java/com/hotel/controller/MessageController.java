package com.hotel.controller;

import com.hotel.model.Message;
import com.hotel.model.User;
import com.hotel.service.MessageService;
import com.hotel.service.ServiceFactory;
import com.hotel.service.UserService;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 留言管理控制器
 */
public class MessageController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(MessageController.class);
    private MessageService messageService;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.messageService = ServiceFactory.createMessageService();
        this.userService = ServiceFactory.createUserService();
        logger.info("MessageController initialized");
    }
    
    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "redirect:/admin/message/inbox";
        }
        
        String action = pathInfo.substring(1); // 去掉开头的"/"
        String method = request.getMethod();
        
        if ("GET".equals(method)) {
            return handleGetRequest(request, action);
        } else if ("POST".equals(method)) {
            return handlePostRequest(request, action);
        }
        
        throw new IllegalArgumentException("不支持的HTTP方法: " + method);
    }
    
    private String handleGetRequest(HttpServletRequest request, String action) throws Exception {
        switch (action) {
            case "inbox":
                return showInbox(request);
            case "outbox":
                return showOutbox(request);
            case "unread":
                return showUnreadMessages(request);
            case "compose":
                return showComposeForm(request);
            case "detail":
                return showMessageDetail(request);
            case "reply":
                return showReplyForm(request);
            case "search":
                return searchMessages(request);
            case "conversation":
                return showConversation(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String handlePostRequest(HttpServletRequest request, String action) throws Exception {
        switch (action) {
            case "send":
                return sendMessage(request);
            case "reply":
                return replyMessage(request);
            case "markRead":
                return markAsRead(request);
            case "markReadBatch":
                return markAsReadBatch(request);
            case "delete":
                return deleteMessage(request);
            case "deleteBatch":
                return batchDeleteMessages(request);
            case "broadcast":
                return broadcastMessage(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    /**
     * 显示收件箱
     */
    private String showInbox(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        String type = getParameter(request, "type");
        
        List<Message> messages;
        int totalCount;
        
        if ("system".equals(type)) {
            messages = messageService.getSystemMessages(currentUser.getUserId());
            totalCount = messages.size();
        } else if ("admin".equals(type)) {
            messages = messageService.getAdminMessages(currentUser.getUserId());
            totalCount = messages.size();
        } else {
            messages = messageService.getInboxWithPaging(currentUser.getUserId(), page, size);
            totalCount = messageService.getInboxCount(currentUser.getUserId());
        }
        
        // 获取未读消息数量
        int unreadCount = messageService.getUnreadCount(currentUser.getUserId());
        
        request.setAttribute("messages", messages);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPages", (totalCount + size - 1) / size);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("selectedType", type);
        request.setAttribute("pageType", "inbox");
        
        return "/admin/message-inbox.jsp";
    }
    
    /**
     * 显示发件箱
     */
    private String showOutbox(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        
        List<Message> messages = messageService.getOutboxWithPaging(currentUser.getUserId(), page, size);
        int totalCount = messageService.getOutboxCount(currentUser.getUserId());
        
        request.setAttribute("messages", messages);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPages", (totalCount + size - 1) / size);
        request.setAttribute("pageType", "outbox");
        
        return "/admin/message-outbox.jsp";
    }
    
    /**
     * 显示未读消息
     */
    private String showUnreadMessages(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        List<Message> messages = messageService.getUnreadMessages(currentUser.getUserId());
        
        request.setAttribute("messages", messages);
        request.setAttribute("pageType", "unread");
        
        return "/admin/message-unread.jsp";
    }
    
    /**
     * 显示写信页面
     */
    private String showComposeForm(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        String receiverIdStr = getParameter(request, "receiverId");
        String replyToStr = getParameter(request, "replyTo");
        
        // 获取所有用户列表用于选择收件人
        List<User> users = userService.findAllUsers();
        // 移除当前用户
        users.removeIf(user -> user.getUserId().equals(currentUser.getUserId()));
        
        request.setAttribute("users", users);
        request.setAttribute("receiverId", receiverIdStr);
        request.setAttribute("replyTo", replyToStr);
        
        // 如果是回复，获取原消息信息
        if (!Utils.isEmpty(replyToStr)) {
            try {
                Integer parentMessageId = Integer.parseInt(replyToStr);
                Message parentMessage = messageService.getMessage(parentMessageId);
                if (parentMessage != null && messageService.canViewMessage(parentMessageId, currentUser.getUserId())) {
                    request.setAttribute("parentMessage", parentMessage);
                    
                    // 智能设置默认收件人：
                    // - 如果当前用户是接收者，则回复给发送者
                    // - 如果当前用户是发送者，则回复给接收者
                    Integer defaultReceiverId;
                    if (parentMessage.getReceiverId().equals(currentUser.getUserId())) {
                        // 当前用户是接收者，回复给发送者
                        defaultReceiverId = parentMessage.getSenderId();
                    } else {
                        // 当前用户是发送者，回复给接收者
                        defaultReceiverId = parentMessage.getReceiverId();
                    }
                    request.setAttribute("receiverId", defaultReceiverId.toString());
                    
                    // 设置默认主题
                    String subject = parentMessage.getSubject();
                    if (!subject.startsWith("RE: ")) {
                        subject = "RE: " + subject;
                    }
                    request.setAttribute("defaultSubject", subject);
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid parent message ID: " + replyToStr);
            }
        }
        
        return "/admin/message-compose.jsp";
    }
    
    /**
     * 显示消息详情
     */
    private String showMessageDetail(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        String messageIdStr = getParameter(request, "id");
        if (Utils.isEmpty(messageIdStr)) {
            throw new IllegalArgumentException("消息ID不能为空");
        }
        
        try {
            Integer messageId = Integer.parseInt(messageIdStr);
            Message message = messageService.getMessage(messageId);
            
            if (message == null) {
                throw new IllegalArgumentException("消息不存在");
            }
            
            // 权限检查
            if (!messageService.canViewMessage(messageId, currentUser.getUserId())) {
                throw new IllegalArgumentException("没有权限查看此消息");
            }
            
            // 如果是接收者且未读，标记为已读
            if (message.getReceiverId().equals(currentUser.getUserId()) && message.isUnread()) {
                messageService.markAsRead(messageId, currentUser.getUserId());
                // 重新获取消息更新状态
                message = messageService.getMessage(messageId);
            }
            
            // 获取回复列表
            List<Message> replies = messageService.getReplies(messageId);
            
            request.setAttribute("message", message);
            request.setAttribute("replies", replies);
            
            return "/admin/message-detail.jsp";
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("消息ID格式错误");
        }
    }
    
    /**
     * 显示回复页面
     */
    private String showReplyForm(HttpServletRequest request) throws Exception {
        String messageIdStr = getParameter(request, "id");
        if (!Utils.isEmpty(messageIdStr)) {
            request.setAttribute("replyTo", messageIdStr);
        }
        return showComposeForm(request);
    }
    
    /**
     * 搜索消息
     */
    private String searchMessages(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        String keyword = getParameter(request, "keyword");
        if (Utils.isEmpty(keyword)) {
            return "json:" + createErrorResponse("搜索关键词不能为空", 400);
        }
        
        List<Message> messages = messageService.searchMessages(currentUser.getUserId(), keyword);
        
        request.setAttribute("messages", messages);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("pageType", "search");
        
        return "/admin/message-search-result.jsp";
    }
    
    /**
     * 显示对话
     */
    private String showConversation(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        String otherUserIdStr = getParameter(request, "userId");
        if (Utils.isEmpty(otherUserIdStr)) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        try {
            Integer otherUserId = Integer.parseInt(otherUserIdStr);
            User otherUser = userService.findUserById(otherUserId);
            
            if (otherUser == null) {
                throw new IllegalArgumentException("用户不存在");
            }
            
            List<Message> conversation = messageService.getConversation(currentUser.getUserId(), otherUserId);
            
            request.setAttribute("conversation", conversation);
            request.setAttribute("otherUser", otherUser);
            request.setAttribute("currentUser", currentUser);
            
            return "/admin/message-conversation.jsp";
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("用户ID格式错误");
        }
    }
    
    /**
     * 发送消息
     */
    private String sendMessage(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        String receiverIdStr = getParameter(request, "receiverId");
        String subject = getParameter(request, "subject");
        String content = getParameter(request, "content");
        String priorityStr = getParameter(request, "priority");
        String parentMessageIdStr = getParameter(request, "parentMessageId");
        
        try {
            // 参数验证
            if (Utils.isEmpty(receiverIdStr)) {
                return "json:" + createErrorResponse("接收者ID不能为空", 400);
            }
            
            if (Utils.isEmpty(subject)) {
                return "json:" + createErrorResponse("主题不能为空", 400);
            }
            
            if (Utils.isEmpty(content)) {
                return "json:" + createErrorResponse("内容不能为空", 400);
            }
            
            Integer receiverId = Integer.parseInt(receiverIdStr);
            Message.Priority priority = Utils.isEmpty(priorityStr) ? 
                Message.Priority.NORMAL : Message.Priority.valueOf(priorityStr);
            
            Integer messageId;
            
            // 如果是回复
            if (!Utils.isEmpty(parentMessageIdStr)) {
                Integer parentMessageId = Integer.parseInt(parentMessageIdStr);
                messageId = messageService.replyMessage(currentUser.getUserId(), parentMessageId, subject, content);
            } else {
                messageId = messageService.sendMessage(currentUser.getUserId(), receiverId, subject, content, 
                    Message.MessageType.USER, priority);
            }
            
            if (messageId != null) {
                return "json:" + createSuccessResponse("消息发送成功");
            } else {
                return "json:" + createErrorResponse("消息发送失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("参数格式错误", 400);
        } catch (IllegalArgumentException e) {
            return "json:" + createErrorResponse("参数错误: " + e.getMessage(), 400);
        } catch (Exception e) {
            logger.error("发送消息失败", e);
            return "json:" + createErrorResponse("发送失败: " + e.getMessage(), 500);
        }
    }
    
    /**
     * 回复消息
     */
    private String replyMessage(HttpServletRequest request) throws Exception {
        return sendMessage(request); // 回复逻辑在 sendMessage 中处理
    }
    
    /**
     * 标记为已读
     */
    private String markAsRead(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        String messageIdStr = getParameter(request, "id");
        if (Utils.isEmpty(messageIdStr)) {
            return "json:" + createErrorResponse("消息ID不能为空", 400);
        }
        
        try {
            Integer messageId = Integer.parseInt(messageIdStr);
            boolean success = messageService.markAsRead(messageId, currentUser.getUserId());
            
            if (success) {
                return "json:" + createSuccessResponse("标记成功");
            } else {
                return "json:" + createErrorResponse("标记失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("消息ID格式错误", 400);
        }
    }
    
    /**
     * 批量标记为已读
     */
    private String markAsReadBatch(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        String[] messageIdStrs = request.getParameterValues("messageIds");
        if (messageIdStrs == null || messageIdStrs.length == 0) {
            return "json:" + createErrorResponse("消息ID列表不能为空", 400);
        }
        
        try {
            List<Integer> messageIds = new ArrayList<>();
            for (String idStr : messageIdStrs) {
                messageIds.add(Integer.parseInt(idStr));
            }
            
            boolean success = messageService.markAsReadBatch(messageIds, currentUser.getUserId());
            
            if (success) {
                return "json:" + createSuccessResponse("批量标记成功");
            } else {
                return "json:" + createErrorResponse("批量标记失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("消息ID格式错误", 400);
        }
    }
    
    /**
     * 删除消息
     */
    private String deleteMessage(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        String messageIdStr = getParameter(request, "id");
        if (Utils.isEmpty(messageIdStr)) {
            return "json:" + createErrorResponse("消息ID不能为空", 400);
        }
        
        try {
            Integer messageId = Integer.parseInt(messageIdStr);
            boolean success = messageService.deleteMessage(messageId, currentUser.getUserId());
            
            if (success) {
                return "json:" + createSuccessResponse("删除成功");
            } else {
                return "json:" + createErrorResponse("删除失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("消息ID格式错误", 400);
        }
    }
    
    /**
     * 批量删除消息
     */
    private String batchDeleteMessages(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        String[] messageIdStrs = request.getParameterValues("messageIds");
        if (messageIdStrs == null || messageIdStrs.length == 0) {
            return "json:" + createErrorResponse("消息ID列表不能为空", 400);
        }
        
        try {
            List<Integer> messageIds = new ArrayList<>();
            for (String idStr : messageIdStrs) {
                messageIds.add(Integer.parseInt(idStr));
            }
            
            boolean success = messageService.batchDeleteMessages(messageIds, currentUser.getUserId());
            
            if (success) {
                return "json:" + createSuccessResponse("批量删除成功");
            } else {
                return "json:" + createErrorResponse("批量删除失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("消息ID格式错误", 400);
        }
    }
    
    /**
     * 群发消息
     */
    private String broadcastMessage(HttpServletRequest request) throws Exception {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "json:" + createErrorResponse("用户未登录", 401);
        }
        
        // 只有管理员可以群发消息
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            return "json:" + createErrorResponse("没有权限执行此操作", 403);
        }
        
        String subject = getParameter(request, "subject");
        String content = getParameter(request, "content");
        String priorityStr = getParameter(request, "priority");
        String targetRole = getParameter(request, "targetRole");
        
        try {
            if (Utils.isEmpty(subject)) {
                return "json:" + createErrorResponse("主题不能为空", 400);
            }
            
            if (Utils.isEmpty(content)) {
                return "json:" + createErrorResponse("内容不能为空", 400);
            }
            
            Message.Priority priority = Utils.isEmpty(priorityStr) ? 
                Message.Priority.NORMAL : Message.Priority.valueOf(priorityStr);
            
            int successCount;
            
            if (Utils.isEmpty(targetRole)) {
                // 群发给所有用户
                successCount = messageService.broadcastMessage(currentUser.getUserId(), subject, content, 
                    Message.MessageType.ADMIN, priority);
            } else {
                // 群发给指定角色
                User.UserRole role = User.UserRole.valueOf(targetRole);
                successCount = messageService.broadcastToRole(currentUser.getUserId(), subject, content, role, 
                    Message.MessageType.ADMIN, priority);
            }
            
            return "json:" + createSuccessResponse("群发成功，共发送 " + successCount + " 条消息");
            
        } catch (IllegalArgumentException e) {
            return "json:" + createErrorResponse("参数错误: " + e.getMessage(), 400);
        } catch (Exception e) {
            logger.error("群发消息失败", e);
            return "json:" + createErrorResponse("群发失败: " + e.getMessage(), 500);
        }
    }
}