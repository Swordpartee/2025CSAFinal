package com.engine.util;

public class Tuple<T1, T2> {
    private final Object[] elements;

    public Tuple(T1 first, T2 second) {
        elements = new Object[]{first, second};
    }

    public T1 getFirst() {
        return (T1) elements[0];
    }

    public T2 getSecond() {
        return (T2) elements[1];
    }
}
