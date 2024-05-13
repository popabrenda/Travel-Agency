package org.example.protobuffprotocol;


import org.example.DTO.FilterDTO;
import org.example.DTO.RezervareDTO;
import org.example.ExcursieDTO;
import org.example.Utilizator;

import java.time.LocalTime;
import java.util.List;

public class ProtoUtils {
    public static org.example.protobuffprotocol.TripProtocol.Request createLoginRequest(Utilizator utilizator1) {
        org.example.protobuffprotocol.TripProtocol.Utilizator utilizator = org.example.protobuffprotocol.TripProtocol.Utilizator.newBuilder().setUsername(utilizator1.getUsername()).setPassword(utilizator1.getPassword()).build();
        org.example.protobuffprotocol.TripProtocol.Request request = org.example.protobuffprotocol.TripProtocol.Request.newBuilder().setType(TripProtocol.Request.RequestType.LOGIN).setUtilizator(utilizator).build();
        return request;
    }

    public static TripProtocol.Request createLogoutRequest(String username) {
        TripProtocol.Request request = TripProtocol.Request.newBuilder().setType(TripProtocol.Request.RequestType.LOGOUT).setUsername(username).build();
        return request;
    }

    public static TripProtocol.Request createGetAllExcursiiRequest() {
        TripProtocol.Request request = TripProtocol.Request.newBuilder().setType(TripProtocol.Request.RequestType.GET_TRIPS).build();
        return request;
    }

    public static TripProtocol.Request createGetTripsFilteredRequest(FilterDTO filterDTO){
        TripProtocol.FilterDTO filter = TripProtocol.FilterDTO.newBuilder().setObiectivTuristic(filterDTO.getObiectivTuristic()).setDeLa(filterDTO.getDeLa().toString()).setPanaLa(filterDTO.getPanaLa().toString()).build();
        TripProtocol.Request request = TripProtocol.Request.newBuilder().setType(TripProtocol.Request.RequestType.GET_TRIPS_FILTERED).setFilter(filter).build();
        return request;
    }

    public static TripProtocol.Request createRezervaBileteRequest(RezervareDTO rezervareDTO){
        TripProtocol.RezervareDTO rezervare = TripProtocol.RezervareDTO.newBuilder().setNumeClient(rezervareDTO.getNumeClient()).setTelefonClient(rezervareDTO.getTelefonClient()).setNumarLocuri(rezervareDTO.getNumarBilete()).setUsername(rezervareDTO.getExcursie()).build();
        TripProtocol.Request request = TripProtocol.Request.newBuilder().setType(TripProtocol.Request.RequestType.RESERVATION).setRezervare(rezervare).build();
        return request;
    }

    public static TripProtocol.Response createOkResponse(){
        TripProtocol.Response response = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.OK).build();
        return response;
    }

    public static TripProtocol.Response createOkResponse(Utilizator utilizator1){
        TripProtocol.Utilizator utilizator = TripProtocol.Utilizator.newBuilder().setUsername(utilizator1.getUsername()).setPassword(utilizator1.getPassword()).build();
        TripProtocol.Response response = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.OK).setUtilizator(utilizator).build();
        return response;
    }

    public static TripProtocol.Response createErrorResponse(String text){
        TripProtocol.Response response = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.ERROR).setError(text).build();
        return response;
    }


    public static TripProtocol.Response createUpdateResponse(){
        TripProtocol.Response response = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.UPDATE).build();
        return response;
    }

    public static TripProtocol.Response createGetTripsResponse(List<ExcursieDTO> excursii){
        TripProtocol.Response.Builder responseBuilder = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.GET_TRIPS);
        for(var excursie : excursii){
            TripProtocol.ExcursieDTO excursieProto = TripProtocol.ExcursieDTO.newBuilder().setId(excursie.getId()).setObiectivTuristic(excursie.getObiectivTuristic()).setFirmaTransport(excursie.getFirmaTransport()).setOraPlecarii(excursie.getOraPlecarii().toString()).setPret(excursie.getPret()).setNumarLocuriDisponibile(excursie.getNumarLocuriDisponibile()).build();
            responseBuilder.addExcursii(excursieProto);
        }
        return responseBuilder.build();
    }

    public static TripProtocol.Response createGetTripsFilteredResponse(List<ExcursieDTO> excursii){
        TripProtocol.Response.Builder responseBuilder = TripProtocol.Response.newBuilder().setType(TripProtocol.Response.ReponseType.GET_TRIPS_FILTERED);
        for(var excursie : excursii){
            TripProtocol.ExcursieDTO excursieProto = TripProtocol.ExcursieDTO.newBuilder().setId(excursie.getId()).setObiectivTuristic(excursie.getObiectivTuristic()).setFirmaTransport(excursie.getFirmaTransport()).setOraPlecarii(excursie.getOraPlecarii().toString()).setPret(excursie.getPret()).setNumarLocuriDisponibile(excursie.getNumarLocuriDisponibile()).build();
            responseBuilder.addExcursii(excursieProto);
        }
        return responseBuilder.build();
    }

    public static Utilizator getUtilizator(TripProtocol.Response response){
        var utilizator = response.getUtilizator();
        return new Utilizator("", utilizator.getUsername(), utilizator.getPassword());
    }

    public static Utilizator getUtilizator(TripProtocol.Request request){
        var utilizator = request.getUtilizator();
        return new Utilizator("", utilizator.getUsername(), utilizator.getPassword());
    }


    public static RezervareDTO getRezervareDTO(TripProtocol.Request request){
        var rezervare = request.getRezervare();
        return new RezervareDTO(rezervare.getNumeClient(), rezervare.getTelefonClient(), rezervare.getUsername(), rezervare.getNumarLocuri());
    }

    public static List<ExcursieDTO> getExcursii(TripProtocol.Response response){
        var excursii = response.getExcursiiList();
        return excursii.stream().map(excursie -> new ExcursieDTO(excursie.getId(), excursie.getObiectivTuristic(), excursie.getFirmaTransport(), LocalTime.parse(excursie.getOraPlecarii()), excursie.getPret(), excursie.getNumarLocuriDisponibile())).toList();
    }

    public static FilterDTO getFilterDTO(TripProtocol.Request request){
        var filter = request.getFilter();
        return new FilterDTO(filter.getObiectivTuristic(), LocalTime.parse(filter.getDeLa()), LocalTime.parse(filter.getPanaLa()));
    }


}