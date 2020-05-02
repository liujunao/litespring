package com.litespring.test.v5_aop;

import com.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import com.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import com.litespring.aop.aspectj.AspectJBeforeAdvice;
import com.litespring.aop.framework.ReflectiveMethodInvocation;
import com.litespring.aop.config.AspectInstanceFactory;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.service.v5.PetStoreService;
import com.litespring.tx.TransactionManager;
import com.litespring.util.MessageTracker;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveMethodInvocationTest_3 extends AbstractV5Test {
    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    private PetStoreService petStoreService = null;
    private TransactionManager tx;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;

    @Before
    public void setUp() throws NoSuchMethodException {
        petStoreService = new PetStoreService();
        tx = new TransactionManager();
        MessageTracker.clearMsgs();

        beanFactory = getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        beforeAdvice = new AspectJBeforeAdvice(TransactionManager.class.getMethod("start"),
                null, aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(TransactionManager.class.getMethod("commit"),
                null, aspectInstanceFactory);

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(TransactionManager.class.getMethod("rollback"),
                null, aspectInstanceFactory);
    }

    @Test
    public void testMethodInvocation1() throws Throwable {
        Method targetMethod = PetStoreService.class.getMethod("placeOrder");
        //拦截器列表
        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);

        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        invocation.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));
    }

    @Test
    public void testMethodInvocation2() throws Throwable {
        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        invocation.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));
    }

    @Test
    public void testAfterThrowing() throws NoSuchMethodException {
        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));
            return;
        }
        Assert.fail("No Exception thrown");
    }
}
