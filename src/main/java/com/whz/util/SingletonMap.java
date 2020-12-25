package com.whz.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  * <p> 确保在map里没有key时仅调用一次 createItemFunction 生成一个实例，与key对应
 *   *
 *    * @author wanghongzhi
 *     * @date 2020/12/24
 *      */
public class SingletonMap<K, T1, T2, V> {
    @FunctionalInterface
    public interface TwoParamsFunc<T1, T2, R> {
        R apply(T1 param1, T2 param2);
    }

    // 定义一个 Item，将lock和value封装在一起，这样避免外部 SingletonMap 定义
    // 两个 ConcurrentHashMap，无法实现对这两个 ConcurrentHashMap 操作的原子性
    // 比如从 map1.remove 和 map2.remove 语句，无法确保这两个语句执行顺序
    private static class Item<V> {
        public volatile V value;
        public volatile Object lock;
        public Item(V value) {
            this.value = value;
            this.lock = new Object();
        }
    }
    private Map<K, Item<V>> map;

    TwoParamsFunc<T1, T2, V> createValueFunc;
    public SingletonMap(TwoParamsFunc<T1, T2, V> createValueFunc) {
        map = new ConcurrentHashMap<>();
        this.createValueFunc = createValueFunc;
    }

    public V getOrCreate(K key, T1 param1, T2 param2) {
        Item<V> item = map.get(key);
        if(item != null) {
            if(item.value != null) {
                return item.value;
            }
        }

        item = new Item<>(null);
        Item<V> oldItem = map.putIfAbsent(key, item);
        Object lock;
        if(null == oldItem) {
            lock = item.lock;
        } else {
            lock = oldItem.lock;
        }

        synchronized (lock) {
            if(null != oldItem) {
                return oldItem.value;
            }

            V value = createValueFunc.apply(param1, param2);
            if(null == value) {
                throw new NullPointerException();
            }
            item.value = value;
            return value;
        }
    }

    public V get(K key) {
        Item<V> item = map.get(key);
        if(item != null) {
            if(item.value != null) {
                return item.value;
            }

            synchronized (item.lock) { // Wait create/remove operation completed.
                return item.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        Item<V> item = map.get(key);
        if(null != item) {
            synchronized (item.lock) {
                map.remove(key);
            }
        }
    }
}