package com.mb.lab.banks.auth.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mb.lab.banks.auth.config.CommonProperties;
import com.mb.lab.banks.auth.security.SecurityContextHelper;
import com.mb.lab.banks.auth.security.recaptcha.AppLoginFailuresManager;

/**
 * @author thanh
 */
@Controller
public class AccountController {
	
    @Autowired
    private CommonProperties commonProperties;
    
    @Autowired
    @Lazy
    private AppLoginFailuresManager loginFailuresManager;
    
    @RequestMapping(value = "/")
    public ModelAndView index() {
        return new ModelAndView("redirect:" + commonProperties.getWebUrl());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) {
        if (SecurityContextHelper.isLoggedIn()) {
            return new ModelAndView("redirect:" + commonProperties.getWebUrl());
        }

        Map<String, Object> model = new HashMap<>(2);
        model.put("language", LocaleContextHolder.getLocale().getLanguage());
        model.put("isRecaptchaRequired", loginFailuresManager.isRecaptchaRequired(request));

        return new ModelAndView("login", model);
    }

}
