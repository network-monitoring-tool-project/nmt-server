package nmt.backend.networkinterface;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class NmtNetworkInterface implements Serializable {

    //region Fields

    private int id = 0;
    private String description = "";
    private String name = "";
    private String mac = "";

    private List<String> addresses = new ArrayList<>();

    private boolean isActive = false;
    private boolean isSelected = false;

    // endregion

    //region Constructor

    public NmtNetworkInterface(){
    }

    public NmtNetworkInterface(int id){
        this.id = id;
    }

    //endregion

    //region Properties

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int geId(){
        return this.id;
    }

    public NmtNetworkInterface setId(int id){
        this.id = id;
        return this;
    }

    public String getName(){
        return this.name;
    }

    public NmtNetworkInterface setName(String name){
        this.name = name;
        return this;
    }

    public String getMac(){
        return this.mac;
    }

    public NmtNetworkInterface setMac(byte[] nativeAddress){

        if (nativeAddress == null){
            this.mac = "not found";
            return this;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nativeAddress.length; i++) {
            sb.append(String.format("%02X%s", nativeAddress[i], (i < nativeAddress.length - 1) ? "-" : ""));
        }

        this.mac = sb.toString();
        return this;
    }

    public NmtNetworkInterface setMac(String mac){
        this.mac = mac;
        return this;
    }

    public List<String> getAddresses(){
        return this.addresses;
    }

    public NmtNetworkInterface setAddresses(List<String> addresses){
        this.addresses = addresses;
        return this;
    }

    public boolean getActiveState(){
        return this.isActive;
    }

    public NmtNetworkInterface setActiveState(boolean isActive){
        this.isActive = isActive;
        return this;
    }

    public NmtNetworkInterface setSelectedState(boolean isSelected){
        this.isSelected = isSelected;
        return this;
    }

    public boolean getSelectedState(){
        return this.isSelected;
    }

    //endregion
}