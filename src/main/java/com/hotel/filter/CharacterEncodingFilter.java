package com.hotel.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字符编码过滤器
 * 统一设置请求和响应的字符编码为UTF-8
 */
public class CharacterEncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    private boolean forceEncoding = false;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }
        
        String forceEncodingParam = filterConfig.getInitParameter("forceEncoding");
        if (forceEncodingParam != null) {
            this.forceEncoding = Boolean.parseBoolean(forceEncodingParam);
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 设置请求编码
        if (forceEncoding || httpRequest.getCharacterEncoding() == null) {
            httpRequest.setCharacterEncoding(encoding);
        }
        
        // 设置响应编码
        if (forceEncoding || httpResponse.getCharacterEncoding() == null || 
            "ISO-8859-1".equals(httpResponse.getCharacterEncoding())) {
            httpResponse.setCharacterEncoding(encoding);
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // 清理资源
    }
}