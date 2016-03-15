package apcsa;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Created by Fox on 2/1/2016.
 * Project: SandBox
 */
public class TypeCatalog {

    private static Type[] validTypes = {
            Integer.TYPE, Integer.class,
            Long.TYPE, Long.class,
            Double.TYPE, Double.class,
            Float.TYPE, Float.class,
            String.class,
            Color.class};

    public static boolean isValid(Type type) {
        for (Type t : validTypes) {
            if (type.equals(t)) return true;
        }
        return false;
    }
}
