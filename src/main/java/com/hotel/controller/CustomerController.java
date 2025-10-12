package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.service.CustomerService;
import com.hotel.service.ServiceFactory;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 客户管理控制器
 */
public class CustomerController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(CustomerController.class);
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.customerService = ServiceFactory.createCustomerService();
        logger.info("CustomerController initialized");
    }
    
    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "redirect:/admin/customer/list";
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
            case "list":
                return listCustomers(request);
            case "search":
                return searchCustomers(request);
            case "add":
                return showAddForm(request);
            case "edit":
                return showEditForm(request);
            case "detail":
                return showCustomerDetail(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String handlePostRequest(HttpServletRequest request, String action) throws Exception {
        switch (action) {
            case "save":
                return saveCustomer(request);
            case "delete":
                return deleteCustomer(request);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String searchCustomers(HttpServletRequest request) throws Exception {
        String keyword = getParameter(request, "keyword");
        if (Utils.isEmpty(keyword)) {
            return "json:" + createErrorResponse("搜索关键词不能为空", 400);
        }
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        
        try {
            List<Customer> customers = customerService.searchCustomersByName(keyword);
            
            // 简单分页
            int total = customers.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            
            if (startIndex < total) {
                customers = customers.subList(startIndex, endIndex);
            } else {
                customers.clear();
            }
            
            request.setAttribute("customers", customers);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", size);
            request.setAttribute("totalCount", total);
            request.setAttribute("totalPages", (total + size - 1) / size);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("isSearchResult", true);
            
            return "/admin/customer-list.jsp";
        } catch (Exception e) {
            logger.error("搜索客户失败", e);
            return "json:" + createErrorResponse("搜索失败: " + e.getMessage(), 500);
        }
    }
    
    private String listCustomers(HttpServletRequest request) throws Exception {
        String pageStr = getParameter(request, "page");
        String sizeStr = getParameter(request, "size");
        String search = getParameter(request, "search");
        String vipLevel = getParameter(request, "vipLevel");
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        
        List<Customer> customers;
        
        if (!Utils.isEmpty(search)) {
            customers = customerService.searchCustomersByName(search);
        } else if (!Utils.isEmpty(vipLevel)) {
            customers = customerService.findCustomersByVipLevel(Integer.parseInt(vipLevel));
        } else {
            customers = customerService.findAllCustomers();
        }
        
        // 简单分页
        int total = customers.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        if (startIndex < total) {
            customers = customers.subList(startIndex, endIndex);
        } else {
            customers.clear();
        }
        
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", total);
        request.setAttribute("totalPages", (total + size - 1) / size);
        request.setAttribute("searchKeyword", search);
        request.setAttribute("selectedVipLevel", vipLevel);
        
        return "/admin/customer-list.jsp";
    }
    
    private String showAddForm(HttpServletRequest request) throws Exception {
        return "/admin/customer-add.jsp";
    }
    
    private String showEditForm(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        int customerId = Integer.parseInt(idStr);
        Customer customer = customerService.findCustomerById(customerId);
        
        if (customer == null) {
            throw new IllegalArgumentException("客户不存在");
        }
        
        request.setAttribute("customer", customer);
        return "/admin/customer-edit.jsp";
    }
    
    private String showCustomerDetail(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        int customerId = Integer.parseInt(idStr);
        Customer customer = customerService.findCustomerById(customerId);
        
        if (customer == null) {
            throw new IllegalArgumentException("客户不存在");
        }
        
        // 计算注册天数
        if (customer.getCreateTime() != null) {
            long currentTime = System.currentTimeMillis();
            long createTime = customer.getCreateTime().getTime();
            long daysDiff = (currentTime - createTime) / (1000 * 60 * 60 * 24);
            request.setAttribute("registrationDays", Math.max(0, daysDiff));
        } else {
            request.setAttribute("registrationDays", 0);
        }
        
        request.setAttribute("customer", customer);
        return "/admin/customer-detail.jsp";
    }
    
    private String saveCustomer(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "customerId");
        String name = getParameter(request, "name");
        String phone = getParameter(request, "phone");
        String email = getParameter(request, "email");
        String idCard = getParameter(request, "idCard");
        String address = getParameter(request, "address");
        String vipLevelStr = getParameter(request, "vipLevel");
        
        // 参数验证
        if (Utils.isEmpty(name)) {
            request.setAttribute("errorMessage", "姓名不能为空");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        if (Utils.isEmpty(phone)) {
            request.setAttribute("errorMessage", "手机号不能为空");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        if (!Utils.validatePhone(phone)) {
            request.setAttribute("errorMessage", "手机号格式不正确");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        // 身份证号必填验证
        if (Utils.isEmpty(idCard)) {
            request.setAttribute("errorMessage", "身份证号不能为空");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        // 身份证号格式验证（18位，符合中国居民身份证规范）
        if (!Utils.validateIdCard(idCard)) {
            request.setAttribute("errorMessage", "身份证号格式不正确，请输入18位有效的中国居民身份证号码");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        if (!Utils.isEmpty(email) && !Utils.validateEmail(email)) {
            request.setAttribute("errorMessage", "邮箱格式不正确");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
        
        try {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setIdCard(idCard);
            customer.setAddress(address);
            
            if (!Utils.isEmpty(vipLevelStr)) {
                customer.setVipLevel(Integer.parseInt(vipLevelStr));
            } else {
                customer.setVipLevel(0); // 普通客户
            }
            
            boolean success;
            String errorMessage = null;
            if (!Utils.isEmpty(idStr)) {
                // 更新客户
                customer.setCustomerId(Integer.parseInt(idStr));
                success = customerService.updateCustomer(customer);
                if (!success) {
                    // 尝试获取详细错误信息
                    String validationError = customerService.validateCustomer(customer);
                    if (validationError != null) {
                        errorMessage = validationError;
                    } else {
                        errorMessage = "更新失败";
                    }
                }
            } else {
                // 新增客户
                Integer customerId = customerService.createCustomer(customer);
                success = customerId != null;
                if (!success) {
                    // 尝试获取详细错误信息
                    String validationError = customerService.validateCustomer(customer);
                    if (validationError != null) {
                        errorMessage = validationError;
                    } else if (customer.getIdCard() != null && !customer.getIdCard().trim().isEmpty() && 
                               customerService.isIdCardExists(customer.getIdCard())) {
                        errorMessage = "身份证号已存在";
                    } else {
                        errorMessage = "添加失败";
                    }
                }
            }
            
            if (success) {
                request.getSession().setAttribute("message", "操作成功");
                request.getSession().setAttribute("messageType", "success");
                return "redirect:/admin/customer/list";
            } else {
                request.setAttribute("errorMessage", errorMessage);
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "参数错误: " + e.getMessage());
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        } catch (Exception e) {
            logger.error("保存客户失败", e);
            request.setAttribute("errorMessage", "保存失败: " + e.getMessage());
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
    }
    
    private String deleteCustomer(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("客户ID不能为空", 400);
        }
        
        try {
            int customerId = Integer.parseInt(idStr);
            boolean success = customerService.deleteCustomer(customerId);
            
            if (success) {
                return "json:" + createSuccessResponse("删除成功");
            } else {
                return "json:" + createErrorResponse("删除失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("客户ID格式错误", 400);
        }
    }
}