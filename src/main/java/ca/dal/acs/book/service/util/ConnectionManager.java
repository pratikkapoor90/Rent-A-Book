package ca.dal.acs.book.service.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
	public static void main(String[] args) {
		getConnection();
	}
	public static Connection getConnection(){
		
		// Connect to database
        String hostName = "cloud-a4.database.windows.net";
        String dbName = "web-project";
        String user = "shipra1101";
        String password = "Amazon@2018";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(url);
                String schema = connection.getSchema();
                System.out.println("Successful connection - Schema: " + schema);


	}
    catch (Exception e) {
            e.printStackTrace();
    }
		return connection;
}
}
