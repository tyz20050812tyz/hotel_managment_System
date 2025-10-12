/**
 * 酒店管理系统通用JavaScript函数库
 */

// 全局配置
const HotelManagement = {
    contextPath: '',
    currentUser: null,
    
    /**
     * 初始化系统
     */
    init: function(contextPath) {
        this.contextPath = contextPath;
        this.initEventListeners();
        this.checkSession();
    },
    
    /**
     * 初始化事件监听器
     */
    initEventListeners: function() {
        // 自动关闭警告消息
        setTimeout(function() {
            $('.alert').fadeOut();
        }, 5000);
        
        // 表格行点击事件
        $('.table tbody tr').click(function() {
            $(this).addClass('table-active').siblings().removeClass('table-active');
        });
        
        // 确认删除对话框
        $('.btn-delete').click(function(e) {
            e.preventDefault();
            const url = $(this).attr('href');
            const name = $(this).data('name') || '此项目';
            
            if (confirm(`确定要删除 ${name} 吗？此操作不可撤销。`)) {
                window.location.href = url;
            }
        });
        
        // 表单验证
        $('.needs-validation').on('submit', function(e) {
            const form = this;
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    },
    
    /**
     * 检查会话状态
     */
    checkSession: function() {
        // 定期检查会话是否过期
        setInterval(() => {
            this.ajax({
                url: '/admin/user/check-session',
                method: 'GET',
                success: function(response) {
                    if (!response.success) {
                        alert('会话已过期，请重新登录');
                        window.location.href = HotelManagement.contextPath + '/login.jsp';
                    }
                },
                error: function() {
                    // 静默处理错误
                }
            });
        }, 300000); // 5分钟检查一次
    },
    
    /**
     * AJAX请求封装
     */
    ajax: function(options) {
        const defaults = {
            method: 'GET',
            dataType: 'json',
            timeout: 30000,
            beforeSend: function() {
                if (options.showLoading !== false) {
                    HotelManagement.showLoading();
                }
            },
            complete: function() {
                if (options.showLoading !== false) {
                    HotelManagement.hideLoading();
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX Error:', error);
                if (xhr.status === 401) {
                    alert('会话已过期，请重新登录');
                    window.location.href = HotelManagement.contextPath + '/login.jsp';
                } else if (xhr.status === 403) {
                    HotelManagement.showAlert('权限不足', 'danger');
                } else if (xhr.status >= 500) {
                    HotelManagement.showAlert('服务器错误，请稍后重试', 'danger');
                } else {
                    HotelManagement.showAlert('请求失败，请检查网络连接', 'danger');
                }
            }
        };
        
        const config = Object.assign({}, defaults, options);
        config.url = this.contextPath + config.url;
        
        return $.ajax(config);
    },
    
    /**
     * 显示加载动画
     */
    showLoading: function() {
        if ($('#loading-overlay').length === 0) {
            $('body').append(`
                <div id="loading-overlay" class="position-fixed w-100 h-100" 
                     style="top: 0; left: 0; background: rgba(0,0,0,0.5); z-index: 9999;">
                    <div class="d-flex justify-content-center align-items-center h-100">
                        <div class="text-center text-white">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">加载中...</span>
                            </div>
                            <div class="mt-2">加载中...</div>
                        </div>
                    </div>
                </div>
            `);
        }
    },
    
    /**
     * 隐藏加载动画
     */
    hideLoading: function() {
        $('#loading-overlay').remove();
    },
    
    /**
     * 显示警告消息
     */
    showAlert: function(message, type = 'info', duration = 5000) {
        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show position-fixed" 
                 style="top: 20px; right: 20px; z-index: 10000; min-width: 300px;">
                <strong>${this.getAlertTitle(type)}</strong> ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        
        $('body').append(alertHtml);
        
        // 自动关闭
        if (duration > 0) {
            setTimeout(function() {
                $('.alert').fadeOut(function() {
                    $(this).remove();
                });
            }, duration);
        }
    },
    
    /**
     * 获取警告标题
     */
    getAlertTitle: function(type) {
        const titles = {
            'success': '成功！',
            'danger': '错误！',
            'warning': '警告！',
            'info': '提示！'
        };
        return titles[type] || '提示！';
    },
    
    /**
     * 确认对话框
     */
    confirm: function(message, callback) {
        if (confirm(message)) {
            callback();
        }
    },
    
    /**
     * 格式化日期
     */
    formatDate: function(date, format = 'YYYY-MM-DD') {
        if (!date) return '';
        
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        const seconds = String(d.getSeconds()).padStart(2, '0');
        
        return format
            .replace('YYYY', year)
            .replace('MM', month)
            .replace('DD', day)
            .replace('HH', hours)
            .replace('mm', minutes)
            .replace('ss', seconds);
    },
    
    /**
     * 格式化货币
     */
    formatCurrency: function(amount) {
        if (isNaN(amount)) return '¥0.00';
        return '¥' + parseFloat(amount).toFixed(2);
    },
    
    /**
     * 数据表格初始化
     */
    initDataTable: function(selector, options = {}) {
        const defaults = {
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sSearch": "搜索:",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                }
            },
            pageLength: 10,
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "全部"]],
            responsive: true,
            order: [[0, 'desc']]
        };
        
        return $(selector).DataTable(Object.assign({}, defaults, options));
    },
    
    /**
     * 表单序列化为对象
     */
    serializeForm: function(form) {
        const formData = {};
        $(form).serializeArray().forEach(function(item) {
            formData[item.name] = item.value;
        });
        return formData;
    },
    
    /**
     * 防抖函数
     */
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },
    
    /**
     * 节流函数
     */
    throttle: function(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }
};

// 工具函数
const Utils = {
    /**
     * 验证身份证号
     */
    validateIdCard: function(idCard) {
        const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        return reg.test(idCard);
    },
    
    /**
     * 验证手机号
     */
    validatePhone: function(phone) {
        const reg = /^1[3-9]\d{9}$/;
        return reg.test(phone);
    },
    
    /**
     * 验证邮箱
     */
    validateEmail: function(email) {
        const reg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return reg.test(email);
    },
    
    /**
     * 生成随机字符串
     */
    randomString: function(length = 8) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < length; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    }
};

// 页面加载完成后初始化
$(document).ready(function() {
    // 从页面获取上下文路径
    const contextPath = $('meta[name="contextPath"]').attr('content') || '';
    HotelManagement.init(contextPath);
    
    // 初始化工具提示
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // 初始化弹出框
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
});