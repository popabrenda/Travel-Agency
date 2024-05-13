package org.example.protobuffprotocol;

import org.example.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.example.DTO.FilterDTO;
import org.example.DTO.RezervareDTO;
import org.example.rpcprotocol.Request;
import org.example.rpcprotocol.RequestType;

public class TripServicesRPCProxyProto implements ITripServices {
    private String host;
    private int port;

    private ITripObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private volatile boolean finished;
    private BlockingQueue<TripProtocol.Response> queueResponses;

    public TripServicesRPCProxyProto(String host, int port) {
        this.host = host;
        this.port = port;
        queueResponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Optional<Utilizator> loginUser(String username, String password, ITripObserver client) throws AppException {
        System.out.println("Login request proto...");
        initializeConnection();
        Utilizator utilizator = new Utilizator("", username, password);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(utilizator).build();
        sendRequest(ProtoUtils.createLoginRequest(utilizator));
        TripProtocol.Response response = readResponse();
        if (response.getType() == TripProtocol.Response.ReponseType.OK) {
            this.client = client;
            return Optional.of(ProtoUtils.getUtilizator(response));
        }
        if (response.getType() == TripProtocol.Response.ReponseType.ERROR) {
            String error = response.getError();
            closeConnection();
            throw new AppException(error);
        }
        return Optional.empty();
    }

    @Override
    public void logOut(String username) throws AppException {
        sendRequest(ProtoUtils.createLogoutRequest(username));
        var response = readResponse();
        if (response.getType() == TripProtocol.Response.ReponseType.ERROR) {
            String error = response.getError();
            throw new AppException(error);
        }
        closeConnection();
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(TripProtocol.Request request) throws AppException {
        try {
//            output.writeObject(request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (Exception e) {
            throw new AppException("Error sending object " + e);
        }
    }

    private TripProtocol.Response readResponse() {
        TripProtocol.Response response = null;
        try {
            response = queueResponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws AppException {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
//            output.flush();
            input = connection.getInputStream();
            finished = false;
            startReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    @Override
    public List<ExcursieDTO> getAllExcursii() throws AppException {
        sendRequest(ProtoUtils.createGetAllExcursiiRequest());
        var response = readResponse();
        if (response.getType() == TripProtocol.Response.ReponseType.ERROR) {
            String error = response.getError();
            throw new AppException(error);
        }
        return ProtoUtils.getExcursii(response);
    }

    @Override
    public List<ExcursieDTO> getExcursiiByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa) throws AppException {
//        Request request = new Request.Builder().type(RequestType.GET_TRIPS_FILTERED).data(new FilterDTO(obiectiv, deLa, panaLa)).build();
        sendRequest(ProtoUtils.createGetTripsFilteredRequest(new FilterDTO(obiectiv, deLa, panaLa)));
        var response = readResponse();
        if (response.getType() == TripProtocol.Response.ReponseType.ERROR) {
            String error = response.getError();
            throw new AppException(error);
        }
        return ProtoUtils.getExcursii(response);
    }

    @Override
    public void rezervaBilete(Integer excursieID, String numeClient, String telefonClient, Integer numarBilete) throws AppException {
        var rezervare = new RezervareDTO(numeClient, telefonClient, excursieID, numarBilete);
//        Request request = new Request.Builder().type(RequestType.RESERVATION).data(rezervare).build();
        sendRequest(ProtoUtils.createRezervaBileteRequest(rezervare));
        var response = readResponse();
        if (response.getType() == TripProtocol.Response.ReponseType.ERROR) {
            String error = response.getError();
            throw new AppException(error);
        }
    }

    private void handleUpdate(TripProtocol.Response response) {
        if (response.getType() == TripProtocol.Response.ReponseType.UPDATE) {
            client.reservationUpdate();
        }
    }
    private boolean isUpdateResponse(TripProtocol.Response response) {
        return response.getType() == TripProtocol.Response.ReponseType.UPDATE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
//                    Object response = input.readObject();
                    TripProtocol.Response response = TripProtocol.Response.parseDelimitedFrom(input);
                    System.out.println("Response received " + response);

                    if(isUpdateResponse(response)){
                        handleUpdate(response);
                    } else {
                        try {
                            queueResponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    if (response != null) {
//                        Response response1 = (Response) response;
//                        if (isUpdateResponse(response1)) {
//                            handleUpdate(response1);
//                        } else {
//                            try {
//                                queueResponses.put();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                } catch (Exception e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}