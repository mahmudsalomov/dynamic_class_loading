package uz.maniac4j.dynamic_class_loading.dynamic.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mdkt.compiler.DynamicClassLoader;
import org.springframework.stereotype.Service;
import uz.maniac4j.dynamic_class_loading.dynamic.DynamicCompiler;
import uz.maniac4j.dynamic_class_loading.dynamic.DynamicExecutor;
import uz.maniac4j.dynamic_class_loading.dynamic.TestDto;
import uz.maniac4j.dynamic_class_loading.dynamic.TestObject;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryFileManager;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.JavaSourceFromString;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JavaHandler {

    private final InMemoryFileManager manager;
    private final JavaCompiler compiler;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final DynamicExecutor dynamicExecutor;
    private final DynamicCompiler dynamicCompiler;

    private final Map<String, CachedScript> compiledClassCache = new HashMap<>();
    private final Map<String, InMemoryFileManager> inMemoryFileManagerMap = new HashMap<>();

    public JavaHandler(DynamicExecutor dynamicExecutor, DynamicCompiler dynamicCompiler) {
        this.dynamicExecutor = dynamicExecutor;
        this.dynamicCompiler = dynamicCompiler;

        compiler = ToolProvider.getSystemJavaCompiler();
        manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null), new DynamicClassLoader(Thread.currentThread().getContextClassLoader()));
        diagnostics = new DiagnosticCollector<>();
    }


    public TestObject execute(String code) {
        TestObject testObject = new TestObject();
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        Object execute = null;
        try {
            System.setOut(new PrintStream(consoleOutput));


            CachedScript cachedScript = compiledClassCache.get("1");

            TestDto testDto = new TestDto("Alkash", 10);

            if (cachedScript != null && cachedScript.hashCode.equals(hashCode(code))) {
                execute = dynamicExecutor.executeMethod(cachedScript.clazz, "execute2", testDto);
            } else {

//                Class<?> clazz = dynamicCompiler.compileAndLoad("Script" + scriptId, code);
                Class<?> clazz = dynamicCompiler.compileAndLoad(Thread.currentThread().getContextClassLoader(), code);

                execute = dynamicExecutor.executeMethod(clazz, "execute2", testDto);

                compiledClassCache.put("1", new CachedScript(clazz, hashCode(code)));

            }

            System.out.println(execute != null ? execute.toString() : null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            testObject.setConsole(consoleOutput.toString());
            System.setOut(originalOut);
            System.out.println(originalOut);
        }
        testObject.setObject(execute);
        return testObject;
    }


//    public TestObject execute2(String code) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        TestObject testObject = new TestObject();
//        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
//        PrintStream originalOut = System.out;
//        Object execute = null;
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
//        InMemoryFileManager manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));
//
//        List<JavaFileObject> sourceFiles = Collections.singletonList(new JavaSourceFromString("uz.maniac4j.dynamic_class_loading.dynamic.MyClass", code));
//
//        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);
//
//        boolean result = task.call();
//
//        if (!result) {
//            diagnostics.getDiagnostics()
//                    .forEach(System.out::println);
//        } else {
//            ClassLoader classLoader = manager.getClassLoader(null);
//            Class<?> clazz = classLoader.loadClass("uz.maniac4j.dynamic_class_loading.dynamic.MyClass");
//            InMemoryClass instanceOfClass = (InMemoryClass) clazz.newInstance();
//
////            Assertions.assertInstanceOf(InMemoryClass.class, instanceOfClass);
//
//            execute=instanceOfClass.execute();
//        }
//        testObject.setObject(execute);
//        return testObject;
//    }


    private static String hashCode(String code) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(code.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CachedScript {
        //        private File file;
        public Class<?> clazz;
        private String hashCode;
    }

}
