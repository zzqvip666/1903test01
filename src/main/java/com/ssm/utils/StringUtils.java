package com.ssm.utils;

public class StringUtils {

    /**
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
}
