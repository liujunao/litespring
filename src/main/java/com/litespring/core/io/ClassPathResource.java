package com.litespring.core.io;

import com.litespring.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

//类文件路径加载
public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    public InputStream getInputStream() throws FileNotFoundException {
        InputStream inputStream = this.classLoader.getResourceAsStream(this.path);
        if (inputStream == null) {
            throw new FileNotFoundException(path + " cannot be open");
        }
        return inputStream;
    }

    public String getDescription() {
        return this.path;
    }
}
