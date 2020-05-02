package com.litespring.aop.aspectj;

import com.litespring.aop.config.AspectInstanceFactory;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object proceed = methodInvocation.proceed();
//        例如：调用TransactionManager的commit方法
        this.invokeAdviceMethod();
        return proceed;
    }
}
