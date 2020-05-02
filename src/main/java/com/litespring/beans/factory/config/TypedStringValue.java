package com.litespring.beans.factory.config;

//PropertyValue 的配置类，获取 value 属性
public class TypedStringValue {
    private String value;

    public TypedStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
