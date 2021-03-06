package de.unidue.inf.is.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.ibm.db2.jcc.DB2Driver;

import de.unidue.inf.is.domain.Babble;



public final class DBUtil {

    private DBUtil() {
    }


    static {
        com.ibm.db2.jcc.DB2Driver driver = new DB2Driver();
        try {
            DriverManager.registerDriver(driver);
        }
        catch (SQLException e) {
            throw new Error("Laden des Datenbanktreiber nicht möglich");
        }
    }

    public static PreparedStatement prepareSQL(Connection myConnection, String commandSQL) throws SQLException {
    	return myConnection.prepareStatement(commandSQL);
    }
    
    
    public static Connection getConnection(String database) throws SQLException {
        final String url = "jdbc:db2:" + database;
        return DriverManager.getConnection(url);
    }


    public static Connection getExternalConnection(String database) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("securityMechanism",
                        Integer.toString(com.ibm.db2.jcc.DB2BaseDataSource.USER_ONLY_SECURITY));
        properties.setProperty("user", "dbp18");
        properties.setProperty("password", "pasho7ci");

        final String url = "jdbc:db2://phoebe.is.inf.uni-due.de:50005/" + database + ":currentSchema=<dbp18>;";
        System.out.println(url);
        Connection connection = DriverManager.getConnection(url, properties);
        return connection;
    }


    public static boolean checkDatabaseExistsExternal(String database) {
        // Nur für Demozwecke!
        boolean exists = false;

        try (Connection connection = getExternalConnection(database)) {
            exists = true;
        }
        catch (SQLException e) {
            exists = false;
            e.printStackTrace();
        }

        return exists;
    }


    public static boolean checkDatabaseExists(String database) {
        // Nur für Demozwecke!
        boolean exists = false;

        try (Connection connection = getConnection(database)) {
            exists = true;
        }
        catch (SQLException e) {
            exists = false;
            e.printStackTrace();
        }

        return exists;
    }

}
