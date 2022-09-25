package dev.arubik.mctl.utils.MultiThings;

import java.util.function.Consumer;

public class ForEachArrays<T> {

    T[] array;

    public ForEachArrays(T[] a) {
        this.array = a;
    }

    public void forEachArray(Consumer a) {
        for (T b : array) {
            a.accept(b);
        }
    }
}
