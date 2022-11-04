package nmt.backend.networkscanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nmt.backend.helper.Utils;
import nmt.backend.networkhost.NmtNetworkHost;
import nmt.backend.networkinterface.NmtNetworkInterface;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.ArpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class NmtNetworkScannerController {
    final static Logger log = LoggerFactory.getLogger(NmtNetworkScannerController.class);

    @GetMapping("/api/host")
    public String getHost(@RequestParam(required = true, defaultValue = "192.168.178.1") String ip) throws SocketException, UnknownHostException, PcapNativeException, JsonProcessingException {

        ArpPacket arp = NmtNetworkScanner.GetHost(ip);
        ArrayList<String> addresses = new ArrayList<>();
        assert arp != null;
        addresses.add(arp.getHeader().getSrcProtocolAddr().toString());

        NmtNetworkInterface netInterfaceHost = new NmtNetworkInterface()
                .setMac(arp.getHeader().getSrcHardwareAddr().getAddress())
                .setAddresses(addresses)
                .setActiveState(true);

        NmtNetworkHost netHost = new NmtNetworkHost(netInterfaceHost);
        netHost.setIsReachable(true);

        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return objWriter.writeValueAsString(netHost);
    }

    @GetMapping("/api/hosts")
    public String getHosts(@RequestParam(required = true, defaultValue = "10.19.224.0") String ip, @RequestParam(required = true, defaultValue = "24") int cidr) throws SocketException, UnknownHostException, PcapNativeException, JsonProcessingException {
        final List<NmtNetworkHost> hosts = new ArrayList<>();

        Utils.getAvailableAddresses(ip, cidr).forEach(address -> {
            try {
                ArpPacket arp = NmtNetworkScanner.GetHost(ip);
                ArrayList<String> addresses = new ArrayList<>();
                assert arp != null;
                addresses.add(arp.getHeader().getSrcProtocolAddr().toString());

                NmtNetworkInterface netInterfaceHost = new NmtNetworkInterface()
                        .setMac(arp.getHeader().getSrcHardwareAddr().getAddress())
                        .setAddresses(addresses)
                        .setActiveState(true);

                NmtNetworkHost netHost = new NmtNetworkHost(netInterfaceHost);
                netHost.setIsReachable(true);

                hosts.add(netHost);
            } catch (Exception e) {
                log.warn(e.getClass().getSimpleName() + ": ", e);
            }

        });

        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return objWriter.writeValueAsString(hosts);
    }
}
