package org.example.DBRepositoy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Interfaces.RepositoryExcursie;
import org.example.Interfaces.RepositoryRezervare;
import org.example.Rezervare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBRezervare implements RepositoryRezervare {

    private static final Logger logger = LogManager.getLogger();
    private RepositoryExcursie repositoryExcursie;
    private UtilsDB dbUtils;

    public RepositoryDBRezervare(Properties properties, RepositoryExcursie repositoryExcursie) {
        logger.info("Initializing RepositoryRezervare with properties: {} ", properties);
        this.repositoryExcursie = repositoryExcursie;
        dbUtils = new UtilsDB(properties);
    }
    @Override
    public Optional<Rezervare> save(Rezervare entity) {
        logger.traceEntry("saving rezervare {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("insert into Rezervari(ID_EXCURSIE, NUME_CLIENT, TELEFON_CLIENT, NR_BILETE) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            preStmt.setInt(1, entity.getExcursie().getId());
            preStmt.setString(2, entity.getNumeClient());
            preStmt.setString(3, entity.getTelefonClient());
            preStmt.setInt(4, entity.getNumarBilete());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            logger.traceExit();
            try (var generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rezervare> update(Rezervare entity) {
        logger.traceEntry("updating rezervare {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("update Rezervari set ID_EXCURSIE=?, NUME_CLIENT=?, TELEFON_CLIENT=?, NR_BILETE=? where id=?")){
            preStm.setInt(1, entity.getExcursie().getId());
            preStm.setString(2, entity.getNumeClient());
            preStm.setString(3, entity.getTelefonClient());
            preStm.setInt(4, entity.getNumarBilete());
            preStm.setInt(5, entity.getId());
            int result = preStm.executeUpdate();
            logger.trace("Updated {} instances", result);
            logger.traceExit();
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rezervare> delete(Integer idEntity) {
        logger.traceEntry("deleting rezervare with {}", idEntity);
        Connection con = dbUtils.getConnection();
        var rezervare = findOne(idEntity);
        try(PreparedStatement preStmt = con.prepareStatement("delete from Rezervari where id=?")){
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            logger.traceExit();
            return rezervare;
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Rezervare> findOne(Integer idEntity) {
        logger.traceEntry("finding rezervare with id {} ", idEntity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Rezervari where id=?")){
            preStmt.setInt(1, idEntity);
            try(var result = preStmt.executeQuery()){
                if(result.next()){
                    int id = result.getInt("id");
                    int idExcursie = result.getInt("ID_EXCURSIE");
                    String numeClient = result.getString("NUME_CLIENT");
                    String telefonClient = result.getString("TELEFON_CLIENT");
                    int nrBilete = result.getInt("NR_BILETE");
                    var excursie = repositoryExcursie.findOne(idExcursie).get();
                    Rezervare rezervare = new Rezervare(numeClient, telefonClient, excursie, nrBilete);
                    rezervare.setId(id);
                    logger.traceExit(rezervare);
                    return Optional.of(rezervare);
                }
                else{
                    logger.traceExit();
                    return Optional.empty();
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Rezervare> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Rezervare> rezervari = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Rezervari")){
            try(var result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    int idExcursie = result.getInt("ID_EXCURSIE");
                    String numeClient = result.getString("NUME_CLIENT");
                    String telefonClient = result.getString("TELEFON_CLIENT");
                    int nrBilete = result.getInt("NR_BILETE");
                    var excursie = repositoryExcursie.findOne(idExcursie).get();
                    Rezervare rezervare = new Rezervare(numeClient, telefonClient, excursie, nrBilete);
                    rezervare.setId(id);
                    rezervari.add(rezervare);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(rezervari);
        return rezervari;
    }

    @Override
    public Integer getNumarOcupate(Integer idExcursie) {
        logger.traceEntry("getting number of occupied seats for excursie with id {} ", idExcursie);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select sum(NR_BILETE) from Rezervari where ID_EXCURSIE=?")){
            preStmt.setInt(1, idExcursie);
            try(var result = preStmt.executeQuery()){
                if(result.next()){
                    int nrOcupate = result.getInt(1);
                    logger.traceExit(nrOcupate);
                    return nrOcupate;
                }
                else{
                    logger.traceExit(0);
                    return 0;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
