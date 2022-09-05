package me.nmt;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("10.16.1.1");
            PcapNetworkInterface nif = Pcaps.getDevByAddress(address);

            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;

            PcapHandle handle = nif.openLive(65536, mode, 10);
            Packet packet = handle.getNextPacketEx();
            handle.close();


            ArpPacket arpPacket = packet.get(ArpPacket.class);

            ArpPacket.Builder sendPacket = new ArpPacket.Builder();
            sendPacket
                    .dstProtocolAddr(address)
                    .dstHardwareAddr(MacAddress.getByName("FF-FF-FF-FF-FF-FF", "-"));


            handle.sendPacket(sendPacket.build());

            String mac = arpPacket.getHeader().getDstHardwareAddr().toString();

            System.out.println(mac);

            if (address.isReachable(5)) {
                System.out.println("Reachable");
            } else {
                System.out.println("NO!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}