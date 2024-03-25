package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Client;
import ro.mpp2024.Repository.Interface.IClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientDBRepository implements IClientRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public ClientDBRepository(Properties props){
        logger.info("Initializing ClientDBRepository with properties: {}",props);
        dbUtils=new JdbcUtils(props);

    }
    @Override
    public void add(Client elem) {

            logger.traceEntry("saving client {} ", elem);
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("insert into Clienti(nume, nrTelefon) values (?,?)")) {
                preStmt.setString(1, elem.getNume());
                preStmt.setString(2, elem.getNrTelefon());
                int result = preStmt.executeUpdate();
                logger.trace("Saved {} instances", result);
            } catch (SQLException e) {
                logger.error(e);
                System.out.println("Error DB " + e);
            }
        logger.traceExit();


    }

    @Override
    public void update(Integer integer, Client elem) {

        logger.traceEntry("updating client {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Clienti set nume=?, nrTelefon=? where id=?")) {
            preStmt.setString(1, elem.getNume());
            preStmt.setString(2, elem.getNrTelefon());
            preStmt.setInt(3, elem.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting client with {}", integer);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Clienti where id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }

    }

    @Override
    public Client findOne(Integer integer) {
        logger.traceEntry("finding client with id {} ", integer);
        Connection con = dbUtils.getConnection();
        Client client = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Clienti where id=?")) {
            preStmt.setInt(1, integer);
            try (var result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String nrTelefon = result.getString("nrTelefon");
                    logger.trace("Found client with id {} nume {} nrTelefon {}", id, nume, nrTelefon);
                    client = new Client(id,nume, nrTelefon);
                    client.setId(id);
                    //System.out.println("repo: clientul gasit in functia find one" + client);

                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(client);
        return client;
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry("finding all clients");
        Connection con = dbUtils.getConnection();
        List<Client> clienti= new ArrayList<>();
        try(java.sql.PreparedStatement preStmt=con.prepareStatement("select * from Clienti")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String nrTelefon = result.getString("nrTelefon");
                    Client client = new Client(id, nume, nrTelefon);
                    client.setId(id);
                    clienti.add(client);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        //System.out.println("repo: clientii gasiti in functia find all" + clienti);
        logger.traceExit(clienti);
        return clienti;
    }

    @Override
    public int size() {
        return 0;
    }
}
