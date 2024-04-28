package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Interfaces.RepositoryExcursie;
import org.example.Interfaces.RepositoryFirmaTransport;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBExcursie implements RepositoryExcursie {
    private static final Logger logger = LogManager.getLogger();
    private RepositoryFirmaTransport repositoryFirmaTransport;
    private UtilsDB dbUtils;

    public RepositoryDBExcursie(Properties props, RepositoryFirmaTransport repositoryFirmaTransport) {
        logger.info("Initializing RepositoryExcursie with properties: {} ", props);
        dbUtils = new UtilsDB(props);
        this.repositoryFirmaTransport = repositoryFirmaTransport;
    }

    @Override
    public Optional<Excursie> save(Excursie entity) {
        logger.traceEntry("saving excursie {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("insert into Excursii(OBIECTIV_TURISTIC, ID_FIRMA_TRANSPORT, ORA_PLECARII, PRET, NR_LOCURI) values (?,?,?,?,?)",  PreparedStatement.RETURN_GENERATED_KEYS)){
            preStmt.setString(1, entity.getObiectivTuristic());
            preStmt.setInt(2, entity.getFirmaTransport().getId());
            preStmt.setTime(3, Time.valueOf(entity.getOraPlecarii()));
            preStmt.setDouble(4, entity.getPret());
            preStmt.setInt(5, entity.getNumarLocuriTotale());
            int result = preStmt.executeUpdate();
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            logger.trace("Saved {} instances", result);
            logger.traceExit();
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Excursie> update(Excursie entity) {
        logger.traceEntry("updating excursie {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("update Excursii set OBIECTIV_TURISTIC=?, ID_FIRMA_TRANSPORT=?, ORA_PLECARII=?, PRET=?, NR_LOCURI=? where id=?")){
            preStm.setString(1, entity.getObiectivTuristic());
            preStm.setInt(2, entity.getFirmaTransport().getId());
            preStm.setTime(3, Time.valueOf(entity.getOraPlecarii()));
            preStm.setDouble(4, entity.getPret());
            preStm.setInt(5, entity.getNumarLocuriTotale());
            preStm.setInt(6, entity.getId());
            int result = preStm.executeUpdate();
            logger.trace("Updated {} instances", result);
            logger.traceExit();
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Excursie> delete(Integer idEntity) {
        logger.traceEntry("deleting excursie with {}", idEntity);
        Connection con = dbUtils.getConnection();
        var excursie = findOne(idEntity);
        try(PreparedStatement preStmt = con.prepareStatement("delete from Excursii where id=?")){
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            logger.traceExit();
            return excursie;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Excursie> findOne(Integer idEntity) {
        logger.traceEntry("finding excursie with id {} ", idEntity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Excursii where id=?")){
            preStmt.setInt(1, idEntity);
            try(var result = preStmt.executeQuery()){
                if(result.next()){
                    String obiectivTuristic = result.getString("OBIECTIV_TURISTIC");
                    Integer idFirmaTransport = result.getInt("ID_FIRMA_TRANSPORT");
                    var oraPlecarii = result.getString("ORA_PLECARII");
                    Double pret = result.getDouble("PRET");
                    Integer numarLocuriDisponibile = result.getInt("NR_LOCURI");
                    var firmaTransport = repositoryFirmaTransport.findOne(idFirmaTransport).get();
                    Excursie excursie = new Excursie(obiectivTuristic, firmaTransport, LocalTime.parse(oraPlecarii), pret, numarLocuriDisponibile);
                    excursie.setId(idEntity);
                    logger.traceExit(excursie);
                    return Optional.of(excursie);
                }
                else{
                    logger.traceExit();
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Excursie> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Excursie> excursii = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Excursii")){
            try(var result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String obiectivTuristic = result.getString("OBIECTIV_TURISTIC");
                    Integer idFirmaTransport = result.getInt("ID_FIRMA_TRANSPORT");
                    var oraPlecarii = result.getString("ORA_PLECARII");
                    Double pret = result.getDouble("PRET");
                    Integer numarLocuriDisponibile = result.getInt("NR_LOCURI");
                    var firmaTransport = repositoryFirmaTransport.findOne(idFirmaTransport).get();
                    Excursie excursie = new Excursie(obiectivTuristic, firmaTransport, LocalTime.parse(oraPlecarii), pret, numarLocuriDisponibile);
                    excursie.setId(id);
                    excursii.add(excursie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(excursii);
        return excursii;
    }

    @Override
    public List<Excursie> findByFilter(String obiectiv, LocalTime deLa, LocalTime panaLa) {
        logger.traceEntry("finding excursii with filter {} {} {}", obiectiv, deLa, panaLa);
        Connection con = dbUtils.getConnection();
        List<Excursie> excursii = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Excursii where OBIECTIV_TURISTIC like ? and ORA_PLECARII between ? and ?")){
            preStmt.setString(1, '%' + obiectiv + '%');
            preStmt.setString(2, deLa.toString());
            preStmt.setString(3, panaLa.toString());
            try(var result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String obiectivTuristic = result.getString("OBIECTIV_TURISTIC");
                    Integer idFirmaTransport = result.getInt("ID_FIRMA_TRANSPORT");
                    var oraPlecarii = result.getString("ORA_PLECARII");
                    Double pret = result.getDouble("PRET");
                    Integer numarLocuriDisponibile = result.getInt("NR_LOCURI");
                    var firmaTransport = repositoryFirmaTransport.findOne(idFirmaTransport).get();
                    Excursie excursie = new Excursie(obiectivTuristic, firmaTransport, LocalTime.parse(oraPlecarii), pret, numarLocuriDisponibile);
                    excursie.setId(id);
                    excursii.add(excursie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(excursii);
        return excursii;
    }
}
