package common;

import com.google.common.base.Function;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YCombinatorTest {
  public static <F, T> Function<F, T> yapply(Function<Function<F, T>, Function<F, T>> f) {
    return YCombinator.<F, T>y().apply(f);
  }

  @Test
  public void testFactorial() {
    Function<Integer, Integer> factorial = yapply(new Function<Function<Integer, Integer>, Function<Integer, Integer>>() {
      public Function<Integer, Integer> apply(final Function<Integer, Integer> f) {
        return new Function<Integer, Integer>() {
          public Integer apply(Integer i) {
            return i <= 0 ? 1 : i * f.apply(i - 1);
          }
        };
      }
    });
    assertEquals(720, factorial.apply(6).intValue());
  }
}
