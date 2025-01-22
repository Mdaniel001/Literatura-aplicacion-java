package com.aluracursos.literalura.modelo;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoResultado(@JsonAlias("results") List<DatoLibro> resultado) {
}
