package com.macro.mall.component;

import cn.hutool.json.JSONUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.mapper.UmsSysLogMapper;
import com.macro.mall.model.UmsSysLog;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Aspect
@Component
public class SysLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);

    @Pointcut("execution(public * com.macro.mall.controller.*.*(..))")
    public void sysLog() {
    }

    @Before("sysLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @AfterReturning(value = "sysLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }

    @Around("sysLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        insertLog(method,request,joinPoint,result,startTime);

        return result;
    }

    @Autowired
    private UmsSysLogMapper sysLogMapper;

    private void insertLog(Method method, HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result, long startTime){
        try {
            UmsSysLog sysLog = new UmsSysLog();
            if (method.isAnnotationPresent(ApiOperation.class)) {
                ApiOperation log = method.getAnnotation(ApiOperation.class);
                sysLog.setAction(log.value());
            }
            sysLog.setAdmin("anonymousUser");
            sysLog.setIp(getIpAddress(request));
            sysLog.setType(1);
            sysLog.setMethod(request.getMethod());
            sysLog.setUrl(request.getRequestURL().toString());
            sysLog.setStatus(0);
            sysLog.setParameter(JSONUtil.toJsonStr(getParameter(method, joinPoint.getArgs())));
            if (CommonResult.class == method.getReturnType()) {
                CommonResult ret = (CommonResult) result;
                if (ret.getCode() == ResultCode.SUCCESS.getCode()) {
                    sysLog.setStatus(1);
                }
            }
            if(!RequestMethod.GET.name().equals(request.getMethod())){
                //TODO 存储非GET请求数据
                sysLog.setResult(JSONUtil.toJsonStr(result));
            }
            sysLog.setCreateTime(new Date(startTime));

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal != null && principal instanceof String){
                sysLog.setAdmin((String) principal);
            }else {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails != null) {
                    sysLog.setAdmin(userDetails.getUsername());
                }
            }

            sysLog.setDeleted(0);

            if(RequestMethod.GET.name().equals(request.getMethod()) && method.getDeclaringClass().getSimpleName().equals("UmsSysLogController")){
                //TODO 系统日志查询不记录
                return;
            }

            sysLogMapper.insert(sysLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
