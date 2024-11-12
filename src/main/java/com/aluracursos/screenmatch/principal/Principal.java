package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodios;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporada;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2225374d";


    public void mostrarMenu(){

        System.out.println("Escriba el nombre de la serie que desea buscar");
        var nombreSerie = sc.nextLine();
        nombreSerie = nombreSerie.replace(" ", "+");
        var json  = consumoAPI.obtenerDatos(URL_BASE+ nombreSerie + API_KEY);
        var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);

        //Busca los datos de todas las temporadas

        List<DatosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json  = consumoAPI.obtenerDatos(URL_BASE + nombreSerie + "&Season=" + i + API_KEY);
            var datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }

        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas

//        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
//            List<DatosEpisodios> episodiosTemporadas = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporadas.size(); j++) {
//                System.out.println(episodiosTemporadas.get(j).titulo());
//            }
//        }

        //Funciones lambda
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las informaciones a una lista del tipo DatosEpisodio


        List<DatosEpisodios> datosEpisodios  = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


//        System.out.println("TOP 5 episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DatosEpisodios::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo filtro (M>m) " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer Mayusculas (m>M) " + e))
//                .limit(5)
//                .forEach(System.out::println);

        //Convirtiendo los datos a una lista de tipo Episodio

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de episodios a partir de x año

//        System.out.println("Indica el año a partir del cual deseas ver los episodios");
//        var fecha = sc.nextInt();
//        sc.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha,  1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//        episodios.stream().filter(e -> e.getFechaLanzamiento() !=  null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println("Temporada "  + e.getTemporada()
//                        + " Episodio "  + e.getTemporada()
//                        + " Fecha de lanzamiento " + e.getFechaLanzamiento().format(dtf)
//                ));

        //Busca episodios por pedazo de titulo

        System.out.println("Ingrese el titulo del episodio que desea ver");

        var pedazoTitulo = sc.nextLine();

        Optional<Episodio> resultado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();


        if (resultado.isPresent()){
            System.out.println("Epidosdio encontrado");
            System.out.println("Los datos son: " + resultado.get());
        }else {
            System.out.println("Episodio no encontrado");
        }

    }
}
