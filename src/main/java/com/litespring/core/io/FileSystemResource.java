package com.litespring.core.io;

import com.litespring.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

//系统文件路径加载
public class FileSystemResource implements Resource {
    private final String path;
    private final File file;

    public FileSystemResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.file = new File(path);
        this.path = path;
    }

    public FileSystemResource(File file) {
        this.path = file.getPath();
        this.file = file;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }
}
