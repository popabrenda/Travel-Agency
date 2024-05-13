package org.example;

import java.io.Serializable;

public class Rezervare extends Entitate<Integer>  implements Serializable {
    private String numeClient;
    private String telefonClient;
    private Excursie excursie;
    private Integer numarBilete;

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getTelefonClient() {
        return telefonClient;
    }

    public void setTelefonClient(String telefonClient) {
        this.telefonClient = telefonClient;
    }

    public Excursie getExcursie() {
        return excursie;
    }

    public void setExcursie(Excursie excursie) {
        this.excursie = excursie;
    }
    public Integer getNumarBilete() {
        return numarBilete;
    }

    public void setNumarBilete(Integer numarBilete) {
        this.numarBilete = numarBilete;
    }

    public Rezervare(String numeClient, String telefonClient, Excursie excursie, Integer numarBilete) {
        this.numeClient = numeClient;
        this.telefonClient = telefonClient;
        this.excursie = excursie;
        this.numarBilete = numarBilete;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "numeClient='" + numeClient + '\'' +
                ", telefonClient='" + telefonClient + '\'' +
                ", excursie=" + excursie +
                ", numarBilete=" + numarBilete +
                '}';
    }
}
