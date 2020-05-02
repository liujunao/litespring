package com.litespring.test.v1_beanFactory;

import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.FileSystemResource;
import com.litespring.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResourceTest_3 {

    //测试类文件加载
    @Test
    public void testClassPathResource() {
        Resource resource = new ClassPathResource("petstore-v1.xml");
        InputStream inputStream = null;
        try {
            try {
                inputStream = resource.getInputStream();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(inputStream); //注意：该测试并不充分
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

    //测试系统文件加载
    @Test
    public void testFileSystemResource() {
        Resource resource = new FileSystemResource("D:\\workCode\\java\\litespring\\src\\test\\resources\\petstore-v1.xml");
        InputStream inputStream = null;
        try {
            try {
                inputStream = resource.getInputStream();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(inputStream); //注意：该测试并不充分
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
}
