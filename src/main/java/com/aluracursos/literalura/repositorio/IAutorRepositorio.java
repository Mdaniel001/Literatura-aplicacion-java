package com.aluracursos.literalura.repositorio;

import com.aluracursos.literalura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAutorRepositorio extends JpaRepository<Autor,Long> {

    @Query(" SELECT a FROM Autor a JOIN FETCH a.libros")
    List<Autor> listaDeAutoresRegistrados();

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anioAutor AND (a.anioMuerte IS NULL OR a.anioMuerte > :anioAutor)")
    List<Autor> listaAutorAnioVivo(String anioAutor);
}
