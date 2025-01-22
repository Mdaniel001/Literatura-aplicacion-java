package com.aluracursos.literalura.repositorio;

import com.aluracursos.literalura.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ILibroRepositorio extends JpaRepository<Libro,Long> {
    List<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

    @Query("SELECT l FROM Libro l WHERE :idiomaLibro MEMBER OF l.idiomas")
    List<Libro> buscarPorIdioma( String idiomaLibro);
}
