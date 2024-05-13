package org.example;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass
public class EntitateHibernate implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    private Integer id;

    public EntitateHibernate() {}
    public EntitateHibernate(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}