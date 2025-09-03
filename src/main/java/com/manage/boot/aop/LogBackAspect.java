package com.manage.boot.aop;


import com.alibaba.fastjson2.JSON;
import com.manage.boot.dto.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LogBackAspect {

    public static final Integer MAX_LENGTH = 200;

    @Pointcut("execution(* com.manage.boot.controller..*.*(..)) ||" +
            "execution(* com.manage.boot.service..*.*(..))")
    public void logPointCut() {
    }

    // 环绕通知：记录方法执行时间
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        // 记录执行时间
        log.debug("[{}] 执行耗时: {}ms", methodName, endTime - startTime);
        return result;
    }

    // 前置通知：记录请求信息
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;

        HttpServletRequest request = attributes.getRequest();

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 构建日志信息
        Map<String, Object> logInfo = new HashMap<>();
        logInfo.put("URL", request.getRequestURL().toString());
        logInfo.put("HTTP Method", request.getMethod());
        logInfo.put("Class.Method", method.getDeclaringClass().getSimpleName() + "." + method.getName());
        logInfo.put("IP", request.getRemoteAddr());
        logInfo.put("Params", getRequestParams(joinPoint));

        // 记录日志
        log.info("API请求信息: {}", JSON.toJSONString(logInfo));
    }

    // 返回通知：记录响应结果
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(Object result) {
        // 只记录APIResponse类型的返回结果
        if (result instanceof APIResponse<?> response) {

            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("Success", response.isSuccess());
            logInfo.put("Message", response.getMessage());

            // 记录数据大小
            if (response.getData() != null) {
                if (response.getData() instanceof Iterable) {
                    int size = 0;
                    for (Object o : (Iterable<?>) response.getData()) size++;
                    logInfo.put("Data Size", size);
                } else {
                    logInfo.put("Data Type", response.getData().getClass().getSimpleName());
                }
            }

            log.info("API响应结果: {}", JSON.toJSONString(logInfo));
        }
    }

    // 异常通知：记录错误信息
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        Map<String, Object> logInfo = new HashMap<>();
        logInfo.put("Exception", e.getClass().getName());
        logInfo.put("Message", e.getMessage());

        // 记录堆栈信息
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            if (element.getClassName().startsWith("com.manage.boot")) {
                stackTrace.append(element).append("\n");
            }
        }
        logInfo.put("StackTrace", stackTrace.toString());

        log.error("API处理异常: {}", JSON.toJSONString(logInfo));
    }

    // 获取请求参数
    private Map<String, Object> getRequestParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        if (paramNames != null && paramNames.length > 0) {
            for (int i = 0; i < paramNames.length; i++) {
                Object arg = args[i];

                // 特殊处理敏感数据和文件参数
                if (isSensitiveParam(paramNames[i])) {
                    params.put(paramNames[i], "******");
                } else if (arg instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) arg;
                    params.put(paramNames[i], file.getOriginalFilename());
                } else if (arg instanceof HttpServletRequest) {
                    // 跳过HttpServletRequest
                } else {
                    try {
                        // 避免序列化大对象
                        if (arg != null && arg.getClass().getName().startsWith("com.manage.boot")) {
                            params.put(paramNames[i], JSON.toJSONString(arg));
                        } else {
                            params.put(paramNames[i], arg);
                        }
                    } catch (Exception e) {
                        params.put(paramNames[i], "[Unserializable Object]");
                    }
                }
            }
        }
        return params;
    }

    // 判断是否是敏感参数
    private boolean isSensitiveParam(String paramName) {
        return paramName.contains("password") ||
                paramName.contains("pwd") ||
                paramName.contains("token") ||
                paramName.contains("secret");
    }
}

