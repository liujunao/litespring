package com.litespring.test.v5_aop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV5.class, PointcutTest_1.class, MethodLocatingFactoryTest_2.class, ReflectiveMethodInvocationTest_3.class,
        CGLibTest_4.class, CGLibAopProxyTest_5.class, BeanDefinitionTestV5_6.class, BeanFactoryTestV5_7.class
})
public class V5AllTests {
}
