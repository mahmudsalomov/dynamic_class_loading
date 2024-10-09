package uz.maniac4j.dynamic_class_loading.dynamic.new_test;

import org.mdkt.compiler.DynamicClassLoader;
import org.mdkt.compiler.ExtendedStandardJavaFileManager;

import javax.tools.*;
import java.util.Hashtable;
import java.util.Map;

public class InMemoryFileManager extends ExtendedStandardJavaFileManager {
    private final Map<String, JavaClassAsBytes> compiledClasses;
//    private final ClassLoader loader;

    public InMemoryFileManager(StandardJavaFileManager standardManager, DynamicClassLoader cl) {
        super(standardManager, cl);
        this.compiledClasses = new Hashtable<>();
    }

//    /**
//     * Used to get the class loader for our compiled class. It creates an anonymous class extending
//     * the SecureClassLoader which uses the byte code created by the compiler and stored in the
//     * JavaClassObject, and returns the Class for it
//     *
//     * @param location where to place or search for file objects.
//     */
//    @Override
//    public ClassLoader getClassLoader(Location location) {
////        return loader;
//        return getClassLoader(location);
//    }
//
//    @Override
//    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
//                                               FileObject sibling) {
//
//        JavaClassAsBytes classAsBytes = new JavaClassAsBytes(
//                className, kind);
//        compiledClasses.put(className, classAsBytes);
//
//        return classAsBytes;
//    }

    public Map<String, JavaClassAsBytes> getBytesMap() {
        return compiledClasses;
    }

    public void remove(String className) {
        compiledClasses.remove(className);
    }
}
