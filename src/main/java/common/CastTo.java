package common;

import com.google.common.base.Function;

public class CastTo<I, O> implements Function<I, O> {

  public static final CastTo instance = new CastTo<>();

  private CastTo() {
  }

  public static <I, O> CastTo<I, O> castTo(Class<O> _) {
    return instance;
  }

  @Override
  public O apply(I input) {
    return (O) input;
  }
}
