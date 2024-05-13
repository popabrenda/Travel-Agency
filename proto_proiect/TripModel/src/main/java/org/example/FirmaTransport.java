package org.example;

import java.io.Serializable;

public class FirmaTransport extends Entitate<Integer>  implements Serializable {
    private String nume;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public FirmaTransport(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "FirmaTransport{" +
                "nume='" + nume + '\'' +
                '}';
    }
}
