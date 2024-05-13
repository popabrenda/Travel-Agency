package org.example.DTO;

import java.io.Serializable;
import java.time.LocalTime;

public class FilterDTO  implements Serializable {
    private String obiectivTuristic;
    private LocalTime deLa;
    private LocalTime panaLa;

    public FilterDTO(String obiectivTuristic, LocalTime deLa, LocalTime panaLa) {
        this.obiectivTuristic = obiectivTuristic;
        this.deLa = deLa;
        this.panaLa = panaLa;
    }

    public String getObiectivTuristic() {
        return obiectivTuristic;
    }

    public void setObiectivTuristic(String obiectivTuristic) {
        this.obiectivTuristic = obiectivTuristic;
    }

    public LocalTime getDeLa() {
        return deLa;
    }

    public void setDeLa(LocalTime deLa) {
        this.deLa = deLa;
    }

    public LocalTime getPanaLa() {
        return panaLa;
    }

    public void setPanaLa(LocalTime panaLa) {
        this.panaLa = panaLa;
    }
}
