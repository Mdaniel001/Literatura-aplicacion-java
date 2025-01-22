package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.excepcion.EntradaInvalidaException;
import com.aluracursos.literalura.excepcion.PeticionNullEspacioEnBlancoException;
import com.aluracursos.literalura.excepcion.ResultadosNoEncontradosException;
import com.aluracursos.literalura.modelo.*;
import com.aluracursos.literalura.repositorio.IAutorRepositorio;
import com.aluracursos.literalura.repositorio.ILibroRepositorio;
import com.aluracursos.literalura.servicio.ConsumoApi;
import com.aluracursos.literalura.servicio.Deserializacion;


import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leer = new Scanner(System.in);
    private DatoResultado dato;
    private final ConsumoApi consumoApi = new ConsumoApi();
    private Deserializacion deserializar = new Deserializacion();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String BUSCAR = "?search=";

    private ILibroRepositorio libroRepositorio;
    private IAutorRepositorio autorRepositorio;


    public Principal(ILibroRepositorio libroRepositorio, IAutorRepositorio autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void muestraElMenu() {

        String entrada;
        int opc;
        imprimirTituloMenu();
        while (true) {
            try {
                imprimirMenu();
                entrada = leer.nextLine();
                opc = Integer.parseInt(entrada);
                switch (opc) {
                    case 1 -> traerDatos();
                    case 2 -> mostrarLibrosRegistrados();
                    case 3 -> mostrarAutoresRegistrados();
                    case 4 -> mostrarAutoresVivos();
                    case 5 -> mostrarLibroIdioma();
                    case 6 -> mostrarTop10Libros();
                    case 7 -> mostrarEstadistica();

                    case 0 -> {
                        System.out.println("Ha finalizado el programa, gracias por elegir nuestra Biblioteca Literalura.");
                        System.exit(0);
                    }
                    default -> System.out.println("La opción ingresada no es válido, inténtelo nuevamente");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: La opción ingresada debe ser un número. Por favor, inténtelo nuevamente.");
            }
        }
    }

    private void traerDatos() {
        String nombreLibro;
        String json;
        try {
            nombreLibro = validarEntrada();
            json = consumoApi.obtenerDatos(URL_BASE + BUSCAR + nombreLibro.replace(" ", "+"));
            if (json.contains("\"count\": 0") || json.contains("\"results\":[]")) {
                throw new ResultadosNoEncontradosException("No se encontraron resultados del libro en la Biblioteca Literalura.");
            }
            //System.out.println("Api completa = " + json);
            dato = deserializar.obtenerDatos(json, DatoResultado.class);
            //System.out.println("Dato de libros encontrados = " + dato);

            filtrarDatosYGuardar(nombreLibro);
        } catch (EntradaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (PeticionNullEspacioEnBlancoException e) {
            System.out.println("No se recibieron datos válidos de la API: " + e.getMessage());
        } catch (ResultadosNoEncontradosException e) {
            System.out.println(e.getMessage());
        }
    }


    private void mostrarLibrosRegistrados() {

        List<Libro> libros = libroRepositorio.findAll();
        this.imprimirTituloDeLibroRegistrado();
        //libros.forEach(libro ->imprimirLibroRegistrado(libro));
        libros.forEach(this::imprimirLibroRegistrado);
        //forma resumida de la exprecion lambda, es una referencia a ese metodo especifico (imprimirLibroRegistrado) que puede ser ejecutado mas tarde
        //es una accion que cada vez que lo necesite se ejecutará
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.listaDeAutoresRegistrados();
        imprimirTituloDeAutorRegistrado();
        autores.forEach(this::imprimirAutoresRegistrados);
    }

    private void mostrarAutoresVivos() {
        String anioAutor;

        while (true) {
            try {
                anioAutor = validarEntradaAnio();
                break;
            } catch (EntradaInvalidaException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Autor> autoresAnioVivo = autorRepositorio.listaAutorAnioVivo(anioAutor);

        if (autoresAnioVivo.isEmpty()) {
            System.out.println("No se han encontrado autores vivos en ese año.");

        }

        imprimirTituloDeAutorVivo();
        autoresAnioVivo.forEach(this::imprimirAutoresRegistrados);
    }

    private void mostrarLibroIdioma() {
        String idiomaLibro;
        while (true) {
            try {
                idiomaLibro = validarEntradaIdioma();
                break;
            } catch (EntradaInvalidaException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Libro> libroPorIdioma = libroRepositorio.buscarPorIdioma(idiomaLibro);
        if (libroPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");

        }
        imprimirTituloDeLibroPorIdioma();
        libroPorIdioma.forEach(this::imprimirLibroRegistrado);

    }

    private void mostrarTop10Libros() {
        List<Libro> top10Libros = libroRepositorio.findAll();

        if (top10Libros.isEmpty()) {
            System.out.println("Lo siento, no hay libros registrados.");

        }
        imprimirTituloDeTop10Libros();

        top10Libros.stream()
                .sorted(Comparator.comparingDouble(Libro::getCantidadDescargas).reversed())
                .limit(10)
                .forEach(this::imprimirLibroRegistrado);
    }

    private void mostrarEstadistica() {
        List<Libro> estadisticaCantidadDeDescarga = libroRepositorio.findAll();

        if (estadisticaCantidadDeDescarga.isEmpty()) {
            System.out.println("Lo siento, no hay libros registrados.");

        }

        DoubleSummaryStatistics est = estadisticaCantidadDeDescarga.stream()
                .mapToDouble(Libro::getCantidadDescargas)
                .summaryStatistics();
        imprimirEstadistica(est);

    }

    private void filtrarDatosYGuardar(String nombreLibro) {
        DatoLibro primerLibroEncontrado;
        Optional<DatoLibro> datos = dato.resultado().stream()
                .filter(l -> l.titulo().trim().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();


        if (datos.isPresent()) {
            primerLibroEncontrado = datos.get();
            System.out.println("\nLibro encontrado y guardado con éxito en tu lista de registros");

            // Crear el libro desde DatoLibro
            Libro libro = new Libro(primerLibroEncontrado);
            //System.out.println("Libro creado: " + libro);

            // Crear la lista de autores desde DatoAutor
            List<Autor> autores = primerLibroEncontrado.autores().stream()
                    .map(Autor::new)
                    .collect(Collectors.toList());

            // establecer autores en el libro
            libro.setAutores(autores);

            //System.out.println("Libro con autores agregados: " + libro);

            libroRepositorio.save(libro);
        } else {
            System.out.println("No se encontró ningún libro con el nombre proporcionado.");
        }
    }

    public String validarEntrada() {
        System.out.println("Por favor, ingresa el título del libro que deseas buscar.");
        String nombre = leer.nextLine().toLowerCase();

        if (nombre.trim().isBlank()) {
            throw new EntradaInvalidaException("Ingresaste un espacio en blanco o presionaste enter. Inténtalo nuevamente.");
        }
        return nombre;

    }

    public String validarEntradaAnio() {

        System.out.println("\nPor favor, ingrese el año del autor en vida (solo numeros).");
        String anio = leer.nextLine();

        if (anio.trim().isBlank()) {
            throw new EntradaInvalidaException("Ingresaste un espacio en blanco o presionaste enter. Inténtalo nuevamente");
        }
        if (!anio.matches("\\d{1,4}")) {
            throw new EntradaInvalidaException("Entreda no válida. Inténtalo nuevamente");
        }

        return anio;
    }

    public String validarEntradaIdioma() {

        imprimirListaIdioma();
        String idioma = leer.nextLine().toLowerCase();

        if (idioma.trim().isBlank()) {
            throw new EntradaInvalidaException("Ingresaste un espacio en blanco o presionaste enter. Inténtalo nuevamente");
        }
        if (!idioma.matches("^(es|en|fr|pt)$")) {
            throw new EntradaInvalidaException("Idioma no válida. Por favor, ingresa uno de los opciones mostradas");
        }

        return idioma;
    }

    public void imprimirListaIdioma() {
        System.out.print("""
                
                Ingrese el idioma en su forma abreviada para buscar el libro:
                es -> Español
                en -> Inglés
                fr -> Francés
                pt -> Portugués
                
                """);
        System.out.print("->");
    }

    public void imprimirLibroRegistrado(Libro libro) {

        String autores = libro.getAutores().stream()
                .map(Autor::getNombreAutor)
                .collect(Collectors.joining(","));
        String idiomas = String.join(",", libro.getIdiomas());

        System.out.printf(""" 
                        ---------- LIBRO ----------
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        Numero de descargas: %.2f
                        ----------------------------
                        """,
                libro.getTitulo(),
                autores,
                idiomas,
                libro.getCantidadDescargas());

        // collect(Collectors.joining(", ")) = combierte una lista en una sola cadena de texto separada por delimitador que en este caso
        // seria la coma y un espacio
        // String.join = los combierte en cadena y lo separa con una copa y espacio se utiliza mas que nada en listas y arrays
        // %.2f = se usa para redondear y mostrar un número flotante con 2 decimales exactos.
    }

    public void imprimirAutoresRegistrados(Autor autor) {
        String libros = autor.getLibros().stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(","));
        if (libros.isEmpty()) {
            System.out.println("Sin libros registrados");

        }
        System.out.printf(""" 
                        ----------------------------
                        Autor: %s
                        Año de nacimiento: %d
                        Año de muerte: %d
                        Libros: %s
                        ----------------------------
                        """,
                autor.getNombreAutor(),
                autor.getAnioNacimiento(),
                autor.getAnioMuerte(),
                libros);
    }

    public void imprimirEstadistica(DoubleSummaryStatistics est) {
        System.out.printf("""
                        ----------------ESTADÍSTICAS----------------
                        Promedio de descargas: %.2f
                        Máximo de descargas: %.2f
                        Mínimo de descargas: %.2f
                        Total de descargas: %.2f
                        Total de libros registrados: %d
                        """,
                est.getAverage(),
                est.getMax(),
                est.getMin(),
                est.getSum(),
                est.getCount());
    }

    public void imprimirTituloDeLibroRegistrado() {
        System.out.print("""
                
                     LIBROS REGISTRADOS
                ----------------------------
                
                """);
    }

    public void imprimirTituloDeAutorRegistrado() {
        System.out.print("""
                
                     AUTORES REGISTRADOS
                ----------------------------
                
                """);
    }

    public void imprimirTituloDeLibroPorIdioma() {
        System.out.print("""
                
                      LIBRO POR IDIOMA
                ----------------------------
                
                """);
    }

    public void imprimirTituloDeTop10Libros() {
        System.out.print("""
                
                      TOP 10 LIBROS MÁS DESCARGADOS
                -----------------------------------------
                
                """);
    }

    public void imprimirTituloDeAutorVivo() {
        System.out.print("""
                
                        PERÍODO DEL AUTOR EN VIDA
                -----------------------------------------
                
                """);
    }


    public void imprimirMenu() {
        String menu = """
                Elija una opción valida:
                1. Buscar libro por título
                2. Lista de libros registrados
                3. Lista de autores registrados
                4. Lista de autores vivos en un determinado año
                5. Lista de libros por idioma
                6. Top 10 libros más descargados
                7. Mostrar estadísticas de descargas
                
                0. SALIR
                
                """;
        System.out.print("\n" + menu + "->");
    }

    public void imprimirTituloMenu() {
        String titulo = """
                ****************************************************
                  *             BIBLIOTECA LITERALURA            *
                ****************************************************
                """;
        System.out.println(titulo);
    }
}
