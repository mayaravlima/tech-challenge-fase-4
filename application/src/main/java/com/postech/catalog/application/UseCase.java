package com.postech.catalog.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN input);
}