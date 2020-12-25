package com.whz.util;

import java.util.*;
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
            if(null != oldItem && oldItem.value != null) {
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

    public V remove(K key) {
        Item<V> item = map.get(key);
        if(null != item) {
            synchronized (item.lock) {
                V value = item.value;
                item.value = null; //let get() to get latest value
                map.remove(key);
                return value;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        final int NUM_OF_THREADS = 80;
        // final int NUM_OF_VALUES = 200;
        SingletonMap<String, Integer, Integer, Integer> map = new SingletonMap<String, Integer, Integer, Integer>(
            (param1, param2) -> {
                Integer result = param1 + param2;
                System.out.println("result = " + result);
                return result;}
        );
//        for(int i = 0; i < NUM_OF_VALUES; i++) {
//            String key = UUID.randomUUID().toString();
//            map.getOrCreate(key, i, 0);
//            keys.add(key);
//        }
//        for(String key: keys) {
//            if( map.get(key) < 0) {
//                System.err.println("value should >= 0");
//            }
//            map.remove(key);
//        }
//        for(String key: keys) {
//            if( map.get(key) != null) {
//                System.err.println("value should be null");
//            }
//        }
        Thread[] threads = new Thread[NUM_OF_THREADS];
        for(int i = 0; i< NUM_OF_THREADS/2; i++) {
            final String key = UUID.randomUUID().toString();
            final int j = i;
            threads[2*i] = new Thread(new Runnable() {
                @Override public void run() {
                    map.getOrCreate(key, j, 0);
                    System.out.println("value insered: " + (j));
                }
            });
            threads[2*i+1] = new Thread(new Runnable() {
                @Override public void run() {
                    map.getOrCreate(key, j, 0);
                    System.out.println("value insered: " + (j));
                }
            });
            threads[2*i].start();
            threads[2*i+1].start();
        }

        System.out.println("bye");
    }
}