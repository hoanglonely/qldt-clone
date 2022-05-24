package com.mb.lab.banks.auth.security.recaptcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

/**
 * @author Thanh
 */
public class AppLoginFailuresManager extends com.github.mkopylec.recaptcha.security.login.LoginFailuresManager {
    
    public static final String SESSION_ATTRIBUTE_NAME = "app_login_fail_count";

    private LoginAttemptRepository loginAttemptRepository;
    private AppRecaptchaProperties extendedRecaptcha;

    public AppLoginFailuresManager(LoginAttemptRepository loginAttemptRepository, AppRecaptchaProperties extendedRecaptcha) {
        super(extendedRecaptcha);
        this.loginAttemptRepository = loginAttemptRepository;
        this.extendedRecaptcha = extendedRecaptcha;
    }

    @Override
    public boolean isRecaptchaRequired(HttpServletRequest request) {
        if (!extendedRecaptcha.isEnabled()) {
            return false;
        }
        if (extendedRecaptcha.getSecurity().getLoginFailuresThreshold() < 1) {
            return true;
        }
        return super.isRecaptchaRequired(request);
    }

    @Override
    public void addLoginFailure(HttpServletRequest request) {
        increaseSessionLoginAttempt(request);
        String username = getUsername(request);
        if (!StringUtils.isEmpty(username)) {
            loginAttemptRepository.increaseLoginAttempt(username);
        }
    }

    @Override
    public int getLoginFailuresCount(HttpServletRequest request) {
        int userLoginAttempt = 0;
        String username = getUsername(request);
        if (!StringUtils.isEmpty(username)) {
            userLoginAttempt = loginAttemptRepository.countLoginAttempts(username);
        }
        return Math.max(userLoginAttempt, getSessionLoginAttempt(request));
    }

    @Override
    public void clearLoginFailures(HttpServletRequest request) {
        clearSessionLoginAttempt(request);
        String username = getUsername(request);
        if (!StringUtils.isEmpty(username)) {
            loginAttemptRepository.clearLoginAttempts(username);
        }
    }

    protected String getUsername(HttpServletRequest request) {
        if (usernameParameter == null) {
            throw new IllegalStateException("Missing username parameter name");
        }
        return request.getParameter(usernameParameter);
    }
    
    private int getSessionLoginAttempt(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer loginAttempt = session != null ? (Integer)session.getAttribute(SESSION_ATTRIBUTE_NAME) : null;
        if (loginAttempt == null) {
            return 0;
        }
        return loginAttempt;
    }
    
    private void increaseSessionLoginAttempt(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_ATTRIBUTE_NAME, getSessionLoginAttempt(request) + 1);
    }
    
    private void clearSessionLoginAttempt(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_ATTRIBUTE_NAME);
    }

}
