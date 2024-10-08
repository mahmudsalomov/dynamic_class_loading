package uz.maniac4j.dynamic_class_loading;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryFileManager;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.JavaSourceFromString;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class DynamicClassLoadingApplicationTests {

    private String sourceCode="""
            
package uz.maniac4j.dynamic_class_loading.dynamic;

import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;

public class MyClass implements InMemoryClass {
    @Override
    public Object execute() {
        ClassLoader classLoader = getClass().getClassLoader();
        ClassLoader a = classLoader.getParent();
        print(a);
        print(a.getDefinedPackages().length);
        for (Package definedPackage : a.getDefinedPackages()) {
            System.out.println(definedPackage.getName());
        }
        print(Math.random());
        return "test";
    }


    public void tubFinder(int limit) {
        for (int i = 2; i <= limit; i++) {
            int count = 0;
            for (int j = 1; j <= i; j++) {
                if (i % j == 0) count++;
            }
        }
    }

    public void print(Object object) {
        System.out.println(object);
    }
}

                        
              
            """;

    @Test
    void contextLoads() {
    }


    @Test
    public void whenStringIsCompiled_ThenCodeShouldExecute() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        InMemoryFileManager manager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));

        List<JavaFileObject> sourceFiles = Collections.singletonList(new JavaSourceFromString("uz.maniac4j.dynamic_class_loading.dynamic.MyClass", sourceCode));

        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);

        boolean result = task.call();

        if (!result) {
            diagnostics.getDiagnostics()
                    .forEach(System.out::println);
        } else {
            ClassLoader classLoader = manager.getClassLoader(null);
            Class<?> clazz = classLoader.loadClass("uz.maniac4j.dynamic_class_loading.dynamic.MyClass");
            InMemoryClass instanceOfClass = (InMemoryClass) clazz.newInstance();

            Assertions.assertInstanceOf(InMemoryClass.class, instanceOfClass);

            instanceOfClass.execute();
        }
    }
}
