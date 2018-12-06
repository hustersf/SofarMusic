package com.sf.utility.reflect;

import java.lang.reflect.Field;

import android.support.v4.util.LruCache;

/**
 *
 */
public class CacheableJavaCalls {

  private static final int MAX_CACHED_FILED_COUNT = 30;

  private static final LruCache<String, Field> mFieldLruCache = new LruCache<String, Field>(MAX_CACHED_FILED_COUNT) {
    @Override
    protected int sizeOf(String key, Field value) {
      return 1;
    }
  };

  public static void freeCache() {
    mFieldLruCache.evictAll();
  }

  public static void removeCache(Class clazz, String fieldName) {
    String key = getKey(clazz, fieldName);
    mFieldLruCache.remove(key);
  }

  public static void removeCache(Object obj, String fieldName) {
    removeCache(obj.getClass(), fieldName);
  }


  public static void setField(Object targetInstance, String fieldName, Object val) {
    try {
      setFieldOrThrow(targetInstance, fieldName, val);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static void setFieldOrThrow(Object targetInstance, String fieldName, Object val)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> cls = targetInstance.getClass();
    final String key = getKey(targetInstance, fieldName);
    Field f = mFieldLruCache.get(key);
    while (f == null) {
      try {
        f = cls.getDeclaredField(fieldName);
        f.setAccessible(true);
        mFieldLruCache.put(key, f);
      } catch (NoSuchFieldException e) {
        cls = cls.getSuperclass();
      }
      if (cls == null) {
        throw new NoSuchFieldException();
      }
    }
    f.set(targetInstance, val);
  }


  public static <T> T getField(Object targetInstance, String fieldName) {
    try {
      return getFieldOrThrow(targetInstance, fieldName);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T getFieldOrThrow(Object targetInstance, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> cls = targetInstance.getClass();
    String key = getKey(targetInstance, fieldName);
    Field f = mFieldLruCache.get(key);
    if (f == null) {
      while (f == null) {
        try {
          f = cls.getDeclaredField(fieldName);
          f.setAccessible(true);
        } catch (NoSuchFieldException e) {
          cls = cls.getSuperclass();
        }
        if (cls == null) {
          throw new NoSuchFieldException();
        }
      }
      f.setAccessible(true);
      mFieldLruCache.put(key, f);
    }

    return (T) f.get(targetInstance);
  }

  private static String getKey(Object obj, String fieldName) {
    return getKey(obj.getClass(), fieldName);
  }

  private static String getKey(Class clazz, String fieldName) {
    return String.format("%s_%s", clazz.getCanonicalName(), fieldName);
  }
}
