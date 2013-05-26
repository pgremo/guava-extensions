package common;

import com.google.common.base.Function;

public class YCombinator {

  private static interface Branch<F, T> extends Function<Branch<F, T>, Function<F, T>> {
  }

  public static <F, T> Function<Function<Function<F, T>, Function<F, T>>, Function<F, T>> y() {
    return new Function<Function<Function<F, T>, Function<F, T>>, Function<F, T>>() {
      public Function<F, T> apply(final Function<Function<F, T>, Function<F, T>> f) {
        return new Branch<F, T>() {
          public Function<F, T> apply(final Branch<F, T> x) {
            return f.apply(new Function<F, T>() {
              public T apply(F y) {
                return x.apply(x).apply(y);
              }
            });
          }
        }.apply(new Branch<F, T>() {
          public Function<F, T> apply(final Branch<F, T> x) {
            return f.apply(new Function<F, T>() {
              public T apply(F y) {
                return x.apply(x).apply(y);
              }
            });
          }
        });
      }
    };
  }
}
