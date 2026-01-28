package com.linkdeco.link_deco.common;

public class ValidationConstants {
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_NICKNAME_LENGTH = 2;
    public static final int MAX_NICKNAME_LENGTH = 10;
    public static final int MAX_IMAGE_URL_LENGTH = 500;

    private ValidationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
