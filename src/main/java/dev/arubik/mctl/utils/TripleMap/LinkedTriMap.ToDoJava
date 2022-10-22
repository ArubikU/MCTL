package dev.arubik.mctl.utils.TripleMap;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LinkedTriMap<K, V, C>
        extends TripleMap<K, V, C>
        implements TriMap<K, V, C> {

    /*
     * Implementation note. A previous version of this class was
     * internally structured a little differently. Because superclass
     * TripleMap now uses trees for some of its nodes, class
     * LinkedTriMap.Entry is now treated as intermediary node class
     * that can also be converted to tree form. The name of this
     * class, LinkedTriMap.Entry, is confusing in several ways in its
     * current context, but cannot be changed. Otherwise, even though
     * it is not exported outside this package, some existing source
     * code is known to have relied on a symbol resolution corner case
     * rule in calls to removeEldestEntry that suppressed compilation
     * errors due to ambiguous usages. So, we keep the name to
     * preserve unmodified compilability.
     *
     * The changes in node classes also require using two fields
     * (head, tail) rather than a pointer to a header node to maintain
     * the doubly-linked before/after list. This class also
     * previously used a different style of callback methods upon
     * access, insertion, and removal.
     */

    /**
     * TripleMap.Node subclass for normal LinkedTriMap entries.
     */
    static class Entry<K, V, C> extends TripleMap.Node<K, V, C> {
        Entry<K, V, C> before, after;

        Entry(int hash, K key, V value, C cvalue, Node<K, V, C> next) {
            super(hash, key, value, cvalue, next);
        }
    }

    @java.io.Serial
    private static final long serialVersionUID = 3801124242820219131L;

    /**
     * The head (eldest) of the doubly linked list.
     */
    transient LinkedTriMap.Entry<K, V, C> head;

    /**
     * The tail (youngest) of the doubly linked list.
     */
    transient LinkedTriMap.Entry<K, V, C> tail;

    /**
     * The iteration ordering method for this linked hash map: {@code true}
     * for access-order, {@code false} for insertion-order.
     *
     * @serial
     */
    final boolean accessOrder;

    // internal utilities

    // link at the end of list
    private void linkNodeLast(LinkedTriMap.Entry<K, V, C> p) {
        LinkedTriMap.Entry<K, V, C> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }

    // apply src's links to dst
    private void transferLinks(LinkedTriMap.Entry<K, V, C> src,
            LinkedTriMap.Entry<K, V, C> dst) {
        LinkedTriMap.Entry<K, V, C> b = dst.before = src.before;
        LinkedTriMap.Entry<K, V, C> a = dst.after = src.after;
        if (b == null)
            head = dst;
        else
            b.after = dst;
        if (a == null)
            tail = dst;
        else
            a.before = dst;
    }

    // overrides ofTripleMap hook methods

    void reinitialize() {
        super.reinitialize();
        head = tail = null;
    }

    Node<K, V, C> newNode(int hash, K key, V value, C cval, Node<K, V, C> e) {
        LinkedTriMap.Entry<K, V, C> p = new LinkedTriMap.Entry<>(hash, key, value, cval, e);
        linkNodeLast(p);
        return p;
    }

    Node<K, V, C> replacementNode(Node<K, V, C> p, Node<K, V, C> next) {
        LinkedTriMap.Entry<K, V, C> q = (LinkedTriMap.Entry<K, V, C>) p;
        LinkedTriMap.Entry<K, V, C> t = new LinkedTriMap.Entry<>(q.hash, q.key, q.value, q.cvalue, next);
        transferLinks(q, t);
        return t;
    }

    TreeNode<K, V, C> newTreeNode(int hash, K key, V value, C cvalue, Node<K, V, C> next) {
        TreeNode<K, V, C> p = new TreeNode<>(hash, key, value, cvalue, next);
        linkNodeLast(p);
        return p;
    }

    TreeNode<K, V, C> replacementTreeNode(Node<K, V, C> p, Node<K, V, C> next) {
        LinkedTriMap.Entry<K, V, C> q = (LinkedTriMap.Entry<K, V, C>) p;
        TreeNode<K, V, C> t = new TreeNode<>(q.hash, q.key, q.value, q.cvalue, next);
        transferLinks(q, t);
        return t;
    }

    void afterNodeRemoval(Node<K, V, C> e) { // unlink
        LinkedTriMap.Entry<K, V, C> p = (LinkedTriMap.Entry<K, V, C>) e, b = p.before, a = p.after;
        p.before = p.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a == null)
            tail = b;
        else
            a.before = b;
    }

    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        LinkedTriMap.Entry<K, V, C> first;
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }

    void afterNodeAccess(Node<K, V, C> e) { // move node to last
        LinkedTriMap.Entry<K, V, C> last;
        if (accessOrder && (last = tail) != e) {
            LinkedTriMap.Entry<K, V, C> p = (LinkedTriMap.Entry<K, V, C>) e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }

    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after) {
            s.writeObject(e.key);
            s.writeObject(e.value);
            s.writeObject(e.cvalue);
        }
    }

    /**
     * Constructs an empty insertion-ordered {@code LinkedTriMap} instance
     * with the specified initial capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public LinkedTriMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }

    /**
     * Constructs an empty insertion-ordered {@code LinkedTriMap} instance
     * with the specified initial capacity and a default load factor (0.75).
     *
     * @param initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    public LinkedTriMap(int initialCapacity) {
        super(initialCapacity);
        accessOrder = false;
    }

    /**
     * Constructs an empty insertion-ordered {@code LinkedTriMap} instance
     * with the default initial capacity (16) and load factor (0.75).
     */
    public LinkedTriMap() {
        super();
        accessOrder = false;
    }

    /**
     * Constructs an insertion-ordered {@code LinkedTriMap} instance with
     * the same mappings as the specified map. The {@code LinkedTriMap}
     * instance is created with a default load factor (0.75) and an initial
     * capacity sufficient to hold the mappings in the specified map.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public LinkedTriMap(TriMap<? extends K, ? extends V, ? extends C> m) {
        super();
        accessOrder = false;
        putMapEntries(m, false);
    }

    /**
     * Constructs an empty {@code LinkedTriMap} instance with the
     * specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @param accessOrder     the ordering mode - {@code true} for
     *                        access-order, {@code false} for insertion-order
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public LinkedTriMap(int initialCapacity,
            float loadFactor,
            boolean accessOrder) {
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value
     */
    public boolean containsValue(Object value) {
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after) {
            V v = e.value;
            if (v == value || (value != null && value.equals(v)))
                return true;
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>
     * More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}. (There can be at most one such mapping.)
     *
     * <p>
     * A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     */
    public V get(Object key) {
        Node<K, V, C> e;
        if ((e = getNode(key)) == null)
            return null;
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }

    /**
     * {@inheritDoc}
     */
    public V getOrDefault(Object key, V defaultValue) {
        Node<K, V, C> e;
        if ((e = getNode(key)) == null)
            return defaultValue;
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        super.clear();
        head = tail = null;
    }

    /**
     * Returns {@code true} if this map should remove its eldest entry.
     * This method is invoked by {@code put} and {@code putAll} after
     * inserting a new entry into the map. It provides the implementor
     * with the opportunity to remove the eldest entry each time a new one
     * is added. This is useful if the map represents a cache: it allows
     * the map to reduce memory consumption by deleting stale entries.
     *
     * <p>
     * Sample use: this override will allow the map to grow up to 100
     * entries and then delete the eldest entry each time a new entry is
     * added, maintaining a steady state of 100 entries.
     * 
     * <pre>
     * private static final int MAX_ENTRIES = 100;
     *
     * protected boolean removeEldestEntry(TriMap.TriEntry eldest) {
     *     return size() &gt; MAX_ENTRIES;
     * }
     * </pre>
     *
     * <p>
     * This method typically does not modify the map in any way,
     * instead allowing the map to modify itself as directed by its
     * return value. It <i>is</i> permitted for this method to modify
     * the map directly, but if it does so, it <i>must</i> return
     * {@code false} (indicating that the map should not attempt any
     * further modification). The effects of returning {@code true}
     * after modifying the map from within this method are unspecified.
     *
     * <p>
     * This implementation merely returns {@code false} (so that this
     * map acts like a normal map - the eldest element is never removed).
     *
     * @param eldest The least recently inserted entry in the map, or if
     *               this is an access-ordered map, the least recently accessed
     *               entry. This is the entry that will be removed it this
     *               method returns {@code true}. If the map was empty prior
     *               to the {@code put} or {@code putAll} invocation resulting
     *               in this invocation, this will be the entry that was just
     *               inserted; in other words, if the map contains a single
     *               entry, the eldest entry is also the newest.
     * @return {@code true} if the eldest entry should be removed
     *         from the map; {@code false} if it should be retained.
     */
    protected boolean removeEldestEntry(TriMap.TriEntry<K, V, C> eldest) {
        return false;
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa. If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined. The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations. It does not support the {@code add} or {@code addAll}
     * operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@codeTripleMap}.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new LinkedKeySet();
            keySet = ks;
        }
        return ks;
    }

    @Override
    final <T> T[] keysToArray(T[] a) {
        Object[] r = a;
        int idx = 0;
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after) {
            r[idx++] = e.key;
        }
        return a;
    }

    @Override
    final <T> T[] valuesToArray(T[] a) {
        Object[] r = a;
        int idx = 0;
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after) {
            r[idx++] = e.value;
        }
        return a;
    }

    final class LinkedKeySet extends AbstractSet<K> {
        public final int size() {
            return size;
        }

        public final void clear() {
            LinkedTriMap.this.clear();
        }

        public final Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }

        public final boolean contains(Object o) {
            return containsKey(o);
        }

        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }

        public final Spliterator<K> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                    Spliterator.ORDERED |
                    Spliterator.DISTINCT);
        }

        public Object[] toArray() {
            return keysToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return keysToArray(prepareArray(a));
        }

        public final void forEach(Consumer<? super K> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
                action.accept(e.key);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa. If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined. The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations. It does not
     * support the {@code add} or {@code addAll} operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@codeTripleMap}.
     *
     * @return a view of the values contained in this map
     */
    public Collection<V> Vvalues() {
        Collection<V> vs = Vvalues;
        if (vs == null) {
            vs = new LinkedValues();
            Vvalues = vs;
        }
        return vs;
    }

    public Collection<C> Cvalues() {
        Collection<C> vs = Cvalues;
        if (vs == null) {
            vs = new LinkedCValues();
            Cvalues = vs;
        }
        return vs;
    }

    final class LinkedValues extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            LinkedTriMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new LinkedValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                    Spliterator.ORDERED);
        }

        public Object[] toArray() {
            return valuesToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return valuesToArray(prepareArray(a));
        }

        public final void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
                action.accept(e.value);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    final class LinkedCValues extends AbstractCollection<C> {
        public final int size() {
            return size;
        }

        public final void clear() {
            LinkedTriMap.this.clear();
        }

        public final Iterator<C> iterator() {
            return new LinkeCValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<C> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                    Spliterator.ORDERED);
        }

        public Object[] toArray() {
            return valuesToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return valuesToArray(prepareArray(a));
        }

        public final void forEach(Consumer<? super V> action, Consumer<? super C> action2) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
                action.accept(e.value);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa. If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined. The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations. It does not support the
     * {@code add} or {@code addAll} operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@codeTripleMap}.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<TriMap.TriEntry<K, V, C>> entrySet() {
        Set<TriMap.TriEntry<K, V, C>> es;
        return (es = entrySet) == null ? (entrySet = new LinkedEntrySet()) : es;
    }

    final class LinkedEntrySet extends AbstractSet<TriMap.TriEntry<K, V, C>> {
        public final int size() {
            return size;
        }

        public final void clear() {
            LinkedTriMap.this.clear();
        }

        public final Iterator<TriMap.TriEntry<K, V, C>> iterator() {
            return new LinkedEntryIterator();
        }

        public final boolean contains(Object o) {
            if (!(o instanceof TriMap.TriEntry))
                return false;
            TriMap.TriEntry<?, ?, ?> e = (TriMap.TriEntry<?, ?, ?>) o;
            Object key = e.getKey();
            Node<K, V, C> candidate = getNode(key);
            return candidate != null && candidate.equals(e);
        }

        public final boolean remove(Object o) {
            if (o instanceof TriMap.TriEntry) {
                TriMap.TriEntry<?, ?, ?> e = (TriMap.TriEntry<?, ?, ?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        public final Spliterator<TriMap.TriEntry<K, V, C>> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                    Spliterator.ORDERED |
                    Spliterator.DISTINCT);
        }

        public final void forEach(Consumer<? super TriMap.TriEntry<K, V, C>> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
                action.accept(e);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    // Map overrides

    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (action == null)
            throw new NullPointerException();
        int mc = modCount;
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
            action.accept(e.key, e.value);
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }

    public void forEachC(BiConsumer<? super K, ? super C> action) {
        if (action == null)
            throw new NullPointerException();
        int mc = modCount;
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
            action.accept(e.key, e.cvalue);
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (function == null)
            throw new NullPointerException();
        int mc = modCount;
        for (LinkedTriMap.Entry<K, V, C> e = head; e != null; e = e.after)
            e.value = function.apply(e.key, e.value);
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }

    // Iterators

    abstract class LinkedHashIterator {
        LinkedTriMap.Entry<K, V, C> next;
        LinkedTriMap.Entry<K, V, C> current;
        int expectedModCount;

        LinkedHashIterator() {
            next = head;
            expectedModCount = modCount;
            current = null;
        }

        public final boolean hasNext() {
            return next != null;
        }

        final LinkedTriMap.Entry<K, V, C> nextNode() {
            LinkedTriMap.Entry<K, V, C> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            current = e;
            next = e.after;
            return e;
        }

        public final void remove() {
            Node<K, V, C> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            removeNode(p.hash, p.key, null, false, false);
            expectedModCount = modCount;
        }
    }

    final class LinkedKeyIterator extends LinkedHashIterator
            implements Iterator<K> {
        public final K next() {
            return nextNode().getKey();
        }
    }

    final class LinkedValueIterator extends LinkedHashIterator
            implements Iterator<V> {
        public final V next() {
            return nextNode().value;
        }
    }

    final class LinkeCValueIterator extends LinkedHashIterator
            implements Iterator<C> {
        public final C next() {
            return nextNode().cvalue;
        }
    }

    final class LinkedEntryIterator extends LinkedHashIterator
            implements Iterator<TriMap.TriEntry<K, V, C>> {
        public final TriMap.TriEntry<K, V, C> next() {
            return nextNode();
        }
    }

}
