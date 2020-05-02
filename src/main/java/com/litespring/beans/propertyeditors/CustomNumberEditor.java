package com.litespring.beans.propertyeditors;

import com.litespring.util.NumberUtils;
import com.litespring.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

//整形类型转换，详见 SimpleTypeConverter
public class CustomNumberEditor extends PropertyEditorSupport {
    private final Class<? extends Number> numberClass;
    private final NumberFormat numberFormat;
    private final boolean allowEmpty;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) {
        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");

        }
        this.numberClass = numberClass;
        this.allowEmpty = allowEmpty;
        this.numberFormat = numberFormat;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else if (numberFormat != null) {
            setValue(NumberUtils.parseNumber(text, numberClass, numberFormat));
        } else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    public void setValue(Object value) {
        if (value instanceof Number) {
            super.setValue(NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        } else {
            super.setValue(value);
        }
    }

    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        } else if (this.numberFormat != null) {
            return this.numberFormat.format(value);
        } else {
            return value.toString();
        }
    }
}
