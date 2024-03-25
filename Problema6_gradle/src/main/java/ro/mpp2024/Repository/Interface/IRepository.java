package ro.mpp2024.Repository.Interface;


public interface IRepository<ID, T>
{
    void add(T elem);
    void update(ID id, T elem);
    void delete(ID id);
    T findOne(ID id);
    Iterable<T> findAll();
    int size();




}
