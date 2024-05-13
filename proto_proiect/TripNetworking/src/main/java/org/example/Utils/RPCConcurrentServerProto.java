package org.example.Utils;

import org.example.ITripServices;
import org.example.protobuffprotocol.TripClientRPCWorkerProto;

import java.net.Socket;

public class RPCConcurrentServerProto extends AbstractConcurrentServer {

    private ITripServices tripServices;
    public RPCConcurrentServerProto(int port, ITripServices tripServices) {
        super(port);
        this.tripServices = tripServices;
        System.out.println("RPCConcurrentServerProto");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TripClientRPCWorkerProto workerProto = new TripClientRPCWorkerProto(tripServices, client);
//        TripClientRPCWorker worker = new TripClientRPCWorker(tripServices, client);
        Thread tw = new Thread(workerProto);
        System.out.println("Creating worker proto...");
        return tw;
    }
}