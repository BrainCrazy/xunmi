package com.ruoyi.common.exception;

/**
 * 业务异常
 *
 * @author ruoyi
 */
public class CodeException extends Exception {
    private static final long serialVersionUID = 1L;

    protected final String message;

    public CodeException(String message) {
        this.message = message;
    }

    public CodeException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
