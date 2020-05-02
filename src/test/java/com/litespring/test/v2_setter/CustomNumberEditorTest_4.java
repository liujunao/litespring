package com.litespring.test.v2_setter;

import com.litespring.beans.propertyeditors.CustomNumberEditor;
import org.junit.Assert;
import org.junit.Test;

public class CustomNumberEditorTest_4 {

    //类型转换的测试(整形)
    @Test
    public void testConvertString() {
        CustomNumberEditor editor = new CustomNumberEditor(Integer.class, true);
        editor.setAsText("3");
        Object value = editor.getValue();

        Assert.assertTrue(value instanceof Integer);
        Assert.assertEquals(3, ((Integer) editor.getValue()).intValue());

        editor.setAsText("");
        Assert.assertTrue(editor.getValue() == null);

        try {
            editor.setAsText("3.1");
        } catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail();
    }
}
