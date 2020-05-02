package com.litespring.test.v4_springAnnotation;

import com.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import com.litespring.beans.factory.annotation.AutowiredFieldElement;
import com.litespring.beans.factory.annotation.InjectionElement;
import com.litespring.beans.factory.annotation.InjectionMetadata;
import com.litespring.beans.factory.config.DependencyDescriptor;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.dao.v4.AccountDao;
import com.litespring.dao.v4.ItemDao;
import com.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class AutowiredAnnotationProcessorTest_8 {
    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();
    DefaultBeanFactory beanFactory = new DefaultBeanFactory() {
        @Override
        public Object resolveDependency(DependencyDescriptor descriptor) {
            if (descriptor.getDependencyType().equals(AccountDao.class)) {
                return accountDao;
            }
            if (descriptor.getDependencyType().equals(ItemDao.class)) {
                return itemDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };

    @Test
    public void testGetInjectionMetadata() {
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();

        processor.setBeanFactory(beanFactory);
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);

        List<InjectionElement> elementList = injectionMetadata.getInjectionElementList();
        Assert.assertEquals(2, elementList.size());
        assertFieldExists(elementList, "accountDao");
        assertFieldExists(elementList, "itemDao");

        PetStoreService petStore = new PetStoreService();
        injectionMetadata.inject(petStore);
        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
    }

    private void assertFieldExists(List<InjectionElement> elements, String fieldName) {
        for (InjectionElement element : elements) {
            AutowiredFieldElement fieldElement = (AutowiredFieldElement) element;
            Field field = fieldElement.getField();
            if (field.getName().equals(fieldName)) {
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }
}
