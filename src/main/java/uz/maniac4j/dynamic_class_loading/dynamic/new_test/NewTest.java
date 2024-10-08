package uz.maniac4j.dynamic_class_loading.dynamic.new_test;


import uz.maniac4j.dynamic_class_loading.dynamic.DynamicExecutor;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;


public class NewTest {
    final static String sourceCode =
            "package uz.liti.main_service.module.system_task.script.dynamic.new_test;\n"
                    +
                    "public class TestClass implements InMemoryClass{\n"
                    + "@Override\n"
                    + "    public void runCode() {\n"
                    + "        System.out.println(\"code is running...\");\n"
                    + "    }\n"
                    + "}\n";
    public static void main(String[] args) throws Exception {
        DynamicExecutor dynamicExecutor = new DynamicExecutor();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        InMemoryFileManager manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));
        List<JavaFileObject> sourceFiles = Collections.singletonList(new JavaSourceFromString("TestClass", sourceCode));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);
        boolean result = task.call();
        if (!result) {
            diagnostics.getDiagnostics()
                    .forEach(System.out::println);
        } else {
            ClassLoader classLoader = manager.getClassLoader(null);
            Class<?> clazz = classLoader.loadClass("uz.liti.main_service.module.system_task.script.dynamic.new_test.TestClass");
//            InMemoryClass instanceOfClass = (InMemoryClass) clazz.newInstance();

//            Assertions.assertInstanceOf(InMemoryClass.class, instanceOfClass);

//            instanceOfClass.runCode();
//            instanceOfClass.runCode();
            Object runCode = dynamicExecutor.executeMethod(clazz, "runCode");
            System.out.println(runCode);
        }
    }
}
