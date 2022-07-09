package deque;

import java.util.Iterator;
import java.lang.Iterable;
public class LinkedListDeque<T> implements Iterable<T>{
    public static class Node<T>{
        public T val;
        public Node<T> pre;
        public Node<T> next;
        public Node(T v){
            val = v;
            pre = null;
            next = null;
        }

        public Node(){
            val = null;
            pre = null;
            next = null;
        }

    }

    private int size;
    private Node<T> head;
    private Node<T> tail;
    public LinkedListDeque(){
        size = 0;
        head = new Node<T>();
        tail = new Node<T>();
        head.next = tail;
        tail.pre = head;

    }
    public void addFirst(T item){
        Node<T> newitem = new Node<T>(item);
        head.next.pre = newitem;
        newitem.next = head.next;
        head.next = newitem;
        newitem.pre = head;

        size += 1;
    }

    public void addLast(T item){
        Node<T> newitem = new Node<T>(item);
        tail.pre.next = newitem;
        newitem.pre = tail.pre;
        tail.pre = newitem;
        newitem.next = tail;

        size += 1;
    }


    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        StringBuilder s = new StringBuilder();
        Node<T> cur = this.head.next;
        while(cur != null && cur.val != null){
            s.append(cur.val.toString());
            if(cur.next != null && cur.next.val != null)
                s.append(" ");
            cur = cur.next;
        }
        System.out.println(s);
        System.out.println();
    }

    public T removeFirst(){
        if(size == 0)
            return null;
        Node<T> removed = head.next;
        head.next = removed.next;
        removed.next.pre = head;
        size -= 1;
        return removed.val;
    }

    public T removeLast(){
        if(size == 0)
            return null;
        Node<T> removed = tail.pre;
        tail.pre = removed.pre;
        removed.pre.next = tail;
        size -= 1;
        return removed.val;
    }

    public T get(int index){
        Node<T> cur = head.next;
        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        return cur.val;

    }
    private Node<T> getRecursive(int index, Node<T> node){
        if(index == 0)
            return node;
        else
            return getRecursive(index - 1, node.next);
    }
    public T getRecursive(int index){
        return getRecursive(index, this.head.next).val;
    }

    public boolean equals(Object o){
        if(o == null) {return false;}
        if(this == o) {return true;}
        if(this.getClass() != o.getClass()) {return false;}
        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
        if(this.size() != other.size()){return false;}
        Node<T> t1 = this.head.next;
        Node<T> t2 = other.head.next;
        while(t1 != null && t1.val != null){
            if(!(t1.val.equals(t2.val)))
                return false;
            t1 = t1.next;
            t2 = t2.next;
        }
        return true;

    }

    class LinkedListDequeIterator implements Iterator<T>{
        Node<T> head;
        LinkedListDequeIterator(Node<T> h){
            head = h;
        }

        @Override
        public boolean hasNext() {
            return head.next.val != null;
        }

        @Override
        public T next() {
            head = head.next;
            return head.val;
        }
    }

    public Iterator<T> iterator(){
        return new LinkedListDequeIterator(this.head);

    }

}
