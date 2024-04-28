package org.example.DTO;

import org.example.Excursie;

import java.io.Serializable;

public class RezervareDTO  implements Serializable {
    private String numeClient;
    private String telefonClient;
    private Integer excursie;
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

    public Integer getExcursie() {
        return excursie;
    }

    public void setExcursie(Integer excursie) {
        this.excursie = excursie;
    }
    public Integer getNumarBilete() {
        return numarBilete;
    }

    public void setNumarBilete(Integer numarBilete) {
        this.numarBilete = numarBilete;
    }

    public RezervareDTO(String numeClient, String telefonClient, Integer excursie, Integer numarBilete) {
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
