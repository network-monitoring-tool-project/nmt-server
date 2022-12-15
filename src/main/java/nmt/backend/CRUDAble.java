package nmt.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class CRUDAble {

    // region CRUD
    public abstract boolean create();
    public abstract boolean read();
    public abstract boolean update();
    public abstract boolean delete();

    // endregion

    // region Connection

    public Connection getConnection() throws SQLException {

        // region Vars

        ConnectionSettings conSettings = ConnectionSettings.getConnectionSettings();

        Connection con = DriverManager.getConnection(conSettings.getConnectionString(), conSettings.getUsername(), conSettings.getPassword());

        // endregion

        return con;
    }

    // endregion
}