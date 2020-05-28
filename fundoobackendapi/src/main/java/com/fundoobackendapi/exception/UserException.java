package com.fundoobackendapi.exception;
public class UserException extends Exception{
    public enum exceptionType {
        INVALID_EMAIL_ID,
        INVALID_PASSWORD,
        USER_ALREADY_EXIST,
        USER_NOT_EXIT,
        NOTE_ID_NOT_EXIT,
        WRONG_OR_EXPIRE_TOKEN;
    }
    private static final long serialVersionUID = 1L;
    public exceptionType type;

    public UserException(exceptionType type) {
        this.type = type;
    }
}
