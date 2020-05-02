package com.litespring.test.v2_setter;

import com.litespring.context.ApplicationContext;
import com.litespring.context.support.ClassPathXmlApplicationContext;
import com.litespring.dao.v2.AccountDao;
import com.litespring.dao.v2.ItemDao;
import com.litespring.service.v2.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

//setter 注入测试
public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStoreService = (PetStoreService) context.getBean("petStore");

        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());
        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);

        //进一步测试
        Assert.assertEquals("xxx", petStoreService.getOwner());
        //再次测试(整形)：失败 --> 无类型转换
        Assert.assertEquals(2, petStoreService.getVersion());
    }
}
