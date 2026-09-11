package com.treasure.basic.helper;

public interface CommonCallback<T> {
    void onContinue(T value);

    default void onError(String value) {
    }
}
