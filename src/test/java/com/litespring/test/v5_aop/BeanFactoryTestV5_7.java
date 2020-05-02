package com.litespring.test.v5_aop;

import com.litespring.aop.Advice;
import com.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import com.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import com.litespring.aop.aspectj.AspectJBeforeAdvice;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.tx.TransactionManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanFactoryTestV5_7 extends AbstractV5Test {
    static String expectedExpression = "execution(* com.litespring.service.v5.*.placeOrder(..))";

    @Test
    public void testGetBeanByType() throws NoSuchMethodException {
        BeanFactory factory = super.getBeanFactory("petstore-v5.xml");
        List<Object> advices = factory.getBeansByType(Advice.class);
        Assert.assertEquals(3, advices.size());
        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice) this.getAdvice(AspectJBeforeAdvice.class, advices);
            Assert.assertEquals(TransactionManager.class.getMethod("start"), advice.getAdviceMethod());
            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());
            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice) this.getAdvice(AspectJAfterReturningAdvice.class, advices);
            Assert.assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());
            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());
            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice) this.getAdvice(AspectJAfterThrowingAdvice.class, advices);
            Assert.assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());
            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());
            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
    }

    public Object getAdvice(Class<?> type, List<Object> advices) {
        for (Object advice : advices) {
            if (advice.getClass().equals(type)) {
                return advice;
            }
        }
        return null;
    }
}
