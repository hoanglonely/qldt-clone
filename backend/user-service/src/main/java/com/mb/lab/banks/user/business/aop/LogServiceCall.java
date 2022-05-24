package com.mb.lab.banks.user.business.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogServiceCall {

    boolean value() default true;

}
