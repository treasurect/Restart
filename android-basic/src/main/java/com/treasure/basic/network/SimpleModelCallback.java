package com.treasure.basic.network;

public abstract class SimpleModelCallback<T> implements ModelCallback<T> {
    @Override
    public void onFailure(String msg) {
        onResultError(msg);
    }

    @Override
    public void onError(String msg) {
        onResultError(msg);
    }

    @Override
    public abstract void onSuccess(T data);

    public abstract void onResultError(String msg);
}
