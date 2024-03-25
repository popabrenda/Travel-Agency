package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Client;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Domain.Rezervare;
import ro.mpp2024.Repository.Interface.IRezervareRepository;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RezervareDBRepository implements IRezervareRepository {
    private JdbcUtils dbUtils;
    Properties props=new Properties();

    public void initializare(){
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
    }
    ClientDBRepository clientDBRepository=new ClientDBRepository(props);
    ExcursieDBRepository excursieDBRepository=new ExcursieDBRepository(props);
    private static final Logger logger = LogManager.getLogger();

    public RezervareDBRepository(Properties dbUtils) {
        logger.info("Initializing RezervareDBRepository with properties: {}", dbUtils);
        this.dbUtils = new JdbcUtils(dbUtils);
        initializare();
    }

    @Override
    public void add(Rezervare entity) {
        logger.traceEntry("saving rezervare {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Rezervari1(id, idClient, idExcursie, nrBilete) values (?,?,?,?)")) {
            preStmt.setInt(1, entity.getId());
            preStmt.setInt(2, entity.getClient().getId());
            preStmt.setInt(3, entity.getExcursie().getId());
            preStmt.setInt(4, entity.getNrBilete());
            if (entity.getExcursie().getLocuriDisponibile() < entity.getNrBilete())
                throw new RuntimeException("Nu sunt suficiente locuri");

            int result1 = preStmt.executeUpdate();
            entity.getExcursie().setLocuriDisponibile(entity.getExcursie().getLocuriDisponibile() - entity.getNrBilete());

            try (var updateStmt = con.prepareStatement("update Excursii set locuriDisponibile = locuriDisponibile - ? where id = ?")) {
                updateStmt.setInt(1, entity.getNrBilete());
                updateStmt.setInt(2, (Integer) entity.getExcursie().getId());
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Trip not found");
                }
                int result = preStmt.executeUpdate();
                if (result > 0) {
                    logger.traceExit();
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
    }
    @Override
    public void update(Integer integer, Rezervare elem) {
        return;
    }

    @Override
    public void delete(Integer integer) {
        return;
    }

    @Override
    public Rezervare findOne(Integer integer) {
        List<Rezervare> rezervari = new ArrayList<>();
        try (PreparedStatement preStmt = dbUtils.getConnection().prepareStatement("select * from Rezervari1 where id = ?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int idClient = result.getInt("idClient");
                    int idExcursie = result.getInt("idExcursie");
                    int nrBilete = result.getInt("nrBilete");
                    Client client = clientDBRepository.findOne(idClient);
                    Excursie excursie = excursieDBRepository.findOne(idExcursie);
                    Rezervare rezervare = new Rezervare(id, client, excursie, nrBilete);
                    rezervari.add(rezervare);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        return (Rezervare) rezervari;
    }

    @Override
    public Iterable<Rezervare> findAll() {
        List<Rezervare> rezervari = new ArrayList<>();
        try (PreparedStatement preStmt = dbUtils.getConnection().prepareStatement("select * from Rezervari1")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int idClient = result.getInt("idClient");
                    int idExcursie = result.getInt("idExcursie");
                    int nrBilete = result.getInt("nrBilete");
                    Client client = clientDBRepository.findOne(idClient);
                    Excursie excursie = excursieDBRepository.findOne(idExcursie);
                    Rezervare rezervare = new Rezervare(id, client, excursie, nrBilete);
                    rezervari.add(rezervare);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        return rezervari;
    }

    @Override
    public int size() {
        List<Rezervare> rezervari = (List<Rezervare>) findAll();
        return rezervari.size();
    }
}

