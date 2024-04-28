package org.example;

import java.io.Serializable;
import java.time.LocalTime;

public class Excursie extends Entitate<Integer>  implements Serializable {
    private String obiectivTuristic;
    private FirmaTransport firmaTransport;
    private LocalTime oraPlecarii;
    private Double pret;
    private Integer numarLocuriTotale;

    public Excursie(String obiectivTuristic, FirmaTransport firmaTransport, LocalTime oraPlecarii, Double pret, Integer numarLocuriTotale) {
        this.obiectivTuristic = obiectivTuristic;
        this.firmaTransport = firmaTransport;
        this.oraPlecarii = oraPlecarii;
        this.pret = pret;
        this.numarLocuriTotale = numarLocuriTotale;
    }

    public String getObiectivTuristic() {
        return obiectivTuristic;
    }

    public void setObiectivTuristic(String obiectivTuristic) {
        this.obiectivTuristic = obiectivTuristic;
    }

    public FirmaTransport getFirmaTransport() {
        return firmaTransport;
    }

    public void setFirmaTransport(FirmaTransport firmaTransport) {
        this.firmaTransport = firmaTransport;
    }
    public LocalTime getOraPlecarii() {
        return oraPlecarii;
    }

    public void setOraPlecarii(LocalTime oraPlecarii) {
        this.oraPlecarii = oraPlecarii;
    }

    public Integer getNumarLocuriTotale() {
        return numarLocuriTotale;
    }

    public void setNumarLocuriTotale(Integer numarLocuriTotale) {
        this.numarLocuriTotale = numarLocuriTotale;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Excursie{" +
                "obiectivTuristic='" + obiectivTuristic + '\'' +
                ", firmaTransport=" + firmaTransport +
                ", oraPlecarii=" + oraPlecarii +
                ", pret=" + pret +
                ", numarLocuriTotale=" + numarLocuriTotale +
                '}';
    }
}
