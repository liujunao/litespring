package com.litespring.test.v1_beanFactory;

import com.litespring.context.ApplicationContext;
import com.litespring.context.support.ClassPathXmlApplicationContext;
import com.litespring.context.support.FileSystemXmlApplicationContext;
import com.litespring.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest_2 {

    //测试类文件路径加载 xml 文件
    @Test
    public void testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService) context.getBean("petStore");
        Assert.assertNotNull(petStore);
    }

    //测试系统文件路径加载 xml 文件
    @Test
    public void testGetBeanFormFileSystemContext() {
        ApplicationContext context = new FileSystemXmlApplicationContext("D:\\workCode\\java\\litespring\\src\\test\\resources\\petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService) context.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }
}
