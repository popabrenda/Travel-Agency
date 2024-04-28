package org.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Interfaces.RepositoryFirmaTransport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBFirmaTransport implements RepositoryFirmaTransport {

    private static final Logger logger = LogManager.getLogger();

    private UtilsDB dbUtils;
    public RepositoryDBFirmaTransport(Properties properties) {
        logger.info("Initializing RepositoryFirmaTransport with properties: {} ", properties);
        dbUtils = new UtilsDB(properties);
    }

    @Override
    public Optional<FirmaTransport> save(FirmaTransport entity) {
        logger.traceEntry("saving firma transport {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into FirmeTransport(NUME) values (?)" , PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getNume());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            logger.traceExit();
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
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
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FirmaTransport> update(FirmaTransport entity) {
        logger.traceEntry("updating firma transport {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update FirmeTransport set NUME=? where id=?")) {
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
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
    public Optional<FirmaTransport> delete(Integer idEntity) {
        logger.traceEntry("deleting firma transport with {}", idEntity);
        Connection con = dbUtils.getConnection();
        var firmaTransport = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from FirmeTransport where id=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            logger.traceExit();
            return firmaTransport;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FirmaTransport> findOne(Integer idEntity) {
        logger.traceEntry("finding firma transport with id {} ", idEntity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from FirmeTransport where id=?")) {
            preStmt.setInt(1, idEntity);
            try (var result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("NUME");
                    FirmaTransport firmaTransport = new FirmaTransport(nume);
                    firmaTransport.setId(id);
                    logger.traceExit(firmaTransport);
                    return Optional.of(firmaTransport);
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
    public List<FirmaTransport> findAll() {
        logger.traceEntry("finding all firma transport");
        Connection con = dbUtils.getConnection();
        List<FirmaTransport> firmeTransport = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from FirmeTransport")) {
            try (var result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("NUME");
                    FirmaTransport firmaTransport = new FirmaTransport(nume);
                    firmaTransport.setId(id);
                    firmeTransport.add(firmaTransport);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
        return firmeTransport;
    }
}
