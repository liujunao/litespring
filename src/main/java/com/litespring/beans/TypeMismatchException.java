package com.litespring.beans;

//用于类型转换 SimpleTypeConverter 中异常的封装
public class TypeMismatchException extends BeansException {
    private transient Object value;
    private Class<?> requiredType;

    public TypeMismatchException(Object value, Class<?> requiredType) {
        super("Failed to convert value:" + value + "to type" + requiredType);
        this.value = value;
        this.requiredType = requiredType;
    }

    public TypeMismatchException(String msg) {
        super(msg);
    }

    public TypeMismatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }
}
