package com.ironhack.otxt.interfaces;

public interface TxTInput<T> {

    public String formatOutput(T value);
    public T parseInput(String input);


}
