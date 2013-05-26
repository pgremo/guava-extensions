package common;

import com.google.common.base.Function;

import java.util.concurrent.Callable;

class DeferredApply<I, O> implements Callable<O> {
  private Function<I, O> f;
  private I input;

  private DeferredApply(Function<I, O> f, I input) {
    this.f = f;
    this.input = input;
  }

  public static <I, O> Callable<O> deferedApply(Function<I, O> f, I input) {
    return new DeferredApply<>(f, input);
  }

  @Override
  public O call() throws Exception {
    return f.apply(input);
  }
}
