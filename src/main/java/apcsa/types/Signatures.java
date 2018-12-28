package apcsa.types;

import apcsa.CacheMap;
import apcsa.ParamNames;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fox on 3/21/2016.
 * Project: ImageNation
 */
public class Signatures {

    private static CacheMap<Method, List<String>> paramNames = new CacheMap<>((k,m)->{
        if(k instanceof Method){
            Method method = (Method) k;
            List<String> names = generateNames(method);
            m.put(method, names);
            return names;
        } else return null;
    });

    public static String getSignature(Method method) {
        if(method.getParameterCount() == 0) return "No Args";
        List<String> names = paramNames.get(method);
        Class<?>[] types = method.getParameterTypes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            builder.append(types[i].getSimpleName());
            if(i < names.size()) builder.append(" ").append(names.get(i));
            if(i < types.length - 1) builder.append(", ");
        }
        return builder.toString();
    }

    public static List<String> getParameterNames(Method method){
        return paramNames.get(method);
    }

    private static List<String> generateNames(Method method){
        List<String> names = new ArrayList<>();
        Annotation[] annotations = method.getDeclaredAnnotations();
        for(Annotation a : annotations){
            if(a instanceof ParamNames){
                String[] n = ((ParamNames) a).value();
                names.addAll(Arrays.asList(n));
                break;
            }
        }
        return names;
    }
}
