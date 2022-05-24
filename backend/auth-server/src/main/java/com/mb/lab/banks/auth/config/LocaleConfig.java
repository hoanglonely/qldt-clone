package com.mb.lab.banks.auth.config;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.mb.lab.banks.auth.security.SessionConfigParams;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    @Autowired
    private CommonProperties commonProperties;

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("hl");
        localeChangeInterceptor.setIgnoreInvalidLocale(true);
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CompoundLocaleResolver localeResolver = new CompoundLocaleResolver(commonProperties.getAvailableLocales());
        localeResolver.setDefaultLocale(commonProperties.getDefaultLocale());
        localeResolver.setCookieName(SessionConfigParams.LOCALE_COOKIE_NAME);
        return localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    public static class CompoundLocaleResolver extends CookieLocaleResolver implements LocaleResolver {

        public static final String DEFAULT_COOKIE_NAME = "lang";

        private AcceptHeaderLocaleResolver acceptHeaderLocaleResolver;

        public CompoundLocaleResolver(List<Locale> supportedLocales) {
            super();
            this.setCookieName(DEFAULT_COOKIE_NAME);
            this.acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
            this.acceptHeaderLocaleResolver.setSupportedLocales(supportedLocales);
        }

        @Override
        protected Locale determineDefaultLocale(HttpServletRequest request) {
            // Try to determine from Accept-Language header
            Locale locale = acceptHeaderLocaleResolver.resolveLocale(request);
            if (locale != null) {
                return locale;
            }
            return super.determineDefaultLocale(request);
        }

        @Override
        public void setDefaultLocale(Locale defaultLocale) {
            super.setDefaultLocale(defaultLocale);
            acceptHeaderLocaleResolver.setDefaultLocale(defaultLocale);
        }

    }

}
