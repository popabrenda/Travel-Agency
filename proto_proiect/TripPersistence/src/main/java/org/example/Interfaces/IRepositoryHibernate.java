package org.example.Interfaces;

import org.example.EntitateHibernate;

import java.util.List;
import java.util.Optional;

public interface IRepositoryHibernate<E extends EntitateHibernate> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> delete(Integer idEntity);
    Optional<E> findOne(Integer idEntity);
    List<E> findAll();
}