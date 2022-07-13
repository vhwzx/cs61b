package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double maxLoad;
    Set<K> keys = new HashSet<>();
    // You should probably define some more!

    /** Constructors */
    private void InitBucket(){
        size = 0;
        for(int i = 0; i < buckets.length; i++)
            buckets[i] = createBucket();
    }
    public MyHashMap() {
        buckets = createTable(100);
        maxLoad = 1;
        InitBucket();
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        maxLoad = 1;
        InitBucket();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        InitBucket();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear(){
        InitBucket();
    }
    private int hash(K key){
        return Math.abs(key.hashCode())%buckets.length;
    }

    private Node find(K key){
        for(Node node : buckets[hash(key)]){
            if(node.key.equals(key))
                return node;
        }
        return null;
    }
    @Override
    public boolean containsKey(K key) {
        Node node = find(key);
        return node != null;
    }

    @Override
    public V get(K key) {
        Node node = find(key);
        if(node == null)
            return null;
        else
            return node.value;
    }

    @Override
    public int size() {
        return size;
    }
    private int hash(K key, int L){
        return Math.abs(key.hashCode())%L;
    }
    private Collection<Node>[] InitBucket(Collection<Node>[] buckets){
        for(int i = 0; i < buckets.length; i++)
            buckets[i] = createBucket();
        return buckets;
    }
    private void resize(){
        int L = buckets.length*2;
        Collection<Node>[] newbuckets = createTable(L);
        newbuckets = InitBucket(newbuckets);
        Iterator<Node> nodes = new MyHashMapNodesIterator(buckets, size);
        for (Iterator<Node> it = nodes; it.hasNext(); ) {
            Node node = it.next();
            newbuckets[hash(node.key, L)].add(node);
        }
        buckets = newbuckets;
    }

    @Override
    public void put(K key, V value) {
        Node node = find(key);
        if(node == null){
            buckets[hash(key)].add(createNode(key, value));
            size += 1;
            keys.add(key);
            if((float)size/buckets.length > maxLoad){
                resize();
            }
        }
        else
            node.value = value;
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }



    private V remove(K key, Node node){
        V value = node.value;
        boolean res = buckets[hash(key)].remove(node);
        size -= 1;
        keys.remove(key);
        return value;
    }

    @Override
    public V remove(K key) {
        Node node = find(key);
        if(node == null)
            return null;
        else
            return remove(key, node);
    }

    @Override
    public V remove(K key, V value) {
        Node node = find(key);
        if(node == null)
            return null;
        else
            return remove(key, node);
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }

    private class MyHashMapNodesIterator implements Iterator<Node>{
        private Collection<Node>[] buckets;
        private int size;
        int bucket_idx;
        Iterator<Node> cur;

        MyHashMapNodesIterator(Collection<Node>[] buckets, int size){
            this.buckets = buckets;
            this.size = size;
            bucket_idx = 0;
            cur = buckets[0].iterator();

        }

        @Override
        public boolean hasNext() {
            return size > 0;
        }

        @Override
        public Node next() {
            while(!cur.hasNext()) {
                cur = buckets[++bucket_idx].iterator();
            }
            size -= 1;
            return cur.next();
        }
    }



}
