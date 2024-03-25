package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Utilizator;
import ro.mpp2024.Repository.Interface.IUtilizatorRepository;

import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UtilizatorDBRepository implements IUtilizatorRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public UtilizatorDBRepository(Properties props){
        logger.info("Initializing UtilizatorDBRepository with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(Utilizator elem) {

        logger.traceEntry("saving utilizator {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Utilizatori values (?,?)")) {
            preStmt.setString(1, elem.getUsername());
            preStmt.setString(2, elem.getPassword());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Utilizator elem)
    {
        logger.traceEntry("updating utilizator {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Utilizatori set username=?, password=? where id=?")) {
            preStmt.setString(1, elem.getUsername());
            preStmt.setString(2, elem.getPassword());
            preStmt.setInt(3, elem.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting utilizator with {}", integer);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Utilizatori where id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();

    }

    @Override
    public Utilizator findOne(Integer integer) {
        logger.traceEntry("finding utilizator with id {} ", integer);
        Connection con = dbUtils.getConnection();
        Utilizator utilizator = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizatori where id=?")) {
            preStmt.setInt(1, integer);
            try (java.sql.ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    utilizator = new Utilizator(username, password);
                    utilizator.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(utilizator);
        return utilizator;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        logger.traceEntry("finding all utilizatori");
        Connection con = dbUtils.getConnection();
        List<Utilizator> utilizatori= new ArrayList<>();
        try(java.sql.PreparedStatement preStmt=con.prepareStatement("select * from Utilizatori")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Utilizator utilizator = new Utilizator(username, password);
                    utilizator.setId(id);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(utilizatori);
        return utilizatori;

    }

    @Override
    public int size() {
        return 0;
    }
}
