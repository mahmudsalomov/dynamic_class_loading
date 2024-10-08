package uz.maniac4j.dynamic_class_loading.dynamic;

import org.springframework.stereotype.Service;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryFileManager;
import uz.maniac4j.dynamic_class_loading.dynamic.new_test.JavaSourceFromString;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class DynamicCompiler {


    private final JavaCompiler compiler;

    public DynamicCompiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    public File compile(String javaCode) throws IOException {
        // Extract the class name from the Java code
        String className = extractClassName(javaCode);

        // Generate a unique directory to avoid conflicts
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "dynamic-classes");
        tempDir.mkdirs();

        // Create the source file
        File sourceFile = new File(tempDir, className + ".java");

        System.out.println(tempDir);
        // Write the java code to the file
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(javaCode);
        }

        // Compile the file
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, sourceFile.getPath());

        if (result == 0) {
            return sourceFile;
        } else {
            throw new RuntimeException("Compilation Failed");
        }
    }


    public Class<?> compileAndLoad(String className, String code) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        List<String> optionList = new ArrayList<>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path"));

        JavaFileObject javaFileObject = new JavaSourceFromString(className, code);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, optionList, null, List.of(javaFileObject));

        if (!task.call()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
            throw new RuntimeException("Compilation failed.");
        }

        return ClassLoader.getSystemClassLoader().loadClass(className);
    }



    public Class<?> compileAndLoad(String code) throws Exception {
        String className = extractClassName(code);
        InMemoryFileManager inMemoryFileManager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));

//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

//        List<String> optionList = new ArrayList<>();
//        optionList.add("-classpath");
//        optionList.add(System.getProperty("java.class.path"));

        JavaFileObject javaFileObject = new JavaSourceFromString(className, code);
//        JavaCompiler.CompilationTask task = compiler.getTask(null, inMemoryFileManager, diagnostics, optionList, null, List.of(javaFileObject));
        JavaCompiler.CompilationTask task = compiler.getTask(null, inMemoryFileManager, diagnostics, null, null, List.of(javaFileObject));

        if (!task.call()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
            throw new RuntimeException("Compilation failed.");
        }

        ClassLoader classLoader = inMemoryFileManager.getClassLoader(null);
        return classLoader.loadClass(className);
    }





    private static String extractClassName(String javaCode) {
        Pattern pattern = Pattern.compile("public class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(javaCode);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class name found in the provided Java code.");
        }
    }
}