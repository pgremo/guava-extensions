package common;

import com.google.common.base.Function;

public class Cast<I, O> implements Function<I, O> {

  public static final Cast instance = new Cast<>();

  private Cast() {
  }

  public static <I, O> Cast<I, O> cast(Class<O> _) {
    return instance;
  }

  @Override
  public O apply(I input) {
    return (O) input;
  }
}
