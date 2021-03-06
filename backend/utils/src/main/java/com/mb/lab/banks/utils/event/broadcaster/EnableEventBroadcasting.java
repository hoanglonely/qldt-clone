package com.mb.lab.banks.utils.event.broadcaster;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thanh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EventBroadcastConfiguration.class)
@Configuration
public @interface EnableEventBroadcasting {

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

}
