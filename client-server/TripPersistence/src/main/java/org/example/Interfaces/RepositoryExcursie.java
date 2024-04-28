package org.example.Interfaces;


import org.example.Excursie;

import java.time.LocalTime;
import java.util.List;

public interface RepositoryExcursie extends Repository<Integer, Excursie> {
    List<Excursie> findByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa);
}
