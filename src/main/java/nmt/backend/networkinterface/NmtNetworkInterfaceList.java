package nmt.backend.networkinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class NmtNetworkInterfaceList extends AbstractList<NmtNetworkInterface> implements Serializable {
    final static Logger log = LoggerFactory.getLogger(NmtNetworkInterfaceList.class);

    //region Fields

    private static final List<NmtNetworkInterface> elements = new ArrayList<>();
    private static int idCounter = 0;

    //endregion

    //region Constructor

    /* no parameterless constructor provided to control instantiation */
    private NmtNetworkInterfaceList() {

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
    public NmtNetworkInterface set(int index, NmtNetworkInterface inf) {
        return elements.set(index, inf);
    }

    @Override
    public void add(int index, NmtNetworkInterface inf) {
        elements.add(index, inf);
    }

    //endregion

    //region Static
    public static List<NmtNetworkInterface> loadNetworkInterfaces() throws SocketException {

        NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(inf -> {
            try {
                NmtNetworkInterface nmtNetInterface = new NmtNetworkInterface()
                        .setId(idCounter)
                        .setName(inf.getName())
                        .setActiveState(inf.isUp())
                        .setMac(inf.getHardwareAddress())
                        .setAddresses(inf.getInterfaceAddresses().stream()
                                .map(x -> x.getAddress().toString())
                                .toList());

                elements.add(idCounter, nmtNetInterface);
            } catch (SocketException e) {
                log.warn("SocketException while loading network interfaces...", e);
            }
        });

        idCounter++;

        return elements;
    }

    //endregion


}
