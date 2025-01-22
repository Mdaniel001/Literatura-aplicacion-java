package com.aluracursos.literalura.servicio;

import com.aluracursos.literalura.excepcion.PeticionNullEspacioEnBlancoException;
import com.aluracursos.literalura.excepcion.ResultadosNoEncontradosException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {
    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            if (body == null || body.isBlank()){
                throw new PeticionNullEspacioEnBlancoException("El cuerpo de la respuesta está vacío o contiene datos nulos.");
            }
//            if (body.contains("\"count\": 0")) {
//                throw new ResultadosNoEncontradosException("No se encontraron resultados para la búsqueda.");
//            }
            return body;

        }catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
