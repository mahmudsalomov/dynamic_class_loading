package uz.maniac4j.dynamic_class_loading.dynamic;

import uz.maniac4j.dynamic_class_loading.dynamic.new_test.InMemoryClass;

public class TestClass implements InMemoryClass {
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
