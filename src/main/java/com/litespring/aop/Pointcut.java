package com.litespring.aop;

public interface Pointcut {
    MethodMatcher getMethodMatcher();

    String getExpression();
}
