package uz.maniac4j.dynamic_class_loading.dynamic;//package uz.liti.main_service.module.system_task.script.dynamic;
//
//import java.io.File;
//import java.net.URL;
//
//public class Test {
//    public static void main(String[] args) {
//        String javaCode= """
//                public class DynamicClass {
//
//                    public void test() {
//                        for (int i = 1; i <120; i++) {
//                            System.out.println(a()*i);
//                        }
//                    }
//
//                    public int a(){
//                        return 5;
//                    }
//
//                }
//
//                """;
//
//        try {
//
//            DynamicExecutor dynamicExecutor=new DynamicExecutor();
//            DynamicCompiler dynamicCompiler=new DynamicCompiler();
//            File compiledFile = dynamicCompiler.compile(javaCode);
//
//            DynamicClassLoader classLoader = new DynamicClassLoader(new URL[]{compiledFile.toURI().toURL()});
//            Class<?> clazz = classLoader.loadClassFromFile(compiledFile);
//
//            dynamicExecutor.executeMethod(clazz, "test");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
