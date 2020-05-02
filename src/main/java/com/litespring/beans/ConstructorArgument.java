package com.litespring.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//构造器注入的基础类(等同 PropertyValue)
public class ConstructorArgument {
    private final List<ValueHolder> argumentValues = new LinkedList<>(); //存储构造器注入的 bean

    public ConstructorArgument() {
    }

    public void addArgumentValue(Object value) {
        this.argumentValues.add(new ValueHolder(value));
    }

    public void addArgumentValue(ValueHolder valueHolder) {
        this.argumentValues.add(valueHolder);
    }

    public List<ValueHolder> getArgumentValues() {
        return Collections.unmodifiableList(this.argumentValues);
    }

    public int getArgumentCount() {
        return this.argumentValues.size();
    }

    public boolean isEmpty() {
        return this.argumentValues.isEmpty();
    }

    // 定义 constructor-arg 的属性值
    // 只用于 ConstructorArgument，因此静态内部类易于高内聚
    public static class ValueHolder {
        private Object value; //value 或 ref
        private String type;
        private String name;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }

        public ValueHolder(Object value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
