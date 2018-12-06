package com.sf.utility;

import java.util.Collection;
import java.util.Map;

public class CollectionUtil {

  public static <T> boolean isEmpty(Collection<T> list) {
    return list == null || list.isEmpty();
  }

  public static <K, V> boolean isEmpty(Map<K, V> map) {
    return map == null || map.isEmpty();
  }
}
