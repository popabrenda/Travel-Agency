package org.example.DBRepositoy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class UtilsDB {
    private Properties dbProps;
    private static final Logger logger = LogManager.getLogger();

    private Connection instance=null;

    public UtilsDB(Properties props){
        dbProps=props;
    }

    private Connection getNewConnection(){
        logger.traceEntry();

        String url=dbProps.getProperty("jdbc.url");
        String user=dbProps.getProperty("jdbc.user");
        String pass=dbProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con=null;
        try {
            if (user!=null && pass!=null)
                con= java.sql.DriverManager.getConnection(url,user,pass);
            else
                con= java.sql.DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }

}
