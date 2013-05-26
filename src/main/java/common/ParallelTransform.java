package common;

import com.google.common.base.Function;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Functions.compose;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static common.AsCallable.asCallable;
import static java.util.concurrent.Executors.newCachedThreadPool;

public class ParallelTransform {

  public static <I, O> Iterable<O> ptransform(final Iterable<I> source, final Function<I, O> f) {
    return new Iterable<O>() {
      @Override
      public Iterator<O> iterator() {
        return ptransform(source.iterator(), f);
      }
    };
  }

  public static <I, O> Iterator<O> ptransform(Iterator<I> items, Function<I, O> f) {
    ExecutorService executor = newCachedThreadPool();
    try {
      return transform(newArrayList(transform(items, compose(WithExecutor.<O>withExecutor(executor), compose(AsFutureTask.<O>asFutureTask(), asCallable(f))))).iterator(), RealizeFuture.<O>realizeFuture());
    } finally {
      executor.shutdown();
    }
  }
}
