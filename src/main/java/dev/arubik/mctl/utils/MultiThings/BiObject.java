package dev.arubik.mctl.utils.MultiThings;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class BiObject<V, C> {

    public @Nullable V v;

    public @Nullable C c;

    public BiObject(V v, C c) {
        this.v = v;
        this.c = c;
    }

    public BiObject() {
        this.v = null;
        this.c = null;
    }

    public void reInitalize() {
        this.v = null;
        this.c = null;
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

    public String toString() {
        return "BiObject [v=" + this.v + ", c=" + this.c + "]";
    }

    public boolean equals(BiObject<? extends V, ? extends C> obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        BiObject<?, ?> rhs = (BiObject<?, ?>) obj;
        return (v == null ? rhs.v == null : v.equals(rhs.v)) && (c == null ? rhs.c == null : c.equals(rhs.c));
    }

    public void setFirstValue(@Nullable V v) {
        this.v = v;
    }

    public void setSecondValue(@Nullable C c) {
        this.c = c;
    }

    public boolean containsFirstValue() {
        return this.v != null;
    }

    public boolean containsSecondValue() {
        return this.c != null;
    }

    // create methos to convert the BiObject to hash code and from hash code to
    // BiObject
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((v == null) ? 0 : v.hashCode());
        result = prime * result + ((c == null) ? 0 : c.hashCode());
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
}
