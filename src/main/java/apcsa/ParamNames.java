package apcsa;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Fox on 3/22/2016.
 * Project: ImageNation
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface ParamNames {

    String[] value();

}
