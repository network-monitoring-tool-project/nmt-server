package nmt.backend;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectionSettings {
    final static Logger log = LoggerFactory.getLogger(ConnectionSettings.class);

    // region Fields
    private final static String CONF = "db.conf";
    private final static String API = "jdbc";
    private String databaseName = "";
    private String ipAddress = "";
    private String port = "";
    private String userName = "";
    private String password = "";

    // endregion

    // region Constructor

    public ConnectionSettings() {

    }

    // endregion

    // region Properties

    public static String getApi() {
        return API;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getPort() {
        return this.port;
    }

    public String getUsername() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    // endregion

    // region Static

    public static ConnectionSettings getConnectionSettings() {
        ConnectionSettings settings = new ConnectionSettings();

        settings.databaseName = "nmt_web";
        settings.port = "8080";
        settings.ipAddress = "127.0.0.1";
        settings.userName = "root";
        settings.password = "rootroot";

        Path conf = getConfFile();
        if (conf != null) {
            applySettings(readConfig(conf), settings);
        }

        return settings;
    }

    private static Map<String, String> readConfig(final Path file) {
        Map<String, String> settings = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.lines().forEach(line -> {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    settings.put(parts[0], parts[1]);
                }
            });
        } catch (Exception e) {
            log.error(String.format("Failed to read from configuration file: %s (%s)", e.getMessage(), e.getClass().getSimpleName()));
            return Collections.emptyMap();
        }
        return settings;
    }

    private static void applySettings(final Map<String, String> settings, final ConnectionSettings conSettings) {
        settings.forEach((key, value) -> {
            if (Objects.equals("database-name", key)) {
                conSettings.databaseName = value;
            }
            if (Objects.equals("ip-address", key)) {
                conSettings.ipAddress = key;
            }
            if (Objects.equals("port", key)) {
                conSettings.port = key;
            }
            if (Objects.equals("username", key)) {
                conSettings.userName = value;
            }
            if (Objects.equals("password", key)) {
                conSettings.password = value;
            }
        });
    }

    public static Path getConfFile() {
        try {
            return Paths.get(ClassLoader.getSystemResource(CONF).toURI());
        } catch (Exception e) {
            log.error(String.format("Failed to get configuration file: %s (%s)", e.getMessage(), e.getClass().getSimpleName()));
            return null;
        }
    }

    public String getConnectionString() {
        if (Objects.equals(this.databaseName, "") || Objects.equals(this.port, "") || Objects.equals(this.ipAddress, "") || Objects.equals(this.userName, "")) {
            return Strings.EMPTY;
        }

        return String.format("%s:mysql://%s:%s/%s", getApi(), this.getIpAddress(), this.getPort(), this.getDatabaseName());
    }

    // endregion
}

