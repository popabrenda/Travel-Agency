package org.example.Interfaces;



import org.example.Utilizator;

import java.util.Optional;

public interface RepositoryUtilizator extends Repository<Integer, Utilizator> {
    Optional<Utilizator> findUtilizatorByUsername(String username);
}