package lk.ijse.pos.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private  static  DBConnection dbConnection;
    BasicDataSource dataSource;
    private DBConnection() throws SQLException {
        dataSource=new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/rebuilddatabase");
        dataSource.setPassword("1234");
        dataSource.setUsername("root");
        dataSource.setMaxTotal(5);
        dataSource.setInitialSize(5);
        Connection connection=dataSource.getConnection();
    }

    public static DBConnection getDbConnection() throws SQLException {
        if(dbConnection==null){
            dbConnection=new DBConnection();
        }
        return dbConnection;
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
