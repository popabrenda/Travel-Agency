package org.example.protobuffprotocol;

import org.example.*;
import org.example.DTO.RezervareDTO;

import java.io.*;
import java.net.Socket;

public class TripClientRPCWorkerProto implements Runnable, ITripObserver {
    private ITripServices server;
    private Socket connection;
    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public TripClientRPCWorkerProto(ITripServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = connection.getOutputStream();
//            output.flush();
            input = connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                var request = TripProtocol.Request.parseDelimitedFrom(input);
                var response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
//                connected = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (
                IOException e) {
            System.out.println("Error " + e);
        }
    }

//    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private TripProtocol.Response handleRequest(TripProtocol.Request request) {
        TripProtocol.Response response = null;
        if (request.getType() == TripProtocol.Request.RequestType.LOGIN) {
            System.out.println("Login request ...");
            Utilizator userDTO = ProtoUtils.getUtilizator(request);
            try {
                var optional = server.loginUser(userDTO.getUsername(), userDTO.getPassword(), this);
                if(optional.isPresent()) {
                    return ProtoUtils.createOkResponse(optional.get());
                }
                else{
                    connected = false;
                    return ProtoUtils.createErrorResponse("Invalid username or password");
                }
            } catch (AppException e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == TripProtocol.Request.RequestType.LOGOUT) {
            System.out.println("Logout request ...");
            String username = request.getUsername();
            try {
                server.logOut(username);
                connected = false;
                return ProtoUtils.createOkResponse();

            } catch (AppException e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == TripProtocol.Request.RequestType.REGISTER) {
            System.out.println("Register request ...");
            return null;
//            Utilizator userDTO = (Utilizator) request.getData();
//            try {
//                server.(userDTO.getUsername(), userDTO.getPassword());
//                return new Response.Builder().type(ResponseType.OK).build();
//            } catch (AppException e) {
//                connected = false;
//                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
//            }
        }
        if (request.getType() == TripProtocol.Request.RequestType.GET_TRIPS) {
            System.out.println("GetTrips request ...");
            try {
                return ProtoUtils.createGetTripsResponse(server.getAllExcursii());
//                return new Response.Builder().type(ResponseType.GET_TRIPS).data(server.getAllExcursii()).build();
            } catch (AppException e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == TripProtocol.Request.RequestType.GET_TRIPS_FILTERED) {
            System.out.println("GetTripsFiltered request ...");
            var filter = ProtoUtils.getFilterDTO(request);
            try{
                return ProtoUtils.createGetTripsResponse(server.getExcursiiByFilter(filter.getObiectivTuristic(), filter.getDeLa(), filter.getPanaLa()));
            } catch (AppException e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == TripProtocol.Request.RequestType.RESERVATION) {
            System.out.println("Reservation request ...");
            RezervareDTO rezervare = ProtoUtils.getRezervareDTO(request);
            try {
                server.rezervaBilete(rezervare.getExcursie(), rezervare.getNumeClient(), rezervare.getTelefonClient(), rezervare.getNumarBilete());
                return ProtoUtils.createOkResponse();
            } catch (AppException e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(TripProtocol.Response response) {
        try {
            response.writeDelimitedTo(output);
//            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void reservationUpdate() {
        System.out.println("o intrat aici");

        TripProtocol.Response response = ProtoUtils.createUpdateResponse();
//        Response response = new Response.Builder().type(ResponseType.UPDATE).data(null).build();
        sendResponse(response);
    }
}