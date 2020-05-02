package com.litespring.test.v2_setter;

import com.litespring.beans.SimpleTypeConverter;
import com.litespring.beans.TypeConverter;
import com.litespring.beans.TypeMismatchException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TypeConverterTest_6 {

    @Test
    public void testConvertStringToInt() {
        TypeConverter typeConverter = new SimpleTypeConverter();
        Integer i = typeConverter.convertIfNecessary("3", Integer.class);
        Assert.assertEquals(3, i.intValue());

        try {
            typeConverter.convertIfNecessary("3.1", Integer.class);
        } catch (TypeMismatchException e) {
            return;
        }
        fail();
    }

    @Test
    public void testConvertStringToBoolean() {
        TypeConverter converter = new SimpleTypeConverter();
        Boolean b = converter.convertIfNecessary("true", Boolean.class);
        Assert.assertEquals(true, b.booleanValue());

        try {
            converter.convertIfNecessary("xxxyyyzzz", Boolean.class);
        } catch (TypeMismatchException e) {
            return;
        }
        fail();
    }
}