package me.eldodebug.soar.types;

public class CircQueue<T> {
    private final T[] arr;
    private int head = 0;
    private int size = 0;

    @SafeVarargs
    public CircQueue(T... arr) {
        this.arr = arr;
        this.size = arr.length;
    }

    public T peek() {
        return arr[head];
    }

    public T poll() {
        T val = arr[head];
        head = (head + 1) % size;
        return val;
    }
}
