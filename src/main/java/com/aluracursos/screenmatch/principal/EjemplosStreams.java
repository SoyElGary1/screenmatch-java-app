package com.aluracursos.screenmatch.principal;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

public class EjemplosStreams {

    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Gary", "Aldo", "Alonso", "Jorge");

        nombres.stream()
                .sorted()
                .filter(n -> n.startsWith("G"))
                .map(n -> n.toUpperCase())
                .limit(2)
                .forEach(System.out::println);
    }
}
