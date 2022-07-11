package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B{
    private class BSTNode<K extends  Comparable<K>, v>{
        private K key;
        private V value;
        private BSTNode<K, v> left;
        private BSTNode<K, V> right;
        public BSTNode(K k, V v){
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }
    private BSTNode<K, V> root;
    private int size;

    public void BSTMap(){
        root = null;
        int size = 0;
    }


    private String keys2string(BSTNode<K, V> node){
        if(node == null)
            return "";
        StringBuilder s = new StringBuilder();
        s.append(keys2string(node.left));
        s.append(" ");
        s.append(node.value.toString());
        s.append(" ");
        s.append(keys2string(node.right));
        s.append(" ");
        return s.toString();
    }
    public void printInOrder(){
        System.out.println(root);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private BSTNode<K, V> find(BSTNode<K, V> node, K key){
        if(node == null)
            return null;
        int res = node.key.compareTo(key);
        if(res == 0)
            return node;
        if(res > 0)
            return find(node.left, key);
        return find(node.right, key);
    }
    @Override
    public boolean containsKey(Object key) {
        BSTNode<K, V> res = find(root, (K) key);
        return res != null;
    }

    @Override
    public Object get(Object key) {
        BSTNode<K, V> res = find(root, (K) key);
        if(res == null)
            return null;
        return res.value;
    }

    @Override
    public int size() {
        return size;
    }
    private boolean isleaf(BSTNode<K, V> node){
        return node.left == null && node.right == null;
    }

    private void put(BSTNode<K, V> node, K key, V value){
        int res = node.key.compareTo(key);
        if(res == 0)
            node.value = value;
        else if (res > 0) {
            if(node.left == null)
                node.left = new BSTNode<>(key, value);
            else
                put(node.left, key, value);
        } else {
            if(node.right == null)
                node.right = new BSTNode<>(key, value);
            else
                put(node.right, key, value);
        }
    }
    @Override
    public void put(Object key, Object value) {
        size += 1;
        if(root == null)
            root = new BSTNode<>((K) key, (V) value);
        else{
            put(root, (K) key, (V) value);
        }
    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public Object remove(Object key, Object value) {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
