package apcsa.types;

import apcsa.CustomImage;
import apcsa.IImage;
import apcsa.Pixel;
import apcsa.SimpleImage;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Created by Fox on 2/1/2016.
 * Project: SandBox
 */
public class TypeCatalog {

    private static Type[] validParameterTypes = {
            Byte.TYPE, Byte.class,
            Short.TYPE, Short.class,
            Integer.TYPE, Integer.class,
            Long.TYPE, Long.class,
            Double.TYPE, Double.class,
            Float.TYPE, Float.class,
            String.class,
            Color.class};

    private static Type[] validReturnTypes = {
            Void.TYPE,
            Pixel[][].class,
            IImage.class,
            SimpleImage.class,
            CustomImage.class
    };

    public static boolean isValidParameter(Type type) {
        for (Type t : validParameterTypes) {
            if (type.equals(t)) return true;
        }
        return false;
    }

    public static boolean isValidReturn(Type type) {
        for (Type t : validReturnTypes) {
            if (type.equals(t)) return true;
        }
        return false;
    }
}
