package com.aluracursos.literalura.excepcion;

public class EntradaInvalidaException extends RuntimeException{
    public EntradaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
