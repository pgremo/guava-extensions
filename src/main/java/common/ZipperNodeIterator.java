package common;

import com.google.common.collect.AbstractIterator;

public class ZipperNodeIterator extends AbstractIterator {
  private Zipper source;

  ZipperNodeIterator(Zipper source) {
    this.source = source;
  }

  @Override
  protected Object computeNext() {
    if (source.end()) return endOfData();
    Object result = source.node();
    source = source.next();
    return result;
  }
}
