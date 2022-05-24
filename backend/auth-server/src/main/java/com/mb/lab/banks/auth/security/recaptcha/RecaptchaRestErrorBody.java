package com.mb.lab.banks.auth.security.recaptcha;

public class RecaptchaRestErrorBody {

    private boolean requireCaptcha;
    private boolean invalidCaptcha;

    public RecaptchaRestErrorBody() {

    }

    public RecaptchaRestErrorBody(boolean requireCaptcha, boolean invalidCaptcha) {
        this.requireCaptcha = requireCaptcha;
        this.invalidCaptcha = invalidCaptcha;
    }

    public boolean isRequireCaptcha() {
        return requireCaptcha;
    }

    public void setRequireCaptcha(boolean requireCaptcha) {
        this.requireCaptcha = requireCaptcha;
    }

    public boolean isInvalidCaptcha() {
        return invalidCaptcha;
    }

    public void setInvalidCaptcha(boolean invalidCaptcha) {
        this.invalidCaptcha = invalidCaptcha;
    }

}
