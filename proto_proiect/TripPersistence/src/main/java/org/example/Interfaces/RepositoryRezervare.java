package org.example.Interfaces;


import org.example.Rezervare;

public interface RepositoryRezervare extends Repository<Integer, Rezervare> {
    Integer getNumarOcupate(Integer idExcursie);
}
