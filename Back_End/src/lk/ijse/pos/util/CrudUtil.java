package lk.ijse.pos.util;


import lk.ijse.pos.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getDbConnection().getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstm.setObject((i + 1), args[i]);
            }
            if (sql.startsWith("SELECT") || sql.startsWith("select")) {
                ResultSet resultSet = pstm.executeQuery();
                return (T) resultSet;
            } else {
                int rowsAffected = pstm.executeUpdate();
                return (T) (Boolean) (rowsAffected > 0);
            }
        }
    }
}
