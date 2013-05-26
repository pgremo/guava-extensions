package common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.*;
import static common.Cast.cast;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public class Zipper {

  public static Zipper zipper(Iterable root) {
    return new Zipper(instanceOf(Iterable.class), Cast.<Object, Iterable>cast(), new Function<Object, Function<Iterable, Object>>() {
      @Override
      public Function<Iterable, Object> apply(Object node) {
        return cast();
      }
    }, root);
  }

  private Predicate<Object> isBranch;
  private Function<Object, Iterable> getChildren;
  private Function<Object, Function<Iterable, Object>> makeNode;
  private Object node;
  private Iterable lefts;
  private Iterable rights;
  private Zipper parent;
  private boolean isChanged;

  public Zipper(Predicate<Object> isBranch, Function<Object, Iterable> getChildren, Function<Object, Function<Iterable, Object>> makeNode, Object node) {
    this(isBranch, getChildren, makeNode, node, emptyList(), emptyList(), null, false);
  }

  private Zipper(Predicate<Object> isBranch, Function<Object, Iterable> getChildren, Function<Object, Function<Iterable, Object>> makeNode, Object node, Iterable lefts, Iterable rights, Zipper parent, boolean isChanged) {
    this.isBranch = isBranch;
    this.getChildren = getChildren;
    this.makeNode = makeNode;
    this.node = node;
    this.lefts = lefts;
    this.rights = rights;
    this.parent = parent;
    this.isChanged = isChanged;
  }

  public Object node() {
    return node;
  }

  public Object root() {
    return root(this);
  }

  private Object root(Zipper current) {
    return current.parent == null ? current.node() : root(current.up());
  }

  public Zipper down() {
    if (!isBranch.apply(node)) return null;
    Iterable children = getChildren.apply(node);
    return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      getFirst(children, null),
      emptyList(),
      skip(children, 1),
      this,
      isChanged);
  }

  public Zipper up() {
    if (parent == null || !isChanged) return parent;
    return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      makeNode.apply(parent.node()).apply(concat(lefts, singleton(node), rights)),
      parent.lefts,
      parent.rights,
      parent.parent,
      isChanged);
  }

  public Zipper right() {
    if (isEmpty(rights)) return null;
    return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      getFirst(rights, null),
      concat(lefts, singleton(node)),
      skip(rights, 1),
      parent,
      isChanged);
  }

  public Zipper rightMost() {
    if (isEmpty(rights)) return this;
    return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      getLast(rights, null),
      concat(lefts, singleton(node), limit(rights, size(rights) - 1)),
      emptyList(),
      parent,
      isChanged);
  }

  public Zipper left() {
    if (isEmpty(lefts)) return null;
    return new Zipper(isBranch,
      getChildren,
      makeNode,
      getLast(lefts, null),
      limit(lefts, size(lefts) - 1),
      concat(singleton(node), rights),
      parent,
      isChanged);
  }

  public boolean end() {
    return false;
  }

  public Zipper next() {
    if (isBranch.apply(node)) return down();
    Zipper right = right();
    if (right != null) return right;
    Zipper p = this;
    while (true) {
      if (p.up() == null)
        return new End(
          isBranch,
          getChildren,
          makeNode,
          makeNode,
          emptyList(),
          emptyList(),
          this,
          isChanged);
      Zipper up = p.up();
      right = up.right();
      if (right != null) return right;
      p = up;
    }
  }

  public Zipper prev() {
    if (isEmpty(lefts)) return up();
    Zipper left = left();
    while (true) {
      Zipper child = isBranch.apply(left) ? left.down() : null;
      if (child == null) return left;
      left = child.rightMost();
    }
  }

  public Zipper remove() {
    if (parent == null) throw new RuntimeException("Cannot remove top");
    if (isEmpty(lefts)) return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      makeNode.apply(parent.node()).apply(rights),
      emptyList(),
      rights,
      parent.parent,
      true
    );
    Zipper left = left();
    while (true) {
      Zipper child = isBranch.apply(left) ? left.down() : null;
      if (child == null) return new Zipper(
        left.isBranch,
        left.getChildren,
        left.makeNode,
        left,
        left.lefts,
        left.rights,
        left.parent.parent,
        true
      );
      left = child.rightMost();
    }
  }

  public Zipper replace(Object value) {
    return new Zipper(
      isBranch,
      getChildren,
      makeNode,
      value,
      lefts,
      rights,
      parent,
      true
    );
  }

  public Zipper edit(Function<Object, Object> function) {
    return replace(function.apply(node));
  }

  private class End extends Zipper {
    private End(Predicate<Object> isBranch, Function<Object, Iterable> getChildren, Function<Object, Function<Iterable, Object>> makeNode, Object node, Iterable lefts, Iterable rights, Zipper parent, boolean isChanged) {
      super(isBranch, getChildren, makeNode, node, lefts, rights, parent, isChanged);
    }

    @Override
    public boolean end() {
      return true;
    }

    @Override
    public Zipper next() {
      return this;
    }
  }
}
