package com.litespring.context.support;

import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.context.ApplicationContext;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.FileSystemResource;
import com.litespring.core.io.Resource;

//系统文件路径加载 xml 文件
public class FileSystemXmlApplicationContext extends AbstractApplicationContext {
    public FileSystemXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }
}
