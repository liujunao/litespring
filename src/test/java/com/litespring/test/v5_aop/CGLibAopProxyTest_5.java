package com.litespring.test.v5_aop;

import com.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import com.litespring.aop.aspectj.AspectJBeforeAdvice;
import com.litespring.aop.aspectj.AspectJExpressionPointcut;
import com.litespring.aop.config.AspectInstanceFactory;
import com.litespring.aop.framework.AopConfig;
import com.litespring.aop.framework.AopConfigSupport;
import com.litespring.aop.framework.CglibProxyFactory;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.service.v5.PetStoreService;
import com.litespring.tx.TransactionManager;
import com.litespring.util.MessageTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

//AopProxy 实现：链式调用+CGLib
public class CGLibAopProxyTest_5 extends AbstractV5Test {
    private static AspectJBeforeAdvice beforeAdvice = null;
    private static AspectJAfterReturningAdvice afterAdvice = null;
    private static AspectJExpressionPointcut expressionPointcut = null;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;
    private TransactionManager tx;

    @Before
    public void setUp() throws Exception {
        MessageTracker.clearMsgs();
        String expression = "execution(* com.litespring.service.v5.*.placeOrder(..))";
        expressionPointcut = new AspectJExpressionPointcut();
        expressionPointcut.setExpression(expression);

        beanFactory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        beforeAdvice = new AspectJBeforeAdvice(getAdviceMethod("start"),
                expressionPointcut, aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(getAdviceMethod("commit"),
                expressionPointcut, aspectInstanceFactory);

    }

    @Test
    public void testGetProxy() {
        AopConfig config = new AopConfigSupport();
        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);
        config.setTargetObject(new PetStoreService());

        CglibProxyFactory proxyFactory = new CglibProxyFactory(config);
        PetStoreService proxy = (PetStoreService) proxyFactory.getProxy();
        proxy.placeOrder();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

        proxy.toString();
    }
}
