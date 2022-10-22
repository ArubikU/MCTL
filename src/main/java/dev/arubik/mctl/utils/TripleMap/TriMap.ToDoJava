package dev.arubik.mctl.utils.TripleMap;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public interface TriMap<K, V, C> {
    public void put(K key, V value, C cvalue);

    public V getv(K key);

    public C getc(K key);

    public void remove(K key);

    public boolean containsKey(K key);

    public boolean containsValues(V value, C cvalue);

    public boolean containsVValue(V value);

    public boolean containsCValue(C cvalue);

    public int size();

    public void clear();

    Set<TriMap.TriEntry<K, V, C>> entrySet();

    interface TriEntry<K, V, C> {
        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry
         *                               has been
         *                               removed from the backing map.
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry. If the mapping
         * has been removed from the backing map (by the iterator's
         * {@code remove} operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry
         *                               has been
         *                               removed from the backing map.
         */
        V getValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation). (Writes through to the map.) The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's {@code remove} operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the {@code put} operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the
         *                                       backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is
         *                                       null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the
         *                                       backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if
         *                                       the entry has been
         *                                       removed from the backing map.
         */
        V setValue(V value);

        /**
         * Returns the value corresponding to this entry. If the mapping
         * has been removed from the backing map (by the iterator's
         * {@code remove} operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry
         *                               has been
         *                               removed from the backing map.
         */
        C getCValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation). (Writes through to the map.) The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's {@code remove} operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the {@code put} operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the
         *                                       backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is
         *                                       null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the
         *                                       backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if
         *                                       the entry has been
         *                                       removed from the backing map.
         */
        C setCValue(C value);

        /**
         * Compares the specified object with this entry for equality.
         * Returns {@code true} if the given object is also a map entry and
         * the two entries represent the same mapping. More formally, two
         * entries {@code e1} and {@code e2} represent the same mapping
         * if
         * 
         * <pre>
         * (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(e2.getKey())) &amp;&amp;
         *         (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(e2.getValue()))
         * </pre>
         * 
         * This ensures that the {@code equals} method works properly across
         * different implementations of the {@code Map.Entry} interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return {@code true} if the specified object is equal to this map
         *         entry
         */
        boolean equals(Object o);

        /**
         * Returns the hash code value for this map entry. The hash code
         * of a map entry {@code e} is defined to be:
         * 
         * <pre>
         * (e.getKey() == null ? 0 : e.getKey().hashCode()) ^
         *         (e.getValue() == null ? 0 : e.getValue().hashCode())
         * </pre>
         * 
         * This ensures that {@code e1.equals(e2)} implies that
         * {@code e1.hashCode()==e2.hashCode()} for any two Entries
         * {@code e1} and {@code e2}, as required by the general
         * contract of {@code Object.hashCode}.
         *
         * @return the hash code value for this map entry
         * @see Object#hashCode()
         * @see Object#equals(Object)
         * @see #equals(Object)
         */
        int hashCode();

        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on key.
         *
         * <p>
         * The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with a null key.
         *
         * @param <K> the {@link Comparable} type of then map keys
         * @param <V> the type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on key.
         * @see Comparable
         * @since 1.8
         */
        public static <K extends Comparable<? super K>, V, C> Comparator<TriMap.TriEntry<K, V, C>> comparingByKey() {
            return (Comparator<TriMap.TriEntry<K, V, C>> & Serializable) (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on
         * value.
         *
         * <p>
         * The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with null values.
         *
         * @param <K> the type of the map keys
         * @param <V> the {@link Comparable} type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on
         *         value.
         * @see Comparable
         * @since 1.8
         */
        public static <K, V extends Comparable<? super V>, C extends Comparable<? super C>> Comparator<TriMap.TriEntry<K, V, C>> comparingByValue() {
            return (Comparator<TriMap.TriEntry<K, V, C>> & Serializable) (c1, c2) -> c1.getValue()
                    .compareTo(c2.getValue());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by key using the given
         * {@link Comparator}.
         *
         * <p>
         * The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the key {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the key.
         * @since 1.8
         */
        public static <K, V, C> Comparator<TriMap.TriEntry<K, V, C>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<TriMap.TriEntry<K, V, C>> & Serializable) (c1, c2) -> cmp.compare(c1.getKey(),
                    c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by value using the given
         * {@link Comparator}.
         *
         * <p>
         * The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the value {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the value.
         * @since 1.8
         */
        public static <K, V, C> Comparator<TriMap.TriEntry<K, V, C>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<TriMap.TriEntry<K, V, C>> & Serializable) (c1, c2) -> cmp.compare(c1.getValue(),
                    c2.getValue());
        }
    }

}
