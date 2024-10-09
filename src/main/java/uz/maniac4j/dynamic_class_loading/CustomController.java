package uz.maniac4j.dynamic_class_loading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.maniac4j.dynamic_class_loading.dynamic.TestDto;
import uz.maniac4j.dynamic_class_loading.dynamic.handler.JavaHandler;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryFileManager;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.JavaSourceFromString;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;

@RestController
public class CustomController {
    @Autowired
    JavaHandler javaHandler;
    private String code= """
            
//package uz.maniac4j.dynamic_class_loading.dynamic;

import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;
import uz.maniac4j.dynamic_class_loading.dynamic.TestDto;
public class MyClass implements InMemoryClass {
    @Override
    public Object execute() {
//        TestDto testDto=new TestDto();
        ClassLoader classLoader = getClass().getClassLoader();
        ClassLoader a = classLoader.getParent();
        print(a);
        print(a.getDefinedPackages().length);
        for (Package definedPackage : a.getDefinedPackages()) {
            System.out.println(definedPackage.getName());
        }
        print(Math.random());
        print(System.getProperty("java.class.path"));
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
    
    public void execute2(TestDto object) {
    print(System.getProperty("java.class.path"));
        System.out.println(object);
    }
}

                        
              
            """;
    @GetMapping("/")
    public HttpEntity<?> home() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

//        TestDto testDto=new TestDto();

        Object execute = javaHandler.execute(code);
        System.out.println(execute);
        return ResponseEntity.ok(execute);
    }
}
