package com.aluracursos.literalura.modelo;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoAutor(@JsonAlias("name") String nombreAutor,
                        @JsonAlias("birth_year") Integer anioNacimiento,
                        @JsonAlias("death_year") Integer anioMuerte) {

}
