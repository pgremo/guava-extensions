package common;

import com.google.common.base.Function;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static common.Reduce.reduce;
import static org.junit.Assert.assertEquals;

public class ReduceTest {
  @Test
  public void should_reduce() {
    assertEquals(10, (int) reduce(newArrayList(1, 2, 3, 4), 0, new Function<Integer, Function<Integer, Integer>>() {
      @Override
      public Function<Integer, Integer> apply(final Integer i1) {
        return new Function<Integer, Integer>() {
          @Override
          public Integer apply(Integer i2) {
            return i2 + i1;
          }
        };
      }
    }));
  }
}
