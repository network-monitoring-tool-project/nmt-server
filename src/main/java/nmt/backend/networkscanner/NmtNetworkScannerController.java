package nmt.backend.networkscanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nmt.backend.networkhost.NmtNetworkHost;
import nmt.backend.networkinterface.NmtNetworkInterface;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.ArpPacket;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

@RestController
@EnableAutoConfiguration
public class NmtNetworkScannerController {

    @GetMapping("/api/host")
    public String GetHost(@RequestParam(required = true, defaultValue = "192.168.178.1") String ip) throws SocketException, UnknownHostException, PcapNativeException, JsonProcessingException {

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


}
