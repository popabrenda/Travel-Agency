package org.example;

import org.example.Interfaces.RepositoryExcursie;
import org.example.Interfaces.RepositoryFirmaTransport;
import org.example.Interfaces.RepositoryRezervare;
import org.example.Interfaces.RepositoryUtilizator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripServices implements ITripServices{


    private RepositoryUtilizator repositoryUtilizator;
    private RepositoryExcursie repositoryExcursie;
    private RepositoryFirmaTransport repositoryFirmaTransport;
    private RepositoryRezervare repositoryRezervare;
    private Map<String, ITripObserver> loggedClients;
    private final int defaultThreadsNo=5;

    public TripServices(RepositoryUtilizator repositoryUtilizator, RepositoryExcursie repositoryExcursie, RepositoryFirmaTransport repositoryFirmaTransport, RepositoryRezervare repositoryRezervare){
        this.repositoryUtilizator = repositoryUtilizator;
        this.repositoryExcursie = repositoryExcursie;
        this.repositoryFirmaTransport = repositoryFirmaTransport;
        this.repositoryRezervare = repositoryRezervare;
        loggedClients= new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Optional<Utilizator> loginUser(String username, String password, ITripObserver client) throws AppException {
        System.out.println("TripServices/LoginUser " + username + password);
        var utilizatorOpt = repositoryUtilizator.findUtilizatorByUsername(username);
        System.out.println("TripServices/LoginUser " + utilizatorOpt);
        if (utilizatorOpt.isPresent()) {
            var utilizator = utilizatorOpt.get();
            var parolaDB = utilizator.getPassword();
            //var parola_Decriptata = decrypt(parolaDB, System.getenv("SECRETKEY"));
            System.out.println("TripServices/LoginUser paroladb" + parolaDB);
            if(!utilizator.getPassword().equals(password)){
                throw new AppException("Parola incorecta");
            }
            else {
                if (loggedClients.get(username) != null)
                    throw new AppException("User already logged in");
                loggedClients.put(username, client);
                return Optional.of(utilizator);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized void logOut(String username) throws AppException{
        if (loggedClients.get(username) == null)
            throw new AppException("User not logged in");
        loggedClients.remove(username);
    }

    @Override
    public List<ExcursieDTO> getAllExcursii(){
        return repositoryExcursie.findAll().stream()
                .map(excursie -> new ExcursieDTO(
                        excursie.getId(),
                        excursie.getObiectivTuristic(),
                        excursie.getFirmaTransport().getNume(),
                        excursie.getOraPlecarii(),
                        excursie.getPret(),
                        excursie.getNumarLocuriTotale() - repositoryRezervare.getNumarOcupate(excursie.getId()))).toList();
    }


    @Override
    public List<ExcursieDTO> getExcursiiByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa) {
        return repositoryExcursie.findByFilter(obiectiv, deLa, panaLa).stream()
                .map(excursie -> new ExcursieDTO(
                        excursie.getId(),
                        excursie.getObiectivTuristic(),
                        excursie.getFirmaTransport().getNume(),
                        excursie.getOraPlecarii(),
                        excursie.getPret(),
                        excursie.getNumarLocuriTotale() - repositoryRezervare.getNumarOcupate(excursie.getId()))).toList();
    }

    @Override
    public synchronized void rezervaBilete(Integer excursieID, String numeClient, String telefonClient, Integer numarBilete) {
        var excursie = repositoryExcursie.findOne(excursieID).get();
        var locuriDisponibile = excursie.getNumarLocuriTotale() - repositoryRezervare.getNumarOcupate(excursieID);
        if(locuriDisponibile < numarBilete){
            throw new RuntimeException("Nu sunt suficiente locuri disponibile");
        }
        repositoryRezervare.save(new Rezervare(numeClient, telefonClient, excursie, numarBilete));
        notifyClients();
    }

    private void notifyClients(){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var client : loggedClients.values()){
            if(client == null)
                continue;
            executor.execute(client::reservationUpdate);
        }
    }




    //    private String encrypt(String strToEncrypt, String secret) throws AppException {
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
//        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
//            return Base64.getEncoder().encodeToString(encryptedBytes);
//        }
//        catch (Exception e){
//            throw new AppException("Invalid password");
//        }
//    }
//
//    private String decrypt(String strToDecrypt, String secret) throws AppException {
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
//        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
//            return new String(decryptedBytes);
//        }
//        catch (Exception e){
//            throw new AppException("Invalid password");
//        }
//    }
}
