package com.litespring.context.support;

import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;

//类文件路径加载 xml 文件
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path, this.getBeanClassLoader());
    }
}