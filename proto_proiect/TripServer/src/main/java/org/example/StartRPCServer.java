package org.example;

import org.example.DBRepositoy.RepositoryDBExcursie;
import org.example.DBRepositoy.RepositoryDBFirmaTransport;
import org.example.DBRepositoy.RepositoryDBRezervare;
import org.example.DBRepositoy.RepositoryDBUtilizator;
import org.example.Interfaces.RepositoryExcursie;
import org.example.Interfaces.RepositoryFirmaTransport;
import org.example.Interfaces.RepositoryRezervare;
import org.example.Interfaces.RepositoryUtilizator;
import org.example.Utils.AbstractServer;
import org.example.Utils.RPCConcurrentServer;
import org.example.Utils.RPCConcurrentServerProto;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileReader;
import java.time.LocalTime;
import java.util.Properties;

public class StartRPCServer {
    private static int defaultPort = 55555;
    static SessionFactory sessionFactory;
    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.load(new FileReader("../bd.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find bd.properties " + e);
        }
        Properties propertiesServer = new Properties();
        try {
            propertiesServer.load(new FileReader("server.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find server.properties " + e);
        }

        RepositoryUtilizator utilizatorRepository = new RepositoryDBUtilizator(properties);
        //RepositoryUtilizator utilizatorRepository = new RepositoryHibernateUtilizator(sessionFactory);
        RepositoryFirmaTransport firmaTransportRepository = new RepositoryDBFirmaTransport(properties);
        RepositoryExcursie excursieRepository = new RepositoryDBExcursie(properties, firmaTransportRepository);
        RepositoryRezervare repositoryRezervare = new RepositoryDBRezervare(properties, excursieRepository);
        ITripServices tripServices = new TripServices(utilizatorRepository, excursieRepository, firmaTransportRepository, repositoryRezervare);

        String pass=propertiesServer.getProperty("Port");
        var intPass = Integer.parseInt(pass);
        System.out.println("Starting server on port " + intPass);

        //AbstractServer server = new RPCConcurrentServer(intPass, tripServices);
        AbstractServer server = new RPCConcurrentServerProto(intPass, tripServices);
        try{
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}