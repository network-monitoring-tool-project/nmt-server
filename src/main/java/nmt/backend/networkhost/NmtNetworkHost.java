package nmt.backend.networkhost;

import nmt.backend.CRUDAble;
import nmt.backend.networkinterface.NmtNetworkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

//NetworkHostHistory (im Web) mit Statuswechsel (beispielsweise gestern zw. 11 - 12 Uhr down)
public class NmtNetworkHost extends CRUDAble implements Serializable {
    final static Logger log = LoggerFactory.getLogger(NmtNetworkHost.class);

    //region Fields

    private NmtNetworkInterface networkInterface;

    // depends on the interface that sent the request
    private boolean isReachable;

    //endregion

    //region Constructor

    public NmtNetworkHost(NmtNetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    //endregion

    //region Properties

    public NmtNetworkInterface getNetworkInterface() {
        return this.networkInterface;
    }

    public NmtNetworkHost setNetworkInterface(NmtNetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
        return this;
    }

    public boolean getIsReachable() {
        return this.isReachable;
    }

    public NmtNetworkHost setIsReachable(boolean isReachable) {
        this.isReachable = isReachable;
        return this;
    }

    // endregion

    // region Override

    @Override
    public boolean create() {

        String sqlQuery = "INSERT INTO addresses(id, mac, ip_v4, ip_v6, scan_id, online, created_at)" +
                "VALUES('@id', '@mac', '@ip_v4', '@ip_v6', '@scan_id', '@online', '@created_at')";

        try {
            sqlQuery = sqlQuery.replace("@id", getNextId());
            sqlQuery = sqlQuery.replace("@id", this.networkInterface.getMac());
            sqlQuery = sqlQuery.replace("@ip_v4", this.networkInterface.getAddresses().get(1));
            sqlQuery = sqlQuery.replace("@ip_v6", this.networkInterface.getAddresses().get(0));
            sqlQuery = sqlQuery.replace("@scan_id", "9999");
            sqlQuery = sqlQuery.replace("@online", "1");
            sqlQuery = sqlQuery.replace("@created_at", LocalDateTime.now().toString());

            getConnection().createStatement().executeQuery(sqlQuery);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean createOrUpdate() throws SQLException {
        if (!update()){
            create();
        }

        return true;
    }

    @Override
    public boolean read() {
        return true;
    }

    @Override
    public boolean update() {
        String sqlQuery = String.format("SELECT * FROM addresses WHERE mac = '%s'", this.networkInterface.getMac());

        try {
            Statement statement = getConnection().createStatement();
            final ResultSet result = statement.executeQuery(sqlQuery);

            if (result.first()) {
                final String values = String.format("ip_v4 = %s, ip_v6 = %s, scan_id = %s, online = %s, created_at = %s",
                        this.networkInterface.getAddresses().get(1),
                        this.networkInterface.getAddresses().get(0),
                        "9999",
                        "1",
                        LocalDateTime.now()
                );
                sqlQuery = String.format("UPDATE addresses SET %s WHERE mac = '%s'", values, this.networkInterface.getMac());

                final ResultSet update = statement.executeQuery(sqlQuery);
                if (update.first()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete() {
        return false;
    }

    //endregion

    // region Helper

    public String getNextId() throws SQLException {

        String sqlQuery = "select MAX(id) from addresses";
        int nextId = -1;

        Statement statement = getConnection().createStatement();
        ResultSet result = statement.executeQuery(sqlQuery);

        if (result.first()) {
            nextId = result.getInt("id");
        }

        return String.valueOf(++nextId);
    }

    // endregion

}