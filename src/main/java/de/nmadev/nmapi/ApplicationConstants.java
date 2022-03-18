package de.nmadev.nmapi;

import org.apache.commons.lang3.math.NumberUtils;

public enum ApplicationConstants {
    JWT_REQUEST_HEADER("Authentication"),
    JWT_SUBJECT("NMAuth"),
    JWT_USERID_CLAIM("uid"),
    JWT_ISSUER("NMApi"),
    DEFAULT_USER_ID("-1")
    ;

    public final String value;

    private ApplicationConstants(String value) {
        this.value = value;
    }

    public Long longValue() {
        return NumberUtils.toLong(value);
    }
}
