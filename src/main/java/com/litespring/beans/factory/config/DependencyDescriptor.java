package com.litespring.beans.factory.config;

import java.lang.reflect.Field;

//对 Filed 注解的描述，如：@Autowired
public class DependencyDescriptor {
    private Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        this.field = field;
        this.required = required;
    }

    public Class<?> getDependencyType() {
        if (this.field != null) {
            return field.getType();
        }
        throw new RuntimeException("only support field depency");
    }

    public boolean isRequired() {
        return required;
    }
}
