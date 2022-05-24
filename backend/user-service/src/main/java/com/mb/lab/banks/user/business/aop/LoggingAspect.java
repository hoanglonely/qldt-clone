package com.mb.lab.banks.user.business.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.mb.lab.banks.utils.event.stream.BusinessLogStreams;
import com.mb.lab.banks.utils.logging.LoggingUtils;


@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusinessLogStreams.OutBound businessLogStreamsOutBound;

    @Pointcut(value = "execution(public * *(..))")
    public void anyPublicMethod() {
    }

    @Pointcut(value = "(@within(com.mb.lab.banks.user.business.aop.LogServiceCall)"
            + " || @annotation(com.mb.lab.banks.user.business.aop.LogServiceCall))")
    public void annotatedWithLogServiceCall() {
    }

    @Around(value = "anyPublicMethod() && annotatedWithLogServiceCall()")
    public Object logEndpointMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LogServiceCall rolePermission = getAnnotation(proceedingJoinPoint, LogServiceCall.class);
        if (rolePermission == null || !rolePermission.value()) {
            return proceedingJoinPoint.proceed();
        }

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getCanonicalName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] args = proceedingJoinPoint.getArgs();

        long start = System.currentTimeMillis();
        LoggingUtils.business(logger, businessLogStreamsOutBound, Level.INFO, LoggingUtils.MARKER_START_ACTION, className, methodName, args, null, false, null);

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            LoggingUtils.business(logger, businessLogStreamsOutBound, Level.INFO, LoggingUtils.MARKER_END_ACTION, className, methodName, args, elapsedTime,
                    false, null);
        }
    }

    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationtype) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        T annotation = AnnotationUtils.findAnnotation(method, annotationtype);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), annotationtype);
        }
        return annotation;
    }
}
