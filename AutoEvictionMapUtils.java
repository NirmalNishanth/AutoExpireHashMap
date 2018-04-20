package autoevictionmap;

import com.google.common.cache.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Basic Version of Auto Eviction of Key from Map.
 * Lot to do : Persisting the removal cache , loading the cache etc
 * @param <K>
 * @param <V>
 */
public class AutoEvictionMapUtils<K,V> implements Map<K,V>
{
    public long size;
    public long duration;
    /** This is  for printing like hdr**/
    private String TAG = "";
    public TimeUnit unit;
    public Cache<K,V> expireCache;
    public ConcurrentMap<K,V> storeMap;

    /*
        isExpireAfterWriteOrAfterWriteAndAccess true means isExpireAfterWrite
        isExpireAfterWriteOrAfterWriteAndAccess false means isExpireAfterWriteAndAccess

        isExpireAfterWrite - Key Expire time is calculated from the instance of time it is added

        isExpireAfterWriteAndAccess - Key Expire Time is based on the latest access
     */

    public AutoEvictionMapUtils(long size, long duration, TimeUnit unit,
                                boolean isExpireAfterWriteOrAfterWriteAndAccess,
                                String tag) {
        this.size = size;
        this.duration = duration;
        this.unit = unit;
        this.TAG = tag;
        if(isExpireAfterWriteOrAfterWriteAndAccess)
            expireCache = getMapExpireAfterWrite();
        else
            expireCache = getMapExpireAfterWriteAndAccess();

        storeMap = expireCache.asMap();
    }

    public Cache<K,V> getMapExpireAfterWrite()
    {
        expireCache = CacheBuilder.newBuilder()
                                  .maximumSize(size)
                                  .expireAfterWrite(duration,unit)
                                  .build();
        return expireCache;

    }

    public Cache<K,V> getMapExpireAfterWriteAndAccess()
    {
        expireCache = CacheBuilder.newBuilder()
                                  .maximumSize(size)
                                  .expireAfterAccess(duration,unit)
                                  .build();
        return expireCache;

    }

    @Override
    public V get(Object key) {
        expireCache.cleanUp();
        V value = this.storeMap.get(key);
        if (value != null) {
            return value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        expireCache.cleanUp();
        return storeMap.put(key, value);
    }

    @Override
    public int size() {
        expireCache.cleanUp();
        System.out.println(" "+TAG+" current size "+storeMap.size());
        return storeMap.size();
    }

    @Override
    public boolean isEmpty() {
        expireCache.cleanUp();
        return storeMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        expireCache.cleanUp();
        return storeMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        expireCache.cleanUp();
        return storeMap.containsValue(value);
    }

    @Override
    public V remove(Object key) {
        System.out.println(" "+TAG+" Removing Key "+key);
        expireCache.cleanUp();
        return storeMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        expireCache.cleanUp();
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        System.out.println(" "+TAG+" Clear AutoEvictionMapUtils Called ");
        expireCache.cleanUp();
        storeMap.clear();
    }

    @Override
    public Set<K> keySet() {
        expireCache.cleanUp();
        return Collections.unmodifiableSet(storeMap.keySet());
    }

    @Override
    public Collection<V> values() {
        expireCache.cleanUp();
        return Collections.unmodifiableCollection(storeMap.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        expireCache.cleanUp();
        return Collections.unmodifiableSet(storeMap.entrySet());
    }
}
