package com.paandw.poke.data.domain;

public interface IDomainCallback<T> {
    void success(T t);

    void failure(String message);
}
