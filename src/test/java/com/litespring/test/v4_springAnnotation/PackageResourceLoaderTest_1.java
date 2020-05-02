package com.litespring.test.v4_springAnnotation;

import com.litespring.core.io.Resource;
import com.litespring.core.io.support.PackageResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

//测试：加载指定 package 下的类文件并封装为 resource
public class PackageResourceLoaderTest_1 {
    @Test
    public void testGetResources() throws IOException {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("com.litespring.dao.v4");
        Assert.assertEquals(2, resources.length);
    }
}
