package com.example.orderapisubject.exception;


public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }
}

