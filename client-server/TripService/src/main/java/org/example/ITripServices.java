package org.example;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ITripServices {

    //interfata pentru metodele din server care vor fi apelate de client

    Optional<Utilizator> loginUser(String username, String password, ITripObserver client) throws AppException;
    void logOut(String username) throws AppException;
    List<ExcursieDTO> getAllExcursii() throws AppException;
    List<ExcursieDTO> getExcursiiByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa) throws AppException;
    void rezervaBilete(Integer excursieID, String numeClient, String telefonClient, Integer numarBilete) throws AppException;

}
