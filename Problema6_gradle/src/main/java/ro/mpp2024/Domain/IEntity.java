package ro.mpp2024.Domain;

public interface IEntity<ID>{
    void setId(ID id);
    ID getId();
}
