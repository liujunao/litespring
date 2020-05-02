package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.ConstructorArgument;
import com.litespring.beans.SimpleTypeConverter;
import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.util.List;

//处理构造器注入时的多个构造器的参数个数匹配、参数类型适配、参数类型转换
//等同 BeanDefinitionValueResolver
public class ConstructorResolver {
    protected final Log logger = LogFactory.getLog(getClass());

    private final AbstractBeanFactory beanFactory;

    public ConstructorResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    //获取 bean 对应的构造器
    public Object autowireConstructor(final BeanDefinition beanDefinition) {
        Constructor<?> constructorToUse = null; //可用(匹配)的构造器
        Object[] argsToUse = null;
        Class<?> beanClass; //bean 的类信息
        try {
            beanClass = beanFactory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(beanDefinition.getID(), "Instantiation of bean failed, can't resolve class", e);
        }
        Constructor<?>[] candidates = beanClass.getConstructors(); //通过反射获取该类的所有 public 构造器
        //后面需要将 reference(ref 属性) 变为 bean 对象
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        ConstructorArgument args = beanDefinition.getConstructorArgument(); //获取通过构造器注入的 bean 对象
        SimpleTypeConverter typeConverter = new SimpleTypeConverter(); //将字符串转为整形
        //遍历候选的构造器
        for (int i = 0; i < candidates.length; i++) {
            //检查 bean 的参数与候选构造器的参数是否对应
            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            if (parameterTypes.length != args.getArgumentCount()) {
                continue;
            }
            argsToUse = new Object[parameterTypes.length];
            //检查 bean 和构造器的参数类型是否对应匹配
            boolean result = valuesMatchTypes(parameterTypes, args.getArgumentValues(), argsToUse, valueResolver, typeConverter);
            if (result) {
                constructorToUse = candidates[i];
                break;
            }
        }
        if (constructorToUse == null) {
            throw new BeanCreationException(beanDefinition.getID(), "can't find a apporiate constructor");
        }
        try {
            return constructorToUse.newInstance(argsToUse); //返回找到的实例对象
        } catch (Exception e) {
            throw new BeanCreationException(beanDefinition.getID(), "can't find a create instance using " + constructorToUse);
        }
    }

    /**
     * 检查 bean 和构造器的参数类型是否对应匹配
     *
     * @param parameterTypes 构造器参数类型
     * @param valueHolders   构造器注入的 bean 对象
     * @param argsToUse
     * @param valueResolver 将 ref 转换为 bean 对象
     * @param typeConverter 用于类型转换
     * @return
     */
    private boolean valuesMatchTypes(Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse, BeanDefinitionValueResolver valueResolver, SimpleTypeConverter typeConverter) {
        for (int i = 0; i < parameterTypes.length; i++) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            //获取参数的值，可能是 TypedStringValue,也可能是 RuntimeBeanReference
            Object originalValue = valueHolder.getValue();
            try {
                //获得真正的值，即 value 值
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                //如果参数类型是int，但是值是字符串，例如"3"，还需要转型
                //如果转型失败，则抛出异常，说明这个构造器不可用
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
                //转型成功，记录下来
                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }
        }
        return true;
    }
}
