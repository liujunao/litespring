package com.litespring.test.v2_setter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BeanDefinitionTestV2_1.class, BeanDefinitionValueResolverTest_2.class, ApplicationContextTestV2.class,
        CustomNumberEditorTest_4.class, CustomBooleanEditorTest_5.class, TypeConverterTest_6.class
})
public class V2AllTests {
}
