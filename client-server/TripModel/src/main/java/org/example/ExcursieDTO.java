package org.example;

import java.io.Serializable;
import java.time.LocalTime;

public class ExcursieDTO  implements Serializable {
    private Integer id;
    private String obiectivTuristic;
    private String firmaTransport;
    private LocalTime oraPlecarii;
    private Double pret;
    private Integer numarLocuriDisponibile;

    public ExcursieDTO(Integer id, String obiectivTuristic, String firmaTransport, LocalTime oraPlecarii, Double pret, Integer numarLocuriDisponibile) {
        this.id = id;
        this.obiectivTuristic = obiectivTuristic;
        this.firmaTransport = firmaTransport;
        this.oraPlecarii = oraPlecarii;
        this.pret = pret;
        this.numarLocuriDisponibile = numarLocuriDisponibile;
    }

    public String getObiectivTuristic() {
        return obiectivTuristic;
    }

    public void setObiectivTuristic(String obiectivTuristic) {
        this.obiectivTuristic = obiectivTuristic;
    }

    public String getFirmaTransport() {
        return firmaTransport;
    }

    public void setFirmaTransport(String firmaTransport) {
        this.firmaTransport = firmaTransport;
    }

    public LocalTime getOraPlecarii() {
        return oraPlecarii;
    }

    public void setOraPlecarii(LocalTime oraPlecarii) {
        this.oraPlecarii = oraPlecarii;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public Integer getNumarLocuriDisponibile() {
        return numarLocuriDisponibile;
    }

    public void setNumarLocuriDisponibile(Integer numarLocuriDisponibile) {
        this.numarLocuriDisponibile = numarLocuriDisponibile;
    }

    public Integer getId() {
        return id;
    }
}
