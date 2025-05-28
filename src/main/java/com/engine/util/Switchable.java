package com.engine.util;

public class Switchable<T1> {
    private final T1 first;
    private final T1 second;

    private boolean condition;

    public Switchable(T1 first, T1 second) {
        this.first = first;
        this.second = second;

        this.condition = true; // Default to first
    }

    public T1 getFirst() {
        return first;
    }

    public T1 getSecond() {
        return second;
    }

    public T1 get() {
        return condition ? first : second;
    }
}

    
