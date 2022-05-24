package com.mb.lab.banks.auth.security.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CaptchaValidator {

	public boolean validate(HttpServletRequest request, String attributeName, String captcha) {
		HttpSession session = request.getSession();
		Object attribute = session.getAttribute(attributeName);
		String saved = attribute != null ? attribute.toString() : null;
		return saved != null && saved.equals(captcha);
    }
	
	public void clear(HttpServletRequest request, String attributeName) {
	    HttpSession session = request.getSession();
	    session.removeAttribute(attributeName);
	}
}
