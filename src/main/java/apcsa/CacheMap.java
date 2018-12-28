package apcsa;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Fox on 3/18/2016.
 * Project: ImageNation
 */
public class CacheMap<K, V> extends HashMap<K, V> {

    final private BiFunction<Object, Map<K, V>, V> callback;

    public CacheMap(BiFunction<Object, Map<K, V>, V> callback) {
        this.callback = callback;
    }

    @Override
    public V get(Object key) {
        if (containsKey(key)) {
            return super.get(key);
        } else {
            return callback.apply(key, this);
        }
    }
}
