package com.mb.lab.banks.auth.config;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Frontend configuration properties.
 */
@ConfigurationProperties("common")
public class CommonProperties {

    private String webUrl;
    private Locale defaultLocale;
    private List<Locale> availableLocales;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public List<Locale> getAvailableLocales() {
		return availableLocales;
	}

	public void setAvailableLocales(List<Locale> availableLocales) {
		this.availableLocales = availableLocales;
	}

}
