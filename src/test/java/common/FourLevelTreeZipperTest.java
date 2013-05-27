package common;

import com.google.common.base.Function;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Lists.newArrayList;
import static common.Zipper.zipper;
import static common.fixtures.Zipper.$;
import static common.fixtures.Zipper.fourLevelTree;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FourLevelTreeZipperTest {

  private Iterable<Iterable<?>> root;
  private Zipper zipper;

  @Before
  public void setup() {
    root = fourLevelTree();
    zipper = zipper(root).down().right().down().right().down().right().left();
  }

  @Test
  public void should_return_node() {
    assertEquals(5, zipper.node());
  }

  @Test
  public void should_return_root() {
    assertEquals(root, zipper.root());
  }

  @Test
  public void should_traverse_rest() {
    assertEquals($(5, 6, 7, 8, 9), newArrayList(filter(new ZipperNodeIterator(zipper), not(instanceOf(Iterable.class)))));
  }

  @Test
  public void should_remove() {
    assertTrue(elementsEqual($(6), (Iterable<?>) zipper.remove().node()));
  }

  @Test
  public void should_edit() {
    Object result = zipper.edit(new Function<Object, Object>() {
      @Override
      public Object apply(Object input) {
        return ((Number) input).intValue() * 2;
      }
    }).up().node();
    assertTrue(elementsEqual($(10, 6), (Iterable<?>) result));
  }
}
