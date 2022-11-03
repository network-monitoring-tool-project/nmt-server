package nmt.backend.networkinterface;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;

public class NmtNetworkInterfaceList extends AbstractList<NmtNetworkInterface> implements Serializable {

    //region Fields

    private static final List<NmtNetworkInterface> elements = new ArrayList<>();
    private static int idCounter = 0;

    //endregion

    //region Constructor

    /* no parameterless constructor provided to control instantiation */
    private NmtNetworkInterfaceList(){

    }

    //endregion

    //region Properties

    @Override
    public int size() {
        return elements.size();
    }

    //endregion

    //region Methods

    @Override
    public NmtNetworkInterface get(int index) {
        return elements.get(index);
    }

    @Override
    public NmtNetworkInterface set(int index, NmtNetworkInterface inf){
        return elements.set(index, inf);
    }

    @Override
    public void add(int index, NmtNetworkInterface inf){
        elements.add(index, inf);
    }

    //endregion

    //region Static
    public static List<NmtNetworkInterface> LoadNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();

        for (NetworkInterface netInterface : Collections.list(netInterfaces)){

            NmtNetworkInterface nmtNetInterface = new NmtNetworkInterface()
                    .setId(idCounter)
                    .setName(netInterface.getName())
                    .setActiveState(netInterface.isUp())
                    .setMac(netInterface.getHardwareAddress())
                    .setAddresses((ArrayList<String>) netInterface.getInterfaceAddresses().stream().map(x->x.getAddress().toString()).collect(Collectors.toList()));

            elements.add(idCounter, nmtNetInterface);
        }

        idCounter++;

        return elements;
    }

    //endregion









}
