package com.mb.lab.banks.auth.security.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.mb.lab.banks.auth.security.recaptcha.AppRecaptchaProperties;

/**
 * CAPTCHA configuration properties
 * 
 * @author thanh
 */
@ConfigurationProperties("captcha")
public class CaptchaProperties extends AppRecaptchaProperties {


}
