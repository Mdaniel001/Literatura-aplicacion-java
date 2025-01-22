package com.aluracursos.literalura.servicio;

public interface IDeserializacion {

    <T> T obtenerDatos(String json, Class<T> clase);
}
