package com.bkowalski.ballsimulator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

// a functional interface, that allows for passing a lambda expression in the form:
// (observable, oldValue, newValue) -> void as an argument for the SingleShotChangeListener constructor
@FunctionalInterface
interface changeLambda<T>{
    void call(ObservableValue<? extends T> observable, T oldValue, T newValue);
}
// we create a class, that implements ChangeListener change method, so that it calls a lambda expression passed
// as a constructor argument and then removes itself from the caller observable
public class SingleShotChangeListener<T> implements ChangeListener<T> {
    private changeLambda<T> lambda;
    public SingleShotChangeListener(changeLambda<T> lambda) {
        super();
        this.lambda = lambda;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        this.lambda.call(observable, oldValue, newValue);
        observable.removeListener(this);
    }
}
