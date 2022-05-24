package com.mb.lab.banks.utils.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MobileNumberUtils {

    private static String VN_COUNTRY_CODE = "84";
    private static BiMap<String, String> VIETNAM_MOBILE_PREFIX_CHANGE_MAP = HashBiMap.create();
    static {
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84120", "8470");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84121", "8479");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84122", "8477");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84126", "8476");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84128", "8478");

        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84123", "8483");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84124", "8484");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84125", "8485");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84127", "8481");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84129", "8482");

        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84162", "8432");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84163", "8433");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84164", "8434");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84165", "8435");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84166", "8436");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84167", "8437");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84168", "8438");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84169", "8439");

        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84186", "8456");
        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84188", "8458");

        VIETNAM_MOBILE_PREFIX_CHANGE_MAP.put("84199", "8459");
    }

    public static boolean isValid(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        // Start with 0, not follow with 0 and have 8 more of any digit
        return mobile.matches("^0[1-9]{1}[0-9]{8}$");
    }

    /**
     * Enforce VN mobile format and remove any country code (include + symbol)
     */
    public static String normalizeVNMobileNumber(String mobile) {
        if (mobile != null) {
            // Force VN mobile number
            if (mobile.startsWith("+")) {
                if (!mobile.startsWith("+84")) {
                    return null;
                }
                mobile = mobile.substring(1);
            }
            if (mobile.startsWith("840")) {
                return mobile.substring(2);
            } else if (mobile.startsWith("84")) {
                return "0" + mobile.substring(2);
            }
        }
        return mobile;
    }

    public static String getNewMobileNumber(String oldMobileNumber) {
        if (oldMobileNumber.length() >= 5) {
            String oldPrefix = oldMobileNumber.substring(0, 5);
            String newPrefix = VIETNAM_MOBILE_PREFIX_CHANGE_MAP.get(oldPrefix);
            if (newPrefix != null) {
                String surfix = "";
                if (oldMobileNumber.length() > 5) {
                    surfix = oldMobileNumber.substring(5, oldMobileNumber.length());
                }

                String newMobileNumber = newPrefix + surfix;
                return newMobileNumber;
            }
        }

        return null;
    }
    
    public static List<String> getNewListMobileNumberToMsisdn(List<String> listOldMobileNumber) {
        if (listOldMobileNumber != null && listOldMobileNumber.size() > 0) {
            List<String> result = new ArrayList<>();
            for(String mobile: listOldMobileNumber) {
                result.add(mobileNumberToMsisdn(mobile));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public static String getOldMobileNumber(String newMobileNumber) {
        if (newMobileNumber.length() >= 4) {
            String newPrefix = newMobileNumber.substring(0, 4);
            String oldPrefix = VIETNAM_MOBILE_PREFIX_CHANGE_MAP.inverse().get(newPrefix);

            if (oldPrefix != null) {
                String surfix = "";
                if (newMobileNumber.length() > 4) {
                    surfix = newMobileNumber.substring(4, newMobileNumber.length());
                }

                String oldMobileNumber = oldPrefix + surfix;
                return oldMobileNumber;
            }
        }

        return null;
    }
    
    public static String mobileNumberToMsisdn(String mobileNumber) {
        if (mobileNumber != null) {
            mobileNumber = mobileNumber.trim().replace(" ", "").toLowerCase();
            if (mobileNumber.matches("^[0][0-9]{9}$")) {
                String surfix = mobileNumber.substring(1, mobileNumber.length());
                return VN_COUNTRY_CODE + surfix;
            } else if (mobileNumber.matches("^[1-9][0-9]{8}$")) {
                return VN_COUNTRY_CODE + mobileNumber;
            }
            return mobileNumber;
        }

        return null;
    }
    
    public static String getLoyaltyServiceIsdn(String msisdn) {
        if (msisdn.startsWith(VN_COUNTRY_CODE)) {
            return msisdn.substring(2);
        }
        if (msisdn.startsWith("0")) {
            return msisdn.substring(1);
        }
        return msisdn;
    }
    
    public static boolean isPhoneNumber(String phone) {
        if (phone == null) {
            return false;
        }
        if ((phone.startsWith(VN_COUNTRY_CODE) && phone.length() == 11)
                || (phone.startsWith("0") && phone.length() == 10)) {
            return phone.matches("[0-9]+");
        }
        return false;
    }

}
