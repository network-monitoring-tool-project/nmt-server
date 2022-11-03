package nmt.backend.networkscanner;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NmtNetworkScanner {

    //region Static

    public static ArpPacket GetHost(String ip) throws UnknownHostException, SocketException, PcapNativeException {

        //TODO: dynamischer machen

        //region Vars

        ArpPacket arp = null;
        InetAddress localAddress = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localAddress);
        MacAddress localMac = MacAddress.getByAddress(ni.getHardwareAddress());
        PcapNetworkInterface nif = Pcaps.getDevByAddress(localAddress);
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10000);

        //endregion

        //region Core

        try {

            //region Getting IP-Address

            System.out.println("Detected local IPv4: " + localAddress);
            System.out.println("Detected local MAC address: " + localMac);

            //Scanner scanner = new Scanner(System.in);
            //System.out.println("Please type in: IPv4 address!");
            //String ipString = scanner.next();

            //endregion

            //region Check

            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(50)) {
                System.out.println(ip + " is reachable.");
            } else {
                System.out.println(ip + " is not reachable!");
                return null;
            }

            //endregion

            //region ARP

            ArpPacket.Builder arpBuilder = new ArpPacket.Builder()
                    .dstProtocolAddr(address)
                    .dstHardwareAddr(MacAddress.getByName("00-00-00-00-00-00", "-"))
                    .srcProtocolAddr(localAddress)
                    .srcHardwareAddr(localMac)
                    .protocolType(EtherType.IPV4)
                    .hardwareType(ArpHardwareType.ETHERNET)
                    .operation(ArpOperation.REQUEST)
                    .hardwareAddrLength((byte)MacAddress.SIZE_IN_BYTES)
                    .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES);

            //endregion

            //region Ethernet

            Packet packet = new EthernetPacket.Builder()
                    .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                    .srcAddr(localMac)
                    .type(EtherType.ARP)
                    .payloadBuilder(arpBuilder)
                    .paddingAtBuild(true)
                    .build();

            //endregion

            //region Send and Receive

            handle.sendPacket(packet);
            arp = (ArpPacket) searchPacket(handle, address);

            //endregion

            //region Result

            String mac = arp != null ? arp.getHeader().getSrcHardwareAddr().toString() : " / ";
            System.out.println("MAC of received ARP: " + mac);
            System.out.println("ARP: " + arp);

            //endregion

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (handle.isOpen()){
                handle.close();
            }
        }

        return arp;

        //endregion
    }

    //endregion

    //region Helper

    private static Packet searchPacket(PcapHandle handle, InetAddress address) {
        try {
            int count = 0;

            while (count < 1000) {
                count++;
                Packet packet = handle.getNextPacketEx();
                if (packet.getPayload() instanceof ArpPacket) {
                    System.out.println("ArpPacket received!");
                    ArpPacket arp = packet.get(ArpPacket.class);
                    if (arp.getHeader().getOperation().equals(ArpOperation.REPLY) && arp.getHeader().getSrcProtocolAddr().equals(address)) {
                        return arp;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Loop counter exceeded, no ArpPacket received!");
        return null;
    }

    //endregion
}
