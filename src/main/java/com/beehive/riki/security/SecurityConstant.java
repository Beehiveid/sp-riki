package com.beehive.riki.security;
public class SecurityConstant {
    static final String SECRET = "aXN0aWdmYXIgcmFobWFu";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/user";
    static final String RESET_PASSWORD_URL = "/user/reset";
    static final String MONITORING_URL = "/sro/monitoring";
    static final String LOCATION_URL = "/location";
    static final String PRIORITY_URL = "/configuration/priority";
}