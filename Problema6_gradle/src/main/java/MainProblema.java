import ro.mpp2024.Domain.Client;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Domain.Rezervare;
import ro.mpp2024.Domain.Utilizator;
import ro.mpp2024.Repository.ClientDBRepository;
import ro.mpp2024.Repository.ExcursieDBRepository;
import ro.mpp2024.Repository.RezervareDBRepository;
import ro.mpp2024.Repository.UtilizatorDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class MainProblema {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        ClientDBRepository clientDBRepository = new ClientDBRepository(props);
        ExcursieDBRepository excursieDBRepository = new ExcursieDBRepository(props);
        RezervareDBRepository rezervareDBRepository = new RezervareDBRepository(props);

        Client c1 = clientDBRepository.findOne(32);
        Excursie e1 = excursieDBRepository.findOne(22);
        Rezervare r1 = new Rezervare(c1, e1, 2);
        //rezervareDBRepository.add(r1);

        Client c2 = new Client( "a", "1");
        clientDBRepository.add(c2);
        Excursie e2 = new Excursie( "a", "Buctur", 10, 1900, 19);
        excursieDBRepository.add(e2);
        Rezervare r2 = new Rezervare( c2, e2, 3);
        //rezervareDBRepository.add(r2);


        Iterable<Client> clienti = clientDBRepository.findAll();
        for(Client c: clienti)
            System.out.println(c);
        Iterable<Excursie>excursii = excursieDBRepository.findAll();
        for (Excursie e: excursii)
            System.out.println(e);
        Iterable<Rezervare> rezervari = rezervareDBRepository.findAll();
        for(Rezervare r: rezervari)
            System.out.println(r);


    }




}
