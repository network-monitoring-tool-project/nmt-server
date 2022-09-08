package me.nmt;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localAddress);
            MacAddress localMac = MacAddress.getByAddress(ni.getHardwareAddress());
            System.out.println("Detected local IPv4: " + localAddress);
            System.out.println("Detected local MAC address: " + localMac);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Please type in: IPv4 address!");
            String ipString = scanner.next();

            InetAddress address = InetAddress.getByName(ipString);
            if (address.isReachable(50)) {
                System.out.println(ipString + " is reachable.");
            } else {
                System.out.println(ipString + " is not reachable!");
                return;
            }


            PcapNetworkInterface nif = Pcaps.getDevByAddress(localAddress);

            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;

            PcapHandle handle = nif.openLive(65536, mode, 10000);

            ArpPacket.Builder sendPacket = new ArpPacket.Builder();
            sendPacket
                    .dstProtocolAddr(address)
                    .dstHardwareAddr(MacAddress.getByName("FF-FF-FF-FF-FF-FF", "-"))
                    .srcProtocolAddr(localAddress)
                    .srcHardwareAddr(localMac)
                    .protocolType(EtherType.ARP)
                    .hardwareType(ArpHardwareType.ETHERNET)
                    .operation(ArpOperation.REQUEST);

            handle.sendPacket(sendPacket.build());

            ArpPacket arp = (ArpPacket) searchPacket(handle, address);

            handle.close();

            String mac = arp != null ? arp.getHeader().getSrcHardwareAddr().toString() : " / ";

            System.out.println("MAC of received ARP: " + mac);
            System.out.println("ARP: " + arp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Packet searchPacket(PcapHandle handle, InetAddress address) {
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

}