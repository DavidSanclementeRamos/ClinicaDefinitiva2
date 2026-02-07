package com.example.ClinicaDefinitiva.util;

import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

public class Paginacion {

    // Método auxiliar para Sort
    private Sort.Order[] parseSort(String[] sort) {
        return Stream.of(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .toArray(Sort.Order[]::new);
    }

}

