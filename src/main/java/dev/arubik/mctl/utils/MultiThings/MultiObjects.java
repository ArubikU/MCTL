package dev.arubik.mctl.utils.MultiThings;

import java.util.function.Consumer;

import javax.annotation.Nullable;

public class MultiObjects {
    public class TriObject<V, C, D> {
        public @Nullable V v;
        public @Nullable C c;
        public @Nullable D d;

        public TriObject(V v, C c, D d) {
            this.v = v;
            this.c = c;
            this.d = d;
        }

        public TriObject() {
            this.v = null;
            this.c = null;
            this.d = null;
        }

        public void reInitalize() {
            this.v = null;
            this.c = null;
            this.d = null;
        }

        public V getFirstValue() {
            return this.v;
        }

        public V getFirstValueOrDefault(V def) {
            return this.v == null ? def : this.v;
        }

        public C getSecondValue() {
            return this.c;
        }

        public C getSecondValueOrDefault(C def) {
            return this.c == null ? def : this.c;
        }

        public D getThirdValue() {
            return this.d;
        }

        public D getThirdValueOrDefault(D def) {
            return this.d == null ? def : this.d;
        }

        @Override
        public String toString() {
            return "TriObject [v=" + this.v + ", c=" + this.c + ", d=" + this.d + "]";
        }

        public boolean equals(TriObject<? extends V, ? extends C, ? extends D> obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            TriObject<?, ?, ?> rhs = (TriObject<?, ?, ?>) obj;
            return ((this.v == rhs.v || (this.v != null && this.v.equals(rhs.v)))
                    && (this.c == rhs.c || (this.c != null && this.c.equals(rhs.c)))
                    && (this.d == rhs.d || (this.d != null && this.d.equals(rhs.d))));
        }

        public void setFirstValue(@Nullable V v) {
            this.v = v;
        }

        public void setSecondValue(@Nullable C c) {
            this.c = c;
        }

        public void setThirdValue(@Nullable D d) {
            this.d = d;
        }

        public boolean containsFirstValue() {
            return this.v != null;
        }

        public boolean containsSecondValue() {
            return this.c != null;
        }

        public boolean containsThirdValue() {
            return this.d != null;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (this.v == null ? 0 : this.v.hashCode());
            result = 31 * result + (this.c == null ? 0 : this.c.hashCode());
            result = 31 * result + (this.d == null ? 0 : this.d.hashCode());
            return result;
        }

        public void functionFirstObject(Consumer<? super V> function) {
            if (function == null) {
                throw new NullPointerException();
            }
            function.accept(this.v);
        }

        public void functionSecondObject(Consumer<? super C> function) {
            if (function == null) {
                throw new NullPointerException();
            }
            function.accept(this.c);
        }

        public void functionThirdObject(Consumer<? super D> function) {
            if (function == null) {
                throw new NullPointerException();
            }
            function.accept(this.d);
        }
    }

    // 4 object
    public class FourObject<V, C, D, E> {
        public @Nullable V v;
        public @Nullable C c;
        public @Nullable D d;
        public @Nullable E e;

        public FourObject(V v, C c, D d, E e) {
            this.v = v;
            this.c = c;
            this.d = d;
            this.e = e;
        }

        public FourObject() {
            this.v = null;
            this.c = null;
            this.d = null;
            this.e = null;
        }

        public void reInitalize() {
            this.v = null;
            this.c = null;
            this.d = null;
            this.e = null;
        }

        public V getFirstValue() {
            return this.v;
        }

        public V getFirstValueOrDefault(V def) {
            return this.v == null ? def : this.v;
        }

        public C getSecondValue() {
            return this.c;
        }

        public C getSecondValueOrDefault(C def) {
            return this.c == null ? def : this.c;
        }

        public D getThirdValue() {
            return this.d;
        }

        public D getThirdValueOrDefault(D def) {
            return this.d == null ? def : this.d;
        }

        public E getFourthValue() {
            return this.e;
        }

        public E getFourthValueOrDefault(E def) {
            return this.e == null ? def : this.e;
        }

        public String toString() {
            return "FourObject [v=" + this.v + ", c=" + this.c + ", d=" + this.d + ", e=" + this.e + "]";
        }

        public boolean equals(FourObject<? extends V, ? extends C, ? extends D, ? extends E> obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            FourObject<?, ?, ?, ?> rhs = (FourObject<?, ?, ?, ?>) obj;
            return ((this.v == rhs.v || (this.v != null && this.v.equals(rhs.v)))
                    && (this.c == rhs.c || (this.c != null && this.c.equals(rhs.c)))
                    && (this.d == rhs.d || (this.d != null && this.d.equals(rhs.d)))
                    && (this.e == rhs.e || (this.e != null && this.e.equals(rhs.e))));
        }
    }

    // 5 object
    public class PentaObject<V, C, D, E, F> {

    }
}
