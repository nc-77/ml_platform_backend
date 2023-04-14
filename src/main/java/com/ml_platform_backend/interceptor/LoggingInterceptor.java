package com.ml_platform_backend.interceptor;

import com.ml_platform_backend.entry.Log;
import com.ml_platform_backend.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {
    @Autowired
    private LogService logService;
    private Log log;

    private long startTime;

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 如果多个IP，则取第一个
        int index = ipAddress.indexOf(",");
        if (index != -1) {
            ipAddress = ipAddress.substring(0, index);
        }
        return ipAddress;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log = new Log();
        log.setHttpPath(request.getRequestURI());
        log.setHttpMethod(request.getMethod());
        log.setIp(getIpAddress(request));
        startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.setTimeTaken(timeTaken);
        log.setHttpCode(response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logService.insertLog(log);
    }
}
