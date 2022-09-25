package dev.arubik.mctl.utils.MultiThings;

import java.util.Objects;

public class MultiConsumers {

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        public void accept(T t, U u, V v);

        public default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
            Objects.requireNonNull(after);
            return (a, b, c) -> {
                accept(a, b, c);
                after.accept(a, b, c);
            };
        }
    }

    @FunctionalInterface
    public interface FourConsumer<T, U, V, C> {
        public void accept(T t, U u, V v, C c);

        public default FourConsumer<T, U, V, C> andThen(
                FourConsumer<? super T, ? super U, ? super V, ? super C> after) {
            Objects.requireNonNull(after);
            return (a, b, c, d) -> {
                accept(a, b, c, d);
                after.accept(a, b, c, d);
            };
        }
    }

    @FunctionalInterface
    public interface FiveConsumer<T, U, V, C, D> {
        public void accept(T t, U u, V v, C c, D d);

        public default FiveConsumer<T, U, V, C, D> andThen(
                FiveConsumer<? super T, ? super U, ? super V, ? super C, ? super D> after) {
            Objects.requireNonNull(after);
            return (a, b, c, d, e) -> {
                accept(a, b, c, d, e);
                after.accept(a, b, c, d, e);
            };
        }
    }

    @FunctionalInterface
    public interface SixConsumer<T, U, V, C, D, E> {
        public void accept(T t, U u, V v, C c, D d, E e);

        public default SixConsumer<T, U, V, C, D, E> andThen(
                SixConsumer<? super T, ? super U, ? super V, ? super C, ? super D, ? super E> after) {
            Objects.requireNonNull(after);
            return (a, b, c, d, e, f) -> {
                accept(a, b, c, d, e, f);
                after.accept(a, b, c, d, e, f);
            };
        }
    }
}
