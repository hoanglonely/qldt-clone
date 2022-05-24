package com.mb.lab.banks.auth.security.recaptcha;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.mb.lab.banks.auth.security.SecurityContextHelper;

public class AppLoginFailuresClearingHandler extends LoginFailuresClearingHandler {

    public AppLoginFailuresClearingHandler(String defaultTargetUrl, LoginFailuresManager failuresManager) {
        super(failuresManager);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        SecurityContextHelper.saveUserIdToSession(request, authentication);
    }
}
