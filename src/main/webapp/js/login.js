/**
 * 登录页面JavaScript
 */
(function() {
    'use strict';
    
    // 页面加载完成后执行
    document.addEventListener('DOMContentLoaded', function() {
        initLoginPage();
    });
    
    /**
     * 初始化登录页面
     */
    function initLoginPage() {
        const loginForm = document.getElementById('loginForm');
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        const togglePasswordBtn = document.getElementById('togglePassword');
        const loginBtn = document.getElementById('loginBtn');
        const rememberMeCheckbox = document.getElementById('rememberMe');
        
        // 自动聚焦到用户名输入框
        if (usernameInput && !usernameInput.value) {
            usernameInput.focus();
        } else if (passwordInput) {
            passwordInput.focus();
        }
        
        // 从本地存储恢复用户名
        if (localStorage.getItem('rememberedUsername')) {
            usernameInput.value = localStorage.getItem('rememberedUsername');
            rememberMeCheckbox.checked = true;
            passwordInput.focus();
        }
        
        // 密码可见性切换
        if (togglePasswordBtn) {
            togglePasswordBtn.addEventListener('click', function() {
                togglePasswordVisibility();
            });
        }
        
        // 表单提交处理
        if (loginForm) {
            loginForm.addEventListener('submit', function(e) {
                handleFormSubmit(e);
            });
        }
        
        // 键盘事件处理
        document.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && document.activeElement.tagName !== 'BUTTON') {
                if (document.activeElement === usernameInput) {
                    passwordInput.focus();
                } else if (document.activeElement === passwordInput) {
                    loginForm.submit();
                }
            }
        });
        
        // 演示账号快速填充
        addDemoAccountButtons();
        
        // 添加登录状态动画
        addLoadingAnimation();
    }
    
    /**
     * 切换密码可见性
     */
    function togglePasswordVisibility() {
        const passwordInput = document.getElementById('password');
        const toggleBtn = document.getElementById('togglePassword');
        const icon = toggleBtn.querySelector('i');
        
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
            toggleBtn.setAttribute('title', '隐藏密码');
        } else {
            passwordInput.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
            toggleBtn.setAttribute('title', '显示密码');
        }
    }
    
    /**
     * 处理表单提交
     */
    function handleFormSubmit(e) {
        const form = e.target;
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const rememberMe = document.getElementById('rememberMe').checked;
        const loginBtn = document.getElementById('loginBtn');
        
        // 基本验证
        if (!validateForm(username, password)) {
            e.preventDefault();
            return false;
        }
        
        // 处理记住用户名
        handleRememberMe(username, rememberMe);
        
        // 显示加载状态
        showLoadingState(loginBtn);
        
        // 设置超时保护
        setTimeout(function() {
            hideLoadingState(loginBtn);
        }, 30000); // 30秒超时
    }
    
    /**
     * 验证表单
     */
    function validateForm(username, password) {
        const errors = [];
        
        if (!username) {
            errors.push('请输入用户名');
        } else if (username.length < 2) {
            errors.push('用户名长度至少2个字符');
        } else if (username.length > 50) {
            errors.push('用户名长度不能超过50个字符');
        }
        
        if (!password) {
            errors.push('请输入密码');
        } else if (password.length < 3) {
            errors.push('密码长度至少3个字符');
        }
        
        if (errors.length > 0) {
            showError(errors.join('<br>'));
            return false;
        }
        
        return true;
    }
    
    /**
     * 处理记住用户名功能
     */
    function handleRememberMe(username, remember) {
        if (remember) {
            localStorage.setItem('rememberedUsername', username);
        } else {
            localStorage.removeItem('rememberedUsername');
        }
    }
    
    /**
     * 显示加载状态
     */
    function showLoadingState(button) {
        if (button) {
            button.disabled = true;
            button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 登录中...';
            button.classList.add('loading');
        }
    }
    
    /**
     * 隐藏加载状态
     */
    function hideLoadingState(button) {
        if (button) {
            button.disabled = false;
            button.innerHTML = '<i class="fas fa-sign-in-alt"></i> 登录';
            button.classList.remove('loading');
        }
    }
    
    /**
     * 显示错误消息
     */
    function showError(message) {
        // 移除现有的错误消息
        const existingError = document.querySelector('.alert-danger');
        if (existingError) {
            existingError.remove();
        }
        
        // 创建新的错误消息
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.innerHTML = `
            <i class="fas fa-exclamation-triangle"></i>
            ${message}
        `;
        
        // 插入到表单前
        const form = document.getElementById('loginForm');
        form.parentNode.insertBefore(errorDiv, form);
        
        // 自动隐藏
        setTimeout(function() {
            errorDiv.style.opacity = '0';
            setTimeout(function() {
                if (errorDiv.parentNode) {
                    errorDiv.parentNode.removeChild(errorDiv);
                }
            }, 300);
        }, 5000);
    }
    
    /**
     * 添加演示账号快速填充按钮
     */
    function addDemoAccountButtons() {
        const demoCard = document.querySelector('.card .card-body');
        if (demoCard) {
            // 为演示账号添加点击事件
            const adminText = demoCard.querySelector('small');
            if (adminText) {
                adminText.style.cursor = 'pointer';
                adminText.addEventListener('click', function(e) {
                    const text = e.target.textContent;
                    if (text.includes('admin')) {
                        fillLoginForm('admin', 'admin');
                    } else if (text.includes('staff001')) {
                        fillLoginForm('staff001', 'hello');
                    }
                });
            }
        }
    }
    
    /**
     * 填充登录表单
     */
    function fillLoginForm(username, password) {
        document.getElementById('username').value = username;
        document.getElementById('password').value = password;
        
        // 添加填充动画效果
        const inputs = [document.getElementById('username'), document.getElementById('password')];
        inputs.forEach(input => {
            input.classList.add('highlight');
            setTimeout(() => {
                input.classList.remove('highlight');
            }, 1000);
        });
    }
    
    /**
     * 添加加载动画样式
     */
    function addLoadingAnimation() {
        const style = document.createElement('style');
        style.textContent = `
            .highlight {
                background-color: rgba(102, 126, 234, 0.1) !important;
                transition: background-color 0.3s ease;
            }
            
            .loading {
                position: relative;
                overflow: hidden;
            }
            
            .loading::after {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
                animation: loadingSlide 1.5s infinite;
            }
            
            @keyframes loadingSlide {
                0% { left: -100%; }
                100% { left: 100%; }
            }
            
            .form-control:focus {
                animation: focusPulse 0.3s ease-out;
            }
            
            @keyframes focusPulse {
                0% { transform: scale(1); }
                50% { transform: scale(1.02); }
                100% { transform: scale(1); }
            }
        `;
        document.head.appendChild(style);
    }
    
    /**
     * 检查浏览器兼容性
     */
    function checkBrowserCompatibility() {
        const isOldBrowser = !window.fetch || !window.Promise || !Array.prototype.includes;
        
        if (isOldBrowser) {
            const warningDiv = document.createElement('div');
            warningDiv.className = 'alert alert-warning';
            warningDiv.innerHTML = `
                <i class="fas fa-exclamation-triangle"></i>
                您的浏览器版本过低，可能影响系统正常使用。建议升级到最新版本。
            `;
            
            document.body.insertBefore(warningDiv, document.body.firstChild);
        }
    }
    
    // 执行浏览器兼容性检查
    checkBrowserCompatibility();
    
})();