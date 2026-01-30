package com.linkdeco.link_deco.common;

public class ValidationConstants {
    public static final int MIN_EMAIL_LENGTH = 2;
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 24;
    public static final int MIN_NICKNAME_LENGTH = 2;
    public static final int MAX_NICKNAME_LENGTH = 10;
    public static final int MAX_IMAGE_URL_LENGTH = 500;

    public static final String EMAIL_REGEX = "^(?!.*\\s)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{"
            + MIN_EMAIL_LENGTH + "," + MAX_EMAIL_LENGTH + "}$";

    public static final String PW_REGEX = "^(?!.*\\s)(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{"
            + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "}$";

    public static final String NICKNAME_REGEX = "^(?!.*\\s).{"
            + MIN_NICKNAME_LENGTH + "," + MAX_NICKNAME_LENGTH + "}$";

    private ValidationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
