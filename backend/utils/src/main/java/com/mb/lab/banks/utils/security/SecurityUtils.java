package com.mb.lab.banks.utils.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityUtils {

    private static final Pattern MERCHANT_ID_REGEX = Pattern.compile("^[0-9]+$");

    public static boolean isMerchantClientId(String clientId) {
        Matcher matcher = MERCHANT_ID_REGEX.matcher(clientId);
        return matcher.find();
    }

    public static boolean isMyViettelAppClientId(String clientId) {
        return clientId.startsWith("myvt_");
    }
}
