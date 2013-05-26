package common;

import com.google.common.base.Function;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.google.common.base.Throwables.propagate;

class RealizeFuture<O> implements Function<Future<O>, O> {
  private RealizeFuture() {
  }

  static <O> Function<Future<O>, O> realizeFuture() {
    return new RealizeFuture<>();
  }

  @Override
  public O apply(Future<O> input) {
    try {
      return input.get();
    } catch (InterruptedException | ExecutionException e) {
      throw propagate(e);
    }
  }
}
