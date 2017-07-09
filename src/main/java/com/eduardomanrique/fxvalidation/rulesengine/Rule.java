package com.eduardomanrique.fxvalidation.rulesengine;

public interface Rule<T> {

    int getPriotiy();

    void onFact(T fact, Validation validation);

}
