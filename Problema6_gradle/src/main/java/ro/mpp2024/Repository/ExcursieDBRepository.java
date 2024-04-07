package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Repository.Interface.IExcursieRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExcursieDBRepository implements IExcursieRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public ExcursieDBRepository(Properties dbUtils){
        logger.info("Initializing ExcursieDBRepository with properties: {}",dbUtils);
        this.dbUtils=new JdbcUtils(dbUtils);
    }
    @Override
    public void add(Excursie elem) {
        logger.traceEntry("saving excursie {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Excursii(id,obiectiv, firmaTransport, oraPlecare, pret, locuriDisponibile) values (?,?,?,?,?,?)")) {
            preStmt.setInt(1, elem.getId());
            preStmt.setString(2, elem.getObiectiv());
            preStmt.setString(3, elem.getFirmaTransport());
            preStmt.setInt(4, elem.getOraPlecare());
            preStmt.setDouble(5, elem.getPret());
            preStmt.setInt(6, elem.getLocuriDisponibile());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();

    }

        @Override
    public void update(Integer id, Excursie elem) {
        logger.traceEntry("updating excursie {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Excursii set locuriDisponibile=? where id=?")) {
            preStmt.setInt(6, elem.getLocuriDisponibile());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();
    }

    public void delete(Integer id) {
        logger.traceEntry("deleting excursie with {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Excursii where id=?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit();
    }


    @Override
    public Iterable<Excursie> findAll() {
        logger.traceEntry("finding all excursii");
        Connection con = dbUtils.getConnection();
        List<Excursie> excursii = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Excursii")) {
            try (java.sql.ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String obiectiv = result.getString("obiectiv");
                    String firmaTransport = result.getString("firmaTransport");
                    int oraPlecare = result.getInt("oraPlecare");
                    int locuriDisponibile = result.getInt("locuriDisponibile");
                    int pret = result.getInt("pret");
                    Excursie excursie = new Excursie(obiectiv, firmaTransport, oraPlecare, pret,locuriDisponibile);
                    excursie.setId(id);
                    excursii.add(excursie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(excursii);
        return excursii;
    }

    @Override
    public Excursie findOne(Integer id) {
        logger.traceEntry("finding excursie with id {} ", id);
        Connection con = dbUtils.getConnection();
        Excursie excursie = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Excursii where id=?")) {
            preStmt.setInt(1, id);
            try (var result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id1 = result.getInt("id");
                    String obiectiv = result.getString("obiectiv");
                    String firmaTransport = result.getString("firmaTransport");
                    int oraPlecare = result.getInt("oraPlecare");
                    int locuriDisponibile = result.getInt("locuriDisponibile");
                    int pret = result.getInt("pret");
                    logger.trace("Found excursie with id {} obiectiv {} firmaTransport {} oraPlecare {} locuriDisponibile {} pret {}", id1, obiectiv, firmaTransport, oraPlecare, locuriDisponibile, pret);
                    excursie = new Excursie(obiectiv, firmaTransport, oraPlecare, locuriDisponibile, pret);
                    excursie.setId(id1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }

        logger.traceExit(excursie);
        return excursie;
    }

    public int size() {
        return 0;
    }

}
