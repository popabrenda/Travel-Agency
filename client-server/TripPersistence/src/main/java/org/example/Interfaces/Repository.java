package org.example.Interfaces;


import org.example.Entitate;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, E extends Entitate<ID>> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> delete(ID idEntity);
    Optional<E> findOne(ID idEntity);
    List<E> findAll();
}
