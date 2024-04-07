package ro.mpp2024.Service;

import ro.mpp2024.Domain.Client;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Domain.Rezervare;
import ro.mpp2024.Domain.Utilizator;
import ro.mpp2024.Repository.Interface.*;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private IClientRepository clientRepository;
    private IExcursieRepository excursieRepository;
    private IRezervareRepository rezervareRepository;
    private IUtilizatorRepository utilizatorRepository;

    public Service(IClientRepository clientRepository,  IExcursieRepository excursieRepository, IRezervareRepository rezervareRepository,IUtilizatorRepository utilizatorRepository)
    {
        this.clientRepository=clientRepository;
        this.excursieRepository=excursieRepository;
        this.rezervareRepository=rezervareRepository;
        this.utilizatorRepository=utilizatorRepository;
    }

    public boolean validateUsername(String username, String password) {
        Iterable<Utilizator> utilizatori = utilizatorRepository.findAll();
        for(Utilizator utilizator : utilizatori) {
            if(utilizator.getUsername().equals(username) && utilizator.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Iterable<Excursie> filterExcursii(String obiectiv, int oraPlecare1, int oraPlecare2) {
        Iterable<Excursie> excursii = excursieRepository.findAll();
        List<Excursie> filteredExcursii = new ArrayList<>();
        if(oraPlecare1> oraPlecare2) {
            int aux = oraPlecare1;
            oraPlecare1 = oraPlecare2;
            oraPlecare2 = aux;
        }
        for(Excursie excursie : excursii) {
            if(excursie.getObiectiv().equals(obiectiv) && excursie.getOraPlecare() >= oraPlecare1 && excursie.getOraPlecare() <= oraPlecare2) {
                filteredExcursii.add(excursie);
            }
        }
        return filteredExcursii;
    }
    public void rezervare(Client client, Excursie excursie, int nrLocuri) throws RuntimeException {

        if (excursie.getLocuriDisponibile() < nrLocuri)
            throw new RuntimeException("Nu sunt suficiente locuri");
        else {
            //excursie.setLocuriDisponibile(excursie.getLocuriDisponibile() - nrLocuri);
            rezervareRepository.add(new Rezervare(client, excursie, nrLocuri));
        }

    }


}
