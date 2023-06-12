package com.apptravel.apitravel.util.exceptions;

public class UsernameNotFoundException extends RuntimeException{

    private static final String ERROR_MESSAGE = "User no exist %s";

    public UsernameNotFoundException(String table) {
        super(String.format(ERROR_MESSAGE, table));
    }
}
