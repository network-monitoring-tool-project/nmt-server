package nmt.backend;

public class ConnectionSettings {

    // region Fields

    private String api = "";
    private String databaseName = "";
    private String ipAddress = "";
    private String port = "";
    private String userName = "";
    private String password = "";

    // endregion

    // region Constructor

    public ConnectionSettings(){

    }

    // endregion

    // region Properties

    public String getApi(){
        return this.api;
    }

    public String getDatabaseName(){
        return this.databaseName;
    }

    public String getIpAddress(){
        return this.ipAddress;
    }

    public String getPort(){
        return this.port;
    }

    public String getUsername(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }

    // endregion

    // region Static

    public ConnectionSettings GetConnectionSettings(){

        //TODO: aus Config-File parsen

        this.api = "jdbc";
        this.databaseName = "nmt_web";
        this.port = "3306";
        this.ipAddress = "127.0.0.1";

        this.userName = "root";
        this.password = "root";

        return this;
    }

    public String GetConnectionString(){
        if (this.api == "" || this.databaseName == "" || this.port == "" || this.ipAddress == "" || this.userName == ""){
            return "";
        }

        StringBuilder sb = new StringBuilder()
                .append(this.api).append(":").append("mysql").append("://")
                .append(this.ipAddress).append(":")
                .append(this.port).append("/")
                .append(this.databaseName);

        return sb.toString();
    }

    // endregion
}

