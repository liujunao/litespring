package com.litespring.beans;

//setter 注入的基础类(等同 ConstructorArgument)，详见 ApplicationContextTestV2_3 测试类
public class PropertyValue {
    private final String name;
    private final Object value; //value 或 ref
    private boolean converted = false;
    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public synchronized boolean isConverted() {
        return converted;
    }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public Object getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(Object convertedValue) {
        this.convertedValue = convertedValue;
    }
}
