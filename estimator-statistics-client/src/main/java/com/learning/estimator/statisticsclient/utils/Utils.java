package com.learning.estimator.statisticsclient.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.Charset;

/**
 * Defines utility methods
 *
 * @author rolea
 */
public class Utils {

    @SuppressWarnings("serial")
    public static final HttpHeaders authenticate(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

}
