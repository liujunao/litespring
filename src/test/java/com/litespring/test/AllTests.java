package com.litespring.test;

import com.litespring.test.v1_beanFactory.V1AllTests;
import com.litespring.test.v2_setter.V2AllTests;
import com.litespring.test.v3_constructor.V3AllTests;
import com.litespring.test.v4_springAnnotation.V4AllTests;
import com.litespring.test.v5_aop.V5AllTests;
import com.litespring.test.v6_jdkProxy.V6AllTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({V1AllTests.class, V2AllTests.class, V3AllTests.class,
        V4AllTests.class, V5AllTests.class, V6AllTests.class
})
public class AllTests {
}
