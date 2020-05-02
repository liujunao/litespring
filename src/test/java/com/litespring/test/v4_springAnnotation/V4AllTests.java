package com.litespring.test.v4_springAnnotation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV4.class, PackageResourceLoaderTest_1.class, ClassReaderTest_2.class,
        MetadataReaderTest_3.class, ClassPathBeanDefinitionScannerTest_4.class, XmlBeanDefinitionReaderTest_5.class,
        DependencyDescriptorTest_6.class, InjectionMetadataTest_7.class, AutowiredAnnotationProcessorTest_8.class
})
public class V4AllTests {

}
