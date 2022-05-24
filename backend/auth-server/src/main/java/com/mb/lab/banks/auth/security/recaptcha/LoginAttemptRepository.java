package com.mb.lab.banks.auth.security.recaptcha;

public interface LoginAttemptRepository {

    int countLoginAttempts(String username);

    void increaseLoginAttempt(String username);

    void clearLoginAttempts(String username);

}
