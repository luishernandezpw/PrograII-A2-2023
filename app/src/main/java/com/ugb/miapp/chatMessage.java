package com.ugb.miapp;

public class chatMessage {
    public boolean posicion;//izquierdo, derecho
    public String message;

    public chatMessage(boolean posicion, String message) {
        super();
        this.posicion = posicion;
        this.message = message;
    }
}