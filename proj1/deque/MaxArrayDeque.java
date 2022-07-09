package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque {
    Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c){
        Comparator<T> cmp = c;
    }

    public MaxArrayDeque() {

    }

    public T max(){
        return max(cmp);
    }
    public T max(Comparator<T> c){
        if(size() == 0)
            return null;
        T max_val = (T) get(0);
        for(Object x : this){
            if(c.compare((T) x, max_val) == 1)
                max_val = (T) x;
        }
        return max_val;

    }


}
