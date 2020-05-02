package com.litespring.test.v5_aop;

import com.litespring.aop.MethodMatcher;
import com.litespring.aop.aspectj.AspectJExpressionPointcut;
import com.litespring.service.v5.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class PointcutTest_1 {

    @Test
    public void testPointcut() throws NoSuchMethodException {
        String expression = "execution(* com.litespring.service.v5.*.placeOrder(..))";

        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression(expression);

        MethodMatcher methodMatcher = pc.getMethodMatcher();
        {
            Class<?> targetClass = PetStoreService.class;

            Method method1 = targetClass.getMethod("placeOrder");
            Assert.assertTrue(methodMatcher.matches(method1));

            Method method2 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(methodMatcher.matches(method2));
        }
        {
            Class<?> targetClass = com.litespring.service.v4.PetStoreService.class;
            Method method = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(methodMatcher.matches(method));
        }
    }
}
