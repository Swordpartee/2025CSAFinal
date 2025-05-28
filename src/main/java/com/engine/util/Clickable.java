package com.engine.util;

public interface Clickable {
    default public boolean onClick(Point point) {
        return false;
    };
}
