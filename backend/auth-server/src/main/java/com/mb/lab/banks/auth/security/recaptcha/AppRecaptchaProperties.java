package com.mb.lab.banks.auth.security.recaptcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.mkopylec.recaptcha.RecaptchaProperties;

/**
 * reCAPTCHA configuration properties. Extended version of {@link com.github.mkopylec.recaptcha.RecaptchaProperties}
 * 
 * @author thanh
 */
@ConfigurationProperties("recaptcha")
public class AppRecaptchaProperties extends RecaptchaProperties {

    /**
     * Enable or disable CAPTCHA feature
     */
    private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

    /**
     * Properties responsible for reCAPTCHA validation on Google's servers.
     */
    private AppValidation validation = new AppValidation();
    

    @Override
    public AppValidation getValidation() {
        return validation;
    }

    public void setValidation(AppValidation validation) {
        this.validation = validation;
    }

    /**
     * Extended version of {@link com.github.mkopylec.recaptcha.RecaptchaProperties.Validation}
     * 
     * @author thanh
     */
    public static class AppValidation extends Validation {

        /**
         * Site key for Google reCAPTCHA service
         */
        private String siteKey;

        public String getSiteKey() {
            return siteKey;
        }

        public void setSiteKey(String siteKey) {
            this.siteKey = siteKey;
        }
    }

}
