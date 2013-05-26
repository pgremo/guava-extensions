package common;

import com.google.common.base.Function;

import java.util.Iterator;

public class Reduce {
  public static <S, I> S reduce(Iterable<I> items, S seed, Function<S, Function<I, S>> f) {
    return reduce(items.iterator(), seed, f);
  }

  public static <S, I> S reduce(Iterator<I> iterator, S seed, Function<S, Function<I, S>> f) {
    return !iterator.hasNext() ? seed : reduce(iterator, f.apply(seed).apply(iterator.next()), f);
  }
}
