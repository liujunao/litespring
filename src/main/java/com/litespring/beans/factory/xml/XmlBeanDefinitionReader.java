package com.litespring.beans.factory.xml;

import com.litespring.aop.config.ConfigBeanDefinitionParser;
import com.litespring.beans.BeanDefinition;
import com.litespring.beans.ConstructorArgument;
import com.litespring.beans.PropertyValue;
import com.litespring.beans.factory.config.RuntimeBeanReference;
import com.litespring.beans.factory.config.TypedStringValue;
import com.litespring.beans.factory.support.BeanDefinitionRegistry;
import com.litespring.beans.factory.support.BeanDefinitionStoreException;
import com.litespring.beans.factory.support.GenericBeanDefinition;
import com.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import com.litespring.core.io.Resource;
import com.litespring.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

//读取和解析 xml 文件
public class XmlBeanDefinitionReader {
    protected final Log logger = LogFactory.getLog(getClass());

    BeanDefinitionRegistry registry; //保存读取的 xml 信息 --> id: BeanDefinition

    //bean 的基本属性
    public static final String ID_ATTRIBUTE = "id";
    public static final String CLASS_ATTRIBUTE = "class";
    //bean 的 scope 属性
    public static final String SCOPE_ATTRIBUTE = "scope";
    //property 标签属性
    public static final String PROPERTY_ELEMENT = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String NAME_ATTRIBUTE = "name";
    //constructor-arg 标签属性(上面已定义 ref 和 value)
    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";

    //对应 xml 文件中的 xsd 引用
    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/c";
    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/p";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    //解析 xml 信息
    public void loadBeanDefinition(Resource resource) {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement(); //遍历 <beans> 标签
            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element nextElement = (Element) iterator.next();
                String namespaceUri = nextElement.getNamespaceURI();
                if (isDefaultNamespace(namespaceUri)) {
                    parseDefaultElement(nextElement); //普通的 bean
                } else if (isContextNamespace(namespaceUri)) {
                    parseComponentElement(nextElement); //例如：<context:component-scan>
                } else if (isAOPNamespace(namespaceUri)){
                    parseAOPElement(nextElement); //例如：<aop:config>
                }
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document does fail");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isAOPNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || AOP_NAMESPACE_URI.equals(namespaceUri));
    }

    //注解注入 bean 的解析，如：<context:component-scan>
    private void parseComponentElement(Element element) {
        String basePackages = element.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);
    }

    //普通 xml 注入 bean 的解析
    private void parseDefaultElement(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(CLASS_ATTRIBUTE);
        BeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);
        if (element.attribute(SCOPE_ATTRIBUTE) != null) { //处理 bean 的 scope 类型
            beanDefinition.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
        }
        parseConstructorArgElements(element, beanDefinition); //解析 bean 中的 constructor-arg 标签
        parsePropertyElement(element, beanDefinition); //解析 bean 中的 property 标签
        this.registry.registerBeanDefinition(id, beanDefinition);
    }

    private void parseAOPElement(Element element) {
        ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
        parser.parse(element, this.registry);
    }

    //解析 property 标签
    private void parsePropertyElement(Element beanElement, BeanDefinition beanDefinition) {
        Iterator iterator = beanElement.elementIterator(PROPERTY_ELEMENT);
        while (iterator.hasNext()) {
            Element propElement = (Element) iterator.next();
            String propertyName = propElement.attributeValue(NAME_ATTRIBUTE); //获取 name 属性
            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }
            //获取 ref 和 value 属性
            Object value = parsePropertyValue(propElement, beanDefinition, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, value);
            beanDefinition.getPropertyValues().add(propertyValue);
        }
    }

    //解析 property 和 constructor-arg 标签的 ref 和 value 属性值
    private Object parsePropertyValue(Element element, BeanDefinition beanDefinition, String propertyName) {
        String elementName = propertyName != null ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";
        boolean hasRefAttribute = element.attribute(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = element.attribute(VALUE_ATTRIBUTE) != null;

        //注意： ref 和 value 只能二选一
        if (hasRefAttribute) {
            String refName = element.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(refName); //封装 ref 信息
            return runtimeBeanReference;
        } else if (hasValueAttribute) {
            //封装 value 信息
            TypedStringValue valueHolder = new TypedStringValue(element.attributeValue(VALUE_ATTRIBUTE));
            return valueHolder;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }

    //解析 constructor-arg 标签
    public void parseConstructorArgElements(Element beanElement, BeanDefinition beanDefinition) {
        Iterator iterator = beanElement.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            parseConstructorArgElement(element, beanDefinition); //解析 constructor-arg 标签的属性值
        }
    }

    //解析 constructor-arg 标签的属性值
    public void parseConstructorArgElement(Element element, BeanDefinition beanDefinition) {
        //获取 type 和 name 属性
        String typeAttribute = element.attributeValue(TYPE_ATTRIBUTE);
        String nameAttribute = element.attributeValue(NAME_ATTRIBUTE);
        //获取 value 和 ref 属性
        Object value = parsePropertyValue(element, beanDefinition, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        //封装 type 和 name 属性
        if (StringUtils.hasLength(typeAttribute)) {
            valueHolder.setType(typeAttribute);
        }
        if (StringUtils.hasLength(nameAttribute)) {
            valueHolder.setName(nameAttribute);
        }
        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder); //加入构造器注入的容器
    }

}
