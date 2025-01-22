package com.aluracursos.literalura.modelo;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas = new ArrayList<>();
    private Double cantidadDescargas;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores = new ArrayList<>();


    public Libro(){
    }

    public Libro(DatoLibro datoLibro) {
        this.titulo = datoLibro.titulo();
        this.idiomas = datoLibro.idiomas();
        this.cantidadDescargas = datoLibro.cantidadDescargas();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Double getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Double cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(a -> a.agregarLibro(this));
        this.autores = autores;
    }

    @Override
    public String toString() {
        return "Libro:" +
                ", titulo='" + titulo + '\'' +
                ", idiomas=" + idiomas +
                ", cantidadDescargas=" + cantidadDescargas +
                ", autores=" + autores +
                '}';
    }
}
