package common.fixtures;

import static com.google.common.collect.Lists.newArrayList;

public class Zipper {
  public static Iterable<Iterable<?>> fourLevelTree() {
    return $($(1, 2, 3), $(4, $(5, 6), 7), $(8, 9));
  }

  public static <T> Iterable<T> $(T... items) {
    return newArrayList(items);
  }
}
