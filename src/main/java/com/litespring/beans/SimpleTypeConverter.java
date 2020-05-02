package com.litespring.beans;

import com.litespring.beans.propertyeditors.CustomBooleanEditor;
import com.litespring.beans.propertyeditors.CustomNumberEditor;
import com.litespring.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

//将字符串转换为整形，如："1" --> 1
// 可用 commons-beanUtils 替换
public class SimpleTypeConverter implements TypeConverter {
    private Map<Class<?>, PropertyEditor> defaultEditors;

    //值的类型转化
    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        // 能赋值，则直接返回
        if (ClassUtils.isAssignableValue(requiredType, value)) {
            return (T) value;
        } else {
            // 只支持字符串
            if (value instanceof String) {
                PropertyEditor defaultEditor = findDefaultEditor(requiredType);
                try {
                    defaultEditor.setAsText((String) value);
                } catch (IllegalArgumentException e) {
                    // 非法参数异常
                    throw new TypeMismatchException(value, requiredType);
                }
                return (T) defaultEditor.getValue();
            } else {
                throw new RuntimeException("Todo : can't convert value for " + value + " class:" + requiredType);
            }
        }
    }

    private PropertyEditor findDefaultEditor(Class<?> requiredType) {
        // 查找DefaultEditor
        PropertyEditor defaultEditor = this.getDefaultEditor(requiredType);
        if (defaultEditor == null) {
            throw new RuntimeException("Editor for" + requiredType + "has not been implemented");
        }
        return defaultEditor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType) {
        if (defaultEditors == null) {
            createDefaultEditors();
        }
        return defaultEditors.get(requiredType);
    }

    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);
        // Spring's CustomBooleanEditor accepts more flag values than the JDK's default
        // editor.
        this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));
        // The JDK does not contain default editors for number wrapper types!
        // Override JDK primitive number editors with our own CustomNumberEditor.
        /*
         * this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class,false));
         *  this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
         *  this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
         * this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
         */
        this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        /*
         * this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
         * this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, true));
         * this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
         * this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
         * this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
         * this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
         * this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
         * this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
         */
    }
}
