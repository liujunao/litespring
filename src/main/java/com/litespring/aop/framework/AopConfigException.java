package com.litespring.aop.framework;

@SuppressWarnings("serial")
public class AopConfigException extends RuntimeException {

    public AopConfigException(String msg) {
        super(msg);
    }

    public AopConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
