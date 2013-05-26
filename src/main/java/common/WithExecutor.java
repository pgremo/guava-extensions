package common;

import com.google.common.base.Function;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

class WithExecutor<O> implements Function<FutureTask<O>, FutureTask<O>> {
  private ExecutorService executor;

  private WithExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  public static <O> Function<FutureTask<O>, FutureTask<O>> withExecutor(ExecutorService executor) {
    return new WithExecutor<>(executor);
  }

  @Override
  public FutureTask<O> apply(FutureTask<O> input) {
    executor.submit(input);
    return input;
  }
}
