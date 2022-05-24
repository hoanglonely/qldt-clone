package com.mb.lab.banks.user.business.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mb.lab.banks.user.persistence.domain.entity.Feature;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequireFeature {

    Feature[] value() default {};

}
