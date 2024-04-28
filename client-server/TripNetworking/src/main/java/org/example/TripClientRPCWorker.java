package org.example;

import org.example.DTO.FilterDTO;
import org.example.DTO.RezervareDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TripClientRPCWorker implements Runnable, ITripObserver {
    private ITripServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TripClientRPCWorker(ITripServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
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

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            System.out.println("Login request ...");
            Utilizator userDTO = (Utilizator) request.getData();
            try {
                //System.out.println("tripWorker////handleRequest//Login request ..." + userDTO.getUsername() + " " + userDTO.getPassword());
                var optional = server.loginUser(userDTO.getUsername(), userDTO.getPassword(), this);
                if(optional.isPresent()) {
                    return new Response.Builder().type(ResponseType.OK).data(optional.get()).build();
                }
                else{
                    connected = false;
                    return new Response.Builder().type(ResponseType.ERROR).data("Username or password invalid!").build();
                }
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.LOGOUT) {
            System.out.println("Logout request ...");
            String username = (String) request.getData();
            try {
                server.logOut(username);
                return okResponse;
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.REGISTER) {
            System.out.println("Register request ...");
            return null;

        }
        if (request.getType() == RequestType.GET_TRIPS) {
            System.out.println("GetTrips request ...");
            try {
                return new Response.Builder().type(ResponseType.GET_TRIPS).data(server.getAllExcursii()).build();
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GET_TRIPS_FILTERED) {
            System.out.println("GetTripsFiltered request ...");
            var filter = (FilterDTO) request.getData();
            try{
                return new Response.Builder().type(ResponseType.GET_TRIPS_FILTERED).data(server.getExcursiiByFilter(filter.getObiectivTuristic(), filter.getDeLa(), filter.getPanaLa())).build();
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.RESERVATION) {
            System.out.println("Reservation request ...");
            RezervareDTO rezervare = (RezervareDTO) request.getData();
            try {
                server.rezervaBilete(rezervare.getExcursie(), rezervare.getNumeClient(), rezervare.getTelefonClient(), rezervare.getNumarBilete());
                return okResponse;
            } catch (AppException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) {
        try {
            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void reservationUpdate() {
        System.out.println("a ajuns in trip client rpc worker: reservation update");
        Response response = new Response.Builder().type(ResponseType.UPDATE).data(null).build();
        sendResponse(response);
    }
}


