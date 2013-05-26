package common;

import com.google.common.base.Function;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class AsFutureTask<O> implements Function<Callable<O>, FutureTask<O>> {
  private AsFutureTask() {
  }

  static <O> Function<Callable<O>, FutureTask<O>> asFutureTask() {
    return new AsFutureTask<>();
  }

  @Override
  public FutureTask<O> apply(Callable<O> input) {
    return new FutureTask<>(input);
  }
}
