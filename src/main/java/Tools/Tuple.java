package Tools;

import org.javatuples.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public abstract class Tuple {
    public static <A> Unit<A> of(A a) {
        return Unit.with(a);
    }
    public static <A, B> Pair<A, B> of(A a, B b) {
        return Pair.with(a, b);
    }
    public static <A, B, C> Triplet<A, B, C> of(A a, B b, C c) {
        return Triplet.with(a, b, c);
    }
    public static <A, B, C, D> Quartet<A, B, C, D> of(A a, B b, C c, D d) {
        return Quartet.with(a, b, c, d);
    }
    public static <A, B, C, D, E> Quintet<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return Quintet.with(a, b, c, d, e);
    }
    public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> of(A a, B b, C c, D d, E e, F f) {
        return Sextet.with(a, b, c, d, e, f);
    }
    public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> of(A a, B b, C c, D d, E e, F f, G g) {
        return Septet.with(a, b, c, d, e, f, g);
    }
    public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> of(A a, B b, C c, D d, E e, F f, G g, H h) {
        return Octet.with(a, b, c, d, e, f, g, h);
    }
    public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> of(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
        return Ennead.with(a, b, c, d, e, f, g, h, i);
    }
    public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> of(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
        return Decade.with(a, b, c, d, e, f, g, h, i, j);
    }

    private abstract static class ZippedIterator<A> implements Iterator<A> {
        private Iterator[] iters;

        public ZippedIterator(Iterator... iters) {this.iters = iters;}
        @Override
        public boolean hasNext() {
            for (var itr : iters) {if (!itr.hasNext()) return false;}
            return true;
        }

    }


    public static <A> Iterable<Unit<A>> zip(Iterable<A> as) {
        var ai = as.iterator();
        return () -> new ZippedIterator<>(ai) {
            @Override
            public Unit<A> next() {return Tuple.of(ai.next());}
        };
    }

    public static <A, B> Iterable<Pair<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        var ai = as.iterator();
        var bi = bs.iterator();
        return () -> new ZippedIterator<>(ai, bi) {
            @Override
            public Pair<A, B> next() {return Tuple.of(ai.next(), bi.next());}
        };
    }

}
