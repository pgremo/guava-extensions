package common;

import com.google.common.base.Function;

import java.util.concurrent.Callable;

import static common.DeferredApply.*;

class AsCallable<I, O> implements Function<I, Callable<O>> {
  private Function<I, O> f;

  private AsCallable(Function<I, O> f) {
    this.f = f;
  }

  public static <I, O> Function<I, Callable<O>> asCallable(Function<I, O> f) {
    return new AsCallable<>(f);
  }

  @Override
  public Callable<O> apply(final I input) {
    return deferedApply(f, input);
  }
}
