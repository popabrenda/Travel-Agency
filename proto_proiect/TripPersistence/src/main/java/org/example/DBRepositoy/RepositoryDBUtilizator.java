package org.example.DBRepositoy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Interfaces.RepositoryUtilizator;
import org.example.Utilizator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBUtilizator implements RepositoryUtilizator {
    private static final Logger logger = LogManager.getLogger();
    private UtilsDB dbUtils;

    public RepositoryDBUtilizator(Properties props) {
        logger.info("Initializing RepositoryUtilizator with properties: {} ", props);
        dbUtils = new UtilsDB(props);
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        logger.traceEntry("saving utilizator {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Utilizatori(NUME, USERNAME, PASSWORD) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getNume());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
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
    public Optional<Utilizator> update(Utilizator entity) {
        logger.traceEntry("updating utilizator {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Utilizatori set NUME=?, USERNAME=?, PASSWORD=? where id=?")) {
            preStmt.setString(1, entity.getNume());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            preStmt.setInt(4, entity.getId());
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
    public Optional<Utilizator> delete(Integer idEntity) {
        logger.traceEntry("deleting utilizator with {}", idEntity);
        Connection con = dbUtils.getConnection();
        var utilizator = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from Utilizatori where id=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            logger.traceExit();
            return utilizator;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> findOne(Integer idEntity) {
        logger.traceEntry("finding utilizator with id {} ", idEntity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizatori where id=?")) {
            preStmt.setInt(1, idEntity);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("NUME");
                    String username = result.getString("USERNAME");
                    String password = result.getString("PASSWORD");
                    Utilizator utilizator = new Utilizator(nume, username, password);
                    utilizator.setId(idEntity);
                    logger.traceExit(utilizator);
                    return Optional.of(utilizator);
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
    public List<Utilizator> findAll() {
        logger.traceEntry("finding all utilizatori");
        Connection con = dbUtils.getConnection();
        List<Utilizator> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizatori")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("NUME");
                    String username = result.getString("USERNAME");
                    String password = result.getString("PASSWORD");
                    Utilizator utilizator = new Utilizator(nume, username, password);
                    utilizator.setId(id);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(utilizatori);
        return utilizatori;
    }

    @Override
    public Optional<Utilizator> findUtilizatorByUsername(String username) {
        //System.out.println("RepositoryDBUtilizator/findUtilizatorByUsername " + username);
        logger.traceEntry("finding user by {}", username);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizatori where USERNAME=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("NUME");
                    String password = result.getString("PASSWORD");
                    Utilizator utilizator = new Utilizator(nume, username, password);
                    utilizator.setId(id);
                    logger.traceExit(utilizator);
                    return Optional.of(utilizator);
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
}
