package com.mb.lab.banks.auth.security.captcha;

import static com.github.mkopylec.recaptcha.validation.ErrorCode.BAD_REQUEST;
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USERNAME_REQUEST_PARAMETER;
import static java.util.Collections.singletonList;
import static org.springframework.util.Assert.notNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationException;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;

public class CaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SESSION_ATTRIBUTE_NAME = "captcha";
    
    protected final CaptchaValidator recaptchaValidator;
    protected final RecaptchaProperties recaptcha;
    protected final LoginFailuresManager failuresManager;

    public CaptchaAuthenticationFilter(
    		CaptchaValidator recaptchaValidator,
            RecaptchaProperties recaptcha,
            LoginFailuresManager failuresManager
    ) {
        this.recaptchaValidator = recaptchaValidator;
        this.recaptcha = recaptcha;
        this.failuresManager = failuresManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (getUsernameParameter() == null) {
            throw new RecaptchaAuthenticationException(singletonList(MISSING_USERNAME_REQUEST_PARAMETER));
        }
        if (failuresManager.isRecaptchaRequired(request)) {
        	String recaptchaResponse = obtainRecaptchaResponse(request);
            boolean isValid = recaptchaValidator.validate(request, SESSION_ATTRIBUTE_NAME, recaptchaResponse);
            if (!isValid) {
                recaptchaValidator.clear(request, SESSION_ATTRIBUTE_NAME);
                throw new RecaptchaAuthenticationException(singletonList(BAD_REQUEST));
            }
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        if (!LoginFailuresClearingHandler.class.isAssignableFrom(successHandler.getClass())) {
            throw new IllegalArgumentException("Invalid login success handler. Handler must be an instance of " + LoginFailuresClearingHandler.class.getName() + " but is " + successHandler);
        }
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        if (!LoginFailuresCountingHandler.class.isAssignableFrom(failureHandler.getClass())) {
            throw new IllegalArgumentException("Invalid login failure handler. Handler must be an instance of " + LoginFailuresCountingHandler.class.getName() + " but is " + failureHandler);
        }
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter(usernameParameter);
        failuresManager.setUsernameParameter(usernameParameter);
    }

    @Override
    public void afterPropertiesSet() {
        notNull(recaptchaValidator, "Missing recaptcha validator");
        notNull(recaptcha, "Missing recaptcha validation configuration properties");
        notNull(failuresManager, "Missing login failure manager");
    }

    protected String obtainRecaptchaResponse(HttpServletRequest request) {
        return request.getParameter(recaptcha.getValidation().getResponseParameter());
    }
}
