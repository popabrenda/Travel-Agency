package org.example;

import org.example.DTO.FilterDTO;
import org.example.DTO.RezervareDTO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TripServicesRPCProxy implements ITripServices{
    private String host;
    private int port;

    private ITripObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private volatile boolean finished;
    private BlockingQueue<Response> queueResponses;

    public TripServicesRPCProxy(String host, int port) {
        this.host = host;
        this.port = port;
        queueResponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Optional<Utilizator> loginUser(String username, String password, ITripObserver client) throws AppException {
        initializeConnection();
        Utilizator utilizator = new Utilizator("", username, password);
        //System.out.println("tripServicesRPCProxy/LoginUser " + utilizator);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(utilizator).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            this.client = client;
            return Optional.of((Utilizator) response.getData());
        }
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            closeConnection();
            throw new AppException(error);
        }
        return Optional.empty();
    }

    @Override
    public void logOut(String username) throws AppException {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(username).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new AppException(error);
        }
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

    private void sendRequest(Request request) throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception e) {
            throw new AppException("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
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
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
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
        Request request = new Request.Builder().type(RequestType.GET_TRIPS).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new AppException(error);
        }
        return (List<ExcursieDTO>) response.getData();
    }

    @Override
    public List<ExcursieDTO> getExcursiiByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa) throws AppException {
        Request request = new Request.Builder().type(RequestType.GET_TRIPS_FILTERED).data(new FilterDTO(obiectiv, deLa, panaLa)).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new AppException(error);
        }
        return (List<ExcursieDTO>) response.getData();
    }

    @Override
    public void rezervaBilete(Integer excursieID, String numeClient, String telefonClient, Integer numarBilete) throws AppException {
        var rezervare = new RezervareDTO(numeClient, telefonClient, excursieID, numarBilete);
        Request request = new Request.Builder().type(RequestType.RESERVATION).data(rezervare).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new AppException(error);
        }
    }

    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.UPDATE) {
            client.reservationUpdate();
        }
    }
    private boolean isUpdateResponse(Response response) {
        return response.getType() == ResponseType.UPDATE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received " + response);
                    if (response instanceof Response) {
                        Response response1 = (Response) response;
                        if (isUpdateResponse(response1)) {
                            handleUpdate(response1);
                        } else {
                            try {
                                queueResponses.put((Response) response);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
