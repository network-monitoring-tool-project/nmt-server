package nmt.backend.networkhost;

import nmt.backend.networkinterface.NmtNetworkInterface;

import java.io.Serializable;

//NetworkHostHistory (im Web) mit Statuswechsel (beispielsweise gestern zw. 11 - 12 Uhr down)
public class NmtNetworkHost implements Serializable {

    //region Fields

    private NmtNetworkInterface networkInterface;

    // depends on the interface that sent the request
    private boolean isReachable;

    //endregion

    //region Constructor

    public NmtNetworkHost(NmtNetworkInterface networkInterface){
        this.networkInterface = networkInterface;
    }

    //endregion

    //region Properties

    public NmtNetworkInterface getNetworkInterface(){
        return this.networkInterface;
    }

    public NmtNetworkHost setNetworkInterface(NmtNetworkInterface networkInterface){
        this.networkInterface = networkInterface;
        return this;
    }

    public boolean getIsReachable(){
        return this.isReachable;
    }

    public NmtNetworkHost setIsReachable(boolean isReachable){
        this.isReachable = isReachable;
        return this;
    }

    //endregion
}