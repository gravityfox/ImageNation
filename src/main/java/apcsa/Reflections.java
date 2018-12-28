package apcsa;

import apcsa.fractal.IColorScheme;
import apcsa.fractal.IFractal;
import apcsa.types.TypeCatalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fox on 3/23/2016.
 * Project: ImageNation
 */
public class Reflections {
    /**
     * Path of images relative to this class
     */
    static final String IMAGE_PATH = "/apcsa/images/";

    private static List<Class<? extends IFractal>> fractalClasses;
    private static List<Class<? extends IColorScheme>> colorClasses;

    static MethodCollection getMethods() {
        MethodCollection methods = new MethodCollection();

        method:
        for (Method method : CustomImage.class.getDeclaredMethods()) {
            for (Parameter p : method.getParameters()) {
                if (!TypeCatalog.isValidParameter(p.getType())) continue method;
            }
            if (Modifier.isPublic(method.getModifiers())
                    && TypeCatalog.isValidReturn(method.getReturnType())) {
                methods.addMethod(method);
            }
        }
        return methods;
    }

    static List<String> getImages() {
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Reflections.class.getResourceAsStream(IMAGE_PATH)));
        String filename;
        try {
            while ((filename = reader.readLine()) != null) {
                list.add(filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    static List<Class<? extends IFractal>> getFractalClasses() {
        if (fractalClasses == null) loadClasses();
        return fractalClasses;
    }

    static List<Class<? extends IColorScheme>> getColorClasses() {
        if (colorClasses == null) loadClasses();
        return colorClasses;
    }

    private static void loadClasses() {
        fractalClasses = new ArrayList<>();
        colorClasses = new ArrayList<>();
        loadClasses("/apcsa/");
        System.out.println(fractalClasses);
        System.out.println(colorClasses);
    }

    @SuppressWarnings("unchecked")
    private static void loadClasses(String path) {
        File file = new File(Reflections.class.getResource(path).getFile());
        if (file.isDirectory()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Reflections.class.getResourceAsStream(path)));
            String name;
            try {
                while ((name = reader.readLine()) != null) {
                    if (path.endsWith("/")) {
                        loadClasses(path + name);
                    } else {
                        loadClasses(path + "/" + name);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (path.endsWith(".class")) {
            try {
                Class<?> clazz = Reflections.class.getClassLoader().loadClass(path.substring(1, path.length() - 6).replace('/', '.'));
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> i : interfaces) {
                    if (i.equals(IFractal.class)) fractalClasses.add((Class<? extends IFractal>) clazz);
                    else if (i.equals(IColorScheme.class)) colorClasses.add((Class<? extends IColorScheme>) clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
