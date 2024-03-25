package ro.mpp2024.Domain;

public class Entity implements IEntity<Integer> {

    protected int id;
    public Entity(int id)
    {
        this.id = id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
