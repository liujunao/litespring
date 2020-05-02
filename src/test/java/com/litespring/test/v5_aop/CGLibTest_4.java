package com.litespring.test.v5_aop;

import com.litespring.service.v5.PetStoreService;
import com.litespring.tx.TransactionManager;
import org.junit.Test;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

//CGLib 的简单测试
public class CGLibTest_4 {

    @Test
    public void testCallBack() {
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setCallback(new TransactionInterceptor());
        //创建代理
        PetStoreService petStoreService = (PetStoreService) enhancer.create();
        petStoreService.placeOrder();
    }

    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            txManager.start();
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();
            return result;
        }
    }

    @Test
    public void testFilter() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setInterceptDuringConstruction(false);
        Callback[] callbacks = {new TransactionInterceptor(), NoOp.INSTANCE};
        Class<?>[] types = new Class<?>[callbacks.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = callbacks[i].getClass();
        }
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);
    }

    private static class ProxyCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if (method.getName().startsWith("place")) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
