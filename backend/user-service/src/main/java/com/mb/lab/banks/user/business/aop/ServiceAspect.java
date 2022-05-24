package com.mb.lab.banks.user.business.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.mb.lab.banks.user.business.service.sub.UserFeatureCacheSubService;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;
import com.mb.lab.banks.user.util.security.UserLogin;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

@Aspect
@Component
public class ServiceAspect {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Autowired
    private UserFeatureCacheSubService userFeatureCacheSubService;

    @Pointcut(value = "execution(public * *(com.mb.lab.banks.user.util.security.UserLogin,..))")
    public void anyPublicMethodWithUserLogin() {
    }

    @Pointcut(value = "(@within(com.mb.lab.banks.user.business.aop.RequireFeature)"
            + " || @annotation(com.mb.lab.banks.user.business.aop.RequireFeature))")
    public void annotatedWithRequireFeature() {
    }

    @Pointcut(value = "(@within(com.mb.lab.banks.user.business.aop.RolePermission)"
            + " || @annotation(com.mb.lab.banks.user.business.aop.RolePermission))")
    public void annotatedWithRolePermission() {
    }

    @Before(value = "anyPublicMethodWithUserLogin() && annotatedWithRequireFeature()")
    public void checkRequiredFeature(JoinPoint joinPoint) {
        RequireFeature featuresAnnotation = getAnnotation(joinPoint, RequireFeature.class);

        if (featuresAnnotation == null) {
            // Should never happen
            throw new UnsupportedOperationException("RequireFeature annotation not found on method %s" + joinPoint.getSignature().toShortString()
                    + ". This may be a configuration mistake with AspectJ");
        }

        Feature[] requiredFeatures = featuresAnnotation.value();
        if (requiredFeatures == null || requiredFeatures.length == 0) {
            // Should never happen
            throw new UnsupportedOperationException("@RequireFeature must specify at least one UserFeature");
        }

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0 || !(args[0] instanceof UserLogin)) {
            // Should never happen
            throw new UnsupportedOperationException("UserLogin is required to access this method");
        }

        UserLogin userLogin = (UserLogin) args[0];

        if (userLogin == null) {
            throw new UnsupportedOperationException("UserLogin is required to access this method");
        }

        boolean skipCheckFeature = userLogin.isRole(UserRole.ADMIN.name()) || userLogin.isRole(UserRole.ECOM_ADMIN.name());
        if (skipCheckFeature) {
            boolean isEcomAdmin = userLogin.isRole(UserRole.ECOM_ADMIN.name());
            for (Feature feature : requiredFeatures) {
                boolean isEcomFeature = feature.name().startsWith("ECOM_");
                if (isEcomFeature == isEcomAdmin) {
                    // Passed check
                    return;
                }
            }
        } else {
            Long userId = userLogin.getId();
            Set<String> userFeatures = userFeatureCacheSubService.getFeatures(userId);

            for (Feature feature : requiredFeatures) {
                boolean featureAllowed = userFeatures.contains(feature.name());
                if (featureAllowed) {
                    // Passed check
                    return;
                }
            }
        }

        throw new BusinessException(BusinessExceptionCode.FORBIDDEN, String.format("User '%s' not allow for this method", userLogin.getId()));
    }

    @Before(value = "anyPublicMethodWithUserLogin() && annotatedWithRolePermission()")
    public void checkRequiredRole(JoinPoint joinPoint) {
        RolePermission rolePermission = getAnnotation(joinPoint, RolePermission.class);

        if (rolePermission == null) {
            // Shoud never happen
            throw new UnsupportedOperationException("RolePermission annotation not found on method %s" + joinPoint.getSignature().toShortString()
                    + ". This may be a configuration mistake with AspectJ");
        }

        UserRole[] roleAlloweds = rolePermission.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0 || !(args[0] instanceof UserLogin)) {
            // Shoud never happen
            throw new UnsupportedOperationException("UserLogin is required to access this method");
        }

        UserLogin userLogin = (UserLogin) args[0];

        if (userLogin == null) {
            throw new UnsupportedOperationException("UserLogin is required to access this method");
        }

        if (roleAlloweds != null) {
            for (UserRole roleAllowed : roleAlloweds) {
                if (userLogin.isRole(roleAllowed.name())) {
                    return;
                }
            }
        }

        throw new UnsupportedOperationException(String.format("Role '%s' not allow for this method", userLogin.getRole()));
    }

    // PRIVATE
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
