package com.mb.lab.banks.utils.common;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class StringUtils extends org.springframework.util.StringUtils {

    /** Unicode dung san */
    private static char VIET_CHARS[] = { '\u00e0', '\u00e1', '\u1ea3', '\u00e3', '\u1ea1', '\u0103', '\u1eb1', '\u1eaf', '\u1eb3', '\u1eb5', '\u1eb7', '\u00e2',
            '\u1ea7', '\u1ea5', '\u1ea9', '\u1eab', '\u1ead', '\u00c0', '\u00c1', '\u1ea2', '\u00c3', '\u1ea0', '\u0102', '\u1eb0', '\u1eae', '\u1eb2',
            '\u1eb4', '\u1eb6', '\u00c2', '\u1ea6', '\u1ea4', '\u1ea8', '\u1eaa', '\u1eac', '\u00e8', '\u00e9', '\u1ebb', '\u1ebd', '\u1eb9', '\u00ea',
            '\u1ec1', '\u1ebf', '\u1ec3', '\u1ec5', '\u1ec7', '\u00c8', '\u00c9', '\u1eba', '\u1ebc', '\u1eb8', '\u00ca', '\u1ec0', '\u1ebe', '\u1ec2',
            '\u1ec4', '\u1ec6', '\u00ec', '\u00ed', '\u1ec9', '\u0129', '\u1ecb', '\u00cc', '\u00cd', '\u1ec8', '\u0128', '\u1eca', '\u00f2', '\u00f3',
            '\u1ecf', '\u00f5', '\u1ecd', '\u00f4', '\u1ed3', '\u1ed1', '\u1ed5', '\u1ed7', '\u1ed9', '\u01a1', '\u1edd', '\u1edb', '\u1edf', '\u1ee1',
            '\u1ee3', '\u00d2', '\u00d3', '\u1ece', '\u00d5', '\u1ecc', '\u00d4', '\u1ed2', '\u1ed0', '\u1ed4', '\u1ed6', '\u1ed8', '\u01a0', '\u1edc',
            '\u1eda', '\u1ede', '\u1ee0', '\u1ee2', '\u00f9', '\u00fa', '\u1ee7', '\u0169', '\u1ee5', '\u01b0', '\u1eeb', '\u1ee9', '\u1eed', '\u1eef',
            '\u1ef1', '\u00d9', '\u00da', '\u1ee6', '\u0168', '\u1ee4', '\u1ef3', '\u00fd', '\u1ef7', '\u1ef9', '\u1ef5', '\u1ef2', '\u00dd', '\u1ef6',
            '\u1ef8', '\u1ef4', '\u0111', '\u0110', '\u01af', '\u1eea', '\u1eec', '\u1eee', '\u1ee8', '\u1ef0' };

    private static char NORMAL_CHARS[] = { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'A', 'A', 'A', 'A', 'A', 'A',
            'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E', 'E', 'E',
            'E', 'E', 'E', 'E', 'i', 'i', 'i', 'i', 'i', 'I', 'I', 'I', 'I', 'I', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o',
            'o', 'o', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u',
            'u', 'U', 'U', 'U', 'U', 'U', 'y', 'y', 'y', 'y', 'y', 'Y', 'Y', 'Y', 'Y', 'Y', 'd', 'D', 'U', 'U', 'U', 'U', 'U', 'U' };

    /**
     * Convert tu unicode to hop sang unicode dung san
     */
    public static String compound2Unicode(String str) {
        str = str.replaceAll("\u0065\u0309", "\u1EBB");
        str = str.replaceAll("\u0065\u0301", "\u00E9");
        str = str.replaceAll("\u0065\u0300", "\u00E8");
        str = str.replaceAll("\u0065\u0323", "\u1EB9");
        str = str.replaceAll("\u0065\u0303", "\u1EBD");
        str = str.replaceAll("\u00EA\u0309", "\u1EC3");
        str = str.replaceAll("\u00EA\u0301", "\u1EBF");
        str = str.replaceAll("\u00EA\u0300", "\u1EC1");
        str = str.replaceAll("\u00EA\u0323", "\u1EC7");
        str = str.replaceAll("\u00EA\u0303", "\u1EC5");
        str = str.replaceAll("\u0079\u0309", "\u1EF7");
        str = str.replaceAll("\u0079\u0301", "\u00FD");
        str = str.replaceAll("\u0079\u0300", "\u1EF3");
        str = str.replaceAll("\u0079\u0323", "\u1EF5");
        str = str.replaceAll("\u0079\u0303", "\u1EF9");
        str = str.replaceAll("\u0075\u0309", "\u1EE7");
        str = str.replaceAll("\u0075\u0301", "\u00FA");
        str = str.replaceAll("\u0075\u0300", "\u00F9");
        str = str.replaceAll("\u0075\u0323", "\u1EE5");
        str = str.replaceAll("\u0075\u0303", "\u0169");
        str = str.replaceAll("\u01B0\u0309", "\u1EED");
        str = str.replaceAll("\u01B0\u0301", "\u1EE9");
        str = str.replaceAll("\u01B0\u0300", "\u1EEB");
        str = str.replaceAll("\u01B0\u0323", "\u1EF1");
        str = str.replaceAll("\u01B0\u0303", "\u1EEF");
        str = str.replaceAll("\u0069\u0309", "\u1EC9");
        str = str.replaceAll("\u0069\u0301", "\u00ED");
        str = str.replaceAll("\u0069\u0300", "\u00EC");
        str = str.replaceAll("\u0069\u0323", "\u1ECB");
        str = str.replaceAll("\u0069\u0303", "\u0129");
        str = str.replaceAll("\u006F\u0309", "\u1ECF");
        str = str.replaceAll("\u006F\u0301", "\u00F3");
        str = str.replaceAll("\u006F\u0300", "\u00F2");
        str = str.replaceAll("\u006F\u0323", "\u1ECD");
        str = str.replaceAll("\u006F\u0303", "\u00F5");
        str = str.replaceAll("\u01A1\u0309", "\u1EDF");
        str = str.replaceAll("\u01A1\u0301", "\u1EDB");
        str = str.replaceAll("\u01A1\u0300", "\u1EDD");
        str = str.replaceAll("\u01A1\u0323", "\u1EE3");
        str = str.replaceAll("\u01A1\u0303", "\u1EE1");
        str = str.replaceAll("\u00F4\u0309", "\u1ED5");
        str = str.replaceAll("\u00F4\u0301", "\u1ED1");
        str = str.replaceAll("\u00F4\u0300", "\u1ED3");
        str = str.replaceAll("\u00F4\u0323", "\u1ED9");
        str = str.replaceAll("\u00F4\u0303", "\u1ED7");
        str = str.replaceAll("\u0061\u0309", "\u1EA3");
        str = str.replaceAll("\u0061\u0301", "\u00E1");
        str = str.replaceAll("\u0061\u0300", "\u00E0");
        str = str.replaceAll("\u0061\u0323", "\u1EA1");
        str = str.replaceAll("\u0061\u0303", "\u00E3");
        str = str.replaceAll("\u0103\u0309", "\u1EB3");
        str = str.replaceAll("\u0103\u0301", "\u1EAF");
        str = str.replaceAll("\u0103\u0300", "\u1EB1");
        str = str.replaceAll("\u0103\u0323", "\u1EB7");
        str = str.replaceAll("\u0103\u0303", "\u1EB5");
        str = str.replaceAll("\u00E2\u0309", "\u1EA9");
        str = str.replaceAll("\u00E2\u0301", "\u1EA5");
        str = str.replaceAll("\u00E2\u0300", "\u1EA7");
        str = str.replaceAll("\u00E2\u0323", "\u1EAD");
        str = str.replaceAll("\u00E2\u0303", "\u1EAB");
        str = str.replaceAll("\u0045\u0309", "\u1EBA");
        str = str.replaceAll("\u0045\u0301", "\u00C9");
        str = str.replaceAll("\u0045\u0300", "\u00C8");
        str = str.replaceAll("\u0045\u0323", "\u1EB8");
        str = str.replaceAll("\u0045\u0303", "\u1EBC");
        str = str.replaceAll("\u00CA\u0309", "\u1EC2");
        str = str.replaceAll("\u00CA\u0301", "\u1EBE");
        str = str.replaceAll("\u00CA\u0300", "\u1EC0");
        str = str.replaceAll("\u00CA\u0323", "\u1EC6");
        str = str.replaceAll("\u00CA\u0303", "\u1EC4");
        str = str.replaceAll("\u0059\u0309", "\u1EF6");
        str = str.replaceAll("\u0059\u0301", "\u00DD");
        str = str.replaceAll("\u0059\u0300", "\u1EF2");
        str = str.replaceAll("\u0059\u0323", "\u1EF4");
        str = str.replaceAll("\u0059\u0303", "\u1EF8");
        str = str.replaceAll("\u0055\u0309", "\u1EE6");
        str = str.replaceAll("\u0055\u0301", "\u00DA");
        str = str.replaceAll("\u0055\u0300", "\u00D9");
        str = str.replaceAll("\u0055\u0323", "\u1EE4");
        str = str.replaceAll("\u0055\u0303", "\u0168");
        str = str.replaceAll("\u01AF\u0309", "\u1EEC");
        str = str.replaceAll("\u01AF\u0301", "\u1EE8");
        str = str.replaceAll("\u01AF\u0300", "\u1EEA");
        str = str.replaceAll("\u01AF\u0323", "\u1EF0");
        str = str.replaceAll("\u01AF\u0303", "\u1EEE");
        str = str.replaceAll("\u0049\u0309", "\u1EC8");
        str = str.replaceAll("\u0049\u0301", "\u00CD");
        str = str.replaceAll("\u0049\u0300", "\u00CC");
        str = str.replaceAll("\u0049\u0323", "\u1ECA");
        str = str.replaceAll("\u0049\u0303", "\u0128");
        str = str.replaceAll("\u004F\u0309", "\u1ECE");
        str = str.replaceAll("\u004F\u0301", "\u00D3");
        str = str.replaceAll("\u004F\u0300", "\u00D2");
        str = str.replaceAll("\u004F\u0323", "\u1ECC");
        str = str.replaceAll("\u004F\u0303", "\u00D5");
        str = str.replaceAll("\u01A0\u0309", "\u1EDE");
        str = str.replaceAll("\u01A0\u0301", "\u1EDA");
        str = str.replaceAll("\u01A0\u0300", "\u1EDC");
        str = str.replaceAll("\u01A0\u0323", "\u1EE2");
        str = str.replaceAll("\u01A0\u0303", "\u1EE0");
        str = str.replaceAll("\u00D4\u0309", "\u1ED4");
        str = str.replaceAll("\u00D4\u0301", "\u1ED0");
        str = str.replaceAll("\u00D4\u0300", "\u1ED2");
        str = str.replaceAll("\u00D4\u0323", "\u1ED8");
        str = str.replaceAll("\u00D4\u0303", "\u1ED6");
        str = str.replaceAll("\u0041\u0309", "\u1EA2");
        str = str.replaceAll("\u0041\u0301", "\u00C1");
        str = str.replaceAll("\u0041\u0300", "\u00C0");
        str = str.replaceAll("\u0041\u0323", "\u1EA0");
        str = str.replaceAll("\u0041\u0303", "\u00C3");
        str = str.replaceAll("\u0102\u0309", "\u1EB2");
        str = str.replaceAll("\u0102\u0301", "\u1EAE");
        str = str.replaceAll("\u0102\u0300", "\u1EB0");
        str = str.replaceAll("\u0102\u0323", "\u1EB6");
        str = str.replaceAll("\u0102\u0303", "\u1EB4");
        str = str.replaceAll("\u00C2\u0309", "\u1EA8");
        str = str.replaceAll("\u00C2\u0301", "\u1EA4");
        str = str.replaceAll("\u00C2\u0300", "\u1EA6");
        str = str.replaceAll("\u00C2\u0323", "\u1EAC");
        str = str.replaceAll("\u00C2\u0303", "\u1EAA");
        return str;
    }

    public static String toOracleSearchLikeSuffix(String searchText) {
        return toOracleSearchLike(searchText);
    }

    public static String toOracleSearchLike(String searchText) {
        String escapeChar = "/";
        String[] arrSpPat = { "/", "%", "_" };

        for (String str : arrSpPat) {
            if (!StringUtils.isEmpty(searchText)) {
                searchText = searchText.replaceAll(str, escapeChar + str);
            }
        }
        searchText = "%" + searchText + "%";
        return searchText;
    }

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str.toString().trim()));
    }

    public static String arrayToDelimitedString(String[] arr, String delim) {
        if (arr == null || arr.length == 0) {
            return "";
        }

        if (arr.length == 1) {
            return ((arr[0] == null) ? "" : arr[0]);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            if (arr[i] != null) {
                sb.append(arr[i]);
            }
        }

        return sb.toString();
    }

    public static String getSearchableString(String input) {
        if (isEmpty(input)) {
            return "";
        }

        input = input.trim();
        input = compound2Unicode(input);
        /*
         * for (int i = 0; i < VIET_CHARS.length; i++) { input = input.replace(VIET_CHARS[i], NORMAL_CHARS[i]); }
         */
        return input;
    }

    public static String getFileName(String input) {
        if (isEmpty(input)) {
            return "";
        }

        input = input.trim();
        input = compound2Unicode(input);

        for (int i = 0; i < VIET_CHARS.length; i++) {
            input = input.replace(VIET_CHARS[i], NORMAL_CHARS[i]);
        }

        return input;
    }

    public static String removeAccent(String input) {
        if (isEmpty(input)) {
            return "";
        }

        input = input.trim();
        input = compound2Unicode(input);

        for (int i = 0; i < VIET_CHARS.length; i++) {
            input = input.replace(VIET_CHARS[i], NORMAL_CHARS[i]);
        }

        return input;
    }

    public static <T> String buildStringFromList(String separator, Collection<T> list, Function<T, String> getStringFunction) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }

        Assert.notNull(getStringFunction, "getStringFunction cannot be null");

        separator = separator == null ? " " : separator;

        StringBuilder text = new StringBuilder();
        boolean first = true;

        for (T t : list) {
            String s = getStringFunction.apply(t);
            s = s == null ? "" : s;

            if (first) {
                first = false;
            } else {
                text.append(separator);
            }

            text.append(s);
        }

        return text.toString();
    }

    public static String repeat(String str, int repeat) {
        return org.apache.commons.lang3.StringUtils.repeat(str, repeat);
    }

    public static String escapeStringForMySQL(String s) {
        // @formatter:off
        return s.replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\\x1A", "\\Z")
                .replace("\\x00", "\\0")
                .replace("'", "\\'")
                .replace("\"", "\\\"");
        // @formatter:on
    }

    public static String escapeStringForExportTableCdr(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return s.replace(";", "_").replace("\n", "\\n").replace("\r", "\\r");
    }

    public static String escapeStringForCdr(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return s.replace("|", "_").replace("\n", "\\n").replace("\r", "\\r");
    }

    public static String escapeWildcardsForMySqlLikeSearch(String s) {
        // @formatter:off
        return escapeStringForMySQL(s)
                .replace("%", "\\%")
                .replace("_", "\\_");
        // @formatter:on
    }

    public static String createCriteriaForMySqlLikeSearch(String s) {
        return escapeWildcardsForMySqlLikeSearch(getSearchableString(s));
    }

    public static String createCriteriaForMySqlFullTextSearch(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return s;
        }
        s = s.replaceAll("[^\\p{L}\\p{N}_]+", " ").replace("[\\s]+", " ").trim();
        String[] words = s.split("\\s+");
        StringBuilder criteria = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!isEmpty(word)) {
                if (i > 0) {
                    criteria.append(" ");
                }
                criteria.append("+").append(word).append("*");
            }
        }
        return criteria.toString();
    }

    public static boolean isEquals(CharSequence string1, CharSequence string2) {
        if (string1 == null && string2 == null) {
            return true;
        }
        if (string1 != null) {
            return string1.equals(string2);
        }

        return string2.equals(string1);
    }

    public static String maxLength(String s, int maxLength) {
        if (s == null || s.length() <= maxLength) {
            return s;
        }
        return s.substring(0, maxLength);
    }
}
