package com.mb.lab.banks.auth.security.recaptcha;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.RecaptchaAwareRedirectStrategy;

public class AppLoginFailuresCountingHandler extends LoginFailuresCountingHandler {

    public AppLoginFailuresCountingHandler(String defaultFailureUrl,
            LoginFailuresManager failuresManager,
            RecaptchaProperties recaptcha,
            RecaptchaAwareRedirectStrategy redirectStrategy) {
        super(failuresManager, recaptcha, redirectStrategy);
        setDefaultFailureUrl(defaultFailureUrl);
    }

}
