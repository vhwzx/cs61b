package deque;

import java.util.Iterator;
import java.lang.Iterable;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int head;
    private int tail;
    private T[] data;


    public ArrayDeque(){
        head = 50;
        tail = 50;
        data = (T []) new Object[100];
    }
    public int size(){
        return tail - head;
    }

    private void resize(int size){
        T[] newdata = (T []) new Object[size];
        System.arraycopy(data, head, newdata, size/3, size());
        tail = size/3 + size();
        head = size/3;
        data = newdata;

    }
    public void addFirst(T item){
        if(head == 0){
            resize(data.length*3);
        }
        head -= 1;
        data[head] = item;
    }
    public void addLast(T item){
        if(tail == data.length)
            resize(data.length*3);
        data[tail] = item;
        tail += 1;
    }
    public boolean isEmpyt(){
        return size() == 0;
    }

    public void printDeque(){
        StringBuilder s = new StringBuilder();
        for(int i = head; i < tail; i++){
            s.append(data[i].toString());
            if(i != tail - 1)
                s.append(" ");
        }
        System.out.println(s);
        System.out.println();
    }
    public T removeFirst(){
        if(size() == 0)
            return null;
        T removed = data[head];
        head += 1;
        return removed;
    }
    public T removeLast(){
        if(size() == 0)
            return null;
        T removed = data[tail - 1];
        tail -= 1;
        return removed;
    }
    public T get(int index){
        return data[head+index];
    }

    public boolean equals(Object o){
        if(o == null) {return false;}
        if(this == o) {return true;}
        if(this.getClass() != o.getClass()) {return false;}
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if(this.size() != other.size()){return false;}
        for(int i = 0; i < size(); i++){
            if(!(this.get(i).equals(other.get(i))))
                return false;
        }
        return true;
    }

    class ArrayDequeIterator implements Iterator<T>{
        int head;
        int tail;
        ArrayDequeIterator(int h, int t){
            head = h;
            tail = t;
        }

        @Override
        public boolean hasNext() {
            return head < tail;
        }

        @Override
        public T next() {
            head += 1;
            return data[head-1];
        }
    }

    public Iterator<T> iterator(){
        return new ArrayDequeIterator(head, tail);
    }
}
