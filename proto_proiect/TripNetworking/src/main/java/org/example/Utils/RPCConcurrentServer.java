package org.example.Utils;

import org.example.ITripServices;
import org.example.rpcprotocol.TripClientRPCWorker;

import java.net.Socket;

public class RPCConcurrentServer extends AbstractConcurrentServer {

    private ITripServices tripServices;
    public RPCConcurrentServer(int port, ITripServices tripServices) {
        super(port);
        this.tripServices = tripServices;
        System.out.println("RPCConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {

        System.out.println("Creating worker...");
        TripClientRPCWorker worker = new TripClientRPCWorker(tripServices, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
