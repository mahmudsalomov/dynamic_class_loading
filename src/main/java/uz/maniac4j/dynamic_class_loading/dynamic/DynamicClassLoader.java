package uz.maniac4j.dynamic_class_loading.dynamic;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader extends URLClassLoader {


    public DynamicClassLoader(URL[] urls) {
        super(urls);
    }

    public Class<?> loadClassFromFile(File file) throws Exception {
        URL url = file.getParentFile().toURI().toURL();
        addURL(url);
        String className = file.getName().substring(0, file.getName().length() - 5);
        return loadClass(className);
    }
}
